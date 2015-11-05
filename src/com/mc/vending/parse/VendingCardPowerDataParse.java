package com.mc.vending.parse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.mc.vending.data.BaseData;
import com.mc.vending.data.VendingCardPowerData;
import com.mc.vending.db.VendingCardPowerDbOper;
import com.mc.vending.parse.listener.DataParseListener;
import com.mc.vending.parse.listener.DataParseRequestListener;
import com.mc.vending.tools.ZillionLog;

public class VendingCardPowerDataParse implements DataParseListener {
    private static VendingCardPowerDataParse instance = null;
    private DataParseRequestListener         listener;

    public DataParseRequestListener getListener() {
        return listener;
    }

    public void setListener(DataParseRequestListener listener) {
        this.listener = listener;
    }

    public VendingCardPowerDataParse() {
    }

    public static VendingCardPowerDataParse getInstance() {
        if (instance == null) {
            instance = new VendingCardPowerDataParse();
        }
        return instance;
    }

    /**
     * 网络请求，下载数据
     */
    public void requestVendingCardPowerData(String optType, String requestURL, String vendingId) {
        JSONObject json = new JSONObject();
        try {
            json.put("VD1_ID", vendingId);
            DataParseHelper helper = new DataParseHelper(this);
            helper.requestSubmitServer(optType, json, requestURL);
        } catch (Exception e) {
            e.printStackTrace();
            ZillionLog.e(this.getClass().toString(), "======>>>>>售货机卡/密码权限网络请求数据异常!");
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
        if (baseData==null || baseData.getData() == null || baseData.getData().length()==0) {
            if (listener != null) {
                listener.parseRequestFailure(baseData);
            }
            return ;
        }
        // 全表
        List<VendingCardPowerData> list = parse(baseData.getData());
        // 0全表时不需要删除原数据-false。1表示需要删除true
        if (list.isEmpty() && !baseData.getDeleteFlag()) {
            if (this.listener != null) {
                this.listener.parseRequestFinised(baseData);
            }
            return;
        }
        VendingCardPowerDbOper vendingCardPowerDbOper = new VendingCardPowerDbOper();
        boolean deleteFlag = vendingCardPowerDbOper.deleteAll();
        if (deleteFlag) {
            boolean addFlag = vendingCardPowerDbOper.batchAddVendingCardPower(list);
            if (addFlag) {
                Log.i("[vendingCardPower]:", "======>>>>>售货机卡/密码权限批量增加成功!" + list.size());
                DataParseHelper parseHelper = new DataParseHelper(this);
                parseHelper.sendLogVersion(list.get(0).getLogVersion());
            } else {
                ZillionLog.e("[vendingCardPower]:", "==========>>>>>售货机卡/密码权限批量增加失败!");
            }
        }

        // System.out.println(vendingCardPowerDbOper.findAll());

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
    public List<VendingCardPowerData> parse(JSONArray jsonArray) {
        if (jsonArray == null) {
            return new ArrayList<VendingCardPowerData>(0);
        }
        List<VendingCardPowerData> list = new ArrayList<VendingCardPowerData>();
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
                VendingCardPowerData data = new VendingCardPowerData();
                data.setVc2Id(jsonObj.getString("ID"));
                data.setVc2M02Id(jsonObj.getString("VC2_M02_ID"));
                data.setVc2Cu1Id(jsonObj.getString("VC2_CU1_ID"));
                data.setVc2Vd1Id(jsonObj.getString("VC2_VD1_ID"));
                data.setVc2Cd1Id(jsonObj.getString("VC2_CD1_ID"));
                data.setLogVersion(jsonObj.getString("LogVision"));
                data.setVc2CreateUser(createUser);
                data.setVc2CreateTime(createTime);
                data.setVc2ModifyUser(modifyUser);
                data.setVc2ModifyTime(modifyTime);
                data.setVc2RowVersion(rowVersion);
                list.add(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
            ZillionLog.e(this.getClass().toString(), "======>>>>>售货机卡/密码权限解析数据异常!");
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
