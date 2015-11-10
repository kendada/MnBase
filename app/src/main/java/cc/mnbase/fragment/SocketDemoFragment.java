package cc.mnbase.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import cc.mnbase.R;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-10-28
 * Time: 10:24
 * Version 1.0
 */

public class SocketDemoFragment extends BaseFragment{

    private Socket mSocket = null;
    private String mBuffer = null;

    private TextView txt1;
    private Button send;
    private EditText ed1;

    private String geted1;

    private String tag = SocketDemoFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_socket_demo_layout, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txt1 = (TextView)view.findViewById(R.id.txt1);
        send = (Button)view.findViewById(R.id.send);
        ed1 = (EditText)view.findViewById(R.id.ed1);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                geted1 = ed1.getText().toString();
                txt1.append("client:"+geted1+"\n");
                //启动线程，并向服务器发送和接收消息
                new MyThread(geted1).start();
            }
        });
    }

    public Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0x11:
                    Bundle bundle = msg.getData();
                    String res = "server:"+bundle.getString("msg")+"\n";
                    txt1.setText(res);
                    Log.i(tag, res);
                    break;
            }
        }
    };

    class MyThread extends Thread{

        public String txt1;

        public MyThread(String str){
            txt1 = str;
        }

        @Override
        public void run() {
            super.run();
            Message msg = mHandler.obtainMessage();
            msg.what = 0x11;
            Bundle bundle = new Bundle();
            bundle.clear();

            try {
                //连接服务器，并设置超时5秒
                mSocket = new Socket();
                mSocket.connect(new InetSocketAddress("192.168.1.51", 3000), 5000);
                //获取输入输出流
                OutputStream ou = mSocket.getOutputStream();
                BufferedReader bff = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
                //读取服务器发来的信息
                String line = null;
                mBuffer = "";

                while((line = bff.readLine()) != null){
                    mBuffer += line;
                }

                //向服务器发送消息
                ou.write((geted1+"\nandroid 客户端").getBytes("UTF-8"));
                ou.flush();
                bundle.putString("msg", mBuffer.toString());
                msg.setData(bundle);

                //发送消息，更新UI
                mHandler.sendMessage(msg);
                //关闭输入输出流
                bff.close();
                ou.close();
                mSocket.close();

            } catch (Exception e){
                e.printStackTrace();
                bundle.putString("msg", "服务器连接失败，请检查网络是否打开");
                msg.setData(bundle);
                mHandler.sendMessage(msg);
            }

        }
    }


}
