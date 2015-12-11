package cc.mnbase.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cc.mn.BaseActivity;
import cc.mnbase.R;
import cc.mnbase.adapter.base.MnBaseAdapter;
import cc.mnbase.view.siling.SwipeBackLinearLayout;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-12-04
 * Time: 09:40
 * Version 1.0
 */

public class SwipeBack3Activity extends BaseActivity {

    private Toolbar toolbar;
    private SwipeBackLinearLayout swipe_back_layout;
    private ListView list_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_back3_layout);

        list_view = (ListView)findViewById(R.id.list_view);
        swipe_back_layout = (SwipeBackLinearLayout)findViewById(R.id.swipe_back_layout);
        swipe_back_layout.setTouchView(list_view);
        swipe_back_layout.setOnSwipeBackListener(new SwipeBackLinearLayout.OnSwipeBackListener() {
            @Override
            public void onFinishActivity() {
                Toast.makeText(getContext(), "已经退出。。。", Toast.LENGTH_SHORT).show();
                SwipeBack3Activity.this.finish();
            }
        });

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        List<String> list = new ArrayList<>();
        for(int i=0;i<100;i++){
            list.add("测试。。。"+i);
        }

        TestAdater adater = new TestAdater(this, list);
        list_view.setAdapter(adater);

    }

    private class TestAdater extends MnBaseAdapter<String>{

        public TestAdater(Context context, List<String> datas) {
            super(context, datas);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView textView = new TextView(mContext);
            AbsListView.LayoutParams params = new AbsListView.LayoutParams(AbsListView.LayoutParams.WRAP_CONTENT,
                    AbsListView.LayoutParams.WRAP_CONTENT);
            textView.setLayoutParams(params);
            textView.setPadding(11, 11, 11, 11);
            textView.setText(mDatas.get(position));
            return textView;
        }
    }

}
