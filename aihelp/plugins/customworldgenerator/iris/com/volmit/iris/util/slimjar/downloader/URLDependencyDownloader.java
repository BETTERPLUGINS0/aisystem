package com.volmit.iris.util.slimjar.downloader;

import com.volmit.iris.util.slimjar.downloader.output.OutputWriter;
import com.volmit.iris.util.slimjar.downloader.output.OutputWriterFactory;
import com.volmit.iris.util.slimjar.downloader.verify.DependencyVerifier;
import com.volmit.iris.util.slimjar.exceptions.DownloaderException;
import com.volmit.iris.util.slimjar.exceptions.UnresolvedDependencyException;
import com.volmit.iris.util.slimjar.logging.LocationAwareProcessLogger;
import com.volmit.iris.util.slimjar.logging.LogDispatcher;
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
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Optional;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class URLDependencyDownloader implements DependencyDownloader {
   @NotNull
   private static final ProcessLogger LOGGER = LocationAwareProcessLogger.generic();
   private static final byte[] BOM_BYTES = "bom-file".getBytes();
   @NotNull
   private final OutputWriterFactory outputWriterProducer;
   @NotNull
   private final DependencyResolver dependencyResolver;
   @NotNull
   private final DependencyVerifier verifier;

   @Contract(
      pure = true
   )
   public URLDependencyDownloader(@NotNull OutputWriterFactory var1, @NotNull DependencyResolver var2, @NotNull DependencyVerifier var3) {
      this.outputWriterProducer = var1;
      this.dependencyResolver = var2;
      this.verifier = var3;
   }

   @NotNull
   public Optional<File> download(@NotNull Dependency var1) {
      File var2 = this.outputWriterProducer.getStrategy().selectFileFor(var1);
      if (this.existingBOM(var2.toPath())) {
         LOGGER.debug("Skipping download of %s because it is a bom.", var1);
         return Optional.empty();
      } else if (this.verifier.verify(var2, var1)) {
         LOGGER.debug("Skipping download of %s because it is already downloaded.", var1);
         return Optional.of(var2);
      } else {
         ResolutionResult var3 = (ResolutionResult)this.dependencyResolver.resolve(var1).orElseThrow(() -> {
            return new UnresolvedDependencyException(var1);
         });
         if (var3.aggregator()) {
            this.writeBOM(var2.toPath());
            return Optional.empty();
         } else {
            this.cleanupExisting(var2, var1);
            URL var4 = var3.dependencyURL();
            LogDispatcher.getMediatingLogger().info("Downloading %s", var4);

            URLConnection var5;
            InputStream var6;
            try {
               var5 = Connections.createDownloadConnection(var4);
               var6 = var5.getInputStream();
            } catch (IOException var9) {
               throw new DownloaderException("Failed to connect to " + String.valueOf(var4), var9);
            }

            LOGGER.debug("Connection successful! Downloading %s", String.valueOf(var1) + "...");
            OutputWriter var7 = this.outputWriterProducer.create(var1);
            LOGGER.debug("%s.Size = %s", var1, var5.getContentLength());
            File var8 = var7.writeFrom(var6, (long)var5.getContentLength());
            Connections.tryDisconnect(var5);
            this.verifier.verify(var8, var1);
            LOGGER.debug("Artifact %s downloaded successfully from %s!", var1, var4);
            return Optional.of(var8);
         }
      }
   }

   private boolean existingBOM(@NotNull Path var1) {
      try {
         return var1.toFile().exists() && var1.toFile().length() == (long)BOM_BYTES.length && Arrays.equals(Files.readAllBytes(var1), BOM_BYTES);
      } catch (IOException var3) {
         throw new DownloaderException("Failed to read existing BOM file", var3);
      }
   }

   private void writeBOM(@NotNull Path var1) {
      try {
         Files.createDirectories(var1.getParent());
         Files.createFile(var1);
         Files.write(var1, BOM_BYTES, new OpenOption[0]);
      } catch (IOException var3) {
         throw new DownloaderException("Failed to write to BOM file.", var3);
      }
   }

   private void cleanupExisting(@NotNull File var1, @NotNull Dependency var2) {
      try {
         if (var1.exists()) {
            Files.delete(var1.toPath());
         }

         Optional var3 = this.verifier.getChecksumFile(var2).filter(File::exists).map(File::toPath);
         if (!var3.isEmpty()) {
            Files.delete((Path)var3.get());
         }
      } catch (IOException var4) {
         throw new DownloaderException("Failed to cleanup existing files.", var4);
      }
   }
}
