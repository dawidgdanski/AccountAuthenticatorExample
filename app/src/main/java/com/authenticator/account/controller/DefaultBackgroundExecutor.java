package com.authenticator.account.controller;

import com.google.common.collect.Lists;

import java.util.Collection;

import rx.Subscription;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

public class DefaultBackgroundExecutor implements BackgroundExecutor {

    private final Collection<Subscription> subscriptions = Lists.newLinkedList();

    @Override
    public synchronized void execute(Action0 action) {

        final Subscription subscription = Schedulers.newThread().createWorker().schedule(action);
        subscriptions.add(subscription);
    }

    @Override
    public synchronized void finish() {
        for(Subscription subscription : subscriptions) {
            subscription.unsubscribe();
        }

        subscriptions.clear();
    }


}
