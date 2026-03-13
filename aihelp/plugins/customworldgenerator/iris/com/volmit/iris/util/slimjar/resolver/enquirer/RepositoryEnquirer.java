package com.volmit.iris.util.slimjar.resolver.enquirer;

import com.volmit.iris.util.slimjar.resolver.ResolutionResult;
import com.volmit.iris.util.slimjar.resolver.data.Dependency;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface RepositoryEnquirer {
   @Nullable
   ResolutionResult enquire(@NotNull Dependency var1);
}
