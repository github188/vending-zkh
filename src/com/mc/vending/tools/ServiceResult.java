package com.mc.vending.tools;

import java.io.Serializable;

/**
 * 服务接口返回对象。
 * <p>
 * 客户端调用服务方法，得到<code>ServiceResult</code>对象后：<br />
 * 1. 判断{@link ServiceResult#isSuccess()}，为true表示服务方法执行成功，为false表示服务方法执行失败；<br />
 * 2. 服务执行成功时，通过{@link ServiceResult#getResult()}取执行结果；
 * 3. 服务执行失败时，通过{@link ServiceResult#getMessage()}取错误描述消息，详细错误信息从服务日志中查询；
 * </p>
 *                       
 * @Filename: ServiceResult.java
 * @Version: 1.0
 * @Author: fenghu
 * @Email: fenghu@mightcloud.com
 *
 */
public class ServiceResult<T> implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 3765720967319047788L;

    private T                 result;
    private boolean           isSuccess        = true;
    private String            message;
    private String            code;

    /**
     * 服务执行失败时，获取错误描述消息。
     * @return
     */
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 服务是否执行成功。
     * @return
     */
    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 获取服务执行结果。
     * @return
     */
    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "ServiceResult [result=" + result + ", message=" + message + ", isSuccess="
               + isSuccess + ", code=" + code + "]";
    }

}
