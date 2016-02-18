package cc.mnbase.activity;

import android.os.Bundle;
import android.view.View;

import cc.mn.BaseActivity;
import cc.mnbase.R;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2016-01-12
 * Time: 17:08
 * Version 1.0
 */

public class TestCrashActivity1 extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_crash_layout1);
    }

    public void onCrashException(View view) {
        throw new RuntimeException("这是一个未在APP里捕获处理的异常");
    }

    public void onTest(View view){
        int i = 3/0;
    }
}
