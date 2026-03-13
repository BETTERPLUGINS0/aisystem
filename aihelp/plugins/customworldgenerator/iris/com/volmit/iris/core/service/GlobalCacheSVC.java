package com.volmit.iris.core.service;

import com.volmit.iris.core.IrisSettings;
import com.volmit.iris.core.pregenerator.cache.PregenCache;
import com.volmit.iris.core.tools.IrisToolbelt;
import com.volmit.iris.util.collection.KMap;
import com.volmit.iris.util.plugin.IrisService;
import com.volmit.iris.util.scheduling.Looper;
import java.io.File;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.function.Function;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.event.world.WorldUnloadEvent;
import org.jetbrains.annotations.Nullable;

public class GlobalCacheSVC implements IrisService {
   private static final KMap<String, Reference<PregenCache>> REFERENCE_CACHE = new KMap();
   private final KMap<String, PregenCache> globalCache = new KMap();
   private transient boolean lastState;
   private static boolean disabled = true;
   private Looper trimmer;

   public void onEnable() {
      disabled = false;
      this.trimmer = new Looper(this) {
         protected long loop() {
            Iterator var1 = GlobalCacheSVC.REFERENCE_CACHE.values().iterator();

            while(var1.hasNext()) {
               PregenCache var2 = (PregenCache)((Reference)var1.next()).get();
               if (var2 == null) {
                  var1.remove();
               } else {
                  var2.trim(10000L);
               }
            }

            return GlobalCacheSVC.disabled ? -1L : 2000L;
         }
      };
      this.trimmer.start();
      this.lastState = !IrisSettings.get().getWorld().isGlobalPregenCache();
      if (!this.lastState) {
         Bukkit.getWorlds().forEach(this::createCache);
      }
   }

   public void onDisable() {
      disabled = true;

      try {
         this.trimmer.join();
      } catch (InterruptedException var2) {
      }

      this.globalCache.qclear((var0, var1) -> {
         var1.write();
      });
   }

   @Nullable
   public PregenCache get(@NonNull World world) {
      if (var1 == null) {
         throw new NullPointerException("world is marked non-null but is null");
      } else {
         return (PregenCache)this.globalCache.get(var1.getName());
      }
   }

   @Nullable
   public PregenCache get(@NonNull String world) {
      if (var1 == null) {
         throw new NullPointerException("world is marked non-null but is null");
      } else {
         return (PregenCache)this.globalCache.get(var1);
      }
   }

   @EventHandler(
      priority = EventPriority.MONITOR
   )
   public void on(WorldInitEvent event) {
      if (!this.isDisabled()) {
         this.createCache(var1.getWorld());
      }
   }

   @EventHandler(
      priority = EventPriority.MONITOR
   )
   public void on(WorldUnloadEvent event) {
      PregenCache var2 = (PregenCache)this.globalCache.remove(var1.getWorld().getName());
      if (var2 != null) {
         var2.write();
      }
   }

   @EventHandler(
      priority = EventPriority.MONITOR
   )
   public void on(ChunkLoadEvent event) {
      PregenCache var2 = this.get(var1.getWorld());
      if (var2 != null) {
         var2.cacheChunk(var1.getChunk().getX(), var1.getChunk().getZ());
      }
   }

   private void createCache(World world) {
      if (IrisToolbelt.isIrisWorld(var1)) {
         this.globalCache.computeIfAbsent(var1.getName(), GlobalCacheSVC::createDefault);
      }
   }

   private boolean isDisabled() {
      boolean var1 = IrisSettings.get().getWorld().isGlobalPregenCache();
      if (this.lastState != var1) {
         return this.lastState;
      } else {
         if (var1) {
            Bukkit.getWorlds().forEach(this::createCache);
         } else {
            this.globalCache.values().removeIf((var0) -> {
               var0.write();
               return true;
            });
         }

         return this.lastState = !var1;
      }
   }

   @NonNull
   public static PregenCache createCache(@NonNull String worldName, @NonNull Function<String, PregenCache> provider) {
      if (var0 == null) {
         throw new NullPointerException("worldName is marked non-null but is null");
      } else if (var1 == null) {
         throw new NullPointerException("provider is marked non-null but is null");
      } else {
         PregenCache[] var2 = new PregenCache[1];
         REFERENCE_CACHE.compute(var0, (var3, var4) -> {
            return (Reference)(var4 != null && (var2[0] = (PregenCache)var4.get()) != null ? var4 : new WeakReference(var2[0] = (PregenCache)var1.apply(var0)));
         });
         return var2[0];
      }
   }

   @NonNull
   public static PregenCache createDefault(@NonNull String worldName) {
      if (var0 == null) {
         throw new NullPointerException("worldName is marked non-null but is null");
      } else {
         return createCache(var0, GlobalCacheSVC::createDefault0);
      }
   }

   private static PregenCache createDefault0(String worldName) {
      return disabled ? PregenCache.EMPTY : PregenCache.create(new File(Bukkit.getWorldContainer(), String.join(File.separator, var0, "iris", "pregen"))).sync();
   }
}
