package com.mc.vending.db;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.mc.vending.data.CusEmpCardPowerData;

/**
 * 客户员工卡/密码权限　操作类
 *                       
 * @Filename: CusEmpCardPowerDbOper.java
 * @Version: 1.0
 * @Author: fenghu
 * @Email: fenghu@mightcloud.com
 *
 */
public class CusEmpCardPowerDbOper {
    public List<CusEmpCardPowerData> findAll() {
        List<CusEmpCardPowerData> list = new ArrayList<CusEmpCardPowerData>();
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        Cursor c = db.rawQuery("SELECT * FROM CusEmpCardPower", null);
        while (c.moveToNext()) {
            CusEmpCardPowerData cusEmpCardPower = new CusEmpCardPowerData();
            cusEmpCardPower.setCe2Id(c.getString(c.getColumnIndex("CE2_ID")));
            cusEmpCardPower.setCe2M02Id(c.getString(c.getColumnIndex("CE2_M02_ID")));
            cusEmpCardPower.setCe2Ce1Id(c.getString(c.getColumnIndex("CE2_CE1_ID")));
            cusEmpCardPower.setCe2Cd1Id(c.getString(c.getColumnIndex("CE2_CD1_ID")));
            cusEmpCardPower.setCe2CreateUser(c.getString(c.getColumnIndex("CE2_CreateUser")));
            cusEmpCardPower.setCe2CreateTime(c.getString(c.getColumnIndex("CE2_CreateTime")));
            cusEmpCardPower.setCe2ModifyUser(c.getString(c.getColumnIndex("CE2_ModifyUser")));
            cusEmpCardPower.setCe2ModifyTime(c.getString(c.getColumnIndex("CE2_ModifyTime")));
            cusEmpCardPower.setCe2RowVersion(c.getString(c.getColumnIndex("CE2_RowVersion")));
            list.add(cusEmpCardPower);
        }
        return list;
    }

    /**
     * 根据卡ID查询客户员工卡/密码权限记录
     * @param cardId　卡ID
     * @return
     */
    public CusEmpCardPowerData getCusEmpCardPowerByCardId(String cardId) {
        CusEmpCardPowerData cusEmpCardPower = null;
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        Cursor c = db.rawQuery(
            "SELECT CE2_ID,CE2_CE1_ID,CE2_CD1_ID FROM CusEmpCardPower WHERE CE2_CD1_ID=? limit 1",
            new String[] { cardId });
        while (c.moveToNext()) {
            cusEmpCardPower = new CusEmpCardPowerData();
            cusEmpCardPower.setCe2Id(c.getString(c.getColumnIndex("CE2_ID")));
            cusEmpCardPower.setCe2Ce1Id(c.getString(c.getColumnIndex("CE2_CE1_ID")));
            cusEmpCardPower.setCe2Cd1Id(c.getString(c.getColumnIndex("CE2_CD1_ID")));
            break;
        }
        return cusEmpCardPower;
    }

    /**
     * 增加客户员工卡/密码权限记录　单条
     * @param cusEmpCardPower
     * @return
     */
    public boolean addCusEmpCardPower(CusEmpCardPowerData cusEmpCardPower) {
        String insertSql = "insert into CusEmpCardPower(CE2_ID,CE2_M02_ID,CE2_CE1_ID,CE2_CD1_ID,CE2_CreateUser,CE2_CreateTime,CE2_ModifyUser,CE2_ModifyTime,CE2_RowVersion)"
                           + "values(?,?,?,?,?,?,?,?,?)";
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        SQLiteStatement stat = db.compileStatement(insertSql);
        stat.bindString(1, cusEmpCardPower.getCe2Id());
        stat.bindString(2, cusEmpCardPower.getCe2M02Id());
        stat.bindString(3, cusEmpCardPower.getCe2Ce1Id());
        stat.bindString(4, cusEmpCardPower.getCe2Cd1Id());
        stat.bindString(5, cusEmpCardPower.getCe2CreateUser());
        stat.bindString(6, cusEmpCardPower.getCe2CreateTime());
        stat.bindString(7, cusEmpCardPower.getCe2ModifyUser());
        stat.bindString(8, cusEmpCardPower.getCe2ModifyTime());
        stat.bindString(9, cusEmpCardPower.getCe2RowVersion());
        long i = stat.executeInsert();
        return i > 0;
    }

    /**
     * 批量增加客户员工卡/密码权限记录
     * @param list
     */
    public boolean batchAddCusEmpCardPower(List<CusEmpCardPowerData> list) {
        boolean flag = false;
        String insertSql = "insert into CusEmpCardPower(CE2_ID,CE2_M02_ID,CE2_CE1_ID,CE2_CD1_ID,CE2_CreateUser,CE2_CreateTime,CE2_ModifyUser,CE2_ModifyTime,CE2_RowVersion)"
                           + "values(?,?,?,?,?,?,?,?,?)";
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        try {
            //开启事务
            db.beginTransaction();
            for (CusEmpCardPowerData cusEmpCardPower : list) {
                SQLiteStatement stat = db.compileStatement(insertSql);
                stat.bindString(1, cusEmpCardPower.getCe2Id());
                stat.bindString(2, cusEmpCardPower.getCe2M02Id());
                stat.bindString(3, cusEmpCardPower.getCe2Ce1Id());
                stat.bindString(4, cusEmpCardPower.getCe2Cd1Id());
                stat.bindString(5, cusEmpCardPower.getCe2CreateUser());
                stat.bindString(6, cusEmpCardPower.getCe2CreateTime());
                stat.bindString(7, cusEmpCardPower.getCe2ModifyUser());
                stat.bindString(8, cusEmpCardPower.getCe2ModifyTime());
                stat.bindString(9, cusEmpCardPower.getCe2RowVersion());
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
        String deleteSql = "DELETE FROM CusEmpCardPower";
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
