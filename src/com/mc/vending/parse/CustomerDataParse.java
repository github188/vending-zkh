package com.mc.vending.parse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.mc.vending.data.BaseData;
import com.mc.vending.data.CardData;
import com.mc.vending.data.CustomerData;
import com.mc.vending.db.CardDbOper;
import com.mc.vending.db.CustomerDbOper;
import com.mc.vending.parse.listener.DataParseListener;
import com.mc.vending.parse.listener.DataParseRequestListener;
import com.mc.vending.tools.ZillionLog;

public class CustomerDataParse implements DataParseListener {
    private static CustomerDataParse instance = null;
    private DataParseRequestListener listener;

    public DataParseRequestListener getListener() {
        return listener;
    }

    public void setListener(DataParseRequestListener listener) {
        this.listener = listener;
    }

    public CustomerDataParse() {
    }

    public static CustomerDataParse getInstance() {
        if (instance == null) {
            instance = new CustomerDataParse();
        }
        return instance;
    }

    /**
     * 网络请求
     */
    public void requestCustomerData(String optType, String requestURL, String vendingId) {
        JSONObject json = new JSONObject();
        try {
            json.put("VD1_ID", vendingId);
            DataParseHelper helper = new DataParseHelper(this);
            helper.requestSubmitServer(optType, json, requestURL);
        } catch (Exception e) {
            e.printStackTrace();
            ZillionLog.e(this.getClass().toString(), "======>>>>>客户网络请求数据异常!");
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
        if (baseData==null || baseData.getData()==null  || baseData.getData().length()==0) {
            if (listener != null) {
                listener.parseRequestFailure(baseData);
            }
            return ;
        }
        // 全表
        List<CustomerData> list = parse(baseData.getData());
        // 0全表时不需要删除原数据-false。1表示需要删除true
        if (list.isEmpty() && !baseData.getDeleteFlag()) {
            if (this.listener != null) {
                this.listener.parseRequestFinised(baseData);
            }
            return;
        }
        List<CustomerData> addList = new ArrayList<CustomerData>();
		List<CustomerData> updateList = new ArrayList<CustomerData>();
		List<CustomerData> deleteList = new ArrayList<CustomerData>();
		for (CustomerData customerData : list) {
			if (customerData.getCRUD().equals("D")) {
				deleteList.add(customerData);
			} else if (customerData.getCRUD().equals("C")) {
				addList.add(customerData);
			} else if (customerData.getCRUD().equals("U")) {
				updateList.add(customerData);
			}
		}
		CustomerDbOper customerDbOper = new CustomerDbOper();
		if (!addList.isEmpty()) {
			boolean addFlag = customerDbOper.batchAddCustomer(addList);
			if (addFlag) {
				Log.i("[customer]:", "客户批量增加成功!" + "======" + list.size());
				DataParseHelper parseHelper = new DataParseHelper(this);
				parseHelper.sendLogVersion(list.get(0).getLogVersion());
			} else {
				ZillionLog.e("[customer]:", "客户批量增加失败!");
			}
		}
		if (!deleteList.isEmpty()) {
			boolean deleteFlag = customerDbOper.batchDeleteCustomer(deleteList);
			if (deleteFlag) {
				Log.i("[customer]:", "客户批量删除成功!" + "======" + list.size());
				DataParseHelper parseHelper = new DataParseHelper(this);
				parseHelper.sendLogVersion(list.get(0).getLogVersion());
			} else {
				ZillionLog.e("[customer]:", "客户批量删除失败!");
			}
		}
		if (!updateList.isEmpty()) {
			boolean deleteFlag = customerDbOper.batchDeleteCustomer(updateList);
			boolean addFlag = customerDbOper.batchAddCustomer(updateList);
			if (deleteFlag && addFlag) {
				Log.i("[customer]:", "客户批量修改成功!" + "======" + list.size());
				DataParseHelper parseHelper = new DataParseHelper(this);
				parseHelper.sendLogVersion(list.get(0).getLogVersion());
			} else {
				ZillionLog.e("[customer]:", "客户批量修改失败!");
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
    public List<CustomerData> parse(JSONArray jsonArray) {
        if (jsonArray == null) {
            return new ArrayList<CustomerData>(0);
        }
        List<CustomerData> list = new ArrayList<CustomerData>();
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

                CustomerData data = new CustomerData();
                data.setCu1Id(jsonObj.getString("ID"));
                data.setCu1M02Id(jsonObj.getString("CU1_M02_ID"));
                data.setCu1Code(jsonObj.getString("CU1_CODE"));
                data.setCu1Name(jsonObj.getString("CU1_Name"));
                data.setCRUD(jsonObj.getString("CRUD"));
                data.setCu1Relation(jsonObj.getString("CU1_Relation"));
                data.setCu1RelationPhone(jsonObj.getString("CU1_RelationPhone"));
                data.setCu1Saler(jsonObj.getString("CU1_Saler"));
                data.setCu1SalerPhone(jsonObj.getString("CU1_SalerPhone"));
                data.setCu1Country(jsonObj.getString("CU1_Country"));
                data.setCu1City(jsonObj.getString("CU1_City"));
                data.setCu1Area(jsonObj.getString("CU1_Area"));
                data.setCu1Address(jsonObj.getString("CU1_Address"));
                data.setCu1LastImportTime(jsonObj.getString("CU1_LastImportTime"));
                data.setCu1CodeFather(jsonObj.getString("CU1_CODE_Father"));
                data.setLogVersion(jsonObj.getString("LogVision"));
                data.setCu1CreateUser(createUser);
                data.setCu1CreateTime(createTime);
                data.setCu1ModifyUser(modifyUser);
                data.setCu1ModifyTime(modifyTime);
                data.setCu1RowVersion(rowVersion);
                list.add(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
            ZillionLog.e(this.getClass().toString(), "======>>>>>客户解析数据异常!");
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
