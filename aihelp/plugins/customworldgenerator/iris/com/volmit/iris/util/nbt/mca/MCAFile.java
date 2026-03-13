package com.volmit.iris.util.nbt.mca;

import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.math.Position2;
import com.volmit.iris.util.nbt.tag.CompoundTag;
import com.volmit.iris.util.scheduling.J;
import java.io.RandomAccessFile;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicReferenceArray;

public class MCAFile {
   public static final int DEFAULT_DATA_VERSION = 1628;
   private final int regionX;
   private final int regionZ;
   private AtomicReferenceArray<Chunk> chunks;
   private ConcurrentLinkedQueue<Runnable> afterSave;

   public MCAFile(int regionX, int regionZ) {
      this.regionX = var1;
      this.regionZ = var2;
      this.afterSave = new ConcurrentLinkedQueue();
   }

   public static int getChunkIndex(int chunkX, int chunkZ) {
      return (var0 & 31) + (var1 & 31) * 32;
   }

   public void deserialize(RandomAccessFile raf) {
      this.deserialize(var1, -1L);
   }

   public void deserialize(RandomAccessFile raf, long loadFlags) {
      this.chunks = new AtomicReferenceArray(1024);

      for(int var4 = 0; var4 < 1024; ++var4) {
         var1.seek((long)(var4 * 4));
         int var5 = var1.read() << 16;
         var5 |= (var1.read() & 255) << 8;
         var5 |= var1.read() & 255;
         if (var1.readByte() != 0) {
            var1.seek((long)(4096 + var4 * 4));
            int var6 = var1.readInt();
            Chunk var7 = new Chunk(var6);
            var1.seek(4096L * (long)var5 + 4L);
            var7.deserialize(var1, var2);
            this.chunks.set(var4, var7);
         }
      }

   }

   public KList<Position2> samplePositions(RandomAccessFile raf) {
      KList var2 = new KList();
      this.chunks = new AtomicReferenceArray(1024);
      int var3 = 0;
      int var4 = 0;

      for(int var5 = 0; var5 < 1024; ++var5) {
         ++var3;
         ++var4;
         var1.seek((long)(var5 * 4));
         int var6 = var1.read() << 16;
         var6 |= (var1.read() & 255) << 8;
         int var10000 = var6 | var1.read() & 255;
         if (var1.readByte() != 0) {
            var2.add((Object)(new Position2(var3 & 31, var4 / 31 & 31)));
         }
      }

      return var2;
   }

   public AtomicReferenceArray<Chunk> getChunks() {
      return this.chunks;
   }

   public int serialize(RandomAccessFile raf) {
      return this.serialize(var1, false);
   }

   public int serialize(RandomAccessFile raf, boolean changeLastUpdate) {
      int var3 = 2;
      int var4 = 0;
      int var5 = (int)(System.currentTimeMillis() / 1000L);
      int var6 = 0;
      int var7 = MCAUtil.regionToChunk(this.regionX);
      int var8 = MCAUtil.regionToChunk(this.regionZ);
      if (this.chunks == null) {
         return 0;
      } else {
         for(int var9 = 0; var9 < 32; ++var9) {
            for(int var10 = 0; var10 < 32; ++var10) {
               int var11 = getChunkIndex(var9, var10);
               Chunk var12 = (Chunk)this.chunks.get(var11);
               if (var12 != null) {
                  var1.seek(4096L * (long)var3);
                  var4 = var12.serialize(var1, var7 + var9, var8 + var10);
                  if (var4 != 0) {
                     ++var6;
                     int var13 = (var4 >> 12) + (var4 % 4096 == 0 ? 0 : 1);
                     var1.seek((long)var11 * 4L);
                     var1.writeByte(var3 >>> 16);
                     var1.writeByte(var3 >> 8 & 255);
                     var1.writeByte(var3 & 255);
                     var1.writeByte(var13);
                     var1.seek((long)var11 * 4L + 4096L);
                     var1.writeInt(var2 ? var5 : var12.getLastMCAUpdate());
                     var3 += var13;
                  }
               }
            }
         }

         if (var4 % 4096 != 0) {
            var1.seek((long)var3 * 4096L - 1L);
            var1.write(0);
         }

         J.a(() -> {
            this.afterSave.forEach((var0) -> {
               var0.run();
            });
         }, 20);
         return var6;
      }
   }

   public void setChunk(int index, Chunk chunk) {
      this.checkIndex(var1);
      if (this.chunks == null) {
         this.chunks = new AtomicReferenceArray(1024);
      }

      this.chunks.set(var1, var2);
   }

   public void setChunk(int chunkX, int chunkZ, Chunk chunk) {
      this.setChunk(getChunkIndex(var1, var2), var3);
   }

   public Chunk getChunk(int index) {
      this.checkIndex(var1);
      return this.chunks == null ? null : (Chunk)this.chunks.get(var1);
   }

   public Chunk getChunk(int chunkX, int chunkZ) {
      return this.getChunk(getChunkIndex(var1, var2));
   }

   public boolean hasChunk(int chunkX, int chunkZ) {
      return this.getChunk(var1, var2) != null;
   }

   private int checkIndex(int index) {
      if (var1 >= 0 && var1 <= 1023) {
         return var1;
      } else {
         throw new IndexOutOfBoundsException();
      }
   }

   private Chunk createChunkIfMissing(int blockX, int blockZ) {
      int var3 = MCAUtil.blockToChunk(var1);
      int var4 = MCAUtil.blockToChunk(var2);
      Chunk var5 = this.getChunk(var3, var4);
      if (var5 == null) {
         var5 = Chunk.newChunk();
         this.setChunk(getChunkIndex(var3, var4), var5);
      }

      return var5;
   }

   public void setBiomeAt(int blockX, int blockY, int blockZ, int biomeID) {
      this.createChunkIfMissing(var1, var3).setBiomeAt(var1, var2, var3, var4);
   }

   public int getBiomeAt(int blockX, int blockY, int blockZ) {
      int var4 = MCAUtil.blockToChunk(var1);
      int var5 = MCAUtil.blockToChunk(var3);
      Chunk var6 = this.getChunk(getChunkIndex(var4, var5));
      return var6 == null ? -1 : var6.getBiomeAt(var1, var2, var3);
   }

   public void setBlockStateAt(int blockX, int blockY, int blockZ, CompoundTag state, boolean cleanup) {
      this.createChunkIfMissing(var1, var3).setBlockStateAt(var1, var2, var3, var4, var5);
   }

   public CompoundTag getBlockStateAt(int blockX, int blockY, int blockZ) {
      int var4 = MCAUtil.blockToChunk(var1);
      int var5 = MCAUtil.blockToChunk(var3);
      Chunk var6 = this.getChunk(var4, var5);
      return var6 == null ? null : var6.getBlockStateAt(var1, var2, var3);
   }

   public void afterSave(Runnable o) {
      this.afterSave.add(var1);
   }
}
