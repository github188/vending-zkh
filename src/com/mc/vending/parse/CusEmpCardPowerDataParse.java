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

public class CusEmpCardPowerDataParse implements DataParseListener {
    private static CusEmpCardPowerDataParse instance = null;
    private DataParseRequestListener        listener;

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
            Log.i(this.getClass().toString(), "======>>>>>客户员工卡/密码权限网络请求数据异常!");
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
        List<CusEmpCardPowerData> list = parse(baseData.getData());
        // 0全表时不需要删除原数据-false。1表示需要删除true
        if (list.isEmpty() && !baseData.getDeleteFlag()) {
            if (this.listener != null) {
                this.listener.parseRequestFinised(baseData);
            }
            return;
        }
        CusEmpCardPowerDbOper cusEmpCardPowerDbOper = new CusEmpCardPowerDbOper();
        boolean deleteFlag = cusEmpCardPowerDbOper.deleteAll();
        if (deleteFlag) {
            boolean addFlag = cusEmpCardPowerDbOper.batchAddCusEmpCardPower(list);
            if (addFlag) {
                Log.i("[cusEmpCardPower]:", "客户员工卡/密码权限批量增加成功!" + "======" + list.size());
                DataParseHelper parseHelper = new DataParseHelper(this);
                parseHelper.sendLogVersion(list.get(0).getLogVersion());
            } else {
                Log.i("[cusEmpCardPower]:", "客户员工卡/密码权限批量增加失败!");
            }
        }
        // System.out.println(cusEmpCardPowerDbOper.findAll());
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
                list.add(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(this.getClass().toString(), "======>>>>>客户员工卡/密码权限解析数据异常!");
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
