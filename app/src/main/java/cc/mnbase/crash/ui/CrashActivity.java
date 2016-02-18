package cc.mnbase.crash.ui;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import cc.mn.BaseActivity;
import cc.mnbase.crash.MnAppConfig;
import cc.mnbase.crash.utils.CrashHelper;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2016-01-12
 * Time: 16:13
 * Version 1.0
 */

public class CrashActivity extends BaseActivity {

    private String stackTrace = "";
    private String deviceInfo = "";

    private String tag = CrashActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ============ 直接恢复到发生错误的界面

     //   regainActivity();
     //   finish();

        // ============ 架构

        if(Build.VERSION.SDK_INT >= 21){
            setTheme(android.R.style.Theme_Material_Wallpaper);
        } if(Build.VERSION.SDK_INT >= 11){
            setTheme(android.R.style.Theme_Holo_Wallpaper);
        } else {
            setTheme(android.R.style.Theme_Wallpaper);
        }

        setTitle("软件奔溃啦!");
        stackTrace = CrashHelper.getStackTraceFromIntent(getIntent());
        deviceInfo = CrashHelper.getDeviceInfo();
        View contentView = buildCustomView();
        setContentView(contentView);


    }

    @Override
    public void onBackPressed() {
        showDialog();
    }

    private View buildCustomView(){
        LinearLayout rootLayout = new LinearLayout(this);
        rootLayout.setOrientation(LinearLayout.VERTICAL);
        rootLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        rootLayout.setBackgroundColor(Color.WHITE);
        rootLayout.setFocusable(true);
        rootLayout.setFocusableInTouchMode(true);

        ScrollView scrollView = new ScrollView(this);
        scrollView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT,
                1.0f));
        LinearLayout scrollableView = new LinearLayout(this);
        scrollableView.setOrientation(LinearLayout.VERTICAL);
        scrollView.addView(scrollableView);

        TextView traceView = new TextView(this);
        traceView.setPadding(10, 10, 10, 10);
        traceView.append(deviceInfo);
        traceView.append(stackTrace);
        traceView.setTextColor(Color.BLACK);
        scrollableView.addView(traceView);
        rootLayout.addView(scrollView);

        Button button = new Button(this);
        button.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        button.setText("我知道了");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        rootLayout.addView(button);
        return rootLayout;
    }

    private void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setItems(new String[]{"告诉开发者", "重新启动", "恢复", "退出软件"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                switch (which) {
                    case 0:
                        sendToQQ();
                        break;
                    case 1:
                        restartApp();
                        break;
                    case 2:
                        regainActivity();
                        break;
                    case 3:
                        exitApp();
                        break;
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * 通过QQ发送错误信息
     * */
    private void sendToQQ(){
        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        android.content.ClipData clip = android.content.ClipData.newPlainText("crash_log", stackTrace);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, "错误日志已复制，请粘贴发送！", Toast.LENGTH_LONG).show();
        try {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            Uri uri = Uri.parse("mqqwpa://im/chat?chat_type=wpa&uin=" + MnAppConfig.DEVELOPER_QQ + "&version=1");
            intent.setData(uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //延迟杀掉软件，以便Toast显示完毕
        CountDownTimer timer = new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                exitApp();
            }
        };
        timer.start();

    }

    /**
     * 重启APP
     * */
    private void restartApp(){
         String packageName = getApplicationContext().getPackageName();
         Intent intent = getPackageManager().getLaunchIntentForPackage(packageName);
         intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
         startActivity(intent);
         finish();

    }

    /**
     * 恢复发生错误的界面
     * */
    private void regainActivity(){
        SharedPreferences spf = getSharedPreferences("error", MODE_ENABLE_WRITE_AHEAD_LOGGING);
        String clazz = spf.getString("activity", null);
        if(clazz==null) return;
        try {
            // 利用反射获取需要重启的页面
            Class<Activity> aClass = (Class<Activity>) Class.forName(clazz);
            Activity activity = aClass.newInstance();
            Intent intent = new Intent(getContext(), activity.getClass());
            startActivity(intent);
            finish();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 退出APP
     * */
    private void exitApp(){
        finish();
    }

}
