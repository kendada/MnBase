package cc.mnbase.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import cc.mnbase.R;
import cc.mnbase.adapter.base.AdapterHolder;
import cc.mnbase.adapter.base.MnBaseAdapter;
import cc.mnbase.model.Girl;
import cc.mnbase.util.MnAppUtil;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-10-30
 * Time: 11:40
 * Version 1.0
 */

public class MnTestAdapter extends MnBaseAdapter<Girl> {

    private int pw = 480;

    public MnTestAdapter(Context context, List<Girl> datas) {
        super(context, datas);
        int margin = context.getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin);
        pw = MnAppUtil.getPhoneW(context) - margin;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AdapterHolder viewHolder = AdapterHolder.get(position, convertView, parent, R.layout.adapter_main);
        ImageView img = viewHolder.getView(R.id.img);
        TextView text = viewHolder.getView(R.id.text);
        img.setMinimumHeight(pw);
        img.setMinimumWidth(pw);
        Girl girl = mDatas.get(position);
        if(girl!=null){
            Picasso.with(mContext).load(girl.getAvatarUrl()).resize(pw, pw).centerCrop().into(img);
            text.setText("模特 "+girl.getRealName()+"\r\n" +
                    "身高 "+girl.getHeight() +"cm 体重 "+girl.getWeight()+"kg");
        }
        return viewHolder.getConvertView();
    }
}
