package cc.mn.view.refreshview.callback;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-12-09
 * Time: 14:50
 * Version 1.0
 */

public interface IHeaderCallback {

    /**
     * 正常状态
     * */
    void onStateNormal();

    /**
     * 准备刷新
     * */
    void onStateReady();

    /**
     * 正在刷新
     * */
    void onStateRefreshing();

    /**
     * 刷新结束
     * */
    void onStateFinish();

    /**
     * @param offset 移动距离和headerView高度比：0-1，0：headerView完全没有显示；1，完全显示；
     *
     * @param offsetY headerView的移动距离
     * */
    void onHeaderMove(double offset, int offsetY, int deltaY);

    /**
     * 设置最近一次下拉时间
     * */
    void setRefreshTime(long lastRefreshTime);

    /**
     * 隐藏HeaderView
     * */
    void hide();

    /**
     * 显示HeaderView
     * */
    void show();

    /**
     * 获取HeaderViewd的高度
     * */
    int getHeaderHeight();


}
