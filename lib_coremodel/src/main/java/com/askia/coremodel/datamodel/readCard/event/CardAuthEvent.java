package com.askia.coremodel.datamodel.readCard.event;

import java.io.Serializable;

/**
 *  首页向其他页面分发的读卡事件
 */
public class CardAuthEvent implements Serializable
{
    private String cardmsg;

    public String getCardmsg() {
        return cardmsg;
    }

    public void setCardmsg(String cardmsg) {
        this.cardmsg = cardmsg;
    }
}
