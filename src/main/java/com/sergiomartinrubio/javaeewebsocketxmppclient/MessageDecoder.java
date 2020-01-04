package com.sergiomartinrubio.javaeewebsocketxmppclient;

import com.google.gson.Gson;

import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

public class MessageDecoder implements Decoder.Text<TextMessage> {


    @Override
    public TextMessage decode(String s) {
        Gson gson = new Gson();
        return gson.fromJson(s, TextMessage.class);
    }

    @Override
    public boolean willDecode(String s) {
        return (s != null);
    }

    @Override
    public void init(EndpointConfig config) {

    }

    @Override
    public void destroy() {

    }
}