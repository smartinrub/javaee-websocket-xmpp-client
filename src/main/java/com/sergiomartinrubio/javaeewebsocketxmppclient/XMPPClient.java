package com.sergiomartinrubio.javaeewebsocketxmppclient;

import lombok.AllArgsConstructor;
import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.impl.JidCreate;

import java.io.IOException;

@AllArgsConstructor
public class XMPPClient {

    private XMPPConnection connection;

    public void connect() throws IOException, InterruptedException, XMPPException, SmackException {
        XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
                .setHost(connection.getHost())
                .setPort(connection.getPort())
                .setXmppDomain(connection.getDomain())
                .setUsernameAndPassword(connection.getUsername(), connection.getPassword())
                .setResource(connection.getResource())
                .build();

        AbstractXMPPConnection connection = new XMPPTCPConnection(config);
        connection.connect().login();

        ChatManager chatManager = ChatManager.getInstanceFor(connection);

        chatManager.addIncomingListener((from, message, chat) ->
                System.out.println("New message from " + from + ": " + message.getBody()));

        EntityBareJid jid = JidCreate.entityBareFrom("user1@localhost");
        Chat chat = chatManager.chatWith(jid);
        chat.send("Howdy!");
    }
}
