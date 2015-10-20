package com.mc.vending.parse.listener;

import com.mc.vending.data.BaseData;

public interface DataParseListener {

    public void parseJson(BaseData baseData);

    public void parseRequestError(BaseData baseData);

}
