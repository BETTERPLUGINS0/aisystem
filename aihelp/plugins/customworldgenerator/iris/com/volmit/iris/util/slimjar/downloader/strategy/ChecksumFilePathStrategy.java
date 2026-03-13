package com.volmit.iris.util.slimjar.downloader.strategy;

import com.volmit.iris.util.slimjar.logging.LocationAwareProcessLogger;
import com.volmit.iris.util.slimjar.logging.ProcessLogger;
import com.volmit.iris.util.slimjar.resolver.data.Dependency;
import java.io.File;
import java.util.Locale;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class ChecksumFilePathStrategy implements FilePathStrategy {
   @NotNull
   private static final ProcessLogger LOGGER = LocationAwareProcessLogger.generic();
   @NotNull
   private static final String DEPENDENCY_FILE_FORMAT = "%s/%s/%s/%s/%3$s-%4$s.jar.%5$s";
   @NotNull
   private final File rootDirectory;
   @NotNull
   private final String algorithm;

   @Contract(
      pure = true
   )
   private ChecksumFilePathStrategy(@NotNull File var1, @NotNull String var2) {
      this.rootDirectory = var1;
      this.algorithm = var2.replaceAll("[ -]", "").toLowerCase(Locale.ENGLISH);
   }

   @Contract(
      value = "_ -> new",
      pure = true
   )
   @NotNull
   public File selectFileFor(@NotNull Dependency var1) {
      String var2 = (String)var1.snapshot().map((var0) -> {
         return "-" + var0;
      }).orElse("");
      Object[] var10001 = new Object[]{this.rootDirectory.getPath(), var1.groupId().replace('.', '/'), var1.artifactId(), null, null};
      String var10004 = var1.version();
      var10001[3] = var10004 + var2;
      var10001[4] = this.algorithm;
      String var3 = "%s/%s/%s/%s/%3$s-%4$s.jar.%5$s".formatted(var10001);
      LOGGER.debug("Selected checksum file for %s at %s", var1, var3);
      return new File(var3);
   }

   @Contract(
      value = "_, _ -> new",
      pure = true
   )
   @NotNull
   public static FilePathStrategy createStrategy(@NotNull File var0, @NotNull String var1) {
      FilePathStrategy.validateDirectory(var0);
      return new ChecksumFilePathStrategy(var0, var1);
   }
}
