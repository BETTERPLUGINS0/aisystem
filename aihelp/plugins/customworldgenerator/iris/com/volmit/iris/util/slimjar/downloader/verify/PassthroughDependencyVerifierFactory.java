package com.volmit.iris.util.slimjar.downloader.verify;

import com.volmit.iris.util.slimjar.resolver.DependencyResolver;
import com.volmit.iris.util.slimjar.resolver.data.Dependency;
import java.io.File;
import java.util.Optional;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class PassthroughDependencyVerifierFactory implements DependencyVerifierFactory {
   @Contract(
      value = "_ -> new",
      pure = true
   )
   @NotNull
   public DependencyVerifier create(@NotNull DependencyResolver var1) {
      return new PassthroughDependencyVerifierFactory.PassthroughVerifier();
   }

   private static final class PassthroughVerifier implements DependencyVerifier {
      @Contract(
         pure = true
      )
      public boolean verify(@NotNull File var1, @NotNull Dependency var2) {
         return var1.exists();
      }

      @Contract(
         pure = true
      )
      @NotNull
      public Optional<File> getChecksumFile(@NotNull Dependency var1) {
         return Optional.empty();
      }
   }
}
