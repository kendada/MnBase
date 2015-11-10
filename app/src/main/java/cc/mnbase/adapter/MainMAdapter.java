package cc.mnbase.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cc.mnbase.R;
import cc.mnbase.view.list.menu.MenuLinearLayout;

/**
 * User: (1203596603@qq.com)
 * Date: 2015-08-17
 * Time: 21:46
 * Version 1.0
 */

public class MainMAdapter extends BaseAdapter {

    private Context mContext = null;
    private List<String> mList = null;

    public MainMAdapter(Context context, List<String> list) {
        mContext = context;
        mList = list;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        final MenuLinearLayout menuLinearLayout;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_menu_list_layout, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        menuLinearLayout = (MenuLinearLayout)convertView;
        holder.menu_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "删除菜单"+position, Toast.LENGTH_SHORT).show();
                if(menuLinearLayout!=null){
                    menuLinearLayout.closeMenu();
                }
                mList.remove(position);
                notifyDataSetChanged();
            }
        });
        holder.menu_to_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "置顶"+position, Toast.LENGTH_SHORT).show();
            }
        });
        String str = mList.get(position);
        if(str!=null){
            holder.text.setText(str);
        }

        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.text)
        TextView text;
        @Bind(R.id.menu_delete)
        TextView menu_delete;
        @Bind(R.id.menu_to_top)
        TextView menu_to_top;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
