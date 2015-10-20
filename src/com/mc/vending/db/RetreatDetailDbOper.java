package com.mc.vending.db;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mc.vending.data.RetreatDetailData;
import com.mc.vending.data.VendingChnData;
import com.mc.vending.data.VendingChnProductWrapperData;

public class RetreatDetailDbOper {

    /**
     * 查询所有的记录列表数据
     * 
     * @return
     */
    public List<VendingChnProductWrapperData> findVendingChnProductWrapperDataByRtId(String rt1Id) {
        List<VendingChnProductWrapperData> list = new ArrayList<VendingChnProductWrapperData>();
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        Cursor c = db.rawQuery("SELECT p.PD1_Name,r.Rt2_PlanQty,c.*,s.vs1_quantity "
                + "FROM RetreatDetail r,Product p,VendingChn c ,vendingchnstock s "
                + "WHERE c.VC1_CODE=r.RT2_VC1_Code and p.PD1_Id=r.RT2_PD1_Id and r.Rt2_rt1_id=? "
                + "and c.VC1_CODE=s.VS1_vc1_code and p.PD1_Id=s.vs1_PD1_Id and s.vs1_vd1_id=c.VC1_VD1_ID",
                new String[] { rt1Id });
        while (c.moveToNext()) {
            VendingChnProductWrapperData data = new VendingChnProductWrapperData();
            VendingChnData vendingChn = new VendingChnData();
            vendingChn.setVc1Id(c.getString(c.getColumnIndex("VC1_ID")));
            vendingChn.setVc1Vd1Id(c.getString(c.getColumnIndex("VC1_VD1_ID")));
            vendingChn.setVc1Code(c.getString(c.getColumnIndex("VC1_CODE")));
            vendingChn.setVc1Type(c.getString(c.getColumnIndex("VC1_Type")));
            vendingChn.setVc1Pd1Id(c.getString(c.getColumnIndex("VC1_PD1_ID")));
            vendingChn.setVc1SaleType(c.getString(c.getColumnIndex("VC1_SaleType")));
            vendingChn.setVc1Sp1Id(c.getString(c.getColumnIndex("VC1_SP1_ID")));
            vendingChn.setVc1BorrowStatus(c.getString(c.getColumnIndex("VC1_BorrowStatus")));
            vendingChn.setVc1Status(c.getString(c.getColumnIndex("VC1_Status")));
            vendingChn.setVc1LineNum(c.getString(c.getColumnIndex("VC1_LineNum")));
            vendingChn.setVc1ColumnNum(c.getString(c.getColumnIndex("VC1_ColumnNum")));
            vendingChn.setVc1Height(c.getString(c.getColumnIndex("VC1_Height")));

            data.setVendingChn(vendingChn);

            data.setProductName(c.getString(c.getColumnIndex("PD1_Name")));
            data.setActQty(c.getInt(c.getColumnIndex("RT2_PlanQty")));
            data.setStock(c.getInt(c.getColumnIndex("VS1_Quantity")));
            list.add(data);
        }
        return list;
    }

    public List<RetreatDetailData> findRetreatDetailDataByRtId(String rt1Id) {
        List<RetreatDetailData> list = new ArrayList<RetreatDetailData>();
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        Cursor c = db.rawQuery("SELECT * from RetreatDetail WHERE Rt2_rt1_id=?", new String[] { rt1Id });
        while (c.moveToNext()) {
            RetreatDetailData data = new RetreatDetailData();

            data.setRt2ActualQty(c.getInt(c.getColumnIndex("RT2_ActualQty")));
            data.setRt2Id(c.getString(c.getColumnIndex("RT2_ID")));
            data.setRt2M02Id(c.getString(c.getColumnIndex("RT2_M02_ID")));
            data.setRt2Pd1Id(c.getString(c.getColumnIndex("RT2_PD1_ID")));
            data.setRt2ActualQty(c.getInt(c.getColumnIndex("RT2_ActualQty")));
            data.setRt2PlanQty(c.getInt(c.getColumnIndex("RT2_PlanQty")));
            data.setRt2Rt1Id(c.getString(c.getColumnIndex("RT2_RT1_ID")));
            data.setRt2SaleType(c.getString(c.getColumnIndex("RT2_SaleType")));
            data.setRt2Sp1Id(c.getString(c.getColumnIndex("RT2_SP1_ID")));
            data.setRt2Vc1Code(c.getString(c.getColumnIndex("RT2_VC1_CODE")));

            list.add(data);
        }
        return list;
    }
}
