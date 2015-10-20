package com.mc.vending.db;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.mc.vending.data.VendingProLinkData;

/**
 * 售货机产品　操作类
 *                       
 * @Filename: VendingProLinkDbOper.java
 * @Version: 1.0
 * @Author: fenghu
 * @Email: fenghu@mightcloud.com
 *
 */
public class VendingProLinkDbOper {
    public List<VendingProLinkData> findAll() {
        List<VendingProLinkData> list = new ArrayList<VendingProLinkData>();
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        Cursor c = db.rawQuery("SELECT * FROM VendingProLink", null);
        while (c.moveToNext()) {
            VendingProLinkData vendingProLink = new VendingProLinkData();
            vendingProLink.setVp1Id(c.getString(c.getColumnIndex("VP1_ID")));
            vendingProLink.setVp1M02Id(c.getString(c.getColumnIndex("VP1_M02_ID")));
            vendingProLink.setVp1Vd1Id(c.getString(c.getColumnIndex("VP1_VD1_ID")));
            vendingProLink.setVp1Pd1Id(c.getString(c.getColumnIndex("VP1_PD1_ID")));
            vendingProLink.setVp1PromptValue(c.getInt(c.getColumnIndex("VP1_PromptValue")));
            vendingProLink.setVp1WarningValue(c.getInt(c.getColumnIndex("VP1_WarningValue")));
            vendingProLink.setVp1CreateUser(c.getString(c.getColumnIndex("VP1_CreateUser")));
            vendingProLink.setVp1CreateTime(c.getString(c.getColumnIndex("VP1_CreateTime")));
            vendingProLink.setVp1ModifyUser(c.getString(c.getColumnIndex("VP1_ModifyUser")));
            vendingProLink.setVp1ModifyTime(c.getString(c.getColumnIndex("VP1_ModifyTime")));
            vendingProLink.setVp1RowVersion(c.getString(c.getColumnIndex("VP1_RowVersion")));
            list.add(vendingProLink);
        }
        return list;
    }

    /**
     * 根据售货机ID与SKUID查询售货机产品记录
     * @param vendingId 售货机ID
     * @param SkuId SKUID
     * @return
     */
    public VendingProLinkData getVendingProLinkByVidAndSkuId(String vendingId, String skuId) {
        VendingProLinkData vendingProLink = null;
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        Cursor c = db
            .rawQuery(
                "SELECT VP1_ID,VP1_VD1_ID,VP1_PD1_ID FROM VendingProLink WHERE VP1_VD1_ID=? and VP1_PD1_ID=? limit 1",
                new String[] { vendingId, skuId });
        while (c.moveToNext()) {
            vendingProLink = new VendingProLinkData();
            vendingProLink.setVp1Id(c.getString(c.getColumnIndex("VP1_ID")));
            vendingProLink.setVp1Vd1Id(c.getString(c.getColumnIndex("VP1_VD1_ID")));
            vendingProLink.setVp1Pd1Id(c.getString(c.getColumnIndex("VP1_PD1_ID")));
            break;
        }
        return vendingProLink;
    }

    /**
     * 增加售货机产品数据　单条增加
     * @param vendingProLink
     * @return
     */
    public boolean addVendingProLink(VendingProLinkData vendingProLink) {
        String insertSql = "insert into VendingProLink(VP1_ID,VP1_M02_ID,VP1_VD1_ID,VP1_PD1_ID,VP1_PromptValue,VP1_WarningValue,VP1_CreateUser,VP1_CreateTime,VP1_ModifyUser,VP1_ModifyTime,VP1_RowVersion)"
                           + "values(?,?,?,?,?,?,?,?,?,?,?)";
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        SQLiteStatement stat = db.compileStatement(insertSql);
        stat.bindString(1, vendingProLink.getVp1Id());
        stat.bindString(2, vendingProLink.getVp1M02Id());
        stat.bindString(3, vendingProLink.getVp1Vd1Id());
        stat.bindString(4, vendingProLink.getVp1Pd1Id());
        stat.bindLong(5, vendingProLink.getVp1PromptValue());
        stat.bindLong(6, vendingProLink.getVp1WarningValue());
        stat.bindString(7, vendingProLink.getVp1CreateUser());
        stat.bindString(8, vendingProLink.getVp1CreateTime());
        stat.bindString(9, vendingProLink.getVp1ModifyUser());
        stat.bindString(10, vendingProLink.getVp1ModifyTime());
        stat.bindString(11, vendingProLink.getVp1RowVersion());
        long i = stat.executeInsert();
        return i > 0;
    }

    /**
     * 批量增加售货机产品数据,用于同步数据
     * @param list
     */
    public boolean batchAddVendingProLink(List<VendingProLinkData> list) {
        boolean flag = false;
        String insertSql = "insert into VendingProLink(VP1_ID,VP1_M02_ID,VP1_VD1_ID,VP1_PD1_ID,VP1_PromptValue,VP1_WarningValue,VP1_CreateUser,VP1_CreateTime,VP1_ModifyUser,VP1_ModifyTime,VP1_RowVersion)"
                           + "values(?,?,?,?,?,?,?,?,?,?,?)";
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        try {
            //开启事务
            db.beginTransaction();
            for (VendingProLinkData vendingProLink : list) {
                SQLiteStatement stat = db.compileStatement(insertSql);
                stat.bindString(1, vendingProLink.getVp1Id());
                stat.bindString(2, vendingProLink.getVp1M02Id());
                stat.bindString(3, vendingProLink.getVp1Vd1Id());
                stat.bindString(4, vendingProLink.getVp1Pd1Id());
                stat.bindLong(5, vendingProLink.getVp1PromptValue());
                stat.bindLong(6, vendingProLink.getVp1WarningValue());
                stat.bindString(7, vendingProLink.getVp1CreateUser());
                stat.bindString(8, vendingProLink.getVp1CreateTime());
                stat.bindString(9, vendingProLink.getVp1ModifyUser());
                stat.bindString(10, vendingProLink.getVp1ModifyTime());
                stat.bindString(11, vendingProLink.getVp1RowVersion());
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
        String deleteSql = "DELETE FROM VendingProLink";
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
