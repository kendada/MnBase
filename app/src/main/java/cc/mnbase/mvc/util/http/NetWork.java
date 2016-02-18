package cc.mnbase.mvc.util.http;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2016-01-18
 * Time: 16:23
 * Version 1.0
 */

public interface NetWork {

    void get(String url, OnNetWorkDataListener listener);

    void get(String url, NetWorkParams params, OnNetWorkDataListener listener);

    void post(String url, OnNetWorkDataListener listener);

    void post(String url, NetWorkParams params, OnNetWorkDataListener listener);
}
