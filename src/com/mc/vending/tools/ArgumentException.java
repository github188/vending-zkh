package com.mc.vending.tools;

/**
 * 参数错误，方法调用的入参不符合要求
 *                       
 * @Filename: ArgumentException.java
 * @Version: 1.0
 * @Author: fenghu
 * @Email: fenghu@mightcloud.com
 *
 */
public class ArgumentException extends BusinessException {
    /**
     *Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = -9190228908394807686L;

    /**
     *Comment for <code>serialVersionUID</code>
     */

    public ArgumentException(String message) {
        super(message);
    }
}