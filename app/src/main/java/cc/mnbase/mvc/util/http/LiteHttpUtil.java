package cc.mnbase.mvc.util.http;

import android.text.TextUtils;
import android.util.Log;

import com.litesuits.http.LiteHttp;
import com.litesuits.http.exception.HttpException;
import com.litesuits.http.listener.HttpListener;
import com.litesuits.http.request.AbstractRequest;
import com.litesuits.http.request.StringRequest;
import com.litesuits.http.response.Response;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2016-01-18
 * Time: 19:03
 * Version 1.0
 */

public class LiteHttpUtil implements NetWork {

    private LiteHttp mLiteHttp = null;

    private String tag = LiteHttpUtil.class.getSimpleName();

    public LiteHttpUtil(){
        mLiteHttp = LiteHttp.newApacheHttpClient(null);
    }

    @Override
    public void get(String url, final OnNetWorkDataListener listener) {
        if(TextUtils.isEmpty(url) || listener == null) return;
        mLiteHttp.executeAsync(new StringRequest(url).setHttpListener(new HttpListener<String>() {
            @Override
            public void onStart(AbstractRequest<String> request) {
                listener.onStart();
            }

            @Override
            public void onSuccess(String s, Response<String> response) {
                Log.i(tag, tag + " onSuccess() = " + response.getResult());
                listener.onSuccess(response.getResult());
            }

            @Override
            public void onFailure(HttpException e, Response<String> response) {
                Log.i(tag, tag + " onFailure() = " + e);
                listener.onError(e);
            }

            @Override
            public void onEnd(Response<String> response) {
                Log.i(tag, tag + " onEnd() ");
                listener.onFinish();
            }
        }));
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
