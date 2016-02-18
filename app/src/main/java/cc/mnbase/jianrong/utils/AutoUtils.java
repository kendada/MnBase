package cc.mnbase.jianrong.utils;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import cc.mnbase.R;
import cc.mnbase.jianrong.conifg.AutoLayoutConifg;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2016-01-11
 * Time: 16:31
 * Version 1.0
 */

public class AutoUtils {

    private static String tag = AutoUtils.class.getSimpleName();

    public static void auto(View view){
        autoSize(view);
        autoPadding(view);
        autoMargin(view);
    }

    public static void autoMargin(View view){
        if(!(view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams)){
            return;
        }

        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams)view.getLayoutParams();
        if(lp == null) return;

        Object tag = view.getTag(R.id.id_tag_autolayout_margin);
        if(tag != null) return;

        view.setTag(R.id.id_tag_autolayout_margin, "Just Identify");

        lp.setMargins(getPercentWidthSize(lp.leftMargin),
                getPercentHeightSize(lp.topMargin),
                getPercentWidthSize(lp.rightMargin),
                getPercentHeightSize(lp.bottomMargin));
    }

    public static void autoPadding(View view){
        Object tag = view.getTag(R.id.id_tag_autolayout_padding);

        if(tag != null) return;
        view.setTag(R.id.id_tag_autolayout_padding, "Just Identify");

        int l = getPercentHeightSize(view.getPaddingLeft());
        int t = getPercentHeightSize(view.getPaddingTop());
        int r = getPercentWidthSize(view.getPaddingRight());
        int b = getPercentHeightSize(view.getPaddingBottom());

        view.setPadding(l, t, r, b);
    }

    public static void autoSize(View view){
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        if(lp == null) return;

        Object tag = view.getTag(R.id.id_tag_autolayout_size);
        if(tag != null) return;

        view.setTag(R.id.id_tag_autolayout_size, "Just Identify");

        if(lp.width > 0){
            int screenWidth = AutoLayoutConifg.getIntance().getmScreenWidth();
            int designWidht = AutoLayoutConifg.getIntance().getmDesignWidth();

            lp.width = (int)(lp.width * 1.0f / designWidht * screenWidth);
        }

        if(lp.height > 0){
            int screenHeight = AutoLayoutConifg.getIntance().getmScreenHeight();
            int designHeight = AutoLayoutConifg.getIntance().getmDesignHeight();

            lp.height = (int)(lp.height * 1.0f / designHeight * screenHeight);
        }
    }

    public static int getPercentWidthSize(int val){
        int screenWidth = AutoLayoutConifg.getIntance().getmScreenWidth();
        int designWidth = AutoLayoutConifg.getIntance().getmDesignWidth();

        return (int)(val * 1.0f / designWidth * screenWidth);
    }

    public static int getPercentWidthSizeBigger(int val){
        int screenWidth = AutoLayoutConifg.getIntance().getmScreenWidth();
        Log.i(tag, "--- screenWidth = " + screenWidth);
        int designWidth = AutoLayoutConifg.getIntance().getmDesignWidth();
        Log.i(tag, "--- designWidth = " + designWidth);

        int res = val * screenWidth;

        if(res % designWidth == 0){
            return res / designWidth;
        } else {
            return res / designWidth + 1;
        }
    }

    public static int getPercentHeightSize(int val){
        int screenHeight = AutoLayoutConifg.getIntance().getmScreenHeight();
        int designHeight = AutoLayoutConifg.getIntance().getmDesignHeight();

        return (int)(val * 1.0f / designHeight * screenHeight);
    }

    public static int getPercentHeightSizeBigger(int val) {
        int screentHeight = AutoLayoutConifg.getIntance().getmScreenHeight();
        int designHeight = AutoLayoutConifg.getIntance().getmDesignHeight();

        int res = val * screentHeight;

        if(res % designHeight == 0){
            return res / designHeight;
        } else {
            return res / designHeight + 1;
        }

    }

}
