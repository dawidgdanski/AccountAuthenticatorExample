package com.authenticator.account.di;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.authenticator.account.ui.ActivityComponent;
import com.authenticator.account.ui.ActivityModule;
import com.authenticator.account.ui.DaggerActivityComponent;
import com.google.common.base.Preconditions;
import com.squareup.leakcanary.RefWatcher;

public class DependencyInjector {

    private static final String PRECONDITION_MESSAGE_GRAPH_NOT_INITIALIZED =
            "Dependency graph not initialized";

    private static DependencyGraph dependencyGraph;

    private DependencyInjector() {
    }

    public static synchronized void initialize(ModuleSupplier supplier) {
        dependencyGraph = MainComponent.Initializer.initialize(supplier);
    }

    public static synchronized boolean isInitialized() {
        return dependencyGraph != null;
    }

    public static synchronized DependencyGraph getGraph() {
        Preconditions.checkState(isInitialized(), PRECONDITION_MESSAGE_GRAPH_NOT_INITIALIZED);

        return dependencyGraph;
    }

    public static ActivityComponent activityComponent(@NonNull final Activity activity) {
        return DaggerActivityComponent.builder()
                .dependencyGraph(getGraph())
                .activityModule(new ActivityModule(activity))
                .build();
    }

    public static RefWatcher refWatcher() {
        return getGraph().refWatcher().get();
    }
}
