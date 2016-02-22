package com.program.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.program.mobilesafe.R;

/**
 * Created by Administrator on 2015/9/10.
 */
public class SettingItemView extends RelativeLayout {

    private static final String NAMESPACE = "http://schemas.android.com/apk/res-auto";

    private TextView tvTitle;
    private TextView tvDesc;
    private CheckBox cbStatus;

    //    private String mTitle = "自动更新设置";
//    private String mDescOn = "自动更新已开启";
//    private String mDescOff = "自动更新已关闭";
    private String mTitle;
    private String mDescOn;
    private String mDescOff;

    public SettingItemView(Context context) {
        super(context);
        init();
    }

    public SettingItemView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mTitle = attrs.getAttributeValue(NAMESPACE, "item_title");// 根据属性名称,获取属性的值
        mDescOn = attrs.getAttributeValue(NAMESPACE, "desc_on");
        mDescOff = attrs.getAttributeValue(NAMESPACE, "desc_off");

        init();
    }

    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 填充部件
     */
    private void init() {
        View view = View.inflate(getContext(), R.layout.view_setting_item, null);
        // 将自定义好的布局文件设置给当前的SettingItemView
        View.inflate(getContext(), R.layout.view_setting_item, this);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvDesc = (TextView) findViewById(R.id.tv_desc);
        cbStatus = (CheckBox) findViewById(R.id.cb_status);

        setTitle(mTitle);
    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    public void setDesc(String desc) {
        tvDesc.setText(desc);
    }

    /**
     * 返回勾选状态
     *
     * @return
     */
    public boolean isChecked() {
        return cbStatus.isChecked();
    }

    public void setChecked(boolean check) {
        cbStatus.setChecked(check);

        // 根据选择的状态,更新文本描述
        if (check) {
            setDesc(mDescOn);
        } else {
            setDesc(mDescOff);
        }
    }
}
