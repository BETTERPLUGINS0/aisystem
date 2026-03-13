package com.volmit.iris.core.loader;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.Excluder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.volmit.iris.Iris;
import com.volmit.iris.core.scripting.environment.PackEnvironment;
import com.volmit.iris.engine.data.cache.AtomicCache;
import com.volmit.iris.engine.framework.Engine;
import com.volmit.iris.engine.object.IrisBiome;
import com.volmit.iris.engine.object.IrisBlockData;
import com.volmit.iris.engine.object.IrisCave;
import com.volmit.iris.engine.object.IrisDimension;
import com.volmit.iris.engine.object.IrisEntity;
import com.volmit.iris.engine.object.IrisExpression;
import com.volmit.iris.engine.object.IrisGenerator;
import com.volmit.iris.engine.object.IrisImage;
import com.volmit.iris.engine.object.IrisJigsawPiece;
import com.volmit.iris.engine.object.IrisJigsawPool;
import com.volmit.iris.engine.object.IrisJigsawStructure;
import com.volmit.iris.engine.object.IrisLootTable;
import com.volmit.iris.engine.object.IrisMarker;
import com.volmit.iris.engine.object.IrisMod;
import com.volmit.iris.engine.object.IrisObject;
import com.volmit.iris.engine.object.IrisRavine;
import com.volmit.iris.engine.object.IrisRegion;
import com.volmit.iris.engine.object.IrisScript;
import com.volmit.iris.engine.object.IrisSpawner;
import com.volmit.iris.engine.object.annotations.Snippet;
import com.volmit.iris.engine.object.matter.IrisMatterObject;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.collection.KMap;
import com.volmit.iris.util.context.IrisContext;
import com.volmit.iris.util.format.C;
import com.volmit.iris.util.mantle.flag.MantleFlag;
import com.volmit.iris.util.mantle.flag.MantleFlagAdapter;
import com.volmit.iris.util.math.RNG;
import com.volmit.iris.util.parallel.BurstExecutor;
import com.volmit.iris.util.parallel.MultiBurst;
import com.volmit.iris.util.reflect.KeyedType;
import com.volmit.iris.util.scheduling.ChronoLatch;
import com.volmit.iris.util.scheduling.J;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import lombok.Generated;
import org.jetbrains.annotations.Nullable;

public class IrisData implements ExclusionStrategy, TypeAdapterFactory {
   private static final KMap<File, IrisData> dataLoaders = new KMap();
   private final File dataFolder;
   private final int id;
   private boolean closed = false;
   private PackEnvironment environment;
   private ResourceLoader<IrisBiome> biomeLoader;
   private ResourceLoader<IrisLootTable> lootLoader;
   private ResourceLoader<IrisRegion> regionLoader;
   private ResourceLoader<IrisDimension> dimensionLoader;
   private ResourceLoader<IrisGenerator> generatorLoader;
   private ResourceLoader<IrisJigsawPiece> jigsawPieceLoader;
   private ResourceLoader<IrisJigsawPool> jigsawPoolLoader;
   private ResourceLoader<IrisJigsawStructure> jigsawStructureLoader;
   private ResourceLoader<IrisEntity> entityLoader;
   private ResourceLoader<IrisMarker> markerLoader;
   private ResourceLoader<IrisSpawner> spawnerLoader;
   private ResourceLoader<IrisMod> modLoader;
   private ResourceLoader<IrisBlockData> blockLoader;
   private ResourceLoader<IrisExpression> expressionLoader;
   private ResourceLoader<IrisObject> objectLoader;
   private ResourceLoader<IrisMatterObject> matterLoader;
   private ResourceLoader<IrisImage> imageLoader;
   private ResourceLoader<IrisScript> scriptLoader;
   private ResourceLoader<IrisCave> caveLoader;
   private ResourceLoader<IrisRavine> ravineLoader;
   private ResourceLoader<IrisMatterObject> matterObjectLoader;
   private KMap<String, KList<String>> possibleSnippets;
   private Gson gson;
   private Gson snippetLoader;
   private GsonBuilder builder;
   private KMap<Class<? extends IrisRegistrant>, ResourceLoader<? extends IrisRegistrant>> loaders = new KMap();
   private Engine engine = null;

   private IrisData(File dataFolder) {
      this.dataFolder = var1;
      this.id = RNG.r.imax();
      this.hotloaded();
   }

   public static IrisData get(File dataFolder) {
      return (IrisData)dataLoaders.computeIfAbsent(var0, IrisData::new);
   }

   public static Optional<IrisData> getLoaded(File dataFolder) {
      return Optional.ofNullable((IrisData)dataLoaders.get(var0));
   }

   public static void dereference() {
      dataLoaders.values().forEach(IrisData::cleanupEngine);
   }

   public static int cacheSize() {
      int var0 = 0;
      Iterator var1 = dataLoaders.values().iterator();

      while(var1.hasNext()) {
         IrisData var2 = (IrisData)var1.next();

         ResourceLoader var4;
         for(Iterator var3 = var2.getLoaders().values().iterator(); var3.hasNext(); var0 = (int)((long)var0 + var4.getLoadCache().getSize())) {
            var4 = (ResourceLoader)var3.next();
         }
      }

      return var0;
   }

   private static void printData(ResourceLoader<?> rl) {
      String var10000 = var0.getResourceTypeName();
      Iris.warn("  " + var10000 + " @ /" + var0.getFolderName() + ": Cache=" + var0.getLoadCache().getSize() + " Folders=" + var0.getFolders().size());
   }

   public static IrisObject loadAnyObject(String key, @Nullable IrisData nearest) {
      return (IrisObject)loadAny(IrisObject.class, var0, var1);
   }

   public static IrisMatterObject loadAnyMatter(String key, @Nullable IrisData nearest) {
      return (IrisMatterObject)loadAny(IrisMatterObject.class, var0, var1);
   }

