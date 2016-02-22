package com.program.domain;

/**
 * Created by Administrator on 2015/9/8.
 */
public class VersionInfo {

    private String mVersionName;// 版本名
    private int mVersionCode;// 版本号
    private String mDesc;// 版本描述
    private String mDownloadUrl;// 下载地址

    @Override
    public String toString() {
        return "VersionInfo{" +
                "mVersionName='" + mVersionName + '\'' +
                ", mVersionCode=" + mVersionCode +
                ", mDesc='" + mDesc + '\'' +
                ", mDownloadUrl='" + mDownloadUrl + '\'' +
                '}';
    }

    public String getmVersionName() {
        return mVersionName;
    }

    public void setmVersionName(String mVersionName) {
        this.mVersionName = mVersionName;
    }

    public int getmVersionCode() {
        return mVersionCode;
    }

    public void setmVersionCode(int mVersionCode) {
        this.mVersionCode = mVersionCode;
    }

    public String getmDesc() {
        return mDesc;
    }

    public void setmDesc(String mDesc) {
        this.mDesc = mDesc;
    }

    public String getmDownloadUrl() {
        return mDownloadUrl;
    }

    public void setmDownloadUrl(String mDownloadUrl) {
        this.mDownloadUrl = mDownloadUrl;
    }
}
