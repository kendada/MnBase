package cc.mnbase.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import cc.mnbase.R;
import cc.mnbase.image.ImageLoader;
import cc.mnbase.image.ImageSize;
import cc.mnbase.model.Emm;
import cc.mnbase.util.ImageConfigBuilder;
import cc.mnbase.util.MnAppUtil;


public class WaterPull2Adapter extends BaseAdapter{

	private Context context;

	private ImageLoader imageLoader;

	public final static String URL2 = "http://img.emm.cc";

	private List<Emm> list;

	private int w = 0, h = 0;

	private String tag = WaterPull2Adapter.class.getSimpleName();

	public WaterPull2Adapter(Context context, List<Emm> list){
		this.context = context;
		this.list = list;
		imageLoader = new ImageLoader(context);
		w = MnAppUtil.getPhoneW(context);
		h = w*396/(264*2);
	}
	
	@Override
	public int getCount() {
		return list!=null?list.size():0;
	}

	@Override
	public Object getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int index, View view, ViewGroup arg2) {
		MyView mv = null;
		if(view == null){
			mv = new MyView();
			view = LayoutInflater.from(context).inflate(R.layout.adapter_water_pull, null);
			view.setTag(mv);
			
			mv.con_img = (ImageView)view.findViewById(R.id.con_img);
			RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(w/2, (int) h);
			mv.con_img.setLayoutParams(rlp);
			mv.content = (TextView)view.findViewById(R.id.content);
			mv.top_layout = (RelativeLayout)view.findViewById(R.id.top_layout);
			
		} else {
			mv = (MyView) view.getTag();
		}
		try{
			Emm emm = list.get(index);
			mv.con_img.setTag(URL2 + emm.getLitpic());
			imageLoader.loadImage(URL2 + emm.getLitpic(), new ImageSize(w / 2, (int) h), mv.con_img,
					ImageConfigBuilder.USER_HEAD_HD_OPTIONS, null, null);
			if(emm.getTitle()!=null){
				mv.content.setText(emm.getTitle());
			}
		} catch (Exception e){
			e.printStackTrace();
		}
		return view;
	}

	class MyView{
		RelativeLayout top_layout;
		ImageView con_img;
		TextView content;
	}

}
