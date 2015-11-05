package com.mc.vending.db;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.mc.vending.data.ProductCardPowerData;
import com.mc.vending.tools.ZillionLog;

/**
 * 卡与产品权限　操作类
 * 
 * @author Forever
 * @date 2015年9月9日
 * @email forever.wang@zillionstar.com
 */
public class ProductCardPowerDbOper {

    public List<ProductCardPowerData> findAll() {
        List<ProductCardPowerData> list = new ArrayList<ProductCardPowerData>();
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        Cursor c = db.rawQuery("SELECT * FROM ProductCardPower", null);
        while (c.moveToNext()) {
            ProductCardPowerData card = new ProductCardPowerData();
            card.setPc1ID(c.getString(c.getColumnIndex("PC1_ID")));
            card.setPc1M02_ID(c.getString(c.getColumnIndex("PC1_M02_ID")));
            card.setPc1VD1_ID(c.getString(c.getColumnIndex("PC1_VD1_ID")));
            card.setPc1CU1_ID(c.getString(c.getColumnIndex("PC1_CU1_ID")));
            card.setPc1CD1_ID(c.getString(c.getColumnIndex("PC1_CD1_ID")));
            card.setPc1VP1_ID(c.getString(c.getColumnIndex("PC1_VP1_ID")));
            card.setPc1Power(c.getString(c.getColumnIndex("PC1_Power")));
            card.setPc1OnceQty(c.getString(c.getColumnIndex("PC1_OnceQty")));
            card.setPc1Period(c.getString(c.getColumnIndex("PC1_Period")));
            card.setPc1IntervalStart(c.getString(c.getColumnIndex("PC1_IntervalStart")));
            card.setPc1IntervalFinish(c.getString(c.getColumnIndex("PC1_IntervalFinish")));
            card.setPc1StartDate(c.getString(c.getColumnIndex("PC1_StartDate")));
            card.setPc1PeriodQty(c.getString(c.getColumnIndex("PC1_PeriodQty")));
            card.setCreateUser(c.getString(c.getColumnIndex("CreateUser")));
            card.setCreateTime(c.getString(c.getColumnIndex("CreateTime")));
            card.setModifyUser(c.getString(c.getColumnIndex("ModifyUser")));
            card.setModifyTime(c.getString(c.getColumnIndex("ModifyTime")));
            card.setRowVersion(c.getString(c.getColumnIndex("RowVersion")));
            list.add(card);
        }
        return list;
    }

    /**
     * 根据卡与产品权限表中的卡ID查询卡与产品权限记录,单条记录
     * 
     * @param cardSerialNo
     * @return
     */
    public ProductCardPowerData getProductCardPowerBySerialNo(String cardId) {
        ProductCardPowerData card = null;
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        Cursor c = db.rawQuery("SELECT * FROM ProductCardPower " + "WHERE PC1_CD1_ID=? limit 1",
                new String[] { cardId });
        while (c.moveToNext()) {
            card = new ProductCardPowerData();
            card.setPc1ID(c.getString(c.getColumnIndex("PC1_ID")));
            card.setPc1M02_ID(c.getString(c.getColumnIndex("PC1_M02_ID")));
            card.setPc1VD1_ID(c.getString(c.getColumnIndex("PC1_VD1_ID")));
            card.setPc1CU1_ID(c.getString(c.getColumnIndex("PC1_CU1_ID")));
            card.setPc1CD1_ID(c.getString(c.getColumnIndex("PC1_CD1_ID")));
            card.setPc1VP1_ID(c.getString(c.getColumnIndex("PC1_VP1_ID")));
            card.setPc1Power(c.getString(c.getColumnIndex("PC1_Power")));
            card.setPc1OnceQty(c.getString(c.getColumnIndex("PC1_OnceQty")));
            card.setPc1Period(c.getString(c.getColumnIndex("PC1_Period")));
            card.setPc1IntervalStart(c.getString(c.getColumnIndex("PC1_IntervalStart")));
            card.setPc1IntervalFinish(c.getString(c.getColumnIndex("PC1_IntervalFinish")));
            card.setPc1StartDate(c.getString(c.getColumnIndex("PC1_StartDate")));
            card.setPc1PeriodQty(c.getString(c.getColumnIndex("PC1_PeriodQty")));
            card.setCreateUser(c.getString(c.getColumnIndex("CreateUser")));
            card.setCreateTime(c.getString(c.getColumnIndex("CreateTime")));
            card.setModifyUser(c.getString(c.getColumnIndex("ModifyUser")));
            card.setModifyTime(c.getString(c.getColumnIndex("ModifyTime")));
            card.setRowVersion(c.getString(c.getColumnIndex("RowVersion")));
            break;
        }
        return card;
    }

    String insertSql = "insert into ProductCardPower (PC1_ID,PC1_VD1_ID,PC1_M02_ID,PC1_CU1_ID,PC1_CD1_ID,PC1_VP1_ID,"
                             + "PC1_Power,PC1_OnceQty,PC1_Period,PC1_IntervalStart,PC1_IntervalFinish,"
                             + "PC1_StartDate,PC1_PeriodQty,CreateUser,CreateTime,ModifyUser,ModifyTime,RowVersion)"
                             + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

