package cc.mnbase.adapter.base;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-10-30
 * Time: 10:51
 * Version 1.0
 */

public abstract class MnBaseAdapter<T> extends BaseAdapter {

    protected List<T> mDatas;
    protected Context mContext;

    public MnBaseAdapter(Context context, List<T> datas){
        mDatas = datas;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mDatas!=null ? mDatas.size() : 0;
    }

    @Override
    public T getItem(int position) {
        return mDatas!=null ? mDatas.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
