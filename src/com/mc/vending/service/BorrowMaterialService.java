package com.mc.vending.service;

import android.util.Log;

import com.mc.vending.data.CustomerEmpLinkData;
import com.mc.vending.data.StockTransactionData;
import com.mc.vending.data.TransactionWrapperData;
import com.mc.vending.data.VendingCardPowerWrapperData;
import com.mc.vending.data.VendingChnData;
import com.mc.vending.db.CustomerEmpLinkDbOper;
import com.mc.vending.db.StockTransactionDbOper;
import com.mc.vending.tools.BusinessException;
import com.mc.vending.tools.ServiceResult;

public class BorrowMaterialService extends BasicService {

    private static BorrowMaterialService instance;

    public static BorrowMaterialService getInstance() {

        if (instance == null) {
            instance = new BorrowMaterialService();
        }
        return instance;
    }

    /**
     * 检查货道借还状态
     * @param vendingChn 货道对象
     * @param commandType 借还指令 VendingChnData.VENDINGCHN_COMMAND_BORROW为借指令，VendingChnData.VENDINGCHN_COMMAND_RETURN为还指令
     * @return
     */
    public ServiceResult<Boolean> checkBorrowStatus(VendingChnData vendingChn,
                                                    TransactionWrapperData transactionWrapper,
                                                    String commandType) {
        ServiceResult<Boolean> result = new ServiceResult<Boolean>();
        try {
            //检查货道
            this.validateBorrowStatus(vendingChn, transactionWrapper, commandType);
            result.setSuccess(true);
            result.setResult(true);
        } catch (BusinessException be) {
            result.setMessage(be.getMessage());
            result.setCode("1");
            result.setSuccess(false);
        } catch (Exception e) {
            Log.i(this.getClass().toString(), "======>>>>检查货道借还状态发生异常");
            result.setSuccess(false);
            result.setCode("0");
            result.setMessage("售货机系统故障>>检查货道借还状态发生异常!");
        }
        return result;
    }

    /**
     * 检查是否原借出人归还
     * @param borrowStatus 借还状态
     * @param cusEmpId 客户员工ID
     * @param commandType 借还指令 VendingChnData.VENDINGCHN_COMMAND_BORROW为借指令，VendingChnData.VENDINGCHN_COMMAND_RETURN为还指令
     * @return
     */
    public ServiceResult<Boolean> checkBorrowCustomer(String borrowStatus, String cusEmpId,
                                                      String commandType,
                                                      TransactionWrapperData transactionWrapper) {
        ServiceResult<Boolean> result = new ServiceResult<Boolean>();
        try {
            //检查货道
            this.validateBorrowCustomer(borrowStatus, cusEmpId, commandType, transactionWrapper);
            result.setSuccess(true);
            result.setResult(true);
        } catch (BusinessException be) {
            result.setMessage(be.getMessage());
            result.setCode("1");
            result.setSuccess(false);
        } catch (Exception e) {
            Log.i(this.getClass().toString(), "======>>>>检查是否原借出人归还发生异常");
            result.setSuccess(false);
            result.setCode("0");
            result.setMessage("售货机系统故障>>检查是否原借出人归还发生异常!");
        }
        return result;
    }

    private void validateBorrowCustomer(String borrowStatus, String cusEmpId, String commandType,
                                        TransactionWrapperData transactionWrapper) {

        if (transactionWrapper != null) {
            int transQty = transactionWrapper.getTransQty();
            String createUser = transactionWrapper.getCreateUser();
            if (transQty == -1 && VendingChnData.VENDINGCHN_COMMAND_RETURN.equals(commandType)) {
                if (!createUser.equals(cusEmpId)) {
                    throw new BusinessException("当前用户并非上次借出人,\n上次是由" + transactionWrapper.getName()
                                                + " 在 " + transactionWrapper.getCreateTime()
                                                + "时间借出,联系电话:\n" + transactionWrapper.getPhone()
                                                + "!");
                }
            }
        }
    }

