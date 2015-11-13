package com.mc.vending.db;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.mc.vending.data.StockTransactionData;
import com.mc.vending.tools.ZillionLog;

public class StockTransactionDbOper {

    /**
     * 根据单据类型，售货机ID,SKUID,售货机货道编号　统计领料交易数量
     * 
     * @param billType
     *            　单据类型
     * @param vendingId
     *            　售货机ID
     * @param skuId
     *            　SKUID
     * @param vendingChnCode
     *            　售货机货道编号
     * @param startDate
     *            查询时间
     * @return
     */
    public int getTransQtyCount(String billType, String vendingId, String skuId, String vendingChnCode,
            String startDate) {
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        Cursor c = db
                .rawQuery(
                        "SELECT TS1_TransQty FROM StockTransaction WHERE TS1_BillType=? and TS1_VD1_ID=? and TS1_PD1_ID=? and TS1_VC1_CODE=? and TS1_CreateTime>=?",
                        new String[] { billType, vendingId, skuId, vendingChnCode, startDate });
        int transQtyCount = 0;
        while (c.moveToNext()) {
            int transQty = c.getInt(c.getColumnIndex("TS1_TransQty"));
            transQtyCount += transQty;
        }
        return transQtyCount;
    }

    public void clearStockTransaction(String startDate) {
        String updateSql = "delete StockTransaction where TS1_UploadStatus='"
                + StockTransactionData.UPLOAD_LOAD + "' and substr(TS1_createtime,0,11) <= ?";
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();

        Date curr = new Date();
        try {
            // 开启事务
            db.beginTransaction();
            SQLiteStatement stat = db.compileStatement(updateSql);
            stat.bindString(1, startDate);
            int i = stat.executeUpdateDelete();
            // 数据插入成功，设置事物成功标志
            db.setTransactionSuccessful();
            // 保存数据
            db.endTransaction();
        } catch (SQLException e) {
            // 结束事物，在这里没有设置成功标志，结束后不保存
            ZillionLog.e(this.getClass().getName(),e.getMessage(),e);
            db.endTransaction();
            e.printStackTrace();
        }
    }

    /**
     * 根据单据类型，售货机ID,SKUID,售货机货道编号　统计领料交易数量 卡ID
     * 
     * @param billType
     * @param vendingId
     * @param skuId
     * @param vendingChnCode
     * @param startDate
     * @param cardId
     * @return
     */
    public int getTransQtyCount(String billType, String vendingId, String skuId, String vendingChnCode,
            String startDate, String cardId) {
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        // 删除vendingChnCode条件 wangy
        Cursor c = db.rawQuery(
                "SELECT TS1_TransQty FROM StockTransaction WHERE TS1_BillType=? and TS1_VD1_ID=? "
                        + "and TS1_PD1_ID=? and TS1_CreateTime>=? and TS1_CD1_ID =?", new String[] {
                        billType, vendingId, skuId, startDate, cardId });
        int transQtyCount = 0;
        while (c.moveToNext()) {
            int transQty = c.getInt(c.getColumnIndex("TS1_TransQty"));
            transQtyCount += transQty;
        }
        return transQtyCount;
    }

    /**
     * 根据单据类型，售货机ID,SKUID　统计领料交易数量
     * 
     * @param billType
     * @param vendingId
     * @param skuId
     * @param startDate
     * @return
     */
    public int getTransQtyCount(String billType, String vendingId, String skuId, String startDate) {
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        Cursor c = db
                .rawQuery(
                        "SELECT TS1_TransQty FROM StockTransaction WHERE TS1_BillType=? and TS1_VD1_ID=? and TS1_PD1_ID=? and TS1_CreateTime>=?",
                        new String[] { billType, vendingId, skuId, startDate });
        int transQtyCount = 0;
        while (c.moveToNext()) {
            int transQty = c.getInt(c.getColumnIndex("TS1_TransQty"));
            transQtyCount += transQty;
        }
        return transQtyCount;
    }

