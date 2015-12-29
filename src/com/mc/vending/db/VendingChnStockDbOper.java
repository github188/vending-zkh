package com.mc.vending.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.mc.vending.data.VendingChnData;
import com.mc.vending.data.VendingChnStockData;
import com.mc.vending.tools.StringHelper;
import com.mc.vending.tools.ZillionLog;

/**
 * 售货机货道库存　操作类
 * 
 * @Filename: VendingChnStockDbOper.java
 * @Version: 1.0
 * @Author: fenghu
 * @Email: fenghu@mightcloud.com
 *
 */
public class VendingChnStockDbOper {
    public List<VendingChnStockData> findAll() {
        List<VendingChnStockData> list = new ArrayList<VendingChnStockData>();
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        Cursor c = db.rawQuery("SELECT * FROM VendingChnStock", null);
        while (c.moveToNext()) {
            VendingChnStockData vendingChnStock = new VendingChnStockData();
            vendingChnStock.setVs1Id(c.getString(c.getColumnIndex("VS1_ID")));
            vendingChnStock.setVs1M02Id(c.getString(c.getColumnIndex("VS1_M02_ID")));
            vendingChnStock.setVs1Vd1Id(c.getString(c.getColumnIndex("VS1_VD1_ID")));
            vendingChnStock.setVs1Vc1Code(c.getString(c.getColumnIndex("VS1_VC1_CODE")));
            vendingChnStock.setVs1Pd1Id(c.getString(c.getColumnIndex("VS1_PD1_ID")));
            vendingChnStock.setVs1Quantity(c.getInt(c.getColumnIndex("VS1_Quantity")));
            vendingChnStock.setVs1CreateUser(c.getString(c.getColumnIndex("VS1_CreateUser")));
            vendingChnStock.setVs1CreateTime(c.getString(c.getColumnIndex("VS1_CreateTime")));
            vendingChnStock.setVs1ModifyUser(c.getString(c.getColumnIndex("VS1_ModifyUser")));
            vendingChnStock.setVs1ModifyTime(c.getString(c.getColumnIndex("VS1_ModifyTime")));
            vendingChnStock.setVs1RowVersion(c.getString(c.getColumnIndex("VS1_RowVersion")));
            list.add(vendingChnStock);
        }
        return list;
    }
    public List<VendingChnStockData> findAllByChn() {
        List<VendingChnStockData> list = new ArrayList<VendingChnStockData>();
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        Cursor c = db.rawQuery("SELECT * FROM VendingChnStock vcs "
                + "left join VendingChn vc on vcs.VS1_PD1_ID = vc.VC1_PD1_ID and vcs.VS1_VC1_CODE = vc.VC1_CODE "
                + "where vc.VC1_Status="+VendingChnData.VENDINGCHN_STATUS_NORMAL, null);
        while (c.moveToNext()) {
            VendingChnStockData vendingChnStock = new VendingChnStockData();
            vendingChnStock.setVs1Id(c.getString(c.getColumnIndex("VS1_ID")));
            vendingChnStock.setVs1M02Id(c.getString(c.getColumnIndex("VS1_M02_ID")));
            vendingChnStock.setVs1Vd1Id(c.getString(c.getColumnIndex("VS1_VD1_ID")));
            vendingChnStock.setVs1Vc1Code(c.getString(c.getColumnIndex("VS1_VC1_CODE")));
            vendingChnStock.setVs1Pd1Id(c.getString(c.getColumnIndex("VS1_PD1_ID")));
            vendingChnStock.setVs1Quantity(c.getInt(c.getColumnIndex("VS1_Quantity")));
            vendingChnStock.setVs1CreateUser(c.getString(c.getColumnIndex("VS1_CreateUser")));
            vendingChnStock.setVs1CreateTime(c.getString(c.getColumnIndex("VS1_CreateTime")));
            vendingChnStock.setVs1ModifyUser(c.getString(c.getColumnIndex("VS1_ModifyUser")));
            vendingChnStock.setVs1ModifyTime(c.getString(c.getColumnIndex("VS1_ModifyTime")));
            vendingChnStock.setVs1RowVersion(c.getString(c.getColumnIndex("VS1_RowVersion")));
            list.add(vendingChnStock);
            
        }
        return list;
    }
    
    /**
     * 获取所有的货道库存量 key为货道编号,value为货道库存
     * @return
     */
    public Map<String, VendingChnStockData> getStockDataMap() {
//        List<VendingChnStockData> chnStockDatas = findAll();
        List<VendingChnStockData> chnStockDatas = findAllByChn();

        Map<String, VendingChnStockData> map = new HashMap<String, VendingChnStockData>();
        for (VendingChnStockData vendingChnStockData : chnStockDatas) {
            map.put(vendingChnStockData.getVs1Vc1Code(), vendingChnStockData);
        }
        
        return map;
    }

