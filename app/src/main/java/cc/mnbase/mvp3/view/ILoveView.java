package cc.mnbase.mvp3.view;

import cc.mnbase.mvp3.model.Data;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2016-02-15
 * Time: 14:22
 * Version 1.0
 */

public interface ILoveView {

    String getKey();

    int getTypeId();

    void toResult(Data data);

}
