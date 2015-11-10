package cc.mn;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * User: (1203596603@qq.com)
 * Date: 2015-08-17
 * Time: 20:31
 * Version 1.0
 * 所有Activity的基类
 */

public class BaseActivity extends AppCompatActivity implements IBaseActivity {
    private Context mContext = null;
    private SharedPreferences preferences = null;

    public static final String APP_SETTING_NAME = "app_setting";
    public static final String APP_SETTING_THEME = "app_theme";

    private String tag = "BaseActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mContext = this;
        preferences = getSharedPreferences(APP_SETTING_NAME, MODE_ENABLE_WRITE_AHEAD_LOGGING);

        setAppTheme(); //设置APP主题
        super.onCreate(savedInstanceState);

        validateInternet();

    }

    @Override
    public boolean validateInternet() {
        boolean connect = doCheckInternetConnect();
        if(!connect){ //没有网络连接时，弹出对话框，进行网络设置
            showInternetDialog();
        }
        return connect;
    }

    @Override
    public boolean hasInternetConnect() {
        return doCheckInternetConnect();
    }

    @Override
    public void isExit() {
        //
        stopService();
    }

    @Override
    public void checkMemoryCard() {
        if(!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
            showCardDialog();
        }
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    @Override
    public SharedPreferences getAppSettingSharedPre() {
        return preferences;
    }

    @Override
    public int getThemeSetting() {
        return preferences.getInt(APP_SETTING_THEME, -1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 判断网络是否已经连接
     * */
    private boolean doCheckInternetConnect(){
        ConnectivityManager manager = (ConnectivityManager)mContext.getSystemService(CONNECTIVITY_SERVICE);
        if(manager==null){
            return false;
        } else {
            NetworkInfo info = manager.getActiveNetworkInfo();
            if(info!=null && info.isConnectedOrConnecting()){
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public void startService() {
        Log.i(tag, "--111---startService()");
    }

    @Override
    public void stopService() {
        Log.i(tag, "---116---stopService()");
    }

    /**
     * 网络设置对话框
     * */
    private void showInternetDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("网络设置");
        builder.setMessage("世界上最遥远的距离就是没网。");
        builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS); //网络设置界面
                mContext.startActivity(intent);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }

    /**
     * 检查内存卡对话框
     * */
    private void showCardDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("设置");
        builder.setMessage("储存卡无法使用。");
        builder.setPositiveButton("去设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_SETTINGS); //网络设置界面
                mContext.startActivity(intent);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }

    /**
     * 设置APP主题
     * */
    private void setAppTheme(){
        int theme = getThemeSetting();
        if(theme>0){
            this.setTheme(getThemeSetting()); //设置APP主题
        }
    }
}
