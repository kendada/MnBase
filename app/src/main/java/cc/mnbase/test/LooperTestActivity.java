package cc.mnbase.test;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import cc.mnbase.R;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2016-02-17
 * Time: 14:16
 * Version 1.0
 */

public class LooperTestActivity extends AppCompatActivity {

    private TextView text;

    private final int MSG_HELLO = 0;
    private final int MSG_WORLD = 1;

    private WorkThread mWorkThread;

    private MyThread myThread;

    private String tag = LooperTestActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_loop_test_layout);

        text = (TextView)findViewById(R.id.text);

        myThread = new MyThread();
        myThread.start();

        mWorkThread = new WorkThread(new OnLoadDataListener() {
            @Override
            public void onResult(Object obj) {
                text.setText(" *** " + obj + " *** ");
            }
        });
        mWorkThread.start();

    }

    public void sendMsg(View view){
        switch (view.getId()){
            case R.id.send_msg:
                String val = " Hello ";
                Log.i(tag, "MainThread is ready to send msg : " + val);
                myThread.mHandler.obtainMessage(MSG_HELLO, val).sendToTarget();
                mWorkThread.mHandler.obtainMessage(MSG_HELLO, " Hello ").sendToTarget();
                break;
            case R.id.send_msg_again:
                String val1 = " World !";
                Log.i(tag, "MainThread is ready to seng msg again : " + val1);
                myThread.mHandler.obtainMessage(MSG_WORLD, val1).sendToTarget();
                mWorkThread.mHandler.obtainMessage(MSG_WORLD, " World !").sendToTarget();
                break;
        }
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MSG_HELLO:

                    break;
                case MSG_WORLD:
                    text.setText(" *** " + msg.obj + " *** ");
                    break;
            }
        }
    };

    class WorkThread extends Thread{

        public Handler mHandler;

        private OnLoadDataListener onLoadDataListener;

        public WorkThread(OnLoadDataListener onLoadDataListener){
            this.onLoadDataListener = onLoadDataListener;
        }

        @Override
        public void run() {
            // 初始化Looper
            Looper.prepare();
            mHandler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    switch (msg.what){
                        case MSG_HELLO:
                            Log.i(tag, " *** " + msg.obj);
                            Toast.makeText(LooperTestActivity.this, " *** " + msg.obj + " *** ", Toast.LENGTH_SHORT).show();
                            break;
                        case MSG_WORLD:
                            Log.i(tag, " ### " + msg.obj);
                            final Object obj = msg.obj;
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if(onLoadDataListener != null){
                                        onLoadDataListener.onResult(obj); // 更新UI
                                    }
                                }
                            });
                            break;
                    }
                }
            };
            // 启动消息循环
            Looper.loop();

        }
    }

    class MyThread extends Thread{

        public Handler mHandler;

        @Override
        public void run() {
            // 初始化Looper
            Looper.prepare();

            mHandler = new Handler(){ // 绑定handler到MyThread实例的Looper对象
                @Override
                public void handleMessage(Message msg) { // 处理消息方法
                    switch (msg.what){
                        case MSG_HELLO:
                            Log.i(tag, " MyThread receive msg : " + msg);
                            break;
                        case MSG_WORLD:
                            Log.i(tag, " MyThread receive msg again : " + msg);
                            break;
                    }
                }
            };
            // 启动消息循环
            Looper.loop();

        }
    }

    public interface OnLoadDataListener{
        void onResult(Object obj);
    }


}
