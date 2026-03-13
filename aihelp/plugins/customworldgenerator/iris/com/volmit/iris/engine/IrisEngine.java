package com.volmit.iris.engine;

import com.google.common.util.concurrent.AtomicDouble;
import com.google.gson.Gson;
import com.volmit.iris.Iris;
import com.volmit.iris.core.ServerConfigurator;
import com.volmit.iris.core.events.IrisEngineHotloadEvent;
import com.volmit.iris.core.gui.PregeneratorJob;
import com.volmit.iris.core.loader.ResourceLoader;
import com.volmit.iris.core.nms.container.BlockPos;
import com.volmit.iris.core.nms.container.Pair;
import com.volmit.iris.core.project.IrisProject;
import com.volmit.iris.core.scripting.environment.EngineEnvironment;
import com.volmit.iris.core.service.PreservationSVC;
import com.volmit.iris.engine.data.cache.AtomicCache;
import com.volmit.iris.engine.framework.Engine;
import com.volmit.iris.engine.framework.EngineEffects;
import com.volmit.iris.engine.framework.EngineMetrics;
import com.volmit.iris.engine.framework.EngineMode;
import com.volmit.iris.engine.framework.EngineTarget;
import com.volmit.iris.engine.framework.EngineWorldManager;
import com.volmit.iris.engine.framework.SeedManager;
import com.volmit.iris.engine.framework.WrongEngineBroException;
import com.volmit.iris.engine.mantle.EngineMantle;
import com.volmit.iris.engine.object.IrisBiome;
import com.volmit.iris.engine.object.IrisBiomePaletteLayer;
import com.volmit.iris.engine.object.IrisDecorator;
import com.volmit.iris.engine.object.IrisDimension;
import com.volmit.iris.engine.object.IrisEngineData;
import com.volmit.iris.engine.object.IrisJigsawStructure;
import com.volmit.iris.engine.object.IrisObjectPlacement;
import com.volmit.iris.engine.object.IrisRegion;
import com.volmit.iris.util.atomics.AtomicRollingSequence;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.collection.KMap;
import com.volmit.iris.util.context.ChunkContext;
import com.volmit.iris.util.context.IrisContext;
import com.volmit.iris.util.documentation.BlockCoordinates;
import com.volmit.iris.util.format.C;
import com.volmit.iris.util.format.Form;
import com.volmit.iris.util.hunk.Hunk;
import com.volmit.iris.util.io.IO;
import com.volmit.iris.util.mantle.flag.MantleFlag;
import com.volmit.iris.util.math.M;
import com.volmit.iris.util.math.RNG;
import com.volmit.iris.util.matter.MatterStructurePOI;
import com.volmit.iris.util.matter.slices.container.JigsawStructureContainer;
import com.volmit.iris.util.scheduling.ChronoLatch;
import com.volmit.iris.util.scheduling.J;
import com.volmit.iris.util.scheduling.PrecisionStopwatch;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import lombok.Generated;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.CommandSender;

public class IrisEngine implements Engine {
   private final AtomicInteger bud;
   private final AtomicInteger buds;
   private final AtomicInteger generated;
   private final AtomicInteger generatedLast;
   private final AtomicDouble perSecond;
   private final AtomicLong lastGPS;
   private final EngineTarget target;
   private final IrisContext context;
   private final EngineMantle mantle;
   private final ChronoLatch perSecondLatch;
   private final ChronoLatch perSecondBudLatch;
   private final EngineMetrics metrics;
   private final boolean studio;
   private final AtomicRollingSequence wallClock;
   private final int art;
   private final AtomicCache<IrisEngineData> engineData = new AtomicCache();
   private final AtomicBoolean cleaning;
   private final ChronoLatch cleanLatch;
   private final SeedManager seedManager;
   private CompletableFuture<Long> hash32;
   private EngineMode mode;
   private EngineEffects effects;
   private EngineEnvironment execution;
   private EngineWorldManager worldManager;
   private volatile int parallelism;
   private boolean failing;
   private boolean closed;
   private int cacheId;
   private double maxBiomeObjectDensity;
   private double maxBiomeLayerDensity;
   private double maxBiomeDecoratorDensity;
   private IrisComplex complex;

