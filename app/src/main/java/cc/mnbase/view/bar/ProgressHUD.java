package cc.mnbase.view.bar;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import cc.mnbase.R;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-10-28
 * Time: 16:44
 * Version 1.0
 */

public class ProgressHUD {

    private Context context;
    private static ProgressHUD mProgressHUD;
    private final static long DISMISSDELAYED = 1000;

    public enum ProgressHUDMaskType{
        None,  // 允许遮罩下面控件点击
        Clear,     // 不允许遮罩下面控件点击
        Black,     // 不允许遮罩下面控件点击，背景黑色半透明
        Gradient,   // 不允许遮罩下面控件点击，背景渐变半透明
        ClearCancel,     // 不允许遮罩下面控件点击，点击遮罩消失
        BlackCancel,     // 不允许遮罩下面控件点击，背景黑色半透明，点击遮罩消失
        GradientCancel   // 不允许遮罩下面控件点击，背景渐变半透明，点击遮罩消失
        ;
    }

    private final FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM);

    private ViewGroup decorView; //activity根View

    private ViewGroup rootView; //mShareView的根View

    private ProgressDefaultView mShareView;

    private Animation outAnim;
    private Animation inAnim;

    private int gravity = Gravity.CENTER;

    private static final ProgressHUD getInstance(Context context){
        if(mProgressHUD == null){
            mProgressHUD = new ProgressHUD();
            mProgressHUD.context = context;
            mProgressHUD.gravity = Gravity.CENTER;
            mProgressHUD.initViews();
            mProgressHUD.initDefaultView();
            mProgressHUD.initAnimation();
        }

        if(context != null && context != mProgressHUD.context){
            mProgressHUD.context = context;
            mProgressHUD.initViews();
        }

        return mProgressHUD;
    }

    protected void initViews(){
        LayoutInflater inflater = LayoutInflater.from(context);
        decorView = (ViewGroup)((Activity)context).getWindow().getDecorView().findViewById(android.R.id.content);
        rootView = (ViewGroup)inflater.inflate(R.layout.layout_svprogresshud, null, false);
        rootView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
    }

    protected void initDefaultView(){
        mShareView = new ProgressDefaultView(context);
        params.gravity = gravity;
        mShareView.setLayoutParams(params);
    }

    protected void initAnimation(){
        if(inAnim == null){
            inAnim = getInAnimation();
        }
        if(outAnim == null){
            outAnim = getOutAnimation();
        }
    }

    private void onAttached(){
        decorView.addView(rootView);
        if(mShareView.getParent()!=null){
            ((ViewGroup)mShareView.getParent()).removeView(mShareView);
        }
        rootView.addView(mShareView);
    }

    

    public Animation getInAnimation(){
        int res = ProgressHUDAnimateUtil.getAnimationResource(this.gravity, true);
        return AnimationUtils.loadAnimation(context, res);
    }

    public Animation getOutAnimation(){
        int res = ProgressHUDAnimateUtil.getAnimationResource(this.gravity, false);
        return AnimationUtils.loadAnimation(context, res);
    }


}
