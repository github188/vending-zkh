package com.mc.vending.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.mc.vending.data.RetreatDetailData;
import com.mc.vending.data.RetreatHeadData;
import com.mc.vending.tools.ZillionLog;

public class RetreatHeadDbOper {

    /**
     * 查询所有的记录列表数据
     * 
     * @return
     */
    public List<RetreatHeadData> findAll() {
        List<RetreatHeadData> list = new ArrayList<RetreatHeadData>();
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        Cursor c = db.rawQuery("SELECT * FROM RetreatHead order by RT1_Status,RT1_RTCode desc", null);
        while (c.moveToNext()) {
            RetreatHeadData data = new RetreatHeadData();
            data.setRt1Id(c.getString(c.getColumnIndex("RT1_ID")));
            data.setRt1Ce1Id(c.getString(c.getColumnIndex("RT1_CE1_ID")));
            data.setRt1M02Id(c.getString(c.getColumnIndex("RT1_M02_ID")));
            data.setRt1Cu1Id(c.getString(c.getColumnIndex("RT1_CU1_ID")));
            data.setRt1Rtcode(c.getString(c.getColumnIndex("RT1_RTCode")));
            data.setRt1Status(c.getString(c.getColumnIndex("RT1_Status")));
            data.setRt1Type(c.getString(c.getColumnIndex("RT1_Type")));
            data.setRt1Vd1Id(c.getString(c.getColumnIndex("RT1_VD1_ID")));
            data.setCreateUser(c.getString(c.getColumnIndex("RT1_CreateUser")));
            data.setCreateTime(c.getString(c.getColumnIndex("RT1_CreateTime")));
            data.setModifyUser(c.getString(c.getColumnIndex("RT1_ModifyUser")));
            data.setModifyTime(c.getString(c.getColumnIndex("RT1_ModifyTime")));
            data.setRowVersion(c.getString(c.getColumnIndex("RT1_RowVersion")));

            List<RetreatDetailData> datas = new RetreatDetailDbOper().findRetreatDetailDataByRtId(data
                    .getRt1Id());

            data.setRetreatDetailDatas(datas);
            list.add(data);
        }
        return list;
    }