    /**
     * 增加卡与产品权限记录　单条　
     * 
     * @param card
     * @return
     */
    public boolean addProductCardPower(ProductCardPowerData card) {
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        SQLiteStatement stat = db.compileStatement(insertSql);
        int i = 1;
        stat.bindString(i++, card.getPc1ID());
        stat.bindString(i++, card.getPc1VD1_ID());
        stat.bindString(i++, card.getPc1M02_ID());
        stat.bindString(i++, card.getPc1CU1_ID());
        stat.bindString(i++, card.getPc1CD1_ID());
        stat.bindString(i++, card.getPc1VP1_ID());
        stat.bindString(i++, card.getPc1Power());
        stat.bindString(i++, card.getPc1OnceQty());
        stat.bindString(i++, card.getPc1Period());
        stat.bindString(i++, card.getPc1IntervalStart());
        stat.bindString(i++, card.getPc1IntervalFinish());
        stat.bindString(i++, card.getPc1StartDate());
        stat.bindString(i++, card.getPc1PeriodQty());
        stat.bindString(i++, card.getCreateUser());
        stat.bindString(i++, card.getCreateTime());
        stat.bindString(i++, card.getModifyUser());
        stat.bindString(i++, card.getModifyTime());
        stat.bindString(i++, card.getRowVersion());

        long r = stat.executeInsert();
        return r > 0;
    }

    /**
     * 批量增加卡与产品权限数据,用于同步数据
     * 
     * @param list
     */
    public boolean batchAddProductCardPower(List<ProductCardPowerData> list) {
        boolean flag = false;
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        try {
            // 开启事务
            db.beginTransaction();
            for (ProductCardPowerData card : list) {
                SQLiteStatement stat = db.compileStatement(insertSql);

                int i = 1;
                stat.bindString(i++, card.getPc1ID());
                stat.bindString(i++, card.getPc1VD1_ID());
                stat.bindString(i++, card.getPc1M02_ID());
                stat.bindString(i++, card.getPc1CU1_ID());
                stat.bindString(i++, card.getPc1CD1_ID());
                stat.bindString(i++, card.getPc1VP1_ID());
                stat.bindString(i++, card.getPc1Power());
                stat.bindString(i++, card.getPc1OnceQty());
                stat.bindString(i++, card.getPc1Period());
                stat.bindString(i++, card.getPc1IntervalStart());
                stat.bindString(i++, card.getPc1IntervalFinish());
                stat.bindString(i++, card.getPc1StartDate());
                stat.bindString(i++, card.getPc1PeriodQty());
                stat.bindString(i++, card.getCreateUser());
                stat.bindString(i++, card.getCreateTime());
                stat.bindString(i++, card.getModifyUser());
                stat.bindString(i++, card.getModifyTime());
                stat.bindString(i++, card.getRowVersion());
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

    public boolean batchDeleteVendingCard(List<String> dbPra) {
        boolean flag = false;
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        try {
            // 开启事务
            db.beginTransaction();
            for (String string : dbPra) {
                String deleteSql = "DELETE FROM ProductCardPower where PC1_CD1_ID='" + string.split(",")[0]
                        + "' and PC1_VD1_ID='" + string.split(",")[1] + "'";
                SQLiteStatement stat = db.compileStatement(deleteSql);
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

    public ProductCardPowerData getVendingProLinkByVidAndSkuId(String cardId, String vp1Id) {
        ProductCardPowerData data = null;
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        Cursor c = db
                .rawQuery(
                        "SELECT PC1_ID,PC1_CU1_ID,PC1_CD1_ID,PC1_VP1_ID,PC1_Power,PC1_OnceQty,PC1_Period,PC1_IntervalStart,PC1_IntervalFinish,PC1_StartDate,PC1_PeriodQty "
                                + "FROM ProductCardPower WHERE PC1_CD1_ID=? and PC1_VP1_ID=? limit 1",
                        new String[] { cardId, vp1Id });
        while (c.moveToNext()) {
            data = new ProductCardPowerData();
            data.setPc1ID(c.getString(c.getColumnIndex("PC1_ID")));
            data.setPc1CU1_ID(c.getString(c.getColumnIndex("PC1_CU1_ID")));
            data.setPc1CD1_ID(c.getString(c.getColumnIndex("PC1_CD1_ID")));
            data.setPc1VP1_ID(c.getString(c.getColumnIndex("PC1_VP1_ID")));
            data.setPc1Power(c.getString(c.getColumnIndex("PC1_Power")));
            data.setPc1OnceQty(c.getString(c.getColumnIndex("PC1_OnceQty")));
            data.setPc1Period(c.getString(c.getColumnIndex("PC1_Period")));
            data.setPc1IntervalStart(c.getString(c.getColumnIndex("PC1_IntervalStart")));
            data.setPc1IntervalFinish(c.getString(c.getColumnIndex("PC1_IntervalFinish")));
            data.setPc1StartDate(c.getString(c.getColumnIndex("PC1_StartDate")));
            data.setPc1PeriodQty(c.getString(c.getColumnIndex("PC1_PeriodQty")));
            break;
        }
        return data;
    }

    /**
     * 获取卡产品权限表中 卡下的产品
     * @param cardId
     * @return
     */
    public List<String> getVendingProLinkByCid(String cardId) {
        List<String> data = new ArrayList<String>();
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        Cursor c = db
                .rawQuery(
                        "SELECT PC1_VP1_ID FROM ProductCardPower WHERE PC1_CD1_ID=?",
                        new String[] { cardId });
        while (c.moveToNext()) {
            data.add(c.getString(c.getColumnIndex("PC1_VP1_ID")));
            
        }
        return data;
    }
}
