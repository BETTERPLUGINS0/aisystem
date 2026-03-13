package com.volmit.iris.core.service;

import com.google.common.util.concurrent.AtomicDouble;
import com.volmit.iris.Iris;
import com.volmit.iris.core.IrisSettings;
import com.volmit.iris.core.loader.ResourceLoader;
import com.volmit.iris.core.tools.IrisToolbelt;
import com.volmit.iris.engine.framework.Engine;
import com.volmit.iris.engine.framework.MeteredCache;
import com.volmit.iris.engine.platform.PlatformChunkGenerator;
import com.volmit.iris.util.collection.KMap;
import com.volmit.iris.util.format.C;
import com.volmit.iris.util.format.Form;
import com.volmit.iris.util.math.RNG;
import com.volmit.iris.util.plugin.IrisService;
import com.volmit.iris.util.plugin.VolmitSender;
import com.volmit.iris.util.scheduling.Looper;
import com.volmit.iris.util.stream.utility.CachedStream2D;
import com.volmit.iris.util.stream.utility.CachedStream3D;
import java.lang.Thread.Builder;
import java.util.Iterator;
import java.util.Objects;
import java.util.Map.Entry;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import lombok.Generated;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;
import org.jetbrains.annotations.Nullable;

public class IrisEngineSVC implements IrisService {
   private static final int TRIM_PERIOD = 2000;
   private final AtomicInteger tectonicLimit = new AtomicInteger(30);
   private final AtomicInteger tectonicPlates = new AtomicInteger();
   private final AtomicInteger queuedTectonicPlates = new AtomicInteger();
   private final AtomicInteger trimmerAlive = new AtomicInteger();
   private final AtomicInteger unloaderAlive = new AtomicInteger();
   private final AtomicInteger totalWorlds = new AtomicInteger();
   private final AtomicDouble maxIdleDuration = new AtomicDouble();
   private final AtomicDouble minIdleDuration = new AtomicDouble();
   private final AtomicLong loadedChunks = new AtomicLong();
   private final KMap<World, IrisEngineSVC.Registered> worlds = new KMap();
   private ScheduledExecutorService service;
   private Looper updateTicker;

   public void onEnable() {
      IrisSettings.IrisSettingsPerformance var1 = IrisSettings.get().getPerformance();
      IrisSettings.IrisSettingsEngineSVC var2 = var1.getEngineSVC();
      this.service = Executors.newScheduledThreadPool(0, ((Builder)(var2.isUseVirtualThreads() ? Thread.ofVirtual() : Thread.ofPlatform().priority(var2.getPriority()))).name("Iris EngineSVC-", 0L).factory());
      this.tectonicLimit.set(var1.getTectonicPlateSize());
      Bukkit.getWorlds().forEach(this::add);
      this.setup();
   }

   public void onDisable() {
      this.service.shutdown();
      this.updateTicker.interrupt();
      this.worlds.keySet().forEach(this::remove);
      this.worlds.clear();
   }

