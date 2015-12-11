package cc.mnbase.view.list.abs;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-12-07
 * Time: 09:24
 * Version 1.0
 */

public class SwipeCardView extends AdapterView<ListAdapter> {

    private ListAdapter mAdapter;

    private int mPostiton;

    private View currtView;

    private String tag = SwipeCardView.class.getSimpleName();

    public SwipeCardView(Context context) {
        super(context);
    }

    public SwipeCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SwipeCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public ListAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        mAdapter = adapter;
        Log.i(tag, "----45----mAdapter="+mAdapter);
        if(mAdapter == null) return;
        currtView = mAdapter.getView(mPostiton, null, this);
    }

    @Override
    public View getSelectedView() {
        return null;
    }

    @Override
    public void setSelection(int position) {
        mPostiton = position;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if(currtView==null) return;
        Log.i(tag, "----72----currtView="+currtView);

        setMeasuredDimension(widthSize, heightSize);

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if(mAdapter == null) return;
        View view = mAdapter.getView(mPostiton, null, this);
        this.addView(view);
        Log.i(tag, "----82----view=" + view);

    }
}
