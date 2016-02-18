package cc.mnbase.mvc;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import cc.mn.BaseActivity;
import cc.mnbase.R;
import cc.mnbase.mvc.model.Root;

/**
 * Date: 2016-01-18
 * Time: 10:24
 * Version 1.0
 */

public class MvcActivity extends BaseActivity implements View.OnClickListener, OnDataListener, OnDataBitmapListener {

    private Button btnData;
    private TextView user_info;
    private ImageView img;

    private MvcActData mvcActData; // 处理数据
    private MvcActDataBitmap mvcActDataBitmap;

    private String tag = MvcActData.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mvc_layout);

        user_info = (TextView)findViewById(R.id.user_info);
        btnData = (Button)findViewById(R.id.btnData);
        img = (ImageView)findViewById(R.id.img);

        btnData.setOnClickListener(this);

        mvcActData = new MvcActData(this);
        mvcActDataBitmap = new MvcActDataBitmap(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnData:
                mvcActData.sendData();
                mvcActDataBitmap.sendBitmap(getContext());
                break;
        }
    }

    @Override
    public void onSuccess(Root root, int page) {
        Log.i(tag, "*** onSuccess() ***");
        if(root == null) return;
        user_info.setText("  获取数据 ： " + root.getShowapiResBody().getDatalist());
    }

    @Override
    public void onFinish() {
        Log.i(tag, "***  onFinish() ***");
    }

    @Override
    public void onError() {
        Log.i(tag, "*** onError() ***");
    }

    @Override
    public void onBitmap(Bitmap bitmap) {
        img.setImageBitmap(bitmap);
    }
}
