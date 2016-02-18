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
 * Time: 16:14
 * Version 1.0
 */

public class ArticleDao extends MNDao<Article>{


    private Dao<Article, Integer> articleDaoPeo;

    private String tag = ArticleDao.class.getSimpleName();

    public ArticleDao(Context context) {
        super(context);
        try{
            articleDaoPeo = mHelper.getDao(Article.class);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int count() {
        int count = 0;
        try{
            count = (int) articleDaoPeo.countOf();
        } catch (Exception e){
            e.printStackTrace();
        }
        Log.i(tag, "--总数="+count);
        return count;
    }

    @Override
    public void add(Article article) {
        try{
            articleDaoPeo.create(article);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        try{
            articleDaoPeo.deleteById(id);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void update(Article article) {
        try{
            articleDaoPeo.update(article);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public List<Article> getDatas(int p) {
        List<Article> articles = null;
        try{
            QueryBuilder<Article, Integer> queryBuilder = articleDaoPeo.queryBuilder();
            queryBuilder.orderBy("id", false);
            queryBuilder.limit(limit);
            queryBuilder.offset(p * limit);
            articles = queryBuilder.query();
        } catch (Exception e){
            e.printStackTrace();
        }
        return articles;
    }

    @Override
    public List<Article> getAllDatas() {
        List<Article> articles = null;
        try{
            articles = articleDaoPeo.queryForAll();
        } catch (Exception e){
            e.printStackTrace();
        }
        return articles;
    }


}