    /**
     * 获取所有的货道库存量 key为货道编号,value为货道库存量
     * 
     * @return
     */
    public Map<String, Integer> getStockMap() {
        Map<String, Integer> map = new HashMap<String, Integer>();
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        Cursor c = db.rawQuery("SELECT * FROM VendingChnStock", null);
        while (c.moveToNext()) {
            String code = c.getString(c.getColumnIndex("VS1_VC1_CODE"));
            int qty = c.getInt(c.getColumnIndex("VS1_Quantity"));
            map.put(code, qty);
        }
        return map;
    }

    /**
     * 根据售货机ID和售货机货道编号查询售货机货道库存信息
     * 
     * @param vendingId
     *            　售货机ID
     * @param vcCode
     *            　货机货道编号
     * @return
     */
    public VendingChnStockData getStockByVidAndVcCode(String vendingId, String vendingChnCode) {
        VendingChnStockData vendingChnStock = null;
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        Cursor c = db.rawQuery(
                "SELECT VS1_ID,VS1_VD1_ID,VS1_VC1_CODE,VS1_PD1_ID,VS1_Quantity FROM VendingChnStock "
                        + "WHERE VS1_VD1_ID=? and VS1_VC1_CODE=? ORDER BY VS1_QUANTITY DESC limit 1",
                new String[] { vendingId, vendingChnCode });
        while (c.moveToNext()) {
            vendingChnStock = new VendingChnStockData();
            vendingChnStock.setVs1Id(c.getString(c.getColumnIndex("VS1_ID")));
            vendingChnStock.setVs1Vd1Id(c.getString(c.getColumnIndex("VS1_VD1_ID")));
            vendingChnStock.setVs1Vc1Code(c.getString(c.getColumnIndex("VS1_VC1_CODE")));
            vendingChnStock.setVs1Pd1Id(c.getString(c.getColumnIndex("VS1_PD1_ID")));
            vendingChnStock.setVs1Quantity(c.getInt(c.getColumnIndex("VS1_Quantity")));
            break;
        }
        return vendingChnStock;
    }

    /**
     * 根据SKUID串查询货机货道库存信息
     * 
     * @param skuIds
     *            多个SKUID连接组成的SKUID串
     * @return　
     */
    public List<VendingChnStockData> findStockBySkuId(String skuIds) {
        if (StringHelper.isEmpty(skuIds, true)) {
            return new ArrayList<VendingChnStockData>(0);
        }
        List<VendingChnStockData> list = new ArrayList<VendingChnStockData>();
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        Cursor c = db.rawQuery("SELECT * FROM VendingChnStock WHERE VS1_PD1_ID IN(" + skuIds
                + ") ORDER by TS1_VC1_CODE", null);
        while (c.moveToNext()) {
            VendingChnStockData vendingChnStock = new VendingChnStockData();
            vendingChnStock.setVs1Id(c.getString(c.getColumnIndex("VS1_ID")));
            vendingChnStock.setVs1M02Id(c.getString(c.getColumnIndex("VS1_M02_ID")));
            vendingChnStock.setVs1Vd1Id(c.getString(c.getColumnIndex("VS1_VD1_ID")));
            vendingChnStock.setVs1Vc1Code(c.getString(c.getColumnIndex("VS1_VC1_CODE")));
            vendingChnStock.setVs1Pd1Id(c.getString(c.getColumnIndex("VS1_PD1_ID")));
            vendingChnStock.setVs1Quantity(c.getInt(c.getColumnIndex("VS1_Quantity")));
            vendingChnStock.setVs1CreateUser(c.getString(c.getColumnIndex("VS1_CreateUser")));
            vendingChnStock.setVs1CreateTime(c.getString(c.getColumnIndex("VS1_CreateTime")));
            vendingChnStock.setVs1ModifyUser(c.getString(c.getColumnIndex("VS1_ModifyUser")));
            vendingChnStock.setVs1ModifyTime(c.getString(c.getColumnIndex("VS1_ModifyTime")));
            vendingChnStock.setVs1RowVersion(c.getString(c.getColumnIndex("VS1_RowVersion")));
            list.add(vendingChnStock);
        }
        return list;
    }

    //7.显示产品组合明细：“产品组合从表.SKUID”关联“产品.ID”表，显示“产品.产品名称”“产品组合从表.组合数量”

    /**
     * 增加售货机货道库存记录　单条
     * 
     * @param vendingChnStock
     * @return
     */
    public boolean addVendingChnStock(VendingChnStockData vendingChnStock) {
        String insertSql = "insert into VendingChnStock(VS1_ID,VS1_M02_ID,VS1_VD1_ID,VS1_VC1_CODE,VS1_PD1_ID,VS1_Quantity,VS1_CreateUser,VS1_CreateTime,VS1_ModifyUser,VS1_ModifyTime,VS1_RowVersion)"
                + "values(?,?,?,?,?,?,?,?,?,?,?)";
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        SQLiteStatement stat = db.compileStatement(insertSql);
        stat.bindString(1, vendingChnStock.getVs1Id());
        stat.bindString(2, vendingChnStock.getVs1M02Id());
        stat.bindString(3, vendingChnStock.getVs1Vd1Id());
        stat.bindString(4, vendingChnStock.getVs1Vc1Code());
        stat.bindString(5, vendingChnStock.getVs1Pd1Id());
        stat.bindLong(6, vendingChnStock.getVs1Quantity());
        stat.bindString(7, vendingChnStock.getVs1CreateUser());
        stat.bindString(8, vendingChnStock.getVs1CreateTime());
        stat.bindString(9, vendingChnStock.getVs1ModifyUser());
        stat.bindString(10, vendingChnStock.getVs1ModifyTime());
        stat.bindString(11, vendingChnStock.getVs1RowVersion());
        long i = stat.executeInsert();

        return i > 0;
    }

