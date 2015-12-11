package cc.mnbase.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.GridView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cc.mnbase.R;
import cc.mnbase.adapter.WaterPullAdapter;
import cc.mnbase.model.Emm;

/**
 * User: (1203596603@qq.com)
 * Date: 2015-10-22
 * Time: 10:16
 * Version 1.0
 */

public class LruImageFragment extends BaseFragment {

    private GridView gridView;
    private Button load_btn;

    private List<Emm> list;

    private WaterPullAdapter adapter;

    private int p = 1;

    private boolean loading = false;

    private String tag = LruImageFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_lru_image_layout, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        gridView = (GridView)view.findViewById(R.id.gridView);
        load_btn = (Button)view.findViewById(R.id.load_btn);

        initData();

        load_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!loading) {
                    loadMore();
                }
            }
        });

        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState){
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE: //停止滑动

                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING: //滚动做出抛的动作

                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL: //正在滚动
                        break;

                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

    }

    private void initData(){
        AsyncHttpClient httpClient = new AsyncHttpClient();
        String url = "http://www.emm.cc/appapi/colType.php?type=hot&typeid=1&p="+p;
        httpClient.get(url, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int i, Header[] headers, String s, Throwable throwable) {

            }

            @Override
            public void onSuccess(int i, Header[] headers, String s) {
                //    list = listEmm(s);
                list = emms(s);
                adapter = new WaterPullAdapter(gridView, list);
                gridView.setAdapter(adapter);
                p++;
            }
        });
    }

    private void loadMore(){
        AsyncHttpClient httpClient = new AsyncHttpClient();
        String url = "http://www.emm.cc/appapi/colType.php?type=hot&typeid=1&p="+p;
        httpClient.get(url, new TextHttpResponseHandler() {
            @Override
            public void onStart() {
                loading = true;
            }

            @Override
            public void onFailure(int i, Header[] headers, String s, Throwable throwable) {

            }

            @Override
            public void onSuccess(int i, Header[] headers, String s) {
                //    List<Emm> more = listEmm(s);
                List<Emm> more = emms(s);
                list.addAll(more);
                adapter.notifyDataSetChanged();
                p++;
            }

            @Override
            public void onFinish() {
                loading = false;
            }
        });
    }

    private List<Emm> emms(String json){
        List<Emm> list = null;
        try{
            Gson gson = new Gson();
            Type type = new TypeToken<List<Emm>>(){}.getType();
            list = gson.fromJson(json, type);
        } catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

    private List<Emm> emmList(String json){
        List<Emm> list = null;
        try{
            JsonReader jsonReader = new JsonReader(new StringReader(json));
            jsonReader.beginArray();
            while(jsonReader.hasNext()){
                jsonReader.beginObject();
                while (jsonReader.hasNext()){
                    String tagName = jsonReader.nextName();
                    Log.i(tag, "----"+tagName);
                }
                jsonReader.endObject();
            }
            jsonReader.endArray();
        } catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

    private List<Emm> listEmm(String json){
        List<Emm> list = null;
        try{
            list = new ArrayList<Emm>();
            JSONArray array = new JSONArray(json);
            int count = array.length();
            for(int i=0; i<count; i++){
                JSONObject object = array.optJSONObject(i);
                String id = object.optString("id");
                String title = object.optString("title");
                String litpic = object.optString("litpic");
                int click = object.optInt("click");
                String description = object.optString("description");
                int lit_width = object.optInt("lit_width");
                int lit_height = object.optInt("lit_height");
                Emm emm = new Emm(id, title, litpic, click, description, lit_width, lit_height);
                list.add(emm);
            }
        } catch (Exception e){

        }
        return list;
    }

}
