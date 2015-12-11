package cc.mnbase.activity;

import android.os.Bundle;
import android.widget.TextView;

import cc.mn.BaseActivity;
import cc.mnbase.R;
import cc.mnbase.view.progress.bar.MnScaleBar;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-11-30
 * Time: 14:44
 * Version 1.0
 */

public class MnScaleBarActivity extends BaseActivity{

    private TextView text;
    private MnScaleBar scale_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scale_bar_layout);


        scale_bar = (MnScaleBar)findViewById(R.id.scale_bar);
        text = (TextView)findViewById(R.id.text);

        scale_bar.setOnScrollListener(new MnScaleBar.OnScrollListener() {
            @Override
            public void onScrollScale(int scale) {
                text.setText("身高："+scale+"cm");
            }
        });
    }
}