    /**
     * 批量增加售货机货道库存,用于同步数据
     * 
     * @param list
     */
    public boolean batchAddVendingChnStock(List<VendingChnStockData> list) {
        boolean flag = false;
        String insertSql = "insert into VendingChnStock(VS1_ID,VS1_M02_ID,VS1_VD1_ID,VS1_VC1_CODE,VS1_PD1_ID,VS1_Quantity,VS1_CreateUser,VS1_CreateTime,VS1_ModifyUser,VS1_ModifyTime,VS1_RowVersion)"
                + "values(?,?,?,?,?,?,?,?,?,?,?)";
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        try {
            //开启事务
            db.beginTransaction();
            for (VendingChnStockData vendingChnStock : list) {
                SQLiteStatement stat = db.compileStatement(insertSql);
                stat.bindString(1, vendingChnStock.getVs1Id());
                stat.bindString(2, vendingChnStock.getVs1M02Id());
                stat.bindString(3, vendingChnStock.getVs1Vd1Id());
                stat.bindString(4, vendingChnStock.getVs1Vc1Code());
                stat.bindString(5, vendingChnStock.getVs1Pd1Id());
                stat.bindLong(6, vendingChnStock.getVs1Quantity());
                stat.bindString(7, vendingChnStock.getVs1CreateUser());
                stat.bindString(8, vendingChnStock.getVs1CreateTime());
                stat.bindString(9, vendingChnStock.getVs1ModifyUser());
                stat.bindString(10, vendingChnStock.getVs1ModifyTime());
                stat.bindString(11, vendingChnStock.getVs1RowVersion());
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

    public Boolean batchUpdateVendingChnStock(List<VendingChnStockData> list) {
        boolean flag = false;
        String updateSql = "UPDATE VendingChnStock SET VS1_Quantity = VS1_Quantity + ? ,VS1_PD1_ID = ?"
                + "WHERE VS1_VD1_ID = ? and VS1_VC1_CODE = ?";
//        String updateSql = "UPDATE VendingChnStock SET VS1_Quantity = VS1_Quantity + ? "
//                + "WHERE VS1_VD1_ID = ? and VS1_VC1_CODE = ?  and VS1_PD1_ID = ? ";
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        try {
            //开启事务
            db.beginTransaction();
            for (VendingChnStockData vendingChnStock : list) {
                SQLiteStatement stat = db.compileStatement(updateSql);
                stat.bindLong(1, vendingChnStock.getVs1Quantity());
                stat.bindString(2, vendingChnStock.getVs1Pd1Id());
                stat.bindString(3, vendingChnStock.getVs1Vd1Id());
                stat.bindString(4, vendingChnStock.getVs1Vc1Code());
                stat.executeUpdateDelete();
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
     * 根据售货机ID,售货机货道编号,SKUID更新售货机库存数量
     * 
     * @param inputQty
     *            领料数量
     * @param vendingChn
     *            售货机货道编号
     * @return
     */
    public Boolean updateStockQuantity(int inputQty, VendingChnData vendingChn) {
        String updateSql = "UPDATE VendingChnStock SET VS1_Quantity = VS1_Quantity + ? "
                + "WHERE VS1_VD1_ID = ? and VS1_VC1_CODE = ? and VS1_PD1_ID = ? ";
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        SQLiteStatement stat = db.compileStatement(updateSql);
        stat.bindLong(1, inputQty);
        stat.bindString(2, vendingChn.getVc1Vd1Id());
        stat.bindString(3, vendingChn.getVc1Code());
        stat.bindString(4, vendingChn.getVc1Pd1Id());
        long i = stat.executeUpdateDelete();
        return i > 0;
    }

    /**
     * 删除所有的货道数据与货道库存数据
     * 
     * @return
     */
    public boolean deleteAll() {
        boolean flag = false;
        String chnSql = "DELETE FROM VendingChnStock";
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        try {
            //开启事务
            db.beginTransaction();
            SQLiteStatement stat = db.compileStatement(chnSql);
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
    
    public boolean batchDeleteVendingChnStock(List<VendingChnStockData> list) {
        boolean flag = false;
        String updateSql = "delete from VendingChnStock  WHERE VS1_VC1_CODE = ?";
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        try {
            //开启事务
            db.beginTransaction();
            for (VendingChnStockData vendingChnStock : list) {
                SQLiteStatement stat = db.compileStatement(updateSql);
                stat.bindString(1, vendingChnStock.getVs1Vc1Code());
                stat.executeUpdateDelete();
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

}