   public static IrisBiome loadAnyBiome(String key, @Nullable IrisData nearest) {
      return (IrisBiome)loadAny(IrisBiome.class, var0, var1);
   }

   public static IrisExpression loadAnyExpression(String key, @Nullable IrisData nearest) {
      return (IrisExpression)loadAny(IrisExpression.class, var0, var1);
   }

   public static IrisMod loadAnyMod(String key, @Nullable IrisData nearest) {
      return (IrisMod)loadAny(IrisMod.class, var0, var1);
   }

   public static IrisJigsawPiece loadAnyJigsawPiece(String key, @Nullable IrisData nearest) {
      return (IrisJigsawPiece)loadAny(IrisJigsawPiece.class, var0, var1);
   }

   public static IrisJigsawPool loadAnyJigsawPool(String key, @Nullable IrisData nearest) {
      return (IrisJigsawPool)loadAny(IrisJigsawPool.class, var0, var1);
   }

   public static IrisEntity loadAnyEntity(String key, @Nullable IrisData nearest) {
      return (IrisEntity)loadAny(IrisEntity.class, var0, var1);
   }

   public static IrisLootTable loadAnyLootTable(String key, @Nullable IrisData nearest) {
      return (IrisLootTable)loadAny(IrisLootTable.class, var0, var1);
   }

   public static IrisBlockData loadAnyBlock(String key, @Nullable IrisData nearest) {
      return (IrisBlockData)loadAny(IrisBlockData.class, var0, var1);
   }

   public static IrisSpawner loadAnySpaner(String key, @Nullable IrisData nearest) {
      return (IrisSpawner)loadAny(IrisSpawner.class, var0, var1);
   }

   public static IrisScript loadAnyScript(String key, @Nullable IrisData nearest) {
      return (IrisScript)loadAny(IrisScript.class, var0, var1);
   }

   public static IrisRavine loadAnyRavine(String key, @Nullable IrisData nearest) {
      return (IrisRavine)loadAny(IrisRavine.class, var0, var1);
   }

   public static IrisRegion loadAnyRegion(String key, @Nullable IrisData nearest) {
      return (IrisRegion)loadAny(IrisRegion.class, var0, var1);
   }

   public static IrisMarker loadAnyMarker(String key, @Nullable IrisData nearest) {
      return (IrisMarker)loadAny(IrisMarker.class, var0, var1);
   }

   public static IrisCave loadAnyCave(String key, @Nullable IrisData nearest) {
      return (IrisCave)loadAny(IrisCave.class, var0, var1);
   }

   public static IrisImage loadAnyImage(String key, @Nullable IrisData nearest) {
      return (IrisImage)loadAny(IrisImage.class, var0, var1);
   }

   public static IrisDimension loadAnyDimension(String key, @Nullable IrisData nearest) {
      return (IrisDimension)loadAny(IrisDimension.class, var0, var1);
   }

   public static IrisJigsawStructure loadAnyJigsawStructure(String key, @Nullable IrisData nearest) {
      return (IrisJigsawStructure)loadAny(IrisJigsawStructure.class, var0, var1);
   }

   public static IrisGenerator loadAnyGenerator(String key, @Nullable IrisData nearest) {
      return (IrisGenerator)loadAny(IrisGenerator.class, var0, var1);
   }

   public static <T extends IrisRegistrant> T loadAny(Class<T> type, String key, @Nullable IrisData nearest) {
      try {
         if (var2 != null) {
            IrisRegistrant var3 = var2.load(var0, var1, false);
            if (var3 != null) {
               return var3;
            }
         }

         File[] var10 = (File[])Objects.requireNonNull(Iris.instance.getDataFolder(new String[]{"packs"}).listFiles());
         int var4 = var10.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            File var6 = var10[var5];
            if (var6.isDirectory()) {
               IrisData var7 = get(var6);
               if (var7 != var2) {
                  IrisRegistrant var8 = var7.load(var0, var1, false);
                  if (var8 != null) {
                     return var8;
                  }
               }
            }
         }
      } catch (Throwable var9) {
         Iris.reportError(var9);
         var9.printStackTrace();
      }

