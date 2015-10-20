/**
 * 
 */
package com.mc.vending.db;

import android.database.sqlite.SQLiteDatabase;

import com.zillionstar.tools.ZillionLog;

/**
 * @author Forever
 * @date 2015年7月27日
 * @email forever.wang@zillionstar.com
 */
public class VersionDbOper {
    private String checkClumn = "PRAGMA table_info (table_name)";

    public static void exec(String sql) {
        try {

            ZillionLog.i("VersionDbOper", sql);
            SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
            db.execSQL(sql);
        } catch (Exception e) {
            ZillionLog.e("VersionDbOper", e.getMessage());
        }
    }
}
