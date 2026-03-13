package com.volmit.iris.util.slimjar.downloader.verify;

import com.volmit.iris.util.slimjar.downloader.output.OutputWriterFactory;
import com.volmit.iris.util.slimjar.logging.LocationAwareProcessLogger;
import com.volmit.iris.util.slimjar.logging.ProcessLogger;
import com.volmit.iris.util.slimjar.resolver.DependencyResolver;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class ChecksumDependencyVerifierFactory implements DependencyVerifierFactory {
   @NotNull
   private static final ProcessLogger LOGGER = LocationAwareProcessLogger.generic();
   @NotNull
   private final OutputWriterFactory outputWriterFactory;
   @NotNull
   private final DependencyVerifierFactory fallbackVerifierFactory;
   @NotNull
   private final ChecksumCalculator checksumCalculator;

   @Contract(
      pure = true
   )
   public ChecksumDependencyVerifierFactory(@NotNull OutputWriterFactory var1, @NotNull DependencyVerifierFactory var2, @NotNull ChecksumCalculator var3) {
      this.outputWriterFactory = var1;
      this.fallbackVerifierFactory = var2;
      this.checksumCalculator = var3;
   }

   @Contract(
      value = "_ -> new",
      pure = true
   )
   @NotNull
   public DependencyVerifier create(@NotNull DependencyResolver var1) {
      LOGGER.debug("Creating verifier...");
      return new ChecksumDependencyVerifier(var1, this.outputWriterFactory, this.fallbackVerifierFactory.create(var1), this.checksumCalculator);
   }
}
