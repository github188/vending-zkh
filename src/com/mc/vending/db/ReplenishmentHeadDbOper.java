package com.mc.vending.db;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.mc.vending.data.ReplenishmentDetailData;
import com.mc.vending.data.ReplenishmentHeadData;
import com.mc.vending.tools.DateHelper;
import com.mc.vending.tools.ZillionLog;

/**
 * 补货单主表操作类
 * 
 * @author wangy
 *
 */
public class ReplenishmentHeadDbOper {

    public List<ReplenishmentHeadData> findAll() {
        List<ReplenishmentHeadData> list = new ArrayList<ReplenishmentHeadData>();
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        Cursor c = db.rawQuery("SELECT * FROM ReplenishmentHead", null);
        while (c.moveToNext()) {
            ReplenishmentHeadData replenishmentHead = new ReplenishmentHeadData();
            replenishmentHead.setRh1Id(c.getString(c.getColumnIndex("RH1_ID")));
            replenishmentHead.setRh1M02Id(c.getString(c.getColumnIndex("RH1_M02_ID")));
            replenishmentHead.setRh1Rhcode(c.getString(c.getColumnIndex("RH1_RHCODE")));
            replenishmentHead.setRh1RhType(c.getString(c.getColumnIndex("RH1_RhType")));
            replenishmentHead.setRh1Cu1Id(c.getString(c.getColumnIndex("RH1_CU1_ID")));
            replenishmentHead.setRh1Vd1Id(c.getString(c.getColumnIndex("RH1_VD1_ID")));
            replenishmentHead.setRh1Wh1Id(c.getString(c.getColumnIndex("RH1_WH1_ID")));
            replenishmentHead.setRh1Ce1IdPh(c.getString(c.getColumnIndex("RH1_CE1_ID_PH")));
            replenishmentHead
                    .setRh1DistributionRemark(c.getString(c.getColumnIndex("RH1_DistributionRemark")));
            replenishmentHead.setRh1St1Id(c.getString(c.getColumnIndex("RH1_ST1_ID")));
            replenishmentHead.setRh1Ce1IdBh(c.getString(c.getColumnIndex("RH1_CE1_ID_BH")));
            replenishmentHead.setRh1ReplenishRemark(c.getString(c.getColumnIndex("RH1_ReplenishRemark")));
            replenishmentHead.setRh1ReplenishReason(c.getString(c.getColumnIndex("RH1_ReplenishReason")));
            replenishmentHead.setRh1OrderStatus(c.getString(c.getColumnIndex("RH1_OrderStatus")));
            replenishmentHead.setRh1DownloadStatus(c.getString(c.getColumnIndex("RH1_DownloadStatus")));
            replenishmentHead.setRh1UploadStatus(c.getString(c.getColumnIndex("RH1_UploadStatus")));
            replenishmentHead.setRh1CreateUser(c.getString(c.getColumnIndex("RH1_CreateUser")));
            replenishmentHead.setRh1CreateTime(c.getString(c.getColumnIndex("RH1_CreateTime")));
            replenishmentHead.setRh1ModifyUser(c.getString(c.getColumnIndex("RH1_ModifyUser")));
            replenishmentHead.setRh1ModifyTime(c.getString(c.getColumnIndex("RH1_ModifyTime")));
            replenishmentHead.setRh1RowVersion(c.getString(c.getColumnIndex("RH1_RowVersion")));
            list.add(replenishmentHead);
        }
        return list;
    }

    public Map<String, String> findAllMap() {
        Map<String, String> map = new HashMap<String, String>();
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        Cursor c = db.rawQuery("SELECT RH1_ID,RH1_RHCODE FROM ReplenishmentHead ", null);
        while (c.moveToNext()) {
            String rh1Id = c.getString(c.getColumnIndex("RH1_ID"));
            String rh1Rhcode = c.getString(c.getColumnIndex("RH1_RHCODE"));
            map.put(rh1Id, rh1Rhcode);
        }
        return map;
    }

