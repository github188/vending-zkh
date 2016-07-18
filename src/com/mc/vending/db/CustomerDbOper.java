package com.mc.vending.db;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.mc.vending.data.CardData;
import com.mc.vending.data.CustomerData;
import com.mc.vending.tools.ZillionLog;

/**
 * 客户　操作类
 *                       
 * @Filename: CustomerDbOper.java
 * @Version: 1.0
 * @Author: fenghu
 * @Email: fenghu@mightcloud.com
 *
 */
public class CustomerDbOper {

    public List<CustomerData> findAll() {
        List<CustomerData> list = new ArrayList<CustomerData>();
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        Cursor c = db.rawQuery("SELECT * FROM Customer", null);
        while (c.moveToNext()) {
            CustomerData customer = new CustomerData();
            customer.setCu1Id(c.getString(c.getColumnIndex("CU1_ID")));
            customer.setCu1M02Id(c.getString(c.getColumnIndex("CU1_M02_ID")));
            customer.setCu1Code(c.getString(c.getColumnIndex("CU1_CODE")));
            customer.setCu1Name(c.getString(c.getColumnIndex("CU1_Name")));
            customer.setCu1Relation(c.getString(c.getColumnIndex("CU1_Relation")));
            customer.setCu1RelationPhone(c.getString(c.getColumnIndex("CU1_RelationPhone")));
            customer.setCu1Saler(c.getString(c.getColumnIndex("CU1_Saler")));
            customer.setCu1SalerPhone(c.getString(c.getColumnIndex("CU1_SalerPhone")));
            customer.setCu1Country(c.getString(c.getColumnIndex("CU1_Country")));
            customer.setCu1City(c.getString(c.getColumnIndex("CU1_City")));
            customer.setCu1Area(c.getString(c.getColumnIndex("CU1_Area")));
            customer.setCu1Address(c.getString(c.getColumnIndex("CU1_Address")));
            customer.setCu1LastImportTime(c.getString(c.getColumnIndex("CU1_LastImportTime")));
            customer.setCu1CodeFather(c.getString(c.getColumnIndex("CU1_CODE_Father")));
            customer.setCu1CreateUser(c.getString(c.getColumnIndex("CU1_CreateUser")));
            customer.setCu1CreateTime(c.getString(c.getColumnIndex("CU1_CreateTime")));
            customer.setCu1ModifyUser(c.getString(c.getColumnIndex("CU1_ModifyUser")));
            customer.setCu1ModifyTime(c.getString(c.getColumnIndex("CU1_ModifyTime")));
            customer.setCu1RowVersion(c.getString(c.getColumnIndex("CU1_RowVersion")));
            list.add(customer);
        }
        return list;
    }

    /**
     * 增加客户记录　单条
     * @param customer
     * @return
     */
    public boolean addCustomer(CustomerData customer) {
        String insertSql = "insert into Customer(CU1_ID,CU1_M02_ID,CU1_CODE,CU1_Name,CU1_Relation,CU1_RelationPhone,CU1_Saler,CU1_SalerPhone,CU1_Country,CU1_City,CU1_Area,CU1_Address,CU1_LastImportTime,CU1_CODE_Father,"
                           + "CU1_CreateUser,CU1_CreateTime,CU1_ModifyUser,CU1_ModifyTime,CU1_RowVersion)"
                           + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        SQLiteStatement stat = db.compileStatement(insertSql);
        stat.bindString(1, customer.getCu1Id());
        stat.bindString(2, customer.getCu1M02Id());
        stat.bindString(3, customer.getCu1Code());
        stat.bindString(4, customer.getCu1Name());
        stat.bindString(5, customer.getCu1Relation());
        stat.bindString(6, customer.getCu1RelationPhone());
        stat.bindString(7, customer.getCu1Saler());
        stat.bindString(8, customer.getCu1SalerPhone());
        stat.bindString(9, customer.getCu1Country());
        stat.bindString(10, customer.getCu1City());
        stat.bindString(11, customer.getCu1Area());
        stat.bindString(12, customer.getCu1Address());
        stat.bindString(13, customer.getCu1LastImportTime());
        stat.bindString(14, customer.getCu1CodeFather());
        stat.bindString(15, customer.getCu1CreateUser());
        stat.bindString(16, customer.getCu1CreateTime());
        stat.bindString(17, customer.getCu1ModifyUser());
        stat.bindString(18, customer.getCu1ModifyTime());
        stat.bindString(19, customer.getCu1RowVersion());
        long i = stat.executeInsert();

        return i > 0;
    }

