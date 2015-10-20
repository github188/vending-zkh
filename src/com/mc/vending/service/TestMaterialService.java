package com.mc.vending.service;

import android.util.Log;

import com.mc.vending.data.VendingChnData;
import com.mc.vending.db.VendingChnDbOper;
import com.mc.vending.tools.BusinessException;
import com.mc.vending.tools.ServiceResult;

public class TestMaterialService extends BasicService {
    private static TestMaterialService instance;

    public static TestMaterialService getInstance() {

        if (instance == null) {
            instance = new TestMaterialService();
        }
        return instance;
    }

    /**
     * 领料测试
     * @param vendingChnCode
     * @return
     */
    public ServiceResult<VendingChnData> testMaterial(String vendingChnCode) {

        ServiceResult<VendingChnData> result = new ServiceResult<VendingChnData>();
        try {
            VendingChnData vendingChn = new VendingChnDbOper().getVendingChnByCode(vendingChnCode);
            if (vendingChn == null) {
                throw new BusinessException("货道号  " + vendingChnCode + " 不存在,请重新输入!");
            }
            result.setSuccess(true);
            result.setResult(vendingChn);
        } catch (BusinessException be) {
            result.setMessage(be.getMessage());
            result.setCode("1");
            result.setSuccess(false);
        } catch (Exception e) {
            Log.i(this.getClass().toString(), "======>>>>领料测试 发生异常");
            result.setSuccess(false);
            result.setCode("0");
            result.setMessage("售货机系统故障>>领料测试 发生异常!");
        }
        return result;
    }
}
