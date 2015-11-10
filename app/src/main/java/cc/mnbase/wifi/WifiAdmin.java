package cc.mnbase.wifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.text.TextUtils;
import android.util.Log;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-10-27
 * Time: 14:55
 * Version 1.0
 */

public abstract class WifiAdmin {

    private String tag = WifiAdmin.class.getSimpleName();

    private WifiManager mWifiManager;

    private WifiInfo mWifiInfo;

    private List<ScanResult> mWifiList; //扫描出网络连接列表
    private List<WifiConfiguration> mWifiConfigurations;

    private WifiLock mWifiLock;

    private String mPasswd;

    private Context mContext;

    public WifiAdmin(Context context){
        mContext = context;

        //获取WIFI管理器
        mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        //获取WifiInfo对象
        mWifiInfo = mWifiManager.getConnectionInfo();

        Log.i(tag, "IP地址："+mWifiInfo.getIpAddress());

    }

    /**
     * 打开WIFI
     * */
    public void openWifi(){
        if(!mWifiManager.isWifiEnabled()){
            mWifiManager.setWifiEnabled(true);
        }
    }

    /**
     * 关闭WIFI
     * */
    public void closedWifi(){
        if(mWifiManager.isWifiEnabled()){
            mWifiManager.setWifiEnabled(false);
        }
    }

    public abstract Intent myRegisterReceiver(BroadcastReceiver receiver, IntentFilter filter);

    public abstract void myUnRegisterReceiver(BroadcastReceiver receiver);

    public abstract void onNotifyWifiConnected();

    public abstract void onNotifyWifiConnectedFailed();

    /**
     * 添加一个网络并连接
     * */
    public void addNetWork(WifiConfiguration wcg){

        register();
        WifiApAdmin.closeWifiAp(mContext);
        int wcgID = mWifiManager.addNetwork(wcg);
        boolean b = mWifiManager.enableNetwork(wcgID, true);

    }

    public final static int TYPE_NO_PASSWD = 0X11;

    public final static int TYPE_WEP = 0x12;

    public final static int TYPE_WPA = 0X13;


