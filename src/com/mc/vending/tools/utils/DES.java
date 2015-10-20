package com.mc.vending.tools.utils;

import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

import com.mc.vending.config.Constant;
import com.mc.vending.tools.DateHelper;

public class DES {

    /**
     * 加密封装
     * @return
     * @throws Exception
     */
    public static String getEncrypt() {
        //        return "M+/9h5/h0VmWj4ArmPA/eFdw7WuODCTm";
        try {
            return Encrypt(DateHelper.format(new Date(), "yyyy-MM-dd HH:mm:ss.SSS"),
                Constant.DES_VI, Constant.BODY_VALUE_PWD);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 加密
     * @param message 加密内容
     * @param vi 加密私钥
     * @param key 加密Key,通过请求接口获得
     * @return
     * @throws Exception
     */
    public static String Encrypt(String message, String vi, String key) throws Exception {
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
        IvParameterSpec iv = new IvParameterSpec(vi.getBytes("UTF-8"));
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);

        byte[] encryptedData = cipher.doFinal(message.getBytes("UTF-8"));
        return Base64.encode(encryptedData);
    }

    /**
     * 解密
     * @param message 解密内容
     * @param vi 加密私钥
     * @param key 加密Key,通过请求接口获得
     * @return
     * @throws Exception
     */
    public static String Decrypt(String message, String vi, String key) throws Exception {
        byte[] byteSrc = new Base64().decode(message);
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
        IvParameterSpec iv = new IvParameterSpec(vi.getBytes("UTF-8"));

        cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);

        byte[] retByte = cipher.doFinal(byteSrc);
        return new String(retByte);
    }
}