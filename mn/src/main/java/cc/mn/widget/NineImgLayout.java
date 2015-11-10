package cc.mn.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.List;

import cc.mn.R;

/**
 * 控件NineImgLayout类似微信，微博九宫格图片布局：不够完善，谨慎使用；
 * Date: 2015-09-22
 * Time: 10:02
 * Version 1.0
 *
 *  用法如下：
 *  1. 一般在adapter使用，NineImgLayout.setPics();
 *  2. 在displayPics()方法里，进行图片下载操作;
 *  3. 设置每张图片点击事件 setOnItemClickListener();
 */

public class NineImgLayout extends ViewGroup{
    private int phoneW = 720;

    private int oneOneWidth;
    private int oneOneHeight;

    private int mWidth;

    private int gap;

    private Rect[] picRects;

    private List<String> picUrls;

    private OnItemClickListener onItemClickListener = null;

    private String tag = "NineImgLayout";

    public NineImgLayout(Context context) {
        this(context, null);
    }

    public NineImgLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NineImgLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init(){
        for(int i=0; i<9; i++){ //初始化添加9个ImageView
            ImageView img = new ImageView(getContext());
            img.setId(i);
            this.addView(img);
        }
        //this.setBackgroundColor(getResources().getColor(R.color.baise));  //此处控件背景
        gap = getResources().getDimensionPixelSize(R.dimen.gap_pics);
        getPhoneWH();

    }

    private void setMobilePicsView(){
        int maxWidth = phoneW - dip2px(18 * 2);
        mWidth = Math.round(maxWidth * 1.0f * 4 / 5);

        picRects = null;

        int size = picUrls.size();
        int imgW = Math.round((mWidth - 2 * gap) * 1.0f / 3.0f);
        int imgH = imgW;
        LinearLayout.LayoutParams layoutParams = null;

        // 4个特殊情况，上2个下2个
        if (size == 4) {

            layoutParams = new LinearLayout.LayoutParams(mWidth, imgH * 2 + gap);

            picRects = new Rect[4];

            Rect rect = new Rect(0, 0, imgW, imgH);
            picRects[0] = rect;
            rect = new Rect(imgW + gap, 0, imgW * 2 + gap, imgH);
            picRects[1] = rect;
            rect = new Rect(0, imgH + gap, imgW, imgH * 2 + gap);
            picRects[2] = rect;
            rect = new Rect(imgW + gap, imgH + gap, imgW * 2 + gap, imgH * 2 + gap);
            picRects[3] = rect;
        } else {
            int oneWidth = 0;
            oneWidth = maxWidth / 3;
            int height = 0;
            switch (size) {
                case 1:
                    // 初始状态
                    if (oneOneHeight == 0) {
                        height = oneWidth;
                    } else {
                        height = Math.round(oneWidth *
                                // 原图的比例
                                (oneOneHeight * 1.0f / oneOneWidth));
                        if (height > oneWidth) {
                            height = oneWidth;
                            oneWidth = Math.round(height / (oneOneHeight * 1.0f / oneOneWidth));
                        }
                    }

                    break;
                case 2:
                case 3:
                    height = imgH;
                    break;
                case 5:
                case 6:
                    height = imgH * 2 + gap;
                    break;
                case 7:
                case 8:
                case 9:
                    height = imgH * 3 + gap * 2;
                    break;
            }

            layoutParams = new LinearLayout.LayoutParams(mWidth, height);

            // 当只有一个图片的时候，特殊处理
            if (size == 1) {
                Rect oneRect = new Rect();
                oneRect.left = 0;
                oneRect.top = 0;
                oneRect.right = oneWidth;
                oneRect.bottom = height;

                int imgSize = oneRect.right - oneRect.left;
                //oneRect.left = dip2px(32);
                //oneRect.right += oneRect.left;

                picRects = new Rect[]{ oneRect };
            } else {
                picRects = new Rect[size];
                for (int i = 0; i < size; i++)
                    picRects[i] = getSmallRectArr()[i];
            }
        }

        setLayoutParams(layoutParams);

        displayPics();

        // 重新绘制
        requestLayout();
    }

