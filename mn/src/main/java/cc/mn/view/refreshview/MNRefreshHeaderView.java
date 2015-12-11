package cc.mn.view.refreshview;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Calendar;

import cc.mn.R;
import cc.mn.view.refreshview.callback.IHeaderCallback;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-12-09
 * Time: 11:13
 * Version 1.0
 */

public class MNRefreshHeaderView extends LinearLayout implements IHeaderCallback {

    private ImageView mArrowImageView;
    private ImageView mOkImageView;
    private ProgressBar mProgressBar;
    private TextView mHintTextView;
    private TextView mHeaderTimeTextView;
    private Animation mRotateUpAnim;
    private Animation mRotateDownAnim;
    private final int ROTATE_ANIM_DURATION = 180;

    public MNRefreshHeaderView(Context context) {
        this(context, null);
    }

    public MNRefreshHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public MNRefreshHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.mn_refreshview_header, this);
        initView(context);
    }

    private void initView(Context context) {
        mArrowImageView = (ImageView) findViewById(R.id.xrefreshview_header_arrow);
        mOkImageView = (ImageView) findViewById(R.id.xrefreshview_header_ok);
        mHintTextView = (TextView) findViewById(R.id.xrefreshview_header_hint_textview);
        mHeaderTimeTextView = (TextView) findViewById(R.id.xrefreshview_header_time);
        mProgressBar = (ProgressBar) findViewById(R.id.xrefreshview_header_progressbar);

        mRotateUpAnim = new RotateAnimation(0.0f, -180.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        mRotateUpAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateUpAnim.setFillAfter(true);
        mRotateDownAnim = new RotateAnimation(-180.0f, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        mRotateDownAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateDownAnim.setFillAfter(true);
    }

    @Override
    public void onStateNormal() {
        mProgressBar.setVisibility(View.GONE);
        mArrowImageView.setVisibility(View.VISIBLE);
        mOkImageView.setVisibility(View.GONE);
        mArrowImageView.startAnimation(mRotateDownAnim);
        mHintTextView.setText(R.string.xrefreshview_header_hint_normal);
    }

    @Override
    public void onStateReady() {
        mProgressBar.setVisibility(View.GONE);
        mOkImageView.setVisibility(View.GONE);
        mArrowImageView.setVisibility(View.VISIBLE);
        mArrowImageView.clearAnimation();
        mArrowImageView.startAnimation(mRotateUpAnim);
        mHintTextView.setText(R.string.xrefreshview_header_hint_ready);
        mHeaderTimeTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStateRefreshing() {
        mArrowImageView.clearAnimation();
        mArrowImageView.setVisibility(View.GONE);
        mOkImageView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
        mHintTextView.setText(R.string.xrefreshview_header_hint_loading);
    }

    @Override
    public void onStateFinish() {
        mArrowImageView.setVisibility(View.GONE);
        mOkImageView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
        mHintTextView.setText(R.string.xrefreshview_header_hint_loaded);
        mHeaderTimeTextView.setVisibility(View.GONE);
    }

    @Override
    public void onHeaderMove(double offset, int offsetY, int deltaY) {

    }

    @Override
    public void setRefreshTime(long lastRefreshTime) {
        Calendar mCalendar = Calendar.getInstance();
        long refreshTime = mCalendar.getTimeInMillis();
        long howLong = refreshTime - lastRefreshTime;
        int minutes = (int) (howLong / 1000 / 60);
        String refreshTimeText = null;
        Resources resources = getContext().getResources();
        if (minutes < 1) {
            refreshTimeText = resources
                    .getString(R.string.xrefreshview_refresh_justnow);
        } else if (minutes < 60) {
            refreshTimeText = resources
                    .getString(R.string.xrefreshview_refresh_minutes_ago);
            refreshTimeText = Utils.format(refreshTimeText, minutes);
        } else if (minutes < 60 * 24) {
            refreshTimeText = resources
                    .getString(R.string.xrefreshview_refresh_hours_ago);
            refreshTimeText = Utils.format(refreshTimeText, minutes / 60);
        } else {
            refreshTimeText = resources
                    .getString(R.string.xrefreshview_refresh_days_ago);
            refreshTimeText = Utils.format(refreshTimeText, minutes / 60 / 24);
        }
        mHeaderTimeTextView.setText(refreshTimeText);
    }

    @Override
    public void hide() {
        setVisibility(View.GONE);
    }

    @Override
    public void show() {
        setVisibility(View.VISIBLE);
    }

    @Override
    public int getHeaderHeight() {
        return getMeasuredHeight();
    }
}
