package com.program.mobilesafe;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.program.utils.Tools;
import com.program.domain.VersionInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class LoadActivity extends Activity {

    private static final String TAG = "msg";

    protected static final int CODE_UPDATE_DIALOG = 0x000;
    protected static final int CODE_ENTER_HOME = 0x001;// 进入主页面

    @ViewInject(R.id.tv_version)
    TextView tvVersion;
    @ViewInject(R.id.tv_progress)
    TextView tvProgress;
    @ViewInject(R.id.layout_load)
    RelativeLayout layoutLoad;

    VersionInfo info = new VersionInfo();
    Message msg = new Message();
    private SharedPreferences mPref;

    /**
     * 通过发送消息启动异步线程
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case CODE_UPDATE_DIALOG:
                    showUpdateDialog();
                    break;
                case CODE_ENTER_HOME:
                    enterHome();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);
        ViewUtils.inject(this);

        tvVersion.setText("版本号:" + getVersionName());

        copyDB("address.db");// 拷贝归属地查询数据库

        // 渐变的动画效果
        AlphaAnimation anim = new AlphaAnimation(0.3f, 1f);
        anim.setDuration(2000);
        layoutLoad.startAnimation(anim);

        mPref = getSharedPreferences("config",MODE_PRIVATE);
        boolean autoUpdate = mPref.getBoolean("auto_update",true);
        if(autoUpdate){
            checkVersion();
        }else {
            mHandler.sendEmptyMessageDelayed(CODE_ENTER_HOME,1000);
        }
    }

    /**
     * 检测版本更新
     */
    private void checkVersion() {
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.GET,
                Tools.CONNECTION_URL + "update.json",
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        // 解析json
                        JSONObject jo = null;
                        info = new VersionInfo();
                        try {
                            jo = new JSONObject(responseInfo.result);
                            info.setmVersionName(jo.getString("versionName"));
                            info.setmVersionCode(jo.getInt("versionCode"));
                            info.setmDesc(jo.getString("description"));
                            info.setmDownloadUrl(jo.getString("downloadUrl"));
                            Log.v("utilscall", info.getmVersionCode() > getVersionCode() ? "true" : "false" + "");
                            if (info.getmVersionCode() > getVersionCode()) {
                                msg.what = CODE_UPDATE_DIALOG;
                            } else {
                                msg.what = CODE_ENTER_HOME;
                            }
                            mHandler.sendMessage(msg);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String errorMsg) {
                        Tools.showToast(LoadActivity.this,"网络连接异常");
                        mHandler.sendEmptyMessageDelayed(CODE_ENTER_HOME,1000);
                    }
                });
    }

    /**
     * 获取版本信息
     */
    private String getVersionName() {
        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            String versionName = packageInfo.versionName;
            return versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    private int getVersionCode() {
        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            int versionCode = packageInfo.versionCode;
            return versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

    /**
     * 检测版本弹出更新对话框
     */
    private void showUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("最新版本:" + info.getmVersionName());
        builder.setMessage(info.getmDesc());
        // builder.setCancelable(false);//不让用户取消对话框, 用户体验太差,尽量不要用
        builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                download();
            }
        });
        builder.setNegativeButton("以后再说", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                enterHome();
            }
        });

        // 设置取消的监听, 用户点击返回键时会触发
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                enterHome();
            }
        });

        builder.show();
    }

    /**
     * 下载apk文件
     */
    protected void download() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {

            tvProgress.setVisibility(View.VISIBLE);// 显示进度

            String target = Environment.getExternalStorageDirectory()
                    + "/catnut-latest.apk";
            // XUtils
            HttpUtils utils = new HttpUtils();
            utils.download(Tools.CONNECTION_URL + "catnut-latest.apk", target, new RequestCallBack<File>() {

                // 下载文件的进度, 该方法在主线程运行
                @Override
                public void onLoading(long total, long current,
                                      boolean isUploading) {
                    super.onLoading(total, current, isUploading);
                    tvProgress.setText("下载进度:" + current * 100 / total + "%");
                }

                // 下载成功,该方法在主线程运行
                @Override
                public void onSuccess(ResponseInfo<File> responseInfo) {
                    // 跳转到系统安装页面
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    intent.setDataAndType(Uri.fromFile(responseInfo.result),
                            "application/vnd.android.package-archive");
                    // startActivity(intent);
                    startActivityForResult(intent, 0);// 如果用户取消安装的话,
                    // 会返回结果,回调方法onActivityResult
                }

                // 下载失败,该方法在主线程运行
                @Override
                public void onFailure(HttpException arg0, String arg1) {
                    Tools.showToast(LoadActivity.this,"下载失败");
                }
            });
        } else {
            Tools.showToast(LoadActivity.this, "没有找到sdcard!");
        }
    }

    // 如果用户取消安装的话,回调此方法
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        enterHome();
    }


    /**
     * 进入主页面
     */
    private void enterHome() {
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * 拷贝数据库
     *
     * @param dbName
     */
    private void copyDB(String dbName) {
        // File filesDir = getFilesDir();
        // System.out.println("路径:" + filesDir.getAbsolutePath());
        File destFile = new File(getFilesDir(), dbName);// 要拷贝的目标地址

        if (destFile.exists()) {
            System.out.println("数据库" + dbName + "已存在!");
            return;
        }

        FileOutputStream out = null;
        InputStream in = null;

        try {
            in = getAssets().open(dbName);
            out = new FileOutputStream(destFile);

            int len = 0;
            byte[] buffer = new byte[1024];

            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
