package com.program.mobilesafe.settings;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.program.mobilesafe.R;


/**
 * 修改归属地显示位置
 *
 * @author Kevin
 */
public class DragViewActivity extends Activity {

    private TextView tvTop;
    private TextView tvBottom;

    private ImageView ivDrag;

    private int startX;
    private int startY;
    private SharedPreferences mPref;

    private DisplayMetrics mDisplayMetrics;
    private int windowWidth;
    private int windowHeight;

    long[] mHits = new long[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag_view);

        mPref = getSharedPreferences("config", MODE_PRIVATE);

        tvTop = (TextView) findViewById(R.id.tv_top);
        tvBottom = (TextView) findViewById(R.id.tv_bottom);
        ivDrag = (ImageView) findViewById(R.id.iv_drag);

        int lastX = mPref.getInt("lastX", 0);
        int lastY = mPref.getInt("lastY", 0);

        mDisplayMetrics = new DisplayMetrics();//屏幕分辨率容器
        getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        windowWidth = mDisplayMetrics.widthPixels;
        windowHeight = mDisplayMetrics.heightPixels;

        //提示框的显示位置
        if (lastY > windowHeight / 2) {
            tvTop.setVisibility(View.VISIBLE);
            tvBottom.setVisibility(View.INVISIBLE);
        } else {
            tvTop.setVisibility(View.INVISIBLE);
            tvBottom.setVisibility(View.VISIBLE);
        }

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ivDrag
                .getLayoutParams();// 获取布局对象
        layoutParams.leftMargin = lastX;// 设置左边距
        layoutParams.topMargin = lastY;// 设置top边距

        ivDrag.setLayoutParams(layoutParams);// 重新设置位置

        ivDrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 识别连续的两次点击
                System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
                mHits[mHits.length - 1] = SystemClock.uptimeMillis();

                if (mHits[0] >= (SystemClock.uptimeMillis() - 500)) {
                    //双击成功
                    ivDrag.layout(windowWidth / 2 - ivDrag.getWidth() / 2,
                            ivDrag.getTop(), windowWidth / 2 + ivDrag.getWidth() / 2, ivDrag.getBottom());
                    // 记录坐标点
                    Editor edit = mPref.edit();
                    edit.putInt("lastX", ivDrag.getLeft());
                    edit.putInt("lastY", ivDrag.getTop());
                    edit.commit();
                }
            }
        });

        // 设置触摸监听
        ivDrag.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // 初始化起点坐标
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int endX = (int) event.getRawX();
                        int endY = (int) event.getRawY();

                        // 计算移动偏移量
                        int dx = endX - startX;
                        int dy = endY - startY;

                        // 更新左上右下距离
                        int l = ivDrag.getLeft() + dx;
                        int r = ivDrag.getRight() + dx;

                        int t = ivDrag.getTop() + dy;
                        int b = ivDrag.getBottom() + dy;

                        // 判断位置是否合法。
                        if (l < 0 || t < 0 || r > windowWidth
                                || b > (windowHeight - 50)) {
                            break;
                        }

                        //更新提示框的显示位置
                        if (t > windowHeight / 2) {
                            tvTop.setVisibility(View.VISIBLE);
                            tvBottom.setVisibility(View.INVISIBLE);
                        } else {
                            tvTop.setVisibility(View.INVISIBLE);
                            tvBottom.setVisibility(View.VISIBLE);
                        }

                        // 更新界面
                        ivDrag.layout(l, t, r, b);

                        // 重新初始化起点坐标
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:
                        // 记录坐标点
                        Editor edit = mPref.edit();
                        edit.putInt("lastX", ivDrag.getLeft());
                        edit.putInt("lastY", ivDrag.getTop());
                        edit.commit();
                        break;

                    default:
                        break;
                }

                return true;
            }
        });
    }
}
