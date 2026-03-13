package com.volmit.iris.util.slimjar.downloader.strategy;

import com.volmit.iris.util.slimjar.resolver.data.Dependency;
import java.io.File;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface FilePathStrategy {
   @NotNull
   File selectFileFor(@NotNull Dependency var1);

   @NotNull
   static FilePathStrategy createDefault(@NotNull File root) {
      return FolderedFilePathStrategy.createStrategy(root);
   }

   @Contract(
      pure = true
   )
   @NotNull
   static FilePathStrategy createRelocationStrategy(@NotNull File root, @NotNull String applicationName) {
      return RelocationFilePathStrategy.createStrategy(root, applicationName);
   }

   @Contract(
      pure = true
   )
   static void validateDirectory(@NotNull File rootDirectory) throws IllegalStateException {
      if (!rootDirectory.exists() && !rootDirectory.mkdirs()) {
         throw new IllegalStateException("Failed to create root directory for checksum file strategy (%s).".formatted(new Object[]{rootDirectory}));
      } else if (!rootDirectory.isDirectory()) {
         throw new IllegalStateException("Root directory for checksum file strategy is not a directory (%s).".formatted(new Object[]{rootDirectory}));
      }
   }
}