    /**
     * 增加交易库存记录并更新售货机货道借还状态
     * @param vendingChn
     * @param vendingCardPower
     * @param commandType 借还指令 VendingChnData.VENDINGCHN_COMMAND_BORROW为借指令，VendingChnData.VENDINGCHN_COMMAND_RETURN为还指令
     */
    public void saveStockTransaction(VendingChnData vendingChn,
                                     VendingCardPowerWrapperData vendingCardPowerWrapper,
                                     String commandType) {
        int qty = 0;

        if (VendingChnData.VENDINGCHN_COMMAND_BORROW.equals(commandType)) {
            qty = -1;
        }
        if (VendingChnData.VENDINGCHN_COMMAND_RETURN.equals(commandType)) {
            qty = 1;
        }

        StockTransactionData stockTransaction = this.buildStockTransaction(qty,
            StockTransactionData.BILL_TYPE_BORROW, "", vendingChn, vendingCardPowerWrapper);
        boolean flag = new StockTransactionDbOper().addStockTransaction(stockTransaction);
    }

    /**
     * 根据“售货机ID、货道号、单据类型=借还“查询“库存交易记录”
     * @param vendingChn
     * @return
     */
    public TransactionWrapperData getStockTransaction(VendingChnData vendingChn) {

        String vendingId = vendingChn.getVc1Vd1Id();
        String vcCode = vendingChn.getVc1Code();
        String createUser = "";
        String createTime = "";
        String name = "";
        String phone = "";
        StockTransactionData stockTransaction = new StockTransactionDbOper().getVendingChnByCode(
            vendingId, vcCode, StockTransactionData.BILL_TYPE_BORROW);
        if (stockTransaction != null) {
            createUser = stockTransaction.getTs1CreateUser();
            createTime = stockTransaction.getTs1CreateTime();
            int transQty = stockTransaction.getTs1TransQty();

            CustomerEmpLinkData customerEmpLink = new CustomerEmpLinkDbOper()
                .getCustomerEmpLinkByCeId(createUser);
            if (customerEmpLink != null) {
                name = customerEmpLink.getCe1Name();
                phone = customerEmpLink.getCe1Phone();
            }
            TransactionWrapperData transactionWrapper = new TransactionWrapperData();
            transactionWrapper.setCreateUser(createUser);
            transactionWrapper.setName(name);
            transactionWrapper.setCreateTime(createTime);
            transactionWrapper.setPhone(phone);
            transactionWrapper.setTransQty(transQty);
            return transactionWrapper;
        } else {
            return null;
        }

    }

    /**
     * 检查货道借还状态
     * @param vendingChn 货道对象
     * @param transactionWrapper 库存交易封装对象
     * @param commandType 借还指令 VendingChnData.VENDINGCHN_COMMAND_BORROW为借指令，VendingChnData.VENDINGCHN_COMMAND_RETURN为还指令
     */
    private void validateBorrowStatus(VendingChnData vendingChn,
                                      TransactionWrapperData transactionWrapper, String commandType) {
        if (transactionWrapper == null) {
            //1.按数字键盘“借”，结束操作，信息提示： 借还货道初始状态，请按“还”键放入库存！
            if (VendingChnData.VENDINGCHN_COMMAND_BORROW.equals(commandType)) {
                throw new BusinessException("借还货道初始状态，请按 '还'键放入库存！");
            }
        } else {

            int transQty = transactionWrapper.getTransQty();
            String vcCode = vendingChn.getVc1Code();
            if (transQty == -1) {
                if (VendingChnData.VENDINGCHN_COMMAND_BORROW.equals(commandType)) {
                    throw new BusinessException("货道号 " + vcCode + " 的产品,\n已由 "
                                                + transactionWrapper.getName() + " 在 "
                                                + transactionWrapper.getCreateTime()
                                                + " 时间借出,联系电话:\n " + transactionWrapper.getPhone()
                                                + " !");
                }
            }

            if (transQty == 1) {
                if (VendingChnData.VENDINGCHN_COMMAND_RETURN.equals(commandType)) {
                    throw new BusinessException("货道号 " + vcCode + " 的产品已归还,请重新输入!");
                }
            }
        }
    }
}
