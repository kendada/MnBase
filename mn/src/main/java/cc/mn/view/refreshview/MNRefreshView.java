package cc.mn.view.refreshview;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.LinearInterpolator;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.Scroller;

import java.util.Calendar;

import cc.mn.view.refreshview.callback.IFooterCallBack;
import cc.mn.view.refreshview.callback.IHeaderCallback;
import cc.mn.view.refreshview.listener.OnBottomLoadMoreTime;
import cc.mn.view.refreshview.listener.OnTopRefreshTime;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-12-09
 * Time: 11:01
 * Version 1.0
 */

public class MNRefreshView extends LinearLayout {

    private View mHeadView; //头部View

    private int mHeaderViewHeight; //头部高度

    private MNRefreshContntView mContentView; //内容View

    private View mFooterView; //页脚View

    private int mLastX = -1;
    private int mLastY = -1;

    private int mInitialMotionY;

    private int mTouchSlop; //最小滑动距离

    private Scroller mScroller;

    private MNRefreshHolder mHolder;

    private boolean mHasScrollBack = false;

    private MotionEvent mLastMoveEvent;

    private boolean isMatchParentWidth = true;

    private boolean isMatchParentHeight = true;

    private String tag = MNRefreshView.class.getSimpleName();

    private boolean mIsPinnedContentWhenRefreshing = true; //加载数据时是否可以滑动

    private boolean isIntercepted = false;
    private boolean mMoveForHorizontal = false;
    private boolean isForHorizontalMove = false;
    private int mHeadMoveDistence;
    private float OFFSET_RADIO = 1.8f;
    private boolean mEnablePullRefresh = true;
    private boolean mPullRefreshing = false;

    private boolean mHasSendCancelEvent = false;
    private boolean mHasSendDownEvent = false;

    private long lastRefreshTime = -1;

    private IHeaderCallback mHeaderCallBack;
    private IFooterCallBack mFooterCallBack;
    private boolean mHasLoadComplete;

    private boolean mEnablePullLoad;
    private boolean mPullLoading;

    //自动加载更多
    private boolean autoLoadMore = true;

    private MNRefreshViewState mState = null;

    private MNRefreshViewListener mRefreshViewListener;

    //滚动时间
    private int SCROLL_DURATION = 300;

    private boolean mIsIntercept = false;

    private Handler mHandler = new Handler();

    /**
     * 当刷新完成以后，headerview和footerview被固定的时间，在这个时间以后headerview才会回弹
     */
    private int mPinnedTime;

    /**
     * 默认不自动刷新
     */
    private boolean autoRefresh = false;
    private int mFootHeight;

    public enum MNRefreshViewState{
        STATE_READY, STATE_REFRESHING, STATE_NORMAL, STATE_LOADING, STATE_COMPLETE;
    }

    public MNRefreshView(Context context) {
        this(context, null);
    }

