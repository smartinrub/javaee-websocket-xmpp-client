package com.sergiomartinrubio.javaeewebsocketxmppclient;

import com.sergiomartinrubio.javaeewebsocketxmppclient.domain.TextMessage;
import com.sergiomartinrubio.javaeewebsocketxmppclient.exception.WebSocketException;
import lombok.extern.slf4j.Slf4j;
import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;

import javax.enterprise.context.ApplicationScoped;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

import static com.sergiomartinrubio.javaeewebsocketxmppclient.domain.MessageType.JOIN;

@Slf4j
@ApplicationScoped
@ServerEndpoint(value = "/chat/{username}", decoders = MessageDecoder.class, encoders = MessageEncoder.class)
public class WebSocketServer {

    private XMPPClient xmppClient = new XMPPClient();
    private AbstractXMPPConnection connection;
    private ChatManager chatManager;

    @OnOpen
    public void open(Session session, @PathParam("username") String username) {
        connection = xmppClient.connect(username + "@" + XMPPClient.XMPP_HOST);
        try {
            connection.connect().login();
        } catch (XMPPException | SmackException | IOException | InterruptedException e) {
            e.printStackTrace();
        }
        TextMessage textMessage = TextMessage.builder()
                .to(username)
                .messageType(JOIN)
                .build();
        try {
            session.getBasicRemote().sendObject(textMessage);
            chatManager = ChatManager.getInstanceFor(connection);
            chatManager.addIncomingListener(new XMPPIncomingChatMessageListener(session));
        } catch (IOException | EncodeException e) {
            e.printStackTrace();
        }
        log.info("Connected!");
    }

    @OnMessage
    public void handleMessage(TextMessage message, Session session) {
        EntityBareJid entityBareJid;
        try {
            entityBareJid = JidCreate.entityBareFrom(message.getTo());
            Chat chat = chatManager.chatWith(entityBareJid);
            chat.send(message.getContent());
        } catch (XmppStringprepException | SmackException.NotConnectedException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @OnClose
    public void close(Session session) {
        connection.disconnect();
        log.info("Connection closed!");
    }

    @OnError
    public void onError(Throwable error) {
        throw new WebSocketException(error);
    }

}
