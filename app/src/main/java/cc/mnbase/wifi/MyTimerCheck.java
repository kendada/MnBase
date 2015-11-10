package cc.mnbase.wifi;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-10-27
 * Time: 17:44
 * Version 1.0
 */

public abstract class MyTimerCheck {

    private int mCount = 0;
    private int mTimeOutCount = 1;
    private int mSleepTime = 1000; //1秒
    private boolean mExitFlag = false;
    private Thread mThread = null;

    public abstract void doTimerCheckWork();

    public abstract void doTimerOutWork();

    public MyTimerCheck(){
        mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(!mExitFlag){
                    mCount++;
                    if(mCount < mTimeOutCount){
                        doTimerCheckWork();
                        try{
                            mThread.sleep(mSleepTime);
                        } catch (Exception e){
                            e.printStackTrace();
                            //
                        }
                    } else {
                        doTimerOutWork();
                    }
                }
            }
        });
    }

    public void start(int timeOutCount, int sleepTime){
        mTimeOutCount = timeOutCount;
        mSleepTime = sleepTime;

        mThread.start();
    }

    public void exit(){
        mExitFlag = true;
    }

}
