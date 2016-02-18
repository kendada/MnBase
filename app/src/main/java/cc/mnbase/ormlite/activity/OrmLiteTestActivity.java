package cc.mnbase.ormlite.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Calendar;
import java.util.List;

import cc.mn.BaseActivity;
import cc.mnbase.R;
import cc.mnbase.ormlite.model.Article;
import cc.mnbase.ormlite.model.ArticleDao;
import cc.mnbase.ormlite.model.User;
import cc.mnbase.ormlite.model.UserDao;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-12-16
 * Time: 14:37
 * Version 1.0
 */

public class OrmLiteTestActivity extends BaseActivity {

    private UserDao mUserDao;

    private ArticleDao mArticleDao;

    private EditText edit, edit_con;
    private Button btn, btn_delete, btn_get;

    private int p = 0;

    private String tag = OrmLiteTestActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orm_lite_test_layout);

        mUserDao = new UserDao(getContext());
        mArticleDao = new ArticleDao(getContext());

        btn = (Button)findViewById(R.id.btn);
        btn_delete = (Button)findViewById(R.id.btn_delete);
        btn_get = (Button)findViewById(R.id.btn_get);
        edit = (EditText)findViewById(R.id.edit);
        edit_con = (EditText)findViewById(R.id.edit_con);

        final User user = new User("张三丰大神");
        user.setId(1);
        mUserDao.add(user);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edit.getText().toString();
                if(TextUtils.isEmpty(name)) return;
                String content = edit_con.getText().toString();
                Article article = new Article(name, content, user);
                mArticleDao.add(article);
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //    mUserDao.delete(user.getId());

                User user = new User("玉萨");
                user.setSignature("手心里的温柔。。。。");
                user.setSex("女");
                user.setAge(35);
                user.setPhoneNum(135807);
                user.setIcon("测试头像");
                mUserDao.add(user);
            }
        });

        btn_get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getList(p);
                getAList(p);
                p++;
            }
        });
    }


    //获取列表
    private void getList(int p){
        try{
            List<User> users = mUserDao.getDatas(p);
            for(User user:users){
                Log.i(tag, user.toString()+"\r\n");
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    //获取列表
    private void getAList(int p){
        try{
            List<Article> articles = mArticleDao.getDatas(p);
            for(Article article:articles){
                Log.i(tag, article.toString()+"\r\n");
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
