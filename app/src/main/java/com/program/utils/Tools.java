package com.program.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Administrator on 2015/9/8.
 */
public class Tools {

    public static final String CONNECTION_URL = "http://10.0.3.3:8080/testweb/";

    public static void showToast(Context context, String str) {
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }


    /**
     * 从服务器获取版本信息进行校验
     */
    public static void checkVersion() {
//        final VersionInfo info = new VersionInfo();

/*
        class Call implements Callable<VersionInfo> {

            VersionInfo info = new VersionInfo();

            @Override
            public VersionInfo call() throws Exception {
                HttpUtils http = new HttpUtils();
                http.send(HttpRequest.HttpMethod.GET,
                        CONNECTION_URL + "update.json",
                        new RequestCallBack<String>() {
                            @Override
                            public void onSuccess(ResponseInfo<String> responseInfo) {
                                // 解析json
                                JSONObject jo = null;
                                try {
                                    jo = new JSONObject(responseInfo.result);
                                    info.setmVersionName(jo.getString("versionName"));
                                    info.setmVersionCode(jo.getInt("versionCode"));
                                    info.setmDesc(jo.getString("description"));
                                    info.setmDownloadUrl(jo.getString("downloadUrl"));
                                    Log.v("utilscall",info.toString());

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(HttpException error, String msg) {
                            }
                        });

                Log.v("return call",info.toString());
                return info;
            }
        }
        ExecutorService executorService = Executors.newCachedThreadPool();
        Future<VersionInfo> future = executorService.submit(new Call());
        try {
            info = future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }*/
    }
}
