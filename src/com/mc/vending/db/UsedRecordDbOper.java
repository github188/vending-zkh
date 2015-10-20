package com.mc.vending.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.mc.vending.data.ConversionData;
import com.mc.vending.data.UsedRecordData;
import com.mc.vending.tools.ConvertHelper;

/**
 * 卡与产品领用 操作类
 * <table border="0" cellpadding="0" cellspacing="0" width="1309" style=
 * "width:983pt;" class="ke-zeroborder">
 * <tbody>
 * <tr>
 * <td rowspan="11" height="242" class="xl71" width="90">卡与产品领用</td>
 * <td class="xl70" width="193">UsedRecord</td>
 * <td class="xl67" width="123">ID</td>
 * <td class="xl67" width="114">B03_TRANS</td>
 * <td class="xl68" width="57">UR1_</td>
 * <td class="xl68" width="97">ID</td>
 * <td class="xl67" width="144">UR1_ID</td>
 * <td class="xl67" width="44">是</td>
 * <td class="xl67" width="44"></td>
 * <td class="xl67" width="44"></td>
 * <td class="xl67" width="42"></td>
 * <td class="xl67" width="51">GUID</td>
 * <td class="xl67" width="51"></td>
 * <td class="xl67" width="215"></td>
 * </tr>
 * <tr>
 * <td height="22" class="xl70">UsedRecord</td>
 * <td class="xl67">事业体ID</td>
 * <td class="xl67">B03_TRANS</td>
 * <td class="xl68">UR1_</td>
 * <td class="xl68">M02_ID</td>
 * <td class="xl67">UR1_M02_ID</td>
 * <td class="xl67"></td>
 * <td class="xl67"></td>
 * <td class="xl67"></td>
 * <td class="xl67"></td>
 * <td class="xl67">GUID</td>
 * <td class="xl67"></td>
 * <td class="xl67"></td>
 * </tr>
 * <tr>
 * <td height="22" class="xl70">UsedRecord</td>
 * <td class="xl67">卡ID</td>
 * <td class="xl67">B03_TRANS</td>
 * <td class="xl68">UR1_</td>
 * <td class="xl68">CD1_ID</td>
 * <td class="xl67">UR1_CD1_ID</td>
 * <td class="xl67"></td>
 * <td class="xl67"></td>
 * <td class="xl67"></td>
 * <td class="xl67"></td>
 * <td class="xl67">GUID</td>
 * <td class="xl67"></td>
 * <td class="xl67"></td>
 * </tr>
 * <tr>
 * <td height="22" class="xl70">UsedRecord</td>
 * <td class="xl67">售货机ID</td>
 * <td class="xl67">B03_TRANS</td>
 * <td class="xl68">UR1_</td>
 * <td class="xl68">VD1_ID</td>
 * <td class="xl67">UR1_VD1_ID</td>
 * <td class="xl67"></td>
 * <td class="xl67"></td>
 * <td class="xl67"></td>
 * <td class="xl67"></td>
 * <td class="xl67">GUID</td>
 * <td class="xl67"></td>
 * <td class="xl67"></td>
 * </tr>
 * <tr>
 * <td height="22" class="xl70">UsedRecord</td>
 * <td class="xl67">SKUID</td>
 * <td class="xl67">B03_TRANS</td>
 * <td class="xl68">UR1_</td>
 * <td class="xl68">PD1_ID</td>
 * <td class="xl67">UR1_PD1_ID</td>
 * <td class="xl67"></td>
 * <td class="xl67"></td>
 * <td class="xl67"></td>
 * <td class="xl67"></td>
 * <td class="xl67">GUID</td>
 * <td class="xl67"></td>
 * <td class="xl67">产品表</td>
 * </tr>
 * <tr>
 * <td height="22" class="xl70">UsedRecord</td>
 * <td class="xl67">领用数量</td>
 * <td class="xl67">B03_TRANS</td>
 * <td class="xl68">UR1_</td>
 * <td class="xl68">Quantity</td>
 * <td class="xl67">UR1_Quantity</td>
 * <td class="xl67"></td>
 * <td class="xl67"></td>
 * <td class="xl67"></td>
 * <td class="xl67"></td>
 * <td class="xl67">数字</td>
 * <td class="xl67">18,4</td>
 * <td class="xl67"></td>
 * </tr>
 * <tr>
 * <td height="22" class="xl70">UsedRecord</td>
 * <td class="xl67">创建人</td>
 * <td class="xl67">B03_TRANS</td>
 * <td class="xl68">UR1_</td>
 * <td class="xl68">CreateUser</td>
 * <td class="xl67">UR1_CreateUser</td>
 * <td class="xl67"></td>
 * <td class="xl67"></td>
 * <td class="xl67"></td>
 * <td class="xl67"></td>
 * <td class="xl67">文本</td>
 * <td class="xl67" align="right">50</td>
 * <td class="xl67"></td>
 * </tr>
 * <tr>
 * <td height="22" class="xl70">UsedRecord</td>
 * <td class="xl67">创建时间</td>
 * <td class="xl67">B03_TRANS</td>
 * <td class="xl68">UR1_</td>
 * <td class="xl68">CreateTime</td>
 * <td class="xl67">UR1_CreateTime</td>
 * <td class="xl67"></td>
 * <td class="xl67"></td>
 * <td class="xl67"></td>
 * <td class="xl67"></td>
 * <td class="xl67">日期</td>
 * <td class="xl67"></td>
 * <td class="xl67"></td>
 * </tr>
 * <tr>
 * <td height="22" class="xl70">UsedRecord</td>
 * <td class="xl67">最后修改人</td>
 * <td class="xl67">B03_TRANS</td>
 * <td class="xl68">UR1_</td>
 * <td class="xl68">ModifyUser</td>
 * <td class="xl67">UR1_ModifyUser</td>
 * <td class="xl67"></td>
 * <td class="xl67"></td>
 * <td class="xl67"></td>
 * <td class="xl67"></td>
 * <td class="xl67">文本</td>
 * <td class="xl67" align="right">50</td>
 * <td class="xl67"></td>
 * </tr>
 * <tr>
 * <td height="22" class="xl70">UsedRecord</td>
 * <td class="xl67">最后修改时间</td>
 * <td class="xl67">B03_TRANS</td>
 * <td class="xl68">UR1_</td>
 * <td class="xl68">ModifyTime</td>
 * <td class="xl67">UR1_ModifyTime</td>
 * <td class="xl67"></td>
 * <td class="xl67"></td>
 * <td class="xl67"></td>
 * <td class="xl67"></td>
 * <td class="xl67">日期</td>
 * <td class="xl67"></td>
 * <td class="xl67"></td>
 * </tr>
 * <tr>
 * <td height="22" class="xl70">UsedRecord</td>
 * <td class="xl67">时间戳</td>
 * <td class="xl67">B03_TRANS</td>
 * <td class="xl68">UR1_</td>
 * <td class="xl68">RowVersion</td>
 * <td class="xl67">UR1_RowVersion</td>
 * <td class="xl67"></td>
 * <td class="xl67"></td>
 * <td class="xl67"></td>
 * <td class="xl67"></td>
 * <td class="xl67">timestamp</td>
 * <td class="xl67"></td>
 * <td class="xl67"></td>
 * </tr>
 * </tbody>
 * </table>
 * 
 * @author Forever
 * @date 2015年9月9日
 * @email forever.wang@zillionstar.com
 */