    /**
     * 
     * 根据单据类型，售货机ID,SKUID　统计领料交易数量 卡ID
     * 
     * @param billType
     * @param vendingId
     * @param skuId
     * @param startDate
     * @param cardId
     * @return
     */
    public int getTransQtyCountAddCardId(String billType, String vendingId, String skuId, String startDate,
            String cardId) {
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        Cursor c = db
                .rawQuery(
                        "SELECT TS1_TransQty FROM StockTransaction "
                                + "WHERE TS1_BillType=? and TS1_VD1_ID=? and TS1_PD1_ID=? and TS1_CreateTime>=? and TS1_CD1_ID =?",
                        new String[] { billType, vendingId, skuId, startDate, cardId });
        int transQtyCount = 0;
        while (c.moveToNext()) {
            int transQty = c.getInt(c.getColumnIndex("TS1_TransQty"));
            transQtyCount += transQty;
        }
        return transQtyCount;
    }

    /**
     * 根据“售货机ID、货道号、单据类型=借还“查询“库存交易记录”
     * 
     * @param vendingId
     *            售货机ID
     * @param vcCode
     *            货道号
     * @return
     */
    public StockTransactionData getVendingChnByCode(String vendingId, String vcCode, String billType) {
        StockTransactionData stockTransaction = null;
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        Cursor c = db
                .rawQuery(
                        "SELECT * FROM StockTransaction WHERE TS1_VD1_ID = ? and TS1_VC1_CODE = ? and TS1_BillType = ? ORDER by TS1_CreateTime desc limit 1",
                        new String[] { vendingId, vcCode, billType });
        while (c.moveToNext()) {
            stockTransaction = new StockTransactionData();
            stockTransaction.setTs1Id(c.getString(c.getColumnIndex("TS1_ID")));
            stockTransaction.setTs1M02Id(c.getString(c.getColumnIndex("TS1_M02_ID")));
            stockTransaction.setTs1BillType(c.getString(c.getColumnIndex("TS1_BillType")));
            stockTransaction.setTs1BillCode(c.getString(c.getColumnIndex("TS1_BillCode")));
            stockTransaction.setTs1Cd1Id(c.getString(c.getColumnIndex("TS1_CD1_ID")));
            stockTransaction.setTs1Vd1Id(c.getString(c.getColumnIndex("TS1_VD1_ID")));
            stockTransaction.setTs1Pd1Id(c.getString(c.getColumnIndex("TS1_PD1_ID")));
            stockTransaction.setTs1Vc1Code(c.getString(c.getColumnIndex("TS1_VC1_CODE")));
            stockTransaction.setTs1TransQty(c.getInt(c.getColumnIndex("TS1_TransQty")));
            stockTransaction.setTs1TransType(c.getString(c.getColumnIndex("TS1_TransType")));
            stockTransaction.setTs1Sp1Code(c.getString(c.getColumnIndex("TS1_SP1_CODE")));
            stockTransaction.setTs1Sp1Name(c.getString(c.getColumnIndex("TS1_SP1_Name")));
            stockTransaction.setTs1Sp1Name(c.getString(c.getColumnIndex("TS1_UploadStatus")));
            stockTransaction.setTs1CreateUser(c.getString(c.getColumnIndex("TS1_CreateUser")));
            stockTransaction.setTs1CreateTime(c.getString(c.getColumnIndex("TS1_CreateTime")));
            stockTransaction.setTs1ModifyUser(c.getString(c.getColumnIndex("TS1_ModifyUser")));
            stockTransaction.setTs1ModifyTime(c.getString(c.getColumnIndex("TS1_ModifyTime")));
            stockTransaction.setTs1RowVersion(c.getString(c.getColumnIndex("TS1_RowVersion")));
            break;
        }
        return stockTransaction;
    }

