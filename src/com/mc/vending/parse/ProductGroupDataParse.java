package com.mc.vending.parse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.mc.vending.data.BaseData;
import com.mc.vending.data.CardData;
import com.mc.vending.data.ProductGroupDetailData;
import com.mc.vending.data.ProductGroupHeadData;
import com.mc.vending.db.CardDbOper;
import com.mc.vending.db.ProductGroupHeadDbOper;
import com.mc.vending.parse.listener.DataParseListener;
import com.mc.vending.parse.listener.DataParseRequestListener;
import com.mc.vending.tools.ConvertHelper;
import com.mc.vending.tools.ZillionLog;

public class ProductGroupDataParse implements DataParseListener {
	private static ProductGroupDataParse instance = null;
	private DataParseRequestListener listener;

	public DataParseRequestListener getListener() {
		return listener;
	}

	public void setListener(DataParseRequestListener listener) {
		this.listener = listener;
	}

	public ProductGroupDataParse() {
	}

	public static ProductGroupDataParse getInstance() {
		if (instance == null) {
			instance = new ProductGroupDataParse();
		}
		return instance;
	}

	/**
	 * 网络请求,下载数据
	 */
	public void requestProductGroupData(String optType, String requestURL, String vendingId) {
		JSONObject json = new JSONObject();
		try {
			json.put("VD1_ID", vendingId);
			DataParseHelper helper = new DataParseHelper(this);
			helper.requestSubmitServer(optType, json, requestURL);
		} catch (Exception e) {
			e.printStackTrace();
			ZillionLog.e(this.getClass().toString(), "==========>>>>>产品组合网络请求异常!");
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
		if (baseData == null || baseData.getData() == null || baseData.getData().length() == 0) {
			if (listener != null) {
				listener.parseRequestFailure(baseData);
			}
			return;
		}
		// 全表
		List<ProductGroupHeadData> list = parse(baseData.getData());
		// 0全表时不需要删除原数据-false。1表示需要删除true
		if (list.isEmpty() && !baseData.getDeleteFlag()) {
			if (this.listener != null) {
				this.listener.parseRequestFinised(baseData);
			}
			return;
		}
		List<ProductGroupHeadData> addList = new ArrayList<ProductGroupHeadData>();
		List<ProductGroupHeadData> updateList = new ArrayList<ProductGroupHeadData>();
		List<ProductGroupHeadData> deleteList = new ArrayList<ProductGroupHeadData>();
		for (ProductGroupHeadData productGroupHeadData : list) {
			if (productGroupHeadData.getCRUD().equals("D")) {
				deleteList.add(productGroupHeadData);
			} else if (productGroupHeadData.getCRUD().equals("C")) {
				addList.add(productGroupHeadData);
			} else if (productGroupHeadData.getCRUD().equals("U")) {
				updateList.add(productGroupHeadData);
			}
		}
		ProductGroupHeadDbOper productGroupHeadDbOper = new ProductGroupHeadDbOper();
		if (!addList.isEmpty()) {
			boolean addFlag = productGroupHeadDbOper.batchAddProductGroupHead(addList);
			if (addFlag) {
				Log.i("[productGroup]:", "产品组合批量增加成功!" + "======" + list.size());
				DataParseHelper parseHelper = new DataParseHelper(this);
				parseHelper.sendLogVersion(list.get(0).getLogVersion());
			} else {
				ZillionLog.e("[productGroup]:", "产品组合批量增加失败!");
			}
		}
		if (!deleteList.isEmpty()) {
			boolean deleteFlag = productGroupHeadDbOper.batchDeleteProductGroupHead(deleteList);
			if (deleteFlag) {
				Log.i("[productGroup]:", "产品组合批量删除成功!" + "======" + list.size());
				DataParseHelper parseHelper = new DataParseHelper(this);
				parseHelper.sendLogVersion(list.get(0).getLogVersion());
			} else {
				ZillionLog.e("[productGroup]:", "产品组合批量删除失败!");
			}
		}
		if (!updateList.isEmpty()) {
			// 1.主表进行更新操作
			boolean deleteFlag = productGroupHeadDbOper.batchDeleteProductGroupHeadOnly(updateList);
			boolean addFlag = productGroupHeadDbOper.batchAddProductGroupHeadOnly(updateList);
			if (deleteFlag && addFlag) {
				Log.i("[productGroup]:", "产品组合批量修改成功!" + "======" + list.size());
				 DataParseHelper parseHelper = new DataParseHelper(this);
				 parseHelper.sendLogVersion(list.get(0).getLogVersion());
			} else {
				ZillionLog.e("[productGroup]:", "产品组合批量修改失败!");
			}
			// 2.对从表进行操作
			for (ProductGroupHeadData productGroupHeadData : updateList) {
				List<ProductGroupDetailData> productGroupDetailDataList = productGroupHeadData.getChildren();
				List<ProductGroupDetailData> addDetailList = new ArrayList<ProductGroupDetailData>();
				List<ProductGroupDetailData> updateDetailList = new ArrayList<ProductGroupDetailData>();
				List<ProductGroupDetailData> deleteDetailList = new ArrayList<ProductGroupDetailData>();
				for (ProductGroupDetailData productGroupDetailData : productGroupDetailDataList) {
					if (productGroupDetailData.getCRUD().equals("D")) {
						deleteDetailList.add(productGroupDetailData);
					} else if (productGroupDetailData.getCRUD().equals("C")) {
						addDetailList.add(productGroupDetailData);
					} else if (productGroupDetailData.getCRUD().equals("U")) {
						updateDetailList.add(productGroupDetailData);
					}
				}
				if (!addDetailList.isEmpty()) {
					boolean addDetailFlag = productGroupHeadDbOper.batchAddProductGroupDetailOnly(addDetailList);
					if (addDetailFlag) {
						Log.i("[productGroupDetail]:", "产品组合从表批量增加成功!" + "======" + list.size());
						DataParseHelper parseHelper = new DataParseHelper(this);
						parseHelper.sendLogVersion(list.get(0).getLogVersion());
					} else {
						ZillionLog.e("[productGroupDetail]:", "产品组合从表批量增加失败!");
					}
				}
				if (!deleteDetailList.isEmpty()) {
					boolean deleteDetailFlag = productGroupHeadDbOper.batchDeleteProductGroupDetailOnly(deleteDetailList);
					if (deleteDetailFlag) {
						Log.i("[productGroupDetail]:", "产品组合从表批量删除成功!" + "======" + list.size());
						DataParseHelper parseHelper = new DataParseHelper(this);
						parseHelper.sendLogVersion(list.get(0).getLogVersion());
					} else {
						ZillionLog.e("[productGroupDetail]:", "产品组合从表批量删除失败!");
					}
				}
				if (!updateDetailList.isEmpty()) {
					boolean deleteDetailFlag = productGroupHeadDbOper.batchDeleteProductGroupDetailOnly(updateDetailList);
					boolean addDetailFlag = productGroupHeadDbOper.batchAddProductGroupDetailOnly(updateDetailList);
					if (deleteDetailFlag && addDetailFlag) {
						Log.i("[productGroupDetail]:", "产品组合从表批量修改成功!" + "======" + list.size());
						DataParseHelper parseHelper = new DataParseHelper(this);
						parseHelper.sendLogVersion(list.get(0).getLogVersion());
					} else {
						ZillionLog.e("[productGroupDetail]:", "产品组合从表批量修改失败!");
					}
				}
				
			}
		}
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
	public List<ProductGroupHeadData> parse(JSONArray jsonArray) {
		if (jsonArray == null) {
			return new ArrayList<ProductGroupHeadData>(0);
		}
		List<ProductGroupHeadData> list = new ArrayList<ProductGroupHeadData>();
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

				ProductGroupHeadData data = new ProductGroupHeadData();
				data.setPg1Id(jsonObj.getString("ID"));
				data.setPg1M02Id(jsonObj.getString("PG1_M02_ID"));
				data.setCRUD(jsonObj.getString("CRUD"));
				data.setPg1Cu1Id(jsonObj.getString("PG1_CU1_ID"));
				data.setPg1Code(jsonObj.getString("PG1_CODE"));
				data.setPg1Name(jsonObj.getString("PG1_Name"));
				data.setLogVersion(jsonObj.getString("LogVision"));
				data.setPg1CreateUser(createUser);
				data.setPg1CreateTime(createTime);
				data.setPg1ModifyUser(modifyUser);
				data.setPg1ModifyTime(modifyTime);
				data.setPg1RowVersion(rowVersion);

				List<ProductGroupDetailData> children = new ArrayList<ProductGroupDetailData>();
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

							ProductGroupDetailData detail = new ProductGroupDetailData();
							detail.setPg2Id(detailJsonObj.getString("ID"));
							detail.setPg2M02Id(detailJsonObj.getString("PG2_M02_ID"));
							detail.setPg2Pg1Id(detailJsonObj.getString("PG2_PG1_ID"));
							detail.setPg2Pd1Id(detailJsonObj.getString("PG2_PD1_ID"));
							detail.setPg2GroupQty(ConvertHelper.toInt(detailJsonObj.getString("PG2_GroupQty"), 0));
							detail.setPg2CreateUser(detil_createUser);
							detail.setPg2CreateTime(detil_createTime);
							detail.setPg2ModifyUser(detil_modifyUser);
							detail.setPg2ModifyTime(detil_modifyTime);
							detail.setPg2RowVersion(rowVersion);

							children.add(detail);
						}
						data.setChildren(children);
					}
				}
				list.add(data);
			}
		} catch (Exception e) {
			e.printStackTrace();
			ZillionLog.e(this.getClass().toString(), "==========>>>>>产品组合数据解析异常!");
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
