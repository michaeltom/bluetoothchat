
package net.flyget.bluetoothchat.view;

import java.util.Hashtable;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

public class DrawerHScrollView extends HorizontalScrollView {
	private static final String TAG = "DrawerHScrollView";
	
	private int currentPage = 0;
	private int totalPages = 1;
	private static Hashtable<Integer, Integer> positionLeftTopOfPages = new Hashtable();
	private LinearLayout mPageIndicLayout;
	private Context mContext;

	public DrawerHScrollView(Context context) {
		super(context);
		this.mContext = context;
	}

	public DrawerHScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
	}

	public DrawerHScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mContext = context;
	}
	
	public void cleanup(){
		currentPage = 0;
		totalPages = 1;
		if(positionLeftTopOfPages != null){
			positionLeftTopOfPages.clear();
		}
	}
	
	public void setParameters(int totalPages, int currentPage, int scrollDisX, int space) {
		Log.d(TAG, "~~~~~setParameters totalPages:"+totalPages +",currentPage:"+ currentPage +",scrollDisX:"+scrollDisX);
		this.totalPages = totalPages;
		this.currentPage = currentPage;
		positionLeftTopOfPages.clear();
		for (int i = 0;i < totalPages;i++){
			int posx = (scrollDisX) * i - space;
			positionLeftTopOfPages.put(i, posx);
			Log.d(TAG, "~~~~~setParameters i:"+i +",posx:"+posx);
		}
		smoothScrollTo(0, 0);
		setPageIndicLayout();
		if(mPageIndicLayout != null){
			updateDrawerPageLayout(totalPages, currentPage);
		}
	}
	
	public void setPageIndicLayout(){
		// 添加表情多页图标布局
		mPageIndicLayout = new LinearLayout(mContext);
		mPageIndicLayout.setGravity(Gravity.CENTER);
		ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		mPageIndicLayout.setLayoutParams(params);
		ViewParent parent = this.getParent();
		if(parent instanceof LinearLayout){
			LinearLayout layout = (LinearLayout)parent;
			layout.addView(mPageIndicLayout, new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		}
	}

	@Override
	public void fling(int velocityX) {
		Log.v(TAG, "-->fling velocityX:"+velocityX);
		boolean change_flag = false;
		if (velocityX > 0 && (currentPage < totalPages - 1)){
			currentPage++;
			change_flag = true;
		} else if (velocityX < 0 && (currentPage > 0)){
			currentPage--;
			change_flag = true;
		}
		if (change_flag){
			int postionTo = (Integer)positionLeftTopOfPages.get(new Integer(currentPage)).intValue();
			Log.v(TAG, "------smoothScrollTo posx:"+postionTo);
			smoothScrollTo(postionTo, 0);
			updateDrawerPageLayout(totalPages, currentPage);
		}
		//super.fling(velocityX);
	}
	
	public void updateDrawerPageLayout(int total_pages, int sel_page) {
		Log.e(TAG, "~~~updateBooksPageLayout total_pages:" + total_pages
				+ ",sel_page:" + sel_page);
		mPageIndicLayout.removeAllViews();
		if (total_pages <= 0 || sel_page < 0 || sel_page >= total_pages) {
			Log.e(TAG, "total_pages or sel_page is outofrange.");
			return;
		}
		for (int i = 0; i < total_pages; i++) {
			if (i != 0) {
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				params.setMargins(5, 0, 0, 0);
				mPageIndicLayout.addView(new PageIndicatorView(mContext), params);
			} else {
				mPageIndicLayout.addView(new PageIndicatorView(mContext));
			}
		}
		PageIndicatorView selItem = (PageIndicatorView) mPageIndicLayout
				.getChildAt(sel_page);
		selItem.setSelectedView(true);
	}
}