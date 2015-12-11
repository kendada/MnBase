package cc.mnbase.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
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

public class SwipeBack1Activity extends BaseActivity {

    private Toolbar toolbar;
    private SwipeBackLinearLayout swipe_back_layout;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_back1_layout);

        swipe_back_layout = (SwipeBackLinearLayout)findViewById(R.id.swipe_back_layout);
        swipe_back_layout.setTouchView(swipe_back_layout);
        swipe_back_layout.setOnSwipeBackListener(new SwipeBackLinearLayout.OnSwipeBackListener() {
            @Override
            public void onFinishActivity() {
                Toast.makeText(getContext(), "已经退出。。。", Toast.LENGTH_SHORT).show();
                SwipeBack1Activity.this.finish();
            }
        });

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        btn = (Button)findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SwipeBack4Activity.class);
                startActivity(intent);
            }
        });
    }



}
