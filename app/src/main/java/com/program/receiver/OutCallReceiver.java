package com.program.receiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.program.db.AddressDao;

/**
 * 监听去电的广播接受者 需要权限: android.permission.PROCESS_OUTGOING_CALLS
 * 
 * @author Kevin
 * 
 */
public class OutCallReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String number = getResultData();//获取去电电话号码

		String address = AddressDao.getAddress(number);
		Toast.makeText(context, address, Toast.LENGTH_LONG).show();
	}

}