    /**
     * 增加库存交易记录
     * 
     * @param stockTransaction
     * @return
     */
    public boolean addStockTransaction(StockTransactionData stockTransaction) {
        String insertSql = "insert into StockTransaction(TS1_ID,TS1_M02_ID,TS1_BillType,TS1_BillCode,TS1_CD1_ID,TS1_VD1_ID,TS1_PD1_ID,TS1_VC1_CODE,TS1_TransQty,TS1_TransType,TS1_SP1_CODE,TS1_SP1_Name,TS1_UploadStatus,"
                + "TS1_CreateUser,TS1_CreateTime,TS1_ModifyUser,TS1_ModifyTime,TS1_RowVersion)"
                + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        SQLiteStatement stat = db.compileStatement(insertSql);
        stat.bindString(1, stockTransaction.getTs1Id());
        stat.bindString(2, stockTransaction.getTs1M02Id());
        stat.bindString(3, stockTransaction.getTs1BillType());
        stat.bindString(4, stockTransaction.getTs1BillCode());
        stat.bindString(5, stockTransaction.getTs1Cd1Id());
        stat.bindString(6, stockTransaction.getTs1Vd1Id());
        stat.bindString(7, stockTransaction.getTs1Pd1Id());
        stat.bindString(8, stockTransaction.getTs1Vc1Code());
        stat.bindLong(9, stockTransaction.getTs1TransQty());
        stat.bindString(10, stockTransaction.getTs1TransType());
        stat.bindString(11, stockTransaction.getTs1Sp1Code());
        stat.bindString(12, stockTransaction.getTs1Sp1Name());
        stat.bindString(13, stockTransaction.getTs1UploadStatus());
        stat.bindString(14, stockTransaction.getTs1CreateUser());
        stat.bindString(15, stockTransaction.getTs1CreateTime());
        stat.bindString(16, stockTransaction.getTs1ModifyUser());
        stat.bindString(17, stockTransaction.getTs1ModifyTime());
        stat.bindString(18, stockTransaction.getTs1RowVersion());
        long i = stat.executeInsert();
        return i > 0;
    }

