package com.volmit.iris.util.slimjar.resolver.mirrors;

import com.volmit.iris.util.slimjar.resolver.data.Mirror;
import com.volmit.iris.util.slimjar.resolver.data.Repository;
import java.util.Collection;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface MirrorSelector {
   @NotNull
   Collection<Repository> select(@NotNull Collection<Repository> var1, @NotNull Collection<Mirror> var2);
}
