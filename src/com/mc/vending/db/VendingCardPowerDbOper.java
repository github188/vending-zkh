package com.mc.vending.db;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.mc.vending.data.VendingCardPowerData;

/**
 * 售货机卡/密码权限 操作类
 * 
 * @Filename: VendingCardPowerDbOper.java
 * @Version: 1.0
 * @Author: fenghu
 * @Email: fenghu@mightcloud.com
 *
 */
public class VendingCardPowerDbOper {

    public List<VendingCardPowerData> findAll() {
        List<VendingCardPowerData> list = new ArrayList<VendingCardPowerData>();
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        Cursor c = db.rawQuery("SELECT * FROM VendingCardPower", null);
        while (c.moveToNext()) {
            VendingCardPowerData vendingCardPower = new VendingCardPowerData();
            vendingCardPower.setVc2Id(c.getString(c.getColumnIndex("VC2_ID")));
            vendingCardPower.setVc2M02Id(c.getString(c.getColumnIndex("VC2_M02_ID")));
            vendingCardPower.setVc2Cu1Id(c.getString(c.getColumnIndex("VC2_CU1_ID")));
            vendingCardPower.setVc2Vd1Id(c.getString(c.getColumnIndex("VC2_VD1_ID")));
            vendingCardPower.setVc2Cd1Id(c.getString(c.getColumnIndex("VC2_CD1_ID")));
            vendingCardPower.setVc2CreateUser(c.getString(c.getColumnIndex("VC2_CreateUser")));
            vendingCardPower.setVc2CreateTime(c.getString(c.getColumnIndex("VC2_CreateTime")));
            vendingCardPower.setVc2ModifyUser(c.getString(c.getColumnIndex("VC2_ModifyUser")));
            vendingCardPower.setVc2ModifyTime(c.getString(c.getColumnIndex("VC2_ModifyTime")));
            vendingCardPower.setVc2RowVersion(c.getString(c.getColumnIndex("VC2_RowVersion")));
            list.add(vendingCardPower);
        }

        return list;
    }

    /**
     * 根据主键取
     * 
     * @param vc2Id
     * @return
     */
    public VendingCardPowerData findCardPower(String vc2Id) {
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        Cursor c = db.rawQuery("SELECT * FROM VendingCardPower where VC2_ID=?", new String[] { vc2Id });
        VendingCardPowerData vendingCardPower = new VendingCardPowerData();
        while (c.moveToNext()) {
            vendingCardPower.setVc2Id(c.getString(c.getColumnIndex("VC2_ID")));
            vendingCardPower.setVc2M02Id(c.getString(c.getColumnIndex("VC2_M02_ID")));
            vendingCardPower.setVc2Cu1Id(c.getString(c.getColumnIndex("VC2_CU1_ID")));
            vendingCardPower.setVc2Vd1Id(c.getString(c.getColumnIndex("VC2_VD1_ID")));
            vendingCardPower.setVc2Cd1Id(c.getString(c.getColumnIndex("VC2_CD1_ID")));
            vendingCardPower.setVc2CreateUser(c.getString(c.getColumnIndex("VC2_CreateUser")));
            vendingCardPower.setVc2CreateTime(c.getString(c.getColumnIndex("VC2_CreateTime")));
            vendingCardPower.setVc2ModifyUser(c.getString(c.getColumnIndex("VC2_ModifyUser")));
            vendingCardPower.setVc2ModifyTime(c.getString(c.getColumnIndex("VC2_ModifyTime")));
            vendingCardPower.setVc2RowVersion(c.getString(c.getColumnIndex("VC2_RowVersion")));
            break;
        }

        return vendingCardPower;
    }

    /**
     * 根据　卡/密码.ID，客户员工表.客户ID，售货机ID查询售货机卡/密码权限表记录
     * 
     * @param cusId
     *            　客户员工表.客户ID
     * @param vendingId
     *            　售货机ID
     * @param cardId
     *            　卡/密码.ID
     * @return
     */
    public VendingCardPowerData getVendingCardPower(String cusId, String vendingId, String cardId) {
        VendingCardPowerData vendingCardPower = null;
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        Cursor c = db
                .rawQuery(
                        "SELECT VC2_ID,VC2_CU1_ID,VC2_VD1_ID,VC2_CD1_ID FROM VendingCardPower WHERE VC2_CU1_ID=? and VC2_VD1_ID=? and VC2_CD1_ID=? limit 1",
                        new String[] { cusId, vendingId, cardId });
        while (c.moveToNext()) {
            vendingCardPower = new VendingCardPowerData();
            vendingCardPower.setVc2Id(c.getString(c.getColumnIndex("VC2_ID")));
            vendingCardPower.setVc2Cu1Id(c.getString(c.getColumnIndex("VC2_CU1_ID")));
            vendingCardPower.setVc2Vd1Id(c.getString(c.getColumnIndex("VC2_VD1_ID")));
            vendingCardPower.setVc2Cd1Id(c.getString(c.getColumnIndex("VC2_CD1_ID")));
            break;
        }

        return vendingCardPower;
    }

