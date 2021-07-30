package com.askia.coremodel.datamodel.readCard.event;

import java.io.Serializable;

public class ReadCardEvent implements Serializable
{
    private String cardmsg;

    public String getCardmsg() {
        return cardmsg;
    }

    public void setCardmsg(String cardmsg) {
        this.cardmsg = cardmsg;
    }
}
