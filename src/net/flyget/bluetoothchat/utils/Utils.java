package net.flyget.bluetoothchat.utils;

import net.flyget.bluetoothchat.R;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class Utils {
	public static final int NOTIFY_ID1 = 1001;
	
	public static void notifyMessage(Context context, String msg, Activity activity){
		//Notification builder; 
		PendingIntent contentIntent = null; 
		NotificationManager nm;
		// 发送通知需要用到NotificationManager对象 
		nm = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
		// 消息对象
        Intent notificationIntent = new Intent(context, activity.getClass());
        // PendingIntent.getActivity(Context context, int requestCode, Intent intent, int flags)
        // 用来获得一个挂起的PendingIntent，让该Intent去启动新的Activity来处理通知
        contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0); 

        
        //定义通知栏展现的内容信息
        int icon = R.drawable.icon;
        long when = System.currentTimeMillis();
        Notification notification = new Notification(icon, msg, when);
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notification.defaults |= Notification.DEFAULT_SOUND; // 调用系统自带声音  
        notification.flags |= Notification.FLAG_AUTO_CANCEL; // 点击清除按钮或点击通知后会自动消失
        notification.defaults |= Notification.DEFAULT_LIGHTS;
        notification.vibrate = new long[]{300, 500};
        notification.setLatestEventInfo(context, "BluetoothChat", msg, contentIntent);
       /* // 定制我们要在状态栏显示的通知样式
		builder = new Notification(context);
		builder.setContentIntent(contentIntent)
		 	.setSmallIcon(R.drawable.ic_launcher)//设置状态栏里面的图标（小图标） 　　　　　　　　　　　　　　　　　　　　.setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.i5))//下拉下拉列表里面的图标（大图标） 　　　　　　　.setTicker("this is bitch!") //设置状态栏的显示的信息
		 	.setWhen(System.currentTimeMillis())//设置时间发生时间
		 	.setAutoCancel(true)//设置可以清除
		 	.setContentTitle("This is ContentTitle")//设置下拉列表里的标题
		 	.setContentText("this is ContentText");//设置上下文内容
        */		
		// 获得刚才创建的通知对象
		// Notification notification = builder.getNotification();//获取一个Notification
	     
	     // 通过NotificationManger来发送通知消息
	     // 参数1通知的ID，参数2发送哪个通知
	     nm.notify(NOTIFY_ID1, notification);
	}
}
