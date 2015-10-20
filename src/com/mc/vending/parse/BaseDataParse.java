/**
 * 
 */
package com.mc.vending.parse;

import com.mc.vending.data.BaseData;
import com.mc.vending.data.BaseParseData;
import com.mc.vending.parse.listener.DataParseListener;

/**
 * @author Forever
 * @date 2015年7月29日
 * @email forever.wang@zillionstar.com
 */
public class BaseDataParse implements DataParseListener {

    @Override
    public void parseJson(BaseData baseData) {
        // TODO Auto-generated method stub

    }

    @Override
    public void parseRequestError(BaseData baseData) {
        // TODO Auto-generated method stub

    }

    public void callBackLogversion(BaseParseData baseParseData) {
        DataParseHelper parseHelper = new DataParseHelper(this);
        parseHelper.sendLogVersion(baseParseData.getLogVersion());

    }

}
