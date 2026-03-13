package com.volmit.iris.util.slimjar.resolver.strategy;

import com.volmit.iris.util.slimjar.resolver.data.Dependency;
import com.volmit.iris.util.slimjar.resolver.data.Repository;
import com.volmit.iris.util.slimjar.util.Repositories;
import java.util.Collection;
import java.util.Collections;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class MavenPathResolutionStrategy implements PathResolutionStrategy {
   @NotNull
   private static final String PATH_FORMAT = "%s%s/%s/%s/%3$s-%4$s.jar";

   @Contract(
      pure = true
   )
   @NotNull
   public Collection<String> pathTo(@NotNull Repository var1, @NotNull Dependency var2) {
      return Collections.singleton(String.format("%s%s/%s/%s/%3$s-%4$s.jar", Repositories.fetchFormattedUrl(var1), var2.groupId().replace('.', '/'), var2.artifactId(), var2.version()));
   }
}
