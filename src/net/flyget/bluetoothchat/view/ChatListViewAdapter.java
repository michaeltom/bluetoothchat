package net.flyget.bluetoothchat.view;

import java.util.ArrayList;
import java.util.HashMap;

import net.flyget.bluetoothchat.R;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.Spanned;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class ChatListViewAdapter extends BaseAdapter {
	public static final int ROLE_OWN = 0;
	public static final int ROLE_TARGET = 1;
	public static final int ROLE_OTHER = 2;
	public static final String KEY_ROLE = "role";
	public static final String KEY_TEXT = "text";
	public static final String KEY_DATE = "date";
	public static final String KEY_NAME = "name";
	public static final String KEY_SHOW_MSG = "show_msg";
	
	private Context mContext;
	
	private ArrayList<HashMap<String, Object>> mDatalist;
	
	private LayoutInflater mInflater;
	
	private DisplayMetrics dm;
	

	public ChatListViewAdapter(Context context, ArrayList<HashMap<String, Object>> data) {
		super();
		mContext = context;
		mDatalist = data;
		mInflater = LayoutInflater.from(context);		
		dm = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm); 
	}

	@Override
	public int getCount() {
		return mDatalist.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView text;
		TextView date;
		if(convertView == null){
			View layout = mInflater.inflate(R.layout.chat_list_item_layout, null);
			if(layout == null)
				return null;
			// 聊天内容TextView
			text = (TextView) layout.findViewById(R.id.tvText);
			ClickListener listener = new ClickListener(text);
			text.setOnClickListener(listener);
			text.setTag(listener);
			
			// 时间日期TextView
			date = (TextView) layout.findViewById(R.id.tvDate);
			ViewHolder holder = new ViewHolder(null, text, date);
			holder.setPosition(position);
			layout.setTag(holder);
			convertView = layout;
		}else{
			ViewHolder holder = (ViewHolder) convertView.getTag();
			text = holder.mText;
			date = holder.mDate;
			holder.setPosition(position);
		}
		if(text == null || date == null)
			return null;
		
		int role = (Integer) mDatalist.get(position).get(KEY_ROLE);
		RelativeLayout rLayout = (RelativeLayout) convertView;
		RelativeLayout.LayoutParams param;
		switch (role) {
		case ROLE_OWN:	// 显示在右边
			rLayout.removeAllViews();
			param = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
			param.addRule(RelativeLayout.ALIGN_PARENT_TOP);
			date.setText((String) mDatalist.get(position).get(KEY_DATE));
			rLayout.addView(date, param);

			text.setTextColor(Color.WHITE);
			text.setBackgroundResource(R.drawable.chart_list_item_right_selector);
			// <emoxxx>
			Spanned spann = makeChatContent((String) mDatalist.get(position).get(KEY_TEXT));
			text.setText(spann);
			ClickListener listener = (ClickListener) text.getTag();
			if(listener != null){
				if((Boolean)mDatalist.get(position).get(KEY_SHOW_MSG)){
					listener.hideMessage();
				}else{
					listener.showMessage();
				}
			}
				
			param = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			param.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			param.addRule(RelativeLayout.BELOW, date.getId());
			rLayout.addView(text, param);
			break;

		case ROLE_TARGET:	// 显示在左边
			rLayout.removeAllViews();
			param = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
			param.addRule(RelativeLayout.ALIGN_PARENT_TOP);
			date.setText((String) mDatalist.get(position).get(KEY_DATE));
			rLayout.addView(date, param);

			text.setTextColor(Color.BLACK);
			text.setBackgroundResource(R.drawable.chart_list_item_left_selector);
			Spanned spann2 = makeChatContent((String) mDatalist.get(position).get(KEY_TEXT));
			text.setText(spann2);
			ClickListener listener2 = (ClickListener) text.getTag();
			if(listener2 != null){
				if((Boolean)mDatalist.get(position).get(KEY_SHOW_MSG)){
					listener2.hideMessage();
				}else{
					listener2.showMessage();
				}
			}
			param = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			param.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			param.addRule(RelativeLayout.BELOW, date.getId());
			rLayout.addView(text, param);
			break;

		default:
			return null;
		}
		return rLayout;
	}
	
	private class TouchListener implements OnTouchListener{
		private TextView mView;
		public TouchListener(TextView v){
			mView = v;
		}
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if(event.getAction() == MotionEvent.ACTION_DOWN ){
				mView.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
			}else if(event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_OUTSIDE){
				mView.setTransformationMethod(PasswordTransformationMethod.getInstance());
			}
			return true;
		}
	}
	
	private class ClickListener implements OnClickListener{
		private TextView mView;
		public ClickListener(TextView v){
			mView = v;
		}
		
		public void showMessage(){
			mView.setTransformationMethod(PasswordTransformationMethod.getInstance());
		}
		
		public void hideMessage(){
			mView.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
		}
		
		@Override
		public void onClick(View v) {
			RelativeLayout rLayout = (RelativeLayout) mView.getParent();
			ViewHolder holder = (ViewHolder) rLayout.getTag();
			int pos = holder.getPosition();
			boolean isShow = (Boolean) mDatalist.get(pos).get(KEY_SHOW_MSG);
			if(isShow){
				mView.setTransformationMethod(PasswordTransformationMethod.getInstance());
				mDatalist.get(pos).put(KEY_SHOW_MSG, false);
			}else {
				mView.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
				mDatalist.get(pos).put(KEY_SHOW_MSG, true);
			}
		}
		
	}
	
	//<emo001]
	private Spanned makeChatContent(String msg){
		String htmlStr = msg;
		while(true){
			int start = htmlStr.indexOf("<emo", 0);
			if(start != -1){
				String resIdStr = htmlStr.substring(start+1,start + 7);
				htmlStr = htmlStr.replaceFirst("<emo...>", "<img src='" + resIdStr +"'/>");
			}else{
				return Html.fromHtml(htmlStr, imgGetter, null);
			}
		}
	}
	
	private ImageGetter imgGetter = new Html.ImageGetter() {
		@Override
		public Drawable getDrawable(String source) {
			int resID =  mContext.getResources().getIdentifier(source, "drawable", mContext.getPackageName());
			Drawable drawable = mContext.getResources().getDrawable(resID);
			int w = (int) (drawable.getIntrinsicWidth() * dm.density / 2);
			int h = (int) (drawable.getIntrinsicHeight() * dm.density / 2);
			drawable.setBounds(0, 0, w , h);
			return drawable;
		}
	};
	
	class ViewHolder {
		public TextView mName, mText, mDate; 
		public int position;
		public ViewHolder(TextView name, TextView text, TextView date){
			this.mName = name;
			this.mText = text;
			this.mDate = date;
		}
		public int getPosition() {
			return position;
		}
		public void setPosition(int position) {
			this.position = position;
		}
	}

}
