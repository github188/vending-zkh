package com.mc.vending.parse;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.mc.vending.data.BaseData;
import com.mc.vending.parse.listener.DataParseListener;
import com.mc.vending.parse.listener.DataParseRequestListener;

/**
 * 售货机数据请求与解析
 * 
 * @author apple
 *
 */
public class VendingDataExistParse implements DataParseListener {

    private static VendingDataExistParse instance = null;
    private DataParseRequestListener     listener;       //使用单例模式，不能使用listener，或者使用完成后将listener清空

    public DataParseRequestListener getListener() {
        return listener;
    }

    public void setListener(DataParseRequestListener listener) {
        this.listener = listener;
    }

    public void removeListener() {
        this.listener = null;
    }

    public VendingDataExistParse() {
    }

    public static VendingDataExistParse getInstance() {
        if (instance == null) {
            instance = new VendingDataExistParse();
        }
        return instance;
    }

    /**
     * 请求回调方法
     */
    @Override
    public void parseJson(BaseData baseData) {

        if (!baseData.isSuccess()) {
            if (this.listener != null) {
                this.listener.parseRequestFailure(baseData);
            }
            return;
        }

        boolean flag = parse(baseData.getData());
        baseData.setUserObject(flag);

        if (this.listener != null) {
            this.listener.parseRequestFinised(baseData);
        }
    }

    /**
     * 网络请求,下载数据
     */
    public void requestVendingExistData(String optType, String requestURL, String vendingCode,
                                        boolean init) {
        JSONObject json = new JSONObject();
        try {
            json.put("VD1_CODE", vendingCode);
            json.put("Init", init ? "1" : "0");
            DataParseHelper helper = new DataParseHelper(this);
            helper.requestSubmitServer(optType, json, requestURL);
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(this.getClass().toString(), "======>>>>>售货机存在请求数据异常!");
        }
    }

    /**
     * 数据解析
     * 
     * @param jsonArray
     * @return
     */
    public boolean parse(JSONArray jsonArray) {
        boolean flag = false;
        try {
            if (jsonArray == null) {
                return flag;
            }
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObj = jsonArray.getJSONObject(i);
                if (jsonObj == null) {
                    continue;
                } else {
                    flag = true;
                }
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(this.getClass().toString(), "======>>>>>售货机验证存在异常!");
        }
        return flag;
    }

    @Override
    public void parseRequestError(BaseData baseData) {
        if (listener != null) {
            listener.parseRequestFailure(baseData);
        }

    }
}
