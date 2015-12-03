package com.authenticator.account.authentication;

import com.authenticator.account.exception.AuthenticationException;

public interface AuthenticationProvider {

    String signUp(Credentials credentials) throws AuthenticationException;

    String signIn(Credentials credentials) throws AuthenticationException;
}
