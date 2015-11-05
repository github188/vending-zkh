package com.mc.vending.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.mc.vending.data.ProductPictureData;
import com.mc.vending.tools.ZillionLog;

/**
 * 产品图片操作类
 * 
 * @Filename: ProductPictureDbOper.java
 * @Version: 1.0
 * @Author: fenghu
 * @Email: fenghu@mightcloud.com
 *
 */
public class ProductPictureDbOper {

    public List<ProductPictureData> findAll() {
        List<ProductPictureData> list = new ArrayList<ProductPictureData>();
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        Cursor c = db.rawQuery("SELECT * FROM ProductPicture", null);
        while (c.moveToNext()) {
            ProductPictureData pp = new ProductPictureData();
            pp.setPp1Id(c.getString(c.getColumnIndex("PP1_ID")));
            pp.setPp1M02Id(c.getString(c.getColumnIndex("PP1_M02_ID")));
            pp.setPp1Pd1Id(c.getString(c.getColumnIndex("PP1_PD1_ID")));
            pp.setPp1FilePath(c.getString(c.getColumnIndex("PP1_FilePath")));
            pp.setPp1CreateUser(c.getString(c.getColumnIndex("PP1_CreateUser")));
            pp.setPp1CreateTime(c.getString(c.getColumnIndex("PP1_CreateTime")));
            pp.setPp1ModifyUser(c.getString(c.getColumnIndex("PP1_ModifyUser")));
            pp.setPp1ModifyTime(c.getString(c.getColumnIndex("PP1_ModifyTime")));
            pp.setPp1RowVersion(c.getString(c.getColumnIndex("PP1_RowVersion")));
            list.add(pp);
        }
        return list;
    }

    public Map<String, String> findAllMap() {
        Map<String, String> map = new HashMap<String, String>();
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        Cursor c = db.rawQuery("SELECT PP1_ID,PP1_PD1_ID FROM ProductPicture", null);
        while (c.moveToNext()) {
            String ppId = c.getString(c.getColumnIndex("PP1_ID"));
            String skuId = c.getString(c.getColumnIndex("PP1_PD1_ID"));
            map.put(ppId, skuId);
        }
        return map;
    }

    /**
     * 根据SKUID查询产品图片表，获取图片信息
     * 
     * @param sku
     * @return
     */
    public ProductPictureData getProductPictureBySku(String sku) {
        ProductPictureData pp = null;
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        Cursor c = db.rawQuery(
                "SELECT PP1_ID,PP1_PD1_ID,PP1_FilePath FROM ProductPicture WHERE PP1_PD1_ID=? limit 1",
                new String[] { sku });
        while (c.moveToNext()) {
            pp = new ProductPictureData();
            pp.setPp1Id(c.getString(c.getColumnIndex("PP1_ID")));
            pp.setPp1Pd1Id(c.getString(c.getColumnIndex("PP1_PD1_ID")));
            pp.setPp1FilePath(c.getString(c.getColumnIndex("PP1_FilePath")));
            break;
        }
        return pp;
    }

    /**
     * 增加产品图片记录数据，单条增加
     * 
     * @param product
     * @return
     */
    public boolean addProductPicture(ProductPictureData productPicture) {
        String insertSql = "insert into ProductPicture(PP1_ID,PP1_M02_ID,PP1_PD1_ID,PP1_FilePath,PP1_CreateUser,PP1_CreateTime,PP1_ModifyUser,PP1_ModifyTime,PP1_RowVersion)"
                + "values(?,?,?,?,?,?,?,?,?)";
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        SQLiteStatement stat = db.compileStatement(insertSql);
        stat.bindString(1, productPicture.getPp1Id());
        stat.bindString(2, productPicture.getPp1M02Id());
        stat.bindString(3, productPicture.getPp1Pd1Id());
        stat.bindString(4, productPicture.getPp1FilePath());
        stat.bindString(5, productPicture.getPp1CreateUser());
        stat.bindString(6, productPicture.getPp1CreateTime());
        stat.bindString(7, productPicture.getPp1ModifyUser());
        stat.bindString(8, productPicture.getPp1ModifyTime());
        stat.bindString(9, productPicture.getPp1RowVersion());
        long i = stat.executeInsert();
        return i > 0;
    }

    /**
     * 批量增加产品图片记录数据
     * 
     * @param list
     */
    public boolean batchAddProductPicture(List<ProductPictureData> list) {
        boolean flag = false;
        String insertSql = "insert into ProductPicture(PP1_ID,PP1_M02_ID,PP1_PD1_ID,PP1_FilePath,PP1_CreateUser,PP1_CreateTime,PP1_ModifyUser,PP1_ModifyTime,PP1_RowVersion)"
                + "values(?,?,?,?,?,?,?,?,?)";
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        try {
            //开启事务
            db.beginTransaction();
            for (ProductPictureData productPicture : list) {
                SQLiteStatement stat = db.compileStatement(insertSql);
                stat.bindString(1, productPicture.getPp1Id());
                stat.bindString(2, productPicture.getPp1M02Id());
                stat.bindString(3, productPicture.getPp1Pd1Id());
                stat.bindString(4, productPicture.getPp1FilePath());
                stat.bindString(5, productPicture.getPp1CreateUser());
                stat.bindString(6, productPicture.getPp1CreateTime());
                stat.bindString(7, productPicture.getPp1ModifyUser());
                stat.bindString(8, productPicture.getPp1ModifyTime());
                stat.bindString(9, productPicture.getPp1RowVersion());
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
     * 批量更新产品图片记录数据
     * 
     * @param list
     */
    public boolean batchUpdateProductPicture(List<ProductPictureData> list) {
        boolean flag = false;
        String updateSql = "UPDATE ProductPicture SET PP1_M02_ID=?,PP1_PD1_ID=?,PP1_FilePath=?,PP1_CreateUser=?,PP1_CreateTime=?,PP1_ModifyUser=?,PP1_ModifyTime=?,PP1_RowVersion=?"
                + " WHERE PP1_ID=?";
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        try {
            //开启事务
            db.beginTransaction();
            for (ProductPictureData productPicture : list) {
                SQLiteStatement stat = db.compileStatement(updateSql);
                stat.bindString(1, productPicture.getPp1M02Id());
                stat.bindString(2, productPicture.getPp1Pd1Id());
                stat.bindString(3, productPicture.getPp1FilePath());
                stat.bindString(4, productPicture.getPp1CreateUser());
                stat.bindString(5, productPicture.getPp1CreateTime());
                stat.bindString(6, productPicture.getPp1ModifyUser());
                stat.bindString(7, productPicture.getPp1ModifyTime());
                stat.bindString(8, productPicture.getPp1RowVersion());
                stat.bindString(9, productPicture.getPp1Id());
                stat.executeUpdateDelete();
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
        String deleteSql = "DELETE FROM ProductPicture";
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
            ZillionLog.e(this.getClass().getName(),e.getMessage(),e);
            db.endTransaction();
            e.printStackTrace();
        }
        return flag;
    }
}
