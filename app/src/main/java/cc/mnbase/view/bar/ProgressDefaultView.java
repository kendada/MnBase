package cc.mnbase.view.bar;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cc.mnbase.R;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-10-28
 * Time: 16:54
 * Version 1.0
 */

public class ProgressDefaultView extends LinearLayout {

    private int resBigLoading = R.mipmap.ic_svstatus_loading;
    private int resInfo = R.mipmap.ic_svstatus_info;
    private int resSuccess = R.mipmap.ic_svstatus_success;
    private int resError = R.mipmap.ic_svstatus_error;

    private ImageView ivBigLoading, ivSmallLoading;
    private TextView tvMsg;

    private CircleProgressBar circleProgressBar;

    private RotateAnimation mRotateAnimation;

    public ProgressDefaultView(Context context) {
        this(context, null);
    }

    public ProgressDefaultView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews();
        init();
    }

    private void initViews(){
        LayoutInflater.from(getContext()).inflate(R.layout.view_svprogressdefault, this, true);
        ivBigLoading = (ImageView)findViewById(R.id.ivBigLoading);
        ivSmallLoading = (ImageView)findViewById(R.id.ivSmallLoading);
        circleProgressBar = (CircleProgressBar)findViewById(R.id.circleProgressBar);

        tvMsg = (TextView)findViewById(R.id.tvMsg);

    }

    private void init(){
        mRotateAnimation = new RotateAnimation(0f, 359f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateAnimation.setDuration(1000L);
        mRotateAnimation.setInterpolator(new LinearInterpolator());
        mRotateAnimation.setRepeatCount(-1);
        mRotateAnimation.setRepeatMode(Animation.RESTART);

    }

    public void show(){
        clearAnimation();
        ivBigLoading.setImageResource(resBigLoading);
        ivBigLoading.setVisibility(View.VISIBLE);
        ivSmallLoading.setVisibility(View.GONE);
        circleProgressBar.setVisibility(View.GONE);
        tvMsg.setVisibility(View.GONE);

        //开启动画
        ivBigLoading.startAnimation(mRotateAnimation);

    }

    public void showWidthStatus(String string){
        if(TextUtils.isEmpty(string)){
            show();
            return;
        }
        showBaseStatus(resBigLoading, string);
        ivSmallLoading.startAnimation(mRotateAnimation);
    }

    public void showInfowidthStatus(String string){
        showBaseStatus(resInfo, string);
    }

    public void showSuccessWidthStatus(String string){
        showBaseStatus(resSuccess, string);
    }

    public void showErrorWidthStatus(String string){
        showBaseStatus(resError, string);
    }

    public void showWidthProgress(String string){
        showProgress(string);
    }

    public CircleProgressBar getCircleProgressBar(){
        return circleProgressBar;
    }

    public void setText(String string){
        tvMsg.setText(string);
    }

    public void showProgress(String string){
        clearAnimation();
        tvMsg.setText(string);
        ivBigLoading.setVisibility(View.GONE);
        ivSmallLoading.setVisibility(View.GONE);
        circleProgressBar.setVisibility(View.VISIBLE);
        tvMsg.setVisibility(View.VISIBLE);
    }

    public void showBaseStatus(int res, String string){
        clearAnimation();
        ivSmallLoading.setImageResource(res);
        tvMsg.setText(string);
        ivBigLoading.setVisibility(View.GONE);
        circleProgressBar.setVisibility(View.GONE);
        ivSmallLoading.setVisibility(View.VISIBLE);
        tvMsg.setVisibility(View.VISIBLE);
    }

    private void clearAnimatios(){
        ivBigLoading.clearAnimation();
        ivSmallLoading.clearAnimation();
    }

}
