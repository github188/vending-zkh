package com.mc.vending.parse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.mc.vending.data.BaseData;
import com.mc.vending.data.VendingChnData;
import com.mc.vending.db.VendingChnDbOper;
import com.mc.vending.parse.listener.DataParseListener;
import com.mc.vending.parse.listener.DataParseRequestListener;
import com.mc.vending.tools.ConvertHelper;
import com.mc.vending.tools.ZillionLog;

public class VendingChnDataParse implements DataParseListener {
    private static VendingChnDataParse instance = null;
    private DataParseRequestListener   listener;

    public DataParseRequestListener getListener() {
        return listener;
    }

    public void setListener(DataParseRequestListener listener) {
        this.listener = listener;
    }

    public VendingChnDataParse() {
    }

    public static VendingChnDataParse getInstance() {
        if (instance == null) {
            instance = new VendingChnDataParse();
        }
        return instance;
    }

    /**
     * 网络请求,下载数据
     */
    public void requestVendingChnData(String optType, String requestURL, String vendingId) {
        JSONObject json = new JSONObject();
        try {
            json.put("VD1_ID", vendingId);
            DataParseHelper helper = new DataParseHelper(this);
            helper.requestSubmitServer(optType, json, requestURL);
        } catch (Exception e) {
            e.printStackTrace();
            ZillionLog.e(this.getClass().toString(), "======>>>>>售货机货道网络请求数据异常!");
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
        List<VendingChnData> list = parse(baseData.getData());
        // 0全表时不需要删除原数据-false。1表示需要删除true
        if (list.isEmpty() && !baseData.getDeleteFlag()) {
            if (this.listener != null) {
                this.listener.parseRequestFinised(baseData);
            }
            return;
        }
        VendingChnDbOper vendingChnDbOper = new VendingChnDbOper();
        boolean deleteFlag = vendingChnDbOper.deleteAll();
        if (deleteFlag) {
            // 批量增加售货机货道记录与售货机货道库存记录
            boolean addflag = vendingChnDbOper.batchAddVendingChn(list);
            if (addflag) {
                Log.i("[vendingChn]:", "======>>>>>售货机货道批量增加成功!" + list.size());
                DataParseHelper parseHelper = new DataParseHelper(this);
                parseHelper.sendLogVersion(list.get(0).getLogVersion());
            } else {
                ZillionLog.e("[vendingChn]:", "==========>>>>>售货机货道批量增加失败!");
            }
        }
        // System.out.println(vendingChnDbOper.findAll());
        // System.out.println(new VendingChnStockDbOper().findAll());

        if (this.listener != null) {
            this.listener.parseRequestFinised(baseData);
        }
    }

    public List<VendingChnData> parse(JSONArray jsonArray) {
        if (jsonArray == null) {
            return new ArrayList<VendingChnData>(0);
        }
        List<VendingChnData> list = new ArrayList<VendingChnData>();
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

                VendingChnData data = new VendingChnData();
                data.setVc1Id(jsonObj.getString("ID"));
                data.setVc1M02Id(jsonObj.getString("VC1_M02_ID"));
                data.setVc1Vd1Id(jsonObj.getString("VC1_VD1_ID"));
                data.setVc1Code(jsonObj.getString("VC1_CODE"));
                data.setVc1Type(jsonObj.getString("VC1_Type"));
                data.setVc1Capacity(ConvertHelper.toInt(jsonObj.getString("VC1_Capacity"), 0));
                data.setVc1ThreadSize(jsonObj.getString("VC1_ThreadSize"));
                data.setVc1Pd1Id(jsonObj.getString("VC1_PD1_ID"));
                data.setVc1SaleType(jsonObj.getString("VC1_SaleType"));
                data.setVc1Sp1Id(jsonObj.getString("VC1_SP1_ID"));
                data.setVc1BorrowStatus(jsonObj.getString("VC1_BorrowStatus"));
                data.setVc1Status(jsonObj.getString("VC1_Status"));
                data.setVc1LineNum(jsonObj.getString("VC1_LineNum"));
                data.setVc1ColumnNum(jsonObj.getString("VC1_ColumnNum"));
                data.setVc1Height(jsonObj.getString("VC1_Height"));
                data.setLogVersion(jsonObj.getString("LogVision"));
                data.setVc1CreateUser(createUser);
                data.setVc1CreateTime(createTime);
                data.setVc1ModifyUser(modifyUser);
                data.setVc1ModifyTime(modifyTime);
                data.setVc1RowVersion(rowVersion);
                list.add(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
            ZillionLog.e(this.getClass().toString(), "======>>>>>售货机货道解析数据异常!");
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
