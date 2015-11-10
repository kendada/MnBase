package cc.mnbase.view.list.pull;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import cc.mnbase.R;

/**
 * User: 靳世坤(1203596603@qq.com)
 * Date: 2015-10-21
 * Time: 14:18
 * Version 1.0
 */

public class PullListView extends ListView implements AbsListView.OnScrollListener{

    private Context mContext;

    private final static int STATE_NONE = 0; //初始状态

    private final static int STATE_PULL = 1; //下拉状态

    private final static int STATE_RELEASE = 2; //下拉状态，松开之后执行STATE_REFRESHING

    private final static int STATE_REFRESHING = 3; //正在刷新，执行结束后进入STATE_NONE

    private View headerView;

    private final static int SPACE = 20;

    private int state = STATE_NONE;

    private int firstVisibleItem;

    private int scrollState;

    private int headerHeight;

    private int headerContentInitialHeight;

    private int startY = 0;

    private boolean isRecorded = false; //显示在首个Item才进行下拉刷新

    private String tag = "PullListView";

    public PullListView(Context context) {
        this(context, null);
    }

    public PullListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initViews();
    }

    private void initViews(){
        headerView = LayoutInflater.from(mContext).inflate(R.layout.pull_header_layout, null);

        headerContentInitialHeight = headerView.getPaddingTop();

        measureView(headerView);
        headerHeight = headerView.getMeasuredHeight();

        headerViewTopPadding(-headerHeight);

        this.addHeaderView(headerView);
    }

    private void headerViewTopPadding(int topPadding){
        headerView.setPadding(headerView.getLeft(), topPadding,
                headerView.getRight(), headerView.getBottom());
        headerView.invalidate();
    }

    /**
     * 测量View的宽高
     * */
    private void measureView(View view){
        if(view == null) return;

        ViewGroup.LayoutParams p = view.getLayoutParams();

        if(p == null){
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        int childWidthSpac = ViewGroup.getChildMeasureSpec(0, 0, p.width);

        int height = p.height;
        int childHeightSpac;
        if(height > 0){
            childHeightSpac = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        } else {
            childHeightSpac = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }

        view.measure(childWidthSpac, childHeightSpac);

    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(firstVisibleItem == 0){
                    isRecorded = true;
                    startY = (int)ev.getY();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                onMove(ev);
                break;
            case MotionEvent.ACTION_UP:
                if(state == STATE_PULL){
                    state = STATE_NONE;
                    refreshHeaderViewByState();
                } else if(state == STATE_RELEASE) {
                    state = STATE_REFRESHING;
                    refreshHeaderViewByState();
                    //
                }
                isRecorded = false;
                break;
        }

        return super.onTouchEvent(ev);
    }

    /**
     * 解析滑动状态
     * */
    private void onMove(MotionEvent event){
        if(!isRecorded){
            return;
        }

        int tmpY = (int) event.getY();
        int space = tmpY - startY;

        int topPadding = space - headerHeight;
        headerViewTopPadding(topPadding);
        switch (state){
            case STATE_NONE:
                if(space > 0){
                    state = STATE_PULL;
                    refreshHeaderViewByState();
                }
                break;
            case STATE_PULL:
                headerViewTopPadding(topPadding);
                if(scrollState==SCROLL_STATE_TOUCH_SCROLL && space>headerHeight+SPACE){
                    state = STATE_RELEASE;
                    refreshHeaderViewByState();
                }
                break;
            case STATE_RELEASE:
                headerViewTopPadding(topPadding);
                if(space>0 && space<headerHeight+SPACE){
                    state = STATE_PULL;
                    refreshHeaderViewByState();
                } else if(space<0) {
                    state = STATE_NONE;
                    refreshHeaderViewByState();
                }

                break;
        }

    }

    private void refreshHeaderViewByState(){
        switch (state){
            case STATE_NONE:
                headerViewTopPadding(-headerHeight);
                break;
            case STATE_PULL:

                break;
            case STATE_RELEASE:

                break;
            case STATE_REFRESHING:
                headerViewTopPadding(headerContentInitialHeight);
                break;
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        this.scrollState = scrollState;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.firstVisibleItem = firstVisibleItem;
    }
}
