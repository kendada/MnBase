package cc.mnbase.jianrong.attr;

import android.view.View;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2016-01-12
 * Time: 09:20
 * Version 1.0
 */

public class PaddingAttr extends AutoAttr {

    public PaddingAttr(int pxVal, int baseWidth, int baseHeight) {
        super(pxVal, baseWidth, baseHeight);
    }

    @Override
    protected int attrVal() {
        return Attrs.PADDING;
    }

    @Override
    protected boolean defaultBaseWidth() {
        return true;
    }

    @Override
    public void apply(View view) {
        int l, t, r, b;
        if(useDefault()){
            l = r = getPercentWidthSize();
            t = b = getPercentHeightSize();
            view.setPadding(l, t, r, b);
            return;
        }
        super.apply(view);
    }

    @Override
    protected void execute(View view, int val) {
        view.setPadding(val, val, val, val);
    }
}
