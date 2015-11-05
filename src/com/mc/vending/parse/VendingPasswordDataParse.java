package com.mc.vending.parse;

import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.mc.vending.data.BaseData;
import com.mc.vending.data.VendingPasswordData;
import com.mc.vending.db.VendingPasswordDbOper;
import com.mc.vending.parse.listener.DataParseListener;
import com.mc.vending.parse.listener.DataParseRequestListener;
import com.mc.vending.tools.ZillionLog;

public class VendingPasswordDataParse implements DataParseListener {
    private static VendingPasswordDataParse instance = null;
    private DataParseRequestListener        listener;

    public DataParseRequestListener getListener() {
        return listener;
    }

    public void setListener(DataParseRequestListener listener) {
        this.listener = listener;
    }

    public VendingPasswordDataParse() {
    }

    public static VendingPasswordDataParse getInstance() {
        if (instance == null) {
            instance = new VendingPasswordDataParse();
        }
        return instance;
    }

    /**
     * 网络请求,下载数据
     */
    public void requestVendingPasswordData(String optType, String requestURL, String vendingId) {
        JSONObject json = new JSONObject();
        try {
            json.put("VD1_ID", vendingId);
            DataParseHelper helper = new DataParseHelper(this);
            helper.requestSubmitServer(optType, json, requestURL);
        } catch (Exception e) {
            e.printStackTrace();
            ZillionLog.e(this.getClass().toString(), "======>>>>>售货机强制密码网络请求数据异常!");
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
        VendingPasswordData vendingPasswordData = parse(baseData.getData());
        // 0全表时不需要删除原数据-false。1表示需要删除true
        if (vendingPasswordData == null && !baseData.getDeleteFlag()) {
            if (this.listener != null) {
                this.listener.parseRequestFinised(baseData);
            }
            return;
        }
        // 全表，只有一条数据
        VendingPasswordDbOper vendingPasswordDbOper = new VendingPasswordDbOper();
        VendingPasswordData vendingPassword = vendingPasswordDbOper.getVendingPassword();
        if (vendingPassword != null) {
            if (vendingPassword.getVp3Id().equals(vendingPasswordData.getVp3Id())) {
                boolean updateFlag = vendingPasswordDbOper.updateVendingPassword(vendingPasswordData);
                if (updateFlag) {
                    Log.i("[vendingPassword]:", "======>>>>>售货机强制密码更新成功!");
                    DataParseHelper parseHelper = new DataParseHelper(this);
                    parseHelper.sendLogVersion(vendingPasswordData.getLogVersion());
                } else {
                    ZillionLog.e("[vendingPassword]:", "==========>>>>>售货机强制密码更新失败!");
                }
            }
        } else {
            boolean insert_flag = vendingPasswordDbOper.addVendingPassword(vendingPasswordData);
            if (insert_flag) {
                Log.i("[vendingPassword]:", "======>>>>>售货机强制密码增加成功!");
                DataParseHelper parseHelper = new DataParseHelper(this);
                parseHelper.sendLogVersion(vendingPasswordData.getLogVersion());
            } else {
                ZillionLog.e("[vendingPassword]:", "==========>>>>>售货机强制密码增加失败!");
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
    public VendingPasswordData parse(JSONArray jsonArray) {
        VendingPasswordData data = null;
        if (jsonArray == null) {
            return null;
        }
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

                data = new VendingPasswordData();
                data.setVp3Id(jsonObj.getString("ID"));
                data.setVp3M02Id(jsonObj.getString("VP3_M02_ID"));
                data.setVp3Password(jsonObj.getString("VP3_Password"));
                data.setLogVersion(jsonObj.getString("LogVision"));
                data.setVp3CreateUser(createUser);
                data.setVp3CreateTime(createTime);
                data.setVp3ModifyUser(modifyUser);
                data.setVp3ModifyTime(modifyTime);
                data.setVp3RowVersion(rowVersion);
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            ZillionLog.e(this.getClass().toString(), "======>>>>>售货机强制密码解析网络数据异常!");
        }
        return data;
    }

    @Override
    public void parseRequestError(BaseData baseData) {
        if (listener != null) {
            listener.parseRequestFailure(baseData);
        }

    }
}
