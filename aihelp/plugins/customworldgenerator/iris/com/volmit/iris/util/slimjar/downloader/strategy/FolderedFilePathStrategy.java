package com.volmit.iris.util.slimjar.downloader.strategy;

import com.volmit.iris.util.slimjar.logging.LocationAwareProcessLogger;
import com.volmit.iris.util.slimjar.logging.ProcessLogger;
import com.volmit.iris.util.slimjar.resolver.data.Dependency;
import java.io.File;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class FolderedFilePathStrategy implements FilePathStrategy {
   @NotNull
   private static final ProcessLogger LOGGER = LocationAwareProcessLogger.generic();
   @NotNull
   private static final String DEPENDENCY_FILE_FORMAT = "%s/%s/%s/%s/%3$s-%4$s.jar";
   @NotNull
   private final File rootDirectory;

   @Contract(
      pure = true
   )
   private FolderedFilePathStrategy(@NotNull File var1) {
      this.rootDirectory = var1;
   }

   @Contract(
      pure = true
   )
   @NotNull
   public File selectFileFor(@NotNull Dependency var1) {
      String var2 = (String)var1.snapshot().map((var0) -> {
         return "-" + var0;
      }).orElse("");
      Object[] var10001 = new Object[]{this.rootDirectory.getPath(), var1.groupId().replace('.', '/'), var1.artifactId(), null};
      String var10004 = var1.version();
      var10001[3] = var10004 + var2;
      String var3 = String.format("%s/%s/%s/%s/%3$s-%4$s.jar", var10001);
      LOGGER.debug("Selected jar file for %s at %s.", var1, var3);
      return new File(var3);
   }

   @Contract(
      value = "_ -> new",
      pure = true
   )
   @NotNull
   public static FilePathStrategy createStrategy(@NotNull File var0) {
      FilePathStrategy.validateDirectory(var0);
      return new FolderedFilePathStrategy(var0);
   }
}