   public void engineStatus(VolmitSender sender) {
      long[] var2 = new long[4];
      long[] var3 = new long[4];

      byte var6;
      int var10002;
      for(Iterator var4 = ((PreservationSVC)Iris.service(PreservationSVC.class)).getCaches().iterator(); var4.hasNext(); var10002 = var3[var6]++) {
         MeteredCache var5 = (MeteredCache)var4.next();
         Objects.requireNonNull(var5);
         byte var8 = 0;
         byte var10000;
         switch(var5.typeSwitch<invokedynamic>(var5, var8)) {
         case 0:
            ResourceLoader var9 = (ResourceLoader)var5;
            var10000 = 0;
            break;
         case 1:
            CachedStream2D var10 = (CachedStream2D)var5;
            var10000 = 1;
            break;
         case 2:
            CachedStream3D var11 = (CachedStream3D)var5;
            var10000 = 2;
            break;
         default:
            var10000 = 3;
         }

         var6 = var10000;
         var2[var6] += var5.getSize();
      }

      String var10001 = String.valueOf(C.DARK_PURPLE);
      var1.sendMessage(var10001 + "-------------------------");
      var10001 = String.valueOf(C.DARK_PURPLE);
      var1.sendMessage(var10001 + "Status:");
      var10001 = String.valueOf(C.DARK_PURPLE);
      var1.sendMessage(var10001 + "- Service: " + String.valueOf(C.LIGHT_PURPLE) + (this.service.isShutdown() ? "Shutdown" : "Running"));
      var10001 = String.valueOf(C.DARK_PURPLE);
      var1.sendMessage(var10001 + "- Updater: " + String.valueOf(C.LIGHT_PURPLE) + (this.updateTicker.isAlive() ? "Running" : "Stopped"));
      var10001 = String.valueOf(C.DARK_PURPLE);
      var1.sendMessage(var10001 + "- Period: " + String.valueOf(C.LIGHT_PURPLE) + Form.duration(2000L));
      var10001 = String.valueOf(C.DARK_PURPLE);
      var1.sendMessage(var10001 + "- Trimmers: " + String.valueOf(C.LIGHT_PURPLE) + this.trimmerAlive.get());
      var10001 = String.valueOf(C.DARK_PURPLE);
      var1.sendMessage(var10001 + "- Unloaders: " + String.valueOf(C.LIGHT_PURPLE) + this.unloaderAlive.get());
      var1.sendMessage(String.valueOf(C.DARK_PURPLE) + "Tectonic Plates:");
      var10001 = String.valueOf(C.DARK_PURPLE);
      var1.sendMessage(var10001 + "- Limit: " + String.valueOf(C.LIGHT_PURPLE) + this.tectonicLimit.get());
      var10001 = String.valueOf(C.DARK_PURPLE);
      var1.sendMessage(var10001 + "- Total: " + String.valueOf(C.LIGHT_PURPLE) + this.tectonicPlates.get());
      var10001 = String.valueOf(C.DARK_PURPLE);
      var1.sendMessage(var10001 + "- Queued: " + String.valueOf(C.LIGHT_PURPLE) + this.queuedTectonicPlates.get());
      var10001 = String.valueOf(C.DARK_PURPLE);
      var1.sendMessage(var10001 + "- Max Idle Duration: " + String.valueOf(C.LIGHT_PURPLE) + Form.duration(this.maxIdleDuration.get(), 2));
      var10001 = String.valueOf(C.DARK_PURPLE);
      var1.sendMessage(var10001 + "- Min Idle Duration: " + String.valueOf(C.LIGHT_PURPLE) + Form.duration(this.minIdleDuration.get(), 2));
      var1.sendMessage(String.valueOf(C.DARK_PURPLE) + "Caches:");
      var10001 = String.valueOf(C.DARK_PURPLE);
      var1.sendMessage(var10001 + "- Resource: " + String.valueOf(C.LIGHT_PURPLE) + var2[0] + " (" + var3[0] + ")");
      var10001 = String.valueOf(C.DARK_PURPLE);
      var1.sendMessage(var10001 + "- 2D Stream: " + String.valueOf(C.LIGHT_PURPLE) + var2[1] + " (" + var3[1] + ")");
      var10001 = String.valueOf(C.DARK_PURPLE);
      var1.sendMessage(var10001 + "- 3D Stream: " + String.valueOf(C.LIGHT_PURPLE) + var2[2] + " (" + var3[2] + ")");
      var10001 = String.valueOf(C.DARK_PURPLE);
      var1.sendMessage(var10001 + "- Other: " + String.valueOf(C.LIGHT_PURPLE) + var2[3] + " (" + var3[3] + ")");
      var1.sendMessage(String.valueOf(C.DARK_PURPLE) + "Other:");
      var10001 = String.valueOf(C.DARK_PURPLE);
      var1.sendMessage(var10001 + "- Iris Worlds: " + String.valueOf(C.LIGHT_PURPLE) + this.totalWorlds.get());
      var10001 = String.valueOf(C.DARK_PURPLE);
      var1.sendMessage(var10001 + "- Loaded Chunks: " + String.valueOf(C.LIGHT_PURPLE) + this.loadedChunks.get());
      var1.sendMessage(String.valueOf(C.DARK_PURPLE) + "-------------------------");
   }

   @EventHandler
   public void onWorldUnload(WorldUnloadEvent event) {
      this.remove(var1.getWorld());
   }

   @EventHandler
   public void onWorldLoad(WorldLoadEvent event) {
      this.add(var1.getWorld());
   }

   private void remove(World world) {
      IrisEngineSVC.Registered var2 = (IrisEngineSVC.Registered)this.worlds.remove(var1);
      if (var2 != null) {
         var2.close();
      }
   }

   private void add(World world) {
      PlatformChunkGenerator var2 = IrisToolbelt.access(var1);
      if (var2 != null) {
         this.worlds.put(var1, new IrisEngineSVC.Registered(var1.getName(), var2));
      }
   }

