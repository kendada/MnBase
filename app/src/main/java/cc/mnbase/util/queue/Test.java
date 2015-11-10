package cc.mnbase.util.queue;

import android.os.Handler;
import android.util.Log;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-10-27
 * Time: 11:07
 * Version 1.0
 */

public class Test {

    long timer = 0;
    int count = 0;

    final Handler h = new Handler();

    private String tag = Test.class.getSimpleName();

    public Test(){
        add();
    }

    public void add(){
        timer = System.currentTimeMillis();
        for(int k=0; k<100; k++){
            new Thread(){
                @Override
                public void run() {
                    super.run();
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    h.post(new Runnable() {
                        @Override
                        public void run() {
                            Log.i(tag, "编号"+(count++)+"线程消耗了"+(System.currentTimeMillis()-timer));
                        }
                    });
                }
            }.start();
        }
    }

}
