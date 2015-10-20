package com.mc.vending.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.util.Log;

import com.mc.vending.data.ChnStockWrapperData;
import com.mc.vending.data.ProductGroupHeadData;
import com.mc.vending.data.ProductGroupHeadWrapperData;
import com.mc.vending.data.ProductGroupPowerData;
import com.mc.vending.data.ProductGroupWrapperData;
import com.mc.vending.data.StockTransactionData;
import com.mc.vending.data.VendingCardPowerData;
import com.mc.vending.data.VendingCardPowerWrapperData;
import com.mc.vending.data.VendingChnData;
import com.mc.vending.data.VendingChnStockData;
import com.mc.vending.data.VendingChnWrapperData;
import com.mc.vending.db.ProductGroupDetailDbOper;
import com.mc.vending.db.ProductGroupHeadDbOper;
import com.mc.vending.db.ProductGroupPowerDbOper;
import com.mc.vending.db.StockTransactionDbOper;
import com.mc.vending.db.VendingChnDbOper;
import com.mc.vending.db.VendingChnStockDbOper;
import com.mc.vending.tools.BusinessException;
import com.mc.vending.tools.DateHelper;
import com.mc.vending.tools.ServiceResult;
import com.mc.vending.tools.StringHelper;

public class CompositeMaterialService extends BasicService {

    private static CompositeMaterialService instance;

    public static CompositeMaterialService getInstance() {

        if (instance == null) {
            instance = new CompositeMaterialService();
        }
        return instance;
    }

    /**
     * 查询售货机产品组合列表
     * 
     * @param vendingChnCode
     * @return
     */
    public ServiceResult<List<ProductGroupHeadData>> findAllProductGroupHead() {
        ServiceResult<List<ProductGroupHeadData>> result = new ServiceResult<List<ProductGroupHeadData>>();
        try {
            // 查询售货机所有的产品组合列表
            List<ProductGroupHeadData> list = new ProductGroupHeadDbOper().findAllProductGroupHead();
            result.setSuccess(true);
            result.setResult(list);
        } catch (BusinessException be) {
            result.setMessage(be.getMessage());
            result.setCode("1");
            result.setSuccess(false);
        } catch (Exception e) {
            Log.i(this.getClass().toString(), "======>>>>查询售货机产品组合列表发生异常");
            result.setSuccess(false);
            result.setCode("0");
            result.setMessage("售货机系统故障>>查询售货机产品组合列表发生异常!");
        }
        return result;
    }

