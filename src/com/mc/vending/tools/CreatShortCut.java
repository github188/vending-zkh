/**  
 * Filename:    Parameter.java     
 * Copyright:   Copyright (c)2011 
 * @author:     张晨 
 * @version:    1.0.0
 * Create at:   2011-09-30
 *  
 * Modification History:  
 * Date         Author      Version     Description  
 * ------------------------------------------------------------------  
 * 2011-09-30    张晨             1.0.0        1.0.0 Version  
 */
package com.mc.vending.tools;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;


/**
 * 功能：此类用做应用程序创建和删除手机桌面快捷方式，代码来源于网络，本人只是做了一些整理工作
 * 
 * 提示：别忘了在AndroidManifest.xml设置创建快捷方式权限，不然程序会出现crash现象 <uses-permission
 * android:name="com.android.launcher.permission.INSTALL_SHORTCUT" />
 * <uses-permission
 * android:name="com.android.launcher.permission.UNINSTALL_SHORTCUT" />
 * <uses-permission android:name="com.android.launcher.permission.READ_SETTINGS"
 * />
 * 
 * @author longsha
 */

public class CreatShortCut {

	/**
	 * 判断是否已经创建了快捷方式（在某些moto的机型中需要判断）
	 * 
	 * @param con
	 *            当前调用的activity
	 * @param appName
	 *            当前应用程序的名称
	 * @return 返回是否已经创建桌面快捷方式
	 */
	public boolean hasShortcut(Activity con, String appName) {
		boolean isInstallShortcut = false;

		final ContentResolver cr = con.getContentResolver();

		final String AUTHORITY = "com.android.launcher.settings";

		final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
				+ "/favorites?notify=true");

		Cursor c = cr.query(CONTENT_URI, new String[] { "title" }, "title=?",
				new String[] { appName.trim() }, null);

		if (c != null && c.getCount() > 0) {
			isInstallShortcut = true;
		}
		try {
			if (c != null) {
				c.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("异常", "在关闭资源的时候----CreatShortCut类的64行");
		}

		return isInstallShortcut;

	}

	/**
	 * 删除程序的快捷方式
	 * 
	 * @param con
	 *            当前调用的activity
	 * @param appName
	 *            当前应用程序的名称
	 */

	public void delShortcut(Activity con, String appName) {

		Intent shortcut = new Intent(
				"com.android.launcher.action.UNINSTALL_SHORTCUT");

		// 快捷方式的名称
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, appName);

		// 指定当前的Activity为快捷方式启动的对象: 如 com.everest.video.VideoPlayer

		// 注意: ComponentName的第二个参数必须是完整的类名（包名+类名），否则无法删除快捷方式

		String appClass = con.getPackageName() + "." + con.getLocalClassName();

		ComponentName comp = new ComponentName(con.getPackageName(), appClass);

		shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(
				Intent.ACTION_MAIN).setComponent(comp));

		con.sendBroadcast(shortcut);

	}

//	/**
//	 * 为程序创建桌面快捷方式
//	 * 
//	 * @param con
//	 *            当前调用的activity
//	 * @param appName
//	 *            当前调用的应用程序名称
//	 * @param icon
//	 *            当前应用程序的icon 小图标
//	 */
//	public void createShorcut(Activity con, String appName, int icon) {
//		ComponentName comp = new ComponentName(con.getApplicationContext(),
//				DailyMenuActivity.class.getName());
//		// Create shortcutIntent here with the intent that will launch you app.
//		Intent shortcutIntent = new Intent(new Intent(Intent.ACTION_MAIN)
//				.setComponent(comp));
//		// 处理点击快捷方式会再次重新启动的问题
//		shortcutIntent.addCategory(Intent.CATEGORY_LAUNCHER);
//		// shortcutIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//		final Intent intent = new Intent();
//		intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
//		// Sets the custom shortcut's title
//		intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, appName);
//		ShortcutIconResource iconRes = Intent.ShortcutIconResource.fromContext(
//				con, icon);
//
//		intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);
//		// set shortcut unique on desktop
//		intent.putExtra("duplicate", false);
//
//		// add the shortcut
//		intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
//		con.sendBroadcast(intent);
//	}
}
