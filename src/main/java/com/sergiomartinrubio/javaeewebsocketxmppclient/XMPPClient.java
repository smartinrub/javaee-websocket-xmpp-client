package com.sergiomartinrubio.javaeewebsocketxmppclient;

import lombok.Getter;
import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;

import java.io.IOException;

public class XMPPClient {

    public static final String XMPP_HOST = "localhost";
    private static final int XMPP_PORT = 5222;

    public AbstractXMPPConnection connect(String jid) {
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
//                    .setSendPresence(true)
                    .build();
            return new XMPPTCPConnection(config);
        } catch (XmppStringprepException e) {
            throw new XMPPConnectionException(e);
        }
    }

    public void chatWith(String jid) throws IOException, SmackException, InterruptedException, XMPPException {
//        AbstractXMPPConnection connection = connect();
//        connection.connect().login();
//        ChatManager chatManager = ChatManager.getInstanceFor(connection);
//
//        chatManager.addIncomingListener((from, message, chat) ->
//                System.out.println("New message from " + from + ": " + message.getBody()));
//
//        EntityBareJid entityBareJid = JidCreate.entityBareFrom(jid);
//        Chat chat = chatManager.chatWith(entityBareJid);
//        chat.send("Howdy!");
    }
}
