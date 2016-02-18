package cc.mnbase.zhujie;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import cc.mnbase.R;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2016-01-15
 * Time: 09:53
 * Version 1.0
 */

public class TestAbsBaseActivity extends AbsBaseActivity {

    @FindViewById(id=R.id.btn_login)
    private Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_test_abs_base_layout);
    }

    @Override
    public void findViews() {

    }

    @Override
    public void setListeners() {
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "**注解测试**", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
