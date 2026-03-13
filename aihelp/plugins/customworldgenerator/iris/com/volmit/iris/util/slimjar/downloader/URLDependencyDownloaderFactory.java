package com.volmit.iris.util.slimjar.downloader;

import com.volmit.iris.util.slimjar.downloader.output.OutputWriterFactory;
import com.volmit.iris.util.slimjar.downloader.verify.DependencyVerifier;
import com.volmit.iris.util.slimjar.resolver.DependencyResolver;
import org.jetbrains.annotations.NotNull;

public final class URLDependencyDownloaderFactory implements DependencyDownloaderFactory {
   @NotNull
   public DependencyDownloader create(@NotNull OutputWriterFactory var1, @NotNull DependencyResolver var2, @NotNull DependencyVerifier var3) {
      return new URLDependencyDownloader(var1, var2, var3);
   }
}
