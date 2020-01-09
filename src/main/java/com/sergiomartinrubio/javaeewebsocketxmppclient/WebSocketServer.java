package com.sergiomartinrubio.javaeewebsocketxmppclient;

import com.sergiomartinrubio.javaeewebsocketxmppclient.domain.TextMessage;
import com.sergiomartinrubio.javaeewebsocketxmppclient.exception.WebSocketException;
import com.sergiomartinrubio.javaeewebsocketxmppclient.exception.XMPPGenericException;
import lombok.SneakyThrows;
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
@ServerEndpoint(value = "/chat/{username}", decoders = MessageDecoder.class, encoders = MessageEncoder.class)
public class WebSocketServer {

    private Map<String, Session> sessions = new HashMap<>();
    private XMPPClient xmppClient = new XMPPClient();
    private AbstractXMPPConnection connection;
    private ChatManager chatManager;

    @SneakyThrows
    @OnOpen
    public void open(Session session, @PathParam("username") String username){
        log.info("Establishing connection with username {} ", username);
        sessions.put(username, session);
        connection = xmppClient.connect(username + "@" + XMPPClient.XMPP_HOST);
        TextMessage textMessage;
        try  {
            connection.connect().login();
        } catch (XMPPException | SmackException | IOException | InterruptedException e) {
            log.error("Error connecting with XMPP server: {}", e.toString());
            connection.disconnect();
            textMessage = TextMessage.builder()
                    .to(username)
                    .messageType(ERROR)
                    .build();
            session.getBasicRemote().sendObject(textMessage);
            throw new XMPPGenericException("Error connecting with XMPP server", e);
        }
        log.info("Connected!");
        try {
            textMessage = TextMessage.builder()
                    .to(username)
                    .messageType(JOIN)
                    .build();
            sessions.get(username).getBasicRemote().sendObject(textMessage);
        } catch (IOException | EncodeException e) {
            connection.disconnect();
            sessions.remove(username);
            throw new WebSocketException(e);
        }
        chatManager = ChatManager.getInstanceFor(connection);
        chatManager.addIncomingListener(new XMPPIncomingChatMessageListener(session));
        log.info("Listening to messages...");
    }

    @OnMessage
    public void handleMessage(TextMessage message, Session session) throws XmppStringprepException,
            SmackException.NotConnectedException, InterruptedException {
        log.info(message.toString());
        EntityBareJid entityBareJid = JidCreate.entityBareFrom(message.getTo());
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