   private synchronized void setup() {
      if (this.updateTicker == null || !this.updateTicker.isAlive()) {
         this.updateTicker = new Looper() {
            protected long loop() {
               try {
                  int var1 = 0;
                  int var2 = 0;
                  long var3 = 0L;
                  int var5 = 0;
                  int var6 = 0;
                  int var7 = 0;
                  double var8 = -9.223372036854776E18D;
                  double var10 = 9.223372036854776E18D;
                  Iterator var12 = IrisEngineSVC.this.worlds.entrySet().iterator();

                  while(var12.hasNext()) {
                     Entry var13 = (Entry)var12.next();
                     IrisEngineSVC.Registered var14 = (IrisEngineSVC.Registered)var13.getValue();
                     if (!var14.closed) {
                        ++var7;
                        if (var14.unloaderAlive()) {
                           ++var5;
                        }

                        if (var14.trimmerAlive()) {
                           ++var6;
                        }

                        Engine var15 = var14.getEngine();
                        if (var15 != null) {
                           var1 += var15.getMantle().getUnloadRegionCount();
                           var2 += var15.getMantle().getLoadedRegionCount();
                           var3 += (long)((World)var13.getKey()).getLoadedChunks().length;
                           double var16 = var15.getMantle().getAdjustedIdleDuration();
                           if (var16 > var8) {
                              var8 = var16;
                           }

                           if (var16 < var10) {
                              var10 = var16;
                           }
                        }
                     }
                  }

                  IrisEngineSVC.this.trimmerAlive.set(var6);
                  IrisEngineSVC.this.unloaderAlive.set(var5);
                  IrisEngineSVC.this.tectonicPlates.set(var2);
                  IrisEngineSVC.this.queuedTectonicPlates.set(var1);
                  IrisEngineSVC.this.maxIdleDuration.set(var8);
                  IrisEngineSVC.this.minIdleDuration.set(var10);
                  IrisEngineSVC.this.loadedChunks.set(var3);
                  IrisEngineSVC.this.totalWorlds.set(var7);
                  IrisEngineSVC.this.worlds.values().forEach(IrisEngineSVC.Registered::update);
               } catch (Throwable var18) {
                  var18.printStackTrace();
               }

               return 1000L;
            }
         };
         this.updateTicker.start();
      }
   }

   private final class Registered {
      @Generated
      private final Object $lock = new Object[0];
      private final String name;
      private final PlatformChunkGenerator access;
      private final int offset;
      private transient ScheduledFuture<?> trimmer;
      private transient ScheduledFuture<?> unloader;
      private transient boolean closed;

      private Registered(String name, PlatformChunkGenerator access) {
         this.offset = RNG.r.nextInt(2000);
         this.name = var2;
         this.access = var3;
         this.update();
      }

      private boolean unloaderAlive() {
         return this.unloader != null && !this.unloader.isDone() && !this.unloader.isCancelled();
      }

      private boolean trimmerAlive() {
         return this.trimmer != null && !this.trimmer.isDone() && !this.trimmer.isCancelled();
      }

      private void update() {
         synchronized(this.$lock) {
            if (!this.closed && IrisEngineSVC.this.service != null && !IrisEngineSVC.this.service.isShutdown()) {
               if (this.trimmer == null || this.trimmer.isDone() || this.trimmer.isCancelled()) {
                  this.trimmer = IrisEngineSVC.this.service.scheduleAtFixedRate(() -> {
                     Engine var1 = this.getEngine();
                     if (var1 != null && var1.getMantle().getMantle().shouldReduce(var1)) {
                        try {
                           var1.getMantle().trim(this.tectonicLimit());
                        } catch (Throwable var3) {
                           Iris.reportError(var3);
                           Iris.error("EngineSVC: Failed to trim for " + this.name);
                           var3.printStackTrace();
                        }

                     }
                  }, (long)this.offset, 2000L, TimeUnit.MILLISECONDS);
               }

               if (this.unloader == null || this.unloader.isDone() || this.unloader.isCancelled()) {
                  this.unloader = IrisEngineSVC.this.service.scheduleAtFixedRate(() -> {
                     Engine var1 = this.getEngine();
                     if (var1 != null && var1.getMantle().getMantle().shouldReduce(var1)) {
                        try {
                           long var2 = System.currentTimeMillis();
                           int var4 = var1.getMantle().unloadTectonicPlate(IrisSettings.get().getPerformance().getEngineSVC().forceMulticoreWrite ? 0 : this.tectonicLimit());
                           if (var4 > 0) {
                              String var10000 = String.valueOf(C.GOLD);
                              Iris.debug(var10000 + "Unloaded " + String.valueOf(C.YELLOW) + var4 + " TectonicPlates in " + String.valueOf(C.RED) + Form.duration(System.currentTimeMillis() - var2, 2));
                           }
                        } catch (Throwable var5) {
                           Iris.reportError(var5);
                           Iris.error("EngineSVC: Failed to unload for " + this.name);
                           var5.printStackTrace();
                        }

                     }
                  }, (long)(this.offset + 1000), 2000L, TimeUnit.MILLISECONDS);
               }

            }
         }
      }

      private int tectonicLimit() {
         return IrisEngineSVC.this.tectonicLimit.get() / Math.max(IrisEngineSVC.this.worlds.size(), 1);
      }

      private void close() {
         synchronized(this.$lock) {
            if (!this.closed) {
               this.closed = true;
               if (this.trimmer != null) {
                  this.trimmer.cancel(false);
                  this.trimmer = null;
               }

               if (this.unloader != null) {
                  this.unloader.cancel(false);
                  this.unloader = null;
               }

            }
         }
      }

      @Nullable
      private Engine getEngine() {
         return this.closed ? null : this.access.getEngine();
      }
   }
}