    public MNRefreshView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public MNRefreshView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL); //水平布局
        mHolder = new MNRefreshHolder();
        mContentView = new MNRefreshContntView();
        mScroller = new Scroller(context, new LinearInterpolator());
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

        initViews(context);
    }

    private void initViews(Context context){
        mHeadView = new MNRefreshHeaderView(context);
        mFooterView = new MNRefreshFooterView(context);
        this.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                setHeadView();
                setFooterView(this);
            }
        });
    }

    private void setHeadView(){
        this.addView(mHeadView, 0);
        mHeadView.measure(0, 0);
        mContentView.setContentView(this.getChildAt(1));
        if(autoLoadMore){
            mContentView.setContainer(this);
        } else {
            mContentView.setContainer(null);
        }
        mContentView.setContentViewLayoutParams(isMatchParentWidth, isMatchParentHeight);
        mHeaderCallBack = (IHeaderCallback)mHeadView;
        mFooterCallBack = (IFooterCallBack)mFooterView;
        checkPullLoadEnable();
        checkPullRefreshEnable();
    }

    private void setFooterView(OnGlobalLayoutListener listener){
        mHeaderViewHeight = ((IHeaderCallback)mHeadView).getHeaderHeight();
        mContentView.setHolder(mHolder);
        mContentView.setScrollListener();
        if(mEnablePullLoad){
            this.addView(mFooterView);
        }
        //移除视图监听器
        removeViewTreeObserver(listener);
        if(autoLoadMore){
            startRefresh();
        }
        if(mHeadMoveDistence == 0){
            int screenHeight = Utils.getScreenSize(getContext()).y;
            mHeadMoveDistence  = screenHeight / 3;
        }
    }

    public void setCustomHeaderView(View headerView){
        if(headerView instanceof IHeaderCallback){
            mHeadView = headerView;
        }
    }

    public void setCustomFooterView(View footerView){
        if(footerView instanceof IFooterCallBack){
            mFooterView = footerView;
        }
    }

    public void setPullRefreshEnable(boolean enable) {
        mEnablePullRefresh = enable;
    }

    /**
     * 设置静默加载更多，旨在提供被刷新的view滚动到底部的监听，自动静默加载更多
     */
    public void setSlienceLoadMore() {
        mContentView.setSlienceLoadMore(true);
        setPullLoadEnable(false);
    }

    public void setPullLoadEnable(boolean enable) {
        mEnablePullLoad = enable;
        setAutoLoadMore(false);
    }

    public void setMoveForHorizontal(boolean isForHorizontalMove) {
        this.isForHorizontalMove = isForHorizontalMove;
    }

    public void setFooterCallBack(IFooterCallBack footerCallBack) {
        mFooterCallBack = footerCallBack;
    }

    /**
     * 设置是否自动加载更多，默认是
     *
     * @param autoLoadMore true则自动刷新
     */
    public void setAutoLoadMore(boolean autoLoadMore) {
        this.autoLoadMore = autoLoadMore;
    }

    /**
     * 恢复上次刷新的时间
     *
     * @param lastRefreshTime
     */
    public void restoreLastRefreshTime(long lastRefreshTime) {
        this.lastRefreshTime = lastRefreshTime;
    }

    /**
     * 设置是否自动刷新，默认不自动刷新
     *
     * @param autoRefresh true则自动刷新
     */
    public void setAutoRefresh(boolean autoRefresh) {
        this.autoRefresh = autoRefresh;
        setRefreshTime();
    }

    public void setRefreshViewListener(MNRefreshViewListener listener) {
        this.mRefreshViewListener = listener;
        mContentView.setMnRefreshViewListener(listener);
    }

    /**
     * 在停止刷新的时候调用，记录这次刷新的时间，用于下次刷新的时候显示
     *
     * @return
     */
    public long getLastRefreshTime() {
        return lastRefreshTime;
    }

    public void startRefresh(){
        if(mHolder.mOffsetY != 0 || mContentView.isLoading() || !isEnabled()){
            return;
        }

        if(mHeaderCallBack == null){
            this.autoLoadMore = true;
        } else {
            if(!mPullRefreshing){
                updateHeaderHeight(0, mHeaderViewHeight, 0);
            }
            mPullRefreshing = true;
            if(mRefreshViewListener != null){
                mRefreshViewListener.onRefresh();
            }
            mContentView.scrollToTop();
        }
    }

    private void updateHeaderHeight(int currentY, int deltaY, int... during){
        boolean isAutoRefresh = during != null && during.length > 0;
        if(isAutoRefresh){
            mHeaderCallBack.onStateRefreshing();
            startScroll(deltaY, during[0]);
        } else {
            if(mHolder.isOverHeader(deltaY)){
                deltaY = -mHolder.mOffsetY;
            }
            moveView(deltaY);
            if(mEnablePullRefresh && !mPullRefreshing){
                if(mHolder.mOffsetY > mHeaderViewHeight){
                    if(mState != MNRefreshViewState.STATE_READY){
                        mHeaderCallBack.onStateReady();
                        mState = MNRefreshViewState.STATE_READY;
                    }
                } else {
                    if(mState != MNRefreshViewState.STATE_NORMAL){
                        mHeaderCallBack.onStateNormal();
                        mState = MNRefreshViewState.STATE_NORMAL;
                    }
                }
            }
        }
    }

    private void startLoadMore(){
        mPullLoading = true;
        if(mRefreshViewListener != null){
            mRefreshViewListener.onLoadMore(false);
        }
    }

    /**
     * 开始滑动
     * */
    public void startScroll(int offsetY, int duration){
        mHasScrollBack = true;
        if(offsetY != 0){
            mScroller.startScroll(0, mHolder.mOffsetY ,0 ,offsetY, duration);
            invalidate();
        }
    }

    /**
     * 移除视图树监听器
     * */
    public void removeViewTreeObserver(OnGlobalLayoutListener listener){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN){
            getViewTreeObserver().removeGlobalOnLayoutListener(listener);
        } else {
            getViewTreeObserver().removeOnGlobalLayoutListener(listener);
        }
    }

    public void setEnablePullRefresh(boolean enable){
        mEnablePullRefresh = enable;
    }

    private void checkPullRefreshEnable(){
        if(!mEnablePullRefresh){
            mHeaderCallBack.hide();
        } else {
            mHeaderCallBack.show();
        }
    }

    private void checkPullLoadEnable(){
        if(!mEnablePullLoad){
            mFooterCallBack.hide();
        } else {
            mPullLoading = false;
            mFooterCallBack.show();
            mFooterCallBack.onStateRefreshing();
        }
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if(mScroller.computeScrollOffset()){
            int lastScrollY = mHolder.mOffsetY;
            int currentY = mScroller.getCurrY();
            int offsetY = currentY - lastScrollY;
            lastScrollY = currentY;
            moveView(offsetY);
        } else {
            if(mHolder.mOffsetY == 0){
                mHasScrollBack = false;
            }
        }
    }

    public void moveView(int deltaY){
        mHolder.move(deltaY);
        mHeadView.offsetTopAndBottom(deltaY);
        mContentView.offsetTopAndBottom(deltaY);

        mFooterView.offsetTopAndBottom(deltaY);

        ViewCompat.postInvalidateOnAnimation(this);

        if(mRefreshViewListener != null && (mContentView.isTop() || mPullRefreshing)){
            double offset = 1.0f * mHolder.mOffsetY / mHeaderViewHeight;
            mRefreshViewListener.onHeaderMove(offset, mHolder.mOffsetY);
            mHeaderCallBack.onHeaderMove(offset, mHolder.mOffsetY, deltaY);
        }

    }

    public boolean hasLoadCompleted() {
        return mHasLoadComplete;
    }

    public boolean invoketLoadMore() {
        if(mEnablePullLoad && !mPullLoading && !mPullRefreshing && !mHasScrollBack && !mHasLoadComplete){
            mFooterCallBack.onStateRefreshing();
            int offset = -mHolder.mOffsetY - mFootHeight;
            startScroll(offset, SCROLL_DURATION);
            startLoadMore();
            return true;
        }
        return false;
    }

    private void sendCancelEvent(){
      if(!mHasSendCancelEvent){
          setRefreshTime();
          mHasSendCancelEvent = true;
          mHasSendDownEvent = false;
          MotionEvent last = mLastMoveEvent;

          MotionEvent e = MotionEvent.obtain(last.getDownTime(), last.getEventTime()+ViewConfiguration.getLongPressTimeout(),
                  MotionEvent.ACTION_CANCEL, last.getX(), last.getY(), last.getMetaState());
        dispatchTouchEventSupper(e);
      }
    }

    private boolean dispatchTouchEventSupper(MotionEvent e) {
        return super.dispatchTouchEvent(e);
    }

    /**
     * 设置并显示上次刷新的时间
     */
    private void setRefreshTime() {
        if (lastRefreshTime <= 0) return;
        if(mHeaderCallBack == null) return;
        mHeaderCallBack.setRefreshTime(lastRefreshTime);
    }

    /**
     * header可下拉的最大距离
     *
     * @param headMoveDistence
     */
    public void setHeadMoveLargestDistence(int headMoveDistence) {
        mHeadMoveDistence = headMoveDistence;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        int deltaX = 0;
        int deltaY = 0;
        switch (action){
            case MotionEvent.ACTION_DOWN:
                mHasSendCancelEvent = false;
                mHasSendDownEvent = false;
                mLastX = (int)ev.getRawX();
                mLastY = (int)ev.getRawY();

                mInitialMotionY = mLastY;
                break;
            case MotionEvent.ACTION_MOVE:
                mLastMoveEvent = ev;
                if(mPullLoading || mPullRefreshing || !isEnabled() || mIsIntercept || mHasScrollBack || mContentView.isLoading()){
                    if(mIsPinnedContentWhenRefreshing){
                        return super.dispatchTouchEvent(ev);
                    } else {
                        sendCancelEvent();
                        return true;
                    }
                }
                int currentY = (int) ev.getRawY();
                int currentX = (int) ev.getRawX();
                deltaY = currentY - mLastY;
                deltaX = currentX - mLastX;
                mLastY = currentY;
                mLastX = currentX;
                if(!isIntercepted && Math.abs(deltaY) < mTouchSlop){
                    isIntercepted = true;
                    return super.dispatchTouchEvent(ev);
                }
                if(isForHorizontalMove && !mMoveForHorizontal && Math.abs(deltaX)>mTouchSlop && Math.abs(deltaX) > Math.abs(deltaY)){
                    if(mHolder.mOffsetY == 0){
                        mMoveForHorizontal = true;
                    }
                }
                if(mMoveForHorizontal){
                    return super.dispatchTouchEvent(ev);
                }

                if(deltaY > 0 && mHolder.mOffsetY <= mHeadMoveDistence || deltaY < 0){
                    deltaY = (int)(deltaY / OFFSET_RADIO);
                } else {
                    deltaY = 0;
                }
                if(mContentView.isTop() && (deltaY > 0 || (deltaY < 0 && mHolder.hasHeaderPullDown()))){
                    sendCancelEvent();
                    updateHeaderHeight(currentY, deltaY);
                } else if(mContentView.isBottom() && (deltaY < 0 || deltaY > 0 && mHolder.hasFooterPullUp())){
                    sendCancelEvent();
                    updateFooterHeight(deltaY);
                } else if (mContentView.isTop() && !mHolder.hasHeaderPullDown() ||
                        mContentView.isBottom() && !mHolder.hasFooterPullUp()){
                    if(Math.abs(deltaY) > 0){
                        sendDownEvent();
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if(mHolder.hasHeaderPullDown() && !mHasScrollBack){
                    if(mEnablePullRefresh && mHolder.mOffsetY > mHeaderViewHeight){
                        mPullRefreshing = true;
                        mHeaderCallBack.onStateRefreshing();
                        mState = MNRefreshViewState.STATE_REFRESHING;
                        if(mRefreshViewListener != null){
                            mRefreshViewListener.onRefresh();
                        }
                    }
                    resetHeaderHeight();
                } else if (mHolder.hasFooterPullUp()){
                    if(mEnablePullLoad && !mHasLoadComplete){
                        invoketLoadMore();
                    } else {
                        int offset = 0 - mHolder.mOffsetY;
                        startScroll(offset, SCROLL_DURATION);
                    }
                }
                mLastY = -1;
                mInitialMotionY = 0;
                isIntercepted = true;
                mMoveForHorizontal = false;
                mIsIntercept =  false;
                break;
        }

        return super.dispatchTouchEvent(ev);
    }

    public void disallowInterceptTouchEvent(boolean isIntercept) {
        mIsIntercept = isIntercept;
    }

    /**
     * 设置在刷新的时候是否可以移动contentView
     *
     * @param isPinned true 固定不移动 反之，可以移动
     */
    public void setPinnedContent(boolean isPinned) {
        mIsPinnedContentWhenRefreshing = !isPinned;
    }

    private void sendDownEvent(){
        if(!mHasSendDownEvent){
            mHasSendCancelEvent = false;
            mHasSendDownEvent = true;
            isIntercepted = false;
            final MotionEvent last = mLastMoveEvent;
            if(last == null) {
                return;
            }
            MotionEvent e = MotionEvent.obtain(last.getDownTime(), last.getEventTime(), MotionEvent.ACTION_DOWN,
                    last.getX(), last.getY(), last.getMetaState());
            dispatchTouchEventSupper(e);
        }
    }

    private void updateFooterHeight(int deltaY){
        if(mState != MNRefreshViewState.STATE_READY && !autoLoadMore){
            mFooterCallBack.onStateReady();
            mState = MNRefreshViewState.STATE_READY;
        }
        moveView(deltaY);
    }

    /**
     * 头部回滚时间：默认400毫秒
     * */
    public void setScrollDuring(int during){
        SCROLL_DURATION = during;
    }

    /**
     * 设置阻尼系数:默认1.8
     * */
    public void setDampingRatio(float ratio){
        OFFSET_RADIO = ratio;
    }

    /**
     * 设置当下拉刷新完成以后，headerview和footerview被固定的时间
     * 注:考虑到ui效果，只有时间大于1s的时候，footerview被固定的效果才会生效
     * */
    public void setPinnedTime(int pinnedTime){
        mPinnedTime = pinnedTime;
        mContentView.setPinnedTime(pinnedTime);
    }

    /**
     * 设置Abslistview的滚动监听事件
     *
     * @param scrollListener
     */
    public void setOnAbsListViewScrollListener(AbsListView.OnScrollListener scrollListener) {
        mContentView.setOnAbsListViewScrollListener(scrollListener);
    }

    /**
     * 设置顶部刷新时机
     *
     * @param topListener
     */
    public void setOnTopRefreshTime(OnTopRefreshTime topListener) {
        mContentView.setOnTopRefreshTime(topListener);
    }

    public void setOnBottomLoadMoreTime(OnBottomLoadMoreTime bottomListener) {
        mContentView.setOnBottomLoadMoreTime(bottomListener);
    }

    public void stopRefresh(){
        if(mPullRefreshing){
            mPullRefreshing = false;
            mHeaderCallBack.onStateRefreshing();
            mState = MNRefreshViewState.STATE_COMPLETE;
            mHasScrollBack = true;
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    resetHeaderHeight();
                    lastRefreshTime = Calendar.getInstance().getTimeInMillis();
                }
            }, mPinnedTime);
        }
    }

    private void resetHeaderHeight(){
        float height = mHolder.mOffsetY;
        if(height == 0) return;
        if(mPullRefreshing && height <= mHeaderViewHeight){
            return;
        }
        int offsetY = 0;
        if(mPullRefreshing){
            offsetY = mHeaderViewHeight - mHolder.mOffsetY;
            startScroll(offsetY, SCROLL_DURATION);
        } else {
            offsetY = -mHolder.mOffsetY;
            startScroll(offsetY, SCROLL_DURATION);
        }
    }

    public void stopLoadMore(){
        if(mPullLoading){
            mPullLoading = false;
            mFooterCallBack.onStateFinish();
            mState = MNRefreshViewState.STATE_COMPLETE;
            if(mPinnedTime >= 1000){
                mHasScrollBack = true;
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        endLoadMore();
                    }
                }, mPinnedTime);
            } else {
                endLoadMore();
            }
        }
        mContentView.stopLoading();
    }

    public void endLoadMore(){
        startScroll(-mHolder.mOffsetY, 0);
        mFooterCallBack.onStateRefreshing();
        if(mHasLoadComplete){
            mFooterCallBack.hide();
        }
    }

    public void setLoadComplete(boolean hasComplete){
        mHasLoadComplete = hasComplete;
        stopLoadMore();
        if(!hasComplete){
            mFooterCallBack.onStateRefreshing();
            mFooterCallBack.show();
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int childCount = getChildCount();
        int finalHeight = 0;
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != View.GONE) {
                measureChild(child, widthMeasureSpec, heightMeasureSpec);
                finalHeight += child.getMeasuredHeight();
            }
        }
        setMeasuredDimension(width, finalHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        mFootHeight = ((IFooterCallBack) mFooterView).getFooterHeight();
        int childCount = getChildCount();
        int top = getPaddingTop() + mHolder.mOffsetY;
        int adHeight = 0;
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != View.GONE) {
                if (i == 0) {
                    adHeight = child.getMeasuredHeight() - mHeaderViewHeight;
                    child.layout(0, top - mHeaderViewHeight,
                            child.getMeasuredWidth(), top + adHeight);
                    top += adHeight;
                } else if (i == 1) {
                    int childHeight = child.getMeasuredHeight() - adHeight;
                    child.layout(0, top, child.getMeasuredWidth(), childHeight
                            + top);
                    top += childHeight;
                } else {
                    child.layout(0, top, child.getMeasuredWidth(),
                            child.getMeasuredHeight() + top);
                    top += child.getMeasuredHeight();
                }
            }
        }
    }

    public interface MNRefreshViewListener{

        void onRefresh();

        void onLoadMore(boolean isSlience);

        void onRelease(float direction);

        void onHeaderMove(double offset, int offsetY);
    }

    public static class SimpleMNRefreshViewListener implements MNRefreshViewListener{

        @Override
        public void onRefresh() {

        }

        @Override
        public void onLoadMore(boolean isSlience) {

        }

        @Override
        public void onRelease(float direction) {

        }

        @Override
        public void onHeaderMove(double offset, int offsetY) {

        }
    }



}
