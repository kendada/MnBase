package cc.mnbase.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import cc.mnbase.R;
import cc.mnbase.view.comment.CommentIconView;

/**
 * User: (1203596603@qq.com)
 * Date: 2015-10-26
 * Time: 16:03
 * Version 1.0
 */

public class IconViewAdapter extends BaseAdapter {

    private List<List<String>> mList;

    private Context mContext;

    private String tag = IconViewAdapter.class.getSimpleName();

    public IconViewAdapter(Context context, List<List<String>> list){
        mList = list;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mList!=null?mList.size():0;
    }

    @Override
    public Object getItem(int position) {
        return mList!=null?mList.get(position):null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_icon_view_layout, null);
            convertView.setTag(holder);

            holder.comment_icon_view = (CommentIconView)convertView.findViewById(R.id.comment_icon_view);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        List<String> list = mList.get(position);
        holder.comment_icon_view.addIcons(list);
        return convertView;
    }

    class ViewHolder{
        CommentIconView comment_icon_view;
    }

}
