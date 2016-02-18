package cc.mnbase.mvp2.util;

import cc.mnbase.mvc.util.http.AsyncHttpUtil;
import cc.mnbase.mvc.util.http.HttpUtil;
import cc.mnbase.mvc.util.http.LiteHttpUtil;
import cc.mnbase.mvc.util.http.OnNetWorkDataListener;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2016-01-20
 * Time: 11:02
 * Version 1.0
 */

public class ApiUtil {

    private HttpUtil mHttpUtil;

    public ApiUtil(){
        //AsyncHttpUtil asyncHttpUtil = new AsyncHttpUtil();

        LiteHttpUtil asyncHttpUtil = new LiteHttpUtil();

        mHttpUtil = new HttpUtil(asyncHttpUtil);
    }

    public void getAirData(String url, OnNetWorkDataListener listener){
        mHttpUtil.get(url, listener);
    }

}
