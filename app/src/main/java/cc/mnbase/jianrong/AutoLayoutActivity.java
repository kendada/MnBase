package cc.mnbase.jianrong;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import cc.mn.BaseActivity;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2016-01-11
 * Time: 17:59
 * Version 1.0
 */

public class AutoLayoutActivity extends BaseActivity {

    private static final String LAYOUT_LINEARLAYOUT = "LinearLayout";
    private static final String LAYOUT_RELATIVELAYOUT = "RelativeLayout";
    private static final String LAYOUT_FRAMELAYOUT = "FrameLayout";

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        View view = null;
        if(name.equals(LAYOUT_LINEARLAYOUT)){
            view = new AutoLinearLayout(context, attrs);
        }

        if(name.equals(LAYOUT_RELATIVELAYOUT)){
            view = new AutoRelativeLayout(context, attrs);
        }

        if(name.equals(LAYOUT_FRAMELAYOUT)){
            view = new AutoFrameLayout(context, attrs);
        }

        if(view != null) return view;

        return super.onCreateView(parent, name, context, attrs);
    }
}