    private static Rect[] small9ggRectArr = null;
    private Rect[] getSmallRectArr() {
        if (small9ggRectArr != null)
            return small9ggRectArr;

        int imgW = Math.round((mWidth - 2 * gap) * 1.0f / 3.0f);
        int imgH = imgW;

        Rect[] tempRects = new Rect[9];

        Rect rect = new Rect(0, 0, imgW, imgH);
        tempRects[0] = rect;
        rect = new Rect(imgW + gap, 0, imgW * 2 + gap, imgH);
        tempRects[1] = rect;
        rect = new Rect(mWidth - imgW, 0, mWidth, imgH);
        tempRects[2] = rect;

        rect = new Rect(0, imgH + gap, imgW, imgH * 2 + gap);
        tempRects[3] = rect;
        rect = new Rect(imgW + gap, imgH + gap, imgW * 2 + gap, imgH * 2 + gap);
        tempRects[4] = rect;
        rect = new Rect(mWidth - imgW, imgH + gap, mWidth, imgH * 2 + gap);
        tempRects[5] = rect;

        rect = new Rect(0, imgH * 2 + gap * 2, imgW, imgH * 3 + gap * 2);
        tempRects[6] = rect;
        rect = new Rect(imgW + gap, imgH * 2 + gap * 2, imgW * 2 + gap, imgH * 3 + gap * 2);
        tempRects[7] = rect;
        rect = new Rect(mWidth - imgW, imgH * 2 + gap * 2, mWidth, imgH * 3 + gap * 2);
        tempRects[8] = rect;

        small9ggRectArr = tempRects;
        return small9ggRectArr;
    }


    public void displayPics(){
        if (picRects == null || picUrls == null || picUrls.size() == 0)
            return;

        for (int i = 0; i < getChildCount(); i++) {
            ImageView imgView = (ImageView) getChildAt(i);
            // 隐藏多余的View
            if (i >= picRects.length) {
                getChildAt(i).setVisibility(View.GONE);
            } else {
                Rect imgRect = picRects[i];

                imgView.setVisibility(View.VISIBLE);
                // 如果是一个图片，就显示大一点
                int size = picUrls.size();
                if (size == 1) {
                    imgView.setScaleType(ImageView.ScaleType.CENTER_CROP); //FIT_CENTER
                } else {
                    imgView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                }
                imgView.setLayoutParams(new LayoutParams(imgRect.right - imgRect.left, imgRect.bottom - imgRect.top));

                String url = picUrls.get(i);

                //此处添加图片下载代码：

            }
        }
    }

