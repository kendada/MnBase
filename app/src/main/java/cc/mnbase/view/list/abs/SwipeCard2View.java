package cc.mnbase.view.list.abs;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-12-07
 * Time: 15:12
 * Version 1.0
 */

public class SwipeCard2View extends ViewGroup {

    private List<View> views = new ArrayList<>();

    private List<View> releasedViewList = new ArrayList<>();

    private List<CardItem> cardItems;

    private View currtView;

    private int index;

    private int mPostition = 0;

    private int offsetStep = 40; //每一层View的偏移量

    private float scale_step = 0.08f;

    private int max_slid_distance_linkage = 400; //水平距离+垂直距离

    private int width, height;

    private int isShowing = 0;

    private ViewDragHelper mDragHelper;

    private int childWidth = 0;

    private int initCenterViewX=0, initCenterViewY=0; //中间View的X,Y坐标

    private static final int X_VEL_THRESHOLD = 900;
    private static final int X_DISTANCE_THRESHOLD = 300;

    public static final int VANISH_TYPE_LEFT = 0;
    public static final int VANISH_TYPE_RIGHT = 1;


    private String tag = SwipeCard2View.class.getSimpleName();

    public SwipeCard2View(Context context) {
        this(context, null);
    }

    public SwipeCard2View(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public SwipeCard2View(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mDragHelper = ViewDragHelper.create(this, 10f, new DrawHelperCallback());
        mDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
    }

    /**
     * 滑动顶层View时，同时调整底层View
     * */
    private void processLinkageView(View changedView){
        int changeViewLeft = changedView.getLeft();
        int changeViewTop = changedView.getTop();
        int distance = Math.abs(changeViewTop - initCenterViewY) + Math.abs(changeViewLeft-initCenterViewX);

        float rate = distance / (float)max_slid_distance_linkage;
        float rate1 = rate;
        float rate2 = rate - 0.2f;

        if(rate > 1){
            rate1 = 1;
        }

        if(rate2 < 0){
            rate2 = 0;
        } else if(rate2 > 1) {
            rate1 = 1;
        }

        ajustLinkageViewItem(changedView, rate1, 1);
        ajustLinkageViewItem(changedView, rate2, 2);
    }

    private void ajustLinkageViewItem(View chagedView, float rate, int index){
        int chanageIndex = views.indexOf(chagedView);
        int initPosY = offsetStep * index;
        float initScale = 1 - scale_step * index;

        int nextPosY = offsetStep * (index - 1);
        float nextScale = 1 - scale_step * (index - 1);

        int offset = (int)(initPosY + (nextPosY - initPosY) * rate);
        float scale = initScale + (nextScale - initScale) *rate;

        View ajustView = views.get(chanageIndex + index);
        ajustView.offsetTopAndBottom(offset - ajustView.getTop() + initCenterViewY);
        ajustView.setScaleX(scale);
        ajustView.setScaleY(scale);
    }

    private class DrawHelperCallback extends ViewDragHelper.Callback{

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            int index = views.indexOf(changedView);
            if(index+2>views.size()){
                return;
            }
            processLinkageView(changedView);
        }

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            if(views.size()==0 || child.getVisibility()!=View.VISIBLE){
                return false;
            }
            int childIndex = views.indexOf(child);
            if(childIndex>0){
                return false;
            }
            return true;
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            return 256; //惯性滑行速度
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            animToSide(releasedChild, xvel, yvel);
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            return left;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            return top;
        }
    }

    /**
     * 松手时处理滑动到边缘的动画
     *
     * @param xvel X方向上的滑动速度
     */
    private void animToSide(View changedView, float xvel, float yvel) {
        int finalX = initCenterViewX;
        int finalY = initCenterViewY;
        int flyType = -1;

        // 1. 下面这一坨计算finalX和finalY
        int dx = changedView.getLeft() - initCenterViewX;
        int dy = changedView.getTop() - initCenterViewY;
        if (dx == 0) {
            // 由于dx作为分母，此处保护处理
            dx = 1;
        }
        if (xvel > X_VEL_THRESHOLD || dx > X_DISTANCE_THRESHOLD) {
            finalX = width;
            finalY = dy * (childWidth + initCenterViewX) / dx + initCenterViewY;
            flyType = VANISH_TYPE_RIGHT;
        } else if (xvel < -X_VEL_THRESHOLD || dx < -X_DISTANCE_THRESHOLD) {
            finalX = -childWidth;
            finalY = dy * (childWidth + initCenterViewX) / (-dx) + dy
                    + initCenterViewY;
            flyType = VANISH_TYPE_LEFT;
        }

        // 如果斜率太高，就折中处理
        if (finalY > height) {
            finalY = height;
        } else if (finalY < - height / 2) {
            finalY = -height / 2;
        }

        // 如果没有飞向两侧，而是回到了中间，需要谨慎处理
        if (finalX != initCenterViewX) {
            releasedViewList.add(changedView);
        }

        // 2. 启动动画
        if (mDragHelper.smoothSlideViewTo(changedView, finalX, finalY)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }


        // 3. 消失动画即将进行，listener回调
    //    if (flyType >= 0 && cardSwitchListener != null) {
    //        cardSwitchListener.onCardVanish(isShowing, flyType);
    //    }
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean shouldIntercept = mDragHelper.shouldInterceptTouchEvent(ev);
        int action = ev.getActionMasked();
        if(action == MotionEvent.ACTION_DOWN){
            orderViewStack();
            mDragHelper.processTouchEvent(ev);
        }
        return shouldIntercept;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        try {
            mDragHelper.processTouchEvent(event);
        } catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public void computeScroll() {
        if(mDragHelper.continueSettling(true)){
            ViewCompat.postInvalidateOnAnimation(this);
        } else {
            synchronized (this){
                if(mDragHelper.getViewDragState() == ViewDragHelper.STATE_IDLE){
                    orderViewStack();
                }
            }
        }
    }

    /**
     * 重新对View排序
     * */
    private void orderViewStack(){
        if(releasedViewList.size() == 0){
            return;
        }

        CardItemView changedView = (CardItemView)releasedViewList.get(0);
        if(changedView.getLeft() == initCenterViewX){
            return;
        }
        changedView.offsetLeftAndRight(initCenterViewX - changedView.getLeft());
        changedView.offsetTopAndBottom(initCenterViewY - changedView.getTop() + offsetStep * 2);
        float scale = 1.0f - scale_step * 2;

        changedView.setScaleX(scale);
        changedView.setScaleY(scale);

        //调整View的层次
        int num = views.size();
        for(int i=num-1; i>0; i--){
            View tempView = views.get(i);
            tempView.bringToFront();
        }

        int newIndex = isShowing + 4;
        if(newIndex < cardItems.size()){
            CardItem cardItem = cardItems.get(newIndex);
            changedView.setData(cardItem);
        } else {
            changedView.setVisibility(View.INVISIBLE);
        }

        views.remove(changedView);
        views.add(changedView);
        releasedViewList.remove(0);

        if(isShowing + 1 < cardItems.size()){
            isShowing++;
        }

        //


    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int size = views.size();
        for(int i=0; i<size; i++){
            View viewItem = views.get(i);
            int childHeight = viewItem.getMeasuredHeight();
            viewItem.layout(l, t, r, t + childHeight);
            int offset = offsetStep * i;
            float sclae = 1 - scale_step * i; // 缩放比例
            if(i > 2){  //第三层和第四层缩放比例一样
                offset = offsetStep * 2;
                sclae = 1 - scale_step * 2;
            }
            viewItem.offsetTopAndBottom(offset);
            viewItem.setScaleX(sclae);
            viewItem.setScaleY(sclae);
        }

        initCenterViewX = getChildAt(0).getLeft();
        initCenterViewY = getChildAt(0).getTop();
        childWidth = views.get(0).getMeasuredWidth();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        int maxWidth = MeasureSpec.getSize(widthMeasureSpec);
        int maxHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(
                resolveSizeAndState(maxWidth, widthMeasureSpec, 0),
                resolveSizeAndState(maxHeight, heightMeasureSpec, 0));

        width = getMeasuredWidth();
        height = getMeasuredHeight();
    }

    /**
     * View中的方法
     * */
    public static int resolveSizeAndState(int size, int measureSpec, int childMeasuredState) {
        int result = size;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize =  MeasureSpec.getSize(measureSpec);
        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
                result = size;
                break;
            case MeasureSpec.AT_MOST:
                if (specSize < size) {
                    result = specSize | MEASURED_STATE_TOO_SMALL;
                } else {
                    result = size;
                }
                break;
            case MeasureSpec.EXACTLY:
                result = specSize;
                break;
        }
        return result | (childMeasuredState&MEASURED_STATE_MASK);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        views.clear();
        int count = getChildCount();
        for(int i=count-1; i>=0; i--){
            CardItemView view = (CardItemView)getChildAt(i);
            view.setTag(i+1);
            views.add(view);
        }
    }

    public void setData(List<CardItem> data){
        cardItems = data;
        int count = views.size();
        for(int i=0; i<count; i++){
            CardItemView cardItemView = (CardItemView)views.get(i);
            cardItemView.setData(cardItems.get(i));
            cardItemView.setVisibility(View.VISIBLE);
        }
    }

}