public class UsedRecordDbOper {

	public List<UsedRecordData> findAll() {
		List<UsedRecordData> list = new ArrayList<UsedRecordData>();
		SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
		Cursor c = db.rawQuery("SELECT * FROM UsedRecord", null);
		while (c.moveToNext()) {
			UsedRecordData data = new UsedRecordData();
			data.setUr1ID(c.getString(c.getColumnIndex("UR1_ID")));
			data.setUr1M02_ID(c.getString(c.getColumnIndex("UR1_M02_ID")));
			data.setUr1CD1_ID(c.getString(c.getColumnIndex("UR1_CD1_ID")));
			data.setUr1VD1_ID(c.getString(c.getColumnIndex("UR1_VD1_ID")));
			data.setUr1PD1_ID(c.getString(c.getColumnIndex("UR1_PD1_ID")));
			data.setUr1Quantity(c.getString(c.getColumnIndex("UR1_Quantity")));

			data.setCreateUser(c.getString(c.getColumnIndex("CreateUser")));
			data.setCreateTime(c.getString(c.getColumnIndex("CreateTime")));
			data.setModifyUser(c.getString(c.getColumnIndex("ModifyUser")));
			data.setModifyTime(c.getString(c.getColumnIndex("ModifyTime")));
			data.setRowVersion(c.getString(c.getColumnIndex("RowVersion")));
			list.add(data);
		}
		return list;
	}

	public Map<String, String> findAllUsed() {
		SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
		Cursor c = db.rawQuery("SELECT * FROM UsedRecord", null);
		Map<String, String> reMap = new HashMap<String, String>();
		while (c.moveToNext()) {
			reMap.put(c.getString(c.getColumnIndex("UR1_ID")), c.getString(c.getColumnIndex("UR1_CD1_ID")));
		}
		return reMap;
	}

