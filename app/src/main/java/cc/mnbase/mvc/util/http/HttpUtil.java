package cc.mnbase.mvc.util.http;


import android.text.TextUtils;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2016-01-18
 * Time: 15:49
 * Version 1.0
 */

public class HttpUtil  {

    private NetWork mNetWork;

    public HttpUtil(NetWork netWork){
        mNetWork = netWork;
    }

    public void get(String url, OnNetWorkDataListener listener){
        if(!isExecute(url, mNetWork, listener)) return;
        mNetWork.get(url, listener);
    }

    public void post(String url, OnNetWorkDataListener listener){
        if(!isExecute(url, mNetWork, listener)) return;
        mNetWork.post(url, listener);
    }

    public void get(String url, NetWorkParams params, OnNetWorkDataListener listener) {
        if(!isExecute(url, mNetWork, listener)) return;
        mNetWork.get(url, params, listener);
    }

    public void post(String url, NetWorkParams params, OnNetWorkDataListener listener) {
        if(!isExecute(url, mNetWork, listener)) return;
        mNetWork.post(url, params, listener);
    }

    private boolean isExecute(String url, NetWork netWork, OnNetWorkDataListener listener){
        if(TextUtils.isEmpty(url) || netWork == null || listener == null) {
            return false;
        } else {
            return true;
        }
    }

}
