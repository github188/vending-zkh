package com.mc.vending.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import android.R.integer;

import com.mc.vending.data.ReplenishmentDetailData;
import com.mc.vending.data.ReplenishmentDetailWrapperData;
import com.mc.vending.data.ReplenishmentHeadData;
import com.mc.vending.data.StockTransactionData;
import com.mc.vending.data.VendingCardPowerWrapperData;
import com.mc.vending.data.VendingChnData;
import com.mc.vending.data.VendingChnProductWrapperData;
import com.mc.vending.data.VendingChnStockData;
import com.mc.vending.db.ProductDbOper;
import com.mc.vending.db.ReplenishmentDetailDbOper;
import com.mc.vending.db.ReplenishmentHeadDbOper;
import com.mc.vending.db.StockTransactionDbOper;
import com.mc.vending.db.VendingChnDbOper;
import com.mc.vending.db.VendingChnStockDbOper;
import com.mc.vending.tools.BusinessException;
import com.mc.vending.tools.ConvertHelper;
import com.mc.vending.tools.DateHelper;
import com.mc.vending.tools.ServiceResult;
import com.mc.vending.tools.Tools;
import com.mc.vending.tools.ZillionLog;
import com.mc.vending.tools.utils.SerialTools;

public class ReplenishmentService extends BasicService {

    private static ReplenishmentService instance;

    public static ReplenishmentService getInstance() {

        if (instance == null) {
            instance = new ReplenishmentService();
        }
        return instance;
    }

    // =========================================================一键补货=========================================================
    /**
     * 一键补货操作
     * 
     * @return
     */
    public ServiceResult<Boolean> oneKeyReplenishment(VendingCardPowerWrapperData vendingCardPowerWrapper) {

        ServiceResult<Boolean> result = new ServiceResult<Boolean>();
        try {
            ReplenishmentHeadData replenishmentHead = new ReplenishmentHeadDbOper()
                    .getReplenishmentHeadByOrderStatus(ReplenishmentHeadData.ORDERSTATUS_CREATED);
            if (replenishmentHead == null) {
                ZillionLog.e(this.getClass().getName(), "oneKeyReplenishment err 不存在未完成的补货单,请与计划员联系!");
                throw new BusinessException("不存在未完成的补货单,请与计划员联系!");
            }

            String rh1Id = replenishmentHead.getRh1Id();
            String vendingId = replenishmentHead.getRh1Vd1Id();
            String rh1Rhcode = replenishmentHead.getRh1Rhcode();
            List<ReplenishmentDetailData> list = new ReplenishmentDetailDbOper()
                    .findReplenishmentDetailkByRh1Id(rh1Id);
            List<StockTransactionData> stockTransactionList = new ArrayList<StockTransactionData>();

            List<VendingChnStockData> updateChnStockList = new ArrayList<VendingChnStockData>();
            List<VendingChnStockData> addChnStockList = new ArrayList<VendingChnStockData>();
            VendingChnStockDbOper vendingChnStockDbOper = new VendingChnStockDbOper();
            Map<String, Integer> vendingChnStockMap = vendingChnStockDbOper.getStockMap();

            for (ReplenishmentDetailData replenishmentDetail : list) {
                String vendingChnCode = replenishmentDetail.getRh2Vc1Code();
                String skuId = replenishmentDetail.getRh2Pd1Id();
                String saleType = replenishmentDetail.getRh2SaleType();
                int qty = replenishmentDetail.getRh2ActualQty();

                // 通过“售货机货道.货主ID“查询”货主“表
                String supplierId = replenishmentDetail.getRh2Sp1Id();

                StockTransactionData stockTransaction = this.buildStockTransaction(qty,
                        StockTransactionData.BILL_TYPE_PLAN, rh1Rhcode, vendingId, vendingChnCode, skuId,
                        saleType, supplierId, vendingCardPowerWrapper);

                stockTransactionList.add(stockTransaction);

                VendingChnData vendingChn = new VendingChnDbOper().getVendingChnByCode(vendingChnCode);
                if (vendingChn.getVc1Type().equals(VendingChnData.VENDINGCHN_TYPE_CELL)) {
                    ZillionLog.i("格子机补货，打开格子机==========================", vendingChnCode);

                    SerialTools.getInstance().openStore(ConvertHelper.toInt(vendingChn.getVc1LineNum(), 0),
                            ConvertHelper.toInt(vendingChn.getVc1ColumnNum(), 0),
                            ConvertHelper.toInt(vendingChn.getVc1Height(), 0));

                }

                VendingChnStockData vendingChnStockData = this.buildVendingChnStock(vendingId,
                        vendingChnCode, skuId, qty);
                if (vendingChnStockMap.get(vendingChnCode) != null) {
                    updateChnStockList.add(vendingChnStockData);
                } else {
                    addChnStockList.add(vendingChnStockData);
                }
            }

            boolean flag = new StockTransactionDbOper().batchAddStockTransaction(stockTransactionList);

            if (flag) {
                if (!addChnStockList.isEmpty()) {
                    // 批量增加库存记录
                    boolean flag_add = vendingChnStockDbOper.batchAddVendingChnStock(addChnStockList);
                }
                if (!updateChnStockList.isEmpty()) {
                    // 批量更新售货机货道库存.库存数量=售货机货道库存.库存数量+补货单从表.实际补货数
                    boolean flag_update = vendingChnStockDbOper
                            .batchUpdateVendingChnStock(updateChnStockList);
                }
            }

            // 自动更新补货单主表.订单状态=“已完成“
            new ReplenishmentHeadDbOper().updateOrderStatusByRh1Id(rh1Id,
                    ReplenishmentHeadData.ORDERSTATUS_FINISHED);
            result.setSuccess(true);
            result.setResult(true);
            result.setMessage(rh1Rhcode);
        } catch (BusinessException be) {
            ZillionLog.e(this.getClass().toString(), "======>>>>一键补货发生异常", be);
            result.setMessage(be.getMessage());
            result.setCode("1");
            result.setSuccess(false);
        } catch (Exception e) {
            ZillionLog.e(this.getClass().toString(), "======>>>>一键补货发生异常", e);
            e.printStackTrace();
            result.setSuccess(false);
            result.setCode("0");
            result.setMessage("售货机系统故障>>一键补货发生异常!");
        }
        return result;
    }

