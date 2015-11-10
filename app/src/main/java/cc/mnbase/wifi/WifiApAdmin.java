package cc.mnbase.wifi;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;

import java.lang.reflect.Method;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-10-27
 * Time: 17:17
 * Version 1.0
 * Info: 创建热点
 */

public class WifiApAdmin {

    public String tag = WifiAdmin.class.getSimpleName();

    public static void closeWifiAp(Context context){
        WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        closeWifiAp(wifiManager);
    }

    private WifiManager mWifiManager = null;

    private Context mContext;

    public WifiApAdmin(Context context){
        mContext = context;
        mWifiManager = (WifiManager)mContext.getSystemService(Context.WIFI_SERVICE);
        closeWifiAp(mWifiManager);
    }

    private String mSSID = "";
    private String mPasswd = "";

    public void startWifiAp(String ssid, String passwd){
        mSSID = ssid;
        mPasswd = passwd;

        if(mWifiManager.isWifiEnabled()){
            mWifiManager.setWifiEnabled(false);
        }

        startWifiAp();

        MyTimerCheck timerCheck = new MyTimerCheck() {
            @Override
            public void doTimerCheckWork() {
                if(isWifiApEnabled(mWifiManager)){
                    this.exit();
                }
            }

            @Override
            public void doTimerOutWork() {
                this.exit();
            }
        };
        timerCheck.start(15, 1000);

    }

    public void startWifiAp(){
        Method method = null;
        try{
            method = mWifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
            WifiConfiguration netConfig = new WifiConfiguration();
            netConfig.SSID = mSSID;
            netConfig.preSharedKey = mPasswd;
            netConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            netConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            netConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            netConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);

            netConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            netConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);

            netConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            netConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);

            method.invoke(mWifiManager, netConfig, true);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void closeWifiAp(WifiManager wifiManager){
        if(isWifiApEnabled(wifiManager)){
            try{
                Method method = wifiManager.getClass().getMethod("getWifiApConfiguration");
                method.setAccessible(true);
                WifiConfiguration config = (WifiConfiguration)method.invoke(wifiManager);

                Method method2 = wifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
                method2.invoke(wifiManager, config, false);

            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private static boolean isWifiApEnabled(WifiManager wifiManager){
        try{
            Method method = wifiManager.getClass().getMethod("isWifiApEnabled");
            method.setAccessible(true);
            return (Boolean)method.invoke(wifiManager);
        } catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

}
