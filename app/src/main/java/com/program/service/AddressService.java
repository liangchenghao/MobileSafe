package com.program.service;


import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.program.db.AddressDao;
import com.program.mobilesafe.R;

/**
 * 来电提醒的服务
 * 
 * @author Kevin
 * 
 */
public class AddressService extends Service {

	private TelephonyManager tm;
	private MyListener listener;
	private OutCallReceiver receiver;
	private WindowManager mWM;
	private View view;
	private SharedPreferences mPref;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		mPref = getSharedPreferences("config", MODE_PRIVATE);

		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		listener = new MyListener();
		tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);// 监听来电的状态

		receiver = new OutCallReceiver();

		IntentFilter filter = new IntentFilter(Intent.ACTION_NEW_OUTGOING_CALL);
		registerReceiver(receiver, filter);// 动态注册广播
	}

	class MyListener extends PhoneStateListener {

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:// 电话铃声响了
//				System.out.println("电话铃响...");
				String address = AddressDao.getAddress(incomingNumber);// 根据来电号码查询归属地
				// Toast.makeText(AddressService.this, address,
				// Toast.LENGTH_LONG)
				// .show();
				showToast(address);
				break;

			case TelephonyManager.CALL_STATE_IDLE:// 电话闲置状态
				if (mWM != null && view != null) {
					mWM.removeView(view);// 从window中移除view
					view = null;
				}
				break;
			default:
				break;
			}

			super.onCallStateChanged(state, incomingNumber);
		}

	}

	/**
	 * 监听去电的广播接受者 需要权限: android.permission.PROCESS_OUTGOING_CALLS
	 * 
	 * @author Kevin
	 * 
	 */
	class OutCallReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String number = getResultData();// 获取去电电话号码

			String address = AddressDao.getAddress(number);
			// Toast.makeText(context, address, Toast.LENGTH_LONG).show();
			showToast(address);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		tm.listen(listener, PhoneStateListener.LISTEN_NONE);// 停止来电监听

		unregisterReceiver(receiver);// 注销广播
	}

	/**
	 * 自定义归属地浮窗
	 */
	private void showToast(String text) {
		mWM = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);

		WindowManager.LayoutParams params = new WindowManager.LayoutParams();
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;
		params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
				| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
		params.format = PixelFormat.TRANSLUCENT;
		params.type = WindowManager.LayoutParams.TYPE_TOAST;
		params.setTitle("Toast");

		// view = new TextView(this);
		view = View.inflate(this, R.layout.toast_address, null);

		int[] bgs = new int[] { R.drawable.call_locate_white,
				R.drawable.call_locate_orange, R.drawable.call_locate_blue,
				R.drawable.call_locate_gray, R.drawable.call_locate_green };
		int style = mPref.getInt("address_style", 0);

		view.setBackgroundResource(bgs[style]);// 根据存储的样式更新背景

		TextView tvText = (TextView) view.findViewById(R.id.tv_number);
		tvText.setText(text);

		mWM.addView(view, params);// 将view添加在屏幕上(Window)
	}

}
