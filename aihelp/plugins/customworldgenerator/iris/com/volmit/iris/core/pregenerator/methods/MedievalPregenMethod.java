package com.volmit.iris.core.pregenerator.methods;

import com.volmit.iris.Iris;
import com.volmit.iris.core.IrisSettings;
import com.volmit.iris.core.pregenerator.PregenListener;
import com.volmit.iris.core.pregenerator.PregeneratorMethod;
import com.volmit.iris.core.tools.IrisToolbelt;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.collection.KMap;
import com.volmit.iris.util.mantle.Mantle;
import com.volmit.iris.util.math.M;
import com.volmit.iris.util.scheduling.J;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;

public class MedievalPregenMethod implements PregeneratorMethod {
   private final World world;
   private final KList<CompletableFuture<?>> futures;
   private final Map<Chunk, Long> lastUse;

   public MedievalPregenMethod(World world) {
      this.world = var1;
      this.futures = new KList();
      this.lastUse = new KMap();
   }

   private void waitForChunks() {
      Iterator var1 = this.futures.iterator();

      while(var1.hasNext()) {
         CompletableFuture var2 = (CompletableFuture)var1.next();

         try {
            var2.get();
         } catch (Throwable var4) {
            var4.printStackTrace();
         }
      }

      this.futures.clear();
   }

   private void unloadAndSaveAllChunks() {
      try {
         J.sfut(() -> {
            if (this.world == null) {
               Iris.warn("World was null somehow...");
            } else {
               Iterator var1 = (new ArrayList(this.lastUse.keySet())).iterator();

               while(var1.hasNext()) {
                  Chunk var2 = (Chunk)var1.next();
                  Long var3 = (Long)this.lastUse.get(var2);
                  if (var3 != null && M.ms() - var3 >= 10L) {
                     var2.unload();
                     this.lastUse.remove(var2);
                  }
               }

               this.world.save();
            }
         }).get();
      } catch (Throwable var2) {
         var2.printStackTrace();
      }

   }

   public void init() {
      this.unloadAndSaveAllChunks();
   }

   public void close() {
      this.unloadAndSaveAllChunks();
   }

   public void save() {
      this.unloadAndSaveAllChunks();
   }

   public boolean supportsRegions(int x, int z, PregenListener listener) {
      return false;
   }

   public void generateRegion(int x, int z, PregenListener listener) {
      throw new UnsupportedOperationException();
   }

   public String getMethod(int x, int z) {
      return "Medieval";
   }

   public void generateChunk(int x, int z, PregenListener listener) {
      if (this.futures.size() > IrisSettings.getThreadCount(IrisSettings.get().getConcurrency().getParallelism())) {
         this.waitForChunks();
      }

      var3.onChunkGenerating(var1, var2);
      this.futures.add((Object)J.sfut(() -> {
         this.world.getChunkAt(var1, var2);
         Chunk var4 = Bukkit.getWorld(this.world.getUID()).getChunkAt(var1, var2);
         this.lastUse.put(var4, M.ms());
         var3.onChunkGenerated(var1, var2);
         var3.onChunkCleaned(var1, var2);
      }));
   }

   public Mantle getMantle() {
      return IrisToolbelt.isIrisWorld(this.world) ? IrisToolbelt.access(this.world).getEngine().getMantle().getMantle() : null;
   }
}
