package com.sergiomartinrubio.javaeewebsocketxmppclient;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class TextMessage {
    private String from;
    private String to;
    private String content;
}
