package cc.mn.view.refreshview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cc.mn.R;
import cc.mn.view.refreshview.callback.IFooterCallBack;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-12-09
 * Time: 11:13
 * Version 1.0
 */

public class MNRefreshFooterView extends LinearLayout implements IFooterCallBack{

    private View mContentView;
    private View mProgressBar;
    private TextView mHintView;
    private TextView mClickView;

    public MNRefreshFooterView(Context context) {
        this(context, null);
    }

    public MNRefreshFooterView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public MNRefreshFooterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(HORIZONTAL);
        inflate(context, R.layout.mn_refreshview_footer, this);
        initView();
    }

    private void initView() {
        mContentView = findViewById(R.id.xrefreshview_footer_content);
        mProgressBar = findViewById(R.id.xrefreshview_footer_progressbar);
        mHintView = (TextView)findViewById(R.id.xrefreshview_footer_hint_textview);
        mClickView = (TextView)findViewById(R.id.xrefreshview_footer_click_textview);
    }

    @Override
    public void callWhenNotAutoLoadMore(final MNRefreshView.MNRefreshViewListener mnRefreshViewListener) {
        mClickView.setText(R.string.xrefreshview_footer_hint_click);
        mClickView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mnRefreshViewListener != null) {
                    mnRefreshViewListener.onLoadMore(false);
                    onStateRefreshing();
                }
            }
        });
    }

    @Override
    public void onStateReady() {
        mHintView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
        mClickView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStateRefreshing() {
        mHintView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
        mClickView.setVisibility(View.GONE);
    }

    @Override
    public void onStateFinish() {
        mHintView.setText(R.string.xrefreshview_footer_hint_normal);
        mHintView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
        mClickView.setVisibility(View.GONE);
    }

    @Override
    public void onStateComplete() {
        mHintView.setText(R.string.xrefreshview_footer_hint_complete);
        mHintView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void hide() {
        LayoutParams params = (LayoutParams)mContentView.getLayoutParams();
        params.height = 0;
        mContentView.setLayoutParams(params);
    }

    @Override
    public void show() {
        LayoutParams params = (LayoutParams)mContentView.getLayoutParams();
        params.height = LayoutParams.WRAP_CONTENT;
        mContentView.setLayoutParams(params);
    }

    @Override
    public int getFooterHeight() {
        return getMeasuredHeight();
    }
}
