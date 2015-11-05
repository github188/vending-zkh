package com.mc.vending.db;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.mc.vending.data.CustomerEmpLinkData;
import com.mc.vending.tools.ZillionLog;

/**
 * 客户员工 　操作类
 *                       
 * @Filename: CustomerEmpLinkDbOper.java
 * @Version: 1.0
 * @Author: fenghu
 * @Email: fenghu@mightcloud.com
 *
 */
public class CustomerEmpLinkDbOper {

    public List<CustomerEmpLinkData> findAll() {
        List<CustomerEmpLinkData> list = new ArrayList<CustomerEmpLinkData>();
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        Cursor c = db.rawQuery("SELECT * FROM CustomerEmpLink", null);
        while (c.moveToNext()) {
            CustomerEmpLinkData customerEmpLink = new CustomerEmpLinkData();
            customerEmpLink.setCe1Id(c.getString(c.getColumnIndex("CE1_ID")));
            customerEmpLink.setCe1M02Id(c.getString(c.getColumnIndex("CE1_M02_ID")));
            customerEmpLink.setCe1Cu1Id(c.getString(c.getColumnIndex("CE1_CU1_ID")));
            customerEmpLink.setCe1Code(c.getString(c.getColumnIndex("CE1_CODE")));
            customerEmpLink.setCe1Name(c.getString(c.getColumnIndex("CE1_Name")));
            customerEmpLink.setCe1EnglishName(c.getString(c.getColumnIndex("CE1_EnglishName")));
            customerEmpLink.setCe1Sex(c.getString(c.getColumnIndex("CE1_Sex")));
            customerEmpLink.setCe1Dp1Id(c.getString(c.getColumnIndex("CE1_DP1_ID")));
            customerEmpLink.setCe1DicIdJob(c.getString(c.getColumnIndex("CE1_Dic_ID_JOB")));
            customerEmpLink.setCe1Phone(c.getString(c.getColumnIndex("CE1_Phone")));
            customerEmpLink.setCe1Status(c.getString(c.getColumnIndex("CE1_Status")));
            customerEmpLink.setCe1Remark(c.getString(c.getColumnIndex("CE1_Remark")));
            customerEmpLink.setCe1CreateUser(c.getString(c.getColumnIndex("CE1_CreateUser")));
            customerEmpLink.setCe1CreateTime(c.getString(c.getColumnIndex("CE1_CreateTime")));
            customerEmpLink.setCe1ModifyUser(c.getString(c.getColumnIndex("CE1_ModifyUser")));
            customerEmpLink.setCe1ModifyTime(c.getString(c.getColumnIndex("CE1_ModifyTime")));
            customerEmpLink.setCe1RowVersion(c.getString(c.getColumnIndex("CE1_RowVersion")));
            list.add(customerEmpLink);
        }
        return list;
    }

    /**
     * 根据客户员工ID查询客户员工表信息
     * @param ceId　客户员工ID
     * @return
     */
    public CustomerEmpLinkData getCustomerEmpLinkByCeId(String ceId) {
        CustomerEmpLinkData customerEmpLink = null;
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        Cursor c = db.rawQuery("SELECT * FROM CustomerEmpLink WHERE CE1_ID=? limit 1",
            new String[] { ceId });
        while (c.moveToNext()) {
            customerEmpLink = new CustomerEmpLinkData();
            customerEmpLink.setCe1Id(c.getString(c.getColumnIndex("CE1_ID")));
            customerEmpLink.setCe1M02Id(c.getString(c.getColumnIndex("CE1_M02_ID")));
            customerEmpLink.setCe1Cu1Id(c.getString(c.getColumnIndex("CE1_CU1_ID")));
            customerEmpLink.setCe1Code(c.getString(c.getColumnIndex("CE1_CODE")));
            customerEmpLink.setCe1Name(c.getString(c.getColumnIndex("CE1_Name")));
            customerEmpLink.setCe1EnglishName(c.getString(c.getColumnIndex("CE1_EnglishName")));
            customerEmpLink.setCe1Sex(c.getString(c.getColumnIndex("CE1_Sex")));
            customerEmpLink.setCe1Dp1Id(c.getString(c.getColumnIndex("CE1_DP1_ID")));
            customerEmpLink.setCe1DicIdJob(c.getString(c.getColumnIndex("CE1_Dic_ID_JOB")));
            customerEmpLink.setCe1Phone(c.getString(c.getColumnIndex("CE1_Phone")));
            customerEmpLink.setCe1Status(c.getString(c.getColumnIndex("CE1_Status")));
            customerEmpLink.setCe1Remark(c.getString(c.getColumnIndex("CE1_Remark")));
            customerEmpLink.setCe1CreateUser(c.getString(c.getColumnIndex("CE1_CreateUser")));
            customerEmpLink.setCe1CreateTime(c.getString(c.getColumnIndex("CE1_CreateTime")));
            customerEmpLink.setCe1ModifyUser(c.getString(c.getColumnIndex("CE1_ModifyUser")));
            customerEmpLink.setCe1ModifyTime(c.getString(c.getColumnIndex("CE1_ModifyTime")));
            customerEmpLink.setCe1RowVersion(c.getString(c.getColumnIndex("CE1_RowVersion")));
            break;
        }
        return customerEmpLink;
    }

    /**
     * 增加客户员工记录，单条
     * @param customerEmpLink
     * @return
     */
    public boolean addCustomerEmpLink(CustomerEmpLinkData customerEmpLink) {
        String insertSql = "insert into CustomerEmpLink(CE1_ID,CE1_M02_ID,CE1_CU1_ID,CE1_CODE,CE1_Name,CE1_EnglishName,CE1_Sex,CE1_DP1_ID,CE1_Dic_ID_JOB,CE1_Phone,CE1_Status,CE1_Remark,"
                           + "CE1_CreateUser,CE1_CreateTime,CE1_ModifyUser,CE1_ModifyTime,CE1_RowVersion)"
                           + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        SQLiteStatement stat = db.compileStatement(insertSql);
        stat.bindString(1, customerEmpLink.getCe1Id());
        stat.bindString(2, customerEmpLink.getCe1M02Id());
        stat.bindString(3, customerEmpLink.getCe1Cu1Id());
        stat.bindString(4, customerEmpLink.getCe1Code());
        stat.bindString(5, customerEmpLink.getCe1Name());
        stat.bindString(6, customerEmpLink.getCe1EnglishName());
        stat.bindString(7, customerEmpLink.getCe1Sex());
        stat.bindString(8, customerEmpLink.getCe1Dp1Id());
        stat.bindString(9, customerEmpLink.getCe1DicIdJob());
        stat.bindString(10, customerEmpLink.getCe1Phone());
        stat.bindString(11, customerEmpLink.getCe1Status());
        stat.bindString(12, customerEmpLink.getCe1Remark());
        stat.bindString(13, customerEmpLink.getCe1CreateUser());
        stat.bindString(14, customerEmpLink.getCe1CreateTime());
        stat.bindString(15, customerEmpLink.getCe1ModifyUser());
        stat.bindString(16, customerEmpLink.getCe1ModifyTime());
        stat.bindString(17, customerEmpLink.getCe1RowVersion());
        long i = stat.executeInsert();

        return i > 0;
    }

    /**
     * 批量增加客户员工记录
     * @param list
     */
    public boolean batchAddCustomerEmpLink(List<CustomerEmpLinkData> list) {
        boolean flag = false;
        String insertSql = "insert into CustomerEmpLink(CE1_ID,CE1_M02_ID,CE1_CU1_ID,CE1_CODE,CE1_Name,CE1_EnglishName,CE1_Sex,CE1_DP1_ID,CE1_Dic_ID_JOB,CE1_Phone,CE1_Status,CE1_Remark,"
                           + "CE1_CreateUser,CE1_CreateTime,CE1_ModifyUser,CE1_ModifyTime,CE1_RowVersion)"
                           + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        try {
            //开启事务
            db.beginTransaction();
            for (CustomerEmpLinkData customerEmpLink : list) {
                SQLiteStatement stat = db.compileStatement(insertSql);
                stat.bindString(1, customerEmpLink.getCe1Id());
                stat.bindString(2, customerEmpLink.getCe1M02Id());
                stat.bindString(3, customerEmpLink.getCe1Cu1Id());
                stat.bindString(4, customerEmpLink.getCe1Code());
                stat.bindString(5, customerEmpLink.getCe1Name());
                stat.bindString(6, customerEmpLink.getCe1EnglishName());
                stat.bindString(7, customerEmpLink.getCe1Sex());
                stat.bindString(8, customerEmpLink.getCe1Dp1Id());
                stat.bindString(9, customerEmpLink.getCe1DicIdJob());
                stat.bindString(10, customerEmpLink.getCe1Phone());
                stat.bindString(11, customerEmpLink.getCe1Status());
                stat.bindString(12, customerEmpLink.getCe1Remark());
                stat.bindString(13, customerEmpLink.getCe1CreateUser());
                stat.bindString(14, customerEmpLink.getCe1CreateTime());
                stat.bindString(15, customerEmpLink.getCe1ModifyUser());
                stat.bindString(16, customerEmpLink.getCe1ModifyTime());
                stat.bindString(17, customerEmpLink.getCe1RowVersion());
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

    public boolean deleteAll() {
        boolean flag = false;
        String deleteSql = "DELETE FROM CustomerEmpLink";
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
