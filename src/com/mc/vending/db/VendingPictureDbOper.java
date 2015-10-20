package com.mc.vending.db;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.mc.vending.data.VendingPictureData;

/**
 * 售货机　操作类
 *                       
 * @Filename: VendingDbOper.java
 * @Version: 1.0
 * @Author: fenghu
 * @Email: fenghu@mightcloud.com
 *
 */
public class VendingPictureDbOper {

    /**
     * 查询售货机待机图片列表
     * @return
     */
    public List<VendingPictureData> findVendingPicture() {
        List<VendingPictureData> list = new ArrayList<VendingPictureData>();
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        Cursor c = db
            .rawQuery(
                "SELECT VP2_ID,VP2_Seq,VP2_FilePath,VP2_RunTime FROM VendingPicture WHERE VP2_Type=1 ORDER BY VP2_Seq",
                null);
        while (c.moveToNext()) {
            VendingPictureData vendingPicture = new VendingPictureData();
            vendingPicture.setVp2Id(c.getString(c.getColumnIndex("VP2_ID")));
            vendingPicture.setVp2Seq(c.getString(c.getColumnIndex("VP2_Seq")));
            vendingPicture.setVp2FilePath(c.getString(c.getColumnIndex("VP2_FilePath")));
            vendingPicture.setVp2RunTime(c.getInt(c.getColumnIndex("VP2_RunTime")));
            list.add(vendingPicture);
        }
        return list;
    }

    /**
     * 查询售货机首页默认图片
     * @return
     */
    public VendingPictureData getDefaultVendingPicture() {
        VendingPictureData vendingPicture = null;
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        Cursor c = db
            .rawQuery(
                "SELECT VP2_ID,VP2_Seq,VP2_FilePath,VP2_RunTime FROM VendingPicture WHERE VP2_Type=0 ORDER BY VP2_Seq limit 1",
                null);
        while (c.moveToNext()) {
            vendingPicture = new VendingPictureData();
            vendingPicture.setVp2Id(c.getString(c.getColumnIndex("VP2_ID")));
            vendingPicture.setVp2Seq(c.getString(c.getColumnIndex("VP2_Seq")));
            vendingPicture.setVp2FilePath(c.getString(c.getColumnIndex("VP2_FilePath")));
            vendingPicture.setVp2RunTime(c.getInt(c.getColumnIndex("VP2_RunTime")));
            break;
        }
        return vendingPicture;
    }

    /**
     * 增加售货机待机图片
     * @param vending
     * @return
     */
    public boolean addVendingPicture(VendingPictureData vendingPicture) {
        String insertSql = "insert into VendingPicture(VP2_ID,VP2_M02_ID,VP2_Seq,VP2_FilePath,VP2_RunTime,VP2_Type,"
                           + "VP2_CreateUser,VP2_CreateTime,VP2_ModifyUser,VP2_ModifyTime,VP2_RowVersion)"
                           + " values(?,?,?,?,?,?,?,?,?,?,?)";
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        SQLiteStatement stat = db.compileStatement(insertSql);
        stat.bindString(1, vendingPicture.getVp2Id());
        stat.bindString(2, vendingPicture.getVp2M02Id());
        stat.bindString(3, vendingPicture.getVp2Seq());
        stat.bindString(4, vendingPicture.getVp2FilePath());
        stat.bindLong(5, vendingPicture.getVp2RunTime());
        stat.bindString(6, vendingPicture.getVp2Type());
        stat.bindString(7, vendingPicture.getVp2CreateUser());
        stat.bindString(8, vendingPicture.getVp2CreateTime());
        stat.bindString(9, vendingPicture.getVp2ModifyUser());
        stat.bindString(10, vendingPicture.getVp2ModifyTime());
        stat.bindString(11, vendingPicture.getVp2RowVersion());
        long i = stat.executeInsert();

        return i > 0;
    }

    /**
     * 批量增加待机图片记录数据
     * @param list
     */
    public boolean batchAddVendingPicture(List<VendingPictureData> list) {
        boolean flag = false;
        String insertSql = "insert into VendingPicture(VP2_ID,VP2_M02_ID,VP2_Seq,VP2_FilePath,VP2_RunTime,VP2_Type,"
                           + "VP2_CreateUser,VP2_CreateTime,VP2_ModifyUser,VP2_ModifyTime,VP2_RowVersion)"
                           + " values(?,?,?,?,?,?,?,?,?,?,?)";
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        try {
            //开启事务
            db.beginTransaction();
            for (VendingPictureData vendingPicture : list) {
                SQLiteStatement stat = db.compileStatement(insertSql);
                stat.bindString(1, vendingPicture.getVp2Id());
                stat.bindString(2, vendingPicture.getVp2M02Id());
                stat.bindString(3, vendingPicture.getVp2Seq());
                stat.bindString(4, vendingPicture.getVp2FilePath());
                stat.bindLong(5, vendingPicture.getVp2RunTime());
                stat.bindString(6, vendingPicture.getVp2Type());
                stat.bindString(7, vendingPicture.getVp2CreateUser());
                stat.bindString(8, vendingPicture.getVp2CreateTime());
                stat.bindString(9, vendingPicture.getVp2ModifyUser());
                stat.bindString(10, vendingPicture.getVp2ModifyTime());
                stat.bindString(11, vendingPicture.getVp2RowVersion());
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
     * 删除整表数据
     * @return
     */
    public boolean deleteAll() {
        boolean flag = false;
        String deleteSql = "DELETE FROM VendingPicture";
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
