package com.mc.vending.db;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.mc.vending.data.ReplenishmentDetailData;
import com.mc.vending.tools.ZillionLog;

/**
 * 补货单从表　操作类
 * 
 * @Filename: ReplenishmentDetailDbOper.java
 * @Version: 1.0
 * @Author: fenghu
 * @Email: fenghu@mightcloud.com
 *
 */
public class ReplenishmentDetailDbOper {

    public List<ReplenishmentDetailData> findAll() {
        List<ReplenishmentDetailData> list = new ArrayList<ReplenishmentDetailData>();
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        Cursor c = db.rawQuery("SELECT * FROM ReplenishmentDetail", null);
        while (c.moveToNext()) {
            ReplenishmentDetailData replenishmentDetail = new ReplenishmentDetailData();
            replenishmentDetail.setRh2Id(c.getString(c.getColumnIndex("RH2_ID")));
            replenishmentDetail.setRh2M02Id(c.getString(c.getColumnIndex("RH2_M02_ID")));
            replenishmentDetail.setRh2Rh1Id(c.getString(c.getColumnIndex("RH2_RH1_ID")));
            replenishmentDetail.setRh2Vc1Code(c.getString(c.getColumnIndex("RH2_VC1_CODE")));
            replenishmentDetail.setRh2Pd1Id(c.getString(c.getColumnIndex("RH2_PD1_ID")));
            replenishmentDetail.setRh2SaleType(c.getString(c.getColumnIndex("RH2_SaleType")));
            replenishmentDetail.setRh2Sp1Id(c.getString(c.getColumnIndex("RH2_SP1_ID")));
            replenishmentDetail.setRh2ActualQty(c.getInt(c.getColumnIndex("RH2_ActualQty")));
            replenishmentDetail.setRh2DifferentiaQty(c.getInt(c.getColumnIndex("RH2_DifferentiaQty")));
            replenishmentDetail.setRh2Rp1Id(c.getString(c.getColumnIndex("RH2_RP1_ID")));
            replenishmentDetail.setRh2UploadStatus(c.getString(c.getColumnIndex("RH2_UploadStatus")));
            replenishmentDetail.setRh2CreateUser(c.getString(c.getColumnIndex("RH2_CreateUser")));
            replenishmentDetail.setRh2CreateTime(c.getString(c.getColumnIndex("RH2_CreateTime")));
            replenishmentDetail.setRh2ModifyUser(c.getString(c.getColumnIndex("RH2_ModifyUser")));
            replenishmentDetail.setRh2ModifyTime(c.getString(c.getColumnIndex("RH2_ModifyTime")));
            replenishmentDetail.setRh2RowVersion(c.getString(c.getColumnIndex("RH2_RowVersion")));
            list.add(replenishmentDetail);
        }
        return list;
    }

    /**
     * 根据补货单主表ID查询补货单从表记录列表
     * 
     * @param rh1Id
     * @return
     */
    public List<ReplenishmentDetailData> findReplenishmentDetailkByRh1Id(String rh1Id) {
        List<ReplenishmentDetailData> list = new ArrayList<ReplenishmentDetailData>();
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        String sql = "SELECT * FROM ReplenishmentDetail WHERE RH2_RH1_ID=? ORDER BY RH2_VC1_CODE ASC ";
        Cursor c = db.rawQuery(sql, new String[] { rh1Id });
        while (c.moveToNext()) {
            ReplenishmentDetailData replenishmentDetail = new ReplenishmentDetailData();
            replenishmentDetail.setRh2Id(c.getString(c.getColumnIndex("RH2_ID")));
            replenishmentDetail.setRh2M02Id(c.getString(c.getColumnIndex("RH2_M02_ID")));
            replenishmentDetail.setRh2Rh1Id(c.getString(c.getColumnIndex("RH2_RH1_ID")));
            replenishmentDetail.setRh2Vc1Code(c.getString(c.getColumnIndex("RH2_VC1_CODE")));
            replenishmentDetail.setRh2Pd1Id(c.getString(c.getColumnIndex("RH2_PD1_ID")));
            replenishmentDetail.setRh2SaleType(c.getString(c.getColumnIndex("RH2_SaleType")));
            replenishmentDetail.setRh2Sp1Id(c.getString(c.getColumnIndex("RH2_SP1_ID")));
            replenishmentDetail.setRh2ActualQty(c.getInt(c.getColumnIndex("RH2_ActualQty")));
            replenishmentDetail.setRh2DifferentiaQty(c.getInt(c.getColumnIndex("RH2_DifferentiaQty")));
            replenishmentDetail.setRh2Rp1Id(c.getString(c.getColumnIndex("RH2_RP1_ID")));
            replenishmentDetail.setRh2UploadStatus(c.getString(c.getColumnIndex("RH2_UploadStatus")));
            replenishmentDetail.setRh2CreateUser(c.getString(c.getColumnIndex("RH2_CreateUser")));
            replenishmentDetail.setRh2CreateTime(c.getString(c.getColumnIndex("RH2_CreateTime")));
            replenishmentDetail.setRh2ModifyUser(c.getString(c.getColumnIndex("RH2_ModifyUser")));
            replenishmentDetail.setRh2ModifyTime(c.getString(c.getColumnIndex("RH2_ModifyTime")));
            replenishmentDetail.setRh2RowVersion(c.getString(c.getColumnIndex("RH2_RowVersion")));
            list.add(replenishmentDetail);
        }

        return list;
    }

