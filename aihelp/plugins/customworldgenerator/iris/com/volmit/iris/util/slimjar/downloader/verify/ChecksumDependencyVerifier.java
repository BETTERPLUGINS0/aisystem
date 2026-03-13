package com.volmit.iris.util.slimjar.downloader.verify;

import com.volmit.iris.util.slimjar.downloader.output.OutputWriter;
import com.volmit.iris.util.slimjar.downloader.output.OutputWriterFactory;
import com.volmit.iris.util.slimjar.exceptions.VerificationException;
import com.volmit.iris.util.slimjar.logging.LocationAwareProcessLogger;
import com.volmit.iris.util.slimjar.logging.ProcessLogger;
import com.volmit.iris.util.slimjar.resolver.DependencyResolver;
import com.volmit.iris.util.slimjar.resolver.ResolutionResult;
import com.volmit.iris.util.slimjar.resolver.data.Dependency;
import com.volmit.iris.util.slimjar.util.Connections;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.Optional;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class ChecksumDependencyVerifier implements DependencyVerifier {
   @NotNull
   private static final ProcessLogger LOGGER = LocationAwareProcessLogger.generic();
   @NotNull
   private final DependencyResolver resolver;
   @NotNull
   private final OutputWriterFactory outputWriterFactory;
   @NotNull
   private final DependencyVerifier fallbackVerifier;
   @NotNull
   private final ChecksumCalculator checksumCalculator;

   @Contract(
      pure = true
   )
   public ChecksumDependencyVerifier(@NotNull DependencyResolver var1, @NotNull OutputWriterFactory var2, @NotNull DependencyVerifier var3, @NotNull ChecksumCalculator var4) {
      this.resolver = var1;
      this.outputWriterFactory = var2;
      this.fallbackVerifier = var3;
      this.checksumCalculator = var4;
   }

   public boolean verify(@NotNull File var1, @NotNull Dependency var2) {
      if (!var1.exists()) {
         return false;
      } else {
         LOGGER.debug("Verifying checksum for %s", var2);
         File var3 = this.outputWriterFactory.getStrategy().selectFileFor(var2);
         var3.getParentFile().mkdirs();
         if (!var3.exists() && !this.prepareChecksumFile(var3, var2)) {
            LOGGER.debug("Unable to resolve checksum for %s, falling back to fallbackVerifier!", var2);
            return this.fallbackVerifier.verify(var1, var2);
         } else if (var3.length() == 0L) {
            LOGGER.debug("Required checksum not found for %s, using fallbackVerifier!", var2);
            return this.fallbackVerifier.verify(var1, var2);
         } else {
            String var4 = this.checksumCalculator.calculate(var1);

            String var5;
            try {
               var5 = (new String(Files.readAllBytes(var3.toPath()))).trim();
               int var6 = var5.indexOf(32);
               if (var6 != -1) {
                  var5 = var5.substring(0, var6);
               }
            } catch (IOException var7) {
               throw new VerificationException("Unable to read bytes from checksum file (%s)".formatted(new Object[]{var3}), var7);
            }

            LOGGER.debug("%s -> Actual checksum: %s;", var2, var4);
            LOGGER.debug("%s -> Expected checksum: %s;", var2, var5);
            if (!var4.equals(var5)) {
               LOGGER.error("Checksum mismatch for %s, expected %s, got %s", var2, var5, var4);
               return false;
            } else {
               LOGGER.debug("Checksum matched for %s.", var2);
               return true;
            }
         }
      }
   }

   @NotNull
   public Optional<File> getChecksumFile(@NotNull Dependency var1) {
      File var2 = this.outputWriterFactory.getStrategy().selectFileFor(var1);
      var2.getParentFile().mkdirs();
      return Optional.of(var2);
   }

   private boolean prepareChecksumFile(@NotNull File var1, @NotNull Dependency var2) {
      Optional var3 = this.resolver.resolve(var2);
      if (var3.isEmpty()) {
         return false;
      } else {
         URL var4 = ((ResolutionResult)var3.get()).checksumURL();
         LOGGER.debug("Resolved checksum URL for %s as %s", var2, var4);

         try {
            if (var4 == null) {
               var1.createNewFile();
               return false;
            }

            URLConnection var5 = Connections.createDownloadConnection(var4);
            InputStream var6 = var5.getInputStream();
            OutputWriter var7 = this.outputWriterFactory.create(var2);
            var7.writeFrom(var6, (long)var5.getContentLength());
            Connections.tryDisconnect(var5);
         } catch (IOException var8) {
            throw new VerificationException("Unable to get checksum for %s".formatted(new Object[]{var2.toString()}), var8);
         }

         LOGGER.debug("Downloaded checksum for %s", var2);
         return true;
      }
   }
}
