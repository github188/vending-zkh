package com.mc.vending.parse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.mc.vending.data.BaseData;
import com.mc.vending.data.StationData;
import com.mc.vending.db.StationDbOper;
import com.mc.vending.parse.listener.DataParseListener;
import com.mc.vending.parse.listener.DataParseRequestListener;

public class StationDataParse implements DataParseListener {
    private static StationDataParse  instance = null;
    private DataParseRequestListener listener;

    public DataParseRequestListener getListener() {
        return listener;
    }

    public void setListener(DataParseRequestListener listener) {
        this.listener = listener;
    }

    public StationDataParse() {
    }

    public static StationDataParse getInstance() {
        if (instance == null) {
            instance = new StationDataParse();
        }
        return instance;
    }

    /**
     * 网络请求,下载数据
     */
    public void requestStationData(String optType, String requestURL, String vendingId, int rowCount) {
        JSONObject json = new JSONObject();
        try {
            json.put("VD1_ID", vendingId);
            json.put("RowCount", rowCount);
            DataParseHelper helper = new DataParseHelper(this);
            helper.requestSubmitServer(optType, json, requestURL);
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(this.getClass().toString(), "======>>>>>站点网络请求数据异常!");
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
        // 增量
        List<StationData> list = parse(baseData.getData());
        if (list.isEmpty()) {
            if (this.listener != null) {
                this.listener.parseRequestFinised(baseData);
            }
            return;
        }
        StationDbOper stationDbOper = new StationDbOper();

        Map<String, String> map = stationDbOper.findAllMap();
        List<StationData> addList = new ArrayList<StationData>();
        List<StationData> updateList = new ArrayList<StationData>();
        for (StationData station : list) {
            if (map.get(station.getSt1Id()) == null) {
                // 数据库中不存在
                addList.add(station);
            } else {
                updateList.add(station);
            }
        }
        // 测试时加上这句话
        // stationDbOper.deleteAll();
        if (!addList.isEmpty()) {
            // 批量增加站点
            boolean flag = stationDbOper.batchAddStation(addList);
            if (flag) {
                Log.i("[station]: ", "======>>>>>站点批量更新成功!" + addList.size());
                DataParseHelper parseHelper = new DataParseHelper(this);
                parseHelper.sendLogVersion(list.get(0).getLogVersion());
            } else {
                Log.i("[station]:", "==========>>>>>站点批量增加失败!");
            }
        } else if (!updateList.isEmpty()) {
            boolean flag_ = stationDbOper.batchUpdateStation(updateList);
            if (flag_) {
                Log.i("[station]:", "==========>>>>>站点批量更新成功!" + "==========" + updateList.size());
                DataParseHelper parseHelper = new DataParseHelper(this);
                parseHelper.sendLogVersion(list.get(0).getLogVersion());
            } else {
                Log.i("[station]:", "==========>>>>>站点批量更新失败!");
            }
        } else {
            DataParseHelper parseHelper = new DataParseHelper(this);
            parseHelper.sendLogVersion(list.get(0).getLogVersion());
        }

        // System.out.println(stationDbOper.findAll());
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
    public List<StationData> parse(JSONArray jsonArray) {
        if (jsonArray == null) {
            return new ArrayList<StationData>(0);
        }
        List<StationData> list = new ArrayList<StationData>();
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

                StationData data = new StationData();
                data.setSt1Id(jsonObj.getString("ID"));
                data.setSt1M02Id(jsonObj.getString("ST1_M02_ID"));
                data.setSt1Code(jsonObj.getString("ST1_CODE"));
                data.setSt1Name(jsonObj.getString("ST1_Name"));
                data.setSt1Ce1Id(jsonObj.getString("ST1_CE1_ID"));
                data.setSt1Wh1Id(jsonObj.getString("ST1_WH1_ID"));
                data.setSt1Coordinate(jsonObj.getString("ST1_Coordinate"));
                data.setSt1Address(jsonObj.getString("ST1_Address"));
                data.setSt1Status(jsonObj.getString("ST1_Status"));
                data.setLogVersion(jsonObj.getString("LogVision"));
                data.setSt1CreateUser(createUser);
                data.setSt1CreateTime(createTime);
                data.setSt1ModifyUser(modifyUser);
                data.setSt1ModifyTime(modifyTime);
                data.setSt1RowVersion(rowVersion);
                list.add(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(this.getClass().toString(), "======>>>>>站点解析数据异常!");
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
