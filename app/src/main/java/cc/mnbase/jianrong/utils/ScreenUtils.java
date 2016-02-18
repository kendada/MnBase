package cc.mnbase.jianrong.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by zhy on 15/12/4.<br/>
 * form http://stackoverflow.com/questions/1016896/get-screen-dimensions-in-pixels/15699681#15699681
 */
public class ScreenUtils
{


    public static int[] getScreenSize(Context context, boolean useDeviceSize)
    {

        int[] size = new int[2];

        Point point = new Point();
        ((Activity)context).getWindowManager().getDefaultDisplay().getRealSize(point);
        DisplayMetrics dm = context.getResources().getDisplayMetrics();

        size[0] = point.x;
        size[1] = point.y;
        return size;
    }

}
