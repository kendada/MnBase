package cc.mnbase.view.comment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

import cc.mnbase.fragment.comm.ImageConfigBuilder;
import cc.mnbase.fragment.comm.SImageLoadingListener;

/**
 * User: (1203596603@qq.com)
 * Date: 2015-10-26
 * Time: 09:47
 * Version 1.0
 */

public class CommentIconView extends View implements View.OnTouchListener{

    private int pw = 720; //屏幕分辨率

    private int countLine = 8; //每行头像的个数

    private int countColumn = 2; //总共两行

    private int itemWidth; //每个Item宽高

    private int itemMar = 10; //每个Item间隔

    private OnItemClickListener onItemClickListener;  //点击事件

    private Paint mPaint; //画笔

    private Rect mRect;

    private Context mContext;

    private Rect[] rects = new Rect[countLine]; //矩形框

    private Bitmap[] bitmaps = new Bitmap[countLine];

    private List<String> mList; //图片地址

    private String tag = CommentIconView.class.getSimpleName();

    public CommentIconView(Context context) {
        this(context, null);
    }

    public CommentIconView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommentIconView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    /**
     * 初始化
     * */
    private void init(){
        //ViewGroup.LayoutParams vp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
      //          itemWidth+itemMar);
        //this.setLayoutParams(vp);

        DisplayMetrics dm = new DisplayMetrics();
        ((Activity)mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);
        pw = dm.widthPixels; //获取屏幕分辨率

        itemWidth = (pw-itemMar*8-20)/countLine; //每个Item的大小

        mRect = new Rect();

        mPaint = new Paint();
        mPaint.setColor(Color.GRAY);
        mPaint.setAntiAlias(true);

        for(int i=0; i<rects.length; i++){
            rects[i] = new Rect(itemWidth*i+itemMar*(i+1), 0, itemWidth*(i+1)+itemMar*(i+1), itemWidth);
        }

        this.setOnTouchListener(this);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        for(int i=0; i<countLine; i++){
            canvas.drawRect(rects[i], mPaint);
        }
        for(int i=0; i<countLine; i++){
            Bitmap bitmap = bitmaps[i];
            Rect rect = rects[i];
            if(bitmap!=null){
                canvas.drawBitmap(bitmap, rect.left, rect.top, mPaint);
            }
        }
        if(mRect!=null){
            canvas.drawRect(mRect, mPaint);
        }
        Log.i(tag, "----onDraw()"+canvas);
        super.onDraw(canvas);
    }

    /**
     * 添加头像
     * */
    public void addIcon(String url){
        if(mList==null){
            mList = new ArrayList<>();
        }
        mList.add(url);
    }

    /**
     * 批量添加头像
     * */
    public void addIcons(List<String> urls){
        Log.i(tag, "----138----"+urls);
        if(mList==null){
            mList = new ArrayList<>();
        }
        mList.addAll(urls);

        ImageSize imageSize = new ImageSize(itemWidth, itemWidth);

        for(int i=0; i<urls.size(); i++){
            final int finalI = i;
            final Rect rect = rects[i];
            ImageLoader.getInstance().loadImage(urls.get(i), imageSize, ImageConfigBuilder.USER_HEAD_HD_OPTIONS,
                    new SImageLoadingListener() {
                        @Override
                        public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                            Log.i(tag, "-----144-----图片加载完成");
                            bitmaps[finalI] = bitmap;
                            invalidate(rect.left, rect.top, rect.right, rect.bottom); //局部刷新
                            Log.i(tag, "----148---刷新View");
                        }
                    });

        }
    }

    /**
     * 删除一个，只能删除自己的
     * */
    public void deleteIcon(){

    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        int index = 0;
        Rect rect = null;
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(y<=itemWidth){
                    for(int i=0; i<rects.length; i++){
                        rect = rects[i];
                        mRect = rect;
                        Log.i(tag, "-----184---刷新---");
                        if(x>rect.left && x<rect.right){
                            if(onItemClickListener!=null){
                                onItemClickListener.onClick(i, mList.get(i), -1);
                            }
                            Log.i(tag, "----x="+x+"-----i="+i);
                            index = i;
                            invalidate(rect.left, rect.top, rect.right, rect.bottom);
                        }
                        return true;
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:

                break;
            case MotionEvent.ACTION_UP:
                Log.i(tag, "-----201-----"+rect);
                if(mRect!=null){
                    invalidate(mRect.left, mRect.top, mRect.right, mRect.bottom);
                }
                break;
        }
        return true;
    }

    /**
     * 点击每个头像回调方法
     * */
    public interface  OnItemClickListener{
        void onClick(int position, String url, int id);
    }

}
