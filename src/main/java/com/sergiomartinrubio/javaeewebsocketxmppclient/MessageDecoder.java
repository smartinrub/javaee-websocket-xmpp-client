package com.sergiomartinrubio.javaeewebsocketxmppclient;

import com.google.gson.Gson;
import com.sergiomartinrubio.javaeewebsocketxmppclient.domain.TextMessage;

import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

public class MessageDecoder implements Decoder.Text<TextMessage> {

    @Override
    public TextMessage decode(String message) {
        Gson gson = new Gson();
        return gson.fromJson(message, TextMessage.class);
    }

    @Override
    public boolean willDecode(String message) {
        return (message != null);
    }

    @Override
    public void init(EndpointConfig config) {

    }

    @Override
    public void destroy() {

    }
}