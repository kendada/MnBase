package cc.mnbase.view.list.menu;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

import cc.mnbase.view.list.basepull.RefreshAdaterView;

/**
 * User: (1203596603@qq.com)
 * Date: 2015-10-22
 * Time: 09:54
 * Version 1.0
 */

public class RefreshMenuListView extends RefreshAdaterView<MenuListView> {

    public RefreshMenuListView(Context context) {
        this(context, null);
    }

    public RefreshMenuListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshMenuListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void setupContentView(Context context) {
        mContentView = new MenuListView(context);
    }

    @Override
    protected boolean isTop() {
        return mContentView.getFirstVisiblePosition() == 0
                && getScrollY() <= mHeaderView.getMeasuredHeight();
    }

    @Override
    protected boolean isBottom() {
        return mContentView != null && mContentView.getAdapter() != null
                && mContentView.getLastVisiblePosition() ==
                mContentView.getAdapter().getCount() - 1;
    }

    @Override
    public MenuListView getContentView() {
        return super.getContentView();
    }
}
