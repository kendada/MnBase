package cc.mnbase.jianrong.attr;

import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2016-01-11
 * Time: 17:04
 * Version 1.0
 */

public class TextSizeAttr extends AutoAttr {

    public TextSizeAttr(int pxVal, int baseWidth, int baseHeight) {
        super(pxVal, baseWidth, baseHeight);
    }

    @Override
    protected int attrVal() {
        return Attrs.TEXTSIZE;
    }

    @Override
    protected boolean defaultBaseWidth() {
        return false;
    }

    @Override
    protected void execute(View view, int val) {
        if(!(view instanceof TextView)) return;
        ((TextView)view).setIncludeFontPadding(false);
        ((TextView)view).setTextSize(TypedValue.COMPLEX_UNIT_PX, val);
    }


}
