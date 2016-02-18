package cc.mnbase.mvp2;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import cc.mn.BaseActivity;
import cc.mnbase.R;
import cc.mnbase.mvp2.model.Air;
import cc.mnbase.mvp2.presenter.AirPresenter;
import cc.mnbase.mvp2.view.IAirView;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2016-01-20
 * Time: 10:18
 * Version 1.0
 */

public class Mvp2TestActivity extends BaseActivity implements IAirView{


    private EditText edit_air;

    private Button btn_air;

    private TextView text_air;

    private AirPresenter mAirPresenter = new AirPresenter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mvp2_test_layout);

        edit_air = (EditText)findViewById(R.id.edit_air);
        btn_air = (Button)findViewById(R.id.btn_air);
        text_air = (TextView)findViewById(R.id.text_air);

        btn_air.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAirPresenter.getData();
            }
        });

    }

    @Override
    public String getCitName() {
        return edit_air.getText().toString(); //
    }

    @Override
    public void toResult(Air air) {
        if(air == null) return;
        text_air.setText(air.toString());
    }
}
