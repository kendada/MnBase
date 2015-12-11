package cc.mnbase.activity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cc.mn.BaseActivity;
import cc.mn.view.refreshview.MNRefreshView;
import cc.mn.view.refreshview.listener.OnBottomLoadMoreTime;
import cc.mnbase.R;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-12-09
 * Time: 11:09
 * Version 1.0
 */

public class MNRefreshViewActivity extends BaseActivity {

    private ArrayAdapter<String> adapter;
    private List<String> str_name = new ArrayList<String>();

    private MNRefreshView refreshView;
    private GridView list_view;

    public static long lastRefreshTime;

    private boolean isLoadingMore = false;
    private int index = 0;

    private String tag = MNRefreshViewActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mn_refresh_view_layout);

        refreshView = (MNRefreshView)findViewById(R.id.refreshView);
        list_view = (GridView)findViewById(R.id.list_view);

        for (int i = 0; i < 100; i++) {
            str_name.add("数据" + i);
        }
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, str_name);
        list_view.setAdapter(adapter);

        // 设置是否可以下拉刷新
        refreshView.setPullRefreshEnable(true);
        // 设置是否可以上拉加载
        refreshView.setPullLoadEnable(true);
        // 设置上次刷新的时间
        refreshView.restoreLastRefreshTime(lastRefreshTime);
        // 设置true时候可以自动刷新
        refreshView.setAutoRefresh(false);
        // 自动加载更多
        refreshView.setAutoLoadMore(false);

        refreshView.setRefreshViewListener(new MNRefreshView.SimpleMNRefreshViewListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshView.stopRefresh();
                        lastRefreshTime = refreshView.getLastRefreshTime();
                        str_name.add(0, "刷新数据");
                        adapter.notifyDataSetChanged();
                    }
                }, 3000);
            }

            @Override
            public void onLoadMore(boolean isSlience) {
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        refreshView.stopLoadMore();
                        str_name.add("新加数据"+index);
                        adapter.notifyDataSetChanged();
                        index++;
                    }
                }, 3000);
            }

            @Override
            public void onRelease(float direction) {
                if (direction > 0) {
                    toast("下拉");
                } else {
                    toast("上拉");
                }
            }

        });

    //    refreshView.setOnBottomLoadMoreTime();

    }

    public void toast(String msg) {
        Toast.makeText(getApplicationContext(), msg, 0).show();
    }

}
