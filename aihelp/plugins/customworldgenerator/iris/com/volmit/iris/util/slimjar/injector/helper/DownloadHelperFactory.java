package com.volmit.iris.util.slimjar.injector.helper;

import com.volmit.iris.util.slimjar.downloader.DependencyDownloader;
import com.volmit.iris.util.slimjar.downloader.DependencyDownloaderFactory;
import com.volmit.iris.util.slimjar.downloader.output.DependencyOutputWriterFactory;
import com.volmit.iris.util.slimjar.downloader.strategy.FilePathStrategy;
import com.volmit.iris.util.slimjar.downloader.verify.DependencyVerifierFactory;
import com.volmit.iris.util.slimjar.relocation.Relocator;
import com.volmit.iris.util.slimjar.relocation.RelocatorFactory;
import com.volmit.iris.util.slimjar.relocation.helper.RelocationHelper;
import com.volmit.iris.util.slimjar.relocation.helper.RelocationHelperFactory;
import com.volmit.iris.util.slimjar.resolver.DependencyResolver;
import com.volmit.iris.util.slimjar.resolver.DependencyResolverFactory;
import com.volmit.iris.util.slimjar.resolver.ResolutionResult;
import com.volmit.iris.util.slimjar.resolver.data.DependencyData;
import com.volmit.iris.util.slimjar.resolver.enquirer.RepositoryEnquirerFactory;
import com.volmit.iris.util.slimjar.resolver.mirrors.MirrorSelector;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Map;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class DownloadHelperFactory {
   @NotNull
   private final Path downloadDirectoryPath;
   @NotNull
   private final RelocatorFactory relocatorFactory;
   @NotNull
   private final RelocationHelperFactory relocationHelperFactory;
   @NotNull
   private final DependencyResolverFactory resolverFactory;
   @NotNull
   private final RepositoryEnquirerFactory enquirerFactory;
   @NotNull
   private final DependencyDownloaderFactory downloaderFactory;
   @NotNull
   private final DependencyVerifierFactory verifier;
   @NotNull
   private final MirrorSelector mirrorSelector;

   public DownloadHelperFactory(@NotNull Path var1, @NotNull RelocatorFactory var2, @NotNull RelocationHelperFactory var3, @NotNull DependencyResolverFactory var4, @NotNull RepositoryEnquirerFactory var5, @NotNull DependencyDownloaderFactory var6, @NotNull DependencyVerifierFactory var7, @NotNull MirrorSelector var8) {
      this.downloadDirectoryPath = var1;
      this.relocatorFactory = var2;
      this.relocationHelperFactory = var3;
      this.resolverFactory = var4;
      this.enquirerFactory = var5;
      this.downloaderFactory = var6;
      this.verifier = var7;
      this.mirrorSelector = var8;
   }

   @Contract("_, _ -> new")
   @NotNull
   public DownloadHelper create(@NotNull DependencyData var1, @NotNull Map<String, ResolutionResult> var2) {
      Collection var3 = this.mirrorSelector.select(var1.repositories(), var1.mirrors());
      Relocator var4 = this.relocatorFactory.create(var1.relocations());
      RelocationHelper var5 = this.relocationHelperFactory.create(var4);
      FilePathStrategy var6 = FilePathStrategy.createDefault(this.downloadDirectoryPath.toFile());
      DependencyOutputWriterFactory var7 = new DependencyOutputWriterFactory(var6);
      DependencyResolver var8 = this.resolverFactory.create(var3, var2, this.enquirerFactory);
      DependencyDownloader var9 = this.downloaderFactory.create(var7, var8, this.verifier.create(var8));
      return new DownloadHelper(var9, var5);
   }
}
