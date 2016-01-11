package com.mc.vending.db;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.mc.vending.config.Constant;
import com.mc.vending.data.VendingData;

/**
 * 售货机 操作类
 * 
 * @Filename: VendingDbOper.java
 * @Version: 1.0
 * @Author: fenghu
 * @Email: fenghu@mightcloud.com
 *
 */
public class VendingDbOper {

	/**
	 * 查询售货机记录，一台售货机只有一条售货机记录
	 * 
	 * @return
	 */
	public VendingData getVending() {
		VendingData vending = null;
		SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
		Cursor c = db.rawQuery("SELECT VD1_ID,VD1_CODE,VD1_Status,VD1_CardType FROM Vending limit 1", null);
		while (c.moveToNext()) {
			vending = new VendingData();
			vending.setVd1Id(c.getString(c.getColumnIndex("VD1_ID")));
			vending.setVd1Code(c.getString(c.getColumnIndex("VD1_CODE")));
			vending.setVd1Status(c.getString(c.getColumnIndex("VD1_Status")));
			vending.setVd1CardType(c.getString(c.getColumnIndex("VD1_CardType")));
			break;
		}
		return vending;
	}

	/**
	 * 增加售货机记录 用于同步数据
	 * 
	 * @param vending
	 * @return
	 */
	public boolean addVending(VendingData vending) {
		String insertSql = "insert into Vending(VD1_ID,VD1_M02_ID,VD1_CODE,VD1_Manufacturer,VD1_VM1_ID,VD1_LastVersion,VD1_LWHSize,VD1_Color,VD1_InstallAddress,VD1_Coordinate,VD1_ST1_ID,VD1_EmergencyRel,VD1_EmergencyRelPhone,VD1_OnlineStatus,VD1_Status,"
				+ "VD1_CreateUser,VD1_CreateTime,VD1_ModifyUser,VD1_ModifyTime,VD1_RowVersion,VD1_CardType,VD1_LockerStatus)"
				+ " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
		SQLiteStatement stat = db.compileStatement(insertSql);
		stat.bindString(1, vending.getVd1Id());
		stat.bindString(2, vending.getVd1M02Id());
		stat.bindString(3, vending.getVd1Code());
		stat.bindString(4, vending.getVd1Manufacturer());
		stat.bindString(5, vending.getVd1Vm1Id());
		stat.bindString(6, vending.getVd1LastVersion());
		stat.bindString(7, vending.getVd1LwhSize());
		stat.bindString(8, vending.getVd1Color());
		stat.bindString(9, vending.getVd1InstallAddress());
		stat.bindString(10, vending.getVd1Coordinate());
		stat.bindString(11, vending.getVd1St1Id());
		stat.bindString(12, vending.getVd1EmergencyRel());
		stat.bindString(13, vending.getVd1EmergencyRelPhone());
		stat.bindString(14, vending.getVd1OnlineStatus());
		stat.bindString(15, vending.getVd1Status());
		stat.bindString(16, vending.getVd1CreateUser());
		stat.bindString(17, vending.getVd1CreateTime());
		stat.bindString(18, vending.getVd1ModifyUser());
		stat.bindString(19, vending.getVd1ModifyTime());
		stat.bindString(20, vending.getVd1RowVersion());
		stat.bindString(21, vending.getVd1CardType());
		stat.bindString(22, vending.getVd1LockerStatus());
		long i = stat.executeInsert();

		return i > 0;
	}

	/**
	 * 更新售货机数据
	 * 
	 * @param vendingData
	 * @return
	 */
	public boolean updateVending(VendingData vending) {
		String insertSql = "UPDATE Vending SET VD1_M02_ID=?,VD1_CODE=?,VD1_Manufacturer=?,VD1_VM1_ID=?,VD1_LastVersion=?,VD1_LWHSize=?,VD1_Color=?,"
				+ "VD1_InstallAddress=?,VD1_Coordinate=?,VD1_ST1_ID=?,VD1_EmergencyRel=?,VD1_EmergencyRelPhone=?,VD1_OnlineStatus=?,VD1_Status=?,"
				+ "VD1_CreateUser=?,VD1_CreateTime=?,VD1_ModifyUser=?,VD1_ModifyTime=?,VD1_RowVersion=?,VD1_CardType=?,VD1_LockerStatus=? WHERE VD1_ID=?";
		SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
		SQLiteStatement stat = db.compileStatement(insertSql);
		stat.bindString(1, vending.getVd1M02Id());
		stat.bindString(2, vending.getVd1Code());
		stat.bindString(3, vending.getVd1Manufacturer());
		stat.bindString(4, vending.getVd1Vm1Id());
		stat.bindString(5, vending.getVd1LastVersion());
		stat.bindString(6, vending.getVd1LwhSize());
		stat.bindString(7, vending.getVd1Color());
		stat.bindString(8, vending.getVd1InstallAddress());
		stat.bindString(9, vending.getVd1Coordinate());
		stat.bindString(10, vending.getVd1St1Id());
		stat.bindString(11, vending.getVd1EmergencyRel());
		stat.bindString(12, vending.getVd1EmergencyRelPhone());
		stat.bindString(13, vending.getVd1OnlineStatus());
		stat.bindString(14, vending.getVd1Status());
		stat.bindString(15, vending.getVd1CreateUser());
		stat.bindString(16, vending.getVd1CreateTime());
		stat.bindString(17, vending.getVd1ModifyUser());
		stat.bindString(18, vending.getVd1ModifyTime());
		stat.bindString(19, vending.getVd1RowVersion());
		stat.bindString(20, vending.getVd1CardType());
		stat.bindString(21, vending.getVd1Id());
		stat.bindString(22, vending.getVd1LockerStatus());
		long i = stat.executeUpdateDelete();
		return i > 0;
	}

	public boolean deleteAll() {
		boolean flag = false;
		String deleteSql = "DELETE FROM Vending";
		SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
		try {
			// 开启事务
			db.beginTransaction();
			SQLiteStatement stat = db.compileStatement(deleteSql);
			stat.executeInsert();
			// 数据插入成功，设置事物成功标志
			db.setTransactionSuccessful();
			// 保存数据
			db.endTransaction();
			flag = true;
		} catch (SQLException e) {
			// 结束事物，在这里没有设置成功标志，结束后不保存
			db.endTransaction();
			e.printStackTrace();
		}
		return flag;
	}

}
