package com.volmit.iris.util.slimjar.resolver.strategy;

import com.volmit.iris.util.slimjar.resolver.data.Dependency;
import com.volmit.iris.util.slimjar.resolver.data.Repository;
import com.volmit.iris.util.slimjar.util.Repositories;
import java.util.Arrays;
import java.util.Collection;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class MavenSnapshotPathResolutionStrategy implements PathResolutionStrategy {
   @NotNull
   private static final String PATH_FORMAT_ALT = "%s%s/%s/%s-SNAPSHOT/%4$s-%s/%3$s-%4$s-%5$s.jar";
   @NotNull
   private static final String PATH_FORMAT = "%s%s/%s/%s-SNAPSHOT/%3$s-%4$s-%5$s.jar";

   @Contract(
      pure = true
   )
   @NotNull
   public Collection<String> pathTo(@NotNull Repository var1, @NotNull Dependency var2) {
      String var3 = Repositories.fetchFormattedUrl(var1);
      String var4 = var2.version().replace("-SNAPSHOT", "");
      String var5 = String.format("%s%s/%s/%s-SNAPSHOT/%4$s-%s/%3$s-%4$s-%5$s.jar", var3, var2.groupId().replace('.', '/'), var2.artifactId(), var4, var2.snapshotId());
      String var6 = String.format("%s%s/%s/%s-SNAPSHOT/%3$s-%4$s-%5$s.jar", var3, var2.groupId().replace('.', '/'), var2.artifactId(), var4, var2.snapshotId());
      return Arrays.asList(var6, var5);
   }
}
