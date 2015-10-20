package com.mc.vending.parse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.mc.vending.data.BaseData;
import com.mc.vending.data.ProductGroupPowerData;
import com.mc.vending.db.ProductGroupPowerDbOper;
import com.mc.vending.parse.listener.DataParseListener;
import com.mc.vending.parse.listener.DataParseRequestListener;
import com.mc.vending.tools.ConvertHelper;

public class ProductGroupPowerDataParse implements DataParseListener {
    private static ProductGroupPowerDataParse instance = null;
    private DataParseRequestListener          listener;

    public DataParseRequestListener getListener() {
        return listener;
    }

    public void setListener(DataParseRequestListener listener) {
        this.listener = listener;
    }

    public ProductGroupPowerDataParse() {
    }

    public static ProductGroupPowerDataParse getInstance() {
        if (instance == null) {
            instance = new ProductGroupPowerDataParse();
        }
        return instance;
    }

    /**
     * 网络请求，下载数据
     */
    public void requestProductGroupPowerData(String optType, String requestURL, String vendingId) {
        JSONObject json = new JSONObject();
        try {
            json.put("VD1_ID", vendingId);
            DataParseHelper helper = new DataParseHelper(this);
            helper.requestSubmitServer(optType, json, requestURL);
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(this.getClass().toString(), "======>>>>>产品组合权限网络请求数据异常!");
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
        List<ProductGroupPowerData> list = parse(baseData.getData());
        // 0全表时不需要删除原数据-false。1表示需要删除true
        if (list.isEmpty() && !baseData.getDeleteFlag()) {
            if (this.listener != null) {
                this.listener.parseRequestFinised(baseData);
            }
            return;
        }
        ProductGroupPowerDbOper productGroupPowerDbOper = new ProductGroupPowerDbOper();
        boolean deleteFlag = productGroupPowerDbOper.deleteAll();
        if (deleteFlag) {
            boolean addFlag = productGroupPowerDbOper.batchAddProductGroupPower(list);
            if (addFlag) {
                Log.i("[productGroupPower]:", "==========>>>>>产品组合权限批量增加成功!" + "==========" + list.size());
                DataParseHelper parseHelper = new DataParseHelper(this);
                parseHelper.sendLogVersion(list.get(0).getLogVersion());
            } else {
                Log.i("[productGroupPower]:", "==========>>>>>产品组合权限批量增加失败!");
            }
        }

        // System.out.println(productGroupPowerDbOper.findAll());
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
    public List<ProductGroupPowerData> parse(JSONArray jsonArray) {
        if (jsonArray == null) {
            return new ArrayList<ProductGroupPowerData>(0);
        }
        List<ProductGroupPowerData> list = new ArrayList<ProductGroupPowerData>();
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
                ProductGroupPowerData data = new ProductGroupPowerData();
                data.setPp1Id(jsonObj.getString("ID"));
                data.setPp1M02Id(jsonObj.getString("PP1_M02_ID"));
                data.setPp1Cu1Id(jsonObj.getString("PP1_CU1_ID"));
                data.setPp1Pg1Id(jsonObj.getString("PP1_PG1_ID"));
                data.setPp1Cd1Id(jsonObj.getString("PP1_CD1_ID"));
                data.setPp1Power(jsonObj.getString("PP1_Power"));
                data.setPp1Period(jsonObj.getString("PP1_Period"));
                data.setPp1IntervalStart(jsonObj.getString("PP1_IntervalStart"));
                data.setPp1IntervalFinish(jsonObj.getString("PP1_IntervalFinish"));
                data.setPp1StartDate(jsonObj.getString("PP1_StartDate"));
                data.setPp1PeriodNum(ConvertHelper.toInt(jsonObj.getString("PP1_PeriodNum"), 0));
                data.setLogVersion(jsonObj.getString("LogVision"));
                data.setPp1CreateUser(createUser);
                data.setPp1CreateTime(createTime);
                data.setPp1ModifyUser(modifyUser);
                data.setPp1ModifyTime(modifyTime);
                data.setPp1RowVersion(rowVersion);

                list.add(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(this.getClass().toString(), "======>>>>>产品组合权限解析数据异常!");
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
