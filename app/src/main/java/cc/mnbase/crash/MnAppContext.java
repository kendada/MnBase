package cc.mnbase.crash;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import cc.mnbase.crash.utils.CrashHelper;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2016-01-12
 * Time: 14:51
 * Version 1.0
 */

public class MnAppContext extends Application {

    private static Context applicationContext;

    public MnAppContext(){
        applicationContext = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initialize(this);
    }

    public static void initialize(Context context){
        initialize(context, null);
    }

    public static void initialize(Context context, Class<? extends Activity> clazz){
        if(context instanceof Application){
            applicationContext = context;
        } else {
            applicationContext = context.getApplicationContext();
        }
        CrashHelper.install(applicationContext, clazz);
    }

    public static Context getGlobalContext(){
        if(applicationContext == null){
            throw  new RuntimeException("Application context is null。。。");
        }
        return applicationContext;
    }
}
