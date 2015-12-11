package cc.mn.view.refreshview.callback;

import cc.mn.view.refreshview.MNRefreshView;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-12-09
 * Time: 16:12
 * Version 1.0
 */

public interface IFooterCallBack {

    void callWhenNotAutoLoadMore(MNRefreshView.MNRefreshViewListener mnRefreshViewListener);

    void onStateReady();

    void onStateRefreshing();

    void onStateFinish();

    void onStateComplete();

    void hide();

    void show();

    public int getFooterHeight();


}
