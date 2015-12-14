package com.authenticator.account.authentication;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Credentials implements Parcelable {

    private static final String PARCELABLE_USERNAME = "username";
    private static final String PARCELABLE_PASSWORD = "password";
    private static final String PARCELABLE_EMAIL = "email";
    private static final String PARCELABLE_ACCOUNT_TYPE = "account_type";
    private static final String PARCELABLE_AUTH_TOKEN = "auth_token";

    public static final Creator<Credentials> CREATOR = new Creator<Credentials>() {
        @Override
        public Credentials createFromParcel(Parcel source) {

            final Bundle bundle = source.readBundle(Credentials.class.getClassLoader());

            return builder()
                    .setAccountType(bundle.getString(PARCELABLE_ACCOUNT_TYPE))
                    .setAuthTokenType(bundle.getString(PARCELABLE_AUTH_TOKEN))
                    .setEmail(bundle.getString(PARCELABLE_EMAIL))
                    .setUsername(bundle.getString(PARCELABLE_USERNAME))
                    .setPassword(bundle.getString(PARCELABLE_PASSWORD))
                    .build();
        }

        @Override
        public Credentials[] newArray(int size) {
            return new Credentials[size];
        }
    };

    public abstract String getUsername();

    public abstract String getPassword();

    public abstract @Nullable String getEmail();

    public abstract @Nullable String getAccountType();

    public abstract @Nullable String getAuthTokenType();

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Bundle bundle = new Bundle(Credentials.class.getClassLoader());
        bundle.putString(PARCELABLE_EMAIL, getEmail());
        bundle.putString(PARCELABLE_PASSWORD, getPassword());
        bundle.putString(PARCELABLE_USERNAME, getUsername());
        bundle.putString(PARCELABLE_AUTH_TOKEN, getAuthTokenType());
        bundle.putString(PARCELABLE_ACCOUNT_TYPE, getAccountType());

        dest.writeBundle(bundle);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static Builder builder() {
        return new AutoValue_Credentials.Builder()
                .setAccountType(Constants.ACCOUNT_TYPE);
    }

    @AutoValue.Builder
    public static abstract class Builder {

        public abstract Builder setUsername(String username);

        public abstract Builder setPassword(String password);

        public abstract Builder setEmail(@Nullable String email);

        public abstract Builder setAccountType(@Nullable String accountType);

        public abstract Builder setAuthTokenType(@Nullable String authTokenType);

        public abstract Credentials build();

    }

}
