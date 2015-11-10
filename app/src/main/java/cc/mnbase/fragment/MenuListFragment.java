package cc.mnbase.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cc.mnbase.R;
import cc.mnbase.adapter.MainMAdapter;
import cc.mnbase.view.list.menu.MenuListView;

/**
 * User: (1203596603@qq.com)
 * Date: 2015-10-19
 * Time: 15:25
 * Version 1.0
 */

public class MenuListFragment extends BaseFragment {

    @Bind(R.id.menu_list_view)
    MenuListView menuListView;

    private MainMAdapter adapter;
    private List<String> list;

    private String tag = "MenuListFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_menu_list_layout, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        list = new ArrayList<>();
        for(int i=0; i<100; i++){
            list.add("测试菜单"+i);
        }
        Log.i(tag, "----"+list);
        adapter = new MainMAdapter(getActivity(), list);
        menuListView.setAdapter(adapter);

        menuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "***" + position + "***", Toast.LENGTH_SHORT).show();
            }
        });

        menuListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "***长按***" + position + "***", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }


}
