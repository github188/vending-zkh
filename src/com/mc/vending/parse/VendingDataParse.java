package com.mc.vending.parse;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mc.vending.config.Constant;
import com.mc.vending.data.BaseData;
import com.mc.vending.data.InterfaceData;
import com.mc.vending.data.VendingData;
import com.mc.vending.db.InterfaceDbOper;
import com.mc.vending.db.VendingDbOper;
import com.mc.vending.parse.listener.DataParseListener;
import com.mc.vending.parse.listener.DataParseRequestListener;
import com.zillionstar.tools.L;
import com.zillionstar.tools.ZillionLog;

import android.util.Log;

/**
 * 售货机数据请求与解析
 * 
 * @author apple
 *
 */
public class VendingDataParse implements DataParseListener {
    static {
        L.logLevel = Constant.LOGLEVEL;
    }

    private static VendingDataParse  instance = null;
    private DataParseRequestListener listener;       // 使用单例模式，不能使用listener，或者使用完成后将listener清空

    public DataParseRequestListener getListener() {
        return listener;
    }

    public void setListener(DataParseRequestListener listener) {
        this.listener = listener;
    }

    public void removeListener() {
        this.listener = null;
    }

    public VendingDataParse() {
    }

    public static VendingDataParse getInstance() {
        if (instance == null) {
            instance = new VendingDataParse();
        }
        return instance;
    }

    /**
     * 请求回调方法
     */
    @Override
    public void parseJson(BaseData baseData) {

        if (!baseData.isSuccess()) {
            if (this.listener != null) {
                this.listener.parseRequestFailure(baseData);
            }
            return;
        }
        VendingData vendingData = parse(baseData.getData());
        // 0全表时不需要删除原数据-false。1表示需要删除true
        if (vendingData == null && !baseData.getDeleteFlag()) {
            if (this.listener != null) {
                this.listener.parseRequestFinised(baseData);
            }
            return;
        }

        // 全表，只有一条数据
        VendingDbOper vendingDbOper = new VendingDbOper();
        VendingData vending = vendingDbOper.getVending();
        if (vending != null) {
            if (vending.getVd1Id().equals(vendingData.getVd1Id())) {
                boolean updateFlag = vendingDbOper.updateVending(vendingData);
                if (updateFlag) {
                    L.i("======>>>>>售货机更新成功!");
                    List<String> wsidList = parseWsid(baseData.getWsidData());
                    syncByWsid(wsidList, vendingData.getVd1Id());
                } else {
                    L.i("==========>>>>>售货机更新失败!");
                }
            }
        } else {
            boolean insert_flag = vendingDbOper.addVending(vendingData);
            if (insert_flag) {
                L.i("======>>>>>售货机增加成功!");
                List<String> wsidList = parseWsid(baseData.getWsidData());
                syncByWsid(wsidList, vendingData.getVd1Id());
            } else {
                L.i("==========>>>>>售货机增加失败!");
            }
        }
        // System.out.println(vendingDbOper.getVending());
        if (this.listener != null) {
            this.listener.parseRequestFinised(baseData);
        }
    }

