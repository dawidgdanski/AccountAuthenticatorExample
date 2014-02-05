package com.authenticator.account.auth;

import com.authenticator.account.interfaces.ServerAuthenticateCallbacks;

public class ServerAuthCallbacksFactory {

    public static ServerAuthenticateCallbacks construct(final ServerAuthenticateCallbacks.Server serverType) {

        switch(serverType) {
            case PARSE_COM:
                return new ParseComServerAuthenticate();

            default:
                throw new IllegalArgumentException("Unsupported server callbacks request");
        }

    }

}