    /**
     * 根据订单状态取得最新的补货单主列表
     * 
     * @param orderStatus
     * @return
     */
    public ReplenishmentHeadData getReplenishmentHeadByOrderStatus(String orderStatus) {
        String selectByOrderStatusSql = "select * from ReplenishmentHead where RH1_OrderStatus=? order by RH1_CreateTime desc limit 1";

        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        ReplenishmentHeadData replenishmentHead = null;
        Cursor c = db.rawQuery(selectByOrderStatusSql, new String[] { orderStatus });
        while (c.moveToNext()) {
            replenishmentHead = new ReplenishmentHeadData();
            replenishmentHead.setRh1Id(c.getString(c.getColumnIndex("RH1_ID")));
            replenishmentHead.setRh1M02Id(c.getString(c.getColumnIndex("RH1_M02_ID")));
            replenishmentHead.setRh1Rhcode(c.getString(c.getColumnIndex("RH1_RHCODE")));
            replenishmentHead.setRh1RhType(c.getString(c.getColumnIndex("RH1_RhType")));
            replenishmentHead.setRh1Cu1Id(c.getString(c.getColumnIndex("RH1_CU1_ID")));
            replenishmentHead.setRh1Vd1Id(c.getString(c.getColumnIndex("RH1_VD1_ID")));
            replenishmentHead.setRh1Wh1Id(c.getString(c.getColumnIndex("RH1_WH1_ID")));
            replenishmentHead.setRh1Ce1IdPh(c.getString(c.getColumnIndex("RH1_CE1_ID_PH")));
            replenishmentHead
                    .setRh1DistributionRemark(c.getString(c.getColumnIndex("RH1_DistributionRemark")));
            replenishmentHead.setRh1St1Id(c.getString(c.getColumnIndex("RH1_ST1_ID")));
            replenishmentHead.setRh1Ce1IdBh(c.getString(c.getColumnIndex("RH1_CE1_ID_BH")));
            replenishmentHead.setRh1ReplenishRemark(c.getString(c.getColumnIndex("RH1_ReplenishRemark")));
            replenishmentHead.setRh1ReplenishReason(c.getString(c.getColumnIndex("RH1_ReplenishReason")));
            replenishmentHead.setRh1OrderStatus(c.getString(c.getColumnIndex("RH1_OrderStatus")));
            replenishmentHead.setRh1DownloadStatus(c.getString(c.getColumnIndex("RH1_DownloadStatus")));
            replenishmentHead.setRh1UploadStatus(c.getString(c.getColumnIndex("RH1_UploadStatus")));
            replenishmentHead.setRh1CreateUser(c.getString(c.getColumnIndex("RH1_CreateUser")));
            replenishmentHead.setRh1CreateTime(c.getString(c.getColumnIndex("RH1_CreateTime")));
            replenishmentHead.setRh1ModifyUser(c.getString(c.getColumnIndex("RH1_ModifyUser")));
            replenishmentHead.setRh1ModifyTime(c.getString(c.getColumnIndex("RH1_ModifyTime")));
            replenishmentHead.setRh1RowVersion(c.getString(c.getColumnIndex("RH1_RowVersion")));
            break;
        }
        return replenishmentHead;
    }

    /**
     * 根据主键查询
     * 
     * @param RH1_ID
     * @return
     */
    public ReplenishmentHeadData getReplenishmentHeadById(String RH1_ID) {
        String selectByOrderStatusSql = "select * from ReplenishmentHead where RH1_ID=? ";

        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        ReplenishmentHeadData replenishmentHead = null;
        Cursor c = db.rawQuery(selectByOrderStatusSql, new String[] { RH1_ID });
        while (c.moveToNext()) {
            replenishmentHead = new ReplenishmentHeadData();
            replenishmentHead.setRh1Id(c.getString(c.getColumnIndex("RH1_ID")));
            replenishmentHead.setRh1M02Id(c.getString(c.getColumnIndex("RH1_M02_ID")));
            replenishmentHead.setRh1Rhcode(c.getString(c.getColumnIndex("RH1_RHCODE")));
            replenishmentHead.setRh1RhType(c.getString(c.getColumnIndex("RH1_RhType")));
            replenishmentHead.setRh1Cu1Id(c.getString(c.getColumnIndex("RH1_CU1_ID")));
            replenishmentHead.setRh1Vd1Id(c.getString(c.getColumnIndex("RH1_VD1_ID")));
            replenishmentHead.setRh1Wh1Id(c.getString(c.getColumnIndex("RH1_WH1_ID")));
            replenishmentHead.setRh1Ce1IdPh(c.getString(c.getColumnIndex("RH1_CE1_ID_PH")));
            replenishmentHead
                    .setRh1DistributionRemark(c.getString(c.getColumnIndex("RH1_DistributionRemark")));
            replenishmentHead.setRh1St1Id(c.getString(c.getColumnIndex("RH1_ST1_ID")));
            replenishmentHead.setRh1Ce1IdBh(c.getString(c.getColumnIndex("RH1_CE1_ID_BH")));
            replenishmentHead.setRh1ReplenishRemark(c.getString(c.getColumnIndex("RH1_ReplenishRemark")));
            replenishmentHead.setRh1ReplenishReason(c.getString(c.getColumnIndex("RH1_ReplenishReason")));
            replenishmentHead.setRh1OrderStatus(c.getString(c.getColumnIndex("RH1_OrderStatus")));
            replenishmentHead.setRh1DownloadStatus(c.getString(c.getColumnIndex("RH1_DownloadStatus")));
            replenishmentHead.setRh1UploadStatus(c.getString(c.getColumnIndex("RH1_UploadStatus")));
            replenishmentHead.setRh1CreateUser(c.getString(c.getColumnIndex("RH1_CreateUser")));
            replenishmentHead.setRh1CreateTime(c.getString(c.getColumnIndex("RH1_CreateTime")));
            replenishmentHead.setRh1ModifyUser(c.getString(c.getColumnIndex("RH1_ModifyUser")));
            replenishmentHead.setRh1ModifyTime(c.getString(c.getColumnIndex("RH1_ModifyTime")));
            replenishmentHead.setRh1RowVersion(c.getString(c.getColumnIndex("RH1_RowVersion")));
            break;
        }
        return replenishmentHead;
    }

