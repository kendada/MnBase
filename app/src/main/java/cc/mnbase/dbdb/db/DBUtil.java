package cc.mnbase.dbdb.db;

import android.text.TextUtils;

import java.util.Locale;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2016-02-16
 * Time: 10:15
 * Version 1.0
 */

public class DBUtil {

    /**
     * 获取数据库表名
     * */
    public static String getTableName(Class<?> aClass){
        return aClass.getSimpleName();
    }

    /**
     * 获取字段的数据类型
     * */
    public static String getColumnType(String type){
        String value = null;
        if(type.contains("String")){
            value = " text ";
        } else if(type.contains("int")) {
            value = " integer ";
        } else if (type.contains("boolean")){
            value = " boolean ";
        } else if(type.contains("float")){
            value = " float ";
        } else if(type.contains("double")){
            value = " double ";
        } else if(type.contains("char")){
            value = " varchar ";
        } else if(type.contains("long")){
            value = " long ";
        }
        return value;
    }

    public static String capitalize(String string) {
        if (!TextUtils.isEmpty(string)) {
            return string.substring(0, 1).toUpperCase(Locale.US) + string.substring(1);
        }
        return string == null ? null : "";
    }

}
