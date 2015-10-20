package com.mc.vending.parse;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.mc.vending.data.BaseData;
import com.mc.vending.data.StockTransactionData;
import com.mc.vending.db.StockTransactionDbOper;
import com.mc.vending.parse.listener.DataParseListener;

public class StockTransactionDataParse implements DataParseListener {
    private static StockTransactionDataParse instance = null;

    public StockTransactionDataParse() {
    }

    public static StockTransactionDataParse getInstance() {
        if (instance == null) {
            instance = new StockTransactionDataParse();
        }
        return instance;
    }

    /**
     * @param optType
     * @param requestURL
     * @param vendingId
     */
    public void requestStockTransactionData(String optType, String requestURL, String vendingId) {
        JSONArray jsonArray = null;
        List<StockTransactionData> datas = new StockTransactionDbOper().findStockTransactionDataToUpload();

        try {
            jsonArray = new JSONArray();
            for (StockTransactionData data : datas) {
                JSONObject json = new JSONObject();
                json.put("ID", data.getTs1Id());
                json.put("TS1_M02_ID", data.getTs1M02Id());
                json.put("TS1_BillType", data.getTs1BillType());
                json.put("TS1_BillCode", data.getTs1BillCode());
                json.put("TS1_CD1_ID", data.getTs1Cd1Id());
                json.put("TS1_VD1_ID", data.getTs1Vd1Id());
                json.put("TS1_PD1_ID", data.getTs1Pd1Id());
                json.put("TS1_VC1_CODE", data.getTs1Vc1Code());
                json.put("TS1_TransQty", data.getTs1TransQty());
                json.put("TS1_TransType", data.getTs1TransType());
                json.put("TS1_SP1_CODE", data.getTs1Sp1Code());
                json.put("TS1_SP1_Name", data.getTs1Sp1Name());
                json.put("CreateUser", data.getTs1CreateUser());
                json.put("CreateTime", data.getTs1CreateTime());
                jsonArray.put(json);
            }
            if (jsonArray.length() > 0) {
                DataParseHelper helper = new DataParseHelper(this);
                helper.requestSubmitServer(optType, jsonArray, requestURL, datas);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(this.getClass().toString(), "======>>>>>库存交易记录网络请求数据异常!");
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void parseJson(BaseData baseData) {
        if (!baseData.isSuccess()) {
            baseData = null;
            return;
        }
        List<StockTransactionData> datas = (List<StockTransactionData>) baseData.getUserObject();
        if (datas.isEmpty()) {
            baseData = null;
            return;
        }
        boolean flag = new StockTransactionDbOper().batchUpdateUploadStatus(datas);
        if (flag) {
            Log.i("[stockTransaction]:", "======>>>>>库存交易记录上传状态批量更新成功!" + datas.size());
        } else {
            Log.i("[stockTransaction]:", "==========>>>>库存交易记录上传状态批量增加失败!");
        }
        baseData = null;
    }

    @Override
    public void parseRequestError(BaseData baseData) {
        // TODO Auto-generated method stub

    }

}
