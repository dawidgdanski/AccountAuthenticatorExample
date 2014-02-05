package com.authenticator.account.model;

import java.io.Serializable;

public class User implements Serializable {

    public static final String JSON_SESSION_TOKEN = "sessionToken";
    public static final String JSON_USERNAME = "username";
    public static final String JSON_PASSWORD = "password";

    private final String mFirstName;
    private final String mLastName;
    private final String mUserName;
    private final String mPhone;
    private final String mObjectId;
    private final String mSessionToken;
    private final String mGravatarId;
    private final String mAvatarUrl;

    public User(String mFirstName, String mLastName, String mUserName, String mPhone, String mObjectId, String mSessionToken, String mGravatarId, String mAvatarUrl) {
        this.mFirstName = mFirstName;
        this.mLastName = mLastName;
        this.mUserName = mUserName;
        this.mPhone = mPhone;
        this.mObjectId = mObjectId;
        this.mSessionToken = mSessionToken;
        this.mGravatarId = mGravatarId;
        this.mAvatarUrl = mAvatarUrl;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public String getUserName() {
        return mUserName;
    }

    public String getPhone() {
        return mPhone;
    }

    public String getObjectId() {
        return mObjectId;
    }

    public String getSessionToken() {
        return mSessionToken;
    }

    public String getGravatarId() {
        return mGravatarId;
    }

    public String getmAvatarUrl() {
        return mAvatarUrl;
    }
}
