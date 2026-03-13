package com.volmit.iris.util.slimjar.downloader;

import com.volmit.iris.util.slimjar.downloader.output.OutputWriterFactory;
import com.volmit.iris.util.slimjar.downloader.verify.DependencyVerifier;
import com.volmit.iris.util.slimjar.resolver.DependencyResolver;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface DependencyDownloaderFactory {
   @NotNull
   DependencyDownloader create(@NotNull OutputWriterFactory var1, @NotNull DependencyResolver var2, @NotNull DependencyVerifier var3);
}
