package cc.mnbase.activity;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.net.Socket;

import cc.mnbase.R;
import cc.mnbase.wifi.WifiAdmin;
import cc.mnbase.wifi.WifiApAdmin;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-10-27
 * Time: 14:53
 * Version 1.0
 */

public class WIFITestActivity extends AppCompatActivity{

    private Button btn, btn2;

    private WifiAdmin mWifiAdmin;

    private String tag = WIFITestActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_test_layout);

        Socket socket;

        btn = (Button)findViewById(R.id.btn);
        btn2 = (Button)findViewById(R.id.btn2);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectWifi();
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createWifi();
            }
        });

    }

    private void createWifi(){
        WifiApAdmin wifiApAdmin = new WifiApAdmin(this);
        wifiApAdmin.startWifiAp("Test0769", "!@#123");
    }

    private void connectWifi(){
        mWifiAdmin = new WifiAdmin(this) {
            @Override
            public Intent myRegisterReceiver(BroadcastReceiver receiver, IntentFilter filter) {
                Log.i(tag, "----48-----"+receiver+"------"+filter);
                return null;
            }

            @Override
            public void myUnRegisterReceiver(BroadcastReceiver receiver) {
                Log.i(tag, "----54------"+receiver);
            }

            @Override
            public void onNotifyWifiConnected() {
                Log.i(tag, "--------59--------");
            }

            @Override
            public void onNotifyWifiConnectedFailed() {
                Log.i(tag, "--------64--------");
            }
        };

        mWifiAdmin.openWifi();

        mWifiAdmin.addNetWork(mWifiAdmin.createWifiInfo("Test0769", "!@#123", WifiAdmin.TYPE_WPA));

    }

}