    // =========================================================一键补满=========================================================
    /**
     * 一键补满操作
     * 1. 更新货道库存
     * 2. 插入交易记录
     * 3. 新建补货单
     * 
     * @return
     */
    public ServiceResult<Boolean> oneKeyReplenishAll(VendingCardPowerWrapperData vendingCardPowerWrapper) {
        
        ServiceResult<Boolean> result = new ServiceResult<Boolean>();
        try {
            ReplenishmentHeadData rHead = new ReplenishmentHeadDbOper().getReplenishmentHeadByOrderStatus(ReplenishmentHeadData.ORDERSTATUS_CREATED);
            if (rHead != null) {
                throw new BusinessException("存在未完成的补货单,请先进行一键补货!");
            }
            String vendingId = vendingCardPowerWrapper.getVendingCardPowerData().getVc2Vd1Id();
            
            List<VendingChnData> vendingChns = new VendingChnDbOper().findAll();

            List<StockTransactionData> sList = new ArrayList<StockTransactionData>();
            List<VendingChnStockData> updateChnStockList = new ArrayList<VendingChnStockData>();
            List<VendingChnStockData> addChnStockList = new ArrayList<VendingChnStockData>();
            List<ReplenishmentDetailData> detailDatas = new ArrayList<ReplenishmentDetailData>();

            Date currentDate = DateHelper.currentDateTime();
            String rhCode = Tools.getVendCode() + DateHelper.format(currentDate, "yyMMddHHmmss");
   
            //构造补货单
            rHead = this.buildReplenishmentHead(rhCode,vendingId);
            
            Map<String, VendingChnStockData> chnStockDataMap = new VendingChnStockDbOper().getStockDataMap();
            
            for (VendingChnData chnData : vendingChns) {
                //货道类型=售货机、销售类型<>借还、货道状态=正常
                if (chnData.getVc1Type().equals(VendingChnData.VENDINGCHN_TYPE_VENDING) && 
                        chnData.getVc1Status().equals(VendingChnData.VENDINGCHN_STATUS_NORMAL) && 
                        !chnData.getVc1SaleType().equals(VendingChnData.VENDINGCHN_SALETYPE_BORROW)) {
                    int qty = 0;
                    //构造货道库存
                    if (chnStockDataMap.containsKey(chnData.getVc1Code())) { //存在此货道库存
                        VendingChnStockData chnStockData = chnStockDataMap.get(chnData.getVc1Code());
                        qty = chnData.getVc1Capacity() - chnStockData.getVs1Quantity();
                        if (qty > 0 && qty <= chnData.getVc1Capacity()) {
                            chnStockData.setVs1Quantity(qty);
                            updateChnStockList.add(chnStockData);
                        }
                    } else { // 不存在此货道库存， 则构造
                        qty = chnData.getVc1Capacity();
                        if (qty > 0 && qty <= chnData.getVc1Capacity()) {
                            VendingChnStockData chnStockData = this.buildVendingChnStock(vendingId,
                                    chnData.getVc1Code(), chnData.getVc1Pd1Id(), qty);
                            addChnStockList.add(chnStockData);
                        }
                    }
                    if (qty > 0 && qty <= chnData.getVc1Capacity()) {
                        //构造交易记录
                        StockTransactionData stockTransaction = this.buildStockTransaction(qty,
                                StockTransactionData.BILL_TYPE_All, rhCode, vendingId, chnData.getVc1Code(),
                                chnData.getVc1Pd1Id(), chnData.getVc1SaleType(), chnData.getVc1Sp1Id(),
                                vendingCardPowerWrapper);
                        sList.add(stockTransaction);

                        //构造补货单详细
                        ReplenishmentDetailData detailData = this.buildReplenishmentDetail(
                                rHead.getRh1Id(), chnData.getVc1Code(),
                                chnData.getVc1Pd1Id(), chnData.getVc1SaleType(), qty, 0);

                        detailDatas.add(detailData);
                    }
                }
            }
            
            if (detailDatas == null || detailDatas.size() == 0) {
                throw new BusinessException("没有要补货的货道!");
            }
//            ZillionLog.i("rHead",rHead);
//            ZillionLog.i("sList",sList);
//            ZillionLog.i("addChnStockList",addChnStockList);
//            ZillionLog.i("updateChnStockList",updateChnStockList);
//            ZillionLog.i("detailDatas",detailDatas);
            
            boolean flag = new StockTransactionDbOper().batchAddStockTransaction(sList);
            
            if (flag) {
                if (!addChnStockList.isEmpty()) {
                    //删除本货道
                    new VendingChnStockDbOper().batchDeleteVendingChnStock(addChnStockList);
                    // 批量增加库存记录
                    flag = new VendingChnStockDbOper().batchAddVendingChnStock(addChnStockList);
                }
                if (flag) {
                    if (!updateChnStockList.isEmpty()) {
                        // 批量更新售货机货道库存.库存数量=售货机货道库存.库存数量+补货单从表.实际补货数
                        flag = new VendingChnStockDbOper().batchUpdateVendingChnStock(updateChnStockList);
                    }
                    if (flag) {
                        //添加补货单
                        if (detailDatas != null && detailDatas.size() > 0) {
                            rHead.setChildren(detailDatas);
                            List<ReplenishmentHeadData> rHeadList = new ArrayList<ReplenishmentHeadData>();
                            rHeadList.add(rHead);
                            flag = new ReplenishmentHeadDbOper().batchAddReplenishmentHead(rHeadList);
                        }
                    }
                }
            }
            
            result.setSuccess(true);
            result.setResult(true);
            result.setMessage(rhCode);
        } catch (BusinessException be) {
            ZillionLog.e(this.getClass().toString(), "======>>>>一键补满发生异常1",be);
            result.setMessage(be.getMessage());
            result.setCode("1");
            result.setSuccess(false);
        } catch (Exception e) {
            ZillionLog.e(this.getClass().toString(), "======>>>>一键补满发生异常2",e);
            e.printStackTrace();
            result.setSuccess(false);
            result.setCode("0");
            result.setMessage("售货机系统故障>>一键补货发生异常!");
        }
        return result;
    }

