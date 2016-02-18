package cc.mnbase.mvp.view;

import cc.mnbase.mvc.model.Root;
import cc.mnbase.mvp.mvp.User;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2016-01-19
 * Time: 15:18
 * Version 1.0
 */

public interface IUserLoginView {

    String getUserName();

    String getPassWord();

    void clearUserName();

    void clearPassWord();

    void showLogining();

    void hideLogin();

    void toMainActivity(Root root);

    void showFailedError();
}