   public IrisEngine(EngineTarget target, boolean studio) {
      this.studio = var2;
      this.target = var1;
      this.getEngineData();
      this.verifySeed();
      this.seedManager = new SeedManager(var1.getWorld().getRawWorldSeed());
      this.bud = new AtomicInteger(0);
      this.buds = new AtomicInteger(0);
      this.metrics = new EngineMetrics(32);
      this.cleanLatch = new ChronoLatch(10000L);
      this.generatedLast = new AtomicInteger(0);
      this.perSecond = new AtomicDouble(0.0D);
      this.perSecondLatch = new ChronoLatch(1000L, false);
      this.perSecondBudLatch = new ChronoLatch(1000L, false);
      this.wallClock = new AtomicRollingSequence(32);
      this.lastGPS = new AtomicLong(M.ms());
      this.generated = new AtomicInteger(0);
      this.mantle = new IrisEngineMantle(this);
      this.context = new IrisContext(this);
      this.cleaning = new AtomicBoolean(false);
      this.execution = this.getData().getEnvironment().with(this);
      if (var2) {
         this.getData().dump();
         this.getData().clearLists();
         this.getTarget().setDimension((IrisDimension)this.getData().getDimensionLoader().load(this.getDimension().getLoadKey()));
      }

      this.context.touch();
      this.getData().setEngine(this);
      this.getData().loadPrefetch(this);
      String var10000 = var1.getWorld().name();
      Iris.info("Initializing Engine: " + var10000 + "/" + var1.getDimension().getLoadKey() + " (" + String.valueOf(var1.getDimension().getDimensionHeight()) + " height) Seed: " + this.getSeedManager().getSeed());
      this.failing = false;
      this.closed = false;
      this.art = J.ar(this::tickRandomPlayer, 0);
      this.setupEngine();
      Iris.debug("Engine Initialized " + this.getCacheID());
   }

   private void verifySeed() {
      if (this.getEngineData().getSeed() != null && this.getEngineData().getSeed() != this.target.getWorld().getRawWorldSeed()) {
         this.target.getWorld().setRawWorldSeed(this.getEngineData().getSeed());
      }

   }

   private void tickRandomPlayer() {
      this.recycle();
      if (this.perSecondBudLatch.flip()) {
         this.buds.set(this.bud.get());
         this.bud.set(0);
      }

      if (this.effects != null) {
         this.effects.tickRandomPlayer();
      }

   }

   private void prehotload() {
      this.worldManager.close();
      this.complex.close();
      this.effects.close();
      this.mode.close();
      this.execution = this.getData().getEnvironment().with(this);
      J.a(() -> {
         return (new IrisProject(this.getData().getDataFolder())).updateWorkspace();
      });
   }

   private void setupEngine() {
      try {
         Iris.debug("Setup Engine " + this.getCacheID());
         this.cacheId = RNG.r.nextInt();
         this.worldManager = new IrisWorldManager(this);
         this.complex = new IrisComplex(this);
         this.effects = new IrisEngineEffects(this);
         this.hash32 = new CompletableFuture();
         this.mantle.hotload();
         this.setupMode();
         KList var10000 = this.getDimension().getEngineScripts();
         EngineEnvironment var10001 = this.execution;
         Objects.requireNonNull(var10001);
         var10000.forEach(var10001::execute);
         J.a(this::computeBiomeMaxes);
         J.a(() -> {
            File[] var1 = (File[])this.getData().getLoaders().values().stream().map(ResourceLoader::getFolderName).map((var1x) -> {
               return new File(this.getData().getDataFolder(), var1x);
            }).filter(File::exists).filter(File::isDirectory).toArray((var0) -> {
               return new File[var0];
            });
            this.hash32.complete(IO.hashRecursive(var1));
         });
      } catch (Throwable var2) {
         Iris.error("FAILED TO SETUP ENGINE!");
         var2.printStackTrace();
      }

      Iris.debug("Engine Setup Complete " + this.getCacheID());
   }

   private void setupMode() {
      if (this.mode != null) {
         this.mode.close();
      }

      this.mode = this.getDimension().getMode().create(this);
   }

   public void generateMatter(int x, int z, boolean multicore, ChunkContext context) {
      this.getMantle().generateMatter(var1, var2, var3, var4);
   }

   public Set<String> getObjectsAt(int x, int z) {
      return this.getMantle().getObjectComponent().guess(var1, var2);
   }

   public Set<Pair<String, BlockPos>> getPOIsAt(int chunkX, int chunkY) {
      HashSet var3 = new HashSet();
      this.getMantle().getMantle().iterateChunk(var1, var2, MatterStructurePOI.class, (var1x, var2x, var3x, var4) -> {
         var3.add(new Pair(var4.getType(), new BlockPos(var1x, var2x, var3x)));
      });
      return var3;
   }

   public IrisJigsawStructure getStructureAt(int x, int z) {
      return this.getMantle().getJigsawComponent().guess(var1, var2);
   }

   public IrisJigsawStructure getStructureAt(int x, int y, int z) {
      JigsawStructureContainer var4 = (JigsawStructureContainer)this.getMantle().getMantle().get(var1, var2, var3, JigsawStructureContainer.class);
      return var4 == null ? null : (IrisJigsawStructure)var4.load(this.getData());
   }

   private void warmupChunk(int x, int z) {
      for(int var3 = 0; var3 < 16; ++var3) {
         for(int var4 = 0; var4 < 16; ++var4) {
            int var5 = var1 + (var3 << 4);
            int var6 = var2 + (var2 << 4);
            this.getComplex().getTrueBiomeStream().get((double)var5, (double)var6);
            this.getComplex().getHeightStream().get((double)var5, (double)var6);
         }
      }

   }

