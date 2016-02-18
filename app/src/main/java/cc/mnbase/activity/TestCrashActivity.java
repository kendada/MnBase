package cc.mnbase.activity;

import android.content.Intent;
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

public class TestCrashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_crash_layout);
    }

    public void onCrashException(View view) {
        Intent intent = new Intent(this, TestCrashActivity1.class);
        startActivity(intent);
        finish();
    }

    public void onTest(View view){
        int i = 3/0;
    }

}
