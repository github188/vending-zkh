/**
 * 
 */
package com.mc.vending.db;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mc.vending.data.ConversionData;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

/**
 * @author junjie.you
 *
 */
public class ConversionDbOper {
	private String insertSql = "INSERT INTO Conversion(CN1_ID,CN1_Upid,CN1_Cpid,CN1_Proportion,CN1_Operation,CN1_CreateUser,CN1_CreateTime,CN1_ModifyUser,CN1_ModifyTime,CN1_RowVersion) VALUES(?,?,?,?,?,?,?,?,?,?);";

	/**
	 * 根据"关联产品ID"查询"单位换算关系表"中有无该产品的换算关系
	 * 
	 * @param cn1Cpid
	 *            关联产品ID
	 * @return 有则查看Proportion(换算比例,如:12)和Operation(操作方式,如:打); 无则返回NULL
	 */
	public ConversionData findConversionByCpid(String cn1Cpid) {
		ConversionData conversionData = null;
		SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
		Cursor c = db.rawQuery("SELECT * FROM Conversion WHERE CN1_Cpid = ? ORDER by CN1_CreateTime desc limit 1",
				new String[] { cn1Cpid });
		while (c.moveToNext()) {
			conversionData = new ConversionData();
			conversionData.setCn1Id(c.getString(c.getColumnIndex("CN1_ID")));
			conversionData.setCn1Upid(c.getString(c.getColumnIndex("CN1_Upid")));
			conversionData.setCn1Cpid(c.getString(c.getColumnIndex("CN1_Cpid")));
			conversionData.setCn1Proportion(c.getString(c.getColumnIndex("CN1_Proportion")));
			conversionData.setCn1Operation(c.getString(c.getColumnIndex("CN1_Operation")));
			conversionData.setCn1CreateUser(c.getString(c.getColumnIndex("CN1_CreateUser")));
			conversionData.setCn1CreateTime(c.getString(c.getColumnIndex("CN1_CreateTime")));
			conversionData.setCn1ModifyUser(c.getString(c.getColumnIndex("CN1_ModifyUser")));
			conversionData.setCn1ModifyTime(c.getString(c.getColumnIndex("CN1_ModifyTime")));
			conversionData.setCn1RowVersion(c.getString(c.getColumnIndex("CN1_RowVersion")));
			break;
		}
		return conversionData;
	}

	/**
	 * 根据"基础产品ID"查询"单位换算关系表"中有无该产品的换算关系
	 * 
	 * @param cn1Upid
	 *            关联产品ID
	 * @return 有则查看Proportion(换算比例,如:12)和Operation(操作方式,如:打); 无则返回NULL
	 */
	public Map<String, Object> findConversionByUpid(String cn1Upid) {
		Map<String, Object> cpIdWithScaleList = new HashMap<String, Object>();
		SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
		Cursor c = db.rawQuery(
				"SELECT CN1_ID,CN1_Cpid,CN1_Proportion,CN1_RowVersion FROM Conversion WHERE CN1_Upid = ? ORDER by CN1_CreateTime desc limit 1",
				new String[] { cn1Upid });
		while (c.moveToNext()) {
			cpIdWithScaleList.put(c.getString(c.getColumnIndex("CN1_Cpid")),
					c.getString(c.getColumnIndex("CN1_Proportion")));
			break;
		}
		return cpIdWithScaleList;
	}

	/**
	 * 增加单位转换关系表 单条
	 * 
	 * @param convertionData
	 * @return
	 */
	public boolean addConversionData(ConversionData convertionData) {
		SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
		SQLiteStatement stat = db.compileStatement(insertSql);
		int i = 1;
		stat.bindString(i++, convertionData.getCn1Id());
		stat.bindString(i++, convertionData.getCn1Upid());
		stat.bindString(i++, convertionData.getCn1Cpid());
		stat.bindString(i++, convertionData.getCn1Proportion());
		stat.bindString(i++, convertionData.getCn1Operation());
		stat.bindString(i++, convertionData.getCreateUser());
		stat.bindString(i++, convertionData.getCreateTime());
		stat.bindString(i++, convertionData.getModifyUser());
		stat.bindString(i++, convertionData.getModifyTime());
		stat.bindString(i++, convertionData.getRowVersion());

		long r = stat.executeInsert();
		return r > 0;
	}

	// CN1_ID,CN1_Upid,CN1_Cpid,CN1_Proportion,CN1_Operation,CN1_CreateUser,CN1_CreateTime,CN1_ModifyUser,CN1_ModifyTime,CN1_RowVersion
	/**
	 * 批量增加卡与产品权限数据,用于同步数据
	 * 
	 * @param list
	 */
	public boolean batchAddConversionData(List<ConversionData> list) {
		boolean flag = false;
		SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
		try {
			// 开启事务
			db.beginTransaction();
			for (ConversionData conversionData : list) {
				SQLiteStatement stat = db.compileStatement(insertSql);

				int i = 1;
				stat.bindString(i++, conversionData.getCn1Id());
				stat.bindString(i++, conversionData.getCn1Upid());
				stat.bindString(i++, conversionData.getCn1Cpid());
				stat.bindString(i++, conversionData.getCn1Proportion());
				stat.bindString(i++, conversionData.getCn1Operation());
				stat.bindString(i++, conversionData.getCreateUser());
				stat.bindString(i++, conversionData.getCreateTime());
				stat.bindString(i++, conversionData.getModifyUser());
				stat.bindString(i++, conversionData.getModifyTime());
				stat.bindString(i++, conversionData.getRowVersion());
				stat.executeInsert();
			}
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

	/**
	 * 批量删除记录
	 * 
	 * @param dbPra
	 * @return
	 */
	public boolean batchDeleteConversion(List<String> dbPra) {
		boolean flag = false;
		SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
		try {
			// 开启事务
			db.beginTransaction();
			for (String string : dbPra) {
				String deleteSql = "DELETE FROM Conversion where CN1_Upid='" + string.split(",")[0] + "' and CN1_Cpid='"
						+ string.split(",")[1] + "'";
				SQLiteStatement stat = db.compileStatement(deleteSql);
				stat.executeInsert();
			}
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
