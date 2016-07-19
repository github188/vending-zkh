package com.mc.vending.parse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.mc.vending.data.BaseData;
import com.mc.vending.data.InterfaceData;
import com.mc.vending.data.VendingCardPowerData;
import com.mc.vending.db.InterfaceDbOper;
import com.mc.vending.db.VendingCardPowerDbOper;
import com.mc.vending.parse.listener.DataParseListener;
import com.mc.vending.parse.listener.DataParseRequestListener;
import com.mc.vending.tools.ConvertHelper;
import com.mc.vending.tools.ZillionLog;

public class ConfigDataParse implements DataParseListener {
    private DataParseRequestListener listener;

    public DataParseRequestListener getListener() {
        return listener;
    }

    public void setListener(DataParseRequestListener listener) {
        this.listener = listener;
    }

    private static ConfigDataParse instance = null;

    public ConfigDataParse() {
    }

    public static ConfigDataParse getInstance() {
        if (instance == null) {
            instance = new ConfigDataParse();
        }
        return instance;
    }

    @Override
    public void parseJson(BaseData baseData) {
        if (!baseData.isSuccess()) {
            if (this.listener != null) {
                this.listener.parseRequestFailure(baseData);
            }
            return;
        }
        if (baseData.getData()==null || baseData.getData() == null || baseData.getData().length()==0) {
            if (listener != null) {
                listener.parseRequestFailure(baseData);
            }
            return;
        }
        //全表
        List<InterfaceData> list = parse(baseData.getData());
        //0全表时不需要删除原数据-false。1表示需要删除true
        if (list.isEmpty() && !baseData.getDeleteFlag()) {
            if (this.listener != null) {
                this.listener.parseRequestFinised(baseData);
            }
            return;
        }

		List<InterfaceData> addList = new ArrayList<InterfaceData>();
		List<InterfaceData> updateList = new ArrayList<InterfaceData>();
		List<InterfaceData> deleteList = new ArrayList<InterfaceData>();
		for (InterfaceData interfaceData : list) {
			if (interfaceData.getCRUD().equals("D")) {
				deleteList.add(interfaceData);
			} else if (interfaceData.getCRUD().equals("C")) {
				addList.add(interfaceData);
			} else if (interfaceData.getCRUD().equals("U")) {
				updateList.add(interfaceData);
			}
		}
		InterfaceDbOper interfaceDbOper = new InterfaceDbOper();
		if (!addList.isEmpty()) {
			boolean addFlag = interfaceDbOper.batchAddInterface(addList);
			if (addFlag) {
				Log.i("[interface]:", "接口配置批量增加成功!" + "======" + list.size());
				DataParseHelper parseHelper = new DataParseHelper(this);
				parseHelper.sendLogVersion(list.get(0).getLogVersion());
			} else {
				ZillionLog.e("[interface]:", "接口配置批量增加失败!");
			}
		}
		if (!deleteList.isEmpty()) {
			boolean deleteFlag = interfaceDbOper.batchDeleteInterface(deleteList);
			if (deleteFlag) {
				Log.i("[interface]:", "接口配置批量增加成功!" + "======" + list.size());
				DataParseHelper parseHelper = new DataParseHelper(this);
				parseHelper.sendLogVersion(list.get(0).getLogVersion());
			} else {
				ZillionLog.e("[interface]:", "接口配置批量删除失败!");
			}
		}
		if (!updateList.isEmpty()) {
			boolean deleteFlag = interfaceDbOper.batchDeleteInterface(updateList);
			boolean addFlag = interfaceDbOper.batchAddInterface(updateList);
			if (deleteFlag && addFlag) {
				Log.i("[interface]:", "接口配置批量修改成功!" + "======" + list.size());
				DataParseHelper parseHelper = new DataParseHelper(this);
				parseHelper.sendLogVersion(list.get(0).getLogVersion());
			} else {
				ZillionLog.e("[interface]:", "接口配置批量修改失败!");
			}
		}
        if (this.listener != null) {
            this.listener.parseRequestFinised(baseData);
        }

    }

    /**
     * 网络请求
     * 
     * @param json
     * @param requestURL
     */
    public void requestConfigData(String optType, String requestURL, String vendingId) {
        JSONObject json = new JSONObject();
        try {
            json.put("VD1_ID", vendingId);
            DataParseHelper helper = new DataParseHelper(this);
            helper.requestSubmitServer(optType, json, requestURL);
        } catch (Exception e) {
            e.printStackTrace();
            ZillionLog.e(this.getClass().toString(), "======>>>>>接口配置网络请求数据异常!");
        }
    }

    /**
     * 数据解析
     * 
     * @param jsonArray
     * @return
     */
    public List<InterfaceData> parse(JSONArray jsonArray) {
        if (jsonArray == null) {
            return new ArrayList<InterfaceData>(0);
        }
        List<InterfaceData> list = new ArrayList<InterfaceData>();
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

                InterfaceData data = new InterfaceData();
                data.setM03Id(jsonObj.getString("ID"));
                data.setM03M02Id(jsonObj.getString("M03_M02_ID"));
                data.setM03Name(jsonObj.getString("M03_Name"));
                data.setCRUD(jsonObj.getString("CRUD"));
                data.setM03Target(jsonObj.getString("M03_Target"));
                data.setM03Optype(jsonObj.getString("M03_Optype"));
                data.setM03Remark(jsonObj.getString("M03_Remark"));
                data.setM03ExeInterval(ConvertHelper.toInt(jsonObj.getString("M03_ExeInterval"), 0));
                data.setM03StartTime(jsonObj.getString("M03_StartTime"));
                data.setM03EndTime(jsonObj.getString("M03_EndTime"));
                data.setM03ExeIndex(ConvertHelper.toInt(jsonObj.getString("M03_ExeIndex"), 0));
                data.setM03RowCount(ConvertHelper.toInt(jsonObj.getString("M03_RowCount"), 0));
                data.setLogVersion(jsonObj.getString("LogVision"));
                data.setM03CreateUser(createUser);
                data.setM03CreateTime(createTime);
                data.setM03ModifyUser(modifyUser);
                data.setM03ModifyTime(modifyTime);
                data.setM03RowVersion(rowVersion);
                list.add(data);

            }
        } catch (Exception e) {
            e.printStackTrace();
            ZillionLog.e(this.getClass().toString(), "======>>>>>接口配置解析数据异常!");
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
