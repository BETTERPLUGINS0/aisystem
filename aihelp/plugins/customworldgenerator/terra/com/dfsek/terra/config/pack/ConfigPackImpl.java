package com.dfsek.terra.config.pack;

import ca.solostudios.strata.version.Version;
import ca.solostudios.strata.version.VersionRange;
import com.dfsek.paralithic.eval.parser.Parser.ParseOptions;
import com.dfsek.tectonic.api.TypeRegistry;
import com.dfsek.tectonic.api.config.Configuration;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;
import com.dfsek.tectonic.api.loader.AbstractConfigLoader;
import com.dfsek.tectonic.api.loader.ConfigLoader;
import com.dfsek.tectonic.api.loader.type.TypeLoader;
import com.dfsek.tectonic.yaml.YamlConfiguration;
import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.addon.BaseAddon;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.config.ConfigType;
import com.dfsek.terra.api.config.Loader;
import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.event.events.config.ConfigurationDiscoveryEvent;
import com.dfsek.terra.api.event.events.config.ConfigurationLoadEvent;
import com.dfsek.terra.api.event.events.config.pack.ConfigPackPostLoadEvent;
import com.dfsek.terra.api.event.events.config.pack.ConfigPackPreLoadEvent;
import com.dfsek.terra.api.event.events.config.type.ConfigTypePostLoadEvent;
import com.dfsek.terra.api.properties.Context;
import com.dfsek.terra.api.registry.CheckedRegistry;
import com.dfsek.terra.api.registry.OpenRegistry;
import com.dfsek.terra.api.registry.key.RegistryKey;
import com.dfsek.terra.api.tectonic.ShortcutLoader;
import com.dfsek.terra.api.util.generic.Construct;
import com.dfsek.terra.api.util.generic.pair.Pair;
import com.dfsek.terra.api.util.reflection.ReflectionUtil;
import com.dfsek.terra.api.util.reflection.TypeKey;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.chunk.generation.stage.GenerationStage;
import com.dfsek.terra.api.world.chunk.generation.util.provider.ChunkGeneratorProvider;
import com.dfsek.terra.config.fileloaders.FolderLoader;
import com.dfsek.terra.config.fileloaders.ZIPLoader;
import com.dfsek.terra.config.loaders.GenericTemplateSupplierLoader;
import com.dfsek.terra.config.loaders.config.BufferedImageLoader;
import com.dfsek.terra.config.preprocessor.MetaListLikePreprocessor;
import com.dfsek.terra.config.preprocessor.MetaMapPreprocessor;
import com.dfsek.terra.config.preprocessor.MetaNumberPreprocessor;
import com.dfsek.terra.config.preprocessor.MetaStringPreprocessor;
import com.dfsek.terra.config.preprocessor.MetaValuePreprocessor;
import com.dfsek.terra.config.prototype.ProtoConfig;
import com.dfsek.terra.lib.google.common.collect.ListMultimap;
import com.dfsek.terra.lib.google.common.collect.Multimap;
import com.dfsek.terra.lib.google.common.collect.Multimaps;
import com.dfsek.terra.registry.CheckedRegistryImpl;
import com.dfsek.terra.registry.OpenRegistryImpl;
import com.dfsek.terra.registry.ShortcutHolder;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigPackImpl implements ConfigPack {
   public static final TypeKey<ConfigType<?, ?>> CONFIG_TYPE_TYPE_KEY = new TypeKey<ConfigType<?, ?>>() {
   };
   private static final Logger logger = LoggerFactory.getLogger(ConfigPackImpl.class);
   private final Context context;
   private final ConfigPackTemplate template;
   private final AbstractConfigLoader abstractConfigLoader;
   private final ConfigLoader selfLoader;
   private final Platform platform;
   private final Loader loader;
   private final Map<BaseAddon, VersionRange> addons;
   private final BiomeProvider seededBiomeProvider;
   private final Map<Type, CheckedRegistryImpl<?>> registryMap;
   private final Map<Type, ShortcutHolder<?>> shortcuts;
   private final OpenRegistry<ConfigType<?, ?>> configTypeRegistry;
   private final TreeMap<Integer, List<Pair<RegistryKey, ConfigType<?, ?>>>> configTypes;
   private final RegistryKey key;
   private final ParseOptions parseOptions;

   public ConfigPackImpl(File folder, Platform platform) {
      this(new FolderLoader(folder.toPath()), (Configuration)Construct.construct(() -> {
         try {
            return new YamlConfiguration(new FileInputStream(new File(folder, "pack.yml")), "pack.yml");
         } catch (FileNotFoundException var2) {
            throw new UncheckedIOException("No pack.yml file found in " + folder.getAbsolutePath(), var2);
         }
      }), platform);
   }

   public ConfigPackImpl(ZipFile file, Platform platform) {
      this(new ZIPLoader(file), (Configuration)Construct.construct(() -> {
         ZipEntry pack = null;
         Enumeration entries = file.entries();

         while(entries.hasMoreElements()) {
            ZipEntry entry = (ZipEntry)entries.nextElement();
            if (entry.getName().equals("pack.yml")) {
               pack = entry;
            }
         }

         if (pack == null) {
            throw new IllegalArgumentException("No pack.yml file found in " + file.getName());
         } else {
            try {
               return new YamlConfiguration(file.getInputStream(pack), "pack.yml");
            } catch (IOException var4) {
               throw new UncheckedIOException("Unable to load pack.yml from ZIP file", var4);
            }
         }
      }), platform);
   }

   private ConfigPackImpl(Loader loader, Configuration packManifest, Platform platform) {
      this.context = new Context();
      this.template = new ConfigPackTemplate();
      this.abstractConfigLoader = new AbstractConfigLoader();
      this.selfLoader = new ConfigLoader();
      this.registryMap = new HashMap();
      this.shortcuts = new HashMap();
      this.configTypes = new TreeMap();
      long start = System.nanoTime();
      this.loader = loader;
      this.platform = platform;
      this.configTypeRegistry = this.createConfigRegistry();
      this.register(this.selfLoader);
      platform.register(this.selfLoader);
      this.register(this.abstractConfigLoader);
      platform.register(this.abstractConfigLoader);
      ConfigPackAddonsTemplate addonsTemplate = new ConfigPackAddonsTemplate();
      this.selfLoader.load(addonsTemplate, packManifest);
      this.addons = addonsTemplate.getAddons();
      ConfigPackExpressionOptionsTemplate expressionOptionsTemplate = new ConfigPackExpressionOptionsTemplate();
      this.selfLoader.load(expressionOptionsTemplate, packManifest);
      this.parseOptions = expressionOptionsTemplate.getParseOptions();
      Map<String, Configuration> configurations = this.discoverConfigurations();
      this.registerMeta(configurations);
      platform.getEventManager().callEvent(new ConfigPackPreLoadEvent(this, (template) -> {
         this.selfLoader.load(template, packManifest);
      }));
      this.selfLoader.load(this.template, packManifest);
      String namespace;
      String id;
      if (this.template.getID().contains(":")) {
         namespace = this.template.getID().substring(0, this.template.getID().indexOf(":"));
         id = this.template.getID().substring(this.template.getID().indexOf(":") + 1);
      } else {
         id = this.template.getID();
         namespace = this.template.getID();
      }

      this.key = RegistryKey.of(namespace, id);
      logger.info("Loading config pack \"{}:{}\"", id, namespace);
      this.configTypes.values().forEach((list) -> {
         list.forEach((pair) -> {
            this.configTypeRegistry.register((RegistryKey)pair.getLeft(), (ConfigType)pair.getRight());
         });
      });
      ListMultimap<ConfigType<?, ?>, Configuration> multimap = (ListMultimap)configurations.values().parallelStream().collect(() -> {
         return Multimaps.newListMultimap(new ConcurrentHashMap(), ArrayList::new);
      }, (configs, configuration) -> {
         if (configuration.contains("type")) {
            ProtoConfig config = new ProtoConfig();
            this.selfLoader.load(config, configuration);
            configs.put(config.getType(), configuration);
         }

      }, Multimap::putAll);
      this.configTypeRegistry.forEach((configType) -> {
         CheckedRegistry registry = this.getCheckedRegistry(configType.getTypeKey());
         ((Stream)this.abstractConfigLoader.loadConfigs(multimap.get(configType)).stream().parallel()).map((configuration) -> {
            logger.debug("Loading abstract config {}", configuration.getID());
            Object loaded = configType.getFactory().build(this.selfLoader.load(configType.getTemplate(this, platform), configuration), platform);
            platform.getEventManager().callEvent(new ConfigurationLoadEvent(this, configuration, (template) -> {
               this.selfLoader.load(template, configuration);
            }, configType, loaded));
            return Pair.of(configuration.getID(), loaded);
         }).toList().forEach((pair) -> {
            registry.register(this.key((String)pair.getLeft()), pair.getRight());
         });
         platform.getEventManager().callEvent(new ConfigTypePostLoadEvent(configType, registry, this));
      });
      platform.getEventManager().callEvent(new ConfigPackPostLoadEvent(this, (template) -> {
         this.selfLoader.load(template, packManifest);
      }));
      logger.info("Loaded config pack \"{}:{}\" v{} by {} in {}ms.", new Object[]{namespace, id, this.getVersion().getFormatted(), this.template.getAuthor(), (double)(System.nanoTime() - start) / 1000000.0D});
      ConfigPackPostTemplate packPostTemplate = new ConfigPackPostTemplate();
      this.selfLoader.load(packPostTemplate, packManifest);
      this.seededBiomeProvider = (BiomeProvider)(this.template.getBiomeCache() ? packPostTemplate.getProviderBuilder().caching(platform) : packPostTemplate.getProviderBuilder());
      this.checkDeadEntries();
   }

   private Map<String, Configuration> discoverConfigurations() {
      Map<String, Configuration> configurations = new HashMap();
      this.platform.getEventManager().callEvent(new ConfigurationDiscoveryEvent(this, this.loader, (s, c) -> {
         configurations.put(s.replace("\\", "/"), c);
      }));
      return configurations;
   }

   private void registerMeta(Map<String, Configuration> configurations) {
      MetaStringPreprocessor stringPreprocessor = new MetaStringPreprocessor(configurations);
      this.selfLoader.registerPreprocessor(Meta.class, stringPreprocessor);
      this.abstractConfigLoader.registerPreprocessor(Meta.class, stringPreprocessor);
      MetaListLikePreprocessor listPreprocessor = new MetaListLikePreprocessor(configurations);
      this.selfLoader.registerPreprocessor(Meta.class, listPreprocessor);
      this.abstractConfigLoader.registerPreprocessor(Meta.class, listPreprocessor);
      MetaMapPreprocessor mapPreprocessor = new MetaMapPreprocessor(configurations);
      this.selfLoader.registerPreprocessor(Meta.class, mapPreprocessor);
      this.abstractConfigLoader.registerPreprocessor(Meta.class, mapPreprocessor);
      MetaValuePreprocessor valuePreprocessor = new MetaValuePreprocessor(configurations);
      this.selfLoader.registerPreprocessor(Meta.class, valuePreprocessor);
      this.abstractConfigLoader.registerPreprocessor(Meta.class, valuePreprocessor);
      MetaNumberPreprocessor numberPreprocessor = new MetaNumberPreprocessor(configurations, this.parseOptions);
      this.selfLoader.registerPreprocessor(Meta.class, numberPreprocessor);
      this.abstractConfigLoader.registerPreprocessor(Meta.class, numberPreprocessor);
   }

   public <T> ConfigPackImpl applyLoader(Type type, TypeLoader<T> loader) {
      this.abstractConfigLoader.registerLoader(type, loader);
      this.selfLoader.registerLoader(type, loader);
      return this;
   }

   public <T> ConfigPackImpl applyLoader(Type type, Supplier<ObjectTemplate<T>> loader) {
      this.abstractConfigLoader.registerLoader(type, loader);
      this.selfLoader.registerLoader(type, loader);
      return this;
   }

   public void register(TypeRegistry registry) {
      registry.registerLoader(ConfigType.class, this.configTypeRegistry).registerLoader(BufferedImage.class, new BufferedImageLoader(this.loader, this));
      Map var10000 = this.registryMap;
      Objects.requireNonNull(registry);
      var10000.forEach(registry::registerLoader);
      var10000 = this.shortcuts;
      Objects.requireNonNull(registry);
      var10000.forEach(registry::registerLoader);
   }

   public ConfigPack registerConfigType(ConfigType<?, ?> type, RegistryKey key, int priority) {
      Set<RegistryKey> contained = new HashSet();
      this.configTypes.forEach((p, configs) -> {
         configs.forEach((pair) -> {
            if (contained.contains(pair.getLeft())) {
               throw new IllegalArgumentException("Duplicate config key: " + String.valueOf(key));
            } else {
               contained.add(key);
            }
         });
      });
      ((List)this.configTypes.computeIfAbsent(priority, (p) -> {
         return new ArrayList();
      })).add(Pair.of(key, type));
      return this;
   }

   public Map<BaseAddon, VersionRange> addons() {
      return this.addons;
   }

   public BiomeProvider getBiomeProvider() {
      return this.seededBiomeProvider;
   }

   public <T> CheckedRegistry<T> getOrCreateRegistry(TypeKey<T> typeKey) {
      return (CheckedRegistry)this.registryMap.computeIfAbsent(typeKey.getType(), (c) -> {
         OpenRegistry<T> registry = new OpenRegistryImpl(typeKey);
         this.selfLoader.registerLoader(c, registry);
         this.abstractConfigLoader.registerLoader(c, registry);
         logger.debug("Registered loader for registry of class {}", ReflectionUtil.typeToString(c));
         Type base = typeKey.getType();
         if (base instanceof ParameterizedType) {
            ParameterizedType param = (ParameterizedType)base;
            base = param.getRawType();
            if (base instanceof Class && Supplier.class.isAssignableFrom((Class)base)) {
               Type supplied = param.getActualTypeArguments()[0];
               if (supplied instanceof ParameterizedType) {
                  ParameterizedType suppliedParam = (ParameterizedType)supplied;
                  Type suppliedBase = suppliedParam.getRawType();
                  if (suppliedBase instanceof Class && ObjectTemplate.class.isAssignableFrom((Class)suppliedBase)) {
                     Type templateType = suppliedParam.getActualTypeArguments()[0];
                     GenericTemplateSupplierLoader<?> loader = new GenericTemplateSupplierLoader(registry);
                     this.selfLoader.registerLoader(templateType, loader);
                     this.abstractConfigLoader.registerLoader(templateType, loader);
                     logger.debug("Registered template loader for registry of class {}", ReflectionUtil.typeToString(templateType));
                  }
               }
            }
         }

         return new CheckedRegistryImpl(registry);
      });
   }

   public List<GenerationStage> getStages() {
      return this.template.getStages();
   }

   public Loader getLoader() {
      return this.loader;
   }

   public String getAuthor() {
      return this.template.getAuthor();
   }

   public Version getVersion() {
      return this.template.getVersion();
   }

   public ParseOptions getExpressionParseOptions() {
      return this.parseOptions;
   }

   public <T> ConfigPack registerShortcut(TypeKey<T> clazz, String shortcut, ShortcutLoader<T> loader) {
      ShortcutHolder<?> holder = ((ShortcutHolder)this.shortcuts.computeIfAbsent(clazz.getType(), (c) -> {
         return new ShortcutHolder(this.getOrCreateRegistry(clazz));
      })).register(shortcut, loader);
      this.selfLoader.registerLoader(clazz.getType(), holder);
      this.abstractConfigLoader.registerLoader(clazz.getType(), holder);
      return this;
   }

   public ChunkGeneratorProvider getGeneratorProvider() {
      return this.template.getGeneratorProvider();
   }

   private OpenRegistry<ConfigType<?, ?>> createConfigRegistry() {
      return new OpenRegistryImpl<ConfigType<?, ?>>(new LinkedHashMap(), CONFIG_TYPE_TYPE_KEY) {
         public boolean register(@NotNull RegistryKey key, @NotNull ConfigType<?, ?> value) {
            if (!ConfigPackImpl.this.registryMap.containsKey(value.getTypeKey().getType())) {
               OpenRegistry<?> openRegistry = new OpenRegistryImpl(value.getTypeKey());
               ConfigPackImpl.this.selfLoader.registerLoader(value.getTypeKey().getType(), openRegistry);
               ConfigPackImpl.this.abstractConfigLoader.registerLoader(value.getTypeKey().getType(), openRegistry);
               ConfigPackImpl.this.registryMap.put(value.getTypeKey().getType(), new CheckedRegistryImpl(openRegistry));
            }

            return super.register(key, (Object)value);
         }
      };
   }

   private void checkDeadEntries() {
      this.registryMap.forEach((clazz, pair) -> {
         ((OpenRegistryImpl)pair.getRegistry()).getDeadEntries().forEach((id, value) -> {
            logger.debug("Dead entry in '{}' registry: '{}'", ReflectionUtil.typeToString(clazz), id);
         });
      });
   }

   public ConfigPackTemplate getTemplate() {
      return this.template;
   }

   public <T> CheckedRegistry<T> getRegistry(Type type) {
      return (CheckedRegistry)this.registryMap.get(type);
   }

   public <T> CheckedRegistry<T> getCheckedRegistry(Type type) throws IllegalStateException {
      return (CheckedRegistry)this.registryMap.get(type);
   }

   public RegistryKey getRegistryKey() {
      return this.key;
   }

   public Context getContext() {
      return this.context;
   }
}
