package com.mc.vending.parse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.mc.vending.data.BaseData;
import com.mc.vending.data.UsedRecordData;
import com.mc.vending.db.UsedRecordDbOper;
import com.mc.vending.parse.listener.DataParseListener;
import com.mc.vending.parse.listener.DataParseRequestListener;

public class UsedRecordDownloadDataParse implements DataParseListener {
    private static UsedRecordDownloadDataParse instance = null;
    private DataParseRequestListener           listener;

    public DataParseRequestListener getListener() {
        return listener;
    }

    public void setListener(DataParseRequestListener listener) {
        this.listener = listener;
    }

    public UsedRecordDownloadDataParse() {
    }

    public static UsedRecordDownloadDataParse getInstance() {
        if (instance == null) {
            instance = new UsedRecordDownloadDataParse();
        }
        return instance;
    }

    /**
     * 网络请求,下载数据
     */
    public void requestUsedRecordData(String optType, String requestURL, String vendingId) {
        JSONObject json = new JSONObject();
        try {
            json.put("VD1_ID", vendingId);
            DataParseHelper helper = new DataParseHelper(this);
            helper.requestSubmitServer(optType, json, requestURL);
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(this.getClass().toString(), "======>>>>>卡与产品领用网络请求数据异常!");
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
        List<UsedRecordData> list = parse(baseData.getData());
        // 0全表时不需要删除原数据-false。1表示需要删除true
        if (list.isEmpty() && !baseData.getDeleteFlag()) {
            if (this.listener != null) {
                this.listener.parseRequestFinised(baseData);
            }
            return;
        }
        UsedRecordDbOper dbOperbOper = new UsedRecordDbOper();
        List<String> dbPra = new ArrayList<String>();
        for (UsedRecordData data : list) {
            dbPra.add(data.getUr1CD1_ID() + "," + data.getUr1VD1_ID());
        }
        boolean deleteFlag = dbOperbOper.batchDeleteVendingCard(dbPra);
        if (deleteFlag) {
            Log.i("[UsedRecord]:", "卡与产品领用批量删除成功!" + "======" + dbPra);
            boolean addFlag = dbOperbOper.batchAddUsedRecord(list);
            if (addFlag) {
                Log.i("[UsedRecord]:", "卡与产品领用批量增加成功!" + "======" + list.size());
                DataParseHelper parseHelper = new DataParseHelper(this);
                parseHelper.sendLogVersion(list.get(0).getLogVersion());
            } else {
                Log.i("[UsedRecord]:", "卡与产品领用批量增加失败!");
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
    public List<UsedRecordData> parse(JSONArray jsonArray) {
        if (jsonArray == null) {
            return new ArrayList<UsedRecordData>(0);
        }
        List<UsedRecordData> list = new ArrayList<UsedRecordData>();
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

                UsedRecordData data = new UsedRecordData();
                data.setUr1ID(jsonObj.getString("ID"));
                data.setUr1M02_ID(jsonObj.getString("UR1_M02_ID"));
                data.setUr1CD1_ID(jsonObj.getString("UR1_CD1_ID"));
                data.setUr1VD1_ID(jsonObj.getString("UR1_VD1_ID"));
                data.setUr1PD1_ID(jsonObj.getString("UR1_PD1_ID"));
                data.setUr1Quantity(jsonObj.getString("UR1_Quantity"));

                data.setLogVersion(jsonObj.getString("LogVision"));
                data.setCreateUser(createUser);
                data.setCreateTime(createTime);
                data.setModifyUser(modifyUser);
                data.setModifyTime(modifyTime);
                data.setRowVersion(rowVersion);
                list.add(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(this.getClass().toString(), "======>>>>>卡与产品领用解析网络数据异常!");
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
