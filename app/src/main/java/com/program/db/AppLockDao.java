package com.program.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * 应用信息数据库管理类
 *
 */
public class AppLockDao {
	private AppLcokDBOpenHelper helper;
	private Context context;

	public AppLockDao(Context context) {
		helper = new AppLcokDBOpenHelper(context);
		this.context = context;
	}

	//增加一条
	public void add(String packname){
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("packname", packname);
		db.insert("applock", null, values);
		db.close();
		Uri uri = Uri.parse("content://com.itheima.mobilesafe/applockdb");
		context.getContentResolver().notifyChange(uri, null);
		
	}

	//删除一条
	public void delete(String packname){
		SQLiteDatabase db = helper.getWritableDatabase();
		db.delete("applock", "packname=?", new String[]{packname});
		db.close();
		Uri uri = Uri.parse("content://com.itheima.mobilesafe/applockdb");
		context.getContentResolver().notifyChange(uri, null);
	}
	
	//查询要保护的包名
	public boolean find(String packname){
		boolean result = false;
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.query("applock", null, "packname=?", new String[]{packname}, null, null, null);
		if(cursor.moveToNext()){
			result = true;
		}
		cursor.close();
		db.close();
		return result;
	}
	
	//查询所有要保护的报名
	public List<String> findAll(){
		List<String> results = new ArrayList<String>();
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.query("applock", new String[]{"packname"}, null, null, null, null, null);
		while(cursor.moveToNext()){
			String packname = cursor.getString(0);
			results.add(packname);
		}
		cursor.close();
		db.close();
		return results;
	}
	
}
