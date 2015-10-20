/**
 * 
 */
package com.mc.vending.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mc.vending.data.ConversionData;
import com.mc.vending.data.StockTransactionData;

/**
 * @author junjie.you
 *
 */
public class ConversionDbOper {

	/**
	 * 根据"关联产品ID"查询"单位换算关系表"中有无该产品的换算关系
	 * 
	 * @param cn1Cpid 关联产品ID
	 * @return 有则查看Proportion(换算比例,如:12)和Operation(操作方式,如:打); 无则返回NULL
	 */
	public ConversionData findConversionByCpid(String cn1Cpid)
	{
		ConversionData conversionData = null;
		SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        Cursor c = db
                .rawQuery(
                        "SELECT * FROM Conversion WHERE CN1_Cpid = ? ORDER by CN1_CreateTime desc limit 1",
                        new String[] { cn1Cpid });
        while (c.moveToNext()) {
            conversionData = new ConversionData();
            conversionData.setCn1Id(c.getString(c.getColumnIndex("CN1_ID")));
            conversionData.setCn1Upid(c.getString(c.getColumnIndex("CN1_Upid")));
            conversionData.setCn1Cpid(c.getString(c.getColumnIndex("CN1_Cpid")));
            conversionData.setCn1Proportion(c.getString(c.getColumnIndex("CN1_Proportion")));
            conversionData.setCn1Operation(c.getString(c.getColumnIndex("CN1_Operation")));
            conversionData.setCn1CreateUser(c.getString(c.getColumnIndex("CN1_CreateUser")));
            conversionData.setCn1CreateTime(c.getString(c.getColumnIndex("CN1_CreateTime")));
            conversionData.setCn1ModifyUser(c.getString(c.getColumnIndex("CN1_ModifyUser")));
            conversionData.setCn1ModifyTime(c.getString(c.getColumnIndex("CN1_ModifyTime")));
            conversionData.setCn1RowVersion(c.getString(c.getColumnIndex("CN1_RowVersion")));
            break;
        }
		return conversionData;
	}

}
