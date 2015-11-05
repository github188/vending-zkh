package com.mc.vending.parse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mc.vending.config.Constant;
import com.mc.vending.data.BaseData;
import com.mc.vending.data.VersionData;
import com.mc.vending.parse.listener.DataParseListener;
import com.mc.vending.parse.listener.DataParseRequestListener;
import com.mc.vending.tools.ZillionLog;

public class VersionDataParse implements DataParseListener {
    private DataParseRequestListener listener;

    public DataParseRequestListener getListener() {
        return listener;
    }

    public void setListener(DataParseRequestListener listener) {
        this.listener = listener;
    }

    private static VersionDataParse instance = null;

    public VersionDataParse() {
    }

    public static VersionDataParse getInstance() {
        if (instance == null) {
            instance = new VersionDataParse();
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
                VersionData data = new VersionData();
                data.setVersion(jsonObj.getString("AP1_Version"));
                data.setDownloadURL(jsonObj.getString("AP1_FilePath"));
                // TODO:获取接口权限加密密码
                baseData.setUserObject(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
            ZillionLog.e(this.getClass().toString(), "======>>>>>版本信息接口解析异常!");
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
    public void requestVersionData(String optType, String requestURL) {
        JSONObject json = new JSONObject();
        
        try {
            json.put("VD1_CurrentVersion", Constant.HEADER_VALUE_CLIENTVER);

            DataParseHelper helper = new DataParseHelper(this);
            helper.requestSubmitServer(optType, json, requestURL);
        } catch (Exception e) {
            e.printStackTrace();
            ZillionLog.e(this.getClass().toString(), "======>>>>>版本信息接口请求数据异常!");
        }
    }

    @Override
    public void parseRequestError(BaseData baseData) {
        if (listener != null) {
            listener.parseRequestFailure(baseData);
        }

    }
}