    /**
     * 批量增加客户记录
     * @param list
     */
    public boolean batchAddCustomer(List<CustomerData> list) {
        boolean flag = false;
        String insertSql = "insert into Customer(CU1_ID,CU1_M02_ID,CU1_CODE,CU1_Name,CU1_Relation,CU1_RelationPhone,CU1_Saler,CU1_SalerPhone,CU1_Country,CU1_City,CU1_Area,CU1_Address,CU1_LastImportTime,CU1_CODE_Father,"
                           + "CU1_CreateUser,CU1_CreateTime,CU1_ModifyUser,CU1_ModifyTime,CU1_RowVersion)"
                           + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        try {
            //开启事务
            db.beginTransaction();
            for (CustomerData customer : list) {
                SQLiteStatement stat = db.compileStatement(insertSql);
                stat.bindString(1, customer.getCu1Id());
                stat.bindString(2, customer.getCu1M02Id());
                stat.bindString(3, customer.getCu1Code());
                stat.bindString(4, customer.getCu1Name());
                stat.bindString(5, customer.getCu1Relation());
                stat.bindString(6, customer.getCu1RelationPhone());
                stat.bindString(7, customer.getCu1Saler());
                stat.bindString(8, customer.getCu1SalerPhone());
                stat.bindString(9, customer.getCu1Country());
                stat.bindString(10, customer.getCu1City());
                stat.bindString(11, customer.getCu1Area());
                stat.bindString(12, customer.getCu1Address());
                stat.bindString(13, customer.getCu1LastImportTime());
                stat.bindString(14, customer.getCu1CodeFather());
                stat.bindString(15, customer.getCu1CreateUser());
                stat.bindString(16, customer.getCu1CreateTime());
                stat.bindString(17, customer.getCu1ModifyUser());
                stat.bindString(18, customer.getCu1ModifyTime());
                stat.bindString(19, customer.getCu1RowVersion());
                stat.executeInsert();
            }
            //数据插入成功，设置事物成功标志  
            db.setTransactionSuccessful();
            //保存数据
            db.endTransaction();
            flag = true;
        } catch (SQLException e) {
            //结束事物，在这里没有设置成功标志，结束后不保存
            ZillionLog.e(this.getClass().getName(),e.getMessage(),e);
            db.endTransaction();
            e.printStackTrace();
        }
        return flag;
    }
    /**
	 * 批量删除客户记录
	 * 
	 * @param list
	 */
	public boolean batchDeleteCustomer(List<CustomerData> list) {
		boolean flag = false;
		SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
		try {
			// 开启事务
			db.beginTransaction();
			for (CustomerData customerData : list) {
				db.delete("Customer", "CU1_ID=?", new String[] { customerData.getCu1Id() });
			}
			// 数据成功，设置事物成功标志
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
        String deleteSql = "DELETE FROM Customer";
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        try {
            //开启事务
            db.beginTransaction();
            SQLiteStatement stat = db.compileStatement(deleteSql);
            stat.executeUpdateDelete();
            //数据插入成功，设置事物成功标志  
            db.setTransactionSuccessful();
            //保存数据
            db.endTransaction();
            flag = true;
        } catch (SQLException e) {
            //结束事物，在这里没有设置成功标志，结束后不保存
            ZillionLog.e(this.getClass().getName(),e.getMessage(),e);
            db.endTransaction();
            e.printStackTrace();
        }
        return flag;
    }
}
