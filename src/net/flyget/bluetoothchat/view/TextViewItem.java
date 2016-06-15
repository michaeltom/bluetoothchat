package net.flyget.bluetoothchat.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

public class TextViewItem extends TextView {
	public static final int ROLE_OWN = 1;
	public static final int ROLE_TARGET = 2;
	public static final int ROLE_OTHER = 3;
	
	private Context mContext;

	public TextViewItem(Context context) {
		super(context);
		mContext = context;
	}

	public TextViewItem(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
	}

	public TextViewItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
	}
	
	public void setRole(int role){
		switch(role){
		case ROLE_OTHER:
			break;
		case ROLE_OWN:
			break;
		case ROLE_TARGET:
			break;
		}
		
	}

}
