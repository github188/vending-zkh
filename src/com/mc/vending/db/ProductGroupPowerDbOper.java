package com.mc.vending.db;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.mc.vending.data.ProductGroupPowerData;

/**
 * 产品组合权限　操作类
 *                       
 * @Filename: ProductGroupHeadDbOper.java
 * @Version: 1.0
 * @Author: fenghu
 * @Email: fenghu@mightcloud.com
 *
 */
public class ProductGroupPowerDbOper {

    public List<ProductGroupPowerData> findAll() {
        List<ProductGroupPowerData> list = new ArrayList<ProductGroupPowerData>();
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        Cursor c = db.rawQuery("SELECT * FROM productGroupPower", null);

        while (c.moveToNext()) {
            ProductGroupPowerData productGroupPower = new ProductGroupPowerData();
            productGroupPower.setPp1Id(c.getString(c.getColumnIndex("PP1_ID")));
            productGroupPower.setPp1M02Id(c.getString(c.getColumnIndex("PP1_M02_ID")));
            productGroupPower.setPp1Cu1Id(c.getString(c.getColumnIndex("PP1_CU1_ID")));
            productGroupPower.setPp1Pg1Id(c.getString(c.getColumnIndex("PP1_PG1_ID")));
            productGroupPower.setPp1Cd1Id(c.getString(c.getColumnIndex("PP1_CD1_ID")));
            productGroupPower.setPp1Power(c.getString(c.getColumnIndex("PP1_Power")));
            productGroupPower.setPp1Period(c.getString(c.getColumnIndex("PP1_Period")));
            productGroupPower
                .setPp1IntervalStart(c.getString(c.getColumnIndex("PP1_IntervalStart")));
            productGroupPower.setPp1IntervalFinish(c.getString(c
                .getColumnIndex("PP1_IntervalFinish")));
            productGroupPower.setPp1StartDate(c.getString(c.getColumnIndex("PP1_StartDate")));
            productGroupPower.setPp1PeriodNum(c.getInt(c.getColumnIndex("PP1_PeriodNum")));
            productGroupPower.setPp1CreateUser(c.getString(c.getColumnIndex("PP1_CreateUser")));
            productGroupPower.setPp1CreateTime(c.getString(c.getColumnIndex("PP1_CreateTime")));
            productGroupPower.setPp1ModifyUser(c.getString(c.getColumnIndex("PP1_ModifyUser")));
            productGroupPower.setPp1ModifyTime(c.getString(c.getColumnIndex("PP1_ModifyTime")));
            productGroupPower.setPp1RowVersion(c.getString(c.getColumnIndex("PP1_RowVersion")));
            list.add(productGroupPower);
        }
        return list;
    }

    /**
     * 通过“客户ID””产品组合主表.ID””卡ID”，查询“产品组合权限“表
     * @param cusId　客户ID
     * @param pg1Id　产品组合主表.ID
     * @param cardId　卡ID
     * @return
     */
    public ProductGroupPowerData getProductGroupPower(String cusId, String pg1Id, String cardId) {
        ProductGroupPowerData productGroupPower = null;
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        Cursor c = db
            .rawQuery(
                "SELECT * FROM productGroupPower WHERE PP1_CU1_ID=? and PP1_PG1_ID=? and PP1_CD1_ID=? limit 1",
                new String[] { cusId, pg1Id, cardId });

        while (c.moveToNext()) {
            productGroupPower = new ProductGroupPowerData();
            productGroupPower.setPp1Id(c.getString(c.getColumnIndex("PP1_ID")));
            productGroupPower.setPp1M02Id(c.getString(c.getColumnIndex("PP1_M02_ID")));
            productGroupPower.setPp1Cu1Id(c.getString(c.getColumnIndex("PP1_CU1_ID")));
            productGroupPower.setPp1Pg1Id(c.getString(c.getColumnIndex("PP1_PG1_ID")));
            productGroupPower.setPp1Cd1Id(c.getString(c.getColumnIndex("PP1_CD1_ID")));
            productGroupPower.setPp1Power(c.getString(c.getColumnIndex("PP1_Power")));
            productGroupPower.setPp1Period(c.getString(c.getColumnIndex("PP1_Period")));
            productGroupPower
                .setPp1IntervalStart(c.getString(c.getColumnIndex("PP1_IntervalStart")));
            productGroupPower.setPp1IntervalFinish(c.getString(c
                .getColumnIndex("PP1_IntervalFinish")));
            productGroupPower.setPp1StartDate(c.getString(c.getColumnIndex("PP1_StartDate")));
            productGroupPower.setPp1PeriodNum(c.getInt(c.getColumnIndex("PP1_PeriodNum")));
            productGroupPower.setPp1CreateUser(c.getString(c.getColumnIndex("PP1_CreateUser")));
            productGroupPower.setPp1CreateTime(c.getString(c.getColumnIndex("PP1_CreateTime")));
            productGroupPower.setPp1ModifyUser(c.getString(c.getColumnIndex("PP1_ModifyUser")));
            productGroupPower.setPp1ModifyTime(c.getString(c.getColumnIndex("PP1_ModifyTime")));
            productGroupPower.setPp1RowVersion(c.getString(c.getColumnIndex("PP1_RowVersion")));
            break;
        }
        return productGroupPower;
    }