    public void addNetWork(String ssid, String passwd, int type){
        if(TextUtils.isEmpty(ssid) || TextUtils.isEmpty(passwd)){
            Log.e(tag, "addNetwork() ## nullpointer error!");
            return;
        }

        if(type!=TYPE_NO_PASSWD && type!=TYPE_WEP && type!=TYPE_WPA){
            Log.e(tag, "addNetwork() ## unknown type = " + type);
        }

        stopTimer();
        unRegister();

        addNetWork(createWifiInfo(ssid, passwd, type));

    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(WifiManager.RSSI_CHANGED_ACTION)){
                Log.i(tag, "RSSI changed");

                if(isWifiConnected(context) == WIFI_CONNECTED) {
                    stopTimer();
                    onNotifyWifiConnected();
                    unRegister();
                } else if(isWifiConnected(context) == WIFI_CONNECT_FAILED){
                    stopTimer();
                    closedWifi();
                    onNotifyWifiConnectedFailed();
                    unRegister();
                } else if(isWifiConnected(context) == WIFI_CONNECTING){

                }

            }
        }
    };

    private final int STATE_REGISTRING = 0X01;
    private final int STATE_REGISTERED = 0X02;
    private final int STATE_UNREGISTRING = 0X03;
    private final int STATE_UNREGISTRED = 0X04;

    private int mHaveRegister = STATE_UNREGISTRED;

    private synchronized void register(){
        Log.v(tag, "register() ##mHaveRegister = " + mHaveRegister);

        if(mHaveRegister == STATE_REGISTRING || mHaveRegister == STATE_REGISTERED){
            return;
        }

        mHaveRegister = STATE_REGISTRING;
        myRegisterReceiver(mBroadcastReceiver, new IntentFilter(WifiManager.RSSI_CHANGED_ACTION));
        mHaveRegister = STATE_REGISTERED;

        startTimer();
    }

    private synchronized void unRegister(){
        Log.v(tag, "unRegister() ##mHaveRegister = " + mHaveRegister);

        if(mHaveRegister == STATE_UNREGISTRED || mHaveRegister == STATE_UNREGISTRING){
            return;
        }

        mHaveRegister = STATE_UNREGISTRING;
        myUnRegisterReceiver(mBroadcastReceiver);
        mHaveRegister = STATE_UNREGISTRED;
    }

    private Timer mTimer = null;

    private void startTimer(){
        if(mTimer != null){
            stopTimer();
        }
        mTimer = new Timer(true);
        mTimer.schedule(mTimerTask, 30 * 1000);

    }

    private TimerTask mTimerTask = new TimerTask() {
        @Override
        public void run() {
            onNotifyWifiConnectedFailed();
            unRegister();
        }
    };

    private void stopTimer(){
        if(mTimer!=null){
            mTimer.cancel();
            mTimer = null;
        }
    }

    @Override
    protected void finalize() {
        try{
            super.finalize();
            unRegister();
        } catch (Throwable e){
            e.printStackTrace();
        }
    }

    public WifiConfiguration createWifiInfo(String SSID, String password, int type){
        Log.v(tag, "SSID = " + SSID + "## Password = " + password + "## Type = " + type);

        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.SSID = "\"" + SSID +"\"";

        WifiConfiguration tempConfig = this.IsExsits(SSID);
        if(tempConfig != null){
            mWifiManager.removeNetwork(tempConfig.networkId);
        }

        //分为3中情况:1，没有密码；2，用wep加密；3，用wpa加密
        if(type == TYPE_NO_PASSWD){
            config.wepKeys[0] = "";
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        } else if(type == TYPE_WEP){
            config.hiddenSSID = true;
            config.wepKeys[0] = "\"" + password + "\"";
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);

            config.wepTxKeyIndex = 0;
        } else if (type == TYPE_WPA){
            config.preSharedKey = "\"" + password + "\"";
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);

            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);

            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            config.status = WifiConfiguration.Status.ENABLED;

        }


        return config;
    }

    public final static int WIFI_CONNECTED = 0X01;
    public final static int WIFI_CONNECT_FAILED = 0X02;
    public final static int WIFI_CONNECTING = 0X03;

    /**
     * 判断WIFI是否连接成功
     * */
    public int isWifiConnected(Context context){
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if(wifiNetworkInfo.getDetailedState() == NetworkInfo.DetailedState.OBTAINING_IPADDR ||
                wifiNetworkInfo.getDetailedState() == NetworkInfo.DetailedState.CONNECTING){
            return WIFI_CONNECTING;
        } else if(wifiNetworkInfo.getDetailedState() == NetworkInfo.DetailedState.CONNECTED) {
            return WIFI_CONNECTED;
        } else {
            return WIFI_CONNECT_FAILED;
        }

    }

    public WifiConfiguration IsExsits(String SSID){
        List<WifiConfiguration> existingConfigs = mWifiManager.getConfiguredNetworks();
        for(WifiConfiguration existingConfig:existingConfigs){
            if(existingConfig.SSID.equals("\"" + SSID + "\"")){
                return existingConfig;
            }
        }

        return null;
    }

    /**
     * 断开指定Id的网络
     * */
    public void disconnectWifi(int netId){
        mWifiManager.disableNetwork(netId);
        mWifiManager.disconnect();
    }

    /**
     * 检查当前WIFI的状态
     * */
    public int checkState(){
        return mWifiManager.getWifiState();
    }

    /**
     * 锁定WifiLock
     * */
    public void acquireWifiLock(){
        mWifiLock.acquire();
    }

    /**
     * 解锁WifiLock
     * */
    public void releaseWifilock(){
        if(mWifiLock.isHeld()){
            mWifiLock.acquire();
        }
    }

    /**
     *  获取配置好的网络
     * */
    public List<WifiConfiguration> getConfiguration(){
        return mWifiConfigurations;
    }

    /**
     * 指定配置好的网络进行连接
     * */
    public void connectConfiguratios(int index){
        if(index > mWifiConfigurations.size()){
            return;
        }

        mWifiManager.enableNetwork(mWifiConfigurations.get(index).networkId, true);

    }

    public void startScan(){
        mWifiManager.startScan();
        mWifiList = mWifiManager.getScanResults();
        mWifiConfigurations = mWifiManager.getConfiguredNetworks();
    }

    /**
     * 获取网络列表
     * */
    public List<ScanResult> getWifiList(){
        return mWifiList;
    }

    /**
     * 查看扫描结果
     * */
    public StringBuilder lookUpScan(){
        StringBuilder stringBuilder = new StringBuilder();

        for(int i=0; i<mWifiList.size(); i++){
            stringBuilder.append("Index_" + new Integer(i+1).toString() + ":");
            stringBuilder.append(mWifiList.get(i).toString());
            stringBuilder.append("/n");
        }

        return stringBuilder;
    }

    /**
     * 获取Mac地址
     * */
    public String getMacAddress(){
        return (mWifiInfo == null) ? "NULL":mWifiInfo.getMacAddress();
    }

    /**
     * 得到接入点的SSID
     * */
    public String getSSID(){
     return (mWifiInfo == null) ? "NULL":mWifiInfo.getSSID();
    }

    /**
     * 得到IP地址
     * */
    public int getIPAddress(){
        return (mWifiInfo == null) ? 0:mWifiInfo.getIpAddress();
    }

    /**
     * 获取连接的ID
     * */
    public int getNetWork(){
        return (mWifiInfo == null) ? 0:mWifiInfo.getNetworkId();
    }

    /**
     * 获取Wifi的所有信息
     * */
    public String getWifiInfo(){
        return (mWifiInfo == null) ? "NULL":mWifiInfo.toString();
    }

}