    private void syncByWsid(List<String> wsidList, String vendingId) {

        Map<String, InterfaceData> configMap = new HashMap<String, InterfaceData>();

        List<InterfaceData> configList = new InterfaceDbOper().findAll();
        for (InterfaceData config : configList) {
            String wsid = config.getM03Target().trim();
            String opType = config.getM03Optype().trim();
            configMap.put(wsid + "_" + opType, config);
        }
        if (wsidList == null) {
            return;
        }
        Log.i(this.getClass().getName(), wsidList.toString());
        for (String wsid : wsidList) {
            if (wsid.equals(Constant.METHOD_WSID_VENDINGCHN)) {
                VendingChnDataParse parse = new VendingChnDataParse();
                // parse.setListener(this);
                parse.requestVendingChnData(Constant.HTTP_OPERATE_TYPE_GETDATA,
                        Constant.METHOD_WSID_VENDINGCHN, vendingId);
            } else if (wsid.equals(Constant.METHOD_WSID_VENDINGPICTURE)) {
                VendingPictureDataParse parse = new VendingPictureDataParse();
                parse.requestVendingPictureData(Constant.HTTP_OPERATE_TYPE_GETDATA,
                        Constant.METHOD_WSID_VENDINGPICTURE, vendingId);
            } else if (wsid.equals(Constant.METHOD_WSID_VENDINGPROLINK)) {
                VendingProLinkDataParse parse = new VendingProLinkDataParse();
                parse.requestVendingProLinkData(Constant.HTTP_OPERATE_TYPE_GETDATA,
                        Constant.METHOD_WSID_VENDINGPROLINK, vendingId);
            } else if (wsid.equals(Constant.METHOD_WSID_PRODUCT)) {
                InterfaceData productConfig = configMap.get(Constant.METHOD_WSID_PRODUCT + "_"
                        + Constant.HTTP_OPERATE_TYPE_GETDATA);
                int productRowCount = productConfig != null ? productConfig.getM03RowCount() : 10;
                ProductDataParse parse = new ProductDataParse();
                parse.requestProductData(Constant.HTTP_OPERATE_TYPE_GETDATA, Constant.METHOD_WSID_PRODUCT,
                        vendingId, productRowCount);
            } else if (wsid.equals(Constant.METHOD_WSID_PRODUCTPICTURE)) {
                InterfaceData productPictureConfig = configMap.get(Constant.METHOD_WSID_PRODUCTPICTURE + "_"
                        + Constant.HTTP_OPERATE_TYPE_GETDATA);
                int productPictureRowCount = productPictureConfig != null ? productPictureConfig
                        .getM03RowCount() : 10;
                ProductPictureDataParse parse = new ProductPictureDataParse();
                parse.requestProductPictureData(Constant.HTTP_OPERATE_TYPE_GETDATA,
                        Constant.METHOD_WSID_PRODUCTPICTURE, vendingId, productPictureRowCount);
            } else if (wsid.equals(Constant.METHOD_WSID_SUPPLIER)) {
                InterfaceData supplierConfig = configMap.get(Constant.METHOD_WSID_SUPPLIER + "_"
                        + Constant.HTTP_OPERATE_TYPE_GETDATA);
                int supplierRowCount = supplierConfig != null ? supplierConfig.getM03RowCount() : 10;

                SupplierDataParse parse = new SupplierDataParse();
                parse.requestSupplierData(Constant.HTTP_OPERATE_TYPE_GETDATA, Constant.METHOD_WSID_SUPPLIER,
                        vendingId, supplierRowCount);
            } else if (wsid.equals(Constant.METHOD_WSID_STATION)) {
                InterfaceData stationConfig = configMap.get(Constant.METHOD_WSID_STATION + "_"
                        + Constant.HTTP_OPERATE_TYPE_GETDATA);
                int stationRowCount = stationConfig != null ? stationConfig.getM03RowCount() : 10;
                StationDataParse parse = new StationDataParse();
                parse.requestStationData(Constant.HTTP_OPERATE_TYPE_GETDATA, Constant.METHOD_WSID_STATION,
                        vendingId, stationRowCount);
            } else if (wsid.equals(Constant.METHOD_WSID_VENDINGCARDPOWER)) {
                VendingCardPowerDataParse parse = new VendingCardPowerDataParse();
                parse.requestVendingCardPowerData(Constant.HTTP_OPERATE_TYPE_GETDATA,
                        Constant.METHOD_WSID_VENDINGCARDPOWER, vendingId);
            } else if (wsid.equals(Constant.METHOD_WSID_PRODUCTMATERIAKPOWER)) {
                ProductMaterialPowerDataParse parse = new ProductMaterialPowerDataParse();
                parse.requestProductMaterialPowerData(Constant.HTTP_OPERATE_TYPE_GETDATA,
                        Constant.METHOD_WSID_PRODUCTMATERIAKPOWER, vendingId);
            } else if (wsid.equals(Constant.METHOD_WSID_CARD)) {
                CardDataParse parse = new CardDataParse();
                parse.requestCardData(Constant.HTTP_OPERATE_TYPE_GETDATA, Constant.METHOD_WSID_CARD,
                        vendingId);
            } else if (wsid.equals(Constant.METHOD_WSID_CUSEMPCARDPOWER)) {
                CusEmpCardPowerDataParse parse = new CusEmpCardPowerDataParse();
                parse.requestCusEmpCardPowerData(Constant.HTTP_OPERATE_TYPE_GETDATA,
                        Constant.METHOD_WSID_CUSEMPCARDPOWER, vendingId);
            } else if (wsid.equals(Constant.METHOD_WSID_CUSTOMEREMPLINK)) {
                CustomerEmpLinkDataParse parse = new CustomerEmpLinkDataParse();
                parse.requestCustomerEmpLinkData(Constant.HTTP_OPERATE_TYPE_GETDATA,
                        Constant.METHOD_WSID_CUSTOMEREMPLINK, vendingId);
            } else if (wsid.equals(Constant.METHOD_WSID_CUSTOMER)) {
                CustomerDataParse parse = new CustomerDataParse();
                parse.requestCustomerData(Constant.HTTP_OPERATE_TYPE_GETDATA, Constant.METHOD_WSID_CUSTOMER,
                        vendingId);
            } else if (wsid.equals(Constant.METHOD_WSID_PRODUCTGROUP)) {
                ProductGroupDataParse parse = new ProductGroupDataParse();
                parse.requestProductGroupData(Constant.HTTP_OPERATE_TYPE_GETDATA,
                        Constant.METHOD_WSID_PRODUCTGROUP, vendingId);
            } else if (wsid.equals(Constant.METHOD_WSID_PRODUCTGROUPPOWER)) {
                ProductGroupPowerDataParse parse = new ProductGroupPowerDataParse();
                parse.requestProductGroupPowerData(Constant.HTTP_OPERATE_TYPE_GETDATA,
                        Constant.METHOD_WSID_PRODUCTGROUPPOWER, vendingId);
            } else if (wsid.equals(Constant.METHOD_WSID_REPLENISHMENT)) {
                InterfaceData replenishmentConfig = configMap.get(Constant.METHOD_WSID_REPLENISHMENT + "_"
                        + Constant.HTTP_OPERATE_TYPE_GETDATA);
                int replenishmentRowCount = replenishmentConfig != null ? replenishmentConfig
                        .getM03RowCount() : 10;

                ReplenishmentDataParse parse = new ReplenishmentDataParse();
                parse.requestReplenishmentData(Constant.HTTP_OPERATE_TYPE_GETDATA,
                        Constant.METHOD_WSID_REPLENISHMENT, vendingId, replenishmentRowCount);
            } else if (wsid.equals(Constant.METHOD_WSID_PASSWORD)) {
                VendingPasswordDataParse parse = new VendingPasswordDataParse();
                parse.requestVendingPasswordData(Constant.HTTP_OPERATE_TYPE_GETDATA,
                        Constant.METHOD_WSID_PASSWORD, vendingId);
            } else if (wsid.equals(Constant.METHOD_WSID_CONFIG)) {
                ConfigDataParse parse = new ConfigDataParse();
                parse.requestConfigData(Constant.HTTP_OPERATE_TYPE_GETDATA, Constant.METHOD_WSID_CONFIG,
                        vendingId);
            } else if (wsid.equals(Constant.METHOD_WSID_RETURNS_FORWARD)) {

                RetreatHeadDataParse parse = new RetreatHeadDataParse();
                parse.requestReturnForward(Constant.HTTP_OPERATE_TYPE_GETDATA,
                        Constant.METHOD_WSID_RETURNS_FORWARD, vendingId, 10);
            } else if (wsid.equals(Constant.METHOD_WSID_PRODUCTCARDPOWER)) {
                ProductCardPowerDataParse parse = new ProductCardPowerDataParse();
                parse.requestProductCardPowerData(Constant.HTTP_OPERATE_TYPE_GETDATA,
                        Constant.METHOD_WSID_PRODUCTCARDPOWER, vendingId);
            } else if (wsid.equals(Constant.METHOD_WSID_USEDRECORD)) {
                UsedRecordDownloadDataParse parse = new UsedRecordDownloadDataParse();
                parse.requestUsedRecordData(Constant.HTTP_OPERATE_TYPE_GETDATA,
                        Constant.METHOD_WSID_USEDRECORD, vendingId);
            }else if(wsid.equals(Constant.METHOD_WSID_CONVERSION)){
            }
        }
    }

