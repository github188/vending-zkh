package com.mc.vending.db;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.mc.vending.data.CardData;
import com.mc.vending.tools.ZillionLog;

/**
 * 卡/密码 操作类
 * 
 * @Filename: CardDbOper.java
 * @Version: 1.0
 * @Author: fenghu
 * @Email: fenghu@mightcloud.com
 *
 */
public class CardDbOper {

	public List<CardData> findAll() {
		List<CardData> list = new ArrayList<CardData>();
		SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
		Cursor c = db.rawQuery("SELECT * FROM Card", null);
		while (c.moveToNext()) {
			CardData card = new CardData();
			card.setCd1Id(c.getString(c.getColumnIndex("CD1_ID")));
			card.setCd1M02Id(c.getString(c.getColumnIndex("CD1_M02_ID")));
			card.setCd1SerialNo(c.getString(c.getColumnIndex("CD1_SerialNo")));
			card.setCd1Code(c.getString(c.getColumnIndex("CD1_CODE")));
			card.setCd1Type(c.getString(c.getColumnIndex("CD1_Type")));
			card.setCd1Password(c.getString(c.getColumnIndex("CD1_Password")));
			card.setCd1Purpose(c.getString(c.getColumnIndex("CD1_Purpose")));
			card.setCd1Status(c.getString(c.getColumnIndex("CD1_Status")));
			card.setCd1CustomerStatus(c.getString(c.getColumnIndex("CD1_CustomerStatus")));
			// 添加产品权限
			card.setCd1ProductPower(c.getString(c.getColumnIndex("CD1_ProductPower")));
			card.setCd1CreateUser(c.getString(c.getColumnIndex("CD1_CreateUser")));
			card.setCd1CreateTime(c.getString(c.getColumnIndex("CD1_CreateTime")));
			card.setCd1ModifyUser(c.getString(c.getColumnIndex("CD1_ModifyUser")));
			card.setCd1ModifyTime(c.getString(c.getColumnIndex("CD1_ModifyTime")));
			card.setCd1RowVersion(c.getString(c.getColumnIndex("CD1_RowVersion")));
			list.add(card);
		}
		return list;
	}

	/**
	 * 根据卡/密码表中的卡序列号查询卡/密码记录,单条记录
	 * 
	 * @param cardId
	 * @return
	 */
	public CardData getCardById(String cardId) {
		CardData cardData = new CardData();
		SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
		Cursor c = db.rawQuery("SELECT cd1_serialNo,cd1_password FROM Card where cd1_id=?", new String[] { cardId });
		while (c.moveToNext()) {
			cardData.setCd1SerialNo(c.getString(c.getColumnIndex("CD1_SerialNo")));
			cardData.setCd1Password(c.getString(c.getColumnIndex("CD1_Password")));
		}
		return cardData;
	}

	/**
	 * 根据卡/密码表中的卡序列号查询卡/密码记录,单条记录
	 * 
	 * @param cardSerialNo
	 * @return
	 */
	public CardData getCardBySerialNo(String cardSerialNo) {
		CardData card = null;
		SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
		Cursor c = db.rawQuery(
				"SELECT CD1_ID,CD1_SerialNo,CD1_CODE,CD1_Type,CD1_Password,CD1_Purpose,CD1_Status,CD1_CustomerStatus,CD1_ProductPower FROM Card "
						+ "WHERE (CD1_SerialNo=? or CD1_Password=?) limit 1",
				new String[] { cardSerialNo, cardSerialNo });
		while (c.moveToNext()) {
			card = new CardData();
			card.setCd1Id(c.getString(c.getColumnIndex("CD1_ID")));
			card.setCd1SerialNo(c.getString(c.getColumnIndex("CD1_SerialNo")));
			card.setCd1Code(c.getString(c.getColumnIndex("CD1_CODE")));
			card.setCd1Type(c.getString(c.getColumnIndex("CD1_Type")));
			card.setCd1Password(c.getString(c.getColumnIndex("CD1_Password")));
			card.setCd1Purpose(c.getString(c.getColumnIndex("CD1_Purpose")));
			card.setCd1Status(c.getString(c.getColumnIndex("CD1_Status")));
			card.setCd1CustomerStatus(c.getString(c.getColumnIndex("CD1_CustomerStatus")));
			card.setCd1ProductPower(c.getString(c.getColumnIndex("CD1_ProductPower")));
			break;
		}
		return card;
	}

	/**
	 * 根据卡/密码表中的密码查询卡/密码记录,单条记录
	 * 
	 * @param password
	 * @return
	 */
	public CardData getCardByPassword(String password) {
		CardData card = null;
		SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
		Cursor c = db.rawQuery(
				"SELECT CD1_ID,CD1_SerialNo,CD1_CODE,CD1_Type,CD1_Password,CD1_Purpose,CD1_Status,CD1_CustomerStatus,CD1_ProductPower FROM Card WHERE CD1_Password=? limit 1",
				new String[] { password });
		while (c.moveToNext()) {
			card = new CardData();
			card.setCd1Id(c.getString(c.getColumnIndex("CD1_ID")));
			card.setCd1SerialNo(c.getString(c.getColumnIndex("CD1_SerialNo")));
			card.setCd1Code(c.getString(c.getColumnIndex("CD1_CODE")));
			card.setCd1Type(c.getString(c.getColumnIndex("CD1_Type")));
			card.setCd1Password(c.getString(c.getColumnIndex("CD1_Password")));
			card.setCd1Purpose(c.getString(c.getColumnIndex("CD1_Purpose")));
			card.setCd1Status(c.getString(c.getColumnIndex("CD1_Status")));
			card.setCd1CustomerStatus(c.getString(c.getColumnIndex("CD1_CustomerStatus")));
			card.setCd1ProductPower(c.getString(c.getColumnIndex("CD1_ProductPower")));
			break;
		}
		return card;
	}

