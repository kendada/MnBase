package cc.mnbase.jianrong.attr;

import android.view.View;
import android.view.ViewGroup;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2016-01-11
 * Time: 17:13
 * Version 1.0
 */

public class HeightAttr extends AutoAttr {

    public HeightAttr(int pxVal, int baseWidth, int baseHeight) {
        super(pxVal, baseWidth, baseHeight);
    }

    @Override
    protected int attrVal() {
        return Attrs.HEIGHT;
    }

    @Override
    protected boolean defaultBaseWidth() {
        return false;
    }

    @Override
    protected void execute(View view, int val) {
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        lp.height = val;
    }

}
