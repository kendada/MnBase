package cc.mnbase.rxbind;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;

import java.util.concurrent.TimeUnit;

import cc.mn.BaseActivity;
import cc.mnbase.R;
import rx.functions.Action1;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2016-01-04
 * Time: 15:48
 * Version 1.0
 */

public class RxbindingDemoActivity extends BaseActivity {

    private EditText edit_con;
    private Button rx_bind_btn;
    private TextView info_text;

    private int index;

    private String tag = RxbindingDemoActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxbinding_demo_layout);

        edit_con = (EditText)findViewById(R.id.edit_con);
        rx_bind_btn = (Button)findViewById(R.id.rx_bind_btn);
        info_text = (TextView)findViewById(R.id.info_text);

        RxView.clicks(rx_bind_btn)
                .throttleFirst(300, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                info_text.setText(edit_con.getText().toString());
                Log.i(tag, " --- 点击按钮次数 --- " + index);
                index++;
            }
        });

        RxTextView.textChanges(edit_con)
                  .debounce(300, TimeUnit.MILLISECONDS) // 输入变化之后300毫秒
                  .subscribe(new Action1<CharSequence>() {
                    @Override
                    public void call(CharSequence charSequence) {
                        Log.i(tag, " 输入的内容： *** " + charSequence.toString() + " *** ");
                    }
                })      ;



    }

    public void test1(View view){

    }


}