	public List<UsedRecordData> findDataToUpload() {
		List<UsedRecordData> list = new ArrayList<UsedRecordData>();
		SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
		Cursor c = db.rawQuery("SELECT * FROM UsedRecord where UR1_CD1_ID", null);
		while (c.moveToNext()) {
			UsedRecordData data = new UsedRecordData();
			data.setUr1ID(c.getString(c.getColumnIndex("UR1_ID")));
			data.setUr1M02_ID(c.getString(c.getColumnIndex("UR1_M02_ID")));
			data.setUr1CD1_ID(c.getString(c.getColumnIndex("UR1_CD1_ID")));
			data.setUr1VD1_ID(c.getString(c.getColumnIndex("UR1_VD1_ID")));
			data.setUr1PD1_ID(c.getString(c.getColumnIndex("UR1_PD1_ID")));
			data.setUr1Quantity(c.getString(c.getColumnIndex("UR1_Quantity")));

			data.setCreateUser(c.getString(c.getColumnIndex("CreateUser")));
			data.setCreateTime(c.getString(c.getColumnIndex("CreateTime")));
			data.setModifyUser(c.getString(c.getColumnIndex("ModifyUser")));
			data.setModifyTime(c.getString(c.getColumnIndex("ModifyTime")));
			data.setRowVersion(c.getString(c.getColumnIndex("RowVersion")));
			list.add(data);
		}
		return list;
	}

	/**
	 * 根据卡与产品领用表中的卡序列号查询卡与产品领用记录,单条记录
	 * 
	 * @param cardSerialNo
	 * @return
	 */
	public int getTransQtyCount(String cardId, String skuId) {
		int count = 0;
		String proportion = "1";// 换算比例
		SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
		Cursor c = db.rawQuery(
				"SELECT max(UR1_Quantity) as UR1_Quantity FROM UsedRecord WHERE UR1_CD1_ID=? and UR1_PD1_ID =?",
				new String[] { cardId, skuId });
		while (c.moveToNext()) {
			String a = c.getString(c.getColumnIndex("UR1_Quantity"));
			count = Double.valueOf(a == null ? "0" : a).intValue();
			break;
		}
		ConversionDbOper conversionDbOper = new ConversionDbOper();
		ConversionData conversionData = conversionDbOper.findConversionByCpid(skuId);// 根据"关联产品ID"查询"单位换算关系表"中有无该产品的换算关系
		if (conversionData != null) {
			proportion = conversionData.getCn1Proportion();
			count = count * ConvertHelper.toInt(proportion, 1);
			// 最终结果要进行单位换算
			// modified
			// by
			// junjie.you
		}
		return count;
	}

	String insertSql = "insert into UsedRecord(UR1_ID,UR1_M02_ID,UR1_CD1_ID,UR1_VD1_ID,UR1_PD1_ID,UR1_Quantity,"
			+ "CreateUser,CreateTime, ModifyUser,ModifyTime,RowVersion)" + "values(?,?,?,?,?,?,?,?,?,?,?)";

	/**
	 * 增加卡与产品领用记录 单条
	 * 
	 * @param data
	 * @return
	 */
	public boolean addUsedRecord(UsedRecordData data) {

		SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
		SQLiteStatement stat = db.compileStatement(insertSql);
		int i = 1;
		stat.bindString(i++, data.getUr1ID());
		stat.bindString(i++, data.getUr1M02_ID());
		stat.bindString(i++, data.getUr1CD1_ID());
		stat.bindString(i++, data.getUr1VD1_ID());
		stat.bindString(i++, data.getUr1PD1_ID());
		stat.bindString(i++, data.getUr1Quantity());

		stat.bindString(i++, data.getCreateUser());
		stat.bindString(i++, data.getCreateTime());
		stat.bindString(i++, data.getModifyUser());
		stat.bindString(i++, data.getModifyTime());
		stat.bindString(i++, data.getRowVersion());
		long r = stat.executeInsert();
		return r > 0;
	}

	/**
	 * 批量增加卡与产品领用数据,用于同步数据
	 * 
	 * @param list
	 */
	public boolean batchAddUsedRecord(List<UsedRecordData> list) {
		boolean flag = false;
		SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
		try {
			// 开启事务
			db.beginTransaction();
			for (UsedRecordData data : list) {
				SQLiteStatement stat = db.compileStatement(insertSql);
				int i = 1;
				stat.bindString(i++, data.getUr1ID());
				stat.bindString(i++, data.getUr1M02_ID());
				stat.bindString(i++, data.getUr1CD1_ID());
				stat.bindString(i++, data.getUr1VD1_ID());
				stat.bindString(i++, data.getUr1PD1_ID());
				stat.bindString(i++, data.getUr1Quantity());

				stat.bindString(i++, data.getCreateUser());
				stat.bindString(i++, data.getCreateTime());
				stat.bindString(i++, data.getModifyUser());
				stat.bindString(i++, data.getModifyTime());
				stat.bindString(i++, data.getRowVersion());
				stat.executeInsert();
			}
			// 数据插入成功，设置事物成功标志
			db.setTransactionSuccessful();
			// 保存数据
			db.endTransaction();
			flag = true;
		} catch (SQLException e) {
			// 结束事物，在这里没有设置成功标志，结束后不保存
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
				String deleteSql = "DELETE FROM UsedRecord where UR1_CD1_ID='" + string.split(",")[0]
						+ "' and UR1_VD1_ID='" + string.split(",")[1] + "'";
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
			db.endTransaction();
			e.printStackTrace();
		}
		return flag;
	}
}
