package cc.mnbase.util.okhttp;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-12-14
 * Time: 11:02
 * Version 1.0
 */

public class OKHttpCookie extends CookieManager {

    private SharedPreferences mSharedPreferences;
    private String cookieStr = null;
    private Map<String, ?> keysMap;

    private String tag = OKHttpCookie.class.getSimpleName();

    public OKHttpCookie(Context context){
        super(null, CookiePolicy.ACCEPT_ALL);
        mSharedPreferences = context.getSharedPreferences("ok_http_cookie", Context.MODE_ENABLE_WRITE_AHEAD_LOGGING);
        keysMap =  mSharedPreferences.getAll();
        Set<String> keys =  keysMap.keySet();
        StringBuffer sb = new StringBuffer();
        for(String key:keys){ //获取Cookie
            sb.append(key+"=");
            sb.append(mSharedPreferences.getString(key, null));
            sb.append("; ");
        }
        cookieStr = sb.toString();
        Log.i(tag, "-----cookie信息：" + cookieStr);
    }

    /**
     * 持久化Cookie
     * */
    private void saveCookie(CookieStore store){
        List<HttpCookie> httpCookies = store.getCookies();
        Log.i(tag, "------Cookie长度："+httpCookies.size());
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        for(HttpCookie httpCookie:httpCookies){ //保存到SharePreferences
            editor.putString(httpCookie.getName(), httpCookie.getValue());
        }
        editor.commit();
    }

    /**
     * 此方法自动调用
     * */
    @Override
    public void put(URI uri, Map<String, List<String>> responseHeaders) throws IOException {
        Log.i(tag, "------65----");
        super.put(uri, responseHeaders);
        Log.i(tag, "-----67----uri="+uri);
        //自动保存持久化Cookie
        CookieStore store = getCookieStore();
        Log.i(tag, "-----70---cookie=" + store.getCookies());
        saveCookie(store);
    }

    @Override
    public CookieStore getCookieStore() {
        Log.i(tag, "----76----cookie=" + super.getCookieStore().getCookies());
        return super.getCookieStore();
    }

    /**
     * 清除Cookie
     * */
    public void clearAllCookie(){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

    /**
     * 删除Cookie某个Item
     * @param key
     * */
    public void deleteCookie(String key){
        if(TextUtils.isEmpty(key)) return;
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(key, ""); //删除
        editor.commit();
    }

    /**
     * 获取组装好字符串Cookie
     * */
    public String getCookie() {
        return cookieStr;
    }
}
