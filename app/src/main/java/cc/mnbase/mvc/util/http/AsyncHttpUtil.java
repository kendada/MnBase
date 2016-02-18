package cc.mnbase.mvc.util.http;

import android.text.TextUtils;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2016-01-18
 * Time: 16:31
 * Version 1.0
 */

public class AsyncHttpUtil implements NetWork {

    @Override
    public void get(String url, final OnNetWorkDataListener listener) {
        if(TextUtils.isEmpty(url) || listener == null) return;
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new TextHttpResponseHandler() {
            @Override
            public void onStart() {
                listener.onStart();
            }

            @Override
            public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
                listener.onError(throwable);
            }

            @Override
            public void onSuccess(int i, Header[] headers, String s) {
                listener.onSuccess(s);
            }

            @Override
            public void onFinish() {
                listener.onFinish();
            }
        });
    }

    @Override
    public void get(String url, NetWorkParams params, OnNetWorkDataListener listener) {

    }

    @Override
    public void post(String url, OnNetWorkDataListener listener) {

    }

    @Override
    public void post(String url, NetWorkParams params, OnNetWorkDataListener listener) {

    }
}
