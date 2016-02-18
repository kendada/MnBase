package cc.mnbase.mvc.util.http;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2016-01-18
 * Time: 16:26
 * Version 1.0
 */

public interface OnNetWorkDataListener {

    void onStart();

    void onSuccess(Object res);

    void onError(Object error);

    void onFinish();
}
