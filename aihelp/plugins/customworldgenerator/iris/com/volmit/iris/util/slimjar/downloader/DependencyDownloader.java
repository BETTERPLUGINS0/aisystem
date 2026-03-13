package com.volmit.iris.util.slimjar.downloader;

import com.volmit.iris.util.slimjar.exceptions.DownloaderException;
import com.volmit.iris.util.slimjar.resolver.data.Dependency;
import java.io.File;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface DependencyDownloader {
   @NotNull
   Optional<File> download(@NotNull Dependency var1) throws DownloaderException;
}
