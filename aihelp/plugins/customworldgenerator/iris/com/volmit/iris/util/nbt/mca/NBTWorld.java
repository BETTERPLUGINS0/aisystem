package com.volmit.iris.util.nbt.mca;

import com.volmit.iris.Iris;
import com.volmit.iris.core.nms.INMS;
import com.volmit.iris.engine.data.cache.Cache;
import com.volmit.iris.util.collection.KMap;
import com.volmit.iris.util.data.B;
import com.volmit.iris.util.format.C;
import com.volmit.iris.util.math.M;
import com.volmit.iris.util.nbt.tag.CompoundTag;
import com.volmit.iris.util.nbt.tag.StringTag;
import com.volmit.iris.util.parallel.HyperLock;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Biome;
import org.bukkit.block.data.BlockData;

public class NBTWorld {
   private static final BlockData AIR = B.get("AIR");
   private static final Map<BlockData, CompoundTag> blockDataCache = new KMap();
   private static final Function<BlockData, CompoundTag> BLOCK_DATA_COMPUTE = (var0) -> {
      CompoundTag var1 = new CompoundTag();
      String var2 = var0.getAsString(true);
      NamespacedKey var3 = var0.getMaterial().getKey();
      String var10002 = var3.getNamespace();
      var1.putString("Name", var10002 + ":" + var3.getKey());
      if (var2.contains("[")) {
         String var4 = var2.split("\\Q[\\E")[1].replaceAll("\\Q]\\E", "");
         CompoundTag var5 = new CompoundTag();
         String[] var6;
         if (var4.contains(",")) {
            var6 = var4.split("\\Q,\\E");
            int var13 = var6.length;

            for(int var14 = 0; var14 < var13; ++var14) {
               String var9 = var6[var14];
               String[] var10 = var9.split("\\Q=\\E");
               String var11 = var10[0];
               String var12 = var10[1];
               var5.put(var11, new StringTag(var12));
            }
         } else {
            var6 = var4.split("\\Q=\\E");
            String var7 = var6[0];
            String var8 = var6[1];
            var5.put(var7, new StringTag(var8));
         }

         var1.put("Properties", var5);
      }

      return var1;
   };
   private static final Map<Biome, Integer> biomeIds = computeBiomeIDs();
   private final KMap<Long, MCAFile> loadedRegions;
   private final HyperLock hyperLock = new HyperLock();
   private final KMap<Long, Long> lastUse;
   private final File worldFolder;
   private final ExecutorService saveQueue;

   public NBTWorld(File worldFolder) {
      this.worldFolder = var1;
      this.loadedRegions = new KMap();
      this.lastUse = new KMap();
      this.saveQueue = Executors.newSingleThreadExecutor((var0) -> {
         Thread var1 = new Thread(var0);
         var1.setName("Iris MCA Writer");
         var1.setPriority(1);
         return var1;
      });
   }

   public static BlockData getBlockData(CompoundTag tag) {
      if (var0 == null) {
         return B.getAir();
      } else {
         StringBuilder var1 = new StringBuilder(var0.getString("Name"));
         if (var0.containsKey("Properties")) {
            CompoundTag var2 = var0.getCompoundTag("Properties");
            var1.append('[');
            Iterator var3 = var2.keySet().iterator();

            while(var3.hasNext()) {
               String var4 = (String)var3.next();
               var1.append(var4).append('=').append(var2.getString(var4)).append(',');
            }

            var1.deleteCharAt(var1.length() - 1).append(']');
         }

         BlockData var5 = B.getOrNull(var1.toString(), true);
         return var5 == null ? B.getAir() : var5;
      }
   }

   public static CompoundTag getCompound(BlockData bd) {
      return (CompoundTag)blockDataCache.computeIfAbsent(var0, BLOCK_DATA_COMPUTE);
   }

   private static Map<Biome, Integer> computeBiomeIDs() {
      KMap var0 = new KMap();
      Biome[] var1 = Biome.values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         Biome var4 = var1[var3];
         if (!var4.name().equals("CUSTOM")) {
            var0.put(var4, INMS.get().getBiomeId(var4));
         }
      }

