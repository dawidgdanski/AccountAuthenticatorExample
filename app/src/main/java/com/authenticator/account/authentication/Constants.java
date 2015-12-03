package com.authenticator.account.authentication;

public final class Constants {

    private Constants() {}

    public static final String ACCOUNT_TYPE = "com.authenticator.account.example";
    public static final String AUTH_TOKEN_TYPE = "AUTH_TOKEN_TYPE";
    public static final String IS_NEW_ACCOUNT = "IS_NEW_ACCOUNT";
    public static final String ACCOUNT_NAME = "My Account";

    public static final class Access {

        private Access() { }

        public static final String AUTH_TOKEN_TYPE_READ_ONLY = "Read Only";
        public static final String AUTH_TOKEN_TYPE_READ_ONLY_LABEL = "Read only access to DD account";

        public static final String AUTH_TOKEN_FULL_ACCESS = "Full Access";
        public static final String AUTH_TOKEN_FULL_ACCESS_LABEL = "Full access to DD account";
    }

    public static final class Parse {

        private Parse() { }

        public static final String PARAMETER_USERNAME = "username";
        public static final String PARAMETER_PASSWORD = "password";
    }
}
