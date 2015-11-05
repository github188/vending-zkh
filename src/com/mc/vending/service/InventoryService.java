package com.mc.vending.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.mc.vending.data.InventoryHistoryData;
import com.mc.vending.data.StockTransactionData;
import com.mc.vending.data.VendingCardPowerData;
import com.mc.vending.data.VendingCardPowerWrapperData;
import com.mc.vending.data.VendingChnData;
import com.mc.vending.data.VendingChnProductWrapperData;
import com.mc.vending.data.VendingChnStockData;
import com.mc.vending.db.InventoryHistoryDbOper;
import com.mc.vending.db.StockTransactionDbOper;
import com.mc.vending.db.VendingChnStockDbOper;
import com.mc.vending.tools.BusinessException;
import com.mc.vending.tools.DateHelper;
import com.mc.vending.tools.ServiceResult;
import com.mc.vending.tools.ZillionLog;

public class InventoryService extends BasicService {

    private static InventoryService instance;

    public static InventoryService getInstance() {

        if (instance == null) {
            instance = new InventoryService();
        }
        return instance;
    }

    /**
     * 现场盘点
     * @param inventoryCode
     * @param list
     * @param vendingCardPowerWrapper
     */
    public ServiceResult<Boolean> saveInventoryHistory(String inventoryCode,
                                                       List<VendingChnProductWrapperData> list,
                                                       VendingCardPowerWrapperData vendingCardPowerWrapper) {
        ServiceResult<Boolean> result = new ServiceResult<Boolean>();
        try {
            List<InventoryHistoryData> inventoryHistoryList = new ArrayList<InventoryHistoryData>();
            List<StockTransactionData> stockTransactionList = new ArrayList<StockTransactionData>();
            List<VendingChnStockData> updateChnStockList = new ArrayList<VendingChnStockData>();
            List<VendingChnStockData> addChnStockList = new ArrayList<VendingChnStockData>();
            VendingChnStockDbOper vendingChnStockDbOper = new VendingChnStockDbOper();
            Map<String, Integer> stockMap = vendingChnStockDbOper.getStockMap();
            for (VendingChnProductWrapperData vendingChnProductWrapper : list) {
                VendingChnData vendingChn = vendingChnProductWrapper.getVendingChn();
                int actQty = vendingChnProductWrapper.getActQty();

                InventoryHistoryData inventoryHistory = this.buildInventoryHistory(actQty,
                    stockMap, inventoryCode, vendingChn, vendingCardPowerWrapper);
                inventoryHistoryList.add(inventoryHistory);
                String vendingChnCode = vendingChn.getVc1Code();
                String vendingId = vendingChn.getVc1Vd1Id();
                String skuId = vendingChn.getVc1Pd1Id();
                int stockQty = stockMap.get(vendingChnCode) == null ? 0 : stockMap
                    .get(vendingChnCode);
                int difQty = actQty - stockQty; // 盘点差异=实盘数量 - 库存数量

                if (difQty != 0) {
                    StockTransactionData stockTransaction = this.buildStockTransaction(difQty,
                        StockTransactionData.BILL_TYPE_PANDIAN, inventoryCode, vendingChn,
                        vendingCardPowerWrapper);
                    stockTransactionList.add(stockTransaction);

                    VendingChnStockData vendingChnStockData = this.buildVendingChnStock(vendingId,
                        vendingChnCode, skuId, difQty);
                    if (stockMap.get(vendingChnCode) != null) {
                        updateChnStockList.add(vendingChnStockData);
                    } else {
                        addChnStockList.add(vendingChnStockData);
                    }

                }
            }
            // 新增“盘点记录表”
            boolean flag = new InventoryHistoryDbOper()
                .batchAddInventoryHistory(inventoryHistoryList);

            if (flag) {
                boolean flag_ = new StockTransactionDbOper()
                    .batchAddStockTransaction(stockTransactionList);

                if (flag_) {
                    if (!addChnStockList.isEmpty()) {
                        //批量增加库存记录
                        boolean flag_add = vendingChnStockDbOper
                            .batchAddVendingChnStock(addChnStockList);
                    }
                    if (!updateChnStockList.isEmpty()) {
                        //批量更新售货机货道库存.库存数量=售货机货道库存.库存数量+“盘点差异”
                        boolean flag_update = vendingChnStockDbOper
                            .batchUpdateVendingChnStock(updateChnStockList);
                    }
                }
            }
            result.setSuccess(true);
            result.setResult(true);
        } catch (BusinessException be) {
            ZillionLog.e(this.getClass().toString(), "======>>>>现场盘点发生异常",be);
            result.setMessage(be.getMessage());
            result.setCode("1");
            result.setSuccess(false);
        } catch (Exception e) {
            ZillionLog.e(this.getClass().toString(), "======>>>>现场盘点发生异常",e);
            result.setSuccess(false);
            result.setCode("0");
            result.setMessage("售货机系统故障>>现场盘点发生异常!");
        }
        return result;
    }

    /**
     * 构建盘点记录对象 
     * @param actQty 实际盘点数量
     * @param stockMap 货道库存Map对象
     * @param inventoryCode 盘点单号
     * @param vendingChn 售货机货道对象
     * @param vendingCardPowerWrapper 卡权限封装对象
     * @return
     */
    public InventoryHistoryData buildInventoryHistory(int actQty,
                                                      Map<String, Integer> stockMap,
                                                      String inventoryCode,
                                                      VendingChnData vendingChn,
                                                      VendingCardPowerWrapperData vendingCardPowerWrapper) {
        Date currentDate = DateHelper.currentDateTime();
        String dateTime = DateHelper.format(currentDate, "yyyy-MM-dd HH:mm:ss");
        VendingCardPowerData vendingCardPower = vendingCardPowerWrapper.getVendingCardPowerData();
        String cusId = vendingCardPower.getVc2Cu1Id();
        String cusEmpId = vendingCardPowerWrapper.getCusEmpId();

        String vendingId = vendingChn.getVc1Vd1Id();
        String vendingChnCode = vendingChn.getVc1Code();
        String skuId = vendingChn.getVc1Pd1Id();
        int stockQty = stockMap.get(vendingChnCode) == null ? 0 : stockMap.get(vendingChnCode);
        int difQty = actQty - stockQty; // 盘点差异=实盘数量 - 库存数量
        InventoryHistoryData inventoryHistory = new InventoryHistoryData();
        inventoryHistory.setIh3Id(UUID.randomUUID().toString());
        inventoryHistory.setIh3M02Id("");
        inventoryHistory.setIh3IHcode(inventoryCode);
        inventoryHistory.setIh3ActualDate(dateTime);
        inventoryHistory.setIh3Cu1Id(cusId);
        inventoryHistory.setIh3InventoryPeople(cusEmpId);
        inventoryHistory.setIh3Vd1Id(vendingId);
        inventoryHistory.setIh3Vc1Code(vendingChnCode);
        inventoryHistory.setIh3Pd1Id(skuId);
        inventoryHistory.setIh3Quantity(stockQty);
        inventoryHistory.setIh3InventoryQty(actQty);
        inventoryHistory.setIh3DifferentiaQty(difQty);
        inventoryHistory.setIh3CreateUser(cusEmpId);
        inventoryHistory.setIh3UploadStatus(InventoryHistoryData.UPLOAD_UNLOAD);
        inventoryHistory.setIh3CreateTime(dateTime);
        inventoryHistory.setIh3ModifyUser(cusEmpId);
        inventoryHistory.setIh3ModifyTime(dateTime);
        inventoryHistory.setIh3RowVersion(currentDate.getTime() + "");

        return inventoryHistory;
    }
}
