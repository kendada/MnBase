package cc.mnbase.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
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


public class WaterPullAdapter extends BaseAdapter implements AbsListView.OnScrollListener{

	private Context context;

	private ImageLoader imageLoader;

	public final static String URL2 = "http://img.emm.cc";

	private List<Emm> list;

	private int w = 0, h = 0;

	private boolean isFirstEnter = true; //记录是否是刚打开程序

	private int mFirstVisibleItem; //一屏的第一个Item位置

	private int mVisibleItemCount; //一屏所有Item的个数

	private GridView gridView;

	private String tag = WaterPullAdapter.class.getSimpleName();
	
	public WaterPullAdapter(GridView gridView, List<Emm> list){
		this.gridView = gridView;
		this.context = gridView.getContext();
		this.list = list;
		imageLoader = new ImageLoader(context);
		gridView.setOnScrollListener(this);
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

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE){
			showImage(mFirstVisibleItem, mVisibleItemCount);
		} else {
			imageLoader.cancelTask();
			Log.i(tag, "-----109-----停止加载");
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		mFirstVisibleItem = firstVisibleItem;
		mVisibleItemCount = visibleItemCount;

		if(isFirstEnter && visibleItemCount>0){
			showImage(mFirstVisibleItem, mVisibleItemCount);
			isFirstEnter = false;
		}

	}

	private void showImage(int firstVisibleItem, int visibleItemCount){
		for(int i=firstVisibleItem; i<firstVisibleItem+visibleItemCount; i++){
			String mImageUrl = URL2+list.get(i).getLitpic();
			ImageView imageView = (ImageView)gridView.findViewWithTag(mImageUrl);
			imageLoader.loadImage(mImageUrl, new ImageSize(w / 2, (int) h), imageView,
					ImageConfigBuilder.USER_HEAD_HD_OPTIONS, null, null);
		}
	}

	class MyView{
		RelativeLayout top_layout;
		ImageView con_img;
		TextView content;
	}

}
