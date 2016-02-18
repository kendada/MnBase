package cc.mnbase.dbdb.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2016-02-16
 * Time: 10:28
 * Version 1.0
 *
 * 根据反射
 */

public class DBManager {

    private MSQLiteHelper mHelper;

    private SQLiteDatabase db = null;

    private String dbName;

    private Context mContext;


    public DBManager(Context context, String dbName, int dbVersion, Class<?> clazz){
        mHelper = new MSQLiteHelper(context, dbName, dbVersion, clazz);
        db = mHelper.getWritableDatabase();
        mContext = context;
        this.dbName = dbName;
    }

    /**
     * 关闭数据库
     * */
    public void closeDB(){
        db.close();
        mHelper = null;
        db = null;
    }

    /**
     * 删除数据库
     * */
    public boolean deleteDB(){
        return mContext.deleteDatabase(dbName);
    }

    /**
     * 插入一条数据
     *
     * @param obj
     * @return 返回-1代表失败，其他成功
     * */
    public long insert(Object obj){
        Class<?> modeClass = obj.getClass();
        Field[] fields = modeClass.getDeclaredFields();

        ContentValues values = new ContentValues();

        for(Field fd : fields){
            fd.setAccessible(true);
            String fieldName = fd.getName();
            if(fieldName.equalsIgnoreCase("id") || fieldName.equalsIgnoreCase("_id")){
                continue;
            }
            putValues(values, fd, obj);
        }
        return db.insert(DBUtil.getTableName(modeClass), null, values);
    }

    /**
     * 查询表中所有数据
     * */
    public <T> List<T> findAllData(Class<?> clazz){
        Cursor cursor = db.query(clazz.getSimpleName(), null, null, null, null, null, null);
        return getEntity(cursor, clazz);
    }

    /**
     * 根据条件查询数据
     * */
    public <T> List<T> findByArgs(Class<?> clazz, String select, String[] selectArgs){
        Cursor cursor = db.query(clazz.getSimpleName(), null, select, selectArgs, null, null, null);
        return getEntity(cursor, clazz);
    }

    /**
     * 通过ID删除一条记录
     * @param clazz
     * @param id
     * */
    public long deteleById(Class<?> clazz, long id){
        return db.delete(DBUtil.getTableName(clazz), "id=" + id, null);
    }

    /**
     * 通过ID更新一条记录
     * @param clazz
     * @param id
     * @param values
     * */
    public long updateById(Class<?> clazz, ContentValues values, long id){
        return db.update(clazz.getSimpleName(), values, "id=" + id, null);
    }

