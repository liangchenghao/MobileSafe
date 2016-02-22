package com.program.mobilesafe.safe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.program.mobilesafe.R;
import com.program.utils.Tools;

/**
 * 第3个设置向导页
 * 
 * @author Kevin
 * 
 */
public class Setup3Activity extends BaseSetupActivity {

	@ViewInject(R.id.et_phone)
	EditText etPhone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup3);
		ViewUtils.inject(this);

		String phone = mPref.getString("safe_phone", "");
		etPhone.setText(phone);
	}

	@Override
	public void showNextPage() {
		String phone = etPhone.getText().toString().trim();// 注意过滤空格

		if (TextUtils.isEmpty(phone)) {
			// Toast.makeText(this, "安全号码不能为空!", Toast.LENGTH_SHORT).show();
			Tools.showToast(this, "安全号码不能为空!");
			return;
		}

		mPref.edit().putString("safe_phone", phone).commit();// 保存安全号码
		startActivity(new Intent(this, Setup4Activity.class));
		finish();

		// 两个界面切换的动画
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);// 进入动画和退出动画
	}

	@Override
	public void showPreviousPage() {
		startActivity(new Intent(this, Setup2Activity.class));
		finish();

		// 两个界面切换的动画
		overridePendingTransition(R.anim.tran_previous_in,
				R.anim.tran_previous_out);// 进入动画和退出动画
	}

	/**
	 * 选择联系人
	 *
	 * @param view
	 */
	public void selectContact(View view) {
		Intent intent = new Intent(this, ContactActivity.class);
		startActivityForResult(intent, 1);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// System.out.println("resultCode:" + resultCode);
		// System.out.println("requestCode:" + requestCode);

		if (resultCode == Activity.RESULT_OK) {
			String phone = data.getStringExtra("phone");
			phone = phone.replaceAll("-", "").replaceAll(" ","").trim();// 替换 " - "

			etPhone.setText(phone);// 把电话号码设置给输入框
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

}