   public void hotload() {
      this.hotloadSilently();
      Iris.callEvent(new IrisEngineHotloadEvent(this));
   }

   public void hotloadComplex() {
      this.complex.close();
      this.complex = new IrisComplex(this);
   }

   public void hotloadSilently() {
      this.getData().dump();
      this.getData().clearLists();
      this.getTarget().setDimension((IrisDimension)this.getData().getDimensionLoader().load(this.getDimension().getLoadKey()));
      this.prehotload();
      this.setupEngine();
      J.a(() -> {
         Class var0 = ServerConfigurator.class;
         synchronized(ServerConfigurator.class) {
            ServerConfigurator.installDataPacks(false);
         }
      });
   }

   public IrisEngineData getEngineData() {
      return (IrisEngineData)this.engineData.aquire(() -> {
         File var1 = new File(this.getWorld().worldFolder(), "iris/engine-data/" + this.getDimension().getLoadKey() + ".json");
         IrisEngineData var2 = null;
         if (var1.exists()) {
            try {
               var2 = (IrisEngineData)(new Gson()).fromJson(IO.readAll(var1), IrisEngineData.class);
               if (var2 == null) {
                  Iris.error("Failed to read Engine Data! Corrupted File? recreating...");
               }
            } catch (IOException var5) {
               var5.printStackTrace();
            }
         }

         if (var2 == null) {
            var2 = new IrisEngineData();
            var2.getStatistics().setVersion(Iris.instance.getIrisVersion());
            var2.getStatistics().setMCVersion(Iris.instance.getMCVersion());
            var2.getStatistics().setUpgradedVersion(Iris.instance.getIrisVersion());
            if (var2.getStatistics().getVersion() == -1 || var2.getStatistics().getMCVersion() == -1) {
               Iris.error("Failed to setup Engine Data!");
            }

            if (!var1.getParentFile().exists() && !var1.getParentFile().mkdirs()) {
               Iris.error("Failed to setup Engine Data!");
            } else {
               try {
                  IO.writeAll(var1, (Object)(new Gson()).toJson(var2));
               } catch (IOException var4) {
                  var4.printStackTrace();
               }
            }
         }

         return var2;
      });
   }

   public int getGenerated() {
      return this.generated.get();
   }

   public double getGeneratedPerSecond() {
      if (this.perSecondLatch.flip()) {
         double var1 = (double)(this.generated.get() - this.generatedLast.get());
         this.generatedLast.set(this.generated.get());
         if (var1 == 0.0D) {
            return 0.0D;
         }

         long var3 = M.ms() - this.lastGPS.get();
         this.lastGPS.set(M.ms());
         this.perSecond.set(var1 / ((double)var3 / 1000.0D));
      }

      return this.perSecond.get();
   }

   public boolean isStudio() {
      return this.studio;
   }

   private void computeBiomeMaxes() {
      double var3;
      for(Iterator var1 = this.getDimension().getAllBiomes(this).iterator(); var1.hasNext(); this.maxBiomeLayerDensity = Math.max(this.maxBiomeLayerDensity, var3)) {
         IrisBiome var2 = (IrisBiome)var1.next();
         var3 = 0.0D;

         Iterator var5;
         IrisObjectPlacement var6;
         for(var5 = var2.getObjects().iterator(); var5.hasNext(); var3 += (double)var6.getDensity() * var6.getChance()) {
            var6 = (IrisObjectPlacement)var5.next();
         }

         this.maxBiomeObjectDensity = Math.max(this.maxBiomeObjectDensity, var3);
         var3 = 0.0D;

         IrisDecorator var7;
         for(var5 = var2.getDecorators().iterator(); var5.hasNext(); var3 += (double)Math.max(var7.getStackMax(), 1) * var7.getChance()) {
            var7 = (IrisDecorator)var5.next();
         }

         this.maxBiomeDecoratorDensity = Math.max(this.maxBiomeDecoratorDensity, var3);
         var3 = 0.0D;

         for(var5 = var2.getLayers().iterator(); var5.hasNext(); ++var3) {
            IrisBiomePaletteLayer var8 = (IrisBiomePaletteLayer)var5.next();
         }
      }

   }

   public int getBlockUpdatesPerSecond() {
      return this.buds.get();
   }

