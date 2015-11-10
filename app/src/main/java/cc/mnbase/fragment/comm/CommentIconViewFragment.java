package cc.mnbase.fragment.comm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cc.mnbase.R;
import cc.mnbase.adapter.IconViewAdapter;
import cc.mnbase.fragment.BaseFragment;
import cc.mnbase.view.comment.CommentIconView;

/**
 * Date: 2015-10-26
 * Time: 10:27
 * Version 1.0
 */

public class CommentIconViewFragment extends BaseFragment {

    private List<String> list;
    private List<List<String>> mLists;
    private IconViewAdapter adapter;

   // private ListView list_view = null;
    private CommentIconView comment_icon_view;

    private String tag = CommentIconViewFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_comm_icon_view_layout, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getList();

        comment_icon_view = (CommentIconView)view.findViewById(R.id.comment_icon_view);
        comment_icon_view.addIcons(list);


     //   list_view = (ListView)view.findViewById(R.id.list_view);

    //    adapter = new IconViewAdapter(getActivity(), mLists);
     //   list_view.setAdapter(adapter);
    }

    private void getList(){
        list = new ArrayList<>();
        list.add("http://mnsfz-img.xiuna.com/pic/meihuo/2015-10-19/1/0.jpg");
        list.add("http://mnsfz-img.xiuna.com/pic/qiaopi/2015-10-13/1/0.jpg");
        list.add("http://mnsfz-img.xiuna.com/pic/qingchun/2015-10-12/1/0.jpg");
        list.add("http://mnsfz-img.xiuna.com/pic/meihuo/2015-10-5/1/0.jpg");
        list.add("http://mnsfz-img.xiuna.com/pic/meihuo/2015-10-1/1/0.jpg");
        list.add("http://mnsfz-img.xiuna.com/pic/meihuo/2015-9-23/1/0.jpg");
        list.add("http://mnsfz-img.xiuna.com/pic/yangguang/2015-10-1/1/0.jpg");
        list.add("http://mnsfz-img.xiuna.com/pic/yangguang/2015-9-23/1/0.jpg");
       // list.add("http://mnsfz-img.xiuna.com/pic/yangguang/2015-9-1/1/0.jpg");
       // list.add("http://mnsfz-img.xiuna.com/pic/yangguang/2015-8-26/1/0.jpg");
      //  list.add("http://mnsfz-img.xiuna.com/pic/yangguang/2015-8-25/1/0.jpg");
      //  list.add("http://mnsfz-img.xiuna.com/pic/yangguang/2015-9-2/1/0.jpg");
      //  list.add("http://mnsfz-img.xiuna.com/pic/yangguang/2015-8-21/1/0.jpg");

        mLists = new ArrayList<>();
        for(int i=0; i<20; i++){
            mLists.add(list);
        }
    }

}
