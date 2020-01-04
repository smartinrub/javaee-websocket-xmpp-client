package com.sergiomartinrubio.javaeewebsocketxmppclient;

import org.jxmpp.stringprep.XmppStringprepException;

public class XMPPConnectionException extends RuntimeException {
    public XMPPConnectionException(Exception e) {
        super(e);
    }
}