    /**
     * 增加产品组合主表记录　单条　
     * @param productGroupHead
     * @return
     */
    public boolean addProductGroupPower(ProductGroupPowerData productGroupPower) {
        String insertSql = "insert into ProductGroupPower(PP1_ID,PP1_M02_ID,PP1_CU1_ID,PP1_PG1_ID,PP1_CD1_ID,PP1_Power,PP1_Period,PP1_IntervalStart,PP1_IntervalFinish,PP1_StartDate,PP1_PeriodNum,"
                           + "PP1_CreateUser,PP1_CreateTime,PP1_ModifyUser,PP1_ModifyTime,PP1_RowVersion)"
                           + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        SQLiteStatement stat = db.compileStatement(insertSql);
        stat.bindString(1, productGroupPower.getPp1Id());
        stat.bindString(2, productGroupPower.getPp1M02Id());
        stat.bindString(3, productGroupPower.getPp1Cu1Id());
        stat.bindString(4, productGroupPower.getPp1Pg1Id());
        stat.bindString(5, productGroupPower.getPp1Cd1Id());
        stat.bindString(6, productGroupPower.getPp1Power());
        stat.bindString(7, productGroupPower.getPp1Period());
        stat.bindString(8, productGroupPower.getPp1IntervalStart());
        stat.bindString(9, productGroupPower.getPp1IntervalFinish());
        stat.bindString(10, productGroupPower.getPp1StartDate());
        stat.bindLong(11, productGroupPower.getPp1PeriodNum());
        stat.bindString(12, productGroupPower.getPp1CreateUser());
        stat.bindString(13, productGroupPower.getPp1CreateTime());
        stat.bindString(14, productGroupPower.getPp1ModifyUser());
        stat.bindString(15, productGroupPower.getPp1ModifyTime());
        stat.bindString(16, productGroupPower.getPp1RowVersion());
        long i = stat.executeInsert();
        return i > 0;
    }

    /**
     * 批量增加产品组合主表记录数据
     * @param list
     */
    public boolean batchAddProductGroupPower(List<ProductGroupPowerData> list) {
        boolean flag = false;
        String insertSql = "insert into ProductGroupPower(PP1_ID,PP1_M02_ID,PP1_CU1_ID,PP1_PG1_ID,PP1_CD1_ID,PP1_Power,PP1_Period,PP1_IntervalStart,PP1_IntervalFinish,PP1_StartDate,PP1_PeriodNum,"
                           + "PP1_CreateUser,PP1_CreateTime,PP1_ModifyUser,PP1_ModifyTime,PP1_RowVersion)"
                           + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        try {
            //开启事务
            db.beginTransaction();
            for (ProductGroupPowerData productGroupPower : list) {
                SQLiteStatement stat = db.compileStatement(insertSql);
                stat.bindString(1, productGroupPower.getPp1Id());
                stat.bindString(2, productGroupPower.getPp1M02Id());
                stat.bindString(3, productGroupPower.getPp1Cu1Id());
                stat.bindString(4, productGroupPower.getPp1Pg1Id());
                stat.bindString(5, productGroupPower.getPp1Cd1Id());
                stat.bindString(6, productGroupPower.getPp1Power());
                stat.bindString(7, productGroupPower.getPp1Period());
                stat.bindString(8, productGroupPower.getPp1IntervalStart());
                stat.bindString(9, productGroupPower.getPp1IntervalFinish());
                stat.bindString(10, productGroupPower.getPp1StartDate());
                stat.bindLong(11, productGroupPower.getPp1PeriodNum());
                stat.bindString(12, productGroupPower.getPp1CreateUser());
                stat.bindString(13, productGroupPower.getPp1CreateTime());
                stat.bindString(14, productGroupPower.getPp1ModifyUser());
                stat.bindString(15, productGroupPower.getPp1ModifyTime());
                stat.bindString(16, productGroupPower.getPp1RowVersion());
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

    public boolean deleteAll() {
        boolean flag = false;
        String deleteSql = "DELETE FROM ProductGroupPower";
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
            db.endTransaction();
            e.printStackTrace();
        }
        return flag;
    }
}
