package com.mc.vending.parse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.mc.vending.data.BaseData;
import com.mc.vending.data.CustomerEmpLinkData;
import com.mc.vending.db.CustomerEmpLinkDbOper;
import com.mc.vending.parse.listener.DataParseListener;
import com.mc.vending.parse.listener.DataParseRequestListener;

public class CustomerEmpLinkDataParse implements DataParseListener {
    private static CustomerEmpLinkDataParse instance = null;
    private DataParseRequestListener        listener;

    public DataParseRequestListener getListener() {
        return listener;
    }

    public void setListener(DataParseRequestListener listener) {
        this.listener = listener;
    }

    public CustomerEmpLinkDataParse() {
    }

    public static CustomerEmpLinkDataParse getInstance() {
        if (instance == null) {
            instance = new CustomerEmpLinkDataParse();
        }
        return instance;
    }

    /**
     * 网络请求
     */
    public void requestCustomerEmpLinkData(String optType, String requestURL, String vendingId) {
        JSONObject json = new JSONObject();
        try {
            json.put("VD1_ID", vendingId);
            DataParseHelper helper = new DataParseHelper(this);
            helper.requestSubmitServer(optType, json, requestURL);
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(this.getClass().toString(), "======>>>>>客户员工网络请求数据异常!");
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
        // 全表
        List<CustomerEmpLinkData> list = parse(baseData.getData());
        // 0全表时不需要删除原数据-false。1表示需要删除true
        if (list.isEmpty() && !baseData.getDeleteFlag()) {
            if (this.listener != null) {
                this.listener.parseRequestFinised(baseData);
            }
            return;
        }
        CustomerEmpLinkDbOper customerEmpLinkDbOper = new CustomerEmpLinkDbOper();
        boolean deleteFlag = customerEmpLinkDbOper.deleteAll();
        if (deleteFlag) {
            boolean addFlag = customerEmpLinkDbOper.batchAddCustomerEmpLink(list);
            if (addFlag) {
                Log.i("[customerEmpLink]:", "客户员工批量增加成功!" + "======" + list.size());
                DataParseHelper parseHelper = new DataParseHelper(this);
                parseHelper.sendLogVersion(list.get(0).getLogVersion());
            } else {
                Log.i("[customerEmpLink]:", "客户员工批量增加失败!");
            }
        }

        // System.out.println(customerEmpLinkDbOper.findAll());
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
    public List<CustomerEmpLinkData> parse(JSONArray jsonArray) {
        if (jsonArray == null) {
            return new ArrayList<CustomerEmpLinkData>(0);
        }
        List<CustomerEmpLinkData> list = new ArrayList<CustomerEmpLinkData>();
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
                CustomerEmpLinkData data = new CustomerEmpLinkData();
                data.setCe1Id(jsonObj.getString("ID"));
                data.setCe1M02Id(jsonObj.getString("CE1_M02_ID"));
                data.setCe1Cu1Id(jsonObj.getString("CE1_CU1_ID"));
                data.setCe1Code(jsonObj.getString("CE1_CODE"));
                data.setCe1Name(jsonObj.getString("CE1_Name"));
                data.setCe1EnglishName(jsonObj.getString("CE1_EnglishName"));
                data.setCe1Sex(jsonObj.getString("CE1_Sex"));
                data.setCe1Dp1Id(jsonObj.getString("CE1_DP1_ID"));
                data.setCe1DicIdJob(jsonObj.getString("CE1_Dic_ID_JOB"));
                data.setCe1Phone(jsonObj.getString("CE1_Phone"));
                data.setCe1Status(jsonObj.getString("CE1_Status"));
                data.setCe1Remark(jsonObj.getString("CE1_Remark"));
                data.setLogVersion(jsonObj.getString("LogVision"));
                data.setCe1CreateUser(createUser);
                data.setCe1CreateTime(createTime);
                data.setCe1ModifyUser(modifyUser);
                data.setCe1ModifyTime(modifyTime);
                data.setCe1RowVersion(rowVersion);
                list.add(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(this.getClass().toString(), "======>>>>>客户员工解析数据异常!");
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
