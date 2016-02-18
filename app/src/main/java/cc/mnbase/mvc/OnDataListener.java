package cc.mnbase.mvc;

import cc.mnbase.mvc.model.Root;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2016-01-18
 * Time: 10:32
 * Version 1.0
 */

public interface OnDataListener {
    void onSuccess(Root root, int page);
    void onFinish();
    void onError();
}
