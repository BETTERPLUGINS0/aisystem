package com.volmit.iris.util.slimjar.resolver.enquirer;

import com.volmit.iris.util.slimjar.resolver.data.Repository;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface RepositoryEnquirerFactory {
   @NotNull
   RepositoryEnquirer create(@NotNull Repository var1);
}
