package cc.mnbase.view.list.menu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * User: (1203596603@qq.com)
 * Date: 2015-10-20
 * Time: 09:30
 * Version 1.0
 */

public class MenuLinearLayout extends LinearLayout{

    private int menu_width;

    private int count_width;

    private int mDownX, downX; //初始按下的位置

    private final static int STATE_CLOSE = 0;

    private final static int STATE_OPEN = 1;

    private int state = STATE_CLOSE; //当前Item状态，2种：STATE_CLOSE，STATE_OPEN

    private String tag = "MenuLinearLayout";

    public MenuLinearLayout(Context context) {
        this(context, null);
    }

    public MenuLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int count = getChildCount();
        for(int i=0; i<count; i++){
            View view = getChildAt(i);
            measureChild(view, widthMeasureSpec, heightMeasureSpec);
            if(i==1){
                menu_width = view.getMeasuredWidth();
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int margeLeft = 0;
        int size = getChildCount();
        for(int i=0; i<size; i++){
            View view = getChildAt(i);
            if(view.getVisibility()!=View.GONE){
                int childWidth = view.getMeasuredWidth();
                view.layout(margeLeft, 0, margeLeft+childWidth, view.getMeasuredHeight());

                margeLeft += childWidth;
            }
        }
    }

    public boolean onSwipe(MotionEvent event){
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mDownX = (int)event.getX();
                downX = mDownX;
                break;
            case MotionEvent.ACTION_MOVE:
                int newX = mDownX - (int)event.getX();
                count_width += newX;
                mDownX = (int)event.getX();
                swipe(newX);
                break;
            case MotionEvent.ACTION_UP:
                if(downX-event.getX() > menu_width/2){
                    openMenu();
                } else {
                    closeMenu();
                }
                break;
        }
        return true;
    }

    private void swipe(int x){
        if(count_width >= menu_width){
            return;
        }

        if(count_width < 0){
            return;
        }

        this.scrollBy(x, 0); //跟随滑动
    }

    public void openMenu(){
        this.scrollTo(menu_width, 0);
        state = STATE_OPEN;
        count_width = menu_width;
    }

    public void closeMenu(){
        this.scrollTo(0, 0);
        state = STATE_CLOSE;
        count_width = 0;
    }

    public boolean isOpen(){
        return state == STATE_OPEN;
    }

    /**
     * 获取滑出菜单的宽度
     * */
    public int getMenu_width(){
        return this.menu_width;
    }

}
