package com.volmit.iris.util.slimjar.downloader.output;

import com.volmit.iris.util.slimjar.downloader.strategy.FilePathStrategy;
import com.volmit.iris.util.slimjar.resolver.data.Dependency;
import org.jetbrains.annotations.NotNull;

public interface OutputWriterFactory {
   @NotNull
   OutputWriter create(@NotNull Dependency var1);

   @NotNull
   FilePathStrategy getStrategy();
}
