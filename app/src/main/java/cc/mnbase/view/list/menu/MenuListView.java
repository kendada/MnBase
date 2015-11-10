package cc.mnbase.view.list.menu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

/**
 * User: 靳世坤(1203596603@qq.com)
 * Date: 2015-10-20
 * Time: 15:32
 * Version 1.0
 */

public class MenuListView extends ListView {

    private final static int TOUCH_STATE_NONE = 0; //没有滑动

    private final static int TOUCH_STATE_X = 1; //水平滑动

    private final static int TOUCH_STATE_Y = 2; //垂直滑动

    private int mTouchState = TOUCH_STATE_NONE; //滑动状态

    private int downX, downY;

    private MenuLinearLayout itemView;

    private int mTouchPosition = 0; //触摸Item的位置

    private int MAX_Y = 5; //y方向的滑动阀值

    private int MAX_X = 3; //x方向的滑动阀值

    private String tag = "MenuListView";

    public MenuListView(Context context) {
        super(context);
    }

    public MenuListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MenuListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(ev.getAction() !=   MotionEvent.ACTION_DOWN && itemView==null){
            return super.onTouchEvent(ev);
        }
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                downX = (int)ev.getX();
                downY = (int)ev.getY();

                int oldPos = mTouchPosition;

                mTouchState = TOUCH_STATE_NONE;
                //根据系统方法获取触摸位置
                mTouchPosition = pointToPosition((int)ev.getX(), (int)ev.getY());
                if(mTouchPosition == oldPos && itemView!=null && itemView.isOpen()){
                    mTouchState = TOUCH_STATE_X;
                    return true;
                }

                View view = getChildAt(mTouchPosition - getFirstVisiblePosition());

                if(itemView!=null && itemView.isOpen()){
                    itemView.closeMenu();
                    itemView = null;
                    return super.onTouchEvent(ev);
                }

                if(view instanceof MenuLinearLayout){
                    itemView = (MenuLinearLayout)view;
                }

                if(itemView!=null){
                    itemView.onSwipe(ev);
                }

                break;
            case MotionEvent.ACTION_MOVE:
                float dy = Math.abs(ev.getY() - downY);
                float dx = Math.abs(ev.getX() - downX);

                if(mTouchState == TOUCH_STATE_X){
                    if(itemView!=null){
                        itemView.onSwipe(ev);
                    }

                    ev.setAction(MotionEvent.ACTION_CANCEL);
                    super.onTouchEvent(ev);
                    return true;
                } else if(mTouchState == TOUCH_STATE_NONE) {
                    if(Math.abs(dy) > MAX_Y){
                        mTouchState = TOUCH_STATE_Y;
                    } else if(dx > MAX_X){
                        mTouchState = TOUCH_STATE_X;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if(mTouchState == TOUCH_STATE_X) {
                    if(itemView!=null){
                        itemView.onSwipe(ev);
                        if(!itemView.isOpen()){
                            mTouchPosition = -1;
                            itemView = null;
                        }
                    }

                    ev.setAction(MotionEvent.ACTION_CANCEL);
                    super.onTouchEvent(ev);
                    return true;
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

}
