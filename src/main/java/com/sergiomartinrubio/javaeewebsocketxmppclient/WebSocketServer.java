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
import java.util.HashMap;
import java.util.Map;

import static com.sergiomartinrubio.javaeewebsocketxmppclient.domain.MessageType.*;

@Slf4j
@ApplicationScoped
@ServerEndpoint(value = "/chat/{jid}", decoders = MessageDecoder.class, encoders = MessageEncoder.class)
public class WebSocketServer {

    private Map<String, Session> connections = new HashMap<>();
    private XMPPClient xmppClient = new XMPPClient();
    private AbstractXMPPConnection connection;

    @OnOpen
    public void open(Session session, @PathParam("jid") String jid) throws IOException, InterruptedException,
            XMPPException, SmackException, EncodeException {
        connections.put(jid, session);
        connection = xmppClient.connect("user1@localhost");
        connection.connect().login();
        log.info("Connected!");
        TextMessage textMessage = TextMessage.builder()
                .from("")
                .to(jid)
                .messageType(JOIN)
                .build();
        session.getBasicRemote().sendObject(textMessage);
    }

    @OnMessage
    public void handleMessage(TextMessage message, Session session) throws XmppStringprepException,
            SmackException.NotConnectedException, InterruptedException {
        log.info(message.toString());
        String toJid = message.getTo();
        EntityBareJid entityBareJid = JidCreate.entityBareFrom(toJid);

        ChatManager chatManager = ChatManager.getInstanceFor(connection);

        chatManager.addIncomingListener((from, messageFrom, chat) -> {
            log.info("New message from " + from + ": " + messageFrom.getBody());
            try {
                TextMessage textMessage = TextMessage.builder()
                        .from(from.getLocalpart().toString())
                        // TODO: handle null
                        .to(messageFrom.getTo().getLocalpartOrNull().toString())
                        .messageType(CHAT)
                        .build();
                session.getBasicRemote().sendObject(textMessage);
            } catch (IOException | EncodeException e) {
                e.printStackTrace();
            }
        });

        Chat chat = chatManager.chatWith(entityBareJid);
        chat.send(message.getContent());
    }

    @OnClose
    public void close(Session session) {
        log.info("Connection closed!");
        connection.disconnect();
//        sessionHandler.removeSession(session);
    }

    @OnError
    public void onError(Throwable error) {
        throw new WebSocketException(error);
    }

}