    /**
     * 根据输入的组合编号，检查组合编号是否存在，并检查组合skuId库存数量,返回产品组合明细——显示“产品.产品名称”“产品组合从表.组合数量”
     * 
     * @param pgCode
     * @return
     */
    public ServiceResult<ProductGroupHeadWrapperData> checkProductGroupStock(String pgCode) {
        ServiceResult<ProductGroupHeadWrapperData> result = new ServiceResult<ProductGroupHeadWrapperData>();
        try {
            // 根据组合编号查询产品组合列表
            ProductGroupHeadData productGroupHead = new ProductGroupHeadDbOper()
                    .getProductGroupHeadByCode(pgCode);
            if (productGroupHead == null) {
                throw new BusinessException("产品组合编号 " + pgCode + " 不存在,请重新输入!");
            }

            // 产品组合主表ID
            String pg1Id = productGroupHead.getPg1Id();
            // 根据产品组合主表ID查询产品组合从表列表
            // List<ProductGroupDetailData> list = new
            // ProductGroupDetailDbOper()
            // .findProductGroupDetailBySkuId(pg1Id);
            List<ProductGroupWrapperData> wrapperList = new ProductGroupDetailDbOper()
                    .findProductGroupDetail(pg1Id);
            VendingChnDbOper vendingChnDbOper = new VendingChnDbOper();
            for (ProductGroupWrapperData productGroupWrapperData : wrapperList) {
                // skuId
                String skuId = productGroupWrapperData.getSkuId();
                // 产品组合数量
                int groupQty = productGroupWrapperData.getGroupQty();
                // 根据skuId连接查询售货机货道与售货机货道库存表
                List<ChnStockWrapperData> chnStockList = vendingChnDbOper.findVendingChnBySkuId(skuId,
                        VendingChnData.VENDINGCHN_STATUS_NORMAL, VendingChnData.VENDINGCHN_TYPE_VENDING);
                // skuId对应多货道的库存总量
                int stockTotal = 0;
                // 各货道应出料数量
                int qty = 0;
                int tmpQty = groupQty;
                List<VendingChnWrapperData> chnWrapperList = new ArrayList<VendingChnWrapperData>();
                for (ChnStockWrapperData chnStock : chnStockList) {
                    VendingChnWrapperData chnWrapper = new VendingChnWrapperData();
                    // 货道库存量
                    int quantity = chnStock.getQuantity();
                    // 循环统计各货道库存数量
                    stockTotal += quantity;

                    if (tmpQty <= quantity) {
                        // 如果组合数量小于等于货道库存数量，则直接在本货道出料，出料量为qty
                        qty = tmpQty;
                        chnWrapper.setChnStock(chnStock);
                        chnWrapper.setQty(qty);
                        chnWrapperList.add(chnWrapper);
                        break;
                    } else {
                        // 如果组合数量大于货道库存数量，则本货道应出料为货道全部库存量
                        qty = quantity;
                        chnWrapper.setChnStock(chnStock);
                        chnWrapper.setQty(qty);
                        chnWrapperList.add(chnWrapper);
                        tmpQty -= quantity;
                    }

                }

                if (stockTotal < groupQty) {
                    throw new BusinessException(productGroupWrapperData.getProductName()
                            + "库存数量不足,不能领料，请重新输入!");
                }
                productGroupWrapperData.setChnWrapperList(chnWrapperList);
            }

            ProductGroupHeadWrapperData productGroupHeadWrapperData = new ProductGroupHeadWrapperData();
            productGroupHeadWrapperData.setPg1Id(pg1Id);
            productGroupHeadWrapperData.setWrapperList(wrapperList);

            result.setSuccess(true);
            result.setResult(productGroupHeadWrapperData);
        } catch (BusinessException be) {
            result.setMessage(be.getMessage());
            result.setCode("1");
            result.setSuccess(false);
        } catch (Exception e) {
            Log.i(this.getClass().toString(), "======>>>>查询产品组合明细发生异常");
            e.printStackTrace();
            result.setSuccess(false);
            result.setCode("0");
            result.setMessage("售货机系统故障>>查询产品组合明细发生异常!");
        }
        return result;
    }

