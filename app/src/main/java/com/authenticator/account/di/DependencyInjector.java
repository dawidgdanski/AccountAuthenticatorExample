package com.authenticator.account.di;

import com.authenticator.account.AuthenticationApplication;
import com.google.common.base.Preconditions;

public class DependencyInjector {

    private static final String PRECONDITION_MESSAGE_GRAPH_NOT_INITIALIZED =
            "Dependency graph not initialized";

    private static DependencyGraph dependencyGraph;

    private DependencyInjector() {
    }

    public static synchronized void initialize(AuthenticationApplication application) {
        dependencyGraph = MainComponent.Initializer.initialize(application, application);
    }

    public static synchronized boolean isInitialized() {
        return dependencyGraph != null;
    }

    public static synchronized DependencyGraph getGraph() {
        Preconditions.checkNotNull(isInitialized(), PRECONDITION_MESSAGE_GRAPH_NOT_INITIALIZED);

        return dependencyGraph;
    }

}
