package com.volmit.iris.engine.platform;

import com.volmit.iris.Iris;
import com.volmit.iris.core.IrisSettings;
import com.volmit.iris.core.IrisWorlds;
import com.volmit.iris.core.loader.IrisData;
import com.volmit.iris.core.nms.INMS;
import com.volmit.iris.core.service.StudioSVC;
import com.volmit.iris.engine.IrisEngine;
import com.volmit.iris.engine.data.cache.AtomicCache;
import com.volmit.iris.engine.data.chunk.TerrainChunk;
import com.volmit.iris.engine.framework.Engine;
import com.volmit.iris.engine.framework.EngineTarget;
import com.volmit.iris.engine.object.IrisDimension;
import com.volmit.iris.engine.object.IrisWorld;
import com.volmit.iris.engine.object.StudioMode;
import com.volmit.iris.engine.platform.studio.StudioGenerator;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.data.IrisBiomeStorage;
import com.volmit.iris.util.hunk.view.BiomeGridHunkHolder;
import com.volmit.iris.util.hunk.view.ChunkDataHunkHolder;
import com.volmit.iris.util.io.ReactiveFolder;
import com.volmit.iris.util.paper.PaperLib;
import com.volmit.iris.util.scheduling.ChronoLatch;
import com.volmit.iris.util.scheduling.J;
import com.volmit.iris.util.scheduling.Looper;
import java.io.File;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import lombok.Generated;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.HeightMap;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;
import org.bukkit.generator.ChunkGenerator.ChunkData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BukkitChunkGenerator extends ChunkGenerator implements PlatformChunkGenerator, Listener {
   private static final int LOAD_LOCKS = Runtime.getRuntime().availableProcessors() * 4;
   private final Semaphore loadLock;
   private final IrisWorld world;
   private final File dataLocation;
   private final String dimensionKey;
   private final ReactiveFolder folder;
   private final ReentrantLock lock = new ReentrantLock();
   private final KList<BlockPopulator> populators = new KList();
   private final ChronoLatch hotloadChecker;
   private final AtomicBoolean setup = new AtomicBoolean(false);
   private final boolean studio;
   private final AtomicInteger a = new AtomicInteger(0);
   private final CompletableFuture<Integer> spawnChunks = new CompletableFuture();
   private final AtomicCache<EngineTarget> targetCache = new AtomicCache();
   private volatile Engine engine;
   private volatile Looper hotloader;
   private volatile StudioMode lastMode;
   private volatile DummyBiomeProvider dummyBiomeProvider = new DummyBiomeProvider();
   private volatile StudioGenerator studioGenerator = null;

   public BukkitChunkGenerator(IrisWorld world, boolean studio, File dataLocation, String dimensionKey) {
      this.loadLock = new Semaphore(LOAD_LOCKS);
      this.world = var1;
      this.hotloadChecker = new ChronoLatch(1000L, false);
      this.studio = var2;
      this.dataLocation = var3;
      this.dimensionKey = var4;
      this.folder = new ReactiveFolder(var3, (var1x, var2x, var3x) -> {
         this.hotload();
      });
      Bukkit.getServer().getPluginManager().registerEvents(this, Iris.instance);
   }

   @EventHandler(
      priority = EventPriority.LOWEST
   )
   public void onWorldInit(WorldInitEvent event) {
      if (this.world.name().equals(var1.getWorld().getName())) {
         Iris.instance.unregisterListener(this);
         this.world.setRawWorldSeed(var1.getWorld().getSeed());
         if (!this.initialize(var1.getWorld())) {
            Iris.warn("Failed to get Engine for " + var1.getWorld().getName() + " re-trying...");
            J.s(() -> {
               if (!this.initialize(var1.getWorld())) {
                  Iris.error("Failed to get Engine for " + var1.getWorld().getName() + "!");
               }

            }, 10);
         }
      }
   }

   private boolean initialize(World world) {
      Engine var2 = this.getEngine(var1);
      if (var2 == null) {
         return false;
      } else {
         try {
            INMS.get().inject(var1.getSeed(), var2, var1);
            Iris.info("Injected Iris Biome Source into " + var1.getName());
         } catch (Throwable var4) {
            Iris.reportError(var4);
            Iris.error("Failed to inject biome source into " + var1.getName());
            var4.printStackTrace();
         }

         this.spawnChunks.complete(INMS.get().getSpawnChunkCount(var1));
         Iris.instance.unregisterListener(this);
         IrisWorlds.get().put(var1.getName(), this.dimensionKey);
         return true;
      }
   }

   @Nullable
   public Location getFixedSpawnLocation(@NotNull World world, @NotNull Random random) {
      Location var3 = new Location(var1, 0.0D, 64.0D, 0.0D);
      PaperLib.getChunkAtAsync(var3).thenAccept((var1x) -> {
         World var2 = var1x.getWorld();
         if (var2.getSpawnLocation().equals(var3)) {
            var2.setSpawnLocation(var3.add(0.0D, (double)(var2.getHighestBlockYAt(var3) - 64), 0.0D));
         }
      });
      return var3;
   }

   private void setupEngine() {
      this.lastMode = StudioMode.NORMAL;
      this.engine = new IrisEngine(this.getTarget(), this.studio);
      this.populators.clear();
      this.targetCache.reset();
   }

   @NotNull
   public EngineTarget getTarget() {
      return this.engine != null ? this.engine.getTarget() : (EngineTarget)this.targetCache.aquire(() -> {
         IrisData var1 = IrisData.get(this.dataLocation);
         var1.dump();
         var1.clearLists();
         IrisDimension var2 = (IrisDimension)var1.getDimensionLoader().load(this.dimensionKey);
         if (var2 == null) {
            Iris.error("Oh No! There's no pack in " + var1.getDataFolder().getPath() + " or... there's no dimension for the key " + this.dimensionKey);
            IrisDimension var3 = IrisData.loadAnyDimension(this.dimensionKey, (IrisData)null);
            if (var3 == null) {
               Iris.error("Nope, you don't have an installation containing " + this.dimensionKey + " try downloading it?");
               throw new RuntimeException("Missing Dimension: " + this.dimensionKey);
            }

            Iris.warn("Looks like " + this.dimensionKey + " exists in " + var3.getLoadFile().getPath() + " ");
            var3 = ((StudioSVC)Iris.service(StudioSVC.class)).installInto(Iris.getSender(), this.dimensionKey, this.dataLocation);
            Iris.warn("Attempted to install into " + var1.getDataFolder().getPath());
            if (var3 == null) {
               Iris.error("Failed to patch dimension!");
               throw new RuntimeException("Missing Dimension: " + this.dimensionKey);
            }

            Iris.success("Woo! Patched the Engine!");
            var2 = var3;
         }

         return new EngineTarget(this.world, var2, var1);
      });
   }

   public void injectChunkReplacement(World world, int x, int z, Executor syncExecutor) {
      try {
         this.loadLock.acquire();
         IrisBiomeStorage var5 = new IrisBiomeStorage();
         TerrainChunk var12 = TerrainChunk.createUnsafe(var1, var5);
         this.world.bind(var1);
         this.getEngine().generate(var2 << 4, var3 << 4, var12, IrisSettings.get().getGenerator().useMulticore);
         Chunk var13 = (Chunk)PaperLib.getChunkAtAsync(var1, var2, var3).thenApply((var0) -> {
            Iris.tickets.addTicket(var0);
            Entity[] var1 = var0.getEntities();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
               Entity var4 = var1[var3];
               if (!(var4 instanceof Player)) {
                  var4.remove();
               }
            }

            return var0;
         }).get();
         KList var14 = new KList(1 + this.getEngine().getHeight() >> 4);

         for(int var9 = this.getEngine().getHeight() >> 4; var9 >= 0; --var9) {
            int var10 = var9 << 4;
            var14.add((Object)CompletableFuture.runAsync(() -> {
               for(int var5 = 0; var5 < 16; ++var5) {
                  for(int var6 = 0; var6 < 16; ++var6) {
                     for(int var7 = 0; var7 < 16; ++var7) {
                        if (var6 + var10 < this.engine.getHeight() && var6 + var10 >= 0) {
                           int var8 = var6 + var10 + var1.getMinHeight();
                           var13.getBlock(var5, var8, var7).setBlockData(var12.getBlockData(var5, var8, var7), false);
                        }
                     }
                  }
               }

            }, var4));
         }

         var14.add((Object)CompletableFuture.runAsync(() -> {
            INMS.get().placeStructures(var13);
         }, var4));
         CompletableFuture.allOf((CompletableFuture[])var14.toArray(new CompletableFuture[0])).thenRunAsync(() -> {
            Iris.tickets.removeTicket(var13);
            this.engine.getWorldManager().onChunkLoad(var13, true);
         }, var4).get();
         Iris.debug("Regenerated " + var2 + " " + var3);
         this.loadLock.release();
      } catch (Throwable var11) {
         this.loadLock.release();
         Iris.error("======================================");
         var11.printStackTrace();
         Iris.reportErrorChunk(var2, var3, var11, "CHUNK");
         Iris.error("======================================");
         ChunkData var6 = Bukkit.createChunkData(var1);

         for(int var7 = 0; var7 < 16; ++var7) {
            for(int var8 = 0; var8 < 16; ++var8) {
               var6.setBlock(var7, 0, var8, Material.RED_GLAZED_TERRACOTTA.createBlockData());
            }
         }
      }

   }

   private Engine getEngine(WorldInfo world) {
      if (this.setup.get()) {
         return this.getEngine();
      } else {
         this.lock.lock();

         Engine var2;
         try {
            if (!this.setup.get()) {
               this.getWorld().setRawWorldSeed(var1.getSeed());
               this.setupEngine();
               this.setup.set(true);
               this.hotloader = this.studio ? new Looper() {
                  protected long loop() {
                     if (BukkitChunkGenerator.this.hotloadChecker.flip()) {
                        BukkitChunkGenerator.this.folder.check();
                     }

                     return 250L;
                  }
               } : null;
               if (this.studio) {
                  this.hotloader.setPriority(1);
                  this.hotloader.start();
                  this.hotloader.setName(this.getTarget().getWorld().name() + " Hotloader");
               }

               var2 = this.engine;
               return var2;
            }

            var2 = this.getEngine();
         } finally {
            this.lock.unlock();
         }

         return var2;
      }
   }

   public void close() {
      this.withExclusiveControl(() -> {
         if (this.isStudio()) {
            this.hotloader.interrupt();
         }

         Engine var1 = this.getEngine();
         if (var1 != null && !var1.isClosed()) {
            var1.close();
         }

         this.folder.clear();
         this.populators.clear();
      });
   }

   public boolean isStudio() {
      return this.studio;
   }

   public void hotload() {
      if (this.isStudio()) {
         this.withExclusiveControl(() -> {
            this.getEngine().hotload();
         });
      }
   }

   public void withExclusiveControl(Runnable r) {
      J.a(() -> {
         try {
            this.loadLock.acquire(LOAD_LOCKS);
            var1.run();
            this.loadLock.release(LOAD_LOCKS);
         } catch (Throwable var3) {
            Iris.reportError(var3);
         }

      });
   }

   public void touch(World world) {
      this.getEngine(var1);
   }

   public void generateNoise(@NotNull WorldInfo world, @NotNull Random random, int x, int z, @NotNull ChunkData d) {
      try {
         Engine var6 = this.getEngine(var1);
         this.computeStudioGenerator();
         TerrainChunk var11 = TerrainChunk.create((ChunkData)var5, new IrisBiomeStorage());
         this.world.bind(var1);
         if (this.studioGenerator != null) {
            this.studioGenerator.generateChunk(var6, var11, var3, var4);
         } else {
            ChunkDataHunkHolder var12 = new ChunkDataHunkHolder(var11);
            BiomeGridHunkHolder var9 = new BiomeGridHunkHolder(var11, var11.getMinHeight(), var11.getMaxHeight());
            var6.generate(var3 << 4, var4 << 4, var12, var9, IrisSettings.get().getGenerator().useMulticore);
            var12.apply();
            var9.apply();
         }

         Iris.debug("Generated " + var3 + " " + var4);
      } catch (Throwable var10) {
         Iris.error("======================================");
         var10.printStackTrace();
         Iris.reportErrorChunk(var3, var4, var10, "CHUNK");
         Iris.error("======================================");

         for(int var7 = 0; var7 < 16; ++var7) {
            for(int var8 = 0; var8 < 16; ++var8) {
               var5.setBlock(var7, 0, var8, Material.RED_GLAZED_TERRACOTTA.createBlockData());
            }
         }
      }

   }

   public int getBaseHeight(@NotNull WorldInfo worldInfo, @NotNull Random random, int x, int z, @NotNull HeightMap heightMap) {
      return 4;
   }

   private void computeStudioGenerator() {
      if (!this.getEngine().getDimension().getStudioMode().equals(this.lastMode)) {
         this.lastMode = this.getEngine().getDimension().getStudioMode();
         this.getEngine().getDimension().getStudioMode().inject(this);
      }

   }

   @NotNull
   public List<BlockPopulator> getDefaultPopulators(@NotNull World world) {
      return this.populators;
   }

   public boolean isParallelCapable() {
      return true;
   }

   public boolean shouldGenerateCaves() {
      return false;
   }

   public boolean shouldGenerateDecorations() {
      return false;
   }

   public boolean shouldGenerateMobs() {
      return false;
   }

   public boolean shouldGenerateStructures() {
      return false;
   }

   public boolean shouldGenerateNoise() {
      return false;
   }

   public boolean shouldGenerateSurface() {
      return false;
   }

   public boolean shouldGenerateBedrock() {
      return false;
   }

   @Nullable
   public BiomeProvider getDefaultBiomeProvider(@NotNull WorldInfo worldInfo) {
      return this.dummyBiomeProvider;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof BukkitChunkGenerator)) {
         return false;
      } else {
         BukkitChunkGenerator var2 = (BukkitChunkGenerator)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (!super.equals(var1)) {
            return false;
         } else if (this.isStudio() != var2.isStudio()) {
            return false;
         } else {
            label220: {
               Semaphore var3 = this.getLoadLock();
               Semaphore var4 = var2.getLoadLock();
               if (var3 == null) {
                  if (var4 == null) {
                     break label220;
                  }
               } else if (var3.equals(var4)) {
                  break label220;
               }

               return false;
            }

            IrisWorld var5 = this.getWorld();
            IrisWorld var6 = var2.getWorld();
            if (var5 == null) {
               if (var6 != null) {
                  return false;
               }
            } else if (!var5.equals(var6)) {
               return false;
            }

            label206: {
               File var7 = this.getDataLocation();
               File var8 = var2.getDataLocation();
               if (var7 == null) {
                  if (var8 == null) {
                     break label206;
                  }
               } else if (var7.equals(var8)) {
                  break label206;
               }

               return false;
            }

            label199: {
               String var9 = this.getDimensionKey();
               String var10 = var2.getDimensionKey();
               if (var9 == null) {
                  if (var10 == null) {
                     break label199;
                  }
               } else if (var9.equals(var10)) {
                  break label199;
               }

               return false;
            }

            ReactiveFolder var11 = this.getFolder();
            ReactiveFolder var12 = var2.getFolder();
            if (var11 == null) {
               if (var12 != null) {
                  return false;
               }
            } else if (!var11.equals(var12)) {
               return false;
            }

            label185: {
               ReentrantLock var13 = this.getLock();
               ReentrantLock var14 = var2.getLock();
               if (var13 == null) {
                  if (var14 == null) {
                     break label185;
                  }
               } else if (var13.equals(var14)) {
                  break label185;
               }

               return false;
            }

            label178: {
               KList var15 = this.getPopulators();
               KList var16 = var2.getPopulators();
               if (var15 == null) {
                  if (var16 == null) {
                     break label178;
                  }
               } else if (var15.equals(var16)) {
                  break label178;
               }

               return false;
            }

            ChronoLatch var17 = this.getHotloadChecker();
            ChronoLatch var18 = var2.getHotloadChecker();
            if (var17 == null) {
               if (var18 != null) {
                  return false;
               }
            } else if (!var17.equals(var18)) {
               return false;
            }

            AtomicBoolean var19 = this.getSetup();
            AtomicBoolean var20 = var2.getSetup();
            if (var19 == null) {
               if (var20 != null) {
                  return false;
               }
            } else if (!var19.equals(var20)) {
               return false;
            }

            label157: {
               AtomicInteger var21 = this.getA();
               AtomicInteger var22 = var2.getA();
               if (var21 == null) {
                  if (var22 == null) {
                     break label157;
                  }
               } else if (var21.equals(var22)) {
                  break label157;
               }

               return false;
            }

            label150: {
               CompletableFuture var23 = this.getSpawnChunks();
               CompletableFuture var24 = var2.getSpawnChunks();
               if (var23 == null) {
                  if (var24 == null) {
                     break label150;
                  }
               } else if (var23.equals(var24)) {
                  break label150;
               }

               return false;
            }

            AtomicCache var25 = this.getTargetCache();
            AtomicCache var26 = var2.getTargetCache();
            if (var25 == null) {
               if (var26 != null) {
                  return false;
               }
            } else if (!var25.equals(var26)) {
               return false;
            }

            label136: {
               Engine var27 = this.getEngine();
               Engine var28 = var2.getEngine();
               if (var27 == null) {
                  if (var28 == null) {
                     break label136;
                  }
               } else if (var27.equals(var28)) {
                  break label136;
               }

               return false;
            }

            Looper var29 = this.getHotloader();
            Looper var30 = var2.getHotloader();
            if (var29 == null) {
               if (var30 != null) {
                  return false;
               }
            } else if (!var29.equals(var30)) {
               return false;
            }

            label122: {
               StudioMode var31 = this.getLastMode();
               StudioMode var32 = var2.getLastMode();
               if (var31 == null) {
                  if (var32 == null) {
                     break label122;
                  }
               } else if (var31.equals(var32)) {
                  break label122;
               }

               return false;
            }

            DummyBiomeProvider var33 = this.getDummyBiomeProvider();
            DummyBiomeProvider var34 = var2.getDummyBiomeProvider();
            if (var33 == null) {
               if (var34 != null) {
                  return false;
               }
            } else if (!var33.equals(var34)) {
               return false;
            }

            StudioGenerator var35 = this.getStudioGenerator();
            StudioGenerator var36 = var2.getStudioGenerator();
            if (var35 == null) {
               if (var36 == null) {
                  return true;
               }
            } else if (var35.equals(var36)) {
               return true;
            }

            return false;
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof BukkitChunkGenerator;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      int var2 = super.hashCode();
      var2 = var2 * 59 + (this.isStudio() ? 79 : 97);
      Semaphore var3 = this.getLoadLock();
      var2 = var2 * 59 + (var3 == null ? 43 : var3.hashCode());
      IrisWorld var4 = this.getWorld();
      var2 = var2 * 59 + (var4 == null ? 43 : var4.hashCode());
      File var5 = this.getDataLocation();
      var2 = var2 * 59 + (var5 == null ? 43 : var5.hashCode());
      String var6 = this.getDimensionKey();
      var2 = var2 * 59 + (var6 == null ? 43 : var6.hashCode());
      ReactiveFolder var7 = this.getFolder();
      var2 = var2 * 59 + (var7 == null ? 43 : var7.hashCode());
      ReentrantLock var8 = this.getLock();
      var2 = var2 * 59 + (var8 == null ? 43 : var8.hashCode());
      KList var9 = this.getPopulators();
      var2 = var2 * 59 + (var9 == null ? 43 : var9.hashCode());
      ChronoLatch var10 = this.getHotloadChecker();
      var2 = var2 * 59 + (var10 == null ? 43 : var10.hashCode());
      AtomicBoolean var11 = this.getSetup();
      var2 = var2 * 59 + (var11 == null ? 43 : var11.hashCode());
      AtomicInteger var12 = this.getA();
      var2 = var2 * 59 + (var12 == null ? 43 : var12.hashCode());
      CompletableFuture var13 = this.getSpawnChunks();
      var2 = var2 * 59 + (var13 == null ? 43 : var13.hashCode());
      AtomicCache var14 = this.getTargetCache();
      var2 = var2 * 59 + (var14 == null ? 43 : var14.hashCode());
      Engine var15 = this.getEngine();
      var2 = var2 * 59 + (var15 == null ? 43 : var15.hashCode());
      Looper var16 = this.getHotloader();
      var2 = var2 * 59 + (var16 == null ? 43 : var16.hashCode());
      StudioMode var17 = this.getLastMode();
      var2 = var2 * 59 + (var17 == null ? 43 : var17.hashCode());
      DummyBiomeProvider var18 = this.getDummyBiomeProvider();
      var2 = var2 * 59 + (var18 == null ? 43 : var18.hashCode());
      StudioGenerator var19 = this.getStudioGenerator();
      var2 = var2 * 59 + (var19 == null ? 43 : var19.hashCode());
      return var2;
   }

   @Generated
   public Semaphore getLoadLock() {
      return this.loadLock;
   }

   @Generated
   public IrisWorld getWorld() {
      return this.world;
   }

   @Generated
   public File getDataLocation() {
      return this.dataLocation;
   }

   @Generated
   public String getDimensionKey() {
      return this.dimensionKey;
   }

   @Generated
   public ReactiveFolder getFolder() {
      return this.folder;
   }

   @Generated
   public ReentrantLock getLock() {
      return this.lock;
   }

   @Generated
   public KList<BlockPopulator> getPopulators() {
      return this.populators;
   }

   @Generated
   public ChronoLatch getHotloadChecker() {
      return this.hotloadChecker;
   }

   @Generated
   public AtomicBoolean getSetup() {
      return this.setup;
   }

   @Generated
   public AtomicInteger getA() {
      return this.a;
   }

   @Generated
   public CompletableFuture<Integer> getSpawnChunks() {
      return this.spawnChunks;
   }

   @Generated
   public AtomicCache<EngineTarget> getTargetCache() {
      return this.targetCache;
   }

   @Generated
   public Engine getEngine() {
      return this.engine;
   }

   @Generated
   public Looper getHotloader() {
      return this.hotloader;
   }

   @Generated
   public StudioMode getLastMode() {
      return this.lastMode;
   }

   @Generated
   public DummyBiomeProvider getDummyBiomeProvider() {
      return this.dummyBiomeProvider;
   }

   @Generated
   public StudioGenerator getStudioGenerator() {
      return this.studioGenerator;
   }

   @Generated
   public void setEngine(final Engine engine) {
      this.engine = var1;
   }

   @Generated
   public void setHotloader(final Looper hotloader) {
      this.hotloader = var1;
   }

   @Generated
   public void setLastMode(final StudioMode lastMode) {
      this.lastMode = var1;
   }

   @Generated
   public void setDummyBiomeProvider(final DummyBiomeProvider dummyBiomeProvider) {
      this.dummyBiomeProvider = var1;
   }

   @Generated
   public String toString() {
      String var10000 = String.valueOf(this.getLoadLock());
      return "BukkitChunkGenerator(loadLock=" + var10000 + ", world=" + String.valueOf(this.getWorld()) + ", dataLocation=" + String.valueOf(this.getDataLocation()) + ", dimensionKey=" + this.getDimensionKey() + ", folder=" + String.valueOf(this.getFolder()) + ", lock=" + String.valueOf(this.getLock()) + ", populators=" + String.valueOf(this.getPopulators()) + ", hotloadChecker=" + String.valueOf(this.getHotloadChecker()) + ", setup=" + String.valueOf(this.getSetup()) + ", studio=" + this.isStudio() + ", a=" + String.valueOf(this.getA()) + ", spawnChunks=" + String.valueOf(this.getSpawnChunks()) + ", targetCache=" + String.valueOf(this.getTargetCache()) + ", engine=" + String.valueOf(this.getEngine()) + ", hotloader=" + String.valueOf(this.getHotloader()) + ", lastMode=" + String.valueOf(this.getLastMode()) + ", dummyBiomeProvider=" + String.valueOf(this.getDummyBiomeProvider()) + ", studioGenerator=" + String.valueOf(this.getStudioGenerator()) + ")";
   }

   @Generated
   public void setStudioGenerator(final StudioGenerator studioGenerator) {
      this.studioGenerator = var1;
   }
}