    /**
     * 查询“补货单主表”记录，条件：订单状态=已完成，并且全部”补货单从表.补货差异=0”，按“补货单号“倒排序
     * 
     * @param orderStatus
     * @return
     */
    public List<ReplenishmentHeadData> findReplenishmentHeadByOrderStatus(String orderStatus,String rhType) {
        List<ReplenishmentHeadData> list = new ArrayList<ReplenishmentHeadData>();
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        Date currentDate = DateHelper.currentDateTime();
        Date date = DateHelper.add(currentDate, Calendar.MONTH, -1);
        String queryDate = DateHelper.format(date, "yyyy-MM-dd HH:mm:ss");
        Cursor c = db
                .rawQuery(
                        "SELECT head.* FROM ReplenishmentHead head JOIN (SELECT RH2_RH1_ID,SUM(abs(RH2_DifferentiaQty)) FROM ReplenishmentDetail GROUP BY RH2_RH1_ID HAVING SUM(abs(RH2_DifferentiaQty))=0 ) tmp "
                                + "ON head.RH1_ID = tmp.RH2_RH1_ID WHERE head.RH1_OrderStatus = ? and head.RH1_CreateTime >= ? and head.RH1_RhType = ? ORDER BY RH1_RHCODE DESC",
                        new String[] { orderStatus, queryDate ,rhType});
        while (c.moveToNext()) {
            ReplenishmentHeadData replenishmentHead = new ReplenishmentHeadData();
            replenishmentHead.setRh1Id(c.getString(c.getColumnIndex("RH1_ID")));
            replenishmentHead.setRh1M02Id(c.getString(c.getColumnIndex("RH1_M02_ID")));
            replenishmentHead.setRh1Rhcode(c.getString(c.getColumnIndex("RH1_RHCODE")));
            replenishmentHead.setRh1RhType(c.getString(c.getColumnIndex("RH1_RhType")));
            replenishmentHead.setRh1Cu1Id(c.getString(c.getColumnIndex("RH1_CU1_ID")));
            replenishmentHead.setRh1Vd1Id(c.getString(c.getColumnIndex("RH1_VD1_ID")));
            replenishmentHead.setRh1Wh1Id(c.getString(c.getColumnIndex("RH1_WH1_ID")));
            replenishmentHead.setRh1Ce1IdPh(c.getString(c.getColumnIndex("RH1_CE1_ID_PH")));
            replenishmentHead
                    .setRh1DistributionRemark(c.getString(c.getColumnIndex("RH1_DistributionRemark")));
            replenishmentHead.setRh1St1Id(c.getString(c.getColumnIndex("RH1_ST1_ID")));
            replenishmentHead.setRh1Ce1IdBh(c.getString(c.getColumnIndex("RH1_CE1_ID_BH")));
            replenishmentHead.setRh1ReplenishRemark(c.getString(c.getColumnIndex("RH1_ReplenishRemark")));
            replenishmentHead.setRh1ReplenishReason(c.getString(c.getColumnIndex("RH1_ReplenishReason")));
            replenishmentHead.setRh1OrderStatus(c.getString(c.getColumnIndex("RH1_OrderStatus")));
            replenishmentHead.setRh1DownloadStatus(c.getString(c.getColumnIndex("RH1_DownloadStatus")));
            replenishmentHead.setRh1UploadStatus(c.getString(c.getColumnIndex("RH1_UploadStatus")));
            replenishmentHead.setRh1CreateUser(c.getString(c.getColumnIndex("RH1_CreateUser")));
            replenishmentHead.setRh1CreateTime(c.getString(c.getColumnIndex("RH1_CreateTime")));
            replenishmentHead.setRh1ModifyUser(c.getString(c.getColumnIndex("RH1_ModifyUser")));
            replenishmentHead.setRh1ModifyTime(c.getString(c.getColumnIndex("RH1_ModifyTime")));
            replenishmentHead.setRh1RowVersion(c.getString(c.getColumnIndex("RH1_RowVersion")));
            list.add(replenishmentHead);
        }
        return list;
    }

