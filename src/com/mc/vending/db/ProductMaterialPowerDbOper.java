package com.mc.vending.db;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.mc.vending.data.ConversionData;
import com.mc.vending.data.ProductMaterialPowerData;
import com.mc.vending.tools.ConvertHelper;
import com.mc.vending.tools.ZillionLog;

/**
 * 产品领料权限 操作类
 * 
 * @Filename: ProductMaterialPowerDbOper.java
 * @Version: 1.0
 * @Author: fenghu
 * @Email: fenghu@mightcloud.com
 *
 */
public class ProductMaterialPowerDbOper {

	public List<ProductMaterialPowerData> findAll() {
		List<ProductMaterialPowerData> list = new ArrayList<ProductMaterialPowerData>();
		SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
		Cursor c = db.rawQuery("SELECT * FROM ProductMaterialPower", null);
		while (c.moveToNext()) {
			ProductMaterialPowerData productMaterialPower = new ProductMaterialPowerData();
			productMaterialPower.setPm1Id(c.getString(c.getColumnIndex("PM1_ID")));
			productMaterialPower.setPm1M02Id(c.getString(c.getColumnIndex("PM1_M02_ID")));
			productMaterialPower.setPm1Cu1Id(c.getString(c.getColumnIndex("PM1_CU1_ID")));
			productMaterialPower.setPm1Vc2Id(c.getString(c.getColumnIndex("PM1_VC2_ID")));
			productMaterialPower.setPm1Vp1Id(c.getString(c.getColumnIndex("PM1_VP1_ID")));
			productMaterialPower.setPm1Power(c.getString(c.getColumnIndex("PM1_Power")));
			productMaterialPower.setPm1OnceQty(c.getInt(c.getColumnIndex("PM1_OnceQty")));
			productMaterialPower.setPm1Period(c.getString(c.getColumnIndex("PM1_Period")));
			productMaterialPower.setPm1IntervalStart(c.getString(c.getColumnIndex("PM1_IntervalStart")));
			productMaterialPower.setPm1IntervalFinish(c.getString(c.getColumnIndex("PM1_IntervalFinish")));
			productMaterialPower.setPm1StartDate(c.getString(c.getColumnIndex("PM1_StartDate")));
			productMaterialPower.setPm1PeriodQty(c.getInt(c.getColumnIndex("PM1_PeriodQty")));
			productMaterialPower.setPm1CreateUser(c.getString(c.getColumnIndex("PM1_CreateUser")));
			productMaterialPower.setPm1CreateTime(c.getString(c.getColumnIndex("PM1_CreateTime")));
			productMaterialPower.setPm1ModifyUser(c.getString(c.getColumnIndex("PM1_ModifyUser")));
			productMaterialPower.setPm1ModifyTime(c.getString(c.getColumnIndex("PM1_ModifyTime")));
			productMaterialPower.setPm1RowVersion(c.getString(c.getColumnIndex("PM1_RowVersion")));
			list.add(productMaterialPower);
		}
		return list;
	}

	/**
	 * 根据 客户ID, 售货机卡/密码权限.ID ,售货机产品.ID 查询产品领料权限记录
	 * 
	 * @param cusId
	 *            客户ID
	 * @param vc2Id
	 *            售货机卡/密码权限.ID
	 * @param vp1Id
	 *            售货机产品.ID
	 * @return
	 */
	public ProductMaterialPowerData getVendingProLinkByVidAndSkuId(String cusId, String vc2Id, String vp1Id) {
		ProductMaterialPowerData productMaterialPower = null;
		SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
		Cursor c = db.rawQuery(
				"SELECT PM1_ID,PM1_CU1_ID,PM1_VC2_ID,PM1_VP1_ID,PM1_Power,PM1_OnceQty,PM1_Period,PM1_IntervalStart,PM1_IntervalFinish,PM1_StartDate,PM1_PeriodQty FROM ProductMaterialPower WHERE PM1_CU1_ID=? and PM1_VC2_ID=? and PM1_VP1_ID=? limit 1",
				new String[] { cusId, vc2Id, vp1Id });
		while (c.moveToNext()) {
			productMaterialPower = new ProductMaterialPowerData();
			productMaterialPower.setPm1Id(c.getString(c.getColumnIndex("PM1_ID")));
			productMaterialPower.setPm1Cu1Id(c.getString(c.getColumnIndex("PM1_CU1_ID")));
			productMaterialPower.setPm1Vc2Id(c.getString(c.getColumnIndex("PM1_VC2_ID")));
			productMaterialPower.setPm1Vp1Id(c.getString(c.getColumnIndex("PM1_VP1_ID")));
			productMaterialPower.setPm1Power(c.getString(c.getColumnIndex("PM1_Power")));
			productMaterialPower.setPm1OnceQty(c.getInt(c.getColumnIndex("PM1_OnceQty")));
			productMaterialPower.setPm1Period(c.getString(c.getColumnIndex("PM1_Period")));
			productMaterialPower.setPm1IntervalStart(c.getString(c.getColumnIndex("PM1_IntervalStart")));
			productMaterialPower.setPm1IntervalFinish(c.getString(c.getColumnIndex("PM1_IntervalFinish")));
			productMaterialPower.setPm1StartDate(c.getString(c.getColumnIndex("PM1_StartDate")));
			productMaterialPower.setPm1PeriodQty(c.getInt(c.getColumnIndex("PM1_PeriodQty")));
			break;
		}
		return productMaterialPower;
	}

