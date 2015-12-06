package com.authenticator.account.controller;

import rx.functions.Action0;

public interface BackgroundExecutor {

    void execute(Action0 action);

    void finish();

}
