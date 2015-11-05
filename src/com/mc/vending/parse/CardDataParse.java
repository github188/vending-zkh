package com.mc.vending.parse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.mc.vending.data.BaseData;
import com.mc.vending.data.CardData;
import com.mc.vending.db.CardDbOper;
import com.mc.vending.parse.listener.DataParseListener;
import com.mc.vending.parse.listener.DataParseRequestListener;
import com.mc.vending.tools.ZillionLog;

public class CardDataParse implements DataParseListener {
    private static CardDataParse     instance = null;
    private DataParseRequestListener listener;

    public DataParseRequestListener getListener() {
        return listener;
    }

    public void setListener(DataParseRequestListener listener) {
        this.listener = listener;
    }

    public CardDataParse() {
    }

    public static CardDataParse getInstance() {
        if (instance == null) {
            instance = new CardDataParse();
        }
        return instance;
    }

    /**
     * 网络请求,下载数据
     */
    public void requestCardData(String optType, String requestURL, String vendingId) {
        JSONObject json = new JSONObject();
        try {
            json.put("VD1_ID", vendingId);
            DataParseHelper helper = new DataParseHelper(this);
            helper.requestSubmitServer(optType, json, requestURL);
        } catch (Exception e) {
            e.printStackTrace();
            ZillionLog.e(this.getClass().toString(), "======>>>>>卡/密码网络请求数据异常!");
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
        List<CardData> list = parse(baseData.getData());
        // 0全表时不需要删除原数据-false。1表示需要删除true
        if (list.isEmpty() && !baseData.getDeleteFlag()) {
            if (this.listener != null) {
                this.listener.parseRequestFinised(baseData);
            }
            return;
        }
        CardDbOper cardDbOper = new CardDbOper();
        boolean deleteFlag = cardDbOper.deleteAll();
        if (deleteFlag) {
            boolean addFlag = cardDbOper.batchAddCard(list);
            if (addFlag) {
                Log.i("[card]:", "卡/密码批量增加成功!" + "======" + list.size());
                DataParseHelper parseHelper = new DataParseHelper(this);
                parseHelper.sendLogVersion(list.get(0).getLogVersion());
            } else {
                ZillionLog.e("[card]:", "卡/密码批量增加失败!");
            }
        }
        // System.out.println(cardDbOper.findAll());

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
    public List<CardData> parse(JSONArray jsonArray) {
        if (jsonArray == null) {
            return new ArrayList<CardData>(0);
        }
        List<CardData> list = new ArrayList<CardData>();
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

                CardData data = new CardData();
                data.setCd1Id(jsonObj.getString("ID"));
                data.setCd1M02Id(jsonObj.getString("CD1_M02_ID"));
                data.setCd1SerialNo(jsonObj.getString("CD1_SerialNo"));
                data.setCd1Code(jsonObj.getString("CD1_CODE"));
                data.setCd1Type(jsonObj.getString("CD1_Type"));
                data.setCd1Password(jsonObj.getString("CD1_Password"));
                data.setCd1Purpose(jsonObj.getString("CD1_Purpose"));
                data.setCd1Status(jsonObj.getString("CD1_Status"));
                data.setCd1CustomerStatus(jsonObj.getString("CD1_CustomerStatus"));
                data.setCd1ProductPower(jsonObj.getString("CD1_ProductPower"));//产品权限
                data.setLogVersion(jsonObj.getString("LogVision"));
                data.setCd1CreateUser(createUser);
                data.setCd1CreateTime(createTime);
                data.setCd1ModifyUser(modifyUser);
                data.setCd1ModifyTime(modifyTime);
                data.setCd1RowVersion(rowVersion);
                list.add(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
            ZillionLog.e(this.getClass().toString(), "======>>>>>卡/密码解析网络数据异常!");
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