    public boolean batchAddReturnForward(List<RetreatHeadData> list) {
        boolean flag = false;
        String insertSql = "insert into RetreatHead (RT1_id,RT1_M02_ID,RT1_RTCode,RT1_Type,RT1_CU1_ID,RT1_VD1_ID,RT1_CE1_ID,RT1_Status,"
                + "RT1_CreateUser,RT1_CreateTime,RT1_ModifyUser,RT1_ModifyTime,RT1_RowVersion)"
                + "values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
        String insertDetail = "insert into RetreatDetail (RT2_ID,RT2_M02_ID,RT2_RT1_ID,RT2_VC1_CODE,RT2_PD1_ID,"
                + "RT2_SaleType,RT2_SP1_ID,RT2_PlanQty,RT2_ActualQty,"
                + "RT2_CreateUser,RT2_CreateTime,RT2_ModifyUser,RT2_ModifyTime,RT2_RowVersion)"
                + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        try {
            //开启事务
            db.beginTransaction();
            for (RetreatHeadData data : list) {

                SQLiteStatement stat = db.compileStatement("DELETE FROM RetreatHead where RT1_id='"
                        + data.getRt1Id() + "'");
                stat.executeUpdateDelete();
                stat = db.compileStatement("DELETE FROM RetreatDetail where RT2_RT1_ID='" + data.getRt1Id()
                        + "'");
                stat.executeUpdateDelete();

                stat = db.compileStatement(insertSql);
                int i = 1;
                stat.bindString(i++, data.getRt1Id());
                stat.bindString(i++, data.getRt1M02Id());
                stat.bindString(i++, data.getRt1Rtcode());
                stat.bindString(i++, data.getRt1Type());
                stat.bindString(i++, data.getRt1Cu1Id());
                stat.bindString(i++, data.getRt1Vd1Id());
                stat.bindString(i++, data.getRt1Ce1Id());
                stat.bindString(i++, data.getRt1Status());
                stat.bindString(i++, data.getCreateUser());
                stat.bindString(i++, data.getCreateTime());
                stat.bindString(i++, data.getModifyUser());
                stat.bindString(i++, data.getModifyTime());
                stat.bindString(i++, data.getRowVersion());
                stat.executeInsert();

                List<RetreatDetailData> detailDatas = data.getRetreatDetailDatas();
                for (RetreatDetailData detailData : detailDatas) {

                    stat = db.compileStatement(insertDetail);
                    i = 1;
                    stat.bindString(i++, detailData.getRt2Id());
                    stat.bindString(i++, detailData.getRt2M02Id());
                    stat.bindString(i++, detailData.getRt2Rt1Id());
                    stat.bindString(i++, detailData.getRt2Vc1Code());
                    stat.bindString(i++, detailData.getRt2Pd1Id());
                    stat.bindString(i++, detailData.getRt2SaleType());
                    stat.bindString(i++, detailData.getRt2Sp1Id());
                    stat.bindLong(i++, detailData.getRt2PlanQty());
                    stat.bindLong(i++, detailData.getRt2ActualQty());
                    stat.bindString(i++, detailData.getCreateUser());
                    stat.bindString(i++, detailData.getCreateTime());
                    stat.bindString(i++, detailData.getModifyUser());
                    stat.bindString(i++, detailData.getModifyTime());
                    stat.bindString(i++, detailData.getRowVersion());
                    stat.executeInsert();
                }

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

    public boolean updateState(String rt1Id, Map<String, Integer> vendingChnQtyMap) {
        boolean flag = false;
        String sql = "update RetreatHead set RT1_Status='1' where RT1_id='" + rt1Id + "'";
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        try {
            //开启事务
            db.beginTransaction();
            SQLiteStatement stat = db.compileStatement(sql);
            stat.executeUpdateDelete();

            Set<String> keySet = vendingChnQtyMap.keySet();
            for (String vendingchn : keySet) {
                stat = db.compileStatement("update RetreatDetail set rt2_actualqty="
                        + vendingChnQtyMap.get(vendingchn) + " where rt2_RT1_id='" + rt1Id
                        + "' and rt2_vc1_code='" + vendingchn + "'");
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

    public void clearRetreat(String startDate) {
        String deleteHeadSql = "DELETE FROM RetreatHead where RT1_Status='"
                + RetreatHeadData.RETREAT_STATUS_FINISH + "' and substr(RT1_CreateTime,0,11) <= '"
                + startDate + "' ";
        String deleteDetailSql = "DELETE FROM ReplenishmentDetail where substr(Rt2_createtime,0,11) <= '"
                + startDate + "' ";
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

    public Map<String, String> findAllMap() {
        Map<String, String> map = new HashMap<String, String>();
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        Cursor c = db.rawQuery("SELECT RT1_ID,RT1_RTCode FROM RetreatHead ", null);
        while (c.moveToNext()) {
            String rh1Id = c.getString(c.getColumnIndex("RT1_ID"));
            String rh1Rhcode = c.getString(c.getColumnIndex("RT1_RTCode"));
            map.put(rh1Id, rh1Rhcode);
        }
        return map;
    }

    public boolean batchUpdateReturnForward(List<RetreatHeadData> list) {
        boolean flag = false;
        String headSql = "UPDATE RetreatHead SET RT1_Status=? WHERE RT1_ID=? ";

        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        try {
            // 开启事务
            db.beginTransaction();
            for (RetreatHeadData replenishmentHead : list) {
                SQLiteStatement stat = db.compileStatement(headSql);
                stat.bindString(1, replenishmentHead.getRt1Status());
                stat.bindString(2, replenishmentHead.getRt1Id());
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
}
