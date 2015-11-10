package cc.mnbase.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cc.mnbase.R;
import cc.mnbase.model.Girl;
import cc.mnbase.util.MnAppUtil;

/**
 * User: (1203596603@qq.com)
 * Date: 2015-08-17
 * Time: 21:46
 * Version 1.0
 */

public class MainAdapter extends BaseAdapter {

    private Context mContext = null;
    private List<Girl> mList = null;

    private int pw = 480;

    private String tag = MainAdapter.class.getSimpleName();

    public MainAdapter(Context context, List<Girl> list) {
        mContext = context;
        mList = list;
        int margin = context.getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin);
        pw = MnAppUtil.getPhoneW(context) - margin;

    }

    @Override
    public int getCount() {
        return mList != null ? mList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_main, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.img.setMinimumHeight(pw);
        holder.img.setMinimumWidth(pw);
        Girl girl = mList.get(position);
        if(girl!=null){
            Picasso.with(mContext).load(girl.getAvatarUrl()).resize(pw, pw).centerCrop().into(holder.img);
            holder.text.setText("模特 "+girl.getRealName()+"\r\n" +
                    "身高 "+girl.getHeight() +"cm 体重 "+girl.getWeight()+"kg");
        }

        return convertView;
    }

    /**42.0.2311.152
        44.0.2403.81 beta (64-bit)
        V44.0.2403.9
     44.0.2403.155
     * This class contains all butterknife-injected Views & Layouts from layout file 'adapter_main.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    static class ViewHolder {
        @Bind(R.id.img)
        ImageView img;
        @Bind(R.id.text)
        TextView text;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
