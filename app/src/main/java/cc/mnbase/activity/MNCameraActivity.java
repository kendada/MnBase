package cc.mnbase.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import cc.mn.BaseCameraActivity;
import cc.mnbase.R;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-12-11
 * Time: 17:01
 * Version 1.0
 */

public class MNCameraActivity extends BaseCameraActivity {

    Button btn1, btn2;
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mn_camera_layout);

        text = (TextView)findViewById(R.id.text);
        btn1 = (Button)findViewById(R.id.btn1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAlbum();
            }
        });
        btn2 = (Button)findViewById(R.id.btn2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera("ccc");
            }
        });
    }

    @Override
    public void selectSuccess(String path) {
        text.setText(path);
    }
}
