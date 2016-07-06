package com.mc.vending.parse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.mc.vending.data.BaseData;
import com.mc.vending.data.CusEmpCardPowerData;
import com.mc.vending.db.CusEmpCardPowerDbOper;
import com.mc.vending.parse.listener.DataParseListener;
import com.mc.vending.parse.listener.DataParseRequestListener;
import com.mc.vending.tools.ZillionLog;

public class CusEmpCardPowerDataParse implements DataParseListener {
	private static CusEmpCardPowerDataParse instance = null;
	private DataParseRequestListener listener;

	public DataParseRequestListener getListener() {
		return listener;
	}

	public void setListener(DataParseRequestListener listener) {
		this.listener = listener;
	}

	public CusEmpCardPowerDataParse() {
	}

	public static CusEmpCardPowerDataParse getInstance() {
		if (instance == null) {
			instance = new CusEmpCardPowerDataParse();
		}
		return instance;
	}

	/**
	 * 网络请求下载数据
	 */
	public void requestCusEmpCardPowerData(String optType, String requestURL, String vendingId) {
		JSONObject json = new JSONObject();
		try {
			json.put("VD1_ID", vendingId);
			DataParseHelper helper = new DataParseHelper(this);
			helper.requestSubmitServer(optType, json, requestURL);
		} catch (Exception e) {
			e.printStackTrace();
			ZillionLog.e(this.getClass().toString(), "======>>>>>客户员工卡/密码权限网络请求数据异常!");
		}
	}

	@Override
	public void parseJson(BaseData baseData) {
		if (!baseData.isSuccess()) {
			if (this.listener != null) {
				this.listener.parseRequestFailure(baseData);
			}
			return;
		}
		if (baseData == null || baseData.getData() == null || baseData.getData().length() == 0) {
			if (listener != null) {
				listener.parseRequestFailure(baseData);
			}
			return;
		}
		// 增量
		List<CusEmpCardPowerData> list = parse(baseData.getData());
		CusEmpCardPowerDbOper cusEmpCardPowerDbOper = new CusEmpCardPowerDbOper();
		Boolean deleteResult = false;
		Boolean addResult = false;
		for (CusEmpCardPowerData cusEmpCardPowerData : list) {
			if (cusEmpCardPowerData.getCRUD().equals("C")) {
				addResult = cusEmpCardPowerDbOper.addCusEmpCardPower(cusEmpCardPowerData);
				if (addResult) {
					Log.i("[cusEmpCardPower]:", "客户员工卡/密码权限添加成功!" + "======" + cusEmpCardPowerData.getCe2Id());
					DataParseHelper parseHelper = new DataParseHelper(this);
					parseHelper.sendLogVersion(list.get(0).getLogVersion());
				} else {
					ZillionLog.e("[cusEmpCardPower]:", "客户员工卡/密码权限删除失败!" + "======" + cusEmpCardPowerData.getCe2Id());
				}
			} else if (cusEmpCardPowerData.getCRUD().equals("U")) {
				deleteResult = cusEmpCardPowerDbOper.deleteCusEmpCardPower(cusEmpCardPowerData);
				if (deleteResult) {
					addResult = cusEmpCardPowerDbOper.addCusEmpCardPower(cusEmpCardPowerData);
					if (addResult) {
						Log.i("[cusEmpCardPower]:", "客户员工卡/密码权限修改成功!" + "======" + cusEmpCardPowerData.getCe2Id());
						DataParseHelper parseHelper = new DataParseHelper(this);
						parseHelper.sendLogVersion(list.get(0).getLogVersion());
					} else {
						ZillionLog.e("[cusEmpCardPower]:",
								"客户员工卡/密码权限修改失败!" + "======" + cusEmpCardPowerData.getCe2Id());
					}
				} else {
					ZillionLog.e("[cusEmpCardPower]:", "客户员工卡/密码权限修改失败!" + "======" + cusEmpCardPowerData.getCe2Id());
				}
			} else if (cusEmpCardPowerData.getCRUD().equals("D")) {
				deleteResult = cusEmpCardPowerDbOper.deleteCusEmpCardPower(cusEmpCardPowerData);
				if (deleteResult) {
					Log.i("[cusEmpCardPower]:", "客户员工卡/密码权限删除成功!" + "======" + cusEmpCardPowerData.getCe2Id());
					DataParseHelper parseHelper = new DataParseHelper(this);
					parseHelper.sendLogVersion(list.get(0).getLogVersion());
				} else {
					ZillionLog.e("[cusEmpCardPower]:", "客户员工卡/密码权限删除失败!" + "======" + cusEmpCardPowerData.getCe2Id());
				}
			}

		}
		if (this.listener != null) {
			this.listener.parseRequestFinised(baseData);
		}
	}

	/**
	 * 数据解析
	 * 
	 * @param jsonArray
	 * @return
	 */
	public List<CusEmpCardPowerData> parse(JSONArray jsonArray) {
		if (jsonArray == null) {
			return new ArrayList<CusEmpCardPowerData>(0);
		}
		List<CusEmpCardPowerData> list = new ArrayList<CusEmpCardPowerData>();
		try {
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObj = jsonArray.getJSONObject(i);
				if (jsonObj == null)
					continue;
				String createUser = jsonObj.getString("CreateUser");
				String createTime = jsonObj.getString("CreateTime");
				String modifyUser = jsonObj.getString("ModifyUser");
				String modifyTime = jsonObj.getString("ModifyTime");
				String rowVersion = new Date().getTime() + "";
				CusEmpCardPowerData data = new CusEmpCardPowerData();

				data.setCe2Id(jsonObj.getString("ID"));
				data.setCe2M02Id(jsonObj.getString("CE2_M02_ID"));
				data.setCe2Ce1Id(jsonObj.getString("CE2_CE1_ID"));
				data.setCe2Cd1Id(jsonObj.getString("CE2_CD1_ID"));
				data.setLogVersion(jsonObj.getString("LogVision"));
				data.setCe2CreateUser(createUser);
				data.setCe2CreateTime(createTime);
				data.setCe2ModifyUser(modifyUser);
				data.setCe2ModifyTime(modifyTime);
				data.setCe2RowVersion(rowVersion);
				data.setCRUD("CRUD");
				list.add(data);
			}
		} catch (Exception e) {
			e.printStackTrace();
			ZillionLog.e(this.getClass().toString(), "======>>>>>客户员工卡/密码权限解析数据异常!");
		}
		return list;
	}

	@Override
	public void parseRequestError(BaseData baseData) {
		if (listener != null) {
			listener.parseRequestFailure(baseData);
		}

	}
}