    // =========================================================补货差异=========================================================
    /**
     * 查询“补货单主表”记录，条件：订单状态=已完成，并且全部”补货单从表.补货差异=0”，按“补货单号“倒排序
     * @param rhType 
     * 
     * @return
     */
    public ServiceResult<List<ReplenishmentHeadData>> getReplenishmentHead(String rhType) {

        ServiceResult<List<ReplenishmentHeadData>> result = new ServiceResult<List<ReplenishmentHeadData>>();
        try {
            List<ReplenishmentHeadData> replenishmentHeadList = new ReplenishmentHeadDbOper()
                    .findReplenishmentHeadByOrderStatus(ReplenishmentHeadData.ORDERSTATUS_FINISHED,rhType);
            result.setSuccess(true);
            result.setResult(replenishmentHeadList);
        } catch (BusinessException be) {
            ZillionLog.e(this.getClass().toString(), "======>>>>查询补化主表记录发生异常", be);
            result.setMessage(be.getMessage());
            result.setCode("1");
            result.setSuccess(false);
        } catch (Exception e) {
            ZillionLog.e(this.getClass().toString(), "======>>>>查询补化主表记录发生异常", e);
            e.printStackTrace();
            result.setSuccess(false);
            result.setCode("0");
            result.setMessage("售货机系统故障>>查询补化主表记录发生异常!");
        }
        return result;
    }