   public void printMetrics(CommandSender sender) {
      KMap var2 = new KMap();
      KMap var3 = new KMap();
      double var4 = this.wallClock.getAverage();
      KMap var6 = this.getMetrics().pull();
      double var7 = 0.0D;
      double var9 = this.getMetrics().getTotal().getAverage();

      Iterator var11;
      double var12;
      for(var11 = var6.values().iterator(); var11.hasNext(); var7 += var12) {
         var12 = (Double)var11.next();
      }

      var11 = var6.k().iterator();

      while(var11.hasNext()) {
         String var21 = (String)var11.next();
         var3.put(this.getName() + "." + var21, var9 / var7 * (Double)var6.get(var21));
      }

      var2.put(this.getName(), var9);
      double var20 = 0.0D;

      Iterator var13;
      double var14;
      for(var13 = var2.values().iterator(); var13.hasNext(); var20 += var14) {
         var14 = (Double)var13.next();
      }

      var13 = var2.k().iterator();

      while(var13.hasNext()) {
         String var23 = (String)var13.next();
         var2.put(var23, var4 / var20 * (Double)var2.get(var23));
      }

      double var22 = 0.0D;

      Iterator var15;
      double var16;
      for(var15 = var3.values().iterator(); var15.hasNext(); var22 += var16) {
         var16 = (Double)var15.next();
      }

      var15 = var3.k().iterator();

      String var24;
      while(var15.hasNext()) {
         var24 = (String)var15.next();
         var3.put(var24, (Double)var3.get(var24) / var22);
      }

      String var10001 = String.valueOf(C.BOLD);
      var1.sendMessage("Total: " + var10001 + String.valueOf(C.WHITE) + Form.duration(var4, 0));
      var15 = var2.k().iterator();

      while(var15.hasNext()) {
         var24 = (String)var15.next();
         var10001 = String.valueOf(C.UNDERLINE);
         var1.sendMessage("  Engine " + var10001 + String.valueOf(C.GREEN) + var24 + String.valueOf(C.RESET) + ": " + String.valueOf(C.BOLD) + String.valueOf(C.WHITE) + Form.duration((Double)var2.get(var24), 0));
      }

      var1.sendMessage("Details: ");
      var15 = var3.sortKNumber().reverse().iterator();

      while(var15.hasNext()) {
         var24 = (String)var15.next();
         String var10000 = String.valueOf(C.UNDERLINE);
         String var17 = var10000 + String.valueOf(C.GREEN) + var24.split("\\Q[\\E")[0] + String.valueOf(C.RESET) + String.valueOf(C.GRAY) + "[";
         var10000 = String.valueOf(C.GOLD);
         String var18 = var10000 + var24.split("\\Q[\\E")[1].split("]")[0] + String.valueOf(C.RESET) + String.valueOf(C.GRAY) + "].";
         var10000 = String.valueOf(C.ITALIC);
         String var19 = var10000 + String.valueOf(C.AQUA) + var24.split("\\Q]\\E")[1].substring(1) + String.valueOf(C.RESET) + String.valueOf(C.GRAY);
         var1.sendMessage("  " + var17 + var18 + var19 + ": " + String.valueOf(C.BOLD) + String.valueOf(C.WHITE) + Form.pc((Double)var3.get(var24), 0));
      }

   }

   public void close() {
      PregeneratorJob.shutdownInstance();
      this.closed = true;
      J.car(this.art);
      this.getWorldManager().close();
      this.getTarget().close();
      this.saveEngineData();
      this.getMantle().close();
      this.getComplex().close();
      this.mode.close();
      this.getData().dump();
      this.getData().clearLists();
      ((PreservationSVC)Iris.service(PreservationSVC.class)).dereference();
      Iris.debug("Engine Fully Shutdown!");
      this.complex = null;
   }

   public boolean isClosed() {
      return this.closed;
   }

   public void recycle() {
      if (this.cleanLatch.flip()) {
         if (this.cleaning.get()) {
            this.cleanLatch.flipDown();
         } else {
            this.cleaning.set(true);
            J.a(() -> {
               try {
                  this.getData().getObjectLoader().clean();
               } catch (Throwable var2) {
                  Iris.reportError(var2);
                  Iris.error("Cleanup failed! Enable debug to see stacktrace.");
               }

               this.cleaning.lazySet(false);
            });
         }
      }
   }

   @BlockCoordinates
   public void generate(int x, int z, Hunk<BlockData> vblocks, Hunk<Biome> vbiomes, boolean multicore) {
      if (this.closed) {
         throw new WrongEngineBroException();
      } else {
         this.context.touch();
         this.getEngineData().getStatistics().generatedChunk();

         try {
            PrecisionStopwatch var6 = PrecisionStopwatch.start();
            Hunk var7 = var3.listen((var3x, var4x, var5x, var6x) -> {
               this.catchBlockUpdates(var1 + var3x, var4x, var2 + var5x, var6x);
            });
            if (!this.getDimension().isDebugChunkCrossSections() || (var1 >> 4) % this.getDimension().getDebugCrossSectionsMod() != 0 && (var2 >> 4) % this.getDimension().getDebugCrossSectionsMod() != 0) {
               this.mode.generate(var1, var2, var7, var4, var5);
            } else {
               for(int var8 = 0; var8 < 16; ++var8) {
                  for(int var9 = 0; var9 < 16; ++var9) {
                     var7.set(var8, 0, var9, Material.CRYING_OBSIDIAN.createBlockData());
                  }
               }
            }

            this.getMantle().getMantle().flag(var1 >> 4, var2 >> 4, MantleFlag.REAL, true);
            this.getMetrics().getTotal().put(var6.getMilliseconds());
            this.generated.incrementAndGet();
            if (this.generated.get() == 661) {
               J.a(() -> {
                  this.getData().savePrefetch(this);
               });
            }
         } catch (Throwable var10) {
            Iris.reportError(var10);
            this.fail("Failed to generate " + var1 + ", " + var2, var10);
         }

      }
   }

