package cc.mnbase.view.tab.weixin;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cc.mnbase.R;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-11-25
 * Time: 14:49
 * Version 1.0
 */

public class WeiXinTabLayout extends RelativeLayout {

    private Context mContext;

    //标题
    private TextView titleTextView;
    private String titleText;
    private float tabTitleSize;
    private int titleColor;
    private static final int titleTextViewID = 10001;
    private int titleNolColor = Color.GRAY;

    //第二层标题
    private TextView titleOverTextView;
    private static final int titleOverTextViewID = 100011;


    //图标
    private ImageView iconImageView;
    private static final int iconImageViewID = 10002;
    private Drawable tabIcon;

    //第二层图标
    private ImageView iconOverImageView;
    private static final int iconOverImageViewID = 100022;
    private Drawable tabIconOver;

    //是否已经被选中
    private boolean isChecked = true;

    private float mAlpha = 1f;

    private String tag = WeiXinTabLayout.class.getSimpleName();

    public WeiXinTabLayout(Context context) {
        this(context, null);
    }

    public WeiXinTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WeiXinTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.WeiXinTabLayoutStyleable);
        isChecked = ta.getBoolean(R.styleable.WeiXinTabLayoutStyleable_tabChecked, false);

        titleColor = ta.getColor(R.styleable.WeiXinTabLayoutStyleable_tabTitleColor, Color.parseColor("#00BCD4"));
        tabTitleSize = ta.getDimension(R.styleable.WeiXinTabLayoutStyleable_tabTitleSize, 15);
        titleText = ta.getString(R.styleable.WeiXinTabLayoutStyleable_tabTitle);

        tabIcon = ta.getDrawable(R.styleable.WeiXinTabLayoutStyleable_tabIcon);
        tabIconOver = ta.getDrawable(R.styleable.WeiXinTabLayoutStyleable_tabIconOver);

        ta.recycle();

        mContext = context;
        initViews();
    }

    /**
     * 初始化控件
     * */
    private void initViews(){
        RelativeLayout.LayoutParams titleLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        titleLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        titleLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        //第二层标题
        titleOverTextView = new TextView(mContext);
        titleOverTextView.setId(titleOverTextViewID);
        titleOverTextView.setLayoutParams(titleLayoutParams);
        this.addView(titleOverTextView);
        //标题
        titleTextView = new TextView(mContext);
        titleTextView.setId(titleTextViewID);
        titleTextView.setLayoutParams(titleLayoutParams);
        this.addView(titleTextView);

        //图标LayoutParams
        RelativeLayout.LayoutParams iconLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        iconLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        iconLayoutParams.addRule(RelativeLayout.ABOVE, titleTextViewID);
        //第二层图标
        iconOverImageView = new ImageView(mContext);
        iconOverImageView.setId(iconOverImageViewID);
        iconOverImageView.setLayoutParams(iconLayoutParams);
        this.addView(iconOverImageView);
        //图标
        iconImageView = new ImageView(mContext);
        iconImageView.setId(iconImageViewID);
        iconImageView.setLayoutParams(iconLayoutParams);
        this.addView(iconImageView);

        //设置数据，并刷新
        refreshData();
    }

    /**
     * 设置选中状态
     * */
    public void setChecked(boolean isChecked) {
        this.isChecked = isChecked;
        setCheckedData();
    }

    /**
     * 滑动时改变颜色
     * */
    public void onScrolling(float alpha){
        mAlpha = alpha;
        onScrollSetData();
    }

    /**
     * 设置图标
     * */
    private void setTabIconData(){
        iconImageView.setImageDrawable(tabIcon);
        if(isChecked){
            iconImageView.setAlpha(1-mAlpha); //api11+
        } else {
            iconImageView.setAlpha(mAlpha);
        }
    }

    /**
     * 设置第二层图标
     * */
    private void setTabIconOverData(){
        iconOverImageView.setImageDrawable(tabIconOver);
        if(isChecked){
            iconOverImageView.setAlpha(mAlpha);
        } else {
            iconOverImageView.setAlpha(1-mAlpha);
        }
    }

    /**
     * 滑动时设置图标透明度以及文字透明度
     * */
    private void onScrollSetData(){
        //设置图标透明度
        iconImageView.setAlpha(1-mAlpha);
        iconOverImageView.setAlpha(mAlpha);
        //设置标题透明度
        titleTextView.setAlpha(1-mAlpha);
        titleOverTextView.setAlpha(mAlpha);
    }

    /**
     * 设置标题数据
     * */
    private void setTitleData(){
        titleTextView.setText(titleText);
        titleTextView.setTextColor(titleNolColor);
        if(isChecked){
            titleTextView.setAlpha(1-mAlpha);
        } else {
            titleTextView.setAlpha(mAlpha);
        }
    }

    /**
     * 设置第二层标题数据
     * */
    private void setTitleOverData(){
        titleOverTextView.setText(titleText);
        titleOverTextView.setTextColor(titleColor);
        if(isChecked){
            titleOverTextView.setAlpha(mAlpha);
        } else {
            titleOverTextView.setAlpha(1-mAlpha);
        }
    }

    /**
     * 选中之后重新设置数据
     * */
    private void setCheckedData(){
        refreshData();
    }

    /**
     * 设置完数据之后刷新
     * */
    private void refreshData(){
        mAlpha = 1.0f;
        setTitleData();
        setTitleOverData();
        setTabIconData();
        setTabIconOverData();
    }

    public String getTitleText() {
        return titleText;
    }

    public void setTitleText(String titleText) {
        this.titleText = titleText;
    }

    public Drawable getTabIcon() {
        return tabIcon;
    }

    public void setTabIcon(Drawable tabIcon) {
        this.tabIcon = tabIcon;
    }

    public boolean isChecked() {
        return isChecked;
    }
}