package com.mc.vending.db;

import java.util.List;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.mc.vending.data.VendingPasswordData;
import com.mc.vending.tools.ZillionLog;

/**
 * 卡/密码　操作类
 * 
 * @Filename: CardDbOper.java
 * @Version: 1.0
 * @Author: fenghu
 * @Email: fenghu@mightcloud.com
 *
 */
public class VendingPasswordDbOper {

    public VendingPasswordData getVendingPassword() {
        VendingPasswordData vendingPassword = null;
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        Cursor c = db.rawQuery(
                "SELECT VP3_ID,VP3_Password FROM VendingPassword ORDER BY VP3_CreateTime DESC limit 1", null);
        while (c.moveToNext()) {
            vendingPassword = new VendingPasswordData();
            vendingPassword.setVp3Id(c.getString(c.getColumnIndex("VP3_ID")));
            vendingPassword.setVp3Password(c.getString(c.getColumnIndex("VP3_Password")));
            break;
        }
        return vendingPassword;
    }

    /**
     * 根据密码查询售货机强制密码表记录,单条记录
     * 
     * @param password
     * @return
     */
    public VendingPasswordData getVendingPasswordByPassword(String password) {
        VendingPasswordData vendingPassword = null;
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        Cursor c = db
                .rawQuery(
                        "SELECT VP3_ID,VP3_Password FROM VendingPassword WHERE VP3_Password=? ORDER BY VP3_CreateTime DESC limit 1",
                        new String[] { password });
        while (c.moveToNext()) {
            vendingPassword = new VendingPasswordData();
            vendingPassword = new VendingPasswordData();
            vendingPassword.setVp3Id(c.getString(c.getColumnIndex("VP3_ID")));
            vendingPassword.setVp3Password(c.getString(c.getColumnIndex("VP3_Password")));
            break;
        }
        return vendingPassword;
    }

    /**
     * 增加售货机强制密码记录　单条　
     * 
     * @param card
     * @return
     */
    public boolean addVendingPassword(VendingPasswordData vendingPassword) {
        String insertSql = "insert into VendingPassword(VP3_ID,VP3_M02_ID,VP3_Password,VP3_CreateUser,VP3_CreateTime,VP3_ModifyUser,VP3_ModifyTime,VP3_RowVersion)"
                + "values(?,?,?,?,?,?,?,?)";
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        SQLiteStatement stat = db.compileStatement(insertSql);
        stat.bindString(1, vendingPassword.getVp3Id());
        stat.bindString(2, vendingPassword.getVp3M02Id());
        stat.bindString(3, vendingPassword.getVp3Password());
        stat.bindString(4, vendingPassword.getVp3CreateUser());
        stat.bindString(5, vendingPassword.getVp3CreateTime());
        stat.bindString(6, vendingPassword.getVp3ModifyUser());
        stat.bindString(7, vendingPassword.getVp3ModifyTime());
        stat.bindString(8, vendingPassword.getVp3RowVersion());
        long i = stat.executeInsert();
        return i > 0;
    }

    /**
     * 批量售货机强制密码数据,用于同步数据
     * 
     * @param list
     */
    public boolean batchAddVendingPassword(List<VendingPasswordData> list) {
        boolean flag = false;
        String insertSql = "insert into VendingPassword(VP3_ID,VP3_M02_ID,VP3_Password,VP3_CreateUser,VP3_CreateTime,VP3_ModifyUser,VP3_ModifyTime,VP3_RowVersion)"
                + "values(?,?,?,?,?,?,?,?)";
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        try {
            // 开启事务
            db.beginTransaction();
            for (VendingPasswordData vendingPassword : list) {
                SQLiteStatement stat = db.compileStatement(insertSql);
                stat.bindString(1, vendingPassword.getVp3Id());
                stat.bindString(2, vendingPassword.getVp3M02Id());
                stat.bindString(3, vendingPassword.getVp3Password());
                stat.bindString(4, vendingPassword.getVp3CreateUser());
                stat.bindString(5, vendingPassword.getVp3CreateTime());
                stat.bindString(6, vendingPassword.getVp3ModifyUser());
                stat.bindString(7, vendingPassword.getVp3ModifyTime());
                stat.bindString(8, vendingPassword.getVp3RowVersion());
                stat.executeInsert();
            }
            // 数据插入成功，设置事物成功标志
            db.setTransactionSuccessful();
            // 保存数据
            db.endTransaction();
            flag = true;
        } catch (SQLException e) {
            // 结束事物，在这里没有设置成功标志，结束后不保存
            ZillionLog.e(this.getClass().getName(),e.getMessage(),e);
            db.endTransaction();
            e.printStackTrace();
        }
        return flag;
    }

    public Boolean updateVendingPassword(VendingPasswordData vendingPassword) {
        String updateSql = "UPDATE VendingPassword SET VP3_Password=? WHERE VP3_ID = ? ";
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        SQLiteStatement stat = db.compileStatement(updateSql);
        stat.bindString(1, vendingPassword.getVp3Password());
        stat.bindString(2, vendingPassword.getVp3Id());
        long i = stat.executeUpdateDelete();
        return i > 0;
    }

    public Boolean batchUpdateVendingPassword(List<VendingPasswordData> list) {
        boolean flag = false;
        String updateSql = "UPDATE VendingPassword SET VP3_Password=? WHERE VP3_ID = ? ";
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        try {
            // 开启事务
            db.beginTransaction();
            for (VendingPasswordData vendingPassword : list) {
                SQLiteStatement stat = db.compileStatement(updateSql);
                stat.bindString(1, vendingPassword.getVp3Password());
                stat.bindString(2, vendingPassword.getVp3Id());
                stat.executeUpdateDelete();
            }
            // 数据插入成功，设置事物成功标志
            db.setTransactionSuccessful();
            // 保存数据
            db.endTransaction();
            flag = true;
        } catch (SQLException e) {
            // 结束事物，在这里没有设置成功标志，结束后不保存
            ZillionLog.e(this.getClass().getName(),e.getMessage(),e);
            db.endTransaction();
            e.printStackTrace();
        }
        return flag;
    }

    public boolean deleteAll() {
        boolean flag = false;
        String deleteSql = "DELETE FROM VendingPassword";
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
            ZillionLog.e(this.getClass().getName(),e.getMessage(),e);
            db.endTransaction();
            e.printStackTrace();
        }
        return flag;
    }
}
