package com.program.mobilesafe.ATools;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import com.program.mobilesafe.R;
import com.program.utils.SmsTools;

import java.io.File;


/**
 * 高级工具
 * 
 * @author Kevin
 * 
 */
public class AToolsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_atools);
	}

	/**
	 * 归属地查询
	 * 
	 * @param view
	 */
	public void numberAddressQuery(View view) {
		startActivity(new Intent(this, AddressActivity.class));
	}

	//短信备份
	public void smsBackup(View view) {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			final File file = new File(
					Environment.getExternalStorageDirectory(), "smsbackup.xml");
			final ProgressDialog pd = new ProgressDialog(this);
			pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			pd.setMessage("稍安勿躁，正在备份中...");
			pd.show();
			new Thread() {
				public void run() {
					try {
						SmsTools.backup(getApplicationContext(), file.getAbsolutePath(), new SmsTools.BackUpCallBack() {
							@Override
							public void onSmsBackup(int progress) {
								pd.setProgress(progress);
							}

							@Override
							public void beforeSmsBackup(int total) {
								pd.setMax(total);
							}
						});
					} catch (Exception e) {
						e.printStackTrace();
					}
					pd.dismiss();
				};
			}.start();
		} else {
			Toast.makeText(this, "sd不可用", Toast.LENGTH_SHORT).show();
		}
	}

	//进入程序锁
	public void enterApplock(View view){
		Intent intent = new Intent(this, AppLockActivity.class);
		startActivity(intent);
	}
}