   public void saveEngineData() {
      File var1 = new File(this.getWorld().worldFolder(), "iris/engine-data/" + this.getDimension().getLoadKey() + ".json");
      var1.getParentFile().mkdirs();

      try {
         IO.writeAll(var1, (Object)(new Gson()).toJson(this.getEngineData()));
         Iris.debug("Saved Engine Data");
      } catch (IOException var3) {
         Iris.error("Failed to save Engine Data");
         var3.printStackTrace();
      }

   }

   public void blockUpdatedMetric() {
      this.bud.incrementAndGet();
   }

   public IrisBiome getFocus() {
      return this.getDimension().getFocus() != null && !this.getDimension().getFocus().trim().isEmpty() ? (IrisBiome)this.getData().getBiomeLoader().load(this.getDimension().getFocus()) : null;
   }

   public IrisRegion getFocusRegion() {
      return this.getDimension().getFocusRegion() != null && !this.getDimension().getFocusRegion().trim().isEmpty() ? (IrisRegion)this.getData().getRegionLoader().load(this.getDimension().getFocusRegion()) : null;
   }

   public void fail(String error, Throwable e) {
      this.failing = true;
      Iris.error(var1);
      var2.printStackTrace();
   }

   public boolean hasFailed() {
      return this.failing;
   }

   public int getCacheID() {
      return this.cacheId;
   }

   private boolean EngineSafe() {
      int var1 = this.getEngineData().getStatistics().getMCVersion();
      int var2 = this.getEngineData().getStatistics().getVersion();
      int var3 = Iris.instance.getMCVersion();
      int var4 = Iris.instance.getIrisVersion();
      if (var2 != var4) {
         return false;
      } else {
         return var1 == var3;
      }
   }

   @Generated
   public AtomicInteger getBud() {
      return this.bud;
   }

   @Generated
   public AtomicInteger getBuds() {
      return this.buds;
   }

   @Generated
   public AtomicInteger getGeneratedLast() {
      return this.generatedLast;
   }

   @Generated
   public AtomicDouble getPerSecond() {
      return this.perSecond;
   }

   @Generated
   public AtomicLong getLastGPS() {
      return this.lastGPS;
   }

   @Generated
   public EngineTarget getTarget() {
      return this.target;
   }

   @Generated
   public IrisContext getContext() {
      return this.context;
   }

   @Generated
   public EngineMantle getMantle() {
      return this.mantle;
   }

   @Generated
   public ChronoLatch getPerSecondLatch() {
      return this.perSecondLatch;
   }

   @Generated
   public ChronoLatch getPerSecondBudLatch() {
      return this.perSecondBudLatch;
   }

   @Generated
   public EngineMetrics getMetrics() {
      return this.metrics;
   }

   @Generated
   public AtomicRollingSequence getWallClock() {
      return this.wallClock;
   }

   @Generated
   public int getArt() {
      return this.art;
   }

   @Generated
   public AtomicBoolean getCleaning() {
      return this.cleaning;
   }

   @Generated
   public ChronoLatch getCleanLatch() {
      return this.cleanLatch;
   }

   @Generated
   public SeedManager getSeedManager() {
      return this.seedManager;
   }

   @Generated
   public CompletableFuture<Long> getHash32() {
      return this.hash32;
   }

   @Generated
   public EngineMode getMode() {
      return this.mode;
   }

   @Generated
   public EngineEffects getEffects() {
      return this.effects;
   }

   @Generated
   public EngineEnvironment getExecution() {
      return this.execution;
   }

   @Generated
   public EngineWorldManager getWorldManager() {
      return this.worldManager;
   }

   @Generated
   public int getParallelism() {
      return this.parallelism;
   }

   @Generated
   public boolean isFailing() {
      return this.failing;
   }

   @Generated
   public double getMaxBiomeObjectDensity() {
      return this.maxBiomeObjectDensity;
   }

   @Generated
   public double getMaxBiomeLayerDensity() {
      return this.maxBiomeLayerDensity;
   }

   @Generated
   public double getMaxBiomeDecoratorDensity() {
      return this.maxBiomeDecoratorDensity;
   }

   @Generated
   public IrisComplex getComplex() {
      return this.complex;
   }

