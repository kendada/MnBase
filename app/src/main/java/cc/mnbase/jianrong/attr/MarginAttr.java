package cc.mnbase.jianrong.attr;

import android.view.View;
import android.view.ViewGroup;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2016-01-11
 * Time: 20:51
 * Version 1.0
 */

public class MarginAttr extends AutoAttr {

    public MarginAttr(int pxVal, int baseWidth, int baseHeight) {
        super(pxVal, baseWidth, baseHeight);
    }

    @Override
    protected int attrVal() {
        return Attrs.MARGIN_LEFT;
    }

    @Override
    protected boolean defaultBaseWidth() {
        return true;
    }

    @Override
    protected void execute(View view, int val) {
        if(!(view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams)) return;
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        params.leftMargin = val;
        params.topMargin = val;
        params.rightMargin = val;
        params.bottomMargin = val;
    }

}
