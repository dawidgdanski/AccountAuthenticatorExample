package com.authenticator.account.di;

interface MainComponent extends DependencyGraph {

    final class Initializer {

        private Initializer() {
        }

        static DependencyGraph initialize(ModuleSupplier moduleSupplier) {
            return DaggerDependencyGraph.builder()
                    .applicationModule(moduleSupplier.provideApplicationModule())
                    .authenticationModule(moduleSupplier.provideAuthenticationModule())
                    .broadcastModule(moduleSupplier.provideBroadcastModule())
                    .httpModule(moduleSupplier.provideHttpModule())
                    .memoryLeakDetectionModule(moduleSupplier.provideMemoryLeakDetectionModule())
                    .build();
        }

    }

}
