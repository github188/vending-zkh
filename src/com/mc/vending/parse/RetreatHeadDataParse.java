package com.mc.vending.parse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.mc.vending.data.BaseData;
import com.mc.vending.data.RetreatDetailData;
import com.mc.vending.data.RetreatHeadData;
import com.mc.vending.db.RetreatHeadDbOper;
import com.mc.vending.parse.listener.DataParseRequestListener;

public class RetreatHeadDataParse extends BaseDataParse {
    private DataParseRequestListener listener;

    public DataParseRequestListener getListener() {
        return listener;
    }

    public void setListener(DataParseRequestListener listener) {
        this.listener = listener;
    }

    private static RetreatHeadDataParse instance = null;

    public RetreatHeadDataParse() {
    }

    public static RetreatHeadDataParse getInstance() {
        if (instance == null) {
            instance = new RetreatHeadDataParse();
        }
        return instance;
    }

    @Override
    public void parseJson(BaseData baseData) {
        if (!baseData.isSuccess()) {
            if (this.listener != null) {
                this.listener.parseRequestFailure(baseData);
            }
            return;
        }
        // 增量
        List<RetreatHeadData> list = parse(baseData.getData());

        if (list.isEmpty()) {
            if (this.listener != null) {
                this.listener.parseRequestFinised(baseData);
            }
            return;
        }
        RetreatHeadDbOper dbOper = new RetreatHeadDbOper();
        List<RetreatHeadData> addList = new ArrayList<RetreatHeadData>();
        List<RetreatHeadData> updateList = new ArrayList<RetreatHeadData>();

        Map<String, String> map = dbOper.findAllMap();

        for (RetreatHeadData retreatHeadData : list) {
            if (map.containsKey(retreatHeadData.getRt1Id())) {
                updateList.add(retreatHeadData);
            } else {
                addList.add(retreatHeadData);
            }
        }
        if (!addList.isEmpty()) {
            // 批量增加
            boolean flag = dbOper.batchAddReturnForward(addList);
            if (flag) {
                Log.i("[ReturnForward]:", "======>>>>>批量增加成功!" + "==========" + list.size());
                callBackLogversion(list.get(0));
            } else {
                Log.i("[ReturnForward]:", "==========>>>>>批量增加失败!");
            }
        } else if (!updateList.isEmpty()) {
            // 批量增加
            boolean flag = dbOper.batchUpdateReturnForward(updateList);
            if (flag) {
                Log.i("[ReturnForward]:", "======>>>>>批量更新成功!" + "==========" + list.size());
                callBackLogversion(list.get(0));
            } else {
                Log.i("[ReturnForward]:", "==========>>>>>批量更新失败!");
            }
        } else {

            callBackLogversion(list.get(0));
        }

        if (this.listener != null) {
            this.listener.parseRequestFinised(baseData);
        }
    }

    /**
     * 网络请求
     * 
     * @param json
     * @param requestURL
     */
    public void requestReturnForward(String optType, String requestURL, String vendingId, int rowCount) {
        JSONObject json = new JSONObject();
        try {
            json.put("VD1_ID", vendingId);
            json.put("RowCount", rowCount);
            DataParseHelper helper = new DataParseHelper(this);
            helper.requestSubmitServer(optType, json, requestURL, rowCount);
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(this.getClass().toString(), "======>>>>>退货单网络请求数据异常!");
        }
    }

    /**
     * 数据解析
     * 
     * @param jsonArray
     * @return
     */
    public List<RetreatHeadData> parse(JSONArray jsonArray) {
        if (jsonArray == null) {
            return new ArrayList<RetreatHeadData>(0);
        }
        List<RetreatHeadData> list = new ArrayList<RetreatHeadData>();
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

                RetreatHeadData data = new RetreatHeadData();
                data.setRt1Id(jsonObj.getString("ID"));
                data.setRt1Ce1Id(jsonObj.getString("RT1_CE1_ID"));
                data.setRt1Cu1Id(jsonObj.getString("RT1_CU1_ID"));
                data.setRt1M02Id(jsonObj.getString("RT1_M02_ID"));
                data.setRt1Rtcode(jsonObj.getString("RT1_RTCode"));
                data.setRt1Status(jsonObj.getString("RT1_Status"));
                data.setRt1Type(jsonObj.getString("RT1_Type"));
                data.setRt1Vd1Id(jsonObj.getString("RT1_VD1_ID"));
                data.setCreateUser(createUser);
                data.setCreateTime(createTime);
                data.setModifyUser(modifyUser);
                data.setModifyTime(modifyTime);
                data.setLogVersion(jsonObj.getString("LogVision"));
                data.setRowVersion(rowVersion);

                JSONArray details = new JSONArray();
                details = jsonObj.getJSONArray("Detail");

                List<RetreatDetailData> retreatDetailDatas = new ArrayList<RetreatDetailData>();
                for (int j = 0; j < details.length(); j++) {
                    JSONObject detail = details.getJSONObject(j);
                    if (detail == null)
                        continue;

                    rowVersion = new Date().getTime() + "";

                    RetreatDetailData detailData = new RetreatDetailData();
                    detailData.setRt2Id(detail.getString("ID"));
                    detailData.setRt2M02Id(detail.getString("RT2_M02_ID"));
                    detailData.setRt2Pd1Id(detail.getString("RT2_PD1_ID"));
                    detailData.setRt2Rt1Id(detail.getString("RT2_RT1_ID"));
                    detailData.setRt2SaleType(detail.getString("RT2_SaleType"));
                    detailData.setRt2Sp1Id(detail.getString("RT2_SP1_ID"));
                    detailData.setRt2Vc1Code(detail.getString("RT2_VC1_CODE"));
                    detailData.setRt2PlanQty(detail.getInt("RT2_PlanQty"));
                    try {
                        detailData.setRt2ActualQty(detail.getInt("RT2_ActualQty"));
                    } catch (Exception e) {
                        detailData.setRt2ActualQty(0);
                    }

                    detailData.setCreateTime(createTime);
                    detailData.setCreateUser(createUser);
                    detailData.setModifyTime(modifyTime);
                    detailData.setModifyUser(modifyUser);
                    detailData.setRowVersion(rowVersion);
                    retreatDetailDatas.add(detailData);
                }

                data.setRetreatDetailDatas(retreatDetailDatas);

                list.add(data);

            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(this.getClass().toString(), "======>>>>>解析数据异常!");
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
