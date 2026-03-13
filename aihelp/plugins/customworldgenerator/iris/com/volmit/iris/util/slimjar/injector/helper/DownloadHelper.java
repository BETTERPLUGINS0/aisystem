package com.volmit.iris.util.slimjar.injector.helper;

import com.volmit.iris.util.slimjar.downloader.DependencyDownloader;
import com.volmit.iris.util.slimjar.relocation.helper.RelocationHelper;
import com.volmit.iris.util.slimjar.resolver.data.Dependency;
import java.io.File;
import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentHashMap.KeySetView;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;

public final class DownloadHelper {
   private final KeySetView<Dependency, Boolean> downloadedDependencies;
   private final DependencyDownloader dependencyDownloader;
   private final RelocationHelper relocationHelper;

   public DownloadHelper(DependencyDownloader var1, RelocationHelper var2) {
      this.dependencyDownloader = var1;
      this.relocationHelper = var2;
      this.downloadedDependencies = ConcurrentHashMap.newKeySet();
   }

   @NotNull
   public Stream<File> fetch(@NotNull Collection<Dependency> var1) {
      Stream var10000 = var1.parallelStream();
      KeySetView var10001 = this.downloadedDependencies;
      Objects.requireNonNull(var10001);
      return var10000.filter(var10001::add).flatMap((var1x) -> {
         return Stream.concat(this.fetch(var1x.transitive()), this.dependencyDownloader.download(var1x).map((var2) -> {
            return this.relocationHelper.relocate(var1x, var2);
         }).stream());
      });
   }
}