   @Generated
   public void setHash32(final CompletableFuture<Long> hash32) {
      this.hash32 = var1;
   }

   @Generated
   public void setMode(final EngineMode mode) {
      this.mode = var1;
   }

   @Generated
   public void setEffects(final EngineEffects effects) {
      this.effects = var1;
   }

   @Generated
   public void setExecution(final EngineEnvironment execution) {
      this.execution = var1;
   }

   @Generated
   public void setWorldManager(final EngineWorldManager worldManager) {
      this.worldManager = var1;
   }

   @Generated
   public void setParallelism(final int parallelism) {
      this.parallelism = var1;
   }

   @Generated
   public void setFailing(final boolean failing) {
      this.failing = var1;
   }

   @Generated
   public void setClosed(final boolean closed) {
      this.closed = var1;
   }

   @Generated
   public void setCacheId(final int cacheId) {
      this.cacheId = var1;
   }

   @Generated
   public void setMaxBiomeObjectDensity(final double maxBiomeObjectDensity) {
      this.maxBiomeObjectDensity = var1;
   }

   @Generated
   public void setMaxBiomeLayerDensity(final double maxBiomeLayerDensity) {
      this.maxBiomeLayerDensity = var1;
   }

   @Generated
   public void setMaxBiomeDecoratorDensity(final double maxBiomeDecoratorDensity) {
      this.maxBiomeDecoratorDensity = var1;
   }

