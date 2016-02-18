package cc.mnbase.jianrong;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import cc.mnbase.jianrong.utils.AutoLayoutHelper;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2016-01-11
 * Time: 14:22
 * Version 1.0
 */

public class AutoLinearLayout extends LinearLayout {

    private AutoLayoutHelper mHelper;

    private String tag = AutoLinearLayout.class.getSimpleName();

    public AutoLinearLayout(Context context) {
        this(context, null);
    }

    public AutoLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mHelper = new AutoLayoutHelper(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(!isInEditMode()){
            mHelper.abjustChildren();
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs)
    {
        return new AutoLinearLayout.LayoutParams(getContext(), attrs);
    }

    public static class LayoutParams extends LinearLayout.LayoutParams implements AutoLayoutHelper.AutoLayoutParams {

        private AutoLayoutInfo mAutoLayoutInfo;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            mAutoLayoutInfo = AutoLayoutHelper.getAutoLayoutInfo(c, attrs);
        }

        @Override
        public AutoLayoutInfo getAutoLayoutInfo() {
            return mAutoLayoutInfo;
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(ViewGroup.LayoutParams p) {
            super(p);
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }
    }
}
