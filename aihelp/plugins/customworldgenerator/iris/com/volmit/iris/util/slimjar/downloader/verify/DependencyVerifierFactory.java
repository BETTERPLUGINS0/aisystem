package com.volmit.iris.util.slimjar.downloader.verify;

import com.volmit.iris.util.slimjar.resolver.DependencyResolver;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface DependencyVerifierFactory {
   @NotNull
   DependencyVerifier create(@NotNull DependencyResolver var1);
}