   @Generated
   public void setComplex(final IrisComplex complex) {
      this.complex = var1;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisEngine)) {
         return false;
      } else {
         IrisEngine var2 = (IrisEngine)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (this.isStudio() != var2.isStudio()) {
            return false;
         } else if (this.getArt() != var2.getArt()) {
            return false;
         } else if (this.getParallelism() != var2.getParallelism()) {
            return false;
         } else if (this.isFailing() != var2.isFailing()) {
            return false;
         } else if (this.isClosed() != var2.isClosed()) {
            return false;
         } else if (this.getCacheID() != var2.getCacheID()) {
            return false;
         } else if (Double.compare(this.getMaxBiomeObjectDensity(), var2.getMaxBiomeObjectDensity()) != 0) {
            return false;
         } else if (Double.compare(this.getMaxBiomeLayerDensity(), var2.getMaxBiomeLayerDensity()) != 0) {
            return false;
         } else if (Double.compare(this.getMaxBiomeDecoratorDensity(), var2.getMaxBiomeDecoratorDensity()) != 0) {
            return false;
         } else {
            label289: {
               AtomicInteger var3 = this.getBud();
               AtomicInteger var4 = var2.getBud();
               if (var3 == null) {
                  if (var4 == null) {
                     break label289;
                  }
               } else if (var3.equals(var4)) {
                  break label289;
               }

               return false;
            }

            AtomicInteger var5 = this.getBuds();
            AtomicInteger var6 = var2.getBuds();
            if (var5 == null) {
               if (var6 != null) {
                  return false;
               }
            } else if (!var5.equals(var6)) {
               return false;
            }

            if (this.getGenerated() != var2.getGenerated()) {
               return false;
            } else {
               AtomicInteger var7 = this.getGeneratedLast();
               AtomicInteger var8 = var2.getGeneratedLast();
               if (var7 == null) {
                  if (var8 != null) {
                     return false;
                  }
               } else if (!var7.equals(var8)) {
                  return false;
               }

               label267: {
                  AtomicDouble var9 = this.getPerSecond();
                  AtomicDouble var10 = var2.getPerSecond();
                  if (var9 == null) {
                     if (var10 == null) {
                        break label267;
                     }
                  } else if (var9.equals(var10)) {
                     break label267;
                  }

                  return false;
               }

               AtomicLong var11 = this.getLastGPS();
               AtomicLong var12 = var2.getLastGPS();
               if (var11 == null) {
                  if (var12 != null) {
                     return false;
                  }
               } else if (!var11.equals(var12)) {
                  return false;
               }

               label253: {
                  EngineTarget var13 = this.getTarget();
                  EngineTarget var14 = var2.getTarget();
                  if (var13 == null) {
                     if (var14 == null) {
                        break label253;
                     }
                  } else if (var13.equals(var14)) {
                     break label253;
                  }

                  return false;
               }

               label246: {
                  EngineMantle var15 = this.getMantle();
                  EngineMantle var16 = var2.getMantle();
                  if (var15 == null) {
                     if (var16 == null) {
                        break label246;
                     }
                  } else if (var15.equals(var16)) {
                     break label246;
                  }

                  return false;
               }

               ChronoLatch var17 = this.getPerSecondLatch();
               ChronoLatch var18 = var2.getPerSecondLatch();
               if (var17 == null) {
                  if (var18 != null) {
                     return false;
                  }
               } else if (!var17.equals(var18)) {
                  return false;
               }

               label232: {
                  ChronoLatch var19 = this.getPerSecondBudLatch();
                  ChronoLatch var20 = var2.getPerSecondBudLatch();
                  if (var19 == null) {
                     if (var20 == null) {
                        break label232;
                     }
                  } else if (var19.equals(var20)) {
                     break label232;
                  }

                  return false;
               }

               label225: {
                  EngineMetrics var21 = this.getMetrics();
                  EngineMetrics var22 = var2.getMetrics();
                  if (var21 == null) {
                     if (var22 == null) {
                        break label225;
                     }
                  } else if (var21.equals(var22)) {
                     break label225;
                  }

                  return false;
               }

               AtomicRollingSequence var23 = this.getWallClock();
               AtomicRollingSequence var24 = var2.getWallClock();
               if (var23 == null) {
                  if (var24 != null) {
                     return false;
                  }
               } else if (!var23.equals(var24)) {
                  return false;
               }

               IrisEngineData var25 = this.getEngineData();
               IrisEngineData var26 = var2.getEngineData();
               if (var25 == null) {
                  if (var26 != null) {
                     return false;
                  }
               } else if (!var25.equals(var26)) {
                  return false;
               }

               label204: {
                  AtomicBoolean var27 = this.getCleaning();
                  AtomicBoolean var28 = var2.getCleaning();
                  if (var27 == null) {
                     if (var28 == null) {
                        break label204;
                     }
                  } else if (var27.equals(var28)) {
                     break label204;
                  }

                  return false;
               }

               ChronoLatch var29 = this.getCleanLatch();
               ChronoLatch var30 = var2.getCleanLatch();
               if (var29 == null) {
                  if (var30 != null) {
                     return false;
                  }
               } else if (!var29.equals(var30)) {
                  return false;
               }

               SeedManager var31 = this.getSeedManager();
               SeedManager var32 = var2.getSeedManager();
               if (var31 == null) {
                  if (var32 != null) {
                     return false;
                  }
               } else if (!var31.equals(var32)) {
                  return false;
               }

               label183: {
                  CompletableFuture var33 = this.getHash32();
                  CompletableFuture var34 = var2.getHash32();
                  if (var33 == null) {
                     if (var34 == null) {
                        break label183;
                     }
                  } else if (var33.equals(var34)) {
                     break label183;
                  }

                  return false;
               }

               label176: {
                  EngineMode var35 = this.getMode();
                  EngineMode var36 = var2.getMode();
                  if (var35 == null) {
                     if (var36 == null) {
                        break label176;
                     }
                  } else if (var35.equals(var36)) {
                     break label176;
                  }

                  return false;
               }

               label169: {
                  EngineEffects var37 = this.getEffects();
                  EngineEffects var38 = var2.getEffects();
                  if (var37 == null) {
                     if (var38 == null) {
                        break label169;
                     }
                  } else if (var37.equals(var38)) {
                     break label169;
                  }

                  return false;
               }

               EngineEnvironment var39 = this.getExecution();
               EngineEnvironment var40 = var2.getExecution();
               if (var39 == null) {
                  if (var40 != null) {
                     return false;
                  }
               } else if (!var39.equals(var40)) {
                  return false;
               }

               label155: {
                  EngineWorldManager var41 = this.getWorldManager();
                  EngineWorldManager var42 = var2.getWorldManager();
                  if (var41 == null) {
                     if (var42 == null) {
                        break label155;
                     }
                  } else if (var41.equals(var42)) {
                     break label155;
                  }

                  return false;
               }

               IrisComplex var43 = this.getComplex();
               IrisComplex var44 = var2.getComplex();
               if (var43 == null) {
                  if (var44 != null) {
                     return false;
                  }
               } else if (!var43.equals(var44)) {
                  return false;
               }

               return true;
            }
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof IrisEngine;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      int var30 = var2 * 59 + (this.isStudio() ? 79 : 97);
      var30 = var30 * 59 + this.getArt();
      var30 = var30 * 59 + this.getParallelism();
      var30 = var30 * 59 + (this.isFailing() ? 79 : 97);
      var30 = var30 * 59 + (this.isClosed() ? 79 : 97);
      var30 = var30 * 59 + this.getCacheID();
      long var3 = Double.doubleToLongBits(this.getMaxBiomeObjectDensity());
      var30 = var30 * 59 + (int)(var3 >>> 32 ^ var3);
      long var5 = Double.doubleToLongBits(this.getMaxBiomeLayerDensity());
      var30 = var30 * 59 + (int)(var5 >>> 32 ^ var5);
      long var7 = Double.doubleToLongBits(this.getMaxBiomeDecoratorDensity());
      var30 = var30 * 59 + (int)(var7 >>> 32 ^ var7);
      AtomicInteger var9 = this.getBud();
      var30 = var30 * 59 + (var9 == null ? 43 : var9.hashCode());
      AtomicInteger var10 = this.getBuds();
      var30 = var30 * 59 + (var10 == null ? 43 : var10.hashCode());
      var30 = var30 * 59 + this.getGenerated();
      AtomicInteger var11 = this.getGeneratedLast();
      var30 = var30 * 59 + (var11 == null ? 43 : var11.hashCode());
      AtomicDouble var12 = this.getPerSecond();
      var30 = var30 * 59 + (var12 == null ? 43 : var12.hashCode());
      AtomicLong var13 = this.getLastGPS();
      var30 = var30 * 59 + (var13 == null ? 43 : var13.hashCode());
      EngineTarget var14 = this.getTarget();
      var30 = var30 * 59 + (var14 == null ? 43 : var14.hashCode());
      EngineMantle var15 = this.getMantle();
      var30 = var30 * 59 + (var15 == null ? 43 : var15.hashCode());
      ChronoLatch var16 = this.getPerSecondLatch();
      var30 = var30 * 59 + (var16 == null ? 43 : var16.hashCode());
      ChronoLatch var17 = this.getPerSecondBudLatch();
      var30 = var30 * 59 + (var17 == null ? 43 : var17.hashCode());
      EngineMetrics var18 = this.getMetrics();
      var30 = var30 * 59 + (var18 == null ? 43 : var18.hashCode());
      AtomicRollingSequence var19 = this.getWallClock();
      var30 = var30 * 59 + (var19 == null ? 43 : var19.hashCode());
      IrisEngineData var20 = this.getEngineData();
      var30 = var30 * 59 + (var20 == null ? 43 : var20.hashCode());
      AtomicBoolean var21 = this.getCleaning();
      var30 = var30 * 59 + (var21 == null ? 43 : var21.hashCode());
      ChronoLatch var22 = this.getCleanLatch();
      var30 = var30 * 59 + (var22 == null ? 43 : var22.hashCode());
      SeedManager var23 = this.getSeedManager();
      var30 = var30 * 59 + (var23 == null ? 43 : var23.hashCode());
      CompletableFuture var24 = this.getHash32();
      var30 = var30 * 59 + (var24 == null ? 43 : var24.hashCode());
      EngineMode var25 = this.getMode();
      var30 = var30 * 59 + (var25 == null ? 43 : var25.hashCode());
      EngineEffects var26 = this.getEffects();
      var30 = var30 * 59 + (var26 == null ? 43 : var26.hashCode());
      EngineEnvironment var27 = this.getExecution();
      var30 = var30 * 59 + (var27 == null ? 43 : var27.hashCode());
      EngineWorldManager var28 = this.getWorldManager();
      var30 = var30 * 59 + (var28 == null ? 43 : var28.hashCode());
      IrisComplex var29 = this.getComplex();
      var30 = var30 * 59 + (var29 == null ? 43 : var29.hashCode());
      return var30;
   }

   @Generated
   public String toString() {
      String var10000 = String.valueOf(this.getBud());
      return "IrisEngine(bud=" + var10000 + ", buds=" + String.valueOf(this.getBuds()) + ", generated=" + this.getGenerated() + ", generatedLast=" + String.valueOf(this.getGeneratedLast()) + ", perSecond=" + String.valueOf(this.getPerSecond()) + ", lastGPS=" + String.valueOf(this.getLastGPS()) + ", target=" + String.valueOf(this.getTarget()) + ", mantle=" + String.valueOf(this.getMantle()) + ", perSecondLatch=" + String.valueOf(this.getPerSecondLatch()) + ", perSecondBudLatch=" + String.valueOf(this.getPerSecondBudLatch()) + ", metrics=" + String.valueOf(this.getMetrics()) + ", studio=" + this.isStudio() + ", wallClock=" + String.valueOf(this.getWallClock()) + ", art=" + this.getArt() + ", engineData=" + String.valueOf(this.getEngineData()) + ", cleaning=" + String.valueOf(this.getCleaning()) + ", cleanLatch=" + String.valueOf(this.getCleanLatch()) + ", seedManager=" + String.valueOf(this.getSeedManager()) + ", hash32=" + String.valueOf(this.getHash32()) + ", mode=" + String.valueOf(this.getMode()) + ", effects=" + String.valueOf(this.getEffects()) + ", execution=" + String.valueOf(this.getExecution()) + ", worldManager=" + String.valueOf(this.getWorldManager()) + ", parallelism=" + this.getParallelism() + ", failing=" + this.isFailing() + ", closed=" + this.isClosed() + ", cacheId=" + this.getCacheID() + ", maxBiomeObjectDensity=" + this.getMaxBiomeObjectDensity() + ", maxBiomeLayerDensity=" + this.getMaxBiomeLayerDensity() + ", maxBiomeDecoratorDensity=" + this.getMaxBiomeDecoratorDensity() + ", complex=" + String.valueOf(this.getComplex()) + ")";
   }
}
