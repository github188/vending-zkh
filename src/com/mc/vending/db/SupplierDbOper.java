package com.mc.vending.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.mc.vending.data.SupplierData;

/**
 * 货主　操作类
 *                       
 * @Filename: SupplierDbOper.java
 * @Version: 1.0
 * @Author: fenghu
 * @Email: fenghu@mightcloud.com
 *
 */
public class SupplierDbOper {
    public List<SupplierData> findAll() {
        List<SupplierData> list = new ArrayList<SupplierData>();
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        Cursor c = db.rawQuery("SELECT * FROM Supplier", null);
        while (c.moveToNext()) {
            SupplierData supplier = new SupplierData();
            supplier.setSp1Id(c.getString(c.getColumnIndex("SP1_ID")));
            supplier.setSp1M02Id(c.getString(c.getColumnIndex("SP1_M02_ID")));
            supplier.setSp1Code(c.getString(c.getColumnIndex("SP1_CODE")));
            supplier.setSp1Name(c.getString(c.getColumnIndex("SP1_Name")));
            supplier.setSp1CreateUser(c.getString(c.getColumnIndex("SP1_CreateUser")));
            supplier.setSp1CreateTime(c.getString(c.getColumnIndex("SP1_CreateTime")));
            supplier.setSp1ModifyUser(c.getString(c.getColumnIndex("SP1_ModifyUser")));
            supplier.setSp1ModifyTime(c.getString(c.getColumnIndex("SP1_ModifyTime")));
            supplier.setSp1RowVersion(c.getString(c.getColumnIndex("SP1_RowVersion")));
            list.add(supplier);
        }
        return list;
    }

    public Map<String, String> findAllMap() {
        Map<String, String> map = new HashMap<String, String>();
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        Cursor c = db.rawQuery("SELECT SP1_ID,SP1_CODE FROM Supplier", null);
        while (c.moveToNext()) {
            String spId = c.getString(c.getColumnIndex("SP1_ID"));
            String spCode = c.getString(c.getColumnIndex("SP1_CODE"));
            map.put(spId, spCode);
        }
        return map;
    }

    /**
     * 根据货主ID查询货主记录信息
     * @param spId
     * @return
     */
    public SupplierData getSupplierBySpId(String spId) {
        SupplierData supplier = null;
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        Cursor c = db.rawQuery(
            "SELECT SP1_ID,SP1_CODE,SP1_Name FROM Supplier WHERE SP1_ID=? limit 1",
            new String[] { spId });
        while (c.moveToNext()) {
            supplier = new SupplierData();
            supplier.setSp1Id(c.getString(c.getColumnIndex("SP1_ID")));
            supplier.setSp1Code(c.getString(c.getColumnIndex("SP1_CODE")));
            supplier.setSp1Name(c.getString(c.getColumnIndex("SP1_Name")));
            break;
        }
        return supplier;
    }

    /**
     * 增加货主记录数据，单条增加
     * @param product
     * @return
     */
    public boolean addSupplier(SupplierData supplier) {
        String insertSql = "insert into Supplier(SP1_ID,SP1_M02_ID,SP1_CODE,SP1_Name,SP1_CreateUser,SP1_CreateTime,SP1_ModifyUser,SP1_ModifyTime,SP1_RowVersion)"
                           + "values(?,?,?,?,?,?,?,?,?)";
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        SQLiteStatement stat = db.compileStatement(insertSql);
        stat.bindString(1, supplier.getSp1Id());
        stat.bindString(2, supplier.getSp1M02Id());
        stat.bindString(3, supplier.getSp1Code());
        stat.bindString(4, supplier.getSp1Name());
        stat.bindString(5, supplier.getSp1CreateUser());
        stat.bindString(6, supplier.getSp1CreateTime());
        stat.bindString(7, supplier.getSp1ModifyUser());
        stat.bindString(8, supplier.getSp1ModifyTime());
        stat.bindString(9, supplier.getSp1RowVersion());
        long i = stat.executeInsert();
        return i > 0;
    }

    /**
     * 批量增加货主记录数据
     * @param list
     */
    public boolean batchAddSupplier(List<SupplierData> list) {
        boolean flag = false;
        String insertSql = "insert into Supplier(SP1_ID,SP1_M02_ID,SP1_CODE,SP1_Name,SP1_CreateUser,SP1_CreateTime,SP1_ModifyUser,SP1_ModifyTime,SP1_RowVersion)"
                           + "values(?,?,?,?,?,?,?,?,?)";
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        try {
            //开启事务
            db.beginTransaction();
            for (SupplierData supplier : list) {
                SQLiteStatement stat = db.compileStatement(insertSql);
                stat.bindString(1, supplier.getSp1Id());
                stat.bindString(2, supplier.getSp1M02Id());
                stat.bindString(3, supplier.getSp1Code());
                stat.bindString(4, supplier.getSp1Name());
                stat.bindString(5, supplier.getSp1CreateUser());
                stat.bindString(6, supplier.getSp1CreateTime());
                stat.bindString(7, supplier.getSp1ModifyUser());
                stat.bindString(8, supplier.getSp1ModifyTime());
                stat.bindString(9, supplier.getSp1RowVersion());
                stat.executeInsert();
            }
            //数据插入成功，设置事物成功标志  
            db.setTransactionSuccessful();
            //保存数据
            db.endTransaction();
            flag = true;
        } catch (SQLException e) {
            //结束事物，在这里没有设置成功标志，结束后不保存
            db.endTransaction();
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 批量增加货主记录数据
     * @param list
     */
    public boolean batchUpdateSupplier(List<SupplierData> list) {
        boolean flag = false;
        String insertSql = "UPDATE Supplier SET SP1_M02_ID=?,SP1_CODE=?,SP1_Name=?,SP1_CreateUser=?,SP1_CreateTime=?,SP1_ModifyUser=?,SP1_ModifyTime=?,SP1_RowVersion=?"
                           + " WHERE SP1_ID=?";
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        try {
            //开启事务
            db.beginTransaction();
            for (SupplierData supplier : list) {
                SQLiteStatement stat = db.compileStatement(insertSql);
                stat.bindString(1, supplier.getSp1M02Id());
                stat.bindString(2, supplier.getSp1Code());
                stat.bindString(3, supplier.getSp1Name());
                stat.bindString(4, supplier.getSp1CreateUser());
                stat.bindString(5, supplier.getSp1CreateTime());
                stat.bindString(6, supplier.getSp1ModifyUser());
                stat.bindString(7, supplier.getSp1ModifyTime());
                stat.bindString(8, supplier.getSp1RowVersion());
                stat.bindString(9, supplier.getSp1Id());
                stat.executeUpdateDelete();
            }
            //数据插入成功，设置事物成功标志  
            db.setTransactionSuccessful();
            //保存数据
            db.endTransaction();
            flag = true;
        } catch (SQLException e) {
            //结束事物，在这里没有设置成功标志，结束后不保存
            db.endTransaction();
            e.printStackTrace();
        }
        return flag;
    }

    public boolean deleteAll() {
        boolean flag = false;
        String deleteSql = "DELETE FROM Supplier";
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        try {
            //开启事务
            db.beginTransaction();
            SQLiteStatement stat = db.compileStatement(deleteSql);
            stat.executeInsert();
            //数据插入成功，设置事物成功标志  
            db.setTransactionSuccessful();
            //保存数据
            db.endTransaction();
            flag = true;
        } catch (SQLException e) {
            //结束事物，在这里没有设置成功标志，结束后不保存
            db.endTransaction();
            e.printStackTrace();
        }
        return flag;
    }
}
