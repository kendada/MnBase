package cc.mnbase.jianrong.attr;

import android.util.Log;
import android.view.View;

import cc.mnbase.jianrong.utils.AutoUtils;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2016-01-11
 * Time: 15:48
 * Version 1.0
 */

public abstract class AutoAttr {

    protected int pxVal;

    protected int baseWidth;

    protected int baseHeight;

    private String tag = AutoAttr.class.getSimpleName();

    public AutoAttr(int pxVal, int baseWidth, int baseHeight){
        this.pxVal = pxVal;
        this.baseWidth = baseWidth;
        this.baseHeight = baseHeight;
    }

    public void apply(View view){
        boolean log = view.getTag() != null && view.getTag().toString().equals("auto");

        if(log){
            Log.i(this.getClass().getSimpleName(), " pxVal = " + pxVal);
        }

        int val;
        if(useDefault()){
            val = defaultBaseWidth() ? getPercentWidthSize() : getPercentHeightSize();
            Log.i(tag, " ^^^^^^^^ " + val);
        } else if (baseWidth()){
            val = getPercentWidthSize();
            Log.i(tag, " %%%%%%% " + val);
        } else {
            val = getPercentHeightSize();
            Log.i(tag, " ###### " + val);
        }

        Log.i(tag, " ***** = " + val);

        val = Math.max(val, 1);
        execute(view, val);
    }

    protected int getPercentWidthSize(){
        return AutoUtils.getPercentWidthSizeBigger(pxVal);
    }

    protected int getPercentHeightSize(){
        return AutoUtils.getPercentHeightSizeBigger(pxVal);
    }

    protected boolean useDefault(){
        return !contains(baseHeight, attrVal()) && !contains(baseWidth, attrVal());
    }

    protected boolean contains(int baseVal, int flag){
        return (baseVal & flag) != 0;
    }

    protected boolean baseWidth(){
        return contains(baseWidth, attrVal());
    }

    protected abstract int attrVal();

    protected abstract boolean defaultBaseWidth();

    protected abstract void execute(View view, int val);

}