    /**
     * 根据补货单从表ID更新补货差异数量
     * 
     * @param differentiaQty
     *            　补货差异
     * @param id
     *            　补货单从表ID
     */
    public boolean updateDifferentiaQty(int differentiaQty, int id) {
        String updateSql = "UPDATE ReplenishmentDetail SET RH2_DifferentiaQty = ? WHERE  ID = ?";
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        SQLiteStatement stat = db.compileStatement(updateSql);
        stat.bindLong(1, differentiaQty);
        stat.bindLong(2, id);
        long i = stat.executeInsert();
        return i > 0;
    }

    /**
     * 批量更新补货单从表ID更新补货差异数量
     * 
     * @param map
     *            　key为补货单从表ID,value为补货差异
     */
    public boolean batchUpdateDifferentiaQty(List<ReplenishmentDetailData> list) {
        boolean flag = false;
        String updateSql = "UPDATE ReplenishmentDetail SET RH2_DifferentiaQty = ? WHERE RH2_ID = ?";
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        try {
            // 开启事务
            db.beginTransaction();
            for (ReplenishmentDetailData replenishmentDetail : list) {
                String rh2Id = replenishmentDetail.getRh2Id();
                int differentiaQty = replenishmentDetail.getRh2DifferentiaQty();
                SQLiteStatement stat = db.compileStatement(updateSql);
                stat.bindLong(1, differentiaQty);
                stat.bindString(2, rh2Id);
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

    public List<ReplenishmentDetailData> findReplenishmentDetailToUpload() {
        List<ReplenishmentDetailData> list = new ArrayList<ReplenishmentDetailData>();
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        Cursor c = db
                .rawQuery(
                        "SELECT RH2_ID,RH2_DifferentiaQty FROM ReplenishmentDetail WHERE RH2_DifferentiaQty != 0 and RH2_UploadStatus = ?",
                        new String[] { ReplenishmentDetailData.UPLOAD_UNLOAD });
        while (c.moveToNext()) {
            ReplenishmentDetailData replenishmentDetail = new ReplenishmentDetailData();
            replenishmentDetail.setRh2Id(c.getString(c.getColumnIndex("RH2_ID")));
            replenishmentDetail.setRh2DifferentiaQty(c.getInt(c.getColumnIndex("RH2_DifferentiaQty")));
            list.add(replenishmentDetail);
        }
        return list;
    }

    public boolean updateUploadStatusByRH2Id(String rh2Id) {
        String updateSql = "update ReplenishmentDetail set RH2_UploadStatus = ? where RH2_ID = ?";
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        SQLiteStatement stat = db.compileStatement(updateSql);
        stat.bindString(1, ReplenishmentDetailData.UPLOAD_LOAD);
        stat.bindString(2, rh2Id);
        int i = stat.executeUpdateDelete();
        return i > 0;
    }

    /**
     * 批量更新上传状态
     * 
     * @return
     */
    public boolean batchUpdateUploadStatus(List<ReplenishmentDetailData> replenishmentDetailList) {
        boolean flag = false;
        String updateSql = "update ReplenishmentDetail set RH2_UploadStatus = ? where RH2_ID = ?";
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        try {
            // 开启事务
            db.beginTransaction();

            for (ReplenishmentDetailData replenishmentDetail : replenishmentDetailList) {
                SQLiteStatement stat = db.compileStatement(updateSql);
                stat.bindString(1, ReplenishmentDetailData.UPLOAD_LOAD);
                stat.bindString(2, replenishmentDetail.getRh2Id());
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
