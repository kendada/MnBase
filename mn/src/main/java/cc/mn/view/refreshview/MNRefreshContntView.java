package cc.mn.view.refreshview;

import android.os.Handler;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;

import cc.mn.view.refreshview.callback.IFooterCallBack;
import cc.mn.view.refreshview.listener.OnBottomLoadMoreTime;
import cc.mn.view.refreshview.listener.OnTopRefreshTime;
import cc.mn.view.refreshview.MNRefreshView.MNRefreshViewListener ;
import cc.mn.view.refreshview.MNRefreshView.MNRefreshViewState;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-12-09
 * Time: 16:44
 * Version 1.0
 */

public class MNRefreshContntView implements OnScrollListener, OnBottomLoadMoreTime, OnTopRefreshTime {

    private View child;

    private int mTotalItemCount;

    private OnTopRefreshTime mOnTopRefreshTime;

    private OnBottomLoadMoreTime mOnBottomLoadMoreTime;

    private MNRefreshView mContainer;

    private OnScrollListener mAbsListViewOnScrollListener;

    private MNRefreshViewListener mnRefreshViewListener;

    private int mFirstVisibleItem = 0;
    private int previousTotal = 0;
    private int mVisibleItemCount;

    private int mLastVisibleItemPosition;
    private boolean mIsLoadingMore;
    private IFooterCallBack mIFooterCallBack;

    private Handler mHandler = new Handler();

    private boolean mHasLoadComplete;
    private int mPinnedTime;
    private MNRefreshHolder mHolder;

    private MNRefreshViewState mState = MNRefreshViewState.STATE_NORMAL;

    //是否进行静默加载
    private boolean mSlienceLoadMore = false;
    //进行静默加载时，提前加载Item的个数
    private int mPreLoadCount;

    private String tag = MNRefreshContntView.class.getSimpleName();

    public void setContentViewLayoutParams(boolean isMacthParentHeight, boolean isMacthParentWidth){
        LayoutParams lp = (LayoutParams)child.getLayoutParams();
        if(isMacthParentHeight){
            lp.height = LayoutParams.MATCH_PARENT;
        }

        if(isMacthParentWidth){
            lp.width = LayoutParams.MATCH_PARENT;
        }

        child.setLayoutParams(lp);
    }

    public void setContentView(View view){
        child = view;
    }

    public View getContentView(){
        return child;
    }

    public void setHolder(MNRefreshHolder holder){
        mHolder = holder;
    }

    /**
     * 设置container！=null 就会自动加载更多
     * */
    public void setContainer(MNRefreshView container){
        mContainer = container;
    }

    public void scrollToTop(){
        if(child instanceof AbsListView){
            AbsListView absListView = (AbsListView)child;
            absListView.setSelection(0);
        } //其他控件进行扩展
    }

    /**
     * 设置是否进行静默加载
     * */
    public void setSlienceLoadMore(boolean slienceLoadMore){
        mSlienceLoadMore = slienceLoadMore;
    }

    /**
     * 设置监听器
     * */
    public void setScrollListener(){
        if(child instanceof AbsListView){
            AbsListView absListView = (AbsListView)child;
            absListView.setOnScrollListener(this);
        }
        //其他控件进行扩展
    }

    public void setOnAbsListViewScrollListener(OnScrollListener listener) {
        mAbsListViewOnScrollListener = listener;
    }

    /**
     * 设置需要静默加载的Item个数
     * */
    public void setPreLoadCount(int preLoadCount){
        if(preLoadCount < 0 ) preLoadCount = 0;
        mPreLoadCount = preLoadCount;
    }

