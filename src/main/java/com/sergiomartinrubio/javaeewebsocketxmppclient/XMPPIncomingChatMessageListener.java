package com.sergiomartinrubio.javaeewebsocketxmppclient;

import com.sergiomartinrubio.javaeewebsocketxmppclient.domain.TextMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.IncomingChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jxmpp.jid.EntityBareJid;

import javax.websocket.EncodeException;
import javax.websocket.Session;

import java.io.IOException;

import static com.sergiomartinrubio.javaeewebsocketxmppclient.domain.MessageType.CHAT;

@Slf4j
@AllArgsConstructor
public class XMPPIncomingChatMessageListener implements IncomingChatMessageListener {

    private final Session session;

    @Override
    public void newIncomingMessage(EntityBareJid from, Message message, Chat chat) {
        log.info("New message from " + from + ": " + message.getBody());
        TextMessage textMessage = TextMessage.builder()
                .from(from.getLocalpart().toString())
                .to(message.getTo().getLocalpartOrNull().toString())
                .content(message.getBody())
                .messageType(CHAT)
                .build();
        try {
            session.getBasicRemote().sendObject(textMessage);
        } catch (IOException | EncodeException e) {
            e.printStackTrace();
        }
    }
}
