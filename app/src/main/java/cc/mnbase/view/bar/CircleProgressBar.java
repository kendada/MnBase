package cc.mnbase.view.bar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import org.w3c.dom.Attr;

import cc.mnbase.R;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-10-28
 * Time: 14:15
 * Version 1.0
 */

public class CircleProgressBar extends View {

    private Context mContext;

    private Paint paint; //画笔

    private int roundColor; //圆环颜色

    private int roundProgressColor; //圆环进度颜色

    private float roundWidth; //圆环宽度

    private int max; //最大进度

    private int progress; //进度

    private int style; //进度条风格，实心或者空心

    public final static int STROKE = 0;
    public final static int FILL = 1;

    public CircleProgressBar(Context context) {
        this(context, null);
    }

    public CircleProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init(attrs);
    }

    /**
     * 初始化
     * */
    private void init(AttributeSet attrs){
        paint = new Paint();

        TypedArray ta = mContext.obtainStyledAttributes(attrs, R.styleable.CircleProgressBar);
        //获取属性
        roundColor = ta.getColor(R.styleable.CircleProgressBar_roundColor, Color.BLUE);
        roundProgressColor = ta.getColor(R.styleable.CircleProgressBar_roundProgressColor, Color.GRAY);
        roundWidth = ta.getDimension(R.styleable.CircleProgressBar_roundWidth, 5);
        max = ta.getInteger(R.styleable.CircleProgressBar_max, 100);
        progress = ta.getInteger(R.styleable.CircleProgressBar_progress, 0);
        style = ta.getInt(R.styleable.CircleProgressBar_style, 0);

        ta.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //画最外层的大圆环
        int center = getWidth()/2; //圆心坐标
        int radius = (int)(center - roundWidth/2); // 圆环半径

        paint.setAntiAlias(true); //消除锯齿
        paint.setColor(roundColor); //设置圆环的颜色
        paint.setStyle(Paint.Style.STROKE); //设置空心
        paint.setStrokeWidth(roundWidth); //设置圆环宽度

        canvas.drawCircle(center, center, radius, paint); //在画布上画圆

        //设置进度是实心还是空心
        paint.setStrokeWidth(roundWidth);
        paint.setColor(roundProgressColor);
        RectF oval = new RectF(center-radius, center-radius, center+radius, center+radius);

        switch (style){
            case STROKE:
                paint.setStyle(Paint.Style.STROKE);
                canvas.drawArc(oval, 270, 360 * progress / max, false, paint); //根据进度画弧度
                break;
            case FILL:
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                if(progress != 0){
                    canvas.drawArc(oval, 270, 360*progress/max, true, paint); //根据进度画弧度
                }
                break;
        }
    }

    public synchronized int getMax(){
        return max;
    }

    public synchronized void setMax(int max){
        if(max < 0) return;
        this.max = max;
    }

    public synchronized int getProgress(){
        return progress;
    }

    public synchronized void setProgress(int progress){
        if(progress < 0) return;
        if(progress > max) this.progress = max;
        if(progress <= max) {
            this.progress = progress;
            postInvalidate();
        }
    }

    public int getRoundColor() {
        return roundColor;
    }

    public void setRoundColor(int roundColor) {
        this.roundColor = roundColor;
    }

    public int getRoundProgressColor() {
        return roundProgressColor;
    }

    public void setRoundProgressColor(int roundProgressColor) {
        this.roundProgressColor = roundProgressColor;
    }

    public float getRoundWidth() {
        return roundWidth;
    }

    public void setRoundWidth(float roundWidth) {
        this.roundWidth = roundWidth;
    }

}
