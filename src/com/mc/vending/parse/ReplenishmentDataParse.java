package com.mc.vending.parse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.mc.vending.config.Constant;
import com.mc.vending.data.BaseData;
import com.mc.vending.data.ReplenishmentDetailData;
import com.mc.vending.data.ReplenishmentHeadData;
import com.mc.vending.db.ReplenishmentDetailDbOper;
import com.mc.vending.db.ReplenishmentHeadDbOper;
import com.mc.vending.parse.listener.DataParseListener;
import com.mc.vending.parse.listener.DataParseRequestListener;
import com.mc.vending.tools.ConvertHelper;

public class ReplenishmentDataParse implements DataParseListener {
    private static ReplenishmentDataParse instance = null;
    private DataParseRequestListener      listener;

    public DataParseRequestListener getListener() {
        return listener;
    }

    public void setListener(DataParseRequestListener listener) {
        this.listener = listener;
    }

    public ReplenishmentDataParse() {
    }

    public static ReplenishmentDataParse getInstance() {
        if (instance == null) {
            instance = new ReplenishmentDataParse();
        }
        return instance;
    }

    /**
     * 网络请求下载数据 上传状态到云端
     * 
     * @param optType
     * @param requestURL
     * @param vendingId
     */
    public void requestReplenishmentData(String optType, String requestURL, String vendingId, int rowCount) {
        try {
            if (Constant.HTTP_OPERATE_TYPE_GETDATA.equals(optType)) {
                // 补货差异
                JSONObject json = new JSONObject();
                json.put("VD1_ID", vendingId);
                json.put("RowCount", rowCount);
                DataParseHelper helper = new DataParseHelper(this);
                helper.requestSubmitServer(optType, json, requestURL);

            } else if (Constant.HTTP_OPERATE_TYPE_UPDATESTATUS.equals(optType)) {
                // 补货单状态更新
                JSONArray jsonArray = null;
                List<ReplenishmentHeadData> datas = new ReplenishmentHeadDbOper()
                        .findReplenishmentHeadToUpload();
                if (datas.isEmpty())
                    return;
                jsonArray = new JSONArray();
                for (ReplenishmentHeadData replenishmentHeadData : datas) {
                    JSONObject json = new JSONObject();
                    json.put("VD1_ID", vendingId);
                    json.put("RH1_ID", replenishmentHeadData.getRh1Id());
                    String state = replenishmentHeadData.getRh1OrderStatus();
                    json.put("RH1_OrderStatus", state);
                    jsonArray.put(json);

                }
                if (jsonArray.length() > 0) {
                    DataParseHelper helper = new DataParseHelper(this);
                    helper.requestSubmitServer(optType, jsonArray, requestURL, datas);
                }
            } else if (Constant.HTTP_OPERATE_TYPE_UPDATEDETAILDIFFERENTIAQTY.equals(optType)) {
                // 补货单差异更新
                JSONArray jsonArray = null;

                List<ReplenishmentDetailData> datas = new ReplenishmentDetailDbOper()
                        .findReplenishmentDetailToUpload();
                if (datas.isEmpty())
                    return;
                jsonArray = new JSONArray();
                for (ReplenishmentDetailData replenishmentDetailData : datas) {
                    JSONObject json = new JSONObject();
                    json.put("VD1_ID", vendingId);
                    json.put("RH2_ID", replenishmentDetailData.getRh2Id());
                    json.put("RH2_DifferentiaQty", replenishmentDetailData.getRh2DifferentiaQty());
                    jsonArray.put(json);
                }
                if (jsonArray.length() > 0) {
                    DataParseHelper helper = new DataParseHelper(this);
                    helper.requestSubmitServer(optType, jsonArray, requestURL, datas);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(this.getClass().toString(), "======>>>>>补货单/补货单状态更新/补化差异网络请求数据异常!");
        }

    }

    @Override
    public void parseJson(BaseData baseData) {
        if (Constant.HTTP_OPERATE_TYPE_UPDATESTATUS.equals(baseData.getOptType())) {// 3.24
                                                                                    // 补货单状态更新
            if (!baseData.isSuccess()) {
                baseData = null;
                return;
            }
            List<ReplenishmentHeadData> datas = (List<ReplenishmentHeadData>) baseData.getUserObject();
            if (datas.isEmpty()) {
                baseData = null;
                return;
            }
            boolean _flag = new ReplenishmentHeadDbOper().batchUpdateUploadStatus(datas);
            if (_flag) {
                Log.i("[replenishment]:", "======>>>>>补货单状态更新上传状态批量更新成功!" + datas.size());
            } else {
                Log.i("[replenishment]:", "==========>>>>>补货单状态更新上传状态批量更新失败!");
            }
            // System.out.println("=========>>" +
            // replenishmentHeadDbOper.findAll());
        } else if (Constant.HTTP_OPERATE_TYPE_UPDATEDETAILDIFFERENTIAQTY.equals(baseData.getOptType())) {
            // 补货单差异更新
            if (!baseData.isSuccess()) {
                baseData = null;
                return;
            }
            List<ReplenishmentDetailData> datas = (List<ReplenishmentDetailData>) baseData.getUserObject();
            if (datas.isEmpty()) {
                baseData = null;
                return;
            }
            boolean flag_ = new ReplenishmentDetailDbOper().batchUpdateUploadStatus(datas);
            if (flag_) {
                Log.i("[replenishment]:", "======>>>>>补货差异上传状态批量更新成功!" + datas.size());
            } else {
                Log.i("[replenishment]:", "==========>>>>>补货差异上传状态批量更新失败!");
            }
            // System.out.println("--------->>" +
            // replenishmentDetailDbOper.findAll());

        } else if (Constant.HTTP_OPERATE_TYPE_GETDATA.equals(baseData.getOptType())) {
            if (!baseData.isSuccess()) {
                if (this.listener != null) {
                    this.listener.parseRequestFailure(baseData);
                }
                return;
            }
            List<ReplenishmentHeadData> list = parse(baseData.getData());
            if (list.isEmpty()) {
                if (this.listener != null) {
                    this.listener.parseRequestFinised(baseData);
                }
                return;
            }

            ReplenishmentHeadDbOper replenishmentHeadDbOper = new ReplenishmentHeadDbOper();
            List<ReplenishmentHeadData> addList = new ArrayList<ReplenishmentHeadData>();
            List<ReplenishmentHeadData> updateList = new ArrayList<ReplenishmentHeadData>();
            Map<String, String> map = replenishmentHeadDbOper.findAllMap();
            for (ReplenishmentHeadData replenishmentHead : list) {
                if (map.containsKey(replenishmentHead.getRh1Id())) {
                    updateList.add(replenishmentHead);
                } else {
                    addList.add(replenishmentHead);
                }
//                if (ReplenishmentHeadData.ORDERSTATUS_CREATED.equals(replenishmentHead.getRh1OrderStatus())) {
//                    addList.add(replenishmentHead);
//                }
//                if (ReplenishmentHeadData.ORDERSTATUS_CLOSED.equals(replenishmentHead.getRh1OrderStatus())) {
//                    if (map.get(replenishmentHead.getRh1Id()) != null) {
//                        updateList.add(replenishmentHead);
//                    }
//                }
            }
            // 增量 测试时加上这句话
            // replenishmentHeadDbOper.deleteAll();
            if (!addList.isEmpty()) {
                // 批量增加主表与从表数据
                boolean flag = replenishmentHeadDbOper.batchAddReplenishmentHead(addList);
                if (flag) {
                    Log.i("[replenishment]:", "======>>>>>补货单批量增加成功!" + addList.size());
                    DataParseHelper parseHelper = new DataParseHelper(this);
                    parseHelper.sendLogVersion(list.get(0).getLogVersion());
                } else {
                    Log.i("[replenishment]:", "==========>>>>>补货单批量增加失败!");
                }
            } else if (!updateList.isEmpty()) {
                boolean flag_ = replenishmentHeadDbOper.batchUpdateReplenishmentHead(updateList);
                if (flag_) {
                    Log.i("[replenishment]:", "======>>>>>补货单批量更新订单状态成功!" + updateList.size());
                    DataParseHelper parseHelper = new DataParseHelper(this);
                    parseHelper.sendLogVersion(list.get(0).getLogVersion());
                } else {
                    Log.i("[replenishment]:", "==========>>>>>补货单批量更新订单失败!");
                }
            } else {
                DataParseHelper parseHelper = new DataParseHelper(this);
                parseHelper.sendLogVersion(list.get(0).getLogVersion());
            }

            // System.out.println("=========>>" +
            // replenishmentHeadDbOper.findAll());
            // System.out.println("--------->>" + new
            // ReplenishmentDetailDbOper().findAll());
            if (this.listener != null) {
                this.listener.parseRequestFinised(baseData);
            }
        }
    }

    /**
     * 数据解析
     * 
     * @param jsonArray
     * @return
     */
    public List<ReplenishmentHeadData> parse(JSONArray jsonArray) {
        if (jsonArray == null) {
            return new ArrayList<ReplenishmentHeadData>(0);
        }
        List<ReplenishmentHeadData> list = new ArrayList<ReplenishmentHeadData>();
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
                ReplenishmentHeadData data = new ReplenishmentHeadData();
                data.setRh1Id(jsonObj.getString("ID"));
                data.setRh1M02Id(jsonObj.getString("RH1_M02_ID"));
                data.setRh1Rhcode(jsonObj.getString("RH1_RHCODE"));
                data.setRh1RhType(jsonObj.getString("RH1_RhType"));
                data.setRh1Cu1Id(jsonObj.getString("RH1_CU1_ID"));

                data.setRh1Vd1Id(jsonObj.getString("RH1_VD1_ID"));
                data.setRh1Wh1Id(jsonObj.getString("RH1_WH1_ID"));
                data.setRh1Ce1IdPh(jsonObj.getString("RH1_CE1_ID_PH"));
                data.setRh1DistributionRemark(jsonObj.getString("RH1_DistributionRemark"));
                data.setRh1St1Id(jsonObj.getString("RH1_ST1_ID"));

                data.setRh1Ce1IdBh(jsonObj.getString("RH1_CE1_ID_BH"));
                data.setRh1ReplenishRemark(jsonObj.getString("RH1_ReplenishRemark"));
                data.setRh1ReplenishReason(jsonObj.getString("RH1_ReplenishReason"));
                data.setRh1OrderStatus(jsonObj.getString("RH1_OrderStatus"));
                data.setRh1DownloadStatus(jsonObj.getString("RH1_DownloadStatus"));
                data.setLogVersion(jsonObj.getString("LogVision"));
                data.setRh1UploadStatus(ReplenishmentHeadData.UPLOAD_UNLOAD);

                data.setRh1CreateUser(createUser);
                data.setRh1CreateTime(createTime);
                data.setRh1ModifyUser(modifyUser);
                data.setRh1ModifyTime(modifyTime);
                data.setRh1RowVersion(rowVersion);
                List<ReplenishmentDetailData> children = new ArrayList<ReplenishmentDetailData>();
                if (jsonObj.get("Detail") instanceof JSONArray) {
                    JSONArray detailArray = jsonObj.getJSONArray("Detail");
                    if (detailArray != null) {
                        for (int j = 0; j < detailArray.length(); j++) {
                            JSONObject detailJsonObj = detailArray.getJSONObject(j);
                            if (detailJsonObj == null)
                                continue;
                            String detil_createUser = detailJsonObj.getString("CreateUser");
                            String detil_createTime = detailJsonObj.getString("CreateTime");
                            String detil_modifyUser = detailJsonObj.getString("ModifyUser");
                            String detil_modifyTime = detailJsonObj.getString("ModifyTime");
                            ReplenishmentDetailData detail = new ReplenishmentDetailData();
                            detail.setRh2Id(detailJsonObj.getString("ID"));
                            detail.setRh2M02Id(detailJsonObj.getString("RH2_M02_ID"));
                            detail.setRh2Rh1Id(detailJsonObj.getString("RH2_RH1_ID"));
                            detail.setRh2Vc1Code(detailJsonObj.getString("RH2_VC1_CODE"));
                            detail.setRh2Pd1Id(detailJsonObj.getString("RH2_PD1_ID"));
                            detail.setRh2SaleType(detailJsonObj.getString("RH2_SaleType"));
                            detail.setRh2Sp1Id(detailJsonObj.getString("RH2_SP1_ID"));
                            detail.setRh2ActualQty(ConvertHelper.toInt(
                                    detailJsonObj.getString("RH2_ActualQty"), 0));
                            detail.setRh2DifferentiaQty(ConvertHelper.toInt(
                                    detailJsonObj.getString("RH2_DifferentiaQty"), 0));
                            detail.setRh2Rp1Id(detailJsonObj.getString("RH2_RP1_ID"));
                            detail.setRh2UploadStatus(ReplenishmentDetailData.UPLOAD_UNLOAD);
                            detail.setRh2CreateUser(detil_createUser);
                            detail.setRh2CreateTime(detil_createTime);
                            detail.setRh2ModifyUser(detil_modifyUser);
                            detail.setRh2ModifyTime(detil_modifyTime);
                            detail.setRh2RowVersion(rowVersion);

                            children.add(detail);
                        }
                    }

                    data.setChildren(children);
                }
                list.add(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(this.getClass().toString(), "======>>>>>补货单解析数据异常!");
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
