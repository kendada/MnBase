package cc.mnbase.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

import cc.mnbase.R;
import cc.mnbase.adapter.base.AdapterHolder;
import cc.mnbase.adapter.base.MnBaseAdapter;
import cc.mnbase.fragment.comm.ImageConfigBuilder;
import cc.mnbase.model.Emm;
import cc.mnbase.model.Girl;
import cc.mnbase.util.MnAppUtil;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-10-30
 * Time: 11:40
 * Version 1.0
 */

public class SwipeCard2Adapter extends MnBaseAdapter<Emm> {

    private int pw = 480;

    public final static String URL2 = "http://img.emm.cc";

    public SwipeCard2Adapter(Context context, List<Emm> datas) {
        super(context, datas);
        pw = MnAppUtil.getPhoneW(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AdapterHolder viewHolder = AdapterHolder.get(position, convertView, parent, R.layout.adapter_swipe_card2);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(pw, pw);
        viewHolder.getConvertView().setLayoutParams(params);
        ImageView img = viewHolder.getView(R.id.img);
        Emm girl = mDatas.get(position);
        if(girl!=null) {
            ImageLoader.getInstance().displayImage(URL2+girl.getLitpic(), img);
        }
        return viewHolder.getConvertView();
    }
}
