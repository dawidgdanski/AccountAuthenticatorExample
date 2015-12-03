package com.authenticator.account.model;

import com.google.auto.value.AutoValue;

import java.io.Serializable;

@AutoValue
public abstract class User implements Serializable {

    public static final String JSON_SESSION_TOKEN = "sessionToken";

    public static User.Builder builder() {
        return new AutoValue_User.Builder();
    }

    public abstract String getFirstName();

    public abstract String getLastName();

    public abstract String getUserName();

    public abstract String getPhone();

    public abstract String getObjectId();

    public abstract String getSessionToken();

    public abstract String getGravatarId();

    public abstract String getAvatarUrl();

    @AutoValue.Builder
    public static abstract class Builder {

        public abstract Builder setFirstName(String firstName);

        public abstract Builder setLastName(String lastName);

        public abstract Builder setUserName(String userName);

        public abstract Builder setPhone(String phone);

        public abstract Builder setObjectId(String objectId);

        public abstract Builder setSessionToken(String sessionToken);

        public abstract Builder setGravatarId(String gravatarId);

        public abstract Builder setAvatarUrl(String gravatarUrl);

        public abstract User build();
    }
}