    private void setWifiTimelinePicsView() {
        picRects = null;

        int gap = getResources().getDimensionPixelSize(R.dimen.gap_pics);
        mWidth = phoneW - 2 * getResources().getDimensionPixelSize(R.dimen.comm_h_gap);

        int size = picUrls.size();
        int random = 0;

        LinearLayout.LayoutParams layoutParams = null;
        Rect[] tempRects = new Rect[size];
        switch (size) {
            case 1:
                int imgW = mWidth/2;
                int imgH = Math.round(imgW * 4.0f / 3.0f);

                Rect rect = new Rect(0, 0, imgW, imgH);
                tempRects[0] = rect;

                layoutParams = new LinearLayout.LayoutParams(mWidth, imgH);
                break;
            case 2:
                imgW = (mWidth - gap) / 2;
                imgH = Math.round(imgW * 4.0f / 3.0f);

                rect = new Rect(0, 0, imgW, imgH);
                tempRects[0] = rect;
                rect = new Rect(imgW + gap, 0, mWidth, imgH);
                tempRects[1] = rect;

                layoutParams = new LinearLayout.LayoutParams(mWidth, imgH);
                break;
            case 3:
                int imgW02 = Math.round((mWidth - gap) * 3.0f / 7.0f);
                int imgH02 = imgW02;
                int imgW01 = Math.round((mWidth - gap) * 4.0f / 7.0f);
                int imgH01 = imgH02 * 2 + gap;
                // 见/doc/3_0.png
                if (random == 0) {
                    rect = new Rect(0, 0, imgW01, imgH01);
                    tempRects[0] = rect;
                    rect = new Rect(gap + imgW01, 0, mWidth, imgH02);
                    tempRects[1] = rect;
                    rect = new Rect(gap + imgW01, imgH02 + gap, mWidth, imgH01);
                    tempRects[2] = rect;
                }
                // 见/doc/3_1.png
                else if (random == 1) {
                    rect = new Rect(0, 0, imgW02, imgH02);
                    tempRects[0] = rect;
                    rect = new Rect(0, imgH02 + gap, imgW02, imgH01);
                    tempRects[1] = rect;
                    rect = new Rect(gap + imgW02, 0, mWidth, imgH01);
                    tempRects[2] = rect;
                }

                layoutParams = new LinearLayout.LayoutParams(mWidth, imgH01);
                break;
            case 4:
                imgW = Math.round((mWidth - gap) * 1.0f / 2);
                imgH = Math.round(imgW * 4.0f / 3.0f);

                rect = new Rect(0, 0, imgW, imgH);
                tempRects[0] = rect;
                rect = new Rect(gap + imgW, 0, mWidth, imgH);
                tempRects[1] = rect;
                rect = new Rect(0, imgH + gap, imgW, imgH * 2 + gap);
                tempRects[2] = rect;
                rect = new Rect(gap + imgW, imgH + gap, mWidth, imgH * 2 + gap);
                tempRects[3] = rect;

                layoutParams = new LinearLayout.LayoutParams(mWidth, imgH * 2 + gap);
                break;
            case 5:
                imgW01 = Math.round((mWidth - gap) * 1.0f / 2);
                imgH01 = Math.round(imgW01 * 4.0f / 3.0f);

                imgW02 = Math.round((mWidth - gap * 2) * 1.0f / 3);
                imgH02 = Math.round(imgW02 * 4.0f / 3.0f);

                int height = imgH01 + imgH02 + gap;
                // 见/doc/5_0.png
                if (random == 0) {
                    rect = new Rect(0, 0, imgW01, imgH01);
                    tempRects[0] = rect;
                    rect = new Rect(gap + imgW01, 0, mWidth, imgH01);
                    tempRects[1] = rect;
                    rect = new Rect(0, imgH01 + gap, imgW02, height);
                    tempRects[2] = rect;
                    rect = new Rect(imgW02 + gap, imgH01 + gap, imgW02 * 2 + gap, height);
                    tempRects[3] = rect;
                    rect = new Rect(mWidth - imgW02, imgH01 + gap, mWidth, height);
                    tempRects[4] = rect;
                }
                // 见/doc/5_1.png
                else if (random == 1) {
                    rect = new Rect(0, 0, imgW02, imgH02);
                    tempRects[0] = rect;
                    rect = new Rect(imgW02 + gap, 0, imgW02 * 2 + gap, imgH02);
                    tempRects[1] = rect;
                    rect = new Rect(mWidth - imgW02, 0, mWidth, imgH02);
                    tempRects[2] = rect;
                    rect = new Rect(0, imgH02 + gap, imgW01, height);
                    tempRects[3] = rect;
                    rect = new Rect(gap + imgW01, imgH02 + gap, mWidth, height);
                    tempRects[4] = rect;
                }

                layoutParams = new LinearLayout.LayoutParams(mWidth, imgH01 + imgH02 + gap);
                break;
            case 6:
                imgW01 = Math.round((mWidth - 2 * gap) * 1.0f / 3.0f);
                imgH01 = Math.round(imgW01 * 4.0f / 3.0f);

                imgW02 = imgW01 * 2 + gap;
                imgH02 = imgH01 * 2 + gap;

                height = imgH01 + imgH02 + gap;
                // 见/doc/6_0.png
                if (random == 0) {
                    rect = new Rect(0, 0, imgW01, imgH01);
                    tempRects[0] = rect;
                    rect = new Rect(gap + imgW01, 0, imgW01 * 2 + gap, imgH01);
                    tempRects[1] = rect;
                    rect = new Rect(mWidth - imgW01, 0, mWidth, imgH01);
                    tempRects[2] = rect;
                    rect = new Rect(0, imgH01 + gap, imgW02, height);
                    tempRects[3] = rect;
                    rect = new Rect(imgW02 + gap, imgH01 + gap, mWidth, height - imgH01 - gap);
                    tempRects[4] = rect;
                    rect = new Rect(imgW02 + gap, height - imgH01, mWidth, height);
                    tempRects[5] = rect;
                }
                // 见/doc/6_1.png
                else if (random == 1) {
                    rect = new Rect(0, 0, imgW01, imgH01);
                    tempRects[0] = rect;
                    rect = new Rect(0, imgH01 + gap, imgW01, imgH01 * 2 + gap);
                    tempRects[1] = rect;
                    rect = new Rect(gap + imgW01, 0, mWidth, imgH02);
                    tempRects[2] = rect;
                    rect = new Rect(0, height - imgH01, imgW01, height);
                    tempRects[3] = rect;
                    rect = new Rect(gap + imgW01, height - imgH01, gap + imgW01 * 2, height);
                    tempRects[4] = rect;
                    rect = new Rect(imgW02 + gap, height - imgH01, mWidth, height);
                    tempRects[5] = rect;
                }
                // 见/doc/6_2.png
                else if (random == 2) {
                    rect = new Rect(0, 0, imgW02, imgH02);
                    tempRects[0] = rect;
                    rect = new Rect(imgW02 + gap, 0, mWidth, imgH01);
                    tempRects[1] = rect;
                    rect = new Rect(gap + imgW02, imgH01 + gap, mWidth, imgH01 * 2 + gap);
                    tempRects[2] = rect;
                    rect = new Rect(0, height - imgH01, imgW01, height);
                    tempRects[3] = rect;
                    rect = new Rect(gap + imgW01, height - imgH01, gap + imgW01 * 2, height);
                    tempRects[4] = rect;
                    rect = new Rect(imgW02 + gap, height - imgH01, mWidth, height);
                    tempRects[5] = rect;
                }
                // 见/doc/6_3.png
                else if (random == 3) {
                    rect = new Rect(0, 0, imgW01, imgH01);
                    tempRects[0] = rect;
                    rect = new Rect(gap + imgW01, 0, imgW01 * 2 + gap, imgH01);
                    tempRects[1] = rect;
                    rect = new Rect(mWidth - imgW01, 0, mWidth, imgH01);
                    tempRects[2] = rect;
                    rect = new Rect(0, imgH01 + gap, imgW01, imgH01 * 2 + gap);
                    tempRects[3] = rect;
                    rect = new Rect(0, height - imgH01, imgW01, height);
                    tempRects[4] = rect;
                    rect = new Rect(imgW01 + gap, imgH01 + gap, mWidth, height);
                    tempRects[5] = rect;
                }
                layoutParams = new LinearLayout.LayoutParams(mWidth, height);
                break;
            case 7:
                imgW01 = Math.round((mWidth - 2 * gap) * 1.0f / 3.0f);
                imgH01 = Math.round(imgW01 * 4.0f / 3.0f);

                imgW02 = mWidth;
                imgH02 = Math.round(imgW02 * 3.0f / 4.0f);

                height = imgH01 * 2 + imgH02 + gap * 2;

                rect = new Rect(0, 0, imgW01, imgH01);
                tempRects[0] = rect;
                rect = new Rect(gap + imgW01, 0, imgW01 * 2 + gap, imgH01);
                tempRects[1] = rect;
                rect = new Rect(mWidth - imgW01, 0, mWidth, imgH01);
                tempRects[2] = rect;

                rect = new Rect(0, imgH01 + gap, imgW02, imgH01 + gap + imgH02);
                tempRects[3] = rect;

                rect = new Rect(0, imgH01 + gap * 2 + imgH02, imgW01, height);
                tempRects[4] = rect;
                rect = new Rect(gap + imgW01, imgH01 + gap * 2 + imgH02, imgW01 * 2 + gap, height);
                tempRects[5] = rect;
                rect = new Rect(mWidth - imgW01, imgH01 + gap * 2 + imgH02, mWidth, height);
                tempRects[6] = rect;

                layoutParams = new LinearLayout.LayoutParams(mWidth, height);
                break;
            case 8:
                imgW01 = Math.round((mWidth - 2 * gap) * 1.0f / 3.0f);
                imgH01 = Math.round(imgW01 * 4.0f / 3.0f);

                imgW02 = Math.round((mWidth - gap) * 1.0f / 2.0f);
                imgH02 = Math.round(imgW02 * 4.0f / 3.0f);

                height = imgH01 * 2 + imgH02 + gap * 2;

                rect = new Rect(0, 0, imgW01, imgH01);
                tempRects[0] = rect;
                rect = new Rect(gap + imgW01, 0, imgW01 * 2 + gap, imgH01);
                tempRects[1] = rect;
                rect = new Rect(mWidth - imgW01, 0, mWidth, imgH01);
                tempRects[2] = rect;

                rect = new Rect(0, imgH01 + gap, imgW02, imgH01 + gap + imgH02);
                tempRects[3] = rect;
                rect = new Rect(imgW02 + gap, imgH01 + gap, mWidth, imgH01 + gap + imgH02);
                tempRects[4] = rect;

                rect = new Rect(0, imgH01 + gap * 2 + imgH02, imgW01, height);
                tempRects[5] = rect;
                rect = new Rect(gap + imgW01, imgH01 + gap * 2 + imgH02, imgW01 * 2 + gap, height);
                tempRects[6] = rect;
                rect = new Rect(mWidth - imgW01, imgH01 + gap * 2 + imgH02, mWidth, height);
                tempRects[7] = rect;

                layoutParams = new LinearLayout.LayoutParams(mWidth, height);
                break;
            case 9:
                imgW01 = Math.round((mWidth - 2 * gap) * 1.0f / 3.0f);
                imgH01 = Math.round(imgW01 * 4.0f / 3.0f);

                height = imgH01 * 2 + imgH01 + gap * 2;

                rect = new Rect(0, 0, imgW01, imgH01);
                tempRects[0] = rect;
                rect = new Rect(gap + imgW01, 0, imgW01 * 2 + gap, imgH01);
                tempRects[1] = rect;
                rect = new Rect(mWidth - imgW01, 0, mWidth, imgH01);
                tempRects[2] = rect;

                rect = new Rect(0, imgH01 + gap, imgW01, imgH01 * 2 + gap);
                tempRects[3] = rect;
                rect = new Rect(gap + imgW01, imgH01 + gap, imgW01 * 2 + gap, imgH01 * 2 + gap);
                tempRects[4] = rect;
                rect = new Rect(mWidth - imgW01, imgH01 + gap, mWidth, imgH01 * 2 + gap);
                tempRects[5] = rect;

                rect = new Rect(0, imgH01 + gap * 2 + imgH01, imgW01, height);
                tempRects[6] = rect;
                rect = new Rect(gap + imgW01, imgH01 + gap * 2 + imgH01, imgW01 * 2 + gap, height);
                tempRects[7] = rect;
                rect = new Rect(mWidth - imgW01, imgH01 + gap * 2 + imgH01, mWidth, height);
                tempRects[8] = rect;

                layoutParams = new LinearLayout.LayoutParams(mWidth, height);
                break;
        }

        setLayoutParams(layoutParams);

        picRects = tempRects;

        displayPics();

        // 重新绘制
        requestLayout();
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (picRects == null)
            return;
        for (int i = 0; i < getChildCount(); i++) {
            // 隐藏多余的View
            if (i < picRects.length) {
                Rect imgRect = picRects[i];

                ImageView childView = (ImageView) getChildAt(i);
                setImgsOnClickListener(childView, i);
                childView.layout(imgRect.left, imgRect.top, imgRect.right, imgRect.bottom);
            }
            else {
                break;
            }
        }
    }

