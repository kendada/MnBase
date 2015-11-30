package cc.mnbase.view.test;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-11-20
 * Time: 11:37
 * Version 1.0
 */

public class MyLinearLayout extends ViewGroup{

    private String tag = MyLinearLayout.class.getSimpleName();

    public MyLinearLayout(Context context) {
        super(context);
    }

    public MyLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //记录总高度
        int mTotalHight = 0;
        int childCount = getChildCount();
        for(int i=0; i<childCount; i++){
            View childView = getChildAt(i);
            int measureHeight = childView.getMeasuredHeight();
            int measureWidth = childView.getMeasuredWidth();
            Log.i(tag, "---41----"+measureWidth+"-----"+measureHeight);
            childView.layout(0, mTotalHight, measureHeight, measureWidth);

            mTotalHight += measureHeight;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //计算控件的大小
        int measureHeight = measureHeight(heightMeasureSpec);
        int measureWidth = measureWidth(widthMeasureSpec);

        //计算ViewGroup子控件的大小
        measureChildren(measureWidth, measureHeight);
        //设置ViewGroup的大小
        setMeasuredDimension(measureWidth, measureHeight);


    }

    private int measureWidth(int widthMeasureSpec){
        int result = 0;
        int widthMode = MeasureSpec.getMode(widthMeasureSpec); //计算的模式
        Log.i(tag, "----70----"+widthMode);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec); //获取尺寸

        Log.i(tag, "---73----"+widthSize);

        switch (widthMode){ //三种模式
           // case MeasureSpec.UNSPECIFIED: //未指定尺寸

            case MeasureSpec.AT_MOST: //最大尺寸

            case MeasureSpec.EXACTLY: //精确尺寸
                result = widthSize;
                break;
        }
        return result;
    }

    private int measureHeight(int heightMeausreSpec){
        int result = 0;
        int heightMode = MeasureSpec.getMode(heightMeausreSpec); //计算模式
        int heightSize = MeasureSpec.getSize(heightMeausreSpec); //获取尺寸
        Log.i(tag, "-----91-----"+heightSize);
        switch (heightMode){
         //   case MeasureSpec.UNSPECIFIED: //未指定尺寸

            case MeasureSpec.AT_MOST:  //最大尺寸

            case MeasureSpec.EXACTLY:  //精确计算尺寸
                result = heightSize;
                break;
        }

        return result;
    }

}