	public List<String> findVendingProLinkByVcId(String vcId) {
		
		List<String> list = new ArrayList<String>();
		SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
		Cursor c = db.rawQuery(
				"SELECT vp.VP1_PD1_ID VP1_PD1_ID FROM VendingProLink vp join ProductMaterialPower pm ON vp.VP1_ID=pm.PM1_VP1_ID WHERE PM1_VC2_ID=?",
				new String[] { vcId });
		while (c.moveToNext()) {
			list.add(c.getString(c.getColumnIndex("VP1_PD1_ID")));
		}
		return list;
	}

	/**
	 * 增加产品领料权限记录 单条
	 * 
	 * @param productMaterialPower
	 * @return
	 */
	public boolean addProductMaterialPower(ProductMaterialPowerData productMaterialPower) {
		String insertSql = "insert into ProductMaterialPower(PM1_ID,PM1_M02_ID,PM1_CU1_ID,PM1_VC2_ID,PM1_VP1_ID,PM1_Power,PM1_OnceQty,PM1_Period,PM1_IntervalStart,PM1_IntervalFinish,PM1_StartDate,PM1_PeriodQty,"
				+ "PM1_CreateUser,PM1_CreateTime,PM1_ModifyUser,PM1_ModifyTime,PM1_RowVersion)"
				+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
		SQLiteStatement stat = db.compileStatement(insertSql);
		stat.bindString(1, productMaterialPower.getPm1Id());
		stat.bindString(2, productMaterialPower.getPm1M02Id());
		stat.bindString(3, productMaterialPower.getPm1Cu1Id());
		stat.bindString(4, productMaterialPower.getPm1Vc2Id());
		stat.bindString(5, productMaterialPower.getPm1Vp1Id());
		stat.bindString(6, productMaterialPower.getPm1Power());
		stat.bindLong(7, productMaterialPower.getPm1OnceQty());
		stat.bindString(8, productMaterialPower.getPm1Period());
		stat.bindString(9, productMaterialPower.getPm1IntervalStart());
		stat.bindString(10, productMaterialPower.getPm1IntervalFinish());
		stat.bindString(11, productMaterialPower.getPm1StartDate());
		stat.bindLong(12, productMaterialPower.getPm1PeriodQty());
		stat.bindString(13, productMaterialPower.getPm1CreateUser());
		stat.bindString(14, productMaterialPower.getPm1CreateTime());
		stat.bindString(15, productMaterialPower.getPm1ModifyUser());
		stat.bindString(16, productMaterialPower.getPm1ModifyTime());
		stat.bindString(17, productMaterialPower.getPm1RowVersion());
		long i = stat.executeInsert();

		return i > 0;
	}

	/**
	 * 批量增加产品领料权限记录
	 * 
	 * @param list
	 */
	public boolean batchAddProductMaterialPower(List<ProductMaterialPowerData> list) {
		boolean flag = false;
		String insertSql = "insert into ProductMaterialPower(PM1_ID,PM1_M02_ID,PM1_CU1_ID,PM1_VC2_ID,PM1_VP1_ID,PM1_Power,PM1_OnceQty,PM1_Period,PM1_IntervalStart,PM1_IntervalFinish,PM1_StartDate,PM1_PeriodQty,"
				+ "PM1_CreateUser,PM1_CreateTime,PM1_ModifyUser,PM1_ModifyTime,PM1_RowVersion)"
				+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
		try {
			// 开启事务
			db.beginTransaction();
			for (ProductMaterialPowerData productMaterialPower : list) {
				SQLiteStatement stat = db.compileStatement(insertSql);
				stat.bindString(1, productMaterialPower.getPm1Id());
				stat.bindString(2, productMaterialPower.getPm1M02Id());
				stat.bindString(3, productMaterialPower.getPm1Cu1Id());
				stat.bindString(4, productMaterialPower.getPm1Vc2Id());
				stat.bindString(5, productMaterialPower.getPm1Vp1Id());
				stat.bindString(6, productMaterialPower.getPm1Power());
				stat.bindLong(7, productMaterialPower.getPm1OnceQty());
				stat.bindString(8, productMaterialPower.getPm1Period());
				stat.bindString(9, productMaterialPower.getPm1IntervalStart());
				stat.bindString(10, productMaterialPower.getPm1IntervalFinish());
				stat.bindString(11, productMaterialPower.getPm1StartDate());
				stat.bindLong(12, productMaterialPower.getPm1PeriodQty());
				stat.bindString(13, productMaterialPower.getPm1CreateUser());
				stat.bindString(14, productMaterialPower.getPm1CreateTime());
				stat.bindString(15, productMaterialPower.getPm1ModifyUser());
				stat.bindString(16, productMaterialPower.getPm1ModifyTime());
				stat.bindString(17, productMaterialPower.getPm1RowVersion());
				stat.executeInsert();
			}
			// 数据插入成功，设置事物成功标志
			db.setTransactionSuccessful();
			// 保存数据
			db.endTransaction();
			flag = true;
		} catch (SQLException e) {
			// 结束事物，在这里没有设置成功标志，结束后不保存
			ZillionLog.e(this.getClass().getName(), e.getMessage(), e);
			db.endTransaction();
			e.printStackTrace();
		}
		return flag;
	}

	public boolean deleteAll() {
		boolean flag = false;
		String deleteSql = "DELETE FROM ProductMaterialPower";
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
			ZillionLog.e(this.getClass().getName(), e.getMessage(), e);
			db.endTransaction();
			e.printStackTrace();
		}
		return flag;
	}
}
