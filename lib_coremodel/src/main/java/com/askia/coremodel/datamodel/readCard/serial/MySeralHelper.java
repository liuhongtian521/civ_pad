package com.askia.coremodel.datamodel.readCard.serial;

import com.apkfuns.logutils.LogUtils;
import com.askia.coremodel.datamodel.readCard.event.ReadCardEvent;
import com.askia.coremodel.util.OtherUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.ttsea.jrxbus2.RxBus2;

public class MySeralHelper extends SerialHelper
{
    @Override
    protected void onDataReceived(final ComBean ComRecData)
    {
        String rfid = OtherUtils.bytesToHex(ComRecData.bRec);
        LogUtils.d("进入数据监听事件中。。。" + rfid);
        ReadCardEvent event = new ReadCardEvent();
        event.setCardmsg(rfid);
        RxBus2.getInstance().post(event);
    }
}
