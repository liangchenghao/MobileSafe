package com.program.mobilesafe.settings;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.program.mobilesafe.R;
import com.program.service.AddressService;
import com.program.utils.ServiceStatusUtils;
import com.program.view.SettingClickView;
import com.program.view.SettingItemView;

public class SettingsActivity extends Activity {

    @ViewInject(R.id.siv_update)
    SettingItemView sivUpdate;
    @ViewInject(R.id.siv_address)
    SettingItemView sivAddress;
    @ViewInject(R.id.scv_address_style)
    SettingClickView scvAddressStyle;
    @ViewInject(R.id.scv_address_location)
    SettingClickView scvAddressLocation;


    private SharedPreferences mPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ViewUtils.inject(this);

        mPref = getSharedPreferences("config", MODE_PRIVATE);

        initUpdateView();
        initAddressView();
        initAddressStyle();
        initAddressLocation();
    }

    /**
     * 初始化自动更新开关
     */
    private void initUpdateView() {
        boolean autoUpdate = mPref.getBoolean("auto_update", true);

        if (autoUpdate) {
            // sivUpdate.setDesc("自动更新已开启");
            sivUpdate.setChecked(true);
        } else {
            // sivUpdate.setDesc("自动更新已关闭");
            sivUpdate.setChecked(false);
        }

        sivUpdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // 判断当前的勾选状态
                if (sivUpdate.isChecked()) {
                    // 设置不勾选
                    sivUpdate.setChecked(false);
                    // sivUpdate.setDesc("自动更新已关闭");
                    // 更新sp
                    mPref.edit().putBoolean("auto_update", false).commit();
                } else {
                    sivUpdate.setChecked(true);
                    // sivUpdate.setDesc("自动更新已开启");
                    // 更新sp
                    mPref.edit().putBoolean("auto_update", true).commit();
                }
            }
        });
    }

    /**
     * 初始化归属地开关
     */
    private void initAddressView() {

        // 根据归属地服务是否运行来更新checkbox
        boolean serviceRunning = ServiceStatusUtils.isServiceRunning(this,
                "com.program.service.AddressService");

        if (serviceRunning) {
            sivAddress.setChecked(true);
        } else {
            sivAddress.setChecked(false);
        }

        sivAddress.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (sivAddress.isChecked()) {
                    sivAddress.setChecked(false);
                    stopService(new Intent(SettingsActivity.this,
                            AddressService.class));// 停止归属地服务
                } else {
                    sivAddress.setChecked(true);
                    startService(new Intent(SettingsActivity.this,
                            AddressService.class));// 开启归属地服务
                }
            }
        });
    }

    final String[] items = new String[] { "半透明", "活力橙", "卫士蓝", "金属灰", "苹果绿" };

    /**
     * 修改提示框显示风格
     */
    private void initAddressStyle() {

        scvAddressStyle.setTitle("归属地提示框风格");

        int style = mPref.getInt("address_style", 0);// 读取保存的style
        scvAddressStyle.setDesc(items[style]);

        scvAddressStyle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showSingleChooseDailog();
            }
        });
    }

    /**
     * 弹出选择风格的单选框
     */
    protected void showSingleChooseDailog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // builder.setIcon(R.drawable.ic_launcher);
        builder.setTitle("归属地提示框风格");

        int style = mPref.getInt("address_style", 0);// 读取保存的style

        builder.setSingleChoiceItems(items, style,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPref.edit().putInt("address_style", which).commit();// 保存选择的风格
                        dialog.dismiss();// 让dialog消失

                        scvAddressStyle.setDesc(items[which]);// 更新组合控件的描述信息
                    }
                });

        builder.setNegativeButton("取消", null);
        builder.show();
    }

    /**
     * 修改归属地显示位置
     */
    private void initAddressLocation() {
        scvAddressLocation = (SettingClickView) findViewById(R.id.scv_address_location);
        scvAddressLocation.setTitle("归属地提示框显示位置");
        scvAddressLocation.setDesc("设置归属地提示框的显示位置");

        scvAddressLocation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this,
                        DragViewActivity.class));
            }
        });
    }
}
