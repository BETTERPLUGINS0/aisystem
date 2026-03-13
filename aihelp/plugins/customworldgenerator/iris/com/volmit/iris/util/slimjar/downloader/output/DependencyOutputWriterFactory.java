package com.volmit.iris.util.slimjar.downloader.output;

import com.volmit.iris.util.slimjar.downloader.strategy.FilePathStrategy;
import com.volmit.iris.util.slimjar.logging.LocationAwareProcessLogger;
import com.volmit.iris.util.slimjar.logging.ProcessLogger;
import com.volmit.iris.util.slimjar.resolver.data.Dependency;
import java.io.File;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class DependencyOutputWriterFactory implements OutputWriterFactory {
   @NotNull
   private static final ProcessLogger LOGGER = LocationAwareProcessLogger.generic();
   @NotNull
   private final FilePathStrategy outputFilePathStrategy;

   @Contract(
      pure = true
   )
   public DependencyOutputWriterFactory(@NotNull FilePathStrategy var1) {
      this.outputFilePathStrategy = var1;
   }

   @Contract(
      value = "_ -> new",
      pure = true
   )
   @NotNull
   public OutputWriter create(@NotNull Dependency var1) {
      LOGGER.debug("Creating OutputWriter for %s", var1);
      File var2 = this.outputFilePathStrategy.selectFileFor(var1);
      var2.getParentFile().mkdirs();
      return new ChanneledFileOutputWriter(var2);
   }

   @Contract(
      pure = true
   )
   @NotNull
   public FilePathStrategy getStrategy() {
      return this.outputFilePathStrategy;
   }
}
