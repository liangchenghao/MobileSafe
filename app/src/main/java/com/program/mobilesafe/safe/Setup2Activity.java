package com.program.mobilesafe.safe;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.program.mobilesafe.R;
import com.program.utils.Tools;
import com.program.view.SettingItemView;

/**
 * 第2个设置向导页
 * 
 * @author Kevin
 * 
 */
public class Setup2Activity extends BaseSetupActivity {

	@ViewInject(R.id.siv_sim)
	SettingItemView sivSIM;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup2);
		ViewUtils.inject(this);

		final String sim = mPref.getString("sim", null);

		if (!TextUtils.isEmpty(sim)) {
			sivSIM.setChecked(true);
		} else {
			sivSIM.setChecked(false);
		}

		sivSIM.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// 判断当前的勾选状态
				if (sivSIM.isChecked()) {
					// 设置不勾选
					sivSIM.setChecked(false);
					if(sim != null){
						mPref.edit().remove("sim").commit();// 删除已绑定的sim卡
					}
				} else {
					sivSIM.setChecked(true);
					// 保存sim卡信息
					TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
					String simSerialNumber = tm.getSimSerialNumber();// 获取sim卡序列号
//					System.out.println("sim卡序列号:" + simSerialNumber);

					mPref.edit().putString("sim", simSerialNumber).commit();// 将sim卡序列号保存在sp中
				}
			}
		});
	}

	@Override
	public void showNextPage() {

		// 如果sim卡没有绑定,就不允许进入下一个页面
		String sim = mPref.getString("sim", null);
		if (TextUtils.isEmpty(sim)) {
			Tools.showToast(Setup2Activity.this, "必须绑定sim卡!");
			return;
		}

		startActivity(new Intent(this, Setup3Activity.class));
		finish();

		// 两个界面切换的动画
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);// 进入动画和退出动画

	}

	@Override
	public void showPreviousPage() {

		startActivity(new Intent(this, Setup1Activity.class));
		finish();

		// 两个界面切换的动画
		overridePendingTransition(R.anim.tran_previous_in,
				R.anim.tran_previous_out);// 进入动画和退出动画

	}
}
