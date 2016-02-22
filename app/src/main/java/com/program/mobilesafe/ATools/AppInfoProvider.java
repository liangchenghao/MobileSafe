package com.program.mobilesafe.ATools;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.program.domain.AppInfo;

//获取手机中所有应用程序的信息
public class AppInfoProvider {
	public static List<AppInfo> getAppInfos(Context context){
		PackageManager pm = context.getPackageManager();
		List<AppInfo> appinfos = new ArrayList<>();
		List<PackageInfo> packinfos = pm.getInstalledPackages(0);

		for(PackageInfo packinfo : packinfos){
			String packname = packinfo.packageName;
			AppInfo appInfo = new AppInfo();
			Drawable icon = packinfo.applicationInfo.loadIcon(pm);
			String name = packinfo.applicationInfo.loadLabel(pm).toString()+packinfo.applicationInfo.uid;
			//应用程序的特征标志，可以是任意标志的组合
			int flags = packinfo.applicationInfo.flags;//应用参数

			if((flags & ApplicationInfo.FLAG_SYSTEM)  == 0){
				//用户应用
				appInfo.setUserApp(true);
			}else{
				//系统应用
				appInfo.setUserApp(false);
			}
			if((flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE)  == 0){
				//手机内存
				appInfo.setInRom(true);
			}else{
				//外部存储
				appInfo.setInRom(false);
			}

			appInfo.setIcon(icon);
			appInfo.setName(name);
			appInfo.setPackname(packname);
			appinfos.add(appInfo);
		}
		return appinfos;
	}
}
