package com.authenticator.account.auth;

public class Auth {

    private Auth() {}

    public static final String USERS_URL = "https://api.parse.com/1/users";
    public static final String LOGIN_URL = "https://api.parse.com/1/login";

    public static final String ACCOUNT_TYPE = "com.authenticator.account.example";
    public static final String AUTH_TOKEN_TYPE = "AUTH_TOKEN_TYPE";
    public static final String IS_NEW_ACCOUNT = "IS_NEW_ACCOUNT";
    public static final String ACCOUNT_NAME = "My Account";

    public class Access {
        public static final String AUTH_TOKEN_TYPE_READ_ONLY = "Read Only";
        public static final String AUTH_TOKEN_TYPE_READ_ONLY_LABEL = "Read only access to DD account";

        public static final String AUTH_TOKEN_FULL_ACCESS = "Full Access";
        public static final String AUTH_TOKEN_FULL_ACCESS_LABEL = "Full access to DD account";
    }

    public static final String USER_NAME = "User name";
    public static final String PASSWORD = "Password";

    public static final String ERROR = "error";

}