    public void setPics(List<String> picUrls) {
        this.picUrls = picUrls;
        if (picUrls == null || picUrls.size() == 0) {
            setVisibility(View.GONE);
        } else {
            setVisibility(View.VISIBLE);
            calculatePicSize();
        }
    }


    // 根据图片尺寸
    private void calculatePicSize() {
        setMobilePicsView();
        //setWifiTimelinePicsView();
    }

    /**
     * 回收内存
     * */
    public void recycle(){
        for(int i=0; i<getChildCount(); i++){
            ImageView img = (ImageView)getChildAt(i);
            Bitmap temp = img.getDrawingCache();
            if(temp!=null && !temp.isRecycled()){
                temp.recycle();
                temp = null;
                System.gc();
            }
        }
    }

    private void setImgsOnClickListener(final ImageView view, final int i){
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener!=null){
                    onItemClickListener.onItemClick(view, i);
                }
            }
        });
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface  OnItemClickListener{
        void onItemClick(View view, int index);
    }

    private int dip2px(int dipValue) {
        float reSize = getContext().getResources().getDisplayMetrics().density;
        return (int) ((dipValue * reSize) + 0.5);
    }

    /**
     * 获取手机分辨率
     * */
    private void getPhoneWH(){
        DisplayMetrics metric = new DisplayMetrics();
        ((Activity)getContext()).getWindowManager().getDefaultDisplay().getMetrics(metric);
        phoneW = metric.widthPixels;     // 屏幕宽度（像素）
    }
}