	/**
	 * 增加卡/密码记录 单条
	 * 
	 * @param card
	 * @return
	 */
	public boolean addCard(CardData card) {
		String insertSql = "insert into Card(CD1_ID,CD1_M02_ID,CD1_SerialNo,CD1_CODE,CD1_Type,CD1_Password,"
				+ "CD1_Purpose,CD1_Status,CD1_CustomerStatus,CD1_CreateUser,CD1_CreateTime,"
				+ "CD1_ModifyUser,CD1_ModifyTime,CD1_RowVersion,CD1_ProductPower)"
				+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
		SQLiteStatement stat = db.compileStatement(insertSql);
		stat.bindString(1, card.getCd1Id());
		stat.bindString(2, card.getCd1M02Id());
		stat.bindString(3, card.getCd1SerialNo());
		stat.bindString(4, card.getCd1Code());
		stat.bindString(5, card.getCd1Type());
		stat.bindString(6, card.getCd1Password());
		stat.bindString(7, card.getCd1Purpose());
		stat.bindString(8, card.getCd1Status());
		stat.bindString(9, card.getCd1CustomerStatus());
		stat.bindString(10, card.getCd1CreateUser());
		stat.bindString(11, card.getCd1CreateTime());
		stat.bindString(12, card.getCd1ModifyUser());
		stat.bindString(13, card.getCd1ModifyTime());
		stat.bindString(14, card.getCd1RowVersion());
		stat.bindString(15, card.getCd1ProductPower());
		long i = stat.executeInsert();
		return i > 0;
	}

	/**
	 * 批量增加卡/密码数据,用于同步数据
	 * 
	 * @param list
	 */
	public boolean batchAddCard(List<CardData> list) {
		boolean flag = false;
		String insertSql = "insert into Card(CD1_ID,CD1_M02_ID,CD1_SerialNo,CD1_CODE,CD1_Type,CD1_Password,"
				+ "CD1_Purpose,CD1_Status,CD1_CustomerStatus,CD1_CreateUser,CD1_CreateTime,"
				+ "CD1_ModifyUser,CD1_ModifyTime,CD1_RowVersion,CD1_ProductPower)"
				+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
		try {
			// 开启事务
			db.beginTransaction();
			for (CardData card : list) {
				SQLiteStatement stat = db.compileStatement(insertSql);
				stat.bindString(1, card.getCd1Id());
				stat.bindString(2, card.getCd1M02Id());
				stat.bindString(3, card.getCd1SerialNo());
				stat.bindString(4, card.getCd1Code());
				stat.bindString(5, card.getCd1Type());
				stat.bindString(6, card.getCd1Password());
				stat.bindString(7, card.getCd1Purpose());
				stat.bindString(8, card.getCd1Status());
				stat.bindString(9, card.getCd1CustomerStatus());
				stat.bindString(10, card.getCd1CreateUser());
				stat.bindString(11, card.getCd1CreateTime());
				stat.bindString(12, card.getCd1ModifyUser());
				stat.bindString(13, card.getCd1ModifyTime());
				stat.bindString(14, card.getCd1RowVersion());
				stat.bindString(15, card.getCd1ProductPower());
				stat.executeInsert();
			}
			// 数据插入成功，设置事物成功标志
			db.setTransactionSuccessful();
			// 保存数据
			db.endTransaction();
			flag = true;
		} catch (SQLException e) {
			// 结束事物，在这里没有设置成功标志，结束后不保存
			ZillionLog.e(this.getClass().getName(), e.getMessage(), e);
			db.endTransaction();
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * 批量增加卡/密码数据,用于同步数据
	 * 
	 * @param list
	 */
	public boolean batchDeleteCard(List<CardData> list) {
		boolean flag = false;
		SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
		try {
			// 开启事务
			db.beginTransaction();
			for (CardData card : list) {
				db.delete("Card", "cd1_id=?", new String[] { card.getCd1Id() });
			}
			// 数据成功，设置事物成功标志
			db.setTransactionSuccessful();
			// 保存数据
			db.endTransaction();
			flag = true;
		} catch (SQLException e) {
			// 结束事物，在这里没有设置成功标志，结束后不保存
			ZillionLog.e(this.getClass().getName(), e.getMessage(), e);
			db.endTransaction();
			e.printStackTrace();
		}
		return flag;
	}

	public boolean deleteAll() {
		boolean flag = false;
		String deleteSql = "DELETE FROM Card";
		SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
		try {
			// 开启事务
			db.beginTransaction();
			SQLiteStatement stat = db.compileStatement(deleteSql);
			stat.executeInsert();
			// 数据插入成功，设置事物成功标志
			db.setTransactionSuccessful();
			// 保存数据
			db.endTransaction();
			flag = true;
		} catch (SQLException e) {
			ZillionLog.e(this.getClass().getName(), e.getMessage(), e);
			// 结束事物，在这里没有设置成功标志，结束后不保存
			db.endTransaction();
			e.printStackTrace();
		}
		return flag;
	}
}
