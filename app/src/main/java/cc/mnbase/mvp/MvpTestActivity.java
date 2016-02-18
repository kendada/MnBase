package cc.mnbase.mvp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cc.mn.BaseActivity;
import cc.mnbase.R;
import cc.mnbase.mvc.model.Root;
import cc.mnbase.mvp.mvp.User;
import cc.mnbase.mvp.presenter.UserLoginPresenter;
import cc.mnbase.mvp.view.IUserLoginView;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2016-01-19
 * Time: 15:22
 * Version 1.0
 */

public class MvpTestActivity extends BaseActivity implements IUserLoginView{

    private Button login_btn, clear_btn;
    private EditText username, password;

    private Root mRoot;

    private UserLoginPresenter mUserLoginPresenter = new UserLoginPresenter(this);

    private String tag = MvpTestActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mvp_test_layout);

        clear_btn = (Button)findViewById(R.id.clear_btn);
        login_btn = (Button)findViewById(R.id.login_btn);
        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserLoginPresenter.login();
            }
        });

        clear_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserLoginPresenter.clear();
            }
        });

    }


    @Override
    public String getUserName() {
        return username.getText().toString();
    }

    @Override
    public String getPassWord() {
        return password.getText().toString();
    }

    @Override
    public void clearUserName() {
        username.setText("");
    }

    @Override
    public void clearPassWord() {
        password.setText("");
    }

    @Override
    public void showLogining() {
        Toast.makeText(getContext(), "登录中...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void hideLogin() {
        Toast.makeText(getContext(), "完成登录...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void toMainActivity(Root root) {
        mRoot = root;
        Log.i(tag, "-----" + mRoot);
    }

    @Override
    public void showFailedError() {
        Toast.makeText(getContext(), "*** 登录发生错误 ***", Toast.LENGTH_SHORT).show();
    }
}
