package cc.mnbase.crash.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.*;
import android.os.Process;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.ref.WeakReference;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import cc.mnbase.crash.MnAppConfig;
import cc.mnbase.crash.ui.CrashActivity;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2016-01-12
 * Time: 15:00
 * Version 1.0
 */

public final class CrashHelper {

    private static final String INTENT_EXTRA_STACK_TRACE = "mn.intent.EXTRA_STACK_TRACE";
    private static final String INTENT_EXTRA_URL = "mn.intent.EXTRA_URL";
    private static final String INTENT_ACTION_CRASH_ACTIVITY = "mn.intent.action.CRASH_ERROR";
    private static final String INTENT_ACTION_BROWSER_ACTIVITY = "mn.intent.action.WEB_BROWSER";

    private static final String TAG = MnAppConfig.DEBUG_TAG + "-" + CrashHelper.class.getSimpleName();
    private static final int MAX_STACK_TRACE_SIEZ = 131071;

    private static Application application;
    private static WeakReference<Activity> lastActivityCreated = new WeakReference<Activity>(null);

    private static Class<? extends Activity> crashActivityClass = null;
    private static Class<? extends Activity> browserActivityClass = null;

    private static boolean isInBackground = false;

    public static void install(Context context){
        install(context, null);
    }

    public static void install(Context context, Class<? extends Activity> clazz){
        try {
            if(context == null){
                Log.e(TAG, "Install failed: context is null!");
                return;
            }
            application = (Application)context.getApplicationContext();
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH){
                Log.w(TAG, "Crash tool will be installed, but may not be reliable in API lower than 14");
            }
            Thread.UncaughtExceptionHandler oldHandler = Thread.getDefaultUncaughtExceptionHandler();
            String pkgName = application.getPackageName();
            Log.d(TAG, "current application package name is " + pkgName);
            if(oldHandler != null && oldHandler.getClass().getName().startsWith(pkgName)){
                Log.e(TAG, "You have already installed crash tool, doing nothing!");
                return;
            }
            if(oldHandler != null && !oldHandler.getClass().getName().startsWith("com.android.internal.os")){
                Log.e(TAG, "IMPORTANT WARNING! You already have an UncaughtExceptionHandler, are you sure this is correct? If you use ACRA, Crashlytics or similar libraries, you must initialize them AFTER this crash tool! Installing anyway, but your original handler will not be called.");
            }
            Thread.setDefaultUncaughtExceptionHandler(new MyUncaughtExceptionHandler());
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH){
                application.registerActivityLifecycleCallbacks(new MyActivityLifecycleCallbacks());
            }
            Log.i(TAG, "Crash tool has been installed.");
        } catch (Throwable t){
            Log.e(TAG, "An unknown error occurred while installing crash tool, it may not have been properly initialized. Please report this as a bug if needed.", t);
        }

        if(clazz != null){
            setCrashActivityClass(clazz);
        }
    }

    /**
     * 打开浏览器
     * */
    public static void startWebBrowser(String url){
        if(browserActivityClass == null){
            browserActivityClass = gussBrowserActivityClass();
        }

        if(browserActivityClass == null){
            throw new RuntimeException("Your browser activity not available, must declare in AndroidManifest.xml use intent-filter action: " + INTENT_ACTION_BROWSER_ACTIVITY);
        }

        Intent intent = new Intent(application, browserActivityClass);
        intent.putExtra(INTENT_EXTRA_URL, url);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        application.startActivity(intent);
    }

    /**
     * 是否在后台运行
     * */
    public static boolean isInBackground(){
        return isInBackground;
    }

    public static void setCrashActivityClass(Class<? extends Activity> crashClass){
        crashActivityClass = crashClass;
    }

    public static void setBrowserActivityClass(Class<? extends Activity> browserClass){
        browserClass = browserClass;
    }

    public static String getStackTraceFromIntent(Intent intent){
        return intent.getStringExtra(INTENT_EXTRA_STACK_TRACE);
    }

    public static String getUrlFromIntent(Intent intent){
        return intent.getStringExtra(INTENT_EXTRA_URL);
    }

    /**
     * 获取设备信息
     * */
    public static String getDeviceInfo() {
        StringBuilder builder = new StringBuilder();
        PackageInfo pi = getPackageInfo();
        String dateTime = DateFormat.getDateTimeInstance().format(Calendar.getInstance(Locale.CHINA).getTime());
        String appName = pi.applicationInfo.loadLabel(application.getPackageManager()).toString();
        int[] pixels = getPixels();
        String cpu;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            cpu = Arrays.deepToString(Build.SUPPORTED_ABIS);
        } else {
            cpu = Build.CPU_ABI;
        }
        builder.append("Date time: ").append(dateTime).append("\n");
        builder.append("APP Version: ").append(appName).append(" v").append(pi.versionName).append("(").append(pi.versionCode).append(")\n");
        builder.append("Android OS: ").append(Build.VERSION.RELEASE).append("(").append(cpu).append(")\n");
        builder.append("Phone Model: ").append(getDeviceModelName()).append("\n");
        builder.append("Screen Pixel: ").append(pixels[0]).append("X").append(pixels[1]).append(",").append(pixels[2]).append("\n\n");
        return builder.toString();
    }

    private static PackageInfo getPackageInfo(){
        PackageInfo info = null;
        try {
            info = application.getPackageManager().getPackageInfo(application.getPackageName(), 0);
        } catch (Exception e){
            e.printStackTrace(System.err);
        }
        return info;
    }

    private static int[] getPixels(){
        int[] pixels = new int[3];
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager)application.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);
        pixels[0] = dm.widthPixels;
        pixels[1] = dm.heightPixels;
        pixels[2] = dm.densityDpi;
        return pixels;
    }

    private static String getDeviceModelName(){
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if(model.startsWith(manufacturer)){
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    /**
     * 首字母大写
     * */
    private static String capitalize(String s){
        if(s == null && s.length() == 0){
            return "";
        }
        char first = s.charAt(0);
        if(Character.isUpperCase(first)){
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    /**
     * 判断Activity是已经注册
     * */
    private static boolean activityAvailable(Class<? extends Activity> clazz){
        Intent intent = new Intent();
        intent.setClass(application, clazz);
        List<ResolveInfo> list = application.getPackageManager().queryIntentActivities(intent, 0);
        return list.size() != 0;
    }

    /**
     * 获取某个意图相关的Activity，未知在AndroidManifest.xml里声明的话将获取不到
     * */
    private static Class<? extends Activity> obtainActivityByIntentAction(String action){
        List<ResolveInfo> resolveInfos = application.getPackageManager().queryIntentActivities(new Intent().setAction(action),
                PackageManager.GET_RESOLVED_FILTER);
        if(resolveInfos != null && resolveInfos.size() > 0){
            ResolveInfo resolveInfo = resolveInfos.get(0);
            try {
                return (Class<? extends Activity>)Class.forName(resolveInfo.activityInfo.name);
            } catch (ClassNotFoundException e){
                Log.e(TAG, "Failed when resolving the error activity class via intent filter, stack trace follows!", e);
            }
        }
        return null;
    }

    private static boolean isStackTraceLikelyConflict(Throwable throwable, Class<? extends Activity> activityClass){
        do{
            StackTraceElement[] stackTrace = throwable.getStackTrace();
            for(StackTraceElement element : stackTrace){
                if((element.getClassName().equals("android.app.ActivityThread") && element.getMethodName().equals("handleBindApplication")) ||
                        element.getClassName().equals(activityClass.getName())){
                    return true;
                }
            }
        }while ((throwable = throwable.getCause()) != null);
        return false;
    }

    private static Class<? extends Activity> gussCrashActivityClass(){
        Class<? extends Activity> reslovedActivityClass = obtainActivityByIntentAction(INTENT_ACTION_CRASH_ACTIVITY);

        if(reslovedActivityClass == null && activityAvailable(CrashActivity.class)){
            reslovedActivityClass = CrashActivity.class;
        }

        return reslovedActivityClass;
    }

    private static Class<? extends Activity> gussBrowserActivityClass(){
        Class<? extends Activity> reslovedActivityClass = obtainActivityByIntentAction(INTENT_ACTION_BROWSER_ACTIVITY);
        return reslovedActivityClass;
    }

    private static void killCurrentProcess() {
        android.os.Process.killProcess(Process.myPid());
        System.exit(0);
    }

    private static class MyUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

        @Override
        public void uncaughtException(Thread thread, Throwable throwable) {
            Log.e(TAG, "App has crashed, executing UncaughtExceptionHandler", throwable);
            final String stackTraceString = toStackTraceString(throwable);

            if(crashActivityClass == null){
                crashActivityClass = gussCrashActivityClass();
            }

            if(crashActivityClass == null){
                Log.e(TAG, "Your crash activity not available, must declare in AndroidManifest.xml use intent-filter action: " + INTENT_ACTION_CRASH_ACTIVITY);

                new Thread(){
                    @Override
                    public void run() {
                        try {
                            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "crash.log");
                            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
                            writer.write(getDeviceInfo() + "\n\n" + stackTraceString);
                            writer.close();
                            Log.i(TAG, "Save stack trace: " + file.getAbsolutePath());
                        } catch (Exception e){
                            Log.e(TAG, "Save stack trace failed", e);
                        }
                    }
                }.start();
            } else {
                if(isStackTraceLikelyConflict(throwable, crashActivityClass)){
                    Log.e(TAG, "Your application class or your crash activity have crashed, the custom activity will not be launched!");
                } else {
                    Intent intent = new Intent(application, crashActivityClass);
                    intent.putExtra(INTENT_EXTRA_STACK_TRACE, stackTraceString);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    application.startActivity(intent);
                }
            }

            Activity lastActivity = lastActivityCreated.get();
            if(lastActivity != null){
                Log.i(TAG, "Last activity: " + lastActivity.getClass().getName());
                saveErrorActivity(lastActivity.getClass().getName());
                lastActivity.finish();
                lastActivityCreated.clear();
            }
            killCurrentProcess();
        }

        private void saveErrorActivity(String activityName){
            SharedPreferences spf = application.getSharedPreferences("error", Context.MODE_ENABLE_WRITE_AHEAD_LOGGING);
            SharedPreferences.Editor et = spf.edit();
            et.putString("activity", activityName);
            et.commit();
        }

        private String toStackTraceString(Throwable throwable){
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            throwable.printStackTrace(pw);
            String stackTraceString = sw.toString();

            if(stackTraceString.length() > MAX_STACK_TRACE_SIEZ){
                String disclaimer = " [stack trace too large]";
                stackTraceString = stackTraceString.substring(0, MAX_STACK_TRACE_SIEZ - disclaimer.length()) + disclaimer;
            }
            return stackTraceString;
        }
    }

    private static class MyActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks{

        int currentlyStartedActivities = 0;

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            if(activity.getClass() != crashActivityClass){
                lastActivityCreated = new WeakReference<Activity>(activity);
            }
        }

        @Override
        public void onActivityStarted(Activity activity) {
            currentlyStartedActivities++;
            isInBackground = (currentlyStartedActivities==0);
        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {
            currentlyStartedActivities--;
            isInBackground = (currentlyStartedActivities==0);
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {

        }
    }

}
