package com.mc.vending.db;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.mc.vending.data.InventoryHistoryData;
import com.mc.vending.tools.ZillionLog;

public class InventoryHistoryDbOper {
    /**
     * 增加盘点记录　单条
     * @param vendingChnStock
     * @return
     */
    public boolean addInventoryHistory(InventoryHistoryData inventoryHistory) {
        String insertSql = "insert into InventoryHistory(IH3_ID,IH3_M02_ID,IH3_IHCODE,IH3_ActualDate,IH3_CU1_ID,IH3_InventoryPeople,IH3_VD1_ID,IH3_VC1_CODE,IH3_PD1_ID,IH3_Quantity,IH3_InventoryQty,IH3_DifferentiaQty,IH3_UploadStatus,IH3_CreateUser,IH3_CreateTime,IH3_ModifyUser,IH3_ModifyTime,IH3_RowVersion)"
                           + " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        SQLiteStatement stat = db.compileStatement(insertSql);

        stat.bindString(1, inventoryHistory.getIh3Id());
        stat.bindString(2, inventoryHistory.getIh3M02Id());
        stat.bindString(3, inventoryHistory.getIh3IHcode());
        stat.bindString(4, inventoryHistory.getIh3ActualDate());
        stat.bindString(5, inventoryHistory.getIh3Cu1Id());
        stat.bindString(6, inventoryHistory.getIh3InventoryPeople());
        stat.bindString(7, inventoryHistory.getIh3Vd1Id());
        stat.bindString(8, inventoryHistory.getIh3Vc1Code());
        stat.bindString(9, inventoryHistory.getIh3Pd1Id());
        stat.bindLong(10, inventoryHistory.getIh3Quantity());
        stat.bindLong(11, inventoryHistory.getIh3InventoryQty());
        stat.bindLong(12, inventoryHistory.getIh3DifferentiaQty());
        stat.bindString(13, inventoryHistory.getIh3UploadStatus());
        stat.bindString(14, inventoryHistory.getIh3CreateUser());
        stat.bindString(15, inventoryHistory.getIh3CreateTime());
        stat.bindString(16, inventoryHistory.getIh3ModifyUser());
        stat.bindString(17, inventoryHistory.getIh3ModifyTime());
        stat.bindString(18, inventoryHistory.getIh3RowVersion());
        long i = stat.executeInsert();

        return i > 0;
    }

