package com.sergiomartinrubio.javaeewebsocketxmppclient;

import lombok.Data;

/**
 *  A connection contains all the information needed to connect to an XMPP server
 *  and sign in.
 */
@Data
public class XMPPConnection {


    /**
     * The address of the server.
     */
    private String host;

    /**
     * The port to use (usually 5222).
     */
    private int port;

    /**
     * The XMPP domain is what follows after the '@' sign in XMPP addresses (JIDs).
     */
    private String domain;

    /**
     * The username is usually the localpart of the clients JID.
     */
    private String username;

    /**
     * The password.
     */
    private String password;

    /**
     * The resource.
     */
    private String resource;


}
