package com.sergiomartinrubio.javaeewebsocketxmppclient;

import javax.json.Json;
import javax.json.JsonObject;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class MessageEncoder implements Encoder.Text<TextMessage> {
    @Override
    public String encode(TextMessage object) throws EncodeException {
//        JsonObject value = Json.createObjectBuilder()
//                .add("firstName", "John")
//                .add("lastName", "Smith")
//                .add("age", 25)
//                .add("address", Json.createObjectBuilder()
//                        .add("streetAddress", "21 2nd Street")
//                        .add("city", "New York")
//                        .add("state", "NY")
//                        .add("postalCode", "10021"))
//                .add("phoneNumber", Json.createArrayBuilder()
//                        .add(Json.createObjectBuilder()
//                                .add("type", "home")
//                                .add("number", "212 555-1234"))
//                        .add(Json.createObjectBuilder()
//                                .add("type", "fax")
//                                .add("number", "646 555-4567")))
//                .build();
        return null;
    }

    @Override
    public void init(EndpointConfig config) {

    }

    @Override
    public void destroy() {

    }
}
