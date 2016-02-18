package cc.mnbase.jianrong.conifg;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import cc.mnbase.jianrong.utils.ScreenUtils;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2016-01-11
 * Time: 16:10
 * Version 1.0
 */

public class AutoLayoutConifg {

    private static AutoLayoutConifg sIntance = new AutoLayoutConifg();

    private static final String KEY_DESIGN_WIDTH = "design_width";
    private static final String KEY_DESIGN_HEIGHT = "design_height";

    private int mScreenWidth;
    private int mScreenHeight;

    private int mDesignWidth;
    private int mDesignHeight;

    private boolean useDeviceSize;

    private AutoLayoutConifg(){

    }

    public void checkParams(){
        if(mDesignHeight <= 0 || mDesignWidth <= 0){
            throw new RuntimeException("you must " + KEY_DESIGN_WIDTH + " and " + KEY_DESIGN_HEIGHT + " in your mainfest file.");
        }
    }

    public AutoLayoutConifg useDeviceSize(){
        useDeviceSize = true;
        return this;
    }

    public static AutoLayoutConifg getIntance(){
        return sIntance;
    }

    public int getmScreenWidth() {
        return mScreenWidth;
    }

    public int getmScreenHeight() {
        return mScreenHeight;
    }

    public int getmDesignWidth() {
        return mDesignWidth;
    }

    public int getmDesignHeight() {
        return mDesignHeight;
    }

    public void init(Context context){
        getMetaData(context);
        int[] screenSize = ScreenUtils.getScreenSize(context, useDeviceSize);
        mScreenWidth = screenSize[0];
        mScreenHeight = screenSize[1];
    }

    private void getMetaData(Context context){
        PackageManager packageManager = context.getPackageManager();
        ApplicationInfo applicationInfo;
        try{
            applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            if(applicationInfo != null && applicationInfo.metaData != null){
                mDesignWidth = (int)applicationInfo.metaData.get(KEY_DESIGN_WIDTH);
                mDesignHeight = (int)applicationInfo.metaData.get(KEY_DESIGN_HEIGHT);
            }
        } catch (PackageManager.NameNotFoundException e){
            throw new RuntimeException("you must " + KEY_DESIGN_WIDTH + " and " + KEY_DESIGN_HEIGHT + " in your mainfest file.");
        }
    }
}
