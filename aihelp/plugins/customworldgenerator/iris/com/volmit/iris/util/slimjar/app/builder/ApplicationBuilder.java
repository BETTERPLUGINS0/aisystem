package com.volmit.iris.util.slimjar.app.builder;

import com.volmit.iris.util.slimjar.app.Application;
import com.volmit.iris.util.slimjar.downloader.DependencyDownloaderFactory;
import com.volmit.iris.util.slimjar.downloader.URLDependencyDownloaderFactory;
import com.volmit.iris.util.slimjar.downloader.output.DependencyOutputWriterFactory;
import com.volmit.iris.util.slimjar.downloader.strategy.ChecksumFilePathStrategy;
import com.volmit.iris.util.slimjar.downloader.strategy.FilePathStrategy;
import com.volmit.iris.util.slimjar.downloader.verify.ChecksumDependencyVerifierFactory;
import com.volmit.iris.util.slimjar.downloader.verify.DependencyVerifierFactory;
import com.volmit.iris.util.slimjar.downloader.verify.FileChecksumCalculator;
import com.volmit.iris.util.slimjar.downloader.verify.PassthroughDependencyVerifierFactory;
import com.volmit.iris.util.slimjar.injector.DependencyInjector;
import com.volmit.iris.util.slimjar.injector.DependencyInjectorFactory;
import com.volmit.iris.util.slimjar.injector.SimpleDependencyInjectorFactory;
import com.volmit.iris.util.slimjar.injector.helper.DownloadHelperFactory;
import com.volmit.iris.util.slimjar.injector.loader.Injectable;
import com.volmit.iris.util.slimjar.logging.LogDispatcher;
import com.volmit.iris.util.slimjar.logging.MediatingProcessLogger;
import com.volmit.iris.util.slimjar.logging.ProcessLogger;
import com.volmit.iris.util.slimjar.relocation.JarFileRelocatorFactory;
import com.volmit.iris.util.slimjar.relocation.RelocatorFactory;
import com.volmit.iris.util.slimjar.relocation.facade.JarRelocatorFacadeFactory;
import com.volmit.iris.util.slimjar.relocation.facade.ReflectiveJarRelocatorFacadeFactory;
import com.volmit.iris.util.slimjar.relocation.helper.RelocationHelperFactory;
import com.volmit.iris.util.slimjar.relocation.helper.VerifyingRelocationHelperFactory;
import com.volmit.iris.util.slimjar.relocation.meta.FlatFileMetaMediatorFactory;
import com.volmit.iris.util.slimjar.resolver.CachingDependencyResolverFactory;
import com.volmit.iris.util.slimjar.resolver.DependencyResolverFactory;
import com.volmit.iris.util.slimjar.resolver.data.Repository;
import com.volmit.iris.util.slimjar.resolver.enquirer.PingingRepositoryEnquirerFactory;
import com.volmit.iris.util.slimjar.resolver.enquirer.RepositoryEnquirerFactory;
import com.volmit.iris.util.slimjar.resolver.mirrors.MirrorSelector;
import com.volmit.iris.util.slimjar.resolver.mirrors.SimpleMirrorSelector;
import com.volmit.iris.util.slimjar.resolver.pinger.HttpURLPinger;
import com.volmit.iris.util.slimjar.resolver.reader.dependency.DependencyDataProviderFactory;
import com.volmit.iris.util.slimjar.resolver.reader.dependency.DependencyReader;
import com.volmit.iris.util.slimjar.resolver.reader.dependency.WrappingDependencyDataProviderFactory;
import com.volmit.iris.util.slimjar.resolver.reader.resolution.PreResolutionDataProviderFactory;
import com.volmit.iris.util.slimjar.resolver.reader.resolution.PreResolutionDataReader;
import com.volmit.iris.util.slimjar.resolver.reader.resolution.WrappingPreResolutionDataProviderFactory;
import com.volmit.iris.util.slimjar.resolver.strategy.MavenChecksumPathResolutionStrategy;
import com.volmit.iris.util.slimjar.resolver.strategy.MavenPathResolutionStrategy;
import com.volmit.iris.util.slimjar.resolver.strategy.MavenPomPathResolutionStrategy;
import com.volmit.iris.util.slimjar.resolver.strategy.MavenSnapshotPathResolutionStrategy;
import com.volmit.iris.util.slimjar.resolver.strategy.MediatingPathResolutionStrategy;
import java.net.URL;
import java.nio.file.Path;
import java.util.Collections;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class ApplicationBuilder<T extends ApplicationBuilder<T>> {
   @NotNull
   private static final Path DEFAULT_DOWNLOAD_DIRECTORY = Path.of(System.getProperty("user.home"), new String[]{".slimjar"});
   @NotNull
   protected final T self = this;
   @NotNull
   private final String applicationName;
   @Nullable
   private URL dependencyFileUrl;
   @Nullable
   private URL preResolutionFileUrl;
   @Nullable
   private Path downloadDirectoryPath;
   @Nullable
   private RelocatorFactory relocatorFactory;
   @Nullable
   private DependencyDataProviderFactory dataProviderFactory;
   @Nullable
   private PreResolutionDataProviderFactory preResolutionDataProviderFactory;
   @Nullable
   private RelocationHelperFactory relocationHelperFactory;
   @Nullable
   private DependencyInjectorFactory injectorFactory;
   @Nullable
   private DependencyResolverFactory resolverFactory;
   @Nullable
   private RepositoryEnquirerFactory enquirerFactory;
   @Nullable
   private DependencyDownloaderFactory downloaderFactory;
   @Nullable
   private DependencyVerifierFactory verifierFactory;
   @Nullable
   private MirrorSelector mirrorSelector;
   @Nullable
   private ProcessLogger logger;

   @Contract(
      pure = true
   )
   protected ApplicationBuilder(@NotNull String var1) {
      this.applicationName = var1;
   }

   @Contract(
      value = "_, _, _ -> new",
      pure = true
   )
   @NotNull
   public static IsolatedApplicationBuilder isolated(@NotNull String var0, @NotNull String var1, @Nullable @NotNull Object... var2) {
      return new IsolatedApplicationBuilder(var0, var1, var2);
   }

   @Contract(
      value = "_ -> new",
      pure = true
   )
   @NotNull
   public static InjectingApplicationBuilder<?> appending(@NotNull String var0) {
      return InjectingApplicationBuilder.create(var0);
   }

   @Contract("_, _ -> new")
   @NotNull
   public static InjectingApplicationBuilder<?> injecting(@NotNull String var0, @NotNull Injectable var1) {
      return InjectingApplicationBuilder.create(var0, var1);
   }

   @Contract(
      value = "_ -> this",
      mutates = "this"
   )
   @NotNull
   public final T dependencyFileUrl(@NotNull URL var1) {
      this.dependencyFileUrl = var1;
      return this.self;
   }

   @Contract(
      value = "_ -> this",
      mutates = "this"
   )
   @NotNull
   public final T preResolutionFileUrl(@NotNull URL var1) {
      this.preResolutionFileUrl = var1;
      return this.self;
   }

   @Contract(
      value = "_ -> this",
      mutates = "this"
   )
   @NotNull
   public final T downloadDirectoryPath(@NotNull Path var1) {
      this.downloadDirectoryPath = var1;
      return this.self;
   }

   @Contract(
      value = "_ -> this",
      mutates = "this"
   )
   @NotNull
   public final T relocatorFactory(@NotNull RelocatorFactory var1) {
      this.relocatorFactory = var1;
      return this.self;
   }

   @Contract(
      value = "_ -> this",
      mutates = "this"
   )
   @NotNull
   public final T dataProviderFactory(@NotNull DependencyDataProviderFactory var1) {
      this.dataProviderFactory = var1;
      return this.self;
   }

   @Contract(
      value = "_ -> this",
      mutates = "this"
   )
   @NotNull
   public final T preResolutionDataProviderFactory(@NotNull PreResolutionDataProviderFactory var1) {
      this.preResolutionDataProviderFactory = var1;
      return this.self;
   }

   @Contract(
      value = "_ -> this",
      mutates = "this"
   )
   @NotNull
   public final T relocationHelperFactory(@NotNull RelocationHelperFactory var1) {
      this.relocationHelperFactory = var1;
      return this.self;
   }

   @Contract(
      value = "_ -> this",
      mutates = "this"
   )
   @NotNull
   public final T injectorFactory(@NotNull DependencyInjectorFactory var1) {
      this.injectorFactory = var1;
      return this.self;
   }

   @Contract(
      value = "_ -> this",
      mutates = "this"
   )
   @NotNull
   public final T resolverFactory(@NotNull DependencyResolverFactory var1) {
      this.resolverFactory = var1;
      return this.self;
   }

   @Contract(
      value = "_ -> this",
      mutates = "this"
   )
   @NotNull
   public final T enquirerFactory(@NotNull RepositoryEnquirerFactory var1) {
      this.enquirerFactory = var1;
      return this.self;
   }

   @Contract(
      value = "_ -> this",
      mutates = "this"
   )
   @NotNull
   public final T downloaderFactory(@NotNull DependencyDownloaderFactory var1) {
      this.downloaderFactory = var1;
      return this.self;
   }

   @Contract(
      value = "_ -> this",
      mutates = "this"
   )
   @NotNull
   public final T verifierFactory(@NotNull DependencyVerifierFactory var1) {
      this.verifierFactory = var1;
      return this.self;
   }

   @Contract(
      value = "_ -> this",
      mutates = "this"
   )
   @NotNull
   public final T mirrorSelector(@NotNull MirrorSelector var1) {
      this.mirrorSelector = var1;
      return this.self;
   }

   @Contract(
      value = "_ -> this",
      mutates = "this"
   )
   @NotNull
   public final T logger(@NotNull ProcessLogger var1) {
      this.logger = var1;
      return this.self;
   }

   @Contract(
      pure = true
   )
   @NotNull
   protected final String getApplicationName() {
      return this.applicationName;
   }

   @Contract(
      mutates = "this"
   )
   @Nullable
   protected final URL getDependencyFileUrl() {
      if (this.dependencyFileUrl == null) {
         this.dependencyFileUrl = this.getClass().getClassLoader().getResource("slimjar.dat");
      }

      return this.dependencyFileUrl;
   }

   @Contract(
      mutates = "this"
   )
   @Nullable
   protected final URL getPreResolutionFileUrl() {
      if (this.preResolutionFileUrl == null) {
         this.preResolutionFileUrl = this.getClass().getClassLoader().getResource("slimjar-resolutions.dat");
      }

      return this.preResolutionFileUrl;
   }

   @Contract(
      mutates = "this"
   )
   @NotNull
   protected final Path getDownloadDirectoryPath() {
      if (this.downloadDirectoryPath == null) {
         this.downloadDirectoryPath = DEFAULT_DOWNLOAD_DIRECTORY;
      }

      return this.downloadDirectoryPath;
   }

   @Contract(
      mutates = "this"
   )
   @NotNull
   protected final RelocatorFactory getRelocatorFactory() {
      if (this.relocatorFactory == null) {
         JarRelocatorFacadeFactory var1 = ReflectiveJarRelocatorFacadeFactory.create(this.getDownloadDirectoryPath(), Collections.singleton(Repository.central()));
         this.relocatorFactory = new JarFileRelocatorFactory(var1);
      }

      return this.relocatorFactory;
   }

   @Contract(
      mutates = "this"
   )
   @NotNull
   protected final DependencyDataProviderFactory getDataProviderFactory() {
      if (this.dataProviderFactory == null) {
         this.dataProviderFactory = new WrappingDependencyDataProviderFactory(DependencyReader.DEFAULT);
      }

      return this.dataProviderFactory;
   }

   @Contract(
      mutates = "this"
   )
   @NotNull
   protected final PreResolutionDataProviderFactory getPreResolutionDataProviderFactory() {
      if (this.preResolutionDataProviderFactory == null) {
         this.preResolutionDataProviderFactory = new WrappingPreResolutionDataProviderFactory(PreResolutionDataReader.DEFAULT);
      }

      return this.preResolutionDataProviderFactory;
   }

   @Contract(
      mutates = "this"
   )
   @NotNull
   protected final RelocationHelperFactory getRelocationHelperFactory() {
      if (this.relocationHelperFactory == null) {
         FileChecksumCalculator var1 = new FileChecksumCalculator("SHA-256");
         FilePathStrategy var2 = FilePathStrategy.createRelocationStrategy(this.getDownloadDirectoryPath().toFile(), this.getApplicationName());
         FlatFileMetaMediatorFactory var3 = new FlatFileMetaMediatorFactory();
         this.relocationHelperFactory = new VerifyingRelocationHelperFactory(var1, var2, var3);
      }

      return this.relocationHelperFactory;
   }

   @Contract(
      mutates = "this"
   )
   @NotNull
   protected final DependencyInjectorFactory getInjectorFactory() {
      if (this.injectorFactory == null) {
         this.injectorFactory = new SimpleDependencyInjectorFactory();
      }

      return this.injectorFactory;
   }

   @Contract(
      mutates = "this"
   )
   @NotNull
   protected final DependencyResolverFactory getResolverFactory() {
      if (this.resolverFactory == null) {
         HttpURLPinger var1 = new HttpURLPinger();
         this.resolverFactory = new CachingDependencyResolverFactory(var1);
      }

      return this.resolverFactory;
   }

   @Contract(
      mutates = "this"
   )
   @NotNull
   protected final RepositoryEnquirerFactory getEnquirerFactory() {
      if (this.enquirerFactory == null) {
         MavenPathResolutionStrategy var1 = new MavenPathResolutionStrategy();
         MavenSnapshotPathResolutionStrategy var2 = new MavenSnapshotPathResolutionStrategy();
         MediatingPathResolutionStrategy var3 = new MediatingPathResolutionStrategy(var1, var2);
         MavenPomPathResolutionStrategy var4 = new MavenPomPathResolutionStrategy();
         MavenChecksumPathResolutionStrategy var5 = new MavenChecksumPathResolutionStrategy("SHA-1", var3);
         HttpURLPinger var6 = new HttpURLPinger();
         this.enquirerFactory = new PingingRepositoryEnquirerFactory(var3, var5, var4, var6);
      }

      return this.enquirerFactory;
   }

   @Contract(
      mutates = "this"
   )
   @NotNull
   protected final DependencyDownloaderFactory getDownloaderFactory() {
      if (this.downloaderFactory == null) {
         this.downloaderFactory = new URLDependencyDownloaderFactory();
      }

      return this.downloaderFactory;
   }

   @Contract(
      mutates = "this"
   )
   @NotNull
   protected final DependencyVerifierFactory getVerifierFactory() {
      if (this.verifierFactory == null) {
         FilePathStrategy var1 = ChecksumFilePathStrategy.createStrategy(this.getDownloadDirectoryPath().toFile(), "SHA-1");
         DependencyOutputWriterFactory var2 = new DependencyOutputWriterFactory(var1);
         PassthroughDependencyVerifierFactory var3 = new PassthroughDependencyVerifierFactory();
         FileChecksumCalculator var4 = new FileChecksumCalculator("SHA-1");
         this.verifierFactory = new ChecksumDependencyVerifierFactory(var2, var3, var4);
      }

      return this.verifierFactory;
   }

   @Contract(
      mutates = "this"
   )
   @NotNull
   protected final MirrorSelector getMirrorSelector() {
      if (this.mirrorSelector == null) {
         this.mirrorSelector = new SimpleMirrorSelector();
      }

      return this.mirrorSelector;
   }

   @Contract(
      mutates = "this"
   )
   @NotNull
   protected final ProcessLogger getLogger() {
      if (this.logger == null) {
         this.logger = (var0, var1) -> {
         };
      }

      return this.logger;
   }

   @Contract(
      value = "-> new",
      mutates = "this"
   )
   @NotNull
   protected final DependencyInjector createInjector() {
      DownloadHelperFactory var1 = new DownloadHelperFactory(this.getDownloadDirectoryPath(), this.getRelocatorFactory(), this.getRelocationHelperFactory(), this.getResolverFactory(), this.getEnquirerFactory(), this.getDownloaderFactory(), this.getVerifierFactory(), this.getMirrorSelector());
      return this.getInjectorFactory().create(var1);
   }

   @Contract(
      value = "-> new",
      mutates = "this"
   )
   @NotNull
   public final Application build() {
      MediatingProcessLogger var1 = LogDispatcher.getMediatingLogger();
      ProcessLogger var2 = this.getLogger();
      var1.addLogger(var2);
      Application var3 = this.buildApplication();
      var1.removeLogger(var2);
      return var3;
   }

   @Contract(
      value = "-> new",
      mutates = "this"
   )
   @NotNull
   protected abstract Application buildApplication();
}
