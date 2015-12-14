package com.mc.vending.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.mc.vending.config.Constant;
import com.mc.vending.data.ProductData;
import com.mc.vending.tools.StringHelper;

/**
 * 产品 操作类
 * 
 * @Filename: ProductDbOper.java
 * @Version: 1.0
 * @Author: fenghu
 * @Email: fenghu@mightcloud.com
 *
 */
public class ProductDbOper {

	/**
	 * 查询所有的产品，封装成Map返回,key为skudId, value为产品名称
	 * 
	 * @return
	 */
	public Map<String, String> findAllProduct() {
		Map<String, String> map = new HashMap<String, String>();
		SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
		Cursor c = db.rawQuery("SELECT PD1_ID, PD1_Name FROM Product", null);
		while (c.moveToNext()) {
			String skuId = c.getString(c.getColumnIndex("PD1_ID"));
			String name = c.getString(c.getColumnIndex("PD1_Name"));
			map.put(skuId, name);
		}
		return map;
	}

	/**
	 * 根据SKUID串查询产品列表
	 * 
	 * @param ids
	 *            多个SKUID连接组成的SKUID串
	 * @return
	 */
	public List<ProductData> findProductByIds(String ids) {
		if (StringHelper.isEmpty(ids, true)) {
			return new ArrayList<ProductData>(0);
		}
		List<ProductData> list = new ArrayList<ProductData>();
		SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
		Cursor c = db.rawQuery("SELECT * FROM Product WHERE PD1_ID IN(" + ids + ")", null);
		while (c.moveToNext()) {
			ProductData product = new ProductData();
			product.setPd1Id(c.getString(c.getColumnIndex("PD1_ID")));
			product.setPd1M02Id(c.getString(c.getColumnIndex("PD1_M02_ID")));
			product.setPd1Code(c.getString(c.getColumnIndex("PD1_CODE")));
			product.setPd1Name(c.getString(c.getColumnIndex("PD1_Name")));
			product.setPd1Description(c.getString(c.getColumnIndex("PD1_Description")));
			product.setPd1ManufactureModel(c.getString(c.getColumnIndex("PD1_ManufactureModel")));
			if (Integer.valueOf(Constant.HEADER_VALUE_CLIENTVER.replace(".", "")) >= 400) {
				product.setPd1Length(c.getString(c.getColumnIndex("PD1_Length")));
				product.setPd1Weight(c.getString(c.getColumnIndex("PD1_Weight")));
			}
			product.setPd1Size(c.getString(c.getColumnIndex("PD1_Size")));
			product.setPd1Brand(c.getString(c.getColumnIndex("PD1_Brand")));
			product.setPd1Package(c.getString(c.getColumnIndex("PD1_Package")));
			product.setPd1Unit(c.getString(c.getColumnIndex("PD1_Unit")));
			product.setPd1LastImportTime(c.getString(c.getColumnIndex("PD1_LastImportTime")));
			list.add(product);
		}
		return list;
	}

	/**
	 * 根据产品ID查询产品记录 单条
	 * 
	 * @param id
	 *            产品ID,即SKUID
	 * @return
	 */
	public ProductData getProductById(String id) {
		ProductData product = null;
		SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
		Cursor c = db.rawQuery("SELECT * FROM Product WHERE PD1_ID=? limit 1", new String[] { id });
		while (c.moveToNext()) {
			product = new ProductData();
			product.setPd1Id(c.getString(c.getColumnIndex("PD1_ID")));
			product.setPd1M02Id(c.getString(c.getColumnIndex("PD1_M02_ID")));
			product.setPd1Code(c.getString(c.getColumnIndex("PD1_CODE")));
			product.setPd1Name(c.getString(c.getColumnIndex("PD1_Name")));
			product.setPd1Description(c.getString(c.getColumnIndex("PD1_Description")));
			product.setPd1ManufactureModel(c.getString(c.getColumnIndex("PD1_ManufactureModel")));
			product.setPd1Size(c.getString(c.getColumnIndex("PD1_Size")));
			product.setPd1Brand(c.getString(c.getColumnIndex("PD1_Brand")));
			if (Integer.valueOf(Constant.HEADER_VALUE_CLIENTVER.replace(".", "")) >= 400) {
				product.setPd1Length(c.getString(c.getColumnIndex("PD1_Length")));
				product.setPd1Weight(c.getString(c.getColumnIndex("PD1_Weight")));
			}
			product.setPd1Package(c.getString(c.getColumnIndex("PD1_Package")));
			product.setPd1Unit(c.getString(c.getColumnIndex("PD1_Unit")));
			product.setPd1LastImportTime(c.getString(c.getColumnIndex("PD1_LastImportTime")));
			break;
		}
		return product;
	}

