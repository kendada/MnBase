package cc.mnbase.mvp3.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cc.mnbase.R;
import cc.mnbase.mvp3.model.LoveData;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2016-02-15
 * Time: 14:44
 * Version 1.0
 */

public class MainAdapter extends BaseAdapter {

    private List<LoveData> mDatas;
    private Context mContext;

    public MainAdapter(Context context, List<LoveData> datas){
        mDatas = datas;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mDatas != null ? mDatas.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyView mv = null;

        if(convertView == null){
            mv = new MyView();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_mvp3_main_layout, parent, false);
            convertView.setTag(mv);

            mv.text = (TextView)convertView.findViewById(R.id.text);
        } else {
            mv = (MyView) convertView.getTag();
        }

        LoveData loveData = mDatas.get(position);
        if (loveData != null){
            mv.text.setText(loveData.content);
        }


        return convertView;
    }

    class MyView {
        TextView text;
    }
}
