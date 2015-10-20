package com.mc.vending.tools.utils;

public interface MC_SerialToolsListener {

    public void serialReturn(String value, int serialType);

    public void serialReturn(String value, int serialType, Object userInfo);
}
