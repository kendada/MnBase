package cc.mnbase.util.okhttp;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-12-14
 * Time: 14:53
 * Version 1.0
 */

public interface OKHttpClientListener {

    void onStart();

    void onSuccess(int code, Object response);

    void onFinish(int code);

    void onFalure(int code, Object error);

}
