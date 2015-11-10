package cc.mnbase.view.list.basepull;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * User: (1203596603@qq.com)
 * Date: 2015-10-21
 * Time: 22:55
 * Version 1.0
 */

public class RefreshListView extends RefreshAdaterView<ListView> {

    /**
     * @param context
     */
    public RefreshListView(Context context) {
        this(context, null);
    }

    /**
     * @param context
     * @param attrs
     */
    public RefreshListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * @param context
     * @param attrs
     * @param defStyle
     */
    public RefreshListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void setupContentView(Context context) {
        mContentView = new ListView(context);
        // 设置滚动监听器
       // mContentView.setOnScrollListener(this);

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
    public ListView getContentView() {
        return super.getContentView();
    }

}
