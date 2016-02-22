package com.program.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AppLcokDBOpenHelper extends SQLiteOpenHelper {

	public AppLcokDBOpenHelper(Context context) {
		super(context, "applock.db", null, 1);
	}
	
	//数据库第一次创建的时候调用的方法
	@Override
	public void onCreate(SQLiteDatabase db) {
		//创建数据库表结构主键的 _id 自增长，锁定应用程序的包名
		db.execSQL("create table applock (_id integer primary key autoincrement , packname varchar(20))");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}
}