      return null;
   }

   public <T extends IrisRegistrant> T load(Class<T> type, String key, boolean warn) {
      ResourceLoader var4 = this.getLoader(var1);
      return var4 == null ? null : var4.load(var2, var3);
   }

   public <T extends IrisRegistrant> ResourceLoader<T> getLoader(Class<T> type) {
      return (ResourceLoader)this.loaders.get(var1);
   }

   public ResourceLoader<?> getTypedLoaderFor(File f) {
      String[] var2 = var1.getPath().split("\\Q" + File.separator + "\\E");
      String[] var3 = var2;
      int var4 = var2.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         String var6 = var3[var5];
         Iterator var7 = this.loaders.values().iterator();

         while(var7.hasNext()) {
            ResourceLoader var8 = (ResourceLoader)var7.next();
            if (var8.getFolderName().equals(var6)) {
               return var8;
            }
         }
      }

      return null;
   }

   public void cleanupEngine() {
      if (this.engine != null && this.engine.isClosed()) {
         this.engine = null;
         int var10000 = this.getId();
         Iris.debug("Dereferenced Data<Engine> " + var10000 + " " + String.valueOf(this.getDataFolder()));
      }

   }

   public void preprocessObject(IrisRegistrant t) {
      try {
         IrisContext var2 = IrisContext.get();
         Engine var3 = this.engine;
         if (var3 == null && var2 != null && var2.getEngine() != null) {
            var3 = var2.getEngine();
         }

         if (var3 == null && var1.getPreprocessors().isNotEmpty()) {
            Iris.error("Failed to preprocess object " + var1.getLoadKey() + " because there is no engine context here. (See stack below)");

            try {
               throw new RuntimeException();
            } catch (Throwable var11) {
               var11.printStackTrace();
            }
         }

         if (var3 == null) {
            return;
         }

         KList var4 = var3.getDimension().getPreProcessors(var1.getFolderName());
         KList var5 = var1.getPreprocessors();
         if (var4 != null && var4.isNotEmpty() || var5.isNotEmpty()) {
            synchronized(this) {
               String var10000;
               Iterator var7;
               String var8;
               if (var4 != null) {
                  var7 = var4.iterator();

                  while(var7.hasNext()) {
                     var8 = (String)var7.next();
                     var3.getExecution().preprocessObject(var8, var1);
                     var10000 = String.valueOf(C.GREEN);
                     Iris.debug("Loader<" + var10000 + var1.getTypeName() + String.valueOf(C.LIGHT_PURPLE) + "> iprocess " + String.valueOf(C.YELLOW) + var1.getLoadKey() + String.valueOf(C.LIGHT_PURPLE) + " in <rainbow>" + var8);
                  }
               }

               var7 = var5.iterator();

               while(var7.hasNext()) {
                  var8 = (String)var7.next();
                  var3.getExecution().preprocessObject(var8, var1);
                  var10000 = String.valueOf(C.GREEN);
                  Iris.debug("Loader<" + var10000 + var1.getTypeName() + String.valueOf(C.LIGHT_PURPLE) + "> iprocess " + String.valueOf(C.YELLOW) + var1.getLoadKey() + String.valueOf(C.LIGHT_PURPLE) + " in <rainbow>" + var8);
               }
            }
         }
      } catch (Throwable var12) {
         Iris.error("Failed to preprocess object!");
         var12.printStackTrace();
      }

   }

   public void close() {
      this.closed = true;
      this.dump();
      dataLoaders.remove(this.dataFolder);
   }

   public IrisData copy() {
      return get(this.dataFolder);
   }

   private <T extends IrisRegistrant> ResourceLoader<T> registerLoader(Class<T> registrant) {
      try {
         IrisRegistrant var2 = (IrisRegistrant)var1.getConstructor().newInstance();
         Object var3 = null;
         if (var1.equals(IrisObject.class)) {
            var3 = new ObjectResourceLoader(this.dataFolder, this, var2.getFolderName(), var2.getTypeName());
         } else if (var1.equals(IrisMatterObject.class)) {
            var3 = new MatterObjectResourceLoader(this.dataFolder, this, var2.getFolderName(), var2.getTypeName());
         } else if (var1.equals(IrisScript.class)) {
            var3 = new ScriptResourceLoader(this.dataFolder, this, var2.getFolderName(), var2.getTypeName());
         } else if (var1.equals(IrisImage.class)) {
            var3 = new ImageResourceLoader(this.dataFolder, this, var2.getFolderName(), var2.getTypeName());
         } else {
            J.attempt(() -> {
               ((IrisRegistrant)var1.getConstructor().newInstance()).registerTypeAdapters(this.builder);
            });
            var3 = new ResourceLoader(this.dataFolder, this, var2.getFolderName(), var2.getTypeName(), var1);
         }

         this.loaders.put(var1, var3);
         return (ResourceLoader)var3;
      } catch (Throwable var4) {
         Iris.reportError(var4);
         var4.printStackTrace();
         Iris.error("Failed to create loader! " + var1.getCanonicalName());
         return null;
      }
   }

   public synchronized void hotloaded() {
      this.closed = false;
      this.possibleSnippets = new KMap();
      this.builder = (new GsonBuilder()).addDeserializationExclusionStrategy(this).addSerializationExclusionStrategy(this).setLenient().registerTypeAdapterFactory(this).registerTypeAdapter(MantleFlag.class, new MantleFlagAdapter()).setPrettyPrinting();
      this.loaders.clear();
      File var1 = this.dataFolder;
      var1.mkdirs();
      this.lootLoader = this.registerLoader(IrisLootTable.class);
      this.spawnerLoader = this.registerLoader(IrisSpawner.class);
      this.entityLoader = this.registerLoader(IrisEntity.class);
      this.regionLoader = this.registerLoader(IrisRegion.class);
      this.biomeLoader = this.registerLoader(IrisBiome.class);
      this.modLoader = this.registerLoader(IrisMod.class);
      this.dimensionLoader = this.registerLoader(IrisDimension.class);
      this.jigsawPoolLoader = this.registerLoader(IrisJigsawPool.class);
      this.jigsawStructureLoader = this.registerLoader(IrisJigsawStructure.class);
      this.jigsawPieceLoader = this.registerLoader(IrisJigsawPiece.class);
      this.generatorLoader = this.registerLoader(IrisGenerator.class);
      this.caveLoader = this.registerLoader(IrisCave.class);
      this.markerLoader = this.registerLoader(IrisMarker.class);
      this.ravineLoader = this.registerLoader(IrisRavine.class);
      this.blockLoader = this.registerLoader(IrisBlockData.class);
      this.expressionLoader = this.registerLoader(IrisExpression.class);
      this.objectLoader = this.registerLoader(IrisObject.class);
      this.imageLoader = this.registerLoader(IrisImage.class);
      this.scriptLoader = this.registerLoader(IrisScript.class);
      this.matterObjectLoader = this.registerLoader(IrisMatterObject.class);
      this.environment = PackEnvironment.create(this);
      this.builder.registerTypeAdapterFactory(KeyedType::createTypeAdapter);
      this.gson = this.builder.create();
      Stream var10000 = this.dimensionLoader.streamAll().map(IrisDimension::getDataScripts).flatMap(Collection::stream);
      PackEnvironment var10001 = this.environment;
      Objects.requireNonNull(var10001);
      var10000.forEach(var10001::execute);
      if (this.engine != null) {
         this.engine.hotload();
      }

   }

   public void dump() {
      Iterator var1 = this.loaders.values().iterator();

      while(var1.hasNext()) {
         ResourceLoader var2 = (ResourceLoader)var1.next();
         var2.clearCache();
      }

   }

   public void clearLists() {
      Iterator var1 = this.loaders.values().iterator();

      while(var1.hasNext()) {
         ResourceLoader var2 = (ResourceLoader)var1.next();
         var2.clearList();
      }

      this.possibleSnippets.clear();
   }

   public Set<Class<?>> resolveSnippets() {
      HashSet var1 = new HashSet();
      HashSet var2 = new HashSet();
      Excluder var3 = this.gson.excluder();
      LinkedList var4 = new LinkedList(this.loaders.keySet());

      while(true) {
         Class var5;
         do {
            do {
               if (var4.isEmpty()) {
                  return var1;
               }

               var5 = (Class)var4.poll();
            } while(var3.excludeClass(var5, false));
         } while(!var2.add(var5));

         if (var5.isAnnotationPresent(Snippet.class)) {
            var1.add(var5);
         }

         try {
            Field[] var6 = var5.getDeclaredFields();
            int var7 = var6.length;

            for(int var8 = 0; var8 < var7; ++var8) {
               Field var9 = var6[var8];
               if (!var3.excludeField(var9, false)) {
                  var4.add(var9.getType());
               }
            }
         } catch (Throwable var10) {
         }
      }
   }

   public String toLoadKey(File f) {
      if (var1.getPath().startsWith(this.getDataFolder().getPath())) {
         String[] var2 = var1.getPath().split("\\Q" + File.separator + "\\E");
         String[] var3 = this.getDataFolder().getPath().split("\\Q" + File.separator + "\\E");
         StringBuilder var4 = new StringBuilder();
         boolean var5 = true;

         for(int var6 = 0; var6 < var2.length; ++var6) {
            if (var6 >= var3.length) {
               if (var5) {
                  var5 = false;
               } else {
                  var4.append("/").append(var2[var6]);
               }
            }
         }

         return var4.substring(1).split("\\Q.\\E")[0];
      } else {
         Iris.error("Forign file from loader " + var1.getPath() + " (loader realm: " + this.getDataFolder().getPath() + ")");
         Iris.error("Failed to load " + var1.getPath() + " (loader realm: " + this.getDataFolder().getPath() + ")");
         return null;
      }
   }

   public boolean shouldSkipField(FieldAttributes f) {
      return false;
   }

   public boolean shouldSkipClass(Class<?> c) {
      return var1.equals(AtomicCache.class) ? true : var1.equals(ChronoLatch.class);
   }

   public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
      if (!var2.getRawType().isAnnotationPresent(Snippet.class)) {
         return null;
      } else {
         String var3 = ((Snippet)var2.getRawType().getDeclaredAnnotation(Snippet.class)).value();
         final String var4 = "snippet/" + var3 + "/";
         return new TypeAdapter<T>() {
            public void write(JsonWriter jsonWriter, T t) {
               var1.getDelegateAdapter(IrisData.this, var2).write(var1x, var2x);
            }

            public T read(JsonReader reader) {
               TypeAdapter var2x = var1.getDelegateAdapter(IrisData.this, var2);
               if (!var1x.peek().equals(JsonToken.STRING)) {
                  try {
                     return var2x.read(var1x);
                  } catch (Throwable var11) {
                     Iris.error("Failed to read " + var2.getRawType().getCanonicalName() + "... faking objects a little to load the file at least.");
                     Iris.reportError(var11);

                     try {
                        return var2.getRawType().getConstructor().newInstance();
                     } catch (Throwable var8) {
                        return null;
                     }
                  }
               } else {
                  String var3 = var1x.nextString();
                  if (!var3.startsWith("snippet/")) {
                     return null;
                  } else {
                     if (!var3.startsWith(var4)) {
                        String var10000 = var4;
                        var3 = var10000 + var3.substring(8);
                     }

                     File var4x = new File(IrisData.this.getDataFolder(), var3 + ".json");
                     if (var4x.exists()) {
                        try {
                           JsonReader var5 = new JsonReader(new FileReader(var4x));

                           Object var6;
                           try {
                              var6 = var2x.read(var5);
                           } catch (Throwable var10) {
                              try {
                                 var5.close();
                              } catch (Throwable var9) {
                                 var10.addSuppressed(var9);
                              }

                              throw var10;
                           }

                           var5.close();
                           return var6;
                        } catch (Throwable var12) {
                           Iris.error("Couldn't read snippet " + var3 + " in " + var1x.getPath() + " (" + var12.getMessage() + ")");
                        }
                     } else {
                        Iris.error("Couldn't find snippet " + var3 + " in " + var1x.getPath());
                     }

                     return null;
                  }
               }
            }
         };
      }
   }

   public KList<String> getPossibleSnippets(String f) {
      return (KList)this.possibleSnippets.computeIfAbsent(var1, (var2) -> {
         KList var3 = new KList();
         File var4 = new File(this.getDataFolder(), "snippet/" + var1);
         if (!var4.exists()) {
            return var3;
         } else {
            String var5 = var4.getAbsolutePath();

            try {
               Stream var6 = Files.walk(var4.toPath());

               try {
                  var6.filter((var0) -> {
                     return Files.isRegularFile(var0, new LinkOption[0]);
                  }).map(Path::toAbsolutePath).map(Path::toString).filter((var0) -> {
                     return var0.endsWith(".json");
                  }).map((var1x) -> {
                     return var1x.substring(var5.length() + 1);
                  }).map((var0) -> {
                     return var0.replace("\\", "/");
                  }).map((var0) -> {
                     return var0.split("\\Q.\\E")[0];
                  }).forEach((var1x) -> {
                     var3.add((Object)("snippet/" + var1x));
                  });
               } catch (Throwable var10) {
                  if (var6 != null) {
                     try {
                        var6.close();
                     } catch (Throwable var9) {
                        var10.addSuppressed(var9);
                     }
                  }

                  throw var10;
               }

               if (var6 != null) {
                  var6.close();
               }
            } catch (Throwable var11) {
               var11.printStackTrace();
            }

            return var3;
         }
      });
   }

   public boolean isClosed() {
      return this.closed;
   }

   public void savePrefetch(Engine engine) {
      BurstExecutor var2 = MultiBurst.ioBurst.burst(this.loaders.size());
      Iterator var3 = this.loaders.values().iterator();

      while(var3.hasNext()) {
         ResourceLoader var4 = (ResourceLoader)var3.next();
         var2.queue(() -> {
            try {
               var4.saveFirstAccess(var1);
            } catch (IOException var3) {
               throw new RuntimeException(var3);
            }
         });
      }

      var2.complete();
      Iris.info("Saved Prefetch Cache to speed up future world startups");
   }

   public void loadPrefetch(Engine engine) {
      BurstExecutor var2 = MultiBurst.ioBurst.burst(this.loaders.size());
      Iterator var3 = this.loaders.values().iterator();

      while(var3.hasNext()) {
         ResourceLoader var4 = (ResourceLoader)var3.next();
         var2.queue(() -> {
            try {
               var4.loadFirstAccess(var1);
            } catch (IOException var3) {
               throw new RuntimeException(var3);
            }
         });
      }

      var2.complete();
      Iris.info("Loaded Prefetch Cache to reduce generation disk use.");
   }

   @Generated
   public File getDataFolder() {
      return this.dataFolder;
   }

   @Generated
   public int getId() {
      return this.id;
   }

   @Generated
   public PackEnvironment getEnvironment() {
      return this.environment;
   }

   @Generated
   public ResourceLoader<IrisBiome> getBiomeLoader() {
      return this.biomeLoader;
   }

   @Generated
   public ResourceLoader<IrisLootTable> getLootLoader() {
      return this.lootLoader;
   }

   @Generated
   public ResourceLoader<IrisRegion> getRegionLoader() {
      return this.regionLoader;
   }

   @Generated
   public ResourceLoader<IrisDimension> getDimensionLoader() {
      return this.dimensionLoader;
   }

   @Generated
   public ResourceLoader<IrisGenerator> getGeneratorLoader() {
      return this.generatorLoader;
   }

   @Generated
   public ResourceLoader<IrisJigsawPiece> getJigsawPieceLoader() {
      return this.jigsawPieceLoader;
   }

   @Generated
   public ResourceLoader<IrisJigsawPool> getJigsawPoolLoader() {
      return this.jigsawPoolLoader;
   }

   @Generated
   public ResourceLoader<IrisJigsawStructure> getJigsawStructureLoader() {
      return this.jigsawStructureLoader;
   }

   @Generated
   public ResourceLoader<IrisEntity> getEntityLoader() {
      return this.entityLoader;
   }

   @Generated
   public ResourceLoader<IrisMarker> getMarkerLoader() {
      return this.markerLoader;
   }

   @Generated
   public ResourceLoader<IrisSpawner> getSpawnerLoader() {
      return this.spawnerLoader;
   }

   @Generated
   public ResourceLoader<IrisMod> getModLoader() {
      return this.modLoader;
   }

   @Generated
   public ResourceLoader<IrisBlockData> getBlockLoader() {
      return this.blockLoader;
   }

   @Generated
   public ResourceLoader<IrisExpression> getExpressionLoader() {
      return this.expressionLoader;
   }

   @Generated
   public ResourceLoader<IrisObject> getObjectLoader() {
      return this.objectLoader;
   }

   @Generated
   public ResourceLoader<IrisMatterObject> getMatterLoader() {
      return this.matterLoader;
   }

   @Generated
   public ResourceLoader<IrisImage> getImageLoader() {
      return this.imageLoader;
   }

   @Generated
   public ResourceLoader<IrisScript> getScriptLoader() {
      return this.scriptLoader;
   }

   @Generated
   public ResourceLoader<IrisCave> getCaveLoader() {
      return this.caveLoader;
   }

   @Generated
   public ResourceLoader<IrisRavine> getRavineLoader() {
      return this.ravineLoader;
   }

   @Generated
   public ResourceLoader<IrisMatterObject> getMatterObjectLoader() {
      return this.matterObjectLoader;
   }

   @Generated
   public KMap<String, KList<String>> getPossibleSnippets() {
      return this.possibleSnippets;
   }

   @Generated
   public Gson getGson() {
      return this.gson;
   }

   @Generated
   public Gson getSnippetLoader() {
      return this.snippetLoader;
   }

   @Generated
   public GsonBuilder getBuilder() {
      return this.builder;
   }

   @Generated
   public KMap<Class<? extends IrisRegistrant>, ResourceLoader<? extends IrisRegistrant>> getLoaders() {
      return this.loaders;
   }

   @Generated
   public Engine getEngine() {
      return this.engine;
   }

   @Generated
   public void setClosed(final boolean closed) {
      this.closed = var1;
   }

   @Generated
   public void setEnvironment(final PackEnvironment environment) {
      this.environment = var1;
   }

   @Generated
   public void setBiomeLoader(final ResourceLoader<IrisBiome> biomeLoader) {
      this.biomeLoader = var1;
   }

   @Generated
   public void setLootLoader(final ResourceLoader<IrisLootTable> lootLoader) {
      this.lootLoader = var1;
   }

   @Generated
   public void setRegionLoader(final ResourceLoader<IrisRegion> regionLoader) {
      this.regionLoader = var1;
   }

   @Generated
   public void setDimensionLoader(final ResourceLoader<IrisDimension> dimensionLoader) {
      this.dimensionLoader = var1;
   }

   @Generated
   public void setGeneratorLoader(final ResourceLoader<IrisGenerator> generatorLoader) {
      this.generatorLoader = var1;
   }

   @Generated
   public void setJigsawPieceLoader(final ResourceLoader<IrisJigsawPiece> jigsawPieceLoader) {
      this.jigsawPieceLoader = var1;
   }

   @Generated
   public void setJigsawPoolLoader(final ResourceLoader<IrisJigsawPool> jigsawPoolLoader) {
      this.jigsawPoolLoader = var1;
   }

   @Generated
   public void setJigsawStructureLoader(final ResourceLoader<IrisJigsawStructure> jigsawStructureLoader) {
      this.jigsawStructureLoader = var1;
   }

   @Generated
   public void setEntityLoader(final ResourceLoader<IrisEntity> entityLoader) {
      this.entityLoader = var1;
   }

   @Generated
   public void setMarkerLoader(final ResourceLoader<IrisMarker> markerLoader) {
      this.markerLoader = var1;
   }

   @Generated
   public void setSpawnerLoader(final ResourceLoader<IrisSpawner> spawnerLoader) {
      this.spawnerLoader = var1;
   }

   @Generated
   public void setModLoader(final ResourceLoader<IrisMod> modLoader) {
      this.modLoader = var1;
   }

   @Generated
   public void setBlockLoader(final ResourceLoader<IrisBlockData> blockLoader) {
      this.blockLoader = var1;
   }

   @Generated
   public void setExpressionLoader(final ResourceLoader<IrisExpression> expressionLoader) {
      this.expressionLoader = var1;
   }

   @Generated
   public void setObjectLoader(final ResourceLoader<IrisObject> objectLoader) {
      this.objectLoader = var1;
   }

   @Generated
   public void setMatterLoader(final ResourceLoader<IrisMatterObject> matterLoader) {
      this.matterLoader = var1;
   }

   @Generated
   public void setImageLoader(final ResourceLoader<IrisImage> imageLoader) {
      this.imageLoader = var1;
   }

   @Generated
   public void setScriptLoader(final ResourceLoader<IrisScript> scriptLoader) {
      this.scriptLoader = var1;
   }

   @Generated
   public void setCaveLoader(final ResourceLoader<IrisCave> caveLoader) {
      this.caveLoader = var1;
   }

   @Generated
   public void setRavineLoader(final ResourceLoader<IrisRavine> ravineLoader) {
      this.ravineLoader = var1;
   }

   @Generated
   public void setMatterObjectLoader(final ResourceLoader<IrisMatterObject> matterObjectLoader) {
      this.matterObjectLoader = var1;
   }

   @Generated
   public void setPossibleSnippets(final KMap<String, KList<String>> possibleSnippets) {
      this.possibleSnippets = var1;
   }

   @Generated
   public void setGson(final Gson gson) {
      this.gson = var1;
   }

   @Generated
   public void setSnippetLoader(final Gson snippetLoader) {
      this.snippetLoader = var1;
   }

   @Generated
   public void setBuilder(final GsonBuilder builder) {
      this.builder = var1;
   }

   @Generated
   public void setLoaders(final KMap<Class<? extends IrisRegistrant>, ResourceLoader<? extends IrisRegistrant>> loaders) {
      this.loaders = var1;
   }

   @Generated
   public void setEngine(final Engine engine) {
      this.engine = var1;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisData)) {
         return false;
      } else {
         IrisData var2 = (IrisData)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (this.getId() != var2.getId()) {
            return false;
         } else if (this.isClosed() != var2.isClosed()) {
            return false;
         } else {
            label364: {
               File var3 = this.getDataFolder();
               File var4 = var2.getDataFolder();
               if (var3 == null) {
                  if (var4 == null) {
                     break label364;
                  }
               } else if (var3.equals(var4)) {
                  break label364;
               }

               return false;
            }

            PackEnvironment var5 = this.getEnvironment();
            PackEnvironment var6 = var2.getEnvironment();
            if (var5 == null) {
               if (var6 != null) {
                  return false;
               }
            } else if (!var5.equals(var6)) {
               return false;
            }

            label350: {
               ResourceLoader var7 = this.getBiomeLoader();
               ResourceLoader var8 = var2.getBiomeLoader();
               if (var7 == null) {
                  if (var8 == null) {
                     break label350;
                  }
               } else if (var7.equals(var8)) {
                  break label350;
               }

               return false;
            }

            label343: {
               ResourceLoader var9 = this.getLootLoader();
               ResourceLoader var10 = var2.getLootLoader();
               if (var9 == null) {
                  if (var10 == null) {
                     break label343;
                  }
               } else if (var9.equals(var10)) {
                  break label343;
               }

               return false;
            }

            ResourceLoader var11 = this.getRegionLoader();
            ResourceLoader var12 = var2.getRegionLoader();
            if (var11 == null) {
               if (var12 != null) {
                  return false;
               }
            } else if (!var11.equals(var12)) {
               return false;
            }

            label329: {
               ResourceLoader var13 = this.getDimensionLoader();
               ResourceLoader var14 = var2.getDimensionLoader();
               if (var13 == null) {
                  if (var14 == null) {
                     break label329;
                  }
               } else if (var13.equals(var14)) {
                  break label329;
               }

               return false;
            }

            label322: {
               ResourceLoader var15 = this.getGeneratorLoader();
               ResourceLoader var16 = var2.getGeneratorLoader();
               if (var15 == null) {
                  if (var16 == null) {
                     break label322;
                  }
               } else if (var15.equals(var16)) {
                  break label322;
               }

               return false;
            }

            ResourceLoader var17 = this.getJigsawPieceLoader();
            ResourceLoader var18 = var2.getJigsawPieceLoader();
            if (var17 == null) {
               if (var18 != null) {
                  return false;
               }
            } else if (!var17.equals(var18)) {
               return false;
            }

            ResourceLoader var19 = this.getJigsawPoolLoader();
            ResourceLoader var20 = var2.getJigsawPoolLoader();
            if (var19 == null) {
               if (var20 != null) {
                  return false;
               }
            } else if (!var19.equals(var20)) {
               return false;
            }

            label301: {
               ResourceLoader var21 = this.getJigsawStructureLoader();
               ResourceLoader var22 = var2.getJigsawStructureLoader();
               if (var21 == null) {
                  if (var22 == null) {
                     break label301;
                  }
               } else if (var21.equals(var22)) {
                  break label301;
               }

               return false;
            }

            label294: {
               ResourceLoader var23 = this.getEntityLoader();
               ResourceLoader var24 = var2.getEntityLoader();
               if (var23 == null) {
                  if (var24 == null) {
                     break label294;
                  }
               } else if (var23.equals(var24)) {
                  break label294;
               }

               return false;
            }

            ResourceLoader var25 = this.getMarkerLoader();
            ResourceLoader var26 = var2.getMarkerLoader();
            if (var25 == null) {
               if (var26 != null) {
                  return false;
               }
            } else if (!var25.equals(var26)) {
               return false;
            }

            label280: {
               ResourceLoader var27 = this.getSpawnerLoader();
               ResourceLoader var28 = var2.getSpawnerLoader();
               if (var27 == null) {
                  if (var28 == null) {
                     break label280;
                  }
               } else if (var27.equals(var28)) {
                  break label280;
               }

               return false;
            }

            ResourceLoader var29 = this.getModLoader();
            ResourceLoader var30 = var2.getModLoader();
            if (var29 == null) {
               if (var30 != null) {
                  return false;
               }
            } else if (!var29.equals(var30)) {
               return false;
            }

            label266: {
               ResourceLoader var31 = this.getBlockLoader();
               ResourceLoader var32 = var2.getBlockLoader();
               if (var31 == null) {
                  if (var32 == null) {
                     break label266;
                  }
               } else if (var31.equals(var32)) {
                  break label266;
               }

               return false;
            }

            ResourceLoader var33 = this.getExpressionLoader();
            ResourceLoader var34 = var2.getExpressionLoader();
            if (var33 == null) {
               if (var34 != null) {
                  return false;
               }
            } else if (!var33.equals(var34)) {
               return false;
            }

            label252: {
               ResourceLoader var35 = this.getObjectLoader();
               ResourceLoader var36 = var2.getObjectLoader();
               if (var35 == null) {
                  if (var36 == null) {
                     break label252;
                  }
               } else if (var35.equals(var36)) {
                  break label252;
               }

               return false;
            }

            ResourceLoader var37 = this.getMatterLoader();
            ResourceLoader var38 = var2.getMatterLoader();
            if (var37 == null) {
               if (var38 != null) {
                  return false;
               }
            } else if (!var37.equals(var38)) {
               return false;
            }

            label238: {
               ResourceLoader var39 = this.getImageLoader();
               ResourceLoader var40 = var2.getImageLoader();
               if (var39 == null) {
                  if (var40 == null) {
                     break label238;
                  }
               } else if (var39.equals(var40)) {
                  break label238;
               }

               return false;
            }

            label231: {
               ResourceLoader var41 = this.getScriptLoader();
               ResourceLoader var42 = var2.getScriptLoader();
               if (var41 == null) {
                  if (var42 == null) {
                     break label231;
                  }
               } else if (var41.equals(var42)) {
                  break label231;
               }

               return false;
            }

            ResourceLoader var43 = this.getCaveLoader();
            ResourceLoader var44 = var2.getCaveLoader();
            if (var43 == null) {
               if (var44 != null) {
                  return false;
               }
            } else if (!var43.equals(var44)) {
               return false;
            }

            label217: {
               ResourceLoader var45 = this.getRavineLoader();
               ResourceLoader var46 = var2.getRavineLoader();
               if (var45 == null) {
                  if (var46 == null) {
                     break label217;
                  }
               } else if (var45.equals(var46)) {
                  break label217;
               }

               return false;
            }

            label210: {
               ResourceLoader var47 = this.getMatterObjectLoader();
               ResourceLoader var48 = var2.getMatterObjectLoader();
               if (var47 == null) {
                  if (var48 == null) {
                     break label210;
                  }
               } else if (var47.equals(var48)) {
                  break label210;
               }

               return false;
            }

            KMap var49 = this.getPossibleSnippets();
            KMap var50 = var2.getPossibleSnippets();
            if (var49 == null) {
               if (var50 != null) {
                  return false;
               }
            } else if (!var49.equals(var50)) {
               return false;
            }

            Gson var51 = this.getGson();
            Gson var52 = var2.getGson();
            if (var51 == null) {
               if (var52 != null) {
                  return false;
               }
            } else if (!var51.equals(var52)) {
               return false;
            }

            label189: {
               Gson var53 = this.getSnippetLoader();
               Gson var54 = var2.getSnippetLoader();
               if (var53 == null) {
                  if (var54 == null) {
                     break label189;
                  }
               } else if (var53.equals(var54)) {
                  break label189;
               }

               return false;
            }

            label182: {
               GsonBuilder var55 = this.getBuilder();
               GsonBuilder var56 = var2.getBuilder();
               if (var55 == null) {
                  if (var56 == null) {
                     break label182;
                  }
               } else if (var55.equals(var56)) {
                  break label182;
               }

               return false;
            }

            KMap var57 = this.getLoaders();
            KMap var58 = var2.getLoaders();
            if (var57 == null) {
               if (var58 != null) {
                  return false;
               }
            } else if (!var57.equals(var58)) {
               return false;
            }

            Engine var59 = this.getEngine();
            Engine var60 = var2.getEngine();
            if (var59 == null) {
               if (var60 != null) {
                  return false;
               }
            } else if (!var59.equals(var60)) {
               return false;
            }

            return true;
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof IrisData;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      int var32 = var2 * 59 + this.getId();
      var32 = var32 * 59 + (this.isClosed() ? 79 : 97);
      File var3 = this.getDataFolder();
      var32 = var32 * 59 + (var3 == null ? 43 : var3.hashCode());
      PackEnvironment var4 = this.getEnvironment();
      var32 = var32 * 59 + (var4 == null ? 43 : var4.hashCode());
      ResourceLoader var5 = this.getBiomeLoader();
      var32 = var32 * 59 + (var5 == null ? 43 : var5.hashCode());
      ResourceLoader var6 = this.getLootLoader();
      var32 = var32 * 59 + (var6 == null ? 43 : var6.hashCode());
      ResourceLoader var7 = this.getRegionLoader();
      var32 = var32 * 59 + (var7 == null ? 43 : var7.hashCode());
      ResourceLoader var8 = this.getDimensionLoader();
      var32 = var32 * 59 + (var8 == null ? 43 : var8.hashCode());
      ResourceLoader var9 = this.getGeneratorLoader();
      var32 = var32 * 59 + (var9 == null ? 43 : var9.hashCode());
      ResourceLoader var10 = this.getJigsawPieceLoader();
      var32 = var32 * 59 + (var10 == null ? 43 : var10.hashCode());
      ResourceLoader var11 = this.getJigsawPoolLoader();
      var32 = var32 * 59 + (var11 == null ? 43 : var11.hashCode());
      ResourceLoader var12 = this.getJigsawStructureLoader();
      var32 = var32 * 59 + (var12 == null ? 43 : var12.hashCode());
      ResourceLoader var13 = this.getEntityLoader();
      var32 = var32 * 59 + (var13 == null ? 43 : var13.hashCode());
      ResourceLoader var14 = this.getMarkerLoader();
      var32 = var32 * 59 + (var14 == null ? 43 : var14.hashCode());
      ResourceLoader var15 = this.getSpawnerLoader();
      var32 = var32 * 59 + (var15 == null ? 43 : var15.hashCode());
      ResourceLoader var16 = this.getModLoader();
      var32 = var32 * 59 + (var16 == null ? 43 : var16.hashCode());
      ResourceLoader var17 = this.getBlockLoader();
      var32 = var32 * 59 + (var17 == null ? 43 : var17.hashCode());
      ResourceLoader var18 = this.getExpressionLoader();
      var32 = var32 * 59 + (var18 == null ? 43 : var18.hashCode());
      ResourceLoader var19 = this.getObjectLoader();
      var32 = var32 * 59 + (var19 == null ? 43 : var19.hashCode());
      ResourceLoader var20 = this.getMatterLoader();
      var32 = var32 * 59 + (var20 == null ? 43 : var20.hashCode());
      ResourceLoader var21 = this.getImageLoader();
      var32 = var32 * 59 + (var21 == null ? 43 : var21.hashCode());
      ResourceLoader var22 = this.getScriptLoader();
      var32 = var32 * 59 + (var22 == null ? 43 : var22.hashCode());
      ResourceLoader var23 = this.getCaveLoader();
      var32 = var32 * 59 + (var23 == null ? 43 : var23.hashCode());
      ResourceLoader var24 = this.getRavineLoader();
      var32 = var32 * 59 + (var24 == null ? 43 : var24.hashCode());
      ResourceLoader var25 = this.getMatterObjectLoader();
      var32 = var32 * 59 + (var25 == null ? 43 : var25.hashCode());
      KMap var26 = this.getPossibleSnippets();
      var32 = var32 * 59 + (var26 == null ? 43 : var26.hashCode());
      Gson var27 = this.getGson();
      var32 = var32 * 59 + (var27 == null ? 43 : var27.hashCode());
      Gson var28 = this.getSnippetLoader();
      var32 = var32 * 59 + (var28 == null ? 43 : var28.hashCode());
      GsonBuilder var29 = this.getBuilder();
      var32 = var32 * 59 + (var29 == null ? 43 : var29.hashCode());
      KMap var30 = this.getLoaders();
      var32 = var32 * 59 + (var30 == null ? 43 : var30.hashCode());
      Engine var31 = this.getEngine();
      var32 = var32 * 59 + (var31 == null ? 43 : var31.hashCode());
      return var32;
   }

   @Generated
   public String toString() {
      String var10000 = String.valueOf(this.getDataFolder());
      return "IrisData(dataFolder=" + var10000 + ", id=" + this.getId() + ", closed=" + this.isClosed() + ", environment=" + String.valueOf(this.getEnvironment()) + ", biomeLoader=" + String.valueOf(this.getBiomeLoader()) + ", lootLoader=" + String.valueOf(this.getLootLoader()) + ", regionLoader=" + String.valueOf(this.getRegionLoader()) + ", dimensionLoader=" + String.valueOf(this.getDimensionLoader()) + ", generatorLoader=" + String.valueOf(this.getGeneratorLoader()) + ", jigsawPieceLoader=" + String.valueOf(this.getJigsawPieceLoader()) + ", jigsawPoolLoader=" + String.valueOf(this.getJigsawPoolLoader()) + ", jigsawStructureLoader=" + String.valueOf(this.getJigsawStructureLoader()) + ", entityLoader=" + String.valueOf(this.getEntityLoader()) + ", markerLoader=" + String.valueOf(this.getMarkerLoader()) + ", spawnerLoader=" + String.valueOf(this.getSpawnerLoader()) + ", modLoader=" + String.valueOf(this.getModLoader()) + ", blockLoader=" + String.valueOf(this.getBlockLoader()) + ", expressionLoader=" + String.valueOf(this.getExpressionLoader()) + ", objectLoader=" + String.valueOf(this.getObjectLoader()) + ", matterLoader=" + String.valueOf(this.getMatterLoader()) + ", imageLoader=" + String.valueOf(this.getImageLoader()) + ", scriptLoader=" + String.valueOf(this.getScriptLoader()) + ", caveLoader=" + String.valueOf(this.getCaveLoader()) + ", ravineLoader=" + String.valueOf(this.getRavineLoader()) + ", matterObjectLoader=" + String.valueOf(this.getMatterObjectLoader()) + ", possibleSnippets=" + String.valueOf(this.getPossibleSnippets()) + ", gson=" + String.valueOf(this.getGson()) + ", snippetLoader=" + String.valueOf(this.getSnippetLoader()) + ", builder=" + String.valueOf(this.getBuilder()) + ", loaders=" + String.valueOf(this.getLoaders()) + ", engine=" + String.valueOf(this.getEngine()) + ")";
   }
}
