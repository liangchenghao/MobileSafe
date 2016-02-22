package com.program.mobilesafe.safe;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.program.mobilesafe.R;

public class SafeActivity extends Activity {

    @ViewInject(R.id.tv_safe_phone)
    TextView tvSafePhone;
    @ViewInject(R.id.iv_protect)
    ImageView ivProtect;

    private SharedPreferences mPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);

        mPref = getSharedPreferences("config",MODE_PRIVATE);

        boolean configed = mPref.getBoolean("configed", false);// 判断是否进入过设置向导
        if (configed) {
            setContentView(R.layout.activity_safe);
            // 根据sp更新安全号码
            tvSafePhone = (TextView) findViewById(R.id.tv_safe_phone);
            String phone = mPref.getString("safe_phone", "");
            tvSafePhone.setText(phone);

            // 根据sp更新保护锁
            ivProtect = (ImageView) findViewById(R.id.iv_protect);
            boolean protect = mPref.getBoolean("protect", false);
            if (protect) {
                ivProtect.setImageResource(R.drawable.lock);
            } else {
                ivProtect.setImageResource(R.drawable.unlock);
            }
        } else {
            // 跳转设置向导页
            startActivity(new Intent(this, Setup1Activity.class));
            finish();
        }
    }

    /**
     * 重新进入设置向导
     *
     * @param view
     */
    public void reEnter(View view) {
        startActivity(new Intent(this, Setup1Activity.class));
        finish();
    }
}
