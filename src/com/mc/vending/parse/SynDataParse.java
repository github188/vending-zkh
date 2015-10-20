package com.mc.vending.parse;

import org.json.JSONObject;

import android.util.Log;

import com.mc.vending.data.BaseData;
import com.mc.vending.parse.listener.DataParseListener;
import com.mc.vending.parse.listener.DataParseRequestListener;

public class SynDataParse implements DataParseListener {

    private DataParseRequestListener listener;

    public DataParseRequestListener getListener() {
        return listener;
    }

    public void setListener(DataParseRequestListener listener) {
        this.listener = listener;
    }

    private static SynDataParse instance = null;

    public SynDataParse() {
    }

    public static SynDataParse getInstance() {
        if (instance == null) {
            instance = new SynDataParse();
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

        if (listener != null) {
            listener.parseRequestFinised(baseData);
        }
    }

    //    {'XmlInput':'{
    //        "wsid":"6e2164cc-45a9-46a4-85a4-b6e3377ebc33",
    //        "deviceid":"ddddddd",
    //        "app":"evmandroid",
    //        "user":"",
    //        "pwd":"M+/9h5/h0VmWj4ArmPA/eFdw7WuODCTm",
    //        "data":[{"optype":"GetData"
    //            }]
    //        }'
    //        } 

    /**
     * 网络请求
     * 
     * @param json
     * @param requestURL
     */
    public void requestSynData(String optType, String requestURL) {
        JSONObject json = new JSONObject();
        try {
            DataParseHelper helper = new DataParseHelper(this);
            helper.requestSubmitServer(optType, json, requestURL);
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(this.getClass().toString(), "======>>>>>同步预先请求数据异常!");
        }
    }

    @Override
    public void parseRequestError(BaseData baseData) {
        if (listener != null) {
            listener.parseRequestFailure(baseData);
        }

    }

}
