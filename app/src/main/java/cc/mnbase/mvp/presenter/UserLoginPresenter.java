package cc.mnbase.mvp.presenter;

import cc.mnbase.mvc.ApiUtil;
import cc.mnbase.mvc.OnDataListener;
import cc.mnbase.mvc.model.Root;
import cc.mnbase.mvp.view.IUserLoginView;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2016-01-19
 * Time: 15:21
 * Version 1.0
 */

public class UserLoginPresenter {

    private IUserLoginView iUserLoginView;

    private ApiUtil mApiUtil;

    private String url = "https://route.showapi.com/578-7" +
            "?showapi_appid=5203";

    public UserLoginPresenter(IUserLoginView iUserLoginView){
        this.iUserLoginView = iUserLoginView;
        mApiUtil = new ApiUtil();
    }

    public void login(){
        url = url + "&showapi_timestamp=" + iUserLoginView.getUserName() +
                "&showapi_sign=" + iUserLoginView.getPassWord();
        iUserLoginView.showLogining();
        mApiUtil.getData(url, new OnDataListener() {
            @Override
            public void onSuccess(Root root, int page) {
                iUserLoginView.toMainActivity(root);
            }

            @Override
            public void onFinish() {
                iUserLoginView.hideLogin();
            }

            @Override
            public void onError() {
                iUserLoginView.showFailedError();
            }
        });
    }

    public void clear(){
        iUserLoginView.clearUserName();
        iUserLoginView.clearPassWord();
    }


}
