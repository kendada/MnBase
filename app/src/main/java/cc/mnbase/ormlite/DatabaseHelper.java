package cc.mnbase.ormlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import cc.mnbase.ormlite.model.Article;
import cc.mnbase.ormlite.model.Message;
import cc.mnbase.ormlite.model.User;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-12-16
 * Time: 11:47
 * Version 1.0
 */

public class DatabaseHelper extends OrmLiteSqliteOpenHelper{

    private final static String  DB_NAME = "test.db";  // 数据库名

    private Map<String, Dao> daos = new HashMap<>();

    private String tag = DatabaseHelper.class.getSimpleName();

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 8);
    }

    // 创建 数据库 表
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            //新安装app直接创建
            TableUtils.createTable(connectionSource, User.class);
            TableUtils.createTable(connectionSource, Article.class);
            TableUtils.createTable(connectionSource ,Message.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //数据库升级时自动调用
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i1) {
        try {
            //添加字段
            sqLiteDatabase.execSQL("alter table tab_user add column icon varchar");
            Log.i(tag, "添加字段成功。。。");

            TableUtils.createTable(connectionSource ,Message.class); //低版本app升级创建
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized Dao getDao(Class aClass) throws SQLException{
        Dao dao = null;
        String className = aClass.getSimpleName();
        if(daos.containsKey(className)){
            dao = daos.get(className);
        }

        if(dao == null){
            dao = super.getDao(aClass);
            daos.put(className, dao);
        }

        return dao;
    }

    @Override
    public void close() {
        super.close();
        for(String key:daos.keySet()){
            Dao dao = daos.get(key);
            dao = null;
        }
    }
}