    public Boolean batchAddStockTransaction(List<StockTransactionData> list) {
        boolean flag = false;
        String insertSql = "insert into StockTransaction(TS1_ID,TS1_M02_ID,TS1_BillType,TS1_BillCode,TS1_CD1_ID,TS1_VD1_ID,TS1_PD1_ID,TS1_VC1_CODE,TS1_TransQty,TS1_TransType,TS1_SP1_CODE,TS1_SP1_Name,TS1_UploadStatus,"
                + "TS1_CreateUser,TS1_CreateTime,TS1_ModifyUser,TS1_ModifyTime,TS1_RowVersion)"
                + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        try {
            // 开启事务
            db.beginTransaction();
            for (StockTransactionData stockTransaction : list) {
                SQLiteStatement stat = db.compileStatement(insertSql);
                stat.bindString(1, stockTransaction.getTs1Id());
                stat.bindString(2, stockTransaction.getTs1M02Id());
                stat.bindString(3, stockTransaction.getTs1BillType());
                stat.bindString(4, stockTransaction.getTs1BillCode());
                stat.bindString(5, stockTransaction.getTs1Cd1Id());
                stat.bindString(6, stockTransaction.getTs1Vd1Id());
                stat.bindString(7, stockTransaction.getTs1Pd1Id());
                stat.bindString(8, stockTransaction.getTs1Vc1Code());
                stat.bindLong(9, stockTransaction.getTs1TransQty());
                stat.bindString(10, stockTransaction.getTs1TransType());
                stat.bindString(11, stockTransaction.getTs1Sp1Code());
                stat.bindString(12, stockTransaction.getTs1Sp1Name());
                stat.bindString(13, stockTransaction.getTs1UploadStatus());
                stat.bindString(14, stockTransaction.getTs1CreateUser());
                stat.bindString(15, stockTransaction.getTs1CreateTime());
                stat.bindString(16, stockTransaction.getTs1ModifyUser());
                stat.bindString(17, stockTransaction.getTs1ModifyTime());
                stat.bindString(18, stockTransaction.getTs1RowVersion());
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

    public List<StockTransactionData> findStockTransactionDataToUpload() {

        List<StockTransactionData> list = new ArrayList<StockTransactionData>();
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        Cursor c = db.rawQuery("SELECT * FROM StockTransaction WHERE TS1_UploadStatus = ? limit 50",
                new String[] { StockTransactionData.UPLOAD_UNLOAD });
        while (c.moveToNext()) {
            StockTransactionData stockTransaction = new StockTransactionData();
            stockTransaction.setTs1Id(c.getString(c.getColumnIndex("TS1_ID")));
            stockTransaction.setTs1M02Id(c.getString(c.getColumnIndex("TS1_M02_ID")));
            stockTransaction.setTs1BillType(c.getString(c.getColumnIndex("TS1_BillType")));
            stockTransaction.setTs1BillCode(c.getString(c.getColumnIndex("TS1_BillCode")));
            stockTransaction.setTs1Cd1Id(c.getString(c.getColumnIndex("TS1_CD1_ID")));
            stockTransaction.setTs1Vd1Id(c.getString(c.getColumnIndex("TS1_VD1_ID")));
            stockTransaction.setTs1Pd1Id(c.getString(c.getColumnIndex("TS1_PD1_ID")));
            stockTransaction.setTs1Vc1Code(c.getString(c.getColumnIndex("TS1_VC1_CODE")));
            stockTransaction.setTs1TransQty(c.getInt(c.getColumnIndex("TS1_TransQty")));
            stockTransaction.setTs1TransType(c.getString(c.getColumnIndex("TS1_TransType")));
            stockTransaction.setTs1Sp1Code(c.getString(c.getColumnIndex("TS1_SP1_CODE")));
            stockTransaction.setTs1Sp1Name(c.getString(c.getColumnIndex("TS1_SP1_Name")));
            stockTransaction.setTs1UploadStatus(c.getString(c.getColumnIndex("TS1_UploadStatus")));
            stockTransaction.setTs1CreateUser(c.getString(c.getColumnIndex("TS1_CreateUser")));
            stockTransaction.setTs1CreateTime(c.getString(c.getColumnIndex("TS1_CreateTime")));
            stockTransaction.setTs1ModifyUser(c.getString(c.getColumnIndex("TS1_ModifyUser")));
            stockTransaction.setTs1ModifyTime(c.getString(c.getColumnIndex("TS1_ModifyTime")));
            stockTransaction.setTs1RowVersion(c.getString(c.getColumnIndex("TS1_RowVersion")));
            list.add(stockTransaction);
        }
        return list;
    }

    public boolean updateUploadStatusByTs1Id(String ts1Id) {
        boolean flag = false;
        String updateSql = "update StockTransaction set TS1_UploadStatus = ? where TS1_ID = ?";
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        try {
            // 开启事务
            db.beginTransaction();
            SQLiteStatement stat = db.compileStatement(updateSql);
            stat.bindString(1, StockTransactionData.UPLOAD_LOAD);
            stat.bindString(2, ts1Id);
            int i = stat.executeUpdateDelete();
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

    /**
     * 批量更新上传状态
     * 
     * @return
     */
    public boolean batchUpdateUploadStatus(List<StockTransactionData> stockTransactionList) {
        boolean flag = false;
        String updateSql = "update StockTransaction set TS1_UploadStatus = ? where TS1_ID = ?";
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        try {
            // 开启事务
            db.beginTransaction();

            for (StockTransactionData stockTransaction : stockTransactionList) {
                SQLiteStatement stat = db.compileStatement(updateSql);
                stat.bindString(1, StockTransactionData.UPLOAD_LOAD);
                stat.bindString(2, stockTransaction.getTs1Id());
                stat.executeUpdateDelete();
            }
            // 数据更新成功，设置事物成功标志
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
