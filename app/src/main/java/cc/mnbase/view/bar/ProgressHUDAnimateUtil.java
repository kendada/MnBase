package cc.mnbase.view.bar;

import android.view.Gravity;

import cc.mnbase.R;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-10-28
 * Time: 16:14
 * Version 1.0
 */

public class ProgressHUDAnimateUtil {

    private final static int INVALID = 0;

    public static int getAnimationResource(int gravity, boolean isInAnimation){

        switch (gravity){
            case Gravity.TOP:
                return isInAnimation ? R.anim.slide_in_top : R.anim.slide_out_top;
            case Gravity.BOTTOM:
                return isInAnimation ? R.anim.slide_in_bottom : R.anim.slide_out_bottom;
            case Gravity.CENTER:
                return isInAnimation ? R.anim.fade_in_center : R.anim.fade_out_center;
        }

        return INVALID;
    }

}
