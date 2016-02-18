package cc.mnbase.ormlite.model;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.util.List;

import cc.mnbase.ormlite.DatabaseHelper;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-12-16
 * Time: 15:29
 * Version 1.0
 */

public class UserDao extends MNDao<User>{

    private Dao<User, Integer> userDaoOpe;

    private String tag = UserDao.class.getSimpleName();

    public UserDao(Context context){
        super(context);
        try{
            userDaoOpe = mHelper.getDao(User.class);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int count() {
        int count = 0;
        try{
            count = (int) userDaoOpe.countOf();
        } catch (Exception e){
            e.printStackTrace();
        }
        Log.i(tag, "--总数=" + count);
        return count;
    }

    /**
     * 添加一个User
     * */
    public void add(User user){
        try {
            userDaoOpe.create(user);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 根据ID删除一个User
     * */
    public void delete(int id){
        try {
            userDaoOpe.deleteById(id);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 更新一个User
     * */
    public void update(User user){
        try {
            userDaoOpe.update(user);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 获取分页User
     * @param p 页数：从0开始
     * */
    public List<User> getDatas(int p){
        List<User> list = null;
        try {
            QueryBuilder<User, Integer> queryBuilder = userDaoOpe.queryBuilder();
            queryBuilder.orderBy("id", false); //根据Id降序排列
            queryBuilder.limit(limit); //每页总数
            queryBuilder.offset(p * limit); //偏移量
            list = queryBuilder.query(); //获取分页数据
        } catch (Exception e){
            e.printStackTrace();
        }

        return list;
    }

    /**
     * 获取全部的User
     * */
    public List<User> getAllDatas(){
        List<User> list = null;
        try {
            list = userDaoOpe.queryForAll();
        } catch (Exception e){
            e.printStackTrace();
        }

        return list;
    }
}