	/**
	 * 增加产品记录数据，单条增加
	 * 
	 * @param product
	 * @return
	 */
	public boolean addProduct(ProductData product) {
		String insertSql = "insert into Product(PD1_ID,PD1_M02_ID,PD1_CODE,PD1_Name,PD1_Description,PD1_ManufactureModel,PD1_Size,PD1_Brand,PD1_Package,PD1_Unit,PD1_LastImportTime)"
				+ " values(?,?,?,?,?,?,?,?,?,?,?)";
		if (Integer.valueOf(Constant.HEADER_VALUE_CLIENTVER.replace(".", "")) >= 400) {
			insertSql = "insert into Product(PD1_ID,PD1_M02_ID,PD1_CODE,PD1_Name,PD1_Description,PD1_ManufactureModel,PD1_Size,PD1_Brand,PD1_Package,PD1_Unit,PD1_LastImportTime,PD1_Length,PD1_Weight)"
					+ " values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
		}
		SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
		SQLiteStatement stat = db.compileStatement(insertSql);
		stat.bindString(1, product.getPd1Id());
		stat.bindString(2, product.getPd1M02Id());
		stat.bindString(3, product.getPd1Code());
		stat.bindString(4, product.getPd1Name());
		stat.bindString(5, product.getPd1Description());
		stat.bindString(6, product.getPd1ManufactureModel());
		stat.bindString(7, product.getPd1Size());

		stat.bindString(8, product.getPd1Brand());
		stat.bindString(9, product.getPd1Package());
		stat.bindString(10, product.getPd1Unit());
		stat.bindString(11, product.getPd1LastImportTime());
		if (Integer.valueOf(Constant.HEADER_VALUE_CLIENTVER.replace(".", "")) >= 400) {
			stat.bindString(12, product.getPd1Length());
			stat.bindString(13, product.getPd1Weight());
		}
		long i = stat.executeInsert();
		return i > 0;
	}

	/**
	 * 批量添加产品记录数据
	 * 
	 * @param list
	 */
	public boolean batchAddProduct(List<ProductData> list) {
		boolean flag = false;
		String insertSql = "insert into Product(PD1_ID,PD1_M02_ID,PD1_CODE,PD1_Name,PD1_Description,PD1_ManufactureModel,PD1_Size,PD1_Brand,PD1_Package,PD1_Unit,PD1_LastImportTime)"
				+ "values(?,?,?,?,?,?,?,?,?,?,?)";
		if (Integer.valueOf(Constant.HEADER_VALUE_CLIENTVER.replace(".", "")) >= 400) {
			insertSql = "insert into Product(PD1_ID,PD1_M02_ID,PD1_CODE,PD1_Name,PD1_Description,PD1_ManufactureModel,PD1_Size,PD1_Brand,PD1_Package,PD1_Unit,PD1_LastImportTime,PD1_Length,PD1_Weight)"
					+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
		}
		SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
		try {
			// 开启事务
			db.beginTransaction();
			for (ProductData product : list) {
				SQLiteStatement stat = db.compileStatement(insertSql);
				stat.bindString(1, product.getPd1Id());
				stat.bindString(2, product.getPd1M02Id());
				stat.bindString(3, product.getPd1Code());
				stat.bindString(4, product.getPd1Name());
				stat.bindString(5, product.getPd1Description());
				stat.bindString(6, product.getPd1ManufactureModel());
				stat.bindString(7, product.getPd1Size());
				stat.bindString(8, product.getPd1Brand());
				stat.bindString(9, product.getPd1Package());
				stat.bindString(10, product.getPd1Unit());
				stat.bindString(11, product.getPd1LastImportTime());
				if (Integer.valueOf(Constant.HEADER_VALUE_CLIENTVER.replace(".", "")) >= 400) {
					stat.bindString(12, product.getPd1Length());
					stat.bindString(13, product.getPd1Weight());
				}
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
	 * 批量更新产品记录数据
	 * 
	 * @param list
	 */
	public boolean batchUpdateProduct(List<ProductData> list) {
		boolean flag = false;
		String updateSql = "UPDATE Product SET PD1_M02_ID=?,PD1_CODE=?,PD1_Name=?,PD1_Description=?,PD1_ManufactureModel=?,PD1_Size=?,PD1_Brand=?,PD1_Package=?,PD1_Unit=?,PD1_LastImportTime=?"
				+ " WHERE PD1_ID=?";
		if (Integer.valueOf(Constant.HEADER_VALUE_CLIENTVER.replace(".", "")) >= 400) {
			updateSql = "UPDATE Product SET PD1_M02_ID=?,PD1_CODE=?,PD1_Name=?,PD1_Description=?,PD1_ManufactureModel=?,PD1_Size=?,PD1_Brand=?,PD1_Package=?,PD1_Unit=?,PD1_LastImportTime=?,PD1_Length=?,PD1_Weight=?"
					+ " WHERE PD1_ID=?";
		}
		SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
		try {
			// 开启事务
			db.beginTransaction();
			for (ProductData product : list) {
				SQLiteStatement stat = db.compileStatement(updateSql);
				stat.bindString(1, product.getPd1M02Id());
				stat.bindString(2, product.getPd1Code());
				stat.bindString(3, product.getPd1Name());
				stat.bindString(4, product.getPd1Description());
				stat.bindString(5, product.getPd1ManufactureModel());
				stat.bindString(6, product.getPd1Size());
				stat.bindString(7, product.getPd1Brand());
				stat.bindString(8, product.getPd1Package());
				stat.bindString(9, product.getPd1Unit());
				stat.bindString(10, product.getPd1LastImportTime());
				stat.bindString(11, product.getPd1Id());
				if (Integer.valueOf(Constant.HEADER_VALUE_CLIENTVER.replace(".", "")) >= 400) {
					stat.bindString(12, product.getPd1Length());
					stat.bindString(13, product.getPd1Weight());
				}
				stat.executeUpdateDelete();
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

	public boolean deleteAll() {
		boolean flag = false;
		String deleteSql = "DELETE FROM Product";
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