    /**
     * 查询已经完成的补货单明细
     * 
     * @param rh1Id
     *            补货单主表id
     * @return
     */
    public List<ReplenishmentDetailWrapperData> findReplenishmentDetail(String rh1Id) {
        List<ReplenishmentDetailWrapperData> returnList = new ArrayList<ReplenishmentDetailWrapperData>();
        List<ReplenishmentDetailData> list = new ReplenishmentDetailDbOper()
                .findReplenishmentDetailkByRh1Id(rh1Id);

        Map<String, String> map = new ProductDbOper().findAllProduct();
        for (ReplenishmentDetailData replenishmentDetail : list) {
            String skuId = replenishmentDetail.getRh2Pd1Id();
            String productName = map.get(skuId);
            ReplenishmentDetailWrapperData wrapperData = new ReplenishmentDetailWrapperData();
            wrapperData.setReplenishmentDetail(replenishmentDetail);
            wrapperData.setProductName(productName);
            returnList.add(wrapperData);
        }
        return returnList;
    }

    /**
     * 
     * 批量更新保存补货差异
     * @param replenishmentHead
     * @param list
     * @param vendingCardPowerWrapper
     * @param billType
     * @return
     */
    public ServiceResult<Boolean> updateReplenishmentDetail(ReplenishmentHeadData replenishmentHead,
            List<ReplenishmentDetailWrapperData> list, VendingCardPowerWrapperData vendingCardPowerWrapper,String billType) {

        ServiceResult<Boolean> result = new ServiceResult<Boolean>();
        try {
            String vendingId = replenishmentHead.getRh1Vd1Id();
            String rh1Rhcode = replenishmentHead.getRh1Rhcode();
            List<StockTransactionData> stockTransactionList = new ArrayList<StockTransactionData>();
            List<ReplenishmentDetailData> newList = new ArrayList<ReplenishmentDetailData>();
            List<VendingChnStockData> updateChnStockList = new ArrayList<VendingChnStockData>();
            List<VendingChnStockData> addChnStockList = new ArrayList<VendingChnStockData>();
            VendingChnStockDbOper vendingChnStockDbOper = new VendingChnStockDbOper();
            Map<String, Integer> vendingChnStockMap = vendingChnStockDbOper.getStockMap();
            for (ReplenishmentDetailWrapperData replenishmentDetailWrapper : list) {
                ReplenishmentDetailData replenishmentDetail = replenishmentDetailWrapper
                        .getReplenishmentDetail();
                int differentiaQty = replenishmentDetail.getRh2DifferentiaQty();
                if (differentiaQty != 0) {
                    String vendingChnCode = replenishmentDetail.getRh2Vc1Code();
                    String skuId = replenishmentDetail.getRh2Pd1Id();
                    String saleType = replenishmentDetail.getRh2SaleType();
                    String supplierId = replenishmentDetail.getRh2Sp1Id();

                    StockTransactionData stockTransaction = this.buildStockTransaction(differentiaQty,
                            billType, rh1Rhcode, vendingId, vendingChnCode, skuId,
                            saleType, supplierId, vendingCardPowerWrapper);
                    stockTransactionList.add(stockTransaction);

                    VendingChnStockData vendingChnStockData = this.buildVendingChnStock(vendingId,
                            vendingChnCode, skuId, differentiaQty);
                    if (vendingChnStockMap.get(vendingChnCode) != null) {
                        updateChnStockList.add(vendingChnStockData);
                    } else {
                        addChnStockList.add(vendingChnStockData);
                    }
                    newList.add(replenishmentDetail);

                }
            }

            if (newList.isEmpty()) {
                throw new BusinessException("暂无补货差异记录，请重新填写补货差异数量!");
            }
            // 批量更新补货单从表.补货差异 = 补货差异数
            boolean flag = new ReplenishmentDetailDbOper().batchUpdateDifferentiaQty(newList);
            if (flag) {
                boolean flag_stock = new StockTransactionDbOper()
                        .batchAddStockTransaction(stockTransactionList);

                if (flag_stock) {
                    if (!addChnStockList.isEmpty()) {
                        // 批量增加库存记录
                        boolean flag_add = vendingChnStockDbOper.batchAddVendingChnStock(addChnStockList);
                    }
                    if (!updateChnStockList.isEmpty()) {
                        // 批量更新售货机货道库存.库存数量=售货机货道库存.库存数量+补货差异数
                        boolean flag_update = vendingChnStockDbOper
                                .batchUpdateVendingChnStock(updateChnStockList);
                    }
                }
            }
            result.setSuccess(true);
            result.setResult(true);
        } catch (BusinessException be) {
            ZillionLog.e(this.getClass().toString(), "======>>>>批量更新保存补货差异发生异常", be);
            result.setMessage(be.getMessage());
            result.setCode("1");
            result.setSuccess(false);
        } catch (Exception e) {
            ZillionLog.e(this.getClass().toString(), "======>>>>批量更新保存补货差异发生异常", e);
            result.setSuccess(false);
            result.setCode("0");
            result.setMessage("售货机系统故障>>批量更新保存补货差异发生异常!");
        }
        return result;
    }

