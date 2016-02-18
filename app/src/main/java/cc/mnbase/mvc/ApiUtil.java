package cc.mnbase.mvc;

import android.util.Log;

import com.google.gson.Gson;

import java.util.List;

import cc.mnbase.mvc.model.Movies;
import cc.mnbase.mvc.model.Root;
import cc.mnbase.mvc.model.ShowapiResBody;
import cc.mnbase.mvc.util.http.AsyncHttpUtil;
import cc.mnbase.mvc.util.http.HttpUtil;
import cc.mnbase.mvc.util.http.LiteHttpUtil;
import cc.mnbase.mvc.util.http.OnNetWorkDataListener;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2016-01-18
 * Time: 19:21
 * Version 1.0
 */

public class ApiUtil {

    private HttpUtil mHttpUtil;

    private String tag = ApiUtil.class.getSimpleName();

    public ApiUtil(){
        AsyncHttpUtil asyncHttpUtil = new AsyncHttpUtil();
    //    LiteHttpUtil liteHttpUtil = new LiteHttpUtil();
        mHttpUtil = new HttpUtil(asyncHttpUtil);
    }

    public void getData(String url, final OnDataListener mOnDataListener){
        mHttpUtil.get(url, new OnNetWorkDataListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(Object res) {
                Log.i(tag, "---- " + res);
                testGson(String.valueOf(res), mOnDataListener);
            }

            @Override
            public void onError(Object error) {
                mOnDataListener.onError();
            }

            @Override
            public void onFinish() {
                mOnDataListener.onFinish();
            }
        });
    }


    private void testGson(String s, OnDataListener mOnDataListener){
        Gson gson = new Gson();
        Root root =  gson.fromJson(s, Root.class); // 根据类的成员变量名，直接反序列化 
        if(root != null){
            ShowapiResBody showapi_res_body = root.getShowapiResBody();
            if(showapi_res_body != null){
                String remark =  showapi_res_body.getRemark();
                Log.i(tag, ""+remark);
                List<Movies> datalists = showapi_res_body.getDatalist();
                if(datalists == null) return;
                for(Movies datalist : datalists){
                    Log.i(tag, datalist.getMovieName());
                }
            }
        }
        mOnDataListener.onSuccess(root, 0);
    }

}
