package cc.mnbase.util.api;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

import java.util.Date;

import cc.mnbase.util.MnDateUtil;

/**
 * User: 靳世坤(1203596603@qq.com)
 * Date: 2015-08-17
 * Time: 21:14
 * Version 1.0
 */

public class ApiUtil  {

    public static final String URL = "http://route.showapi.com/126-2";

    public static void girlsData(int page, ResponseHandlerInterface handlerInterface){
        RequestParams params = new RequestParams();
        params.put("showapi_appid", "5207");
        params.put("showapi_sign", "b437410b3a564f74990ef0590dd99f83");
        params.put("showapi_timestamp", MnDateUtil.stringByFormat(new Date(), "yyyyMMddHHmmss"));
        params.put("showapi_sign_method", "md5");

        params.put("page", page);

        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.get(URL, params, handlerInterface);
    }
}