    private <T> List<T> getEntity(Cursor cursor, Class<?> clazz){
        List<T> list = new ArrayList<>();
        try{
            if(cursor != null && cursor.moveToFirst()){
                do{
                    Field[] fields = clazz.getDeclaredFields();
                    T modeClass = (T) clazz.newInstance();
                    for(Field fd : fields){
                        Class<?> cursorClass = cursor.getClass();
                        String columnMethodName = getColumnMethodName(fd.getType());

                        Method cursorMethod = cursorClass.getMethod(columnMethodName, int.class);
                        Object value = cursorMethod.invoke(cursor, cursor.getColumnIndex(fd.getName()));

                        if(fd.getType() == boolean.class || fd.getType() == Boolean.class){
                            if("0".equals(String.valueOf(value))){
                                value = false;
                            } else if("1".equals(String.valueOf(value))){
                                value = true;
                            }
                        } else if(fd.getType() == char.class || fd.getType() == Character.class){
                            value = ((String)value).charAt(0);
                        } else if (fd.getType() == Date.class){
                            long date = (long)value;
                            if(date <= 0){
                                value = null;
                            } else {
                                value = new Date(date);
                            }
                        }
                        String methodName = makeSetterMethodName(fd);
                        Method method = clazz.getDeclaredMethod(methodName, fd.getType());
                        method.invoke(modeClass, value);
                    }
                    list.add(modeClass);
                } while (cursor.moveToNext());
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if(cursor != null){
                cursor.close();
            }
        }
        return list;
    }

    private void putValues(ContentValues values, Field fd, Object obj){
        Class<?> clazz = values.getClass();
        try {
            Object[] parameters = new Object[]{fd.getName(), fd.get(obj)};
            Class<?>[] parameterTypes = getParameterTypes(fd, fd.get(obj), parameters);

            Method method = clazz.getDeclaredMethod("put", parameterTypes);
            method.setAccessible(true);
            method.invoke(values, parameters);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private Class<?>[] getParameterTypes(Field field, Object fieldValue, Object[] parameters){
        Class<?>[] parameterTypes;
        if(isCharType(field)){
            parameters[1] = String.valueOf(fieldValue);
            parameterTypes = new Class[]{String.class, String.class};
        } else {
            if(field.getType().isPrimitive()){
                parameterTypes = new Class[]{String.class, getObjectType(field.getType())};
            } else if("java.util.Date".equals(field.getType().getName())){
                parameterTypes = new Class[]{String.class, Long.class};
            } else {
                parameterTypes = new Class[]{String.class, field.getType()};
            }
        }
        return parameterTypes;
    }

    private String getColumnMethodName(Class<?> fieldType){
        String typeName;
        if(fieldType.isPrimitive()){
            typeName = DBUtil.capitalize(fieldType.getName());
        } else {
            typeName = fieldType.getSimpleName();
        }
        String methodName = "get" + typeName;
        if("getBoolean".equals(methodName)){
            methodName = "getInt";
        } else if("getChar".equals(methodName) || "getCharacter".equals(methodName)){
            methodName = "getString";
        } else if ("getDate".equals(methodName)){
            methodName = "getLong";
        } else if("getInteger".equals(methodName)){
            methodName = "getInt";
        }
        return methodName;
    }

    private boolean isPrimitiveBooleanType(Field field){
        Class<?> fieldType = field.getType();
        if("boolean".equals(fieldType.getName())){
            return true;
        } else {
            return false;
        }
    }

    //set方法
    private String makeSetterMethodName(Field field){
        String setterMethodName;
        String setterMethodPrefix = "set";
        if(isPrimitiveBooleanType(field) && field.getName().matches("^is[A-Z]{1}.*$")){
            setterMethodName = setterMethodPrefix + field.getName().substring(2);
        } else if(field.getName().matches("^[a-z]{1}[A-Z]{1}.*")){
            setterMethodName = setterMethodPrefix + field.getName();
        } else {
            setterMethodName = setterMethodPrefix + DBUtil.capitalize(field.getName());
        }
        return setterMethodName;
    }

    /**
     * 是否是字符串类型
     * */
    private boolean isCharType(Field field){
        String type = field.getType().getName();
        return type.equals("char") || type.endsWith("Character");
    }

    /**
     * 得到对象的类型
     * */
    private Class<?> getObjectType(Class<?> primitiveType){
        if(primitiveType != null){
            if(primitiveType.isPrimitive()){
                String basicTypeName = primitiveType.getName();
                if("int".equals(basicTypeName)){
                    return Integer.class;
                } else if ("short".equals(basicTypeName)){
                    return Short.class;
                } else if ("long".equals(basicTypeName)){
                    return Long.class;
                } else if ("float".equals(basicTypeName)){
                    return Float.class;
                } else if ("double".equals(basicTypeName)){
                    return Double.class;
                } else if("boolean".equals(basicTypeName)){
                    return Boolean.class;
                } else if ("char".equals(basicTypeName)){
                    return Character.class;
                }
            }
        }
        return null;
    }

    //创建数据库，更新数据
    class MSQLiteHelper extends SQLiteOpenHelper{

        private String tag = MSQLiteHelper.class.getSimpleName();

        private Class mClass;

        public MSQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        public MSQLiteHelper(Context context, String dbName, int dbVersion, Class aClass){
            this(context, dbName, null, dbVersion);
            mClass = aClass;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            createTable(db);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS" + DBUtil.getTableName(mClass));
            createTable(db);
        }

        /**
         * 根据指定的类创建表
         * */
        private void createTable(SQLiteDatabase db){
            db.execSQL(getCreateTabSql(mClass));
        }

        /**
         * 获取创建表的语句
         * */
        private String getCreateTabSql(Class<?> clazz){
            StringBuilder sb = new StringBuilder();
            String tabName = DBUtil.getTableName(clazz);
            sb.append("create table ").append(tabName).append(" (id  INTEGER PRIMARY KEY AUTOINCREMENT, ");
            // 利用反射获取对象中的所有属性
            Field[] fields = clazz.getDeclaredFields();
            for(Field fd : fields){
                String fieldName = fd.getName();
                String fieldType = fd.getType().getName();
                // 移除用户设置的ID属性
                if(fieldName.equalsIgnoreCase("_id") || fieldName.equalsIgnoreCase("id")){
                    continue;
                } else {
                    sb.append(fieldName).append(DBUtil.getColumnType(fieldType)).append(", ");
                }
            }
            int len = sb.length();
            sb.replace(len - 2, len, ")");  // 替换多出的","
            Log.i(tag, " ***** " + sb.toString());
            return sb.toString();
        }
    }

}