    /**
     * 插入
     * 
     * @param replenishmentHead
     * @return
     */
    public boolean addReplenishmentHead(ReplenishmentHeadData replenishmentHead) {
        String insertSql = "insert into ReplenishmentHead(RH1_ID,RH1_M02_ID,RH1_RHCODE,RH1_RhType,RH1_CU1_ID,RH1_VD1_ID,"
                + "RH1_WH1_ID,RH1_CE1_ID_PH,RH1_DistributionRemark,RH1_ST1_ID,RH1_CE1_ID_BH,RH1_ReplenishRemark,"
                + "RH1_ReplenishReason,RH1_OrderStatus,RH1_DownloadStatus,RH1_UploadStatus,RH1_CreateUser,RH1_CreateTime,"
                + "RH1_ModifyUser,RH1_ModifyTime,RH1_RowVersion)"
                + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        SQLiteStatement stat = db.compileStatement(insertSql);
        stat.bindString(1, replenishmentHead.getRh1Id());
        stat.bindString(2, replenishmentHead.getRh1M02Id());
        stat.bindString(3, replenishmentHead.getRh1Rhcode());
        stat.bindString(4, replenishmentHead.getRh1RhType());
        stat.bindString(5, replenishmentHead.getRh1Cu1Id());
        stat.bindString(6, replenishmentHead.getRh1Vd1Id());
        stat.bindString(7, replenishmentHead.getRh1Wh1Id());
        stat.bindString(8, replenishmentHead.getRh1Ce1IdPh());
        stat.bindString(9, replenishmentHead.getRh1DistributionRemark());
        stat.bindString(10, replenishmentHead.getRh1St1Id());
        stat.bindString(11, replenishmentHead.getRh1Ce1IdBh());
        stat.bindString(12, replenishmentHead.getRh1ReplenishRemark());
        stat.bindString(13, replenishmentHead.getRh1ReplenishReason());
        stat.bindString(14, replenishmentHead.getRh1OrderStatus());
        stat.bindString(15, replenishmentHead.getRh1DownloadStatus());
        stat.bindString(16, replenishmentHead.getRh1UploadStatus());
        stat.bindString(17, replenishmentHead.getRh1CreateUser());
        stat.bindString(18, replenishmentHead.getRh1CreateTime());
        stat.bindString(19, replenishmentHead.getRh1ModifyUser());
        stat.bindString(20, replenishmentHead.getRh1ModifyTime());
        stat.bindString(21, replenishmentHead.getRh1RowVersion());
        long i = stat.executeInsert();
        return i > 0;
    }

