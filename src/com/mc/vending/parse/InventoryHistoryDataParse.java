package com.mc.vending.parse;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.mc.vending.data.BaseData;
import com.mc.vending.data.InventoryHistoryData;
import com.mc.vending.db.InventoryHistoryDbOper;
import com.mc.vending.parse.listener.DataParseListener;
import com.mc.vending.tools.ZillionLog;

public class InventoryHistoryDataParse implements DataParseListener {
    private static InventoryHistoryDataParse instance = null;

    public InventoryHistoryDataParse() {
    }

    public static InventoryHistoryDataParse getInstance() {
        if (instance == null) {
            instance = new InventoryHistoryDataParse();
        }
        return instance;
    }

    /**
     * @param optType
     * @param requestURL
     * @param vendingId
     */
    public void requestInventoryHistoryData(String optType, String requestURL, String vendingId) {
        // 盘点记录同步
        JSONArray jsonArray = null;
        List<InventoryHistoryData> datas = new InventoryHistoryDbOper()
            .findInventoryHistoryDataToUpload();

        try {
            jsonArray = new JSONArray();
            for (InventoryHistoryData data : datas) {
                JSONObject json = new JSONObject();
                json.put("ID", data.getIh3Id());
                json.put("IH3_M02_ID", data.getIh3M02Id());
                json.put("IH3_IHCODE", data.getIh3IHcode());
                json.put("IH3_ActualDate", data.getIh3ActualDate());
                json.put("IH3_CU1_ID", data.getIh3Cu1Id());
                json.put("IH3_InventoryPeople", data.getIh3InventoryPeople());
                json.put("IH3_VD1_ID", data.getIh3Vd1Id());
                json.put("IH3_VC1_CODE", data.getIh3Vc1Code());
                json.put("IH3_PD1_ID", data.getIh3Pd1Id());
                json.put("IH3_Quantity", data.getIh3Quantity());
                json.put("IH3_InventoryQty", data.getIh3InventoryQty());
                json.put("IH3_DifferentiaQty", data.getIh3DifferentiaQty());
                json.put("CreateUser", data.getIh3CreateUser());
                json.put("CreateTime", data.getIh3CreateTime());
                jsonArray.put(json);
            }
            if (jsonArray.length() > 0) {
                DataParseHelper helper = new DataParseHelper(this);
                helper.requestSubmitServer(optType, jsonArray, requestURL, datas);
            }
        } catch (Exception e) {
            e.printStackTrace();
            ZillionLog.e(this.getClass().toString(), "==========>>>>>盘点交易记录网络请求异常!");
        }
    }

    @Override
    public void parseJson(BaseData baseData) {
        if (!baseData.isSuccess()) {
            baseData = null;
            return;
        }
//        if (baseData==null || baseData.getData() == null || baseData.getData().length()==0) {
//            return ;
//        }
        List<InventoryHistoryData> datas = (List<InventoryHistoryData>) baseData.getUserObject();
        if (datas.isEmpty()) {
            baseData = null;
            return;
        }
        boolean flag = new InventoryHistoryDbOper().batchUpdateUploadStatus(datas);
        if (flag) {
            Log.i("[inventoryHistory]:",
                "==========>>>>>盘点交易记录上传状态批量更新成功!" + "========" + datas.size());
        } else {
            ZillionLog.e("[inventoryHistory]:", "盘点交易记录上传状态批量更新失败!");
        }
        baseData = null;
    }

    @Override
    public void parseRequestError(BaseData baseData) {
        // TODO Auto-generated method stub

    }

}