    /**
     * 检查产品组合权限逻辑
     * 
     * @param vendingCardPowerWrapper
     * @param productGroupHeadWrapperData
     * @param vendingId
     *            售货机ID
     * @return
     */
    public ServiceResult<Boolean> checkProductGroupPower(VendingCardPowerWrapperData vendingCardPowerWrapper,
            ProductGroupHeadWrapperData productGroupHeadWrapperData, String vendingId) {

        ServiceResult<Boolean> result = new ServiceResult<Boolean>();
        try {
            VendingCardPowerData vendingCardPowerData = vendingCardPowerWrapper.getVendingCardPowerData();
            String cusId = vendingCardPowerData.getVc2Cu1Id();
            String cardId = vendingCardPowerData.getVc2Cd1Id();
            String pg1Id = productGroupHeadWrapperData.getPg1Id();
            ProductGroupPowerData productGroupPower = new ProductGroupPowerDbOper().getProductGroupPower(
                    cusId, pg1Id, cardId);
            // 1.判断记录数=0，不允许领料，信息提示：输入的卡号或密码无权限领料，请重新输入！
            if (productGroupPower == null) {
                throw new BusinessException("输入的卡号或密码无权限领料，请重新输入");
            }
            String pp1Power = productGroupPower.getPp1Power();
            if (ProductGroupPowerData.GROUP_POWER_NO.equals(pp1Power)) {
                throw new BusinessException("输入的卡号或密码无权限领料,请重新输入!");
            }
            String period = productGroupPower.getPp1Period();
            String intervalStart = productGroupPower.getPp1IntervalStart();
            String intervalFinish = productGroupPower.getPp1IntervalFinish();
            String startDate = productGroupPower.getPp1StartDate();
            int periodQty = productGroupPower.getPp1PeriodNum();

            List<ProductGroupWrapperData> list = productGroupHeadWrapperData.getWrapperList();
            if (!StringHelper.isEmpty(period, true)) {
                Date date = this.getDate(period, intervalStart, intervalFinish, startDate);
                StockTransactionDbOper stockTransactionDb = new StockTransactionDbOper();
                for (ProductGroupWrapperData productGroupWrapperData : list) {
                    int inputQty = productGroupWrapperData.getGroupQty();
                    String skuId = productGroupWrapperData.getSkuId();
                    String productName = productGroupWrapperData.getProductName();
                    int transQtyTotal = 0;
                    if (date != null) {
                        String dateStr = DateHelper.format(date, "yyyy-MM-dd HH:mm:ss");
                        // 根据售货机ID,单据号="领料",skuId,创建时间 统计skuId领料数量
                        // transQtyTotal = stockTransactionDb.getTransQtyCount(
                        // StockTransactionData.BILL_TYPE_GET, vendingId, skuId,
                        // dateStr);

                        transQtyTotal = stockTransactionDb.getTransQtyCountAddCardId(
                                StockTransactionData.BILL_TYPE_GET, vendingId, skuId, dateStr, cardId);
                    }
                    // (交易数量 * -1)+产品的领料数<=(期间领料数 * 组合数量)
                    int total = transQtyTotal * (-1);
                    if (total + inputQty > (periodQty * inputQty)) {
                        throw new BusinessException("产品: " + productName + "的领料权限是" + periodQty * inputQty
                                + ",累积已领取 " + total + ",不允许超领，请重新输入!");
                    }
                }
            }
            result.setSuccess(true);
            result.setResult(true);
        } catch (BusinessException be) {
            result.setMessage(be.getMessage());
            result.setCode("1");
            result.setSuccess(false);
        } catch (Exception e) {
            Log.i(this.getClass().toString(), "======>>>>检查产品组合权限逻辑发生异常");
            result.setSuccess(false);
            result.setCode("0");
            result.setMessage("售货机系统故障>>检查产品组合权限逻辑发生异常!");
        }
        return result;
    }

    /**
     * 组合领料成功，新增库存交易记录,库存交易记录新增成功，更新售货机货道库存
     * 
     * @param inputQty
     *            输入领料数量
     * @param vendingChn
     *            售货机货道对象
     * @param vendingCardPower
     *            售货机卡/密码权限对象
     */
    public void addStockTransaction1(List<ProductGroupWrapperData> wrapperList,
            VendingCardPowerWrapperData vendingCardPowerWrapper) {

        List<StockTransactionData> list = new ArrayList<StockTransactionData>();

        List<VendingChnStockData> updateChnStockList = new ArrayList<VendingChnStockData>();
        for (ProductGroupWrapperData wrapper : wrapperList) {
            List<VendingChnWrapperData> chnWrapperList = wrapper.getChnWrapperList();
            for (VendingChnWrapperData chnWrapper : chnWrapperList) {
                // 售货机货道出料数量
                int qty = chnWrapper.getQty();
                ChnStockWrapperData chnStock = chnWrapper.getChnStock();
                VendingChnData vendingChn = chnStock.getVendingChn();
                // 构建组装StockTransactionData

                StockTransactionData stockTransaction = this.buildStockTransaction(qty * (-1),
                        StockTransactionData.BILL_TYPE_GET, "", vendingChn, vendingCardPowerWrapper);

                list.add(stockTransaction);

                int getQty = qty * (-1);
                String vendingId = vendingChn.getVc1Vd1Id();
                String vendingChnCode = vendingChn.getVc1Code();
                String skuId = vendingChn.getVc1Pd1Id();
                VendingChnStockData vendingChnStockData = this.buildVendingChnStock(vendingId,
                        vendingChnCode, skuId, getQty);
                updateChnStockList.add(vendingChnStockData);
            }
        }

        boolean flag = new StockTransactionDbOper().batchAddStockTransaction(list);

        if (flag) {
            if (!updateChnStockList.isEmpty()) {
                // 批量更新售货机货道库存.库存数量=售货机货道库存.库存数量+领料数量(注意领料数量为负数)
                boolean flag_update = new VendingChnStockDbOper()
                        .batchUpdateVendingChnStock(updateChnStockList);
            }
        }
    }
}
