package com.mc.vending.parse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mc.vending.data.BaseData;
import com.mc.vending.parse.listener.DataParseListener;
import com.mc.vending.parse.listener.DataParseRequestListener;
import com.mc.vending.tools.ZillionLog;

public class AutherDataParse implements DataParseListener {

    private DataParseRequestListener listener;

    public DataParseRequestListener getListener() {
        return listener;
    }

    public void setListener(DataParseRequestListener listener) {
        this.listener = listener;
    }

    private static AutherDataParse instance = null;

    public AutherDataParse() {
    }

    public static AutherDataParse getInstance() {
        if (instance == null) {
            instance = new AutherDataParse();
        }
        return instance;
    }

    @Override
    public void parseJson(BaseData baseData) {
        if (!baseData.isSuccess()) {
            if (listener != null) {
                listener.parseRequestFailure(baseData);
            }
            return;
        }

        if (baseData==null || baseData.getData() == null || baseData.getData().length()==0) {
            if (listener != null) {
                listener.parseRequestFailure(baseData);
            }
            return ;
        }
        JSONArray jsonArray = baseData.getData();
        try {
            if (jsonArray == null) {
                if (listener != null) {
                    listener.parseRequestFailure(baseData);
                }
                return;
            }
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObj = jsonArray.getJSONObject(i);
                if (jsonObj == null)
                    continue;

                // String deviceId = jsonObj.getString("DeviceID");
                String key = jsonObj.getString("Key");
                // TODO:获取接口权限加密密码
                baseData.setUserObject(key);
            }
        } catch (Exception e) {
            ZillionLog.e(this.getClass().toString(), "======>>>>>接口认证解析数据异常!", e);
            e.printStackTrace();
        }
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
    public void requestAutherData(String optType, String requestURL, String deviceId) {
        JSONObject json = new JSONObject();
        try {
            json.put("deviceid", deviceId);
            DataParseHelper helper = new DataParseHelper(this);
            helper.requestSubmitServer(optType, json, requestURL);
        } catch (Exception e) {
            ZillionLog.e(this.getClass().toString(), "======>>>>>接口认证网络请求数据异常!",e);
            e.printStackTrace();
        }
    }

    @Override
    public void parseRequestError(BaseData baseData) {
        if (listener != null) {
            listener.parseRequestFailure(baseData);
        }

    }

}
