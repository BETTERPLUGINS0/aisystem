package com.dfsek.terra;

import com.dfsek.tectonic.api.TypeRegistry;
import com.dfsek.terra.addon.BootstrapAddonLoader;
import com.dfsek.terra.addon.DependencySorter;
import com.dfsek.terra.addon.EphemeralAddon;
import com.dfsek.terra.addon.InternalAddon;
import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.addon.BaseAddon;
import com.dfsek.terra.api.addon.bootstrap.BootstrapAddonClassLoader;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.config.PluginConfig;
import com.dfsek.terra.api.event.EventManager;
import com.dfsek.terra.api.event.events.platform.PlatformInitializationEvent;
import com.dfsek.terra.api.event.functional.FunctionalEventHandler;
import com.dfsek.terra.api.inject.Injector;
import com.dfsek.terra.api.inject.impl.InjectorImpl;
import com.dfsek.terra.api.profiler.Profiler;
import com.dfsek.terra.api.registry.CheckedRegistry;
import com.dfsek.terra.api.registry.Registry;
import com.dfsek.terra.api.registry.key.StringIdentifiable;
import com.dfsek.terra.api.util.generic.pair.Pair;
import com.dfsek.terra.api.util.mutable.MutableBoolean;
import com.dfsek.terra.api.util.reflection.TypeKey;
import com.dfsek.terra.config.GenericLoaders;
import com.dfsek.terra.config.PluginConfigImpl;
import com.dfsek.terra.event.EventManagerImpl;
import com.dfsek.terra.lib.commons.io.FileUtils;
import com.dfsek.terra.lib.commons.io.IOUtils;
import com.dfsek.terra.lib.yaml.snakeyaml.Yaml;
import com.dfsek.terra.profiler.ProfilerImpl;
import com.dfsek.terra.registry.CheckedRegistryImpl;
import com.dfsek.terra.registry.LockedRegistryImpl;
import com.dfsek.terra.registry.OpenRegistryImpl;
import com.dfsek.terra.registry.master.ConfigRegistry;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractPlatform implements Platform {
   private static final Logger logger = LoggerFactory.getLogger(AbstractPlatform.class);
   private static final MutableBoolean LOADED = new MutableBoolean(false);
   private final EventManager eventManager = new EventManagerImpl();
   private final ConfigRegistry configRegistry = new ConfigRegistry();
   private final CheckedRegistry<ConfigPack> checkedConfigRegistry;
   private final Profiler profiler;
   private final GenericLoaders loaders;
   private final PluginConfigImpl config;
   private final CheckedRegistry<BaseAddon> addonRegistry;
   private final Registry<BaseAddon> lockedAddonRegistry;
   private static final String moonrise = "Moonrise";

   public AbstractPlatform() {
      this.checkedConfigRegistry = new CheckedRegistryImpl(this.configRegistry);
      this.profiler = new ProfilerImpl();
      this.loaders = new GenericLoaders(this);
      this.config = new PluginConfigImpl();
      this.addonRegistry = new CheckedRegistryImpl(new OpenRegistryImpl(TypeKey.of(BaseAddon.class)));
      this.lockedAddonRegistry = new LockedRegistryImpl(this.addonRegistry);
   }

   public ConfigRegistry getRawConfigRegistry() {
      return this.configRegistry;
   }

   protected Iterable<BaseAddon> platformAddon() {
      return Collections.emptySet();
   }

   protected InternalAddon load() {
      if (LOADED.get()) {
         throw new IllegalStateException("Someone tried to initialize Terra, but Terra has already initialized. This is most likely due to a broken platform implementation, or a misbehaving mod.");
      } else {
         LOADED.set(true);
         logger.info("Initializing Terra...");

         try {
            InputStream stream = this.getClass().getResourceAsStream("/config.yml");

            try {
               logger.info("Loading config.yml");
               File configFile = new File(this.getDataFolder(), "config.yml");
               if (!configFile.exists()) {
                  logger.info("Dumping config.yml...");
                  if (stream == null) {
                     logger.warn("Could not find config.yml in JAR");
                  } else {
                     FileUtils.copyInputStreamToFile(stream, configFile);
                  }
               }
            } catch (Throwable var5) {
               if (stream != null) {
                  try {
                     stream.close();
                  } catch (Throwable var4) {
                     var5.addSuppressed(var4);
                  }
               }

               throw var5;
            }

            if (stream != null) {
               stream.close();
            }
         } catch (IOException var6) {
            logger.error("Error loading config.yml resource from jar", var6);
         }

         this.config.load(this);
         if (this.config.dumpDefaultConfig()) {
            this.dumpResources();
         } else {
            logger.info("Skipping resource dumping.");
         }

         if (this.config.isDebugProfiler()) {
            this.profiler.start();
         }

         InternalAddon internalAddon = this.loadAddons();
         ((FunctionalEventHandler)this.eventManager.getHandler(FunctionalEventHandler.class)).register(internalAddon, PlatformInitializationEvent.class).then((event) -> {
            logger.info("Loading config packs...");
            this.configRegistry.loadAll(this);
            logger.info("Loaded packs.");
         }).global();
         logger.info("Terra addons successfully loaded.");
         logger.info("Finished initialization.");
         return internalAddon;
      }
   }

   protected InternalAddon loadAddons() {
      List<BaseAddon> addonList = new ArrayList();
      InternalAddon internalAddon = new InternalAddon();
      addonList.add(internalAddon);
      Iterable var10000 = this.platformAddon();
      Objects.requireNonNull(addonList);
      var10000.forEach(addonList::add);
      BootstrapAddonLoader bootstrapAddonLoader = new BootstrapAddonLoader();
      Path addonsFolder = this.getDataFolder().toPath().resolve("addons");
      Injector<Platform> platformInjector = new InjectorImpl(this);
      platformInjector.addExplicitTarget(Platform.class);
      BootstrapAddonClassLoader bootstrapAddonClassLoader = new BootstrapAddonClassLoader(new URL[0], this.getClass().getClassLoader());
      bootstrapAddonLoader.loadAddons(addonsFolder, bootstrapAddonClassLoader).forEach((bootstrapAddon) -> {
         platformInjector.inject(bootstrapAddon);
         Iterable var10000 = bootstrapAddon.loadAddons(addonsFolder, bootstrapAddonClassLoader);
         Objects.requireNonNull(addonList);
         var10000.forEach(addonList::add);
      });
      addonList.sort(Comparator.comparing(StringIdentifiable::getID));
      if (logger.isInfoEnabled()) {
         StringBuilder builder = new StringBuilder();
         builder.append("Loading ").append(addonList.size()).append(" Terra addons:");
         Iterator var8 = addonList.iterator();

         while(var8.hasNext()) {
            BaseAddon addon = (BaseAddon)var8.next();
            builder.append("\n        ").append("- ").append(addon.getID()).append("@").append(addon.getVersion().getFormatted());
         }

         logger.info(builder.toString());
      }

      DependencySorter sorter = new DependencySorter();
      Objects.requireNonNull(sorter);
      addonList.forEach(sorter::add);
      sorter.sort().forEach((addonx) -> {
         platformInjector.inject(addonx);
         addonx.initialize();
         if (!(addonx instanceof EphemeralAddon)) {
            this.addonRegistry.register(addonx.key(addonx.getID()), addonx);
         }

      });
      return internalAddon;
   }

   protected void dumpResources() {
      try {
         InputStream resourcesConfig = this.getClass().getResourceAsStream("/resources.yml");

         label54: {
            try {
               if (resourcesConfig == null) {
                  logger.info("No resources config found. Skipping resource dumping.");
                  break label54;
               }

               Path data = this.getDataFolder().toPath();
               Path addonsPath = data.resolve("addons");
               Files.createDirectories(addonsPath);
               Set<Pair<Path, String>> paths = (Set)Files.walk(addonsPath).map((path) -> {
                  return Pair.of(path, data.relativize(path).toString());
               }).map(Pair.mapRight((s) -> {
                  return s.contains("+") ? s.substring(0, s.lastIndexOf(43)) : s;
               })).filter(Pair.testRight((s) -> {
                  return s.contains(".");
               })).map(Pair.mapRight((s) -> {
                  return s.substring(0, s.lastIndexOf(46));
               })).filter(Pair.testRight((s) -> {
                  return s.contains(".");
               })).map(Pair.mapRight((s) -> {
                  return s.substring(0, s.lastIndexOf(46));
               })).collect(Collectors.toSet());
               Set<String> pathsNoMajor = (Set)paths.stream().filter(Pair.testRight((s) -> {
                  return s.contains(".");
               })).map(Pair.mapRight((s) -> {
                  return s.substring(0, s.lastIndexOf(46));
               })).map(Pair.unwrapRight()).collect(Collectors.toSet());
               String resourceYaml = IOUtils.toString(resourcesConfig, StandardCharsets.UTF_8);
               Map<String, List<String>> resources = (Map)(new Yaml()).load(resourceYaml);
               resources.forEach((dir, entries) -> {
                  entries.forEach((entry) -> {
                     String resourceClassPath = dir + "/" + entry;
                     String resourcePath = resourceClassPath.replace('/', File.separatorChar);
                     File resource = new File(this.getDataFolder(), resourcePath);
                     if (!resource.exists()) {
                        try {
                           InputStream is = this.getClass().getResourceAsStream("/" + resourceClassPath);

                           label73: {
                              try {
                                 if (is != null) {
                                    Stream var10000 = paths.stream();
                                    Objects.requireNonNull(resourcePath);
                                    var10000.filter(Pair.testRight(resourcePath::startsWith)).forEach(Pair.consumeLeft((path) -> {
                                       logger.info("Removing outdated resource {}, replacing with {}", path, resourcePath);

                                       try {
                                          Files.delete(path);
                                       } catch (IOException var3) {
                                          throw new UncheckedIOException(var3);
                                       }
                                    }));
                                    var10000 = pathsNoMajor.stream();
                                    Objects.requireNonNull(resourcePath);
                                    if (var10000.anyMatch(resourcePath::startsWith)) {
                                       var10000 = paths.stream().map(Pair.unwrapRight());
                                       Objects.requireNonNull(resourcePath);
                                       if (var10000.noneMatch(resourcePath::startsWith)) {
                                          logger.warn("Addon {} has a new major version available. It will not be automatically updated; you will need to ensure compatibility and update manually.", resourcePath);
                                       }
                                    }

                                    logger.info("Dumping resource {}...", resource.getAbsolutePath());
                                    resource.getParentFile().mkdirs();
                                    resource.createNewFile();
                                    FileOutputStream os = new FileOutputStream(resource);

                                    try {
                                       IOUtils.copy(is, os);
                                    } catch (Throwable var14) {
                                       try {
                                          os.close();
                                       } catch (Throwable var13) {
                                          var14.addSuppressed(var13);
                                       }

                                       throw var14;
                                    }

                                    os.close();
                                    break label73;
                                 }

                                 logger.error("Resource {} doesn't exist on the classpath!", resourcePath);
                              } catch (Throwable var15) {
                                 if (is != null) {
                                    try {
                                       is.close();
                                    } catch (Throwable var12) {
                                       var15.addSuppressed(var12);
                                    }
                                 }

                                 throw var15;
                              }

                              if (is != null) {
                                 is.close();
                              }

                              return;
                           }

                           if (is != null) {
                              is.close();
                           }

                        } catch (IOException var16) {
                           throw new UncheckedIOException(var16);
                        }
                     }
                  });
               });
            } catch (Throwable var9) {
               if (resourcesConfig != null) {
                  try {
                     resourcesConfig.close();
                  } catch (Throwable var8) {
                     var9.addSuppressed(var8);
                  }
               }

               throw var9;
            }

            if (resourcesConfig != null) {
               resourcesConfig.close();
            }

            return;
         }

         if (resourcesConfig != null) {
            resourcesConfig.close();
         }

      } catch (IOException var10) {
         logger.error("Error while dumping resources...", var10);
      }
   }

   public static int getGenerationThreadsWithReflection(String className, String fieldName, String project) {
      try {
         Class aClass = Class.forName(className);
         int threads = aClass.getField(fieldName).getInt((Object)null);
         logger.info("{} found, setting {} generation threads.", project, threads);
         return threads;
      } catch (ClassNotFoundException var5) {
         logger.info("{} not found.", project);
      } catch (NoSuchFieldException var6) {
         logger.warn("{} found, but {} field not found this probably means {0} has changed its code and Terra has not updated to reflect that.", project, fieldName);
      } catch (IllegalAccessException var7) {
         logger.error("Failed to access {} field in {}, assuming 1 generation thread.", new Object[]{fieldName, project, var7});
      }

      return 0;
   }

   public static int getMoonriseGenerationThreadsWithReflection() {
      try {
         Class<?> prioritisedThreadPoolClazz = Class.forName("ca.spottedleaf.concurrentutil.executor.thread.PrioritisedThreadPool");
         Method getCoreThreadsMethod = prioritisedThreadPoolClazz.getDeclaredMethod("getCoreThreads");
         getCoreThreadsMethod.setAccessible(true);
         Class<?> moonriseCommonClazz = Class.forName("ca.spottedleaf.moonrise.common.util.MoonriseCommon");
         Object pool = moonriseCommonClazz.getDeclaredField("WORKER_POOL").get((Object)null);
         int threads = ((Thread[])getCoreThreadsMethod.invoke(pool)).length;
         logger.info("{} found, setting {} generation threads.", "Moonrise", threads);
         return threads;
      } catch (ClassNotFoundException var5) {
         logger.info("{} not found.", "Moonrise");
      } catch (NoSuchFieldException | NoSuchMethodException var6) {
         logger.warn("{} found, but field/method not found this probably means {0} has changed its code and Terra has not updated to reflect that.", "Moonrise");
      } catch (InvocationTargetException | IllegalAccessException var7) {
         logger.error("Failed to access thread values in {}, assuming 1 generation thread.", "Moonrise", var7);
      }

      return 0;
   }

   public void register(TypeRegistry registry) {
      this.loaders.register(registry);
   }

   @NotNull
   public PluginConfig getTerraConfig() {
      return this.config;
   }

   @NotNull
   public CheckedRegistry<ConfigPack> getConfigRegistry() {
      return this.checkedConfigRegistry;
   }

   @NotNull
   public Registry<BaseAddon> getAddons() {
      return this.lockedAddonRegistry;
   }

   @NotNull
   public EventManager getEventManager() {
      return this.eventManager;
   }

   @NotNull
   public Profiler getProfiler() {
      return this.profiler;
   }

   public int getGenerationThreads() {
      return 1;
   }
}