    /**
     * 批量增加售盘点记录
     * @param list
     */
    public boolean batchAddInventoryHistory(List<InventoryHistoryData> list) {
        boolean flag = false;
        String insertSql = "insert into InventoryHistory(IH3_ID,IH3_M02_ID,IH3_IHCODE,IH3_ActualDate,IH3_CU1_ID,IH3_InventoryPeople,IH3_VD1_ID,IH3_VC1_CODE,IH3_PD1_ID,IH3_Quantity,IH3_InventoryQty,IH3_DifferentiaQty,IH3_UploadStatus,IH3_CreateUser,IH3_CreateTime,IH3_ModifyUser,IH3_ModifyTime,IH3_RowVersion)"
                           + " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        try {
            //开启事务
            db.beginTransaction();
            for (InventoryHistoryData inventoryHistory : list) {
                SQLiteStatement stat = db.compileStatement(insertSql);
                stat.bindString(1, inventoryHistory.getIh3Id());
                stat.bindString(2, inventoryHistory.getIh3M02Id());
                stat.bindString(3, inventoryHistory.getIh3IHcode());
                stat.bindString(4, inventoryHistory.getIh3ActualDate());
                stat.bindString(5, inventoryHistory.getIh3Cu1Id());
                stat.bindString(6, inventoryHistory.getIh3InventoryPeople());
                stat.bindString(7, inventoryHistory.getIh3Vd1Id());
                stat.bindString(8, inventoryHistory.getIh3Vc1Code());
                stat.bindString(9, inventoryHistory.getIh3Pd1Id());
                stat.bindLong(10, inventoryHistory.getIh3Quantity());
                stat.bindLong(11, inventoryHistory.getIh3InventoryQty());
                stat.bindLong(12, inventoryHistory.getIh3DifferentiaQty());
                stat.bindString(13, inventoryHistory.getIh3UploadStatus());
                stat.bindString(14, inventoryHistory.getIh3CreateUser());
                stat.bindString(15, inventoryHistory.getIh3CreateTime());
                stat.bindString(16, inventoryHistory.getIh3ModifyUser());
                stat.bindString(17, inventoryHistory.getIh3ModifyTime());
                stat.bindString(18, inventoryHistory.getIh3RowVersion());
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
     * 根据未上传状态得到记录
     * @return
     */
    public List<InventoryHistoryData> findInventoryHistoryDataToUpload() {
        List<InventoryHistoryData> list = new ArrayList<InventoryHistoryData>();
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        Cursor c = db.rawQuery("SELECT * FROM InventoryHistory WHERE IH3_UploadStatus=?",
            new String[] { InventoryHistoryData.UPLOAD_UNLOAD });
        while (c.moveToNext()) {
            InventoryHistoryData inventoryHistory = new InventoryHistoryData();
            inventoryHistory.setIh3Id(c.getString(c.getColumnIndex("IH3_ID")));
            inventoryHistory.setIh3M02Id(c.getString(c.getColumnIndex("IH3_M02_ID")));
            inventoryHistory.setIh3IHcode(c.getString(c.getColumnIndex("IH3_IHCODE")));
            inventoryHistory.setIh3ActualDate(c.getString(c.getColumnIndex("IH3_ActualDate")));
            inventoryHistory.setIh3Cu1Id(c.getString(c.getColumnIndex("IH3_CU1_ID")));
            inventoryHistory.setIh3InventoryPeople(c.getString(c
                .getColumnIndex("IH3_InventoryPeople")));
            inventoryHistory.setIh3Vd1Id(c.getString(c.getColumnIndex("IH3_VD1_ID")));
            inventoryHistory.setIh3Vc1Code(c.getString(c.getColumnIndex("IH3_VC1_CODE")));
            inventoryHistory.setIh3Pd1Id(c.getString(c.getColumnIndex("IH3_PD1_ID")));
            inventoryHistory.setIh3Quantity(c.getInt(c.getColumnIndex("IH3_Quantity")));
            inventoryHistory.setIh3InventoryQty(c.getInt(c.getColumnIndex("IH3_InventoryQty")));
            inventoryHistory.setIh3DifferentiaQty(c.getInt(c.getColumnIndex("IH3_DifferentiaQty")));
            inventoryHistory.setIh3CreateUser(c.getString(c.getColumnIndex("IH3_CreateUser")));
            inventoryHistory.setIh3CreateTime(c.getString(c.getColumnIndex("IH3_CreateTime")));

            list.add(inventoryHistory);
        }
        return list;
    }

    /**
     * 更新上传状态
     * @param ih3Id
     * @return
     */
    public boolean updateUploadStatusByIh3Id(String ih3Id) {
        String updateSql = "update InventoryHistory set IH3_UploadStatus=? where IH3_ID=?";
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        SQLiteStatement stat = db.compileStatement(updateSql);
        stat.bindString(1, InventoryHistoryData.UPLOAD_LOAD);
        stat.bindString(2, ih3Id);
        int i = stat.executeUpdateDelete();
        return i > 0;
    }

    /**
     * 批量更新上传状态
     * @return
     */
    public boolean batchUpdateUploadStatus(List<InventoryHistoryData> inventoryHistoryList) {
        boolean flag = false;
        String updateSql = "update InventoryHistory set IH3_UploadStatus=? where IH3_ID=?";
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        try {
            //开启事务
            db.beginTransaction();

            for (InventoryHistoryData inventoryHistoryData : inventoryHistoryList) {
                SQLiteStatement stat = db.compileStatement(updateSql);
                stat.bindString(1, InventoryHistoryData.UPLOAD_LOAD);
                stat.bindString(2, inventoryHistoryData.getIh3Id());
                stat.executeUpdateDelete();
            }
            //数据更新成功，设置事物成功标志  
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