    /**
     * 根据　卡/密码.ID，客户员工表.客户ID，售货机ID查询售货机卡/密码权限表记录
     * 
     * @param vendingId
     *            　售货机ID
     * @param cardId
     *            　卡/密码.ID
     * @return
     */
    public VendingCardPowerData getVendingCardPower(String vendingId, String cardId) {
        VendingCardPowerData vendingCardPower = null;
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        Cursor c = db
                .rawQuery(
                        "SELECT VC2_ID,VC2_CU1_ID,VC2_VD1_ID,VC2_CD1_ID FROM VendingCardPower WHERE VC2_VD1_ID=? and VC2_CD1_ID=? limit 1",
                        new String[] { vendingId, cardId });
        while (c.moveToNext()) {
            vendingCardPower = new VendingCardPowerData();
            vendingCardPower.setVc2Id(c.getString(c.getColumnIndex("VC2_ID")));
            vendingCardPower.setVc2Cu1Id(c.getString(c.getColumnIndex("VC2_CU1_ID")));
            vendingCardPower.setVc2Vd1Id(c.getString(c.getColumnIndex("VC2_VD1_ID")));
            vendingCardPower.setVc2Cd1Id(c.getString(c.getColumnIndex("VC2_CD1_ID")));
            break;
        }

        return vendingCardPower;
    }

    /**
     * 增加售货机卡/密码权限记录数据，单条增加
     * 
     * @param product
     * @return
     */
    public boolean addVendingCardPower(VendingCardPowerData vendingCardPower) {
        String insertSql = "insert into VendingCardPower(VC2_ID,VC2_M02_ID,VC2_CU1_ID,VC2_VD1_ID,VC2_CD1_ID,VC2_CreateUser,VC2_CreateTime,VC2_ModifyUser,VC2_ModifyTime,VC2_RowVersion)"
                + "values(?,?,?,?,?,?,?,?,?,?)";
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        SQLiteStatement stat = db.compileStatement(insertSql);
        stat.bindString(1, vendingCardPower.getVc2Id());
        stat.bindString(2, vendingCardPower.getVc2M02Id());
        stat.bindString(3, vendingCardPower.getVc2Cu1Id());
        stat.bindString(4, vendingCardPower.getVc2Vd1Id());
        stat.bindString(5, vendingCardPower.getVc2Cd1Id());
        stat.bindString(6, vendingCardPower.getVc2CreateUser());
        stat.bindString(7, vendingCardPower.getVc2CreateTime());
        stat.bindString(8, vendingCardPower.getVc2ModifyUser());
        stat.bindString(9, vendingCardPower.getVc2ModifyTime());
        stat.bindString(10, vendingCardPower.getVc2RowVersion());
        long i = stat.executeInsert();
        return i > 0;
    }

    /**
     * 批量增加售货机卡/密码权限记录数据
     * 
     * @param list
     */
    public boolean batchAddVendingCardPower(List<VendingCardPowerData> list) {
        boolean flag = false;
        String insertSql = "insert into VendingCardPower(VC2_ID,VC2_M02_ID,VC2_CU1_ID,VC2_VD1_ID,VC2_CD1_ID,VC2_CreateUser,VC2_CreateTime,VC2_ModifyUser,VC2_ModifyTime,VC2_RowVersion)"
                + "values(?,?,?,?,?,?,?,?,?,?)";
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        try {
            // 开启事务
            db.beginTransaction();
            for (VendingCardPowerData vendingCardPower : list) {
                SQLiteStatement stat = db.compileStatement(insertSql);
                stat.bindString(1, vendingCardPower.getVc2Id());
                stat.bindString(2, vendingCardPower.getVc2M02Id());
                stat.bindString(3, vendingCardPower.getVc2Cu1Id());
                stat.bindString(4, vendingCardPower.getVc2Vd1Id());
                stat.bindString(5, vendingCardPower.getVc2Cd1Id());
                stat.bindString(6, vendingCardPower.getVc2CreateUser());
                stat.bindString(7, vendingCardPower.getVc2CreateTime());
                stat.bindString(8, vendingCardPower.getVc2ModifyUser());
                stat.bindString(9, vendingCardPower.getVc2ModifyTime());
                stat.bindString(10, vendingCardPower.getVc2RowVersion());
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

    public boolean deleteAll() {
        boolean flag = false;
        String deleteSql = "DELETE FROM VendingCardPower";
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
