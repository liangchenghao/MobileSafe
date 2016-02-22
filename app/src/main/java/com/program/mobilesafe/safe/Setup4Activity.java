package com.program.mobilesafe.safe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.program.mobilesafe.R;
/**
 * 第4个设置向导页
 * 
 * @author Kevin
 * 
 */
public class Setup4Activity extends BaseSetupActivity {

	private SharedPreferences mPref;
	@ViewInject(R.id.cb_protect)
	CheckBox cbProtect;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup4);
		ViewUtils.inject(this);

		mPref = getSharedPreferences("config", MODE_PRIVATE);
		boolean protect = mPref.getBoolean("protect", false);

		// 根据sp保存的状态,更新checkbox
		if (protect) {
			cbProtect.setText("防盗保护已经开启");
			cbProtect.setChecked(true);
		} else {
			cbProtect.setText("防盗保护没有开启");
			cbProtect.setChecked(false);
		}

		// 当checkbox发生变化时,回调此方法
		cbProtect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
										 boolean isChecked) {
				if (isChecked) {
					cbProtect.setText("防盗保护已经开启");
					mPref.edit().putBoolean("protect", true).commit();
				} else {
					cbProtect.setText("防盗保护没有开启");
					mPref.edit().putBoolean("protect", false).commit();
				}
			}
		});
	}

	@Override
	public void showNextPage() {
		startActivity(new Intent(this, SafeActivity.class));
		finish();

		// 两个界面切换的动画
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);// 进入动画和退出动画

		mPref.edit().putBoolean("configed", true).commit();// 更新sp,表示已经展示过设置向导了,下次进来就不展示啦
	}

	@Override
	public void showPreviousPage() {
		startActivity(new Intent(this, Setup3Activity.class));
		finish();

		// 两个界面切换的动画
		overridePendingTransition(R.anim.tran_previous_in,
				R.anim.tran_previous_out);// 进入动画和退出动画
	}
}
