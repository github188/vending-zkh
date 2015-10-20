package com.mc.vending.parse.listener;

import com.mc.vending.data.BaseData;

public interface DataParseRequestListener {

    public void parseRequestFinised(BaseData baseData);

    public void parseRequestFailure(BaseData baseData);
}
