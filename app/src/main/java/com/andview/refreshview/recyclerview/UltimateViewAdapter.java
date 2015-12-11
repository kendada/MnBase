package com.andview.refreshview.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.andview.refreshview.callback.IFooterCallBack;

import java.util.Collections;
import java.util.List;

/**
 * An abstract adapter which can be extended for Recyclerview
 */
public abstract class UltimateViewAdapter<VH extends RecyclerView.ViewHolder>
		extends RecyclerView.Adapter<VH> implements
		StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder> {

	protected View customLoadMoreView = null;

	@Override
	public VH onCreateViewHolder(ViewGroup parent, int viewType) {

		if (viewType == VIEW_TYPES.FOOTER) {
			VH viewHolder = getViewHolder(customLoadMoreView);
			if (getAdapterItemCount() == 0)
				viewHolder.itemView.setVisibility(View.GONE);
			return viewHolder;
		} else if (viewType == VIEW_TYPES.CHANGED_FOOTER) {
			VH viewHolder = getViewHolder(customLoadMoreView);
			if (getAdapterItemCount() == 0)
				viewHolder.itemView.setVisibility(View.GONE);
			return viewHolder;
		}
		// else if (viewType==VIEW_TYPES.STICKY_HEADER){
		// return new
		// UltimateRecyclerviewViewHolder(LayoutInflater.from(parent.getContext())
		// .inflate(R.layout.stick_header_item, parent, false));
		// }

		return onCreateViewHolder(parent);

	}

	public abstract VH getViewHolder(View view);

	public abstract VH onCreateViewHolder(ViewGroup parent);

	/**
	 * Using a custom LoadMoreView
	 * 
	 * @param footerView
	 *            the inflated view
	 */
	public void setCustomLoadMoreView(View footerView) {
		if (footerView instanceof IFooterCallBack) {
			customLoadMoreView = footerView;
		} else {
			throw new RuntimeException(
					"footerView must be implementes IFooterCallBack!");
		}
	}

	/**
	 * Changing the loadmore view
	 * 
	 * @param customview
	 *            the inflated view
	 */
	public void swipeCustomLoadMoreView(View customview) {
		customLoadMoreView = customview;
		isLoadMoreChanged = true;
	}

	public View getCustomLoadMoreView() {
		return customLoadMoreView;
	}

	public boolean isLoadMoreChanged = false;

	@Override
	public int getItemViewType(int position) {
		if (position == getItemCount() - 1 && customLoadMoreView != null) {
			if (isLoadMoreChanged) {
				return VIEW_TYPES.CHANGED_FOOTER;
			} else {
				return VIEW_TYPES.FOOTER;
			}
		} else
			return VIEW_TYPES.NORMAL;
	}

	/**
	 * Returns the total number of items in the data set hold by the adapter.
	 * 
	 * @return The total number of items in this adapter.
	 */
	@Override
	public int getItemCount() {
		int Footer = 0;
		if (customLoadMoreView != null)
			Footer++;
		return getAdapterItemCount() + Footer;
	}

	/**
	 * Returns the number of items in the adapter bound to the parent
	 * RecyclerView.
	 * 
	 * @return The number of items in the bound adapter
	 */
	public abstract int getAdapterItemCount();

	public void toggleSelection(int pos) {
		notifyItemChanged(pos);
	}

	public void clearSelection(int pos) {
		notifyItemChanged(pos);
	}

	public void setSelected(int pos) {
		notifyItemChanged(pos);
	}

	/**
	 * Swap the item of list
	 * 
	 * @param list
	 *            data list
	 * @param from
	 *            position from
	 * @param to
	 *            position to
	 */
	public void swapPositions(List<?> list, int from, int to) {
		Collections.swap(list, from, to);
	}

	/**
	 * Insert a item to the list of the adapter
	 * 
	 * @param list
	 *            data list
	 * @param object
	 *            object T
	 * @param position
	 *            position
	 * @param <T>
	 *            in T
	 */
	public <T> void insert(List<T> list, T object, int position) {
		list.add(position, object);
		notifyItemInserted(position);
	}

	/**
	 * Remove a item of the list of the adapter
	 * 
	 * @param list
	 *            data list
	 * @param position
	 *            position
	 */
	public void remove(List<?> list, int position) {
		if (list.size() > 0) {
			notifyItemRemoved(position);
		}
	}

	/**
	 * Clear the list of the adapter
	 * 
	 * @param list
	 *            data list
	 */
	public void clear(List<?> list) {
		int size = list.size();
		list.clear();
		notifyItemRangeRemoved(0, size);
	}

	@Override
	public long getHeaderId(int position) {
		if (customLoadMoreView != null && position >= getItemCount() - 1)
			return -1;
		if (getAdapterItemCount() > 0)
			return generateHeaderId(position);
		else
			return -1;
	}

	public abstract long generateHeaderId(int position);

	protected class VIEW_TYPES {
		public static final int NORMAL = 0;
		public static final int FOOTER = 2;
		public static final int CHANGED_FOOTER = 3;
	}

	protected enum AdapterAnimationType {
		AlphaIn, SlideInBottom, ScaleIn, SlideInLeft, SlideInRight,
	}

	protected OnStartDragListener mDragStartListener = null;

	/**
	 * Listener for manual initiation of a drag.
	 */
	public interface OnStartDragListener {

		/**
		 * Called when a view is requesting a start of a drag.
		 * 
		 * @param viewHolder
		 *            The holder of the view to drag.
		 */
		void onStartDrag(RecyclerView.ViewHolder viewHolder);
	}
}