      return var0;
   }

   public void close() {
      Iterator var1 = this.loadedRegions.k().iterator();

      while(var1.hasNext()) {
         Long var2 = (Long)var1.next();
         this.queueSaveUnload(Cache.keyX(var2), Cache.keyZ(var2));
      }

      this.saveQueue.shutdown();

      try {
         while(!this.saveQueue.awaitTermination(3L, TimeUnit.SECONDS)) {
            Iris.info("Still Waiting to save MCA Files...");
         }
      } catch (InterruptedException var3) {
         var3.printStackTrace();
      }

   }

   public void flushNow() {
      Iterator var1 = this.loadedRegions.k().iterator();

      while(var1.hasNext()) {
         Long var2 = (Long)var1.next();
         this.doSaveUnload(Cache.keyX(var2), Cache.keyZ(var2));
      }

   }

   public void queueSaveUnload(int x, int z) {
      this.saveQueue.submit(() -> {
         this.doSaveUnload(var1, var2);
      });
   }

   public void doSaveUnload(int x, int z) {
      MCAFile var3 = this.getMCAOrNull(var1, var2);
      if (var3 != null) {
         this.unloadRegion(var1, var2);
      }

      this.saveRegion(var1, var2, var3);
   }

   public void save() {
      boolean var1 = true;
      Iterator var2 = this.loadedRegions.k().iterator();

      while(var2.hasNext()) {
         Long var3 = (Long)var2.next();
         int var4 = Cache.keyX(var3);
         int var5 = Cache.keyZ(var3);
         if (!this.lastUse.containsKey(var3)) {
            this.lastUse.put(var3, M.ms());
         }

         if (this.shouldUnload(var4, var5)) {
            this.queueSaveUnload(var4, var5);
         }
      }

      String var10000 = String.valueOf(C.GOLD);
      Iris.debug("Regions: " + var10000 + this.loadedRegions.size() + String.valueOf(C.LIGHT_PURPLE));
   }

   public void queueSave() {
   }

   public synchronized void unloadRegion(int x, int z) {
      long var3 = Cache.key(var1, var2);
      this.loadedRegions.remove(var3);
      this.lastUse.remove(var3);
      Iris.debug("Unloaded Region " + String.valueOf(C.GOLD) + var1 + " " + var2);
   }

   public void saveRegion(int x, int z) {
      Cache.key(var1, var2);
      MCAFile var5 = this.getMCAOrNull(var1, var2);

      try {
         MCAUtil.write(var5, this.getRegionFile(var1, var2), true);
         Iris.debug("Saved Region " + String.valueOf(C.GOLD) + var1 + " " + var2);
      } catch (IOException var7) {
         Iris.error("Failed to save region " + this.getRegionFile(var1, var2).getPath());
         var7.printStackTrace();
      }

   }

   public void saveRegion(int x, int z, MCAFile mca) {
      try {
         MCAUtil.write(var3, this.getRegionFile(var1, var2), true);
         Iris.debug("Saved Region " + String.valueOf(C.GOLD) + var1 + " " + var2);
      } catch (IOException var5) {
         Iris.error("Failed to save region " + this.getRegionFile(var1, var2).getPath());
         var5.printStackTrace();
      }

   }

   public boolean shouldUnload(int x, int z) {
      return this.getIdleDuration(var1, var2) > 60000L;
   }

   public File getRegionFile(int x, int z) {
      return new File(this.worldFolder, "region/r." + var1 + "." + var2 + ".mca");
   }

   public BlockData getBlockData(int x, int y, int z) {
      try {
         CompoundTag var4 = this.getChunkSection(var1 >> 4, var2 >> 4, var3 >> 4).getBlockStateAt(var1 & 15, var2 & 15, var3 & 15);
         return var4 == null ? AIR : getBlockData(var4);
      } catch (Throwable var5) {
         Iris.reportError(var5);
         return AIR;
      }
   }

   public void setBlockData(int x, int y, int z, BlockData data) {
      this.getChunkSection(var1 >> 4, var2 >> 4, var3 >> 4).setBlockStateAt(var1 & 15, var2 & 15, var3 & 15, getCompound(var4), false);
   }

   public int getBiomeId(Biome b) {
      return (Integer)biomeIds.get(var1);
   }

   public void setBiome(int x, int y, int z, Biome biome) {
      this.getChunk(var1 >> 4, var3 >> 4).setBiomeAt(var1 & 15, var2, var3 & 15, (Integer)biomeIds.get(var4));
   }

   public Section getChunkSection(int x, int y, int z) {
      Chunk var4 = this.getChunk(var1, var3);
      Section var5 = var4.getSection(var2);
      if (var5 == null) {
         var5 = Section.newSection();
         var4.setSection(var2, var5);
      }

      return var5;
   }

   public Chunk getChunk(int x, int z) {
      return this.getChunk(this.getMCA(var1 >> 5, var2 >> 5), var1, var2);
   }

   public Chunk getChunk(MCAFile mca, int x, int z) {
      Chunk var4 = var1.getChunk(var2 & 31, var3 & 31);
      if (var4 == null) {
         var4 = Chunk.newChunk();
         var1.setChunk(var2 & 31, var3 & 31, var4);
      }

      return var4;
   }

   public Chunk getNewChunk(MCAFile mca, int x, int z) {
      Chunk var4 = Chunk.newChunk();
      var1.setChunk(var2 & 31, var3 & 31, var4);
      return var4;
   }

   public long getIdleDuration(int x, int z) {
      return (Long)this.hyperLock.withResult(var1, var2, () -> {
         Long var3 = (Long)this.lastUse.get(Cache.key(var1, var2));
         return var3 == null ? 0L : M.ms() - var3;
      });
   }

   public MCAFile getMCA(int x, int z) {
      long var3 = Cache.key(var1, var2);
      return (MCAFile)this.hyperLock.withResult(var1, var2, () -> {
         this.lastUse.put(var3, M.ms());
         MCAFile var5 = (MCAFile)this.loadedRegions.get(var3);
         if (var5 == null) {
            var5 = new MCAFile(var1, var2);
            this.loadedRegions.put(var3, var5);
         }

         return var5;
      });
   }

   public MCAFile getMCAOrNull(int x, int z) {
      long var3 = Cache.key(var1, var2);
      return (MCAFile)this.hyperLock.withResult(var1, var2, () -> {
         if (this.loadedRegions.containsKey(var3)) {
            this.lastUse.put(var3, M.ms());
            return (MCAFile)this.loadedRegions.get(var3);
         } else {
            return null;
         }
      });
   }

   public int size() {
      return this.loadedRegions.size();
   }

   public boolean isLoaded(int x, int z) {
      return this.loadedRegions.containsKey(Cache.key(var1, var2));
   }
}
