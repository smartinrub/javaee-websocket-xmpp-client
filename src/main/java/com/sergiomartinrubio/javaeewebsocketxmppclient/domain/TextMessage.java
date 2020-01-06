package com.sergiomartinrubio.javaeewebsocketxmppclient.domain;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class TextMessage {
    private final String from;
    private final String to;
    private final String content;
    private final MessageType messageType;
}
