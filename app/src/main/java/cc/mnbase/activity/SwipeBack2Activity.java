package cc.mnbase.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ScrollView;
import android.widget.Toast;

import cc.mn.BaseActivity;
import cc.mnbase.R;
import cc.mnbase.view.siling.SwipeBackLinearLayout;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-12-04
 * Time: 09:40
 * Version 1.0
 */

public class SwipeBack2Activity extends BaseActivity {

    private Toolbar toolbar;
    private SwipeBackLinearLayout swipe_back_layout;
    private ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_back2_layout);

        scrollView = (ScrollView)findViewById(R.id.scrollView);
        swipe_back_layout = (SwipeBackLinearLayout)findViewById(R.id.swipe_back_layout);
        swipe_back_layout.setTouchView(scrollView);
        swipe_back_layout.setOnSwipeBackListener(new SwipeBackLinearLayout.OnSwipeBackListener() {
            @Override
            public void onFinishActivity() {
                Toast.makeText(getContext(), "已经退出。。。", Toast.LENGTH_SHORT).show();
                SwipeBack2Activity.this.finish();
            }
        });

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }



}
