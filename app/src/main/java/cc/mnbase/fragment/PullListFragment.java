package cc.mnbase.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import cc.mnbase.R;
import cc.mnbase.adapter.MainMAdapter;
import cc.mnbase.view.list.basepull.RefreshLayoutBase;
import cc.mnbase.view.list.menu.RefreshMenuListView;

/**
 * User: 靳世坤(1203596603@qq.com)
 * Date: 2015-10-21
 * Time: 15:13
 * Version 1.0
 */

public class PullListFragment extends BaseFragment {

    private RefreshMenuListView refreshListView;

    private ListView listView;

    private List<String> list;

    private MainMAdapter adapter;

    private String tag = "PullListFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pull_list_layout, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        refreshListView = (RefreshMenuListView)view.findViewById(R.id.list_view);
        listView = refreshListView.getContentView();

        list = new ArrayList<>();
        for(int i=0; i<30; i++){
            list.add("测试菜单"+i);
        }
        Log.i(tag, "----" + list);
        adapter = new MainMAdapter(getActivity(), list);
        listView.setAdapter(adapter);

        refreshListView.setOnRefreshListener(new RefreshLayoutBase.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i(tag, "------59-------");
                refreshListView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshListView.refreshComplete();
                    }
                }, 500);
            }
        });

        refreshListView.setOnLoadListener(new RefreshLayoutBase.OnLoadListener() {
            @Override
            public void onLoadMore() {
                Log.i(tag, "------67-------");
                listView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshListView.loadCompelte();
                    }
                }, 500);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i(tag, "----单击---**" + position + "**");
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i(tag, "----长按----**" + position + "**");
                return true;
            }
        });

    }

}