    // =========================================================紧急补货=========================================================

    /**
     * 1.新增库存交易记录，2.更新售货机货道库存的库存数量
     * 
     * @param billCode
     * @param list
     * @param vendingCardPower
     */
    public ServiceResult<Boolean> emergencyReplenishment(String billCode,
            List<VendingChnProductWrapperData> list, VendingCardPowerWrapperData vendingCardPowerWrapper) {
        ServiceResult<Boolean> result = new ServiceResult<Boolean>();
        try {
            List<StockTransactionData> stockTransactionList = new ArrayList<StockTransactionData>();
            List<VendingChnStockData> updateChnStockList = new ArrayList<VendingChnStockData>();
            List<VendingChnStockData> addChnStockList = new ArrayList<VendingChnStockData>();
            VendingChnStockDbOper vendingChnStockDbOper = new VendingChnStockDbOper();
            Map<String, Integer> vendingChnStockMap = vendingChnStockDbOper.getStockMap();
            for (VendingChnProductWrapperData vendingChnProductWrapper : list) {
                VendingChnData vendingChn = vendingChnProductWrapper.getVendingChn();
                int actQty = vendingChnProductWrapper.getActQty();
                
                // 计算下来理论上能补货的最大值
                int calcMaxQty = calcActReplenishmentCount(vendingChn);
                if (actQty > calcMaxQty) {
                    actQty = calcMaxQty;// 输入的补货数量不能大于当前理论最大值
                }
                
                if (actQty != 0) {

                    if (vendingChn.getVc1Type().equals(VendingChnData.VENDINGCHN_TYPE_CELL)) {
                        ZillionLog.i(this.getClass().getName(), vendingChn.getVc1Code() + "格子机补货，打开格子机");

                        SerialTools.getInstance().openStore(
                                ConvertHelper.toInt(vendingChn.getVc1LineNum(), 0),
                                ConvertHelper.toInt(vendingChn.getVc1ColumnNum(), 0),
                                ConvertHelper.toInt(vendingChn.getVc1Height(), 0));

                    }

                    StockTransactionData stockTransaction = this.buildStockTransaction(actQty,
                            StockTransactionData.BILL_TYPE_EMERGENCY, billCode, vendingChn,
                            vendingCardPowerWrapper);
                    stockTransactionList.add(stockTransaction);

                    String vendingId = vendingChn.getVc1Vd1Id();
                    String vendingChnCode = vendingChn.getVc1Code();
                    String skuId = vendingChn.getVc1Pd1Id();
                    VendingChnStockData vendingChnStockData = this.buildVendingChnStock(vendingId,
                            vendingChnCode, skuId, actQty);
                    if (vendingChnStockMap.get(vendingChnCode) != null) {
                        updateChnStockList.add(vendingChnStockData);
                    } else {
                        addChnStockList.add(vendingChnStockData);
                    }
                }
            }

            if (stockTransactionList.isEmpty()) {
                throw new BusinessException("暂无紧急补货记录，请重新填写补货数量!");
            }

            // 批量增加库存交易记录
            boolean flag = new StockTransactionDbOper().batchAddStockTransaction(stockTransactionList);

            if (flag) {
                if (!addChnStockList.isEmpty()) {
                    // 批量增加库存记录
                    boolean flag_add = vendingChnStockDbOper.batchAddVendingChnStock(addChnStockList);
                }
                if (!updateChnStockList.isEmpty()) {
                    // 批量更新售货机货道库存.库存数量=售货机货道库存.库存数量+实际补货数
                    boolean flag_update = vendingChnStockDbOper
                            .batchUpdateVendingChnStock(updateChnStockList);
                }
            }

            result.setSuccess(true);
            result.setResult(true);
        } catch (BusinessException be) {
            ZillionLog.e(this.getClass().toString(), "======>>>>紧急补货发生异常", be);
            result.setMessage(be.getMessage());
            result.setCode("1");
            result.setSuccess(false);
        } catch (Exception e) {
            ZillionLog.e(this.getClass().toString(), "======>>>>紧急补货发生异常", e);
            result.setSuccess(false);
            result.setCode("0");
            result.setMessage("售货机系统故障>>紧急补货发生异常!");
        }
        return result;
    }

    /**
     * 计算补货时，每个货道实际能补货数的最大值
     * 
     * @param vendingChn 货道信息对象
     * @return
     */
    private int calcActReplenishmentCount(VendingChnData vendingChn) {
        int intRtn = 0;
        String vendingId = vendingChn.getVc1Vd1Id();
        String vendingChnCode = vendingChn.getVc1Code();
        // 1.获取本货道最大容量
        int vendingChnStockCapacity = vendingChn.getVc1Capacity();
        // 2.获取本货道库存容量
        int stockCount = GeneralMaterialService.getInstance().getVendingChnStock(vendingId, vendingChnCode);
        // 3.计算实际能补货的数量: 货道最大容量 - 货道库存容量
        if (vendingChnStockCapacity < 0) {
            vendingChnStockCapacity = 0;
        }
        if (stockCount < 0) {
            stockCount = 0;
        }
        intRtn = vendingChnStockCapacity - stockCount;
        return intRtn;
    }
    
}
