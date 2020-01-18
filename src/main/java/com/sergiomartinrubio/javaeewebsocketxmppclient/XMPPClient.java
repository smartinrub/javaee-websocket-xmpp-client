package com.sergiomartinrubio.javaeewebsocketxmppclient;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;

public class XMPPClient {

    public static final String XMPP_HOST = "localhost";
    private static final int XMPP_PORT = 5222;

    public XMPPTCPConnection connect(String jid) {
        EntityBareJid entityBareJid;
        try {
            entityBareJid = JidCreate.entityBareFrom(jid);
            XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
                    .setHost(XMPP_HOST)
                    .setPort(XMPP_PORT)
                    .setXmppDomain(XMPP_HOST)
                    .setUsernameAndPassword(entityBareJid.getLocalpart(), "password")
                    .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                    .setResource(entityBareJid.getResourceOrEmpty())
                    .setSendPresence(true)
                    .build();
            return new XMPPTCPConnection(config);
        } catch (XmppStringprepException e) {
            throw new XMPPConnectionException(e);
        }
    }

}
