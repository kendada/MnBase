package cc.mnbase.gson;

import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;

import java.io.File;
import java.util.Date;
import java.util.List;

import cc.mn.BaseActivity;
import cc.mnbase.gson.cache.CacheHelper;
import cc.mnbase.gson.model.Movies;
import cc.mnbase.gson.model.Root;
import cc.mnbase.gson.model.ShowapiResBody;
import cc.mnbase.util.MnDateUtil;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2016-01-13
 * Time: 14:27
 * Version 1.0
 */

public class TestGsonActivity extends BaseActivity {

    private String url = "https://route.showapi.com/578-7" +
            "?showapi_appid=5203" +
            "&showapi_timestamp=20160215160002" +
            "&showapi_sign=A84A65D7CF4F6DCB4E0210491117C430";

    CacheHelper helper = null;

    private String tag = TestGsonActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        helper = new CacheHelper(this);
        File[] files = helper.cacheDataList();
        if(files != null && files.length > 0){
            String res = helper.getCacheData(files[0].getPath());
            Log.i(tag, "----" + res);
        }
        initData();
    }


    private void initData(){
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int i, Header[] headers, String s, Throwable throwable) {

            }

            @Override
            public void onSuccess(int i, Header[] headers, String s) {
                Log.i(tag, "---- " + s);
                helper.createCache("cache_car" +
                        MnDateUtil.stringByFormat(new Date(), MnDateUtil.dateFormatFileName)+".json", s); // 缓存文件
                testGson(s);
            }
        });
    }


    private void testGson(String s){
        Gson gson = new Gson();
        Root root =  gson.fromJson(s, Root.class); // 根据类的成员变量名，直接反序列化
        if(root != null){
            ShowapiResBody showapi_res_body = root.getShowapiResBody();
            Log.i(tag, "----" + showapi_res_body);
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


        Gson gson1 = new Gson();

        String json = gson1.toJson(root, Root.class);
        Log.i(tag, "***  " + json);

    }





















}
