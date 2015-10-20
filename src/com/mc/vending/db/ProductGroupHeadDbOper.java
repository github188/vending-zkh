package com.mc.vending.db;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.mc.vending.data.ProductGroupDetailData;
import com.mc.vending.data.ProductGroupHeadData;

/**
 * 产品组合主表 操作类
 *                       
 * @Filename: ProductGroupHeadDbOper.java
 * @Version: 1.0
 * @Author: fenghu
 * @Email: fenghu@mightcloud.com
 *
 */
public class ProductGroupHeadDbOper {

    /**
     * 查询所有产品组合主表记录
     * @return
     */
    public List<ProductGroupHeadData> findAllProductGroupHead() {
        List<ProductGroupHeadData> list = new ArrayList<ProductGroupHeadData>();
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        Cursor c = db.rawQuery(
            "SELECT PG1_ID,PG1_CU1_ID,PG1_CODE,PG1_Name FROM ProductGroupHead ORDER BY PG1_CODE",
            null);
        while (c.moveToNext()) {
            ProductGroupHeadData productGroupHead = new ProductGroupHeadData();
            productGroupHead.setPg1Id(c.getString(c.getColumnIndex("PG1_ID")));
            productGroupHead.setPg1Cu1Id(c.getString(c.getColumnIndex("PG1_CU1_ID")));
            productGroupHead.setPg1Code(c.getString(c.getColumnIndex("PG1_CODE")));
            productGroupHead.setPg1Name(c.getString(c.getColumnIndex("PG1_Name")));
            list.add(productGroupHead);
        }
        return list;
    }

    /**
     * 根据产品组合编号查询产品组合主表记录
     * @param pgCode 产品组合编号
     * @return
     */
    public ProductGroupHeadData getProductGroupHeadByCode(String pgCode) {
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        Cursor c = db
            .rawQuery(
                "SELECT PG1_ID,PG1_CU1_ID,PG1_CODE,PG1_Name FROM ProductGroupHead WHERE PG1_CODE=? limit 1",
                new String[] { pgCode });
        ProductGroupHeadData productGroupHead = null;
        while (c.moveToNext()) {
            productGroupHead = new ProductGroupHeadData();
            productGroupHead.setPg1Id(c.getString(c.getColumnIndex("PG1_ID")));
            productGroupHead.setPg1Cu1Id(c.getString(c.getColumnIndex("PG1_CU1_ID")));
            productGroupHead.setPg1Code(c.getString(c.getColumnIndex("PG1_CODE")));
            productGroupHead.setPg1Name(c.getString(c.getColumnIndex("PG1_Name")));
            break;
        }
        return productGroupHead;
    }

    /**
     * 增加产品组合主表记录　单条　
     * @param productGroupHead
     * @return
     */
    public boolean addProductGroupHead(ProductGroupHeadData productGroupHead) {
        String insertSql = "insert into ProductGroupHead(PG1_ID,PG1_M02_ID,PG1_CU1_ID,PG1_CODE,PG1_Name,PG1_CreateUser,PG1_CreateTime,PG1_ModifyUser,PG1_ModifyTime,PG1_RowVersion)"
                           + "values(?,?,?,?,?,?,?,?,?,?)";
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        SQLiteStatement stat = db.compileStatement(insertSql);
        stat.bindString(1, productGroupHead.getPg1Id());
        stat.bindString(2, productGroupHead.getPg1M02Id());
        stat.bindString(3, productGroupHead.getPg1Cu1Id());
        stat.bindString(4, productGroupHead.getPg1Code());
        stat.bindString(5, productGroupHead.getPg1Name());
        stat.bindString(6, productGroupHead.getPg1CreateUser());
        stat.bindString(7, productGroupHead.getPg1CreateTime());
        stat.bindString(8, productGroupHead.getPg1ModifyUser());
        stat.bindString(9, productGroupHead.getPg1ModifyTime());
        stat.bindString(10, productGroupHead.getPg1RowVersion());
        long i = stat.executeInsert();
        return i > 0;
    }

    /**
     * 批量增加产品组合主表记录数据
     * @param list
     */
    public boolean batchAddProductGroupHead(List<ProductGroupHeadData> list) {
        boolean flag = false;
        String headSql = "insert into ProductGroupHead(PG1_ID,PG1_M02_ID,PG1_CU1_ID,PG1_CODE,PG1_Name,PG1_CreateUser,PG1_CreateTime,PG1_ModifyUser,PG1_ModifyTime,PG1_RowVersion)"
                         + "values(?,?,?,?,?,?,?,?,?,?)";
        String detailSql = "insert into ProductGroupDetail(PG2_ID,PG2_M02_ID,PG2_PG1_ID,PG2_PD1_ID,PG2_GroupQty,PG2_CreateUser,PG2_CreateTime,PG2_ModifyUser,PG2_ModifyTime,PG2_RowVersion)"
                           + "values(?,?,?,?,?,?,?,?,?,?)";
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        try {
            //开启事务
            db.beginTransaction();
            for (ProductGroupHeadData productGroupHead : list) {
                SQLiteStatement stat = db.compileStatement(headSql);
                stat.bindString(1, productGroupHead.getPg1Id());
                stat.bindString(2, productGroupHead.getPg1M02Id());
                stat.bindString(3, productGroupHead.getPg1Cu1Id());
                stat.bindString(4, productGroupHead.getPg1Code());
                stat.bindString(5, productGroupHead.getPg1Name());
                stat.bindString(6, productGroupHead.getPg1CreateUser());
                stat.bindString(7, productGroupHead.getPg1CreateTime());
                stat.bindString(8, productGroupHead.getPg1ModifyUser());
                stat.bindString(9, productGroupHead.getPg1ModifyTime());
                stat.bindString(10, productGroupHead.getPg1RowVersion());
                stat.executeInsert();

                List<ProductGroupDetailData> children = productGroupHead.getChildren();
                if (children != null) {
                    for (ProductGroupDetailData productGroupDetail : children) {
                        SQLiteStatement detail_stat = db.compileStatement(detailSql);
                        detail_stat.bindString(1, productGroupDetail.getPg2Id());
                        detail_stat.bindString(2, productGroupDetail.getPg2M02Id());
                        detail_stat.bindString(3, productGroupDetail.getPg2Pg1Id());
                        detail_stat.bindString(4, productGroupDetail.getPg2Pd1Id());
                        detail_stat.bindLong(5, productGroupDetail.getPg2GroupQty());
                        detail_stat.bindString(6, productGroupDetail.getPg2CreateUser());
                        detail_stat.bindString(7, productGroupDetail.getPg2CreateTime());
                        detail_stat.bindString(8, productGroupDetail.getPg2ModifyUser());
                        detail_stat.bindString(9, productGroupDetail.getPg2ModifyTime());
                        detail_stat.bindString(10, productGroupDetail.getPg2RowVersion());
                        detail_stat.executeInsert();
                    }
                }
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
        String deleteHeadSql = "DELETE FROM ProductGroupHead";
        String deleteDetailSql = "DELETE FROM ProductGroupDetail";
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        try {
            //开启事务
            db.beginTransaction();
            SQLiteStatement detail_stat = db.compileStatement(deleteDetailSql);
            detail_stat.executeUpdateDelete();
            SQLiteStatement head_stat = db.compileStatement(deleteHeadSql);
            head_stat.executeUpdateDelete();
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
