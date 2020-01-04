package com.sergiomartinrubio.javaeewebsocketxmppclient;

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
import java.util.Map;

@Slf4j
@ApplicationScoped
@ServerEndpoint(value = "/chat/{jid}", decoders = MessageDecoder.class, encoders = MessageEncoder.class)
public class WebSocketServer {

    private Map<String, String> connections;
    private XMPPClient xmppClient = new XMPPClient();
    private AbstractXMPPConnection connection;

    @OnOpen
    public void open(Session session, @PathParam("jid") String jid) throws IOException, InterruptedException, XMPPException, SmackException {
        // TODO: Get session and Websocket connection
//        connections.put(session.getId(), jid);
//        Message message = new Message();
//        message.setTo(jid);
//        message.setBody("Connected!");
        connection = xmppClient.connect("user1@localhost");
        connection.connect().login();
        log.info("Connected!");
        session.getBasicRemote().sendText("Connected!");
    }

    @OnMessage
    public void handleMessage(TextMessage message, Session session) throws XmppStringprepException, SmackException.NotConnectedException, InterruptedException {
        // TODO: Handle new message
        log.info(message.toString());
        String toJid = message.getTo();
        EntityBareJid entityBareJid = JidCreate.entityBareFrom(toJid);

        ChatManager chatManager = ChatManager.getInstanceFor(connection);

        chatManager.addIncomingListener((from, messageFrom, chat) -> {
            System.out.println("New message from " + from + ": " + messageFrom.getBody());
        });

        Chat chat = chatManager.chatWith(entityBareJid);
        chat.send(message.getContent());
//        try (JsonReader reader = Json.createReader(new StringReader(message))) {
//            JsonObject jsonMessage = reader.readObject();
//
//            if ("add".equals(jsonMessage.getString("action"))) {
//                Device device = new Device();
//                device.setName(jsonMessage.getString("name"));
//                device.setDescription(jsonMessage.getString("description"));
//                device.setType(jsonMessage.getString("type"));
//                device.setStatus("Off");
//                sessionHandler.addDevice(device);
//            }
//
//            if ("remove".equals(jsonMessage.getString("action"))) {
//                int id = (int) jsonMessage.getInt("id");
//                sessionHandler.removeDevice(id);
//            }
//
//            if ("toggle".equals(jsonMessage.getString("action"))) {
//                int id = (int) jsonMessage.getInt("id");
//                sessionHandler.toggleDevice(id);
//            }
//        }
    }

    @OnClose
    public void close(Session session) {
        log.info("Connection closed!");
        // TODO: Websocket connection closes
//        sessionHandler.removeSession(session);
    }

    @OnError
    public void onError(Throwable error) {
        throw new WebSocketException(error);
    }

}
