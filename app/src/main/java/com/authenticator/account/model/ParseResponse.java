package com.authenticator.account.model;

public class ParseResponse {

    public final String createdAt;

    public final String objectId;

    public final String sessionToken;

    public final String updatedAt;

    public final String username;

    public ParseResponse(String createdAt,
                         String objectId,
                         String sessionToken,
                         String updatedAt,
                         String username) {
        this.createdAt = createdAt;
        this.objectId = objectId;
        this.sessionToken = sessionToken;
        this.updatedAt = updatedAt;
        this.username = username;
    }
}
