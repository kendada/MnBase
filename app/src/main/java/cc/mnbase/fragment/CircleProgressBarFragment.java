package cc.mnbase.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cc.mnbase.R;
import cc.mnbase.view.bar.CircleProgressBar;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-10-28
 * Time: 15:00
 * Version 1.0
 */

public class CircleProgressBarFragment extends BaseFragment {

    private CircleProgressBar progressBar = null;

    private int pro = 0;

    private String tag = CircleProgressBarFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_circle_pro_bar_layout, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar = (CircleProgressBar)view.findViewById(R.id.progressBar);


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Message msg = handler.obtainMessage(0);
                handler.sendMessage(msg);
            }
        }).start();

    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    pro += 10;
                    progressBar.setProgress(pro);
                    Log.i(tag, "进度："+pro);
                    break;
            }
        }
    };


}