    /**
     * 批量插入
     * 
     * @param list
     */
    public boolean batchAddReplenishmentHead(List<ReplenishmentHeadData> list) {
        boolean flag = false;
        String headSql = "insert into ReplenishmentHead(RH1_ID,RH1_M02_ID,RH1_RHCODE,RH1_RhType,RH1_CU1_ID,RH1_VD1_ID,"
                + "RH1_WH1_ID,RH1_CE1_ID_PH,RH1_DistributionRemark,RH1_ST1_ID,RH1_CE1_ID_BH,RH1_ReplenishRemark,"
                + "RH1_ReplenishReason,RH1_OrderStatus,RH1_DownloadStatus,RH1_UploadStatus,RH1_CreateUser,RH1_CreateTime,"
                + "RH1_ModifyUser,RH1_ModifyTime,RH1_RowVersion)"
                + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        String detailSql = "insert into ReplenishmentDetail(RH2_ID,RH2_M02_ID,RH2_RH1_ID,RH2_VC1_CODE,RH2_PD1_ID,RH2_SaleType,RH2_SP1_ID,RH2_ActualQty,RH2_DifferentiaQty,RH2_RP1_ID,RH2_UploadStatus,"
                + "RH2_CreateUser,RH2_CreateTime,RH2_ModifyUser,RH2_ModifyTime,RH2_RowVersion)"
                + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        try {
            // 开启事务
            db.beginTransaction();
            for (ReplenishmentHeadData replenishmentHead : list) {
                SQLiteStatement stat = db.compileStatement(headSql);
                stat.bindString(1, replenishmentHead.getRh1Id());
                stat.bindString(2, replenishmentHead.getRh1M02Id());
                stat.bindString(3, replenishmentHead.getRh1Rhcode());
                stat.bindString(4, replenishmentHead.getRh1RhType());
                stat.bindString(5, replenishmentHead.getRh1Cu1Id());
                stat.bindString(6, replenishmentHead.getRh1Vd1Id());
                stat.bindString(7, replenishmentHead.getRh1Wh1Id());
                stat.bindString(8, replenishmentHead.getRh1Ce1IdPh());
                stat.bindString(9, replenishmentHead.getRh1DistributionRemark());
                stat.bindString(10, replenishmentHead.getRh1St1Id());
                stat.bindString(11, replenishmentHead.getRh1Ce1IdBh());
                stat.bindString(12, replenishmentHead.getRh1ReplenishRemark());
                stat.bindString(13, replenishmentHead.getRh1ReplenishReason());
                stat.bindString(14, replenishmentHead.getRh1OrderStatus());
                stat.bindString(15, replenishmentHead.getRh1DownloadStatus());
                stat.bindString(16, replenishmentHead.getRh1UploadStatus());
                stat.bindString(17, replenishmentHead.getRh1CreateUser());
                stat.bindString(18, replenishmentHead.getRh1CreateTime());
                stat.bindString(19, replenishmentHead.getRh1ModifyUser());
                stat.bindString(20, replenishmentHead.getRh1ModifyTime());
                stat.bindString(21, replenishmentHead.getRh1RowVersion());
                long i = stat.executeInsert();

                List<ReplenishmentDetailData> children = replenishmentHead.getChildren();
                if (children != null) {
                    for (ReplenishmentDetailData replenishmentDetail : children) {
                        SQLiteStatement detail_stat = db.compileStatement(detailSql);
                        detail_stat.bindString(1, replenishmentDetail.getRh2Id());
                        detail_stat.bindString(2, replenishmentDetail.getRh2M02Id());
                        detail_stat.bindString(3, replenishmentDetail.getRh2Rh1Id());
                        detail_stat.bindString(4, replenishmentDetail.getRh2Vc1Code());
                        detail_stat.bindString(5, replenishmentDetail.getRh2Pd1Id());
                        detail_stat.bindString(6, replenishmentDetail.getRh2SaleType());
                        detail_stat.bindString(7, replenishmentDetail.getRh2Sp1Id());
                        detail_stat.bindLong(8, replenishmentDetail.getRh2ActualQty());
                        detail_stat.bindLong(9, replenishmentDetail.getRh2DifferentiaQty());
                        detail_stat.bindString(10, replenishmentDetail.getRh2Rp1Id());
                        detail_stat.bindString(11, replenishmentDetail.getRh2UploadStatus());
                        detail_stat.bindString(12, replenishmentDetail.getRh2CreateUser());
                        detail_stat.bindString(13, replenishmentDetail.getRh2CreateTime());
                        detail_stat.bindString(14, replenishmentDetail.getRh2ModifyUser());
                        detail_stat.bindString(15, replenishmentDetail.getRh2ModifyTime());
                        detail_stat.bindString(16, replenishmentDetail.getRh2RowVersion());
                        detail_stat.executeInsert();
                    }
                }

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

    /**
     * 批量更新订单状态
     * 
     * @param list
     */
    public boolean batchUpdateReplenishmentHead(List<ReplenishmentHeadData> list) {
        boolean flag = false;
        String headSql = "UPDATE ReplenishmentHead SET RH1_OrderStatus=? WHERE RH1_ID=? AND RH1_OrderStatus=0";

        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        try {
            // 开启事务
            db.beginTransaction();
            for (ReplenishmentHeadData replenishmentHead : list) {
                SQLiteStatement stat = db.compileStatement(headSql);
                stat.bindString(1, replenishmentHead.getRh1OrderStatus());
                stat.bindString(2, replenishmentHead.getRh1Id());
                long i = stat.executeUpdateDelete();
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

    public void clearReplenishment(String startDate) {
        String deleteHeadSql = "DELETE FROM ReplenishmentHead where RH1_UploadStatus='"
                + ReplenishmentHeadData.UPLOAD_LOAD + "' and substr(Rh1_createtime,0,11) <= '" + startDate
                + "' ";
        String deleteDetailSql = "DELETE FROM ReplenishmentDetail where RH2_UploadStatus='"
                + ReplenishmentDetailData.UPLOAD_LOAD + "' and substr(Rh2_createtime,0,11) <= '" + startDate
                + "' ";
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        try {
            // 开启事务
            db.beginTransaction();
            SQLiteStatement detail_stat = db.compileStatement(deleteDetailSql);
            detail_stat.executeInsert();

            SQLiteStatement stat = db.compileStatement(deleteHeadSql);
            stat.executeInsert();

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

    public boolean deleteAll() {
        boolean flag = false;
        String deleteHeadSql = "DELETE FROM ReplenishmentHead";
        String deleteDetailSql = "DELETE FROM ReplenishmentDetail";
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        try {
            // 开启事务
            db.beginTransaction();
            SQLiteStatement detail_stat = db.compileStatement(deleteDetailSql);
            detail_stat.executeInsert();

            SQLiteStatement stat = db.compileStatement(deleteHeadSql);
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

    /**
     * 根据补货单主表id更新状态
     * 
     * @param rhcode
     * @param modifyUser
     * @param modifyTime
     * @param rowVersion
     * @param orderStatus
     * @return
     */
    public boolean updateOrderStatusByRh1Id(String rh1Id, String orderStatus) {
        String updateSql = "update ReplenishmentHead set RH1_OrderStatus = ? where RH1_ID = ?";
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        SQLiteStatement stat = db.compileStatement(updateSql);
        stat.bindString(1, orderStatus);
        stat.bindString(2, rh1Id);
        int i = stat.executeUpdateDelete();
        return i > 0;
    }

    /**
     * 上传到云端回传数据更新上传状态
     * 
     * @param rh1Id
     * @param orderStatus
     * @return
     */
    public boolean updateUploadStatusByRH1Id(String rh1Id) {
        String updateSql = "update ReplenishmentHead set RH1_UploadStatus = ? where RH1_ID = ?";
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        SQLiteStatement stat = db.compileStatement(updateSql);
        stat.bindString(1, ReplenishmentHeadData.UPLOAD_LOAD);
        stat.bindString(2, rh1Id);
        int i = stat.executeUpdateDelete();
        return i > 0;
    }

    /**
     * 批量更新上传状态
     * 
     * @return
     */
    public boolean batchUpdateUploadStatus(List<ReplenishmentHeadData> replenishmentHeadList) {
        boolean flag = false;
        String updateSql = "update ReplenishmentHead set RH1_UploadStatus = ? where RH1_ID = ?";
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        try {
            // 开启事务
            db.beginTransaction();

            for (ReplenishmentHeadData replenishmentHead : replenishmentHeadList) {
                SQLiteStatement stat = db.compileStatement(updateSql);
                stat.bindString(1, ReplenishmentHeadData.UPLOAD_LOAD);
                stat.bindString(2, replenishmentHead.getRh1Id());
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

    /**
     * 根据售货机ID得到补货单列表
     * 
     * @param vendingId
     * @return
     */
    public List<ReplenishmentHeadData> findReplenishmentHeadToUpload() {
        List<ReplenishmentHeadData> list = new ArrayList<ReplenishmentHeadData>();
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        Cursor c = db
                .rawQuery(
                        "SELECT RH1_ID,RH1_OrderStatus FROM ReplenishmentHead where RH1_OrderStatus = ? and RH1_UploadStatus= ?",
                        new String[] { ReplenishmentHeadData.ORDERSTATUS_FINISHED,
                                ReplenishmentHeadData.UPLOAD_UNLOAD });
        while (c.moveToNext()) {
            ReplenishmentHeadData replenishmentHead = new ReplenishmentHeadData();
            replenishmentHead.setRh1Id(c.getString(c.getColumnIndex("RH1_ID")));
            replenishmentHead.setRh1OrderStatus(c.getString(c.getColumnIndex("RH1_OrderStatus")));
            list.add(replenishmentHead);
        }
        return list;
    }

}
