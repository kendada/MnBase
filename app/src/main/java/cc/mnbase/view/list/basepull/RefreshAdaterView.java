package cc.mnbase.view.list.basepull;

import android.content.Context;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.util.AttributeSet;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;

/**
 * User: 靳世坤(1203596603@qq.com)
 * Date: 2015-10-21
 * Time: 22:54
 * Version 1.0
 */

public abstract class RefreshAdaterView<T extends AbsListView> extends RefreshLayoutBase<T> {

    public RefreshAdaterView(Context context) {
        this(context, null);
    }

    public RefreshAdaterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshAdaterView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setAdapter(ListAdapter adapter) {
        mContentView.setAdapter(adapter);
    }

    public ListAdapter getAdapter() {
        return mContentView.getAdapter();
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener){
        mContentView.setOnItemClickListener(onItemClickListener);
    }

    public AdapterView.OnItemClickListener getOnItemClickListener(){
        return mContentView.getOnItemClickListener();
    }

    public void setOnItemLongClickListener(AdapterView.OnItemLongClickListener onItemLongClickListener){
        mContentView.setOnItemLongClickListener(onItemLongClickListener);
    }

    public AdapterView.OnItemLongClickListener getOnItemLongClickListener(){
       return mContentView.getOnItemLongClickListener();
    }

}