    /**
     * 网络请求,下载数据
     */
    public void requestVendingData(String optType, String requestURL, String vendingCode, boolean init) {
        JSONObject json = new JSONObject();
        try {
            json.put("VD1_CODE", vendingCode);
            json.put("Init", init ? "1" : "0");
            DataParseHelper helper = new DataParseHelper(this);
            helper.requestSubmitServer(optType, json, requestURL);
        } catch (Exception e) {
            e.printStackTrace();
            L.e("======>>>>>售货机网络请求数据异常!" + e.getMessage());
        }
    }

    private List<String> parseWsid(JSONArray jsonArray) {
        List<String> wsidList = new ArrayList<String>();
        if (jsonArray == null) {
            return null;
        }
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObj;
                jsonObj = jsonArray.getJSONObject(i);
                wsidList.add(jsonObj.getString("WSID"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            ZillionLog.e("======>>>>>售货机WSID解析数据异常!" + e.getMessage());
        }

        return wsidList;
    }

    /**
     * 数据解析
     * 
     * @param jsonArray
     * @return
     */
    public VendingData parse(JSONArray jsonArray) {
        VendingData data = null;
        if (jsonArray == null) {
            return null;
        }
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

                data = new VendingData();
                data.setVd1Id(jsonObj.getString("ID"));
                data.setVd1M02Id(jsonObj.getString("VD1_M02_ID"));
                data.setVd1Code(jsonObj.getString("VD1_CODE"));
                data.setVd1Manufacturer(jsonObj.getString("VD1_manufacturer"));
                data.setVd1Vm1Id(jsonObj.getString("VD1_VM1_ID"));
                data.setVd1LastVersion(jsonObj.getString("VD1_LastVersion"));
                data.setVd1LwhSize(jsonObj.getString("VD1_LWHSize"));
                data.setVd1Color(jsonObj.getString("VD1_Color"));
                data.setVd1InstallAddress(jsonObj.getString("VD1_InstallAddress"));
                data.setVd1Coordinate(jsonObj.getString("VD1_Coordinate"));
                data.setVd1St1Id(jsonObj.getString("VD1_ST1_ID"));
                data.setVd1EmergencyRel(jsonObj.getString("VD1_EmergencyRel"));
                data.setVd1EmergencyRelPhone(jsonObj.getString("VD1_EmergencyRelPhone"));
                data.setVd1OnlineStatus(jsonObj.getString("VD1_OnlineStatus"));
                data.setVd1Status(jsonObj.getString("VD1_Status"));
                try {
                    data.setVd1CardType(jsonObj.getString("VD1_CardType"));
                } catch (Exception e) {
                    data.setVd1CardType("1");
                }
                data.setLogVersion(jsonObj.getString("LogVision"));
                data.setVd1CreateUser(createUser);
                data.setVd1CreateTime(createTime);
                data.setVd1ModifyUser(modifyUser);
                data.setVd1ModifyTime(modifyTime);
                data.setVd1RowVersion(rowVersion);
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            L.e("======>>>>>售货机解析数据异常!" + e.getMessage());
        }
        return data;
    }

    @Override
    public void parseRequestError(BaseData baseData) {
        if (listener != null) {
            listener.parseRequestFailure(baseData);
        }

    }
}
