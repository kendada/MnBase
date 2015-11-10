package cc.mnbase.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cc.mn.widget.StateFrameLayout;
import cc.mnbase.R;
import cc.mnbase.adapter.MnTestAdapter;
import cc.mnbase.model.Girl;
import cc.mnbase.util.api.ApiUtil;
import cc.mnbase.util.json.JsonUtil;

/**
 * User: (1203596603@qq.com)
 * Date: 2015-08-17
 * Time: 20:51
 * Version 1.0
 */

public class MainFragment extends BaseFragment {

    @Bind(R.id.list_view)
    ListView listView;
    @Bind(R.id.state_frame_layout)
    StateFrameLayout stateFrameLayout;

    private int page = 1;

    private List<Girl> girlList = null;
    private MnTestAdapter adapter = null;

    private String tag = "MainFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(firstVisibleItem + visibleItemCount == totalItemCount-1){
                    loadMore();
                }
            }
        });
    }

    public void initData(){
        stateFrameLayout.setViewState(StateFrameLayout.VIEW_STATE_LOADING);
        ApiUtil.girlsData(page, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
                stateFrameLayout.setViewState(StateFrameLayout.VIEW_STATE_ERROR);
                setViewOnClickListener(StateFrameLayout.VIEW_STATE_ERROR);
            }

            @Override
            public void onSuccess(int i, Header[] headers, String s) {
                stateFrameLayout.setViewState(StateFrameLayout.VIEW_STATE_CONTENT);
                girlList = JsonUtil.girlList(s);
                if (girlList != null && girlList.size() > 0) {
                    adapter = new MnTestAdapter(getActivity(), girlList);
                    listView.setAdapter(adapter);
                    page++;
                } else {
                    stateFrameLayout.setViewState(StateFrameLayout.VIEW_STATE_EMPTY);
                    setViewOnClickListener(StateFrameLayout.VIEW_STATE_EMPTY);
                }
            }

            @Override
            public void onFinish() {

            }
        });
    }

    private void loadMore(){
        stateFrameLayout.setViewState(StateFrameLayout.VIEW_STATE_LOADING);
        ApiUtil.girlsData(page, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
                stateFrameLayout.setViewState(StateFrameLayout.VIEW_STATE_ERROR);
                setViewOnClickListener(StateFrameLayout.VIEW_STATE_ERROR);
            }

            @Override
            public void onSuccess(int i, Header[] headers, String s) {
                stateFrameLayout.setViewState(StateFrameLayout.VIEW_STATE_CONTENT);
                List<Girl> moreList = JsonUtil.girlList(s);
                if (moreList != null && moreList.size() > 0) {
                    girlList.addAll(moreList);
                    adapter.notifyDataSetChanged();
                    page++;
                }
            }

            @Override
            public void onFinish() {

            }
        });
    }

    /**
     * 设置OnClickListener
     * */
    private void setViewOnClickListener(@StateFrameLayout.ViewState int state){
        View view = stateFrameLayout.getView(state);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
