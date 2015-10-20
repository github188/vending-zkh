package com.mc.vending.parse;

import org.json.JSONObject;

import android.util.Log;

import com.mc.vending.data.BaseData;
import com.mc.vending.parse.listener.DataParseListener;
import com.mc.vending.parse.listener.DataParseRequestListener;

public class InitDataParse implements DataParseListener {

    private DataParseRequestListener listener;

    public DataParseRequestListener getListener() {
        return listener;
    }

    public void setListener(DataParseRequestListener listener) {
        this.listener = listener;
    }

    private static InitDataParse instance = null;

    public InitDataParse() {
    }

    public static InitDataParse getInstance() {
        if (instance == null) {
            instance = new InitDataParse();
        }
        return instance;
    }

    @Override
    public void parseJson(BaseData baseData) {
        if (listener != null) {
            listener.parseRequestFinised(baseData);
        }
    }

    /**
     * 网络请求
     * 
     * @param json
     * @param requestURL
     */
    public void requestInitData(String optType, String requestURL, String vendingCode) {
        JSONObject json = new JSONObject();
        try {
            json.put("VD1_CODE", vendingCode);
            DataParseHelper helper = new DataParseHelper(this);
            helper.requestSubmitServer(optType, json, requestURL);
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(this.getClass().toString(), "======>>>>>初始化请求验证网络请求数据异常!");
        }
    }

    @Override
    public void parseRequestError(BaseData baseData) {
        if (listener != null) {
            listener.parseRequestFailure(baseData);
        }

    }

}
