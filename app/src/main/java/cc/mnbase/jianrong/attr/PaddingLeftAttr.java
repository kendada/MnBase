package cc.mnbase.jianrong.attr;

import android.view.View;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2016-01-12
 * Time: 09:27
 * Version 1.0
 */

public class PaddingLeftAttr extends AutoAttr {

    public PaddingLeftAttr(int pxVal, int baseWidth, int baseHeight) {
        super(pxVal, baseWidth, baseHeight);
    }

    @Override
    protected int attrVal() {
        return Attrs.PADDING_LEFT;
    }

    @Override
    protected boolean defaultBaseWidth() {
        return true;
    }

    @Override
    protected void execute(View view, int val) {
        int t = view.getPaddingTop();
        int r = view.getPaddingRight();
        int b = view.getPaddingBottom();
        view.setPadding(val, t, r, b);
    }
}
