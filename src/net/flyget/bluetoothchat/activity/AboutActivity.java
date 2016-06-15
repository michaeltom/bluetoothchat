package net.flyget.bluetoothchat.activity;

import net.flyget.bluetoothchat.R;
import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.widget.TextView;

public class AboutActivity extends Activity {
	private TextView mVersionTv;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_download);
		mVersionTv = (TextView) findViewById(R.id.versionTv);
		mVersionTv.setText("当前版本：" + getVersionName());
	}
	
	private String getVersionName(){
		// 获取packagemanager的实例
		PackageManager packageManager = this.getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取程序包默认信息
		PackageInfo packInfo = null;
		try {
			packInfo = packageManager.getPackageInfo(
					this.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return packInfo.versionName;
	}
}
