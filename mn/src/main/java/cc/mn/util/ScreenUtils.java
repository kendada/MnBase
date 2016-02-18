package cc.mn.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-12-29
 * Time: 11:28
 * Version 1.0
 *
 * 屏幕相关的帮助类
 */

public class ScreenUtils {

    private Context mContext;

    public ScreenUtils(Context context){
        mContext = context;
    }

    /**
     * 获取屏幕宽度
     * */
    public int getScreenWidth(){
        WindowManager manager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }

    /**
     * 获取屏幕高度
     * */
    public int getScreenHeight(){
        WindowManager manager = (WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metrics);
        return metrics.heightPixels;
    }

    /**
     * 利用反射获取状态栏高度
     * */
    public int getStatusHeight(){
        int statusHeight = -1;
        try{
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height").get(object).toString());
            statusHeight = mContext.getResources().getDimensionPixelSize(height);
        } catch (Exception e){
            e.printStackTrace();
        }
        return statusHeight;
    }

    /**
     * 截图(包含状态栏)
     * */
    public Bitmap snapShotWidthStatusBar(){
        View view = ((Activity)(mContext)).getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();

        int width = getScreenWidth();
        int height = getScreenHeight();

        Bitmap bp = null;
        bp = Bitmap.createBitmap(bitmap, 0, 0, width, height);
        view.destroyDrawingCache();
        return bp;
    }

    /**
     * 截图(不包含状态栏)
     * */
    public Bitmap snapShotWithoutStatusBar(){
        View view = ((Activity)(mContext)).getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();

        Bitmap bitmap = view.getDrawingCache();
        Rect frame = new Rect();

        ((Activity)(mContext)).getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;

        int width = getScreenWidth();
        int height = getScreenHeight();

        Bitmap bp = null;
        bp = Bitmap.createBitmap(bitmap, 0, statusBarHeight, width, height-statusBarHeight);
        view.destroyDrawingCache();;
        return bp;
    }

}
