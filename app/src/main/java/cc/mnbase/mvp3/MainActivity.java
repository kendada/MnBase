package cc.mnbase.mvp3;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import java.util.List;

import cc.mnbase.R;
import cc.mnbase.mvp3.adapter.MainAdapter;
import cc.mnbase.mvp3.model.Data;
import cc.mnbase.mvp3.model.LoveData;
import cc.mnbase.mvp3.presenter.LovePresenter;
import cc.mnbase.mvp3.view.ILoveView;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2016-02-15
 * Time: 14:13
 * Version 1.0
 */

public class MainActivity extends AppCompatActivity implements ILoveView {

    private ListView list_view;
    private MainAdapter adapter;

    private LovePresenter lovePresenter = new LovePresenter(this);

    private String tag = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mvp3_main_layout);

        list_view = (ListView)findViewById(R.id.list_view);

        lovePresenter.getData(); // 获取数据
    }


    @Override
    public String getKey() {
        return "1d5e605179a34d009cc50cfff3e3cec1";
    }

    @Override
    public int getTypeId() {
        return 1;
    }

    @Override
    public void toResult(Data data) {
        Log.i(tag, " *** " + data);
        if(data == null) return;
        if(data.error_code != 0) return;
        if(data.result == null) return;
        List<LoveData> loveDatas = data.result.loveDatas;
        if(loveDatas != null && loveDatas.size() !=0){

            adapter = new MainAdapter(this, loveDatas);
            list_view.setAdapter(adapter);

        }
    }

}
