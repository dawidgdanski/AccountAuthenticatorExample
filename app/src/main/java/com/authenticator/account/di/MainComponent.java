package com.authenticator.account.di;

import com.authenticator.account.AuthenticationApplication;

interface MainComponent extends DependencyGraph {

    final class Initializer {

        private Initializer() {
        }

        static DependencyGraph initialize(AuthenticationApplication application,
                                          ModuleProvisionContract moduleProvisionContract) {
            return DaggerDependencyGraph.builder()
                    .applicationModule(moduleProvisionContract.provideApplicationModule(application))
                    .authenticationModule(moduleProvisionContract.provideAuthenticationModule(application))
                    .broadcastModule(moduleProvisionContract.provideBroadcastModule(application))
                    .httpModule(moduleProvisionContract.provideHttpModule(application))
                    .build();
        }

    }

}