    /**
     * 加载完成隐藏底部
     * */
    public void loadCompleted(){
        if(mState!=MNRefreshViewState.STATE_COMPLETE){
            mIFooterCallBack.onStateComplete();
            mState = MNRefreshViewState.STATE_COMPLETE;
            mPinnedTime = mPinnedTime < 1000 ? 1000:mPinnedTime;
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mIFooterCallBack.hide();
                }
            }, mPinnedTime);
        }
    }

    public void setLoadComplete(boolean hasComplete){
        mHasLoadComplete = hasComplete;
        if(!hasComplete){
            mIFooterCallBack.show();
            mIsLoadingMore = false;
        }
    }

    public void setPinnedTime(int pinnedTime){
        mPinnedTime = pinnedTime;
    }

    public void setAbsListViewOnScrollListener(OnScrollListener listener){
        mAbsListViewOnScrollListener = listener;
    }

    public void setMnRefreshViewListener(MNRefreshViewListener listener){
        mnRefreshViewListener = listener;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if(mSlienceLoadMore){
            if(mnRefreshViewListener!=null && mContainer !=null && !mContainer.hasLoadCompleted() &&
                    !mIsLoadingMore && mTotalItemCount-1 <= view.getLastVisiblePosition()+mPreLoadCount){
                mnRefreshViewListener.onLoadMore(true);
                mIsLoadingMore = true;
            }
        } else if (mContainer != null && !mContainer.hasLoadCompleted() &&
                scrollState == OnScrollListener.SCROLL_STATE_IDLE &&
                mTotalItemCount - 1 <= view.getLastVisiblePosition()+mPreLoadCount) {
            if(!mIsLoadingMore){
                mIsLoadingMore = mContainer.invoketLoadMore();
            }
        }

        if(mAbsListViewOnScrollListener!=null){
            mAbsListViewOnScrollListener.onScrollStateChanged(view, scrollState);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        mTotalItemCount = totalItemCount;
        if(mAbsListViewOnScrollListener!=null){
            mAbsListViewOnScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
    }

    @Override
    public boolean isTop() {
        if(mOnTopRefreshTime!=null){
            return mOnTopRefreshTime.isTop();
        }
        return hasChildOnTop();
    }

    @Override
    public boolean isBottom() {
        if(mOnBottomLoadMoreTime!=null){
            return mOnBottomLoadMoreTime.isBottom();
        }
        return hasChildOnBottom();
    }

    public void setOnTopRefreshTime(OnTopRefreshTime onTopRefreshTime){
        mOnTopRefreshTime = onTopRefreshTime;
    }

    public void setOnBottomLoadMoreTime(OnBottomLoadMoreTime onBottomLoadMoreTime){
        mOnBottomLoadMoreTime = onBottomLoadMoreTime;
    }

    public boolean hasChildOnTop(){
        return !canChildPullDown();
    }

    public boolean hasChildOnBottom(){
        return !canChildPullUp();
    }

    public boolean isLoading(){
        if(mSlienceLoadMore){
            return false;
        }
        return mSlienceLoadMore;
    }

    public void stopLoading(){
        mSlienceLoadMore = true;
        mTotalItemCount = 0;
    }

    public boolean canChildPullDown(){
        if(child instanceof AbsListView){
            final AbsListView absListView = (AbsListView)child;
            return canScrollVerticaly(child, -1)
                    || absListView.getChildCount() > 0
                    &&(absListView.getFirstVisiblePosition()>0 || absListView.getChildAt(0).getTop() < absListView.getPaddingTop());
        } else {
            return canScrollVerticaly(child, -1) || child.getScaleY() > 0;
        }
    }

    public boolean canChildPullUp(){
        if(child instanceof AbsListView){
            AbsListView absListView = (AbsListView)child;
            return canScrollVerticaly(child, 1) || absListView.getLastVisiblePosition() != mTotalItemCount - 1;
        } else if (child instanceof WebView){
            WebView webView = (WebView)child;
            return canScrollVerticaly(child, 1)
                    || webView.getContentHeight() * webView.getScale() != webView.getHeight() + webView.getScaleY();
        } else if (child instanceof ScrollView){
            ScrollView scrollView = (ScrollView)child;
            View childView = scrollView.getChildAt(0);
            if(childView != null){
                return canScrollVerticaly(child, 1)
                        || scrollView.getScaleY() != childView.getHeight() - scrollView.getHeight();
            }
        } else {
            return canScrollVerticaly(child, 1);
        }

        return true;
    }

    /**
     * 用来判断view在竖直方向上能不能向上或者向下滑动
     *
     * @param view      v
     * @param direction 方向 负数代表向上滑动 ，正数则反之
     * @return
     */
    public boolean canScrollVerticaly(View view, int direction){
        return ViewCompat.canScrollVertically(view, direction);
    }

    public void offsetTopAndBottom(int offset){
        child.offsetTopAndBottom(offset);
    }

    public int getTotalItemCount(){
        return mTotalItemCount;
    }


}
