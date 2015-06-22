package com.domin.net.common;

import java.io.Serializable;

public class ForwardedMessage implements Serializable {

    private static final long serialVersionUID = -1087666860625064634L;
    public final Object message;
    public final int senderID;

    public ForwardedMessage(int senderID, Object message) {
        this.senderID = senderID;
        this.message = message;
    }

}
