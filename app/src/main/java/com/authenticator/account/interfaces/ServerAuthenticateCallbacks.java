package com.authenticator.account.interfaces;

import com.authenticator.account.exception.AuthException;

public interface ServerAuthenticateCallbacks {

    String CONTENT_TYPE = "Content-Type";

    String onUserSignUp(final String name, final String email, final String password, final String authType) throws AuthException;
    String onUserSignIn(final String user, final String password, final String authType) throws AuthException;

    enum Server {
        PARSE_COM
    }
}
