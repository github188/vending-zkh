package com.mc.vending.parse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.mc.vending.data.BaseData;
import com.mc.vending.data.ProductMaterialPowerData;
import com.mc.vending.db.ProductMaterialPowerDbOper;
import com.mc.vending.parse.listener.DataParseListener;
import com.mc.vending.parse.listener.DataParseRequestListener;
import com.mc.vending.tools.ConvertHelper;
import com.mc.vending.tools.ZillionLog;

public class ProductMaterialPowerDataParse implements DataParseListener {
    private static ProductMaterialPowerDataParse instance = null;
    private DataParseRequestListener             listener;

    public DataParseRequestListener getListener() {
        return listener;
    }

    public void setListener(DataParseRequestListener listener) {
        this.listener = listener;
    }

    public ProductMaterialPowerDataParse() {
    }

    public static ProductMaterialPowerDataParse getInstance() {
        if (instance == null) {
            instance = new ProductMaterialPowerDataParse();
        }
        return instance;
    }

    /**
     * 网络请求,下载数据
     */
    public void requestProductMaterialPowerData(String optType, String requestURL, String vendingId) {
        JSONObject json = new JSONObject();
        try {
            json.put("VD1_ID", vendingId);
            DataParseHelper helper = new DataParseHelper(this);
            helper.requestSubmitServer(optType, json, requestURL);
        } catch (Exception e) {
            e.printStackTrace();
            ZillionLog.e(this.getClass().toString(), "======>>>>>产品领料权限数据异常!");
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
        List<ProductMaterialPowerData> list = parse(baseData.getData());
        // 0全表时不需要删除原数据-false。1表示需要删除true
        if (list.isEmpty() && !baseData.getDeleteFlag()) {
            if (this.listener != null) {
                this.listener.parseRequestFinised(baseData);
            }
            return;
        }
        ProductMaterialPowerDbOper productMaterialPowerDbOper = new ProductMaterialPowerDbOper();
        // 0全表时不需要删除原数据-false。1表示需要删除true
        boolean deleteFlag = productMaterialPowerDbOper.deleteAll();
        if (deleteFlag) {
            boolean addFlag = productMaterialPowerDbOper.batchAddProductMaterialPower(list);
            if (addFlag) {
                Log.i("[productMaterialPower]:", "======>>>>>产品领料权限批量增加成功!" + "====" + list.size());
                DataParseHelper parseHelper = new DataParseHelper(this);
                parseHelper.sendLogVersion(list.get(0).getLogVersion());
            } else {
                ZillionLog.e("[productMaterialPower]:", "==========>>>>>产品领料权限批量增加失败!");
            }
        }

        // System.out.println(productMaterialPowerDbOper.findAll());

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
    public List<ProductMaterialPowerData> parse(JSONArray jsonArray) {
        if (jsonArray == null) {
            return new ArrayList<ProductMaterialPowerData>(0);
        }
        List<ProductMaterialPowerData> list = new ArrayList<ProductMaterialPowerData>();
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
                ProductMaterialPowerData data = new ProductMaterialPowerData();
                data.setPm1Id(jsonObj.getString("ID"));
                data.setPm1M02Id(jsonObj.getString("PM1_M02_ID"));
                data.setPm1Cu1Id(jsonObj.getString("PM1_CU1_ID"));
                data.setPm1Vc2Id(jsonObj.getString("PM1_VC2_ID"));
                data.setPm1Vp1Id(jsonObj.getString("PM1_VP1_ID"));
                data.setPm1Power(jsonObj.getString("PM1_Power"));
                data.setPm1OnceQty(ConvertHelper.toInt(jsonObj.getString("PM1_OnceQty"), 0));
                data.setPm1Period(jsonObj.getString("PM1_Period"));
                data.setPm1IntervalStart(jsonObj.getString("PM1_IntervalStart"));
                data.setPm1IntervalFinish(jsonObj.getString("PM1_IntervalFinish"));
                data.setPm1StartDate(jsonObj.getString("PM1_StartDate"));
                data.setPm1PeriodQty(ConvertHelper.toInt(jsonObj.getString("PM1_PeriodQty"), 0));
                data.setLogVersion(jsonObj.getString("LogVision"));
                data.setPm1CreateUser(createUser);
                data.setPm1CreateTime(createTime);
                data.setPm1ModifyUser(modifyUser);
                data.setPm1ModifyTime(modifyTime);
                data.setPm1RowVersion(rowVersion);
                list.add(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
            ZillionLog.e(this.getClass().toString(), "======>>>>>产品领料权限解析数据异常!");
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
