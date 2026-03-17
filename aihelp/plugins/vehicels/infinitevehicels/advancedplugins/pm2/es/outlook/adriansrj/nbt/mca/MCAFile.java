package es.outlook.adriansrj.nbt.mca;

import es.outlook.adriansrj.nbt.nbt.tag.CompoundTag;
import java.io.RandomAccessFile;

public class MCAFile {
   public static final int DEFAULT_DATA_VERSION = 1628;
   private int regionX;
   private int regionZ;
   private Chunk[] chunks;

   public MCAFile(int var1, int var2) {
      this.regionX = var1;
      this.regionZ = var2;
   }

   public void deserialize(RandomAccessFile var1) {
      this.deserialize(var1, -1L);
   }

   public void deserialize(RandomAccessFile var1, long var2) {
      this.chunks = new Chunk[1024];

      for(int var4 = 0; var4 < 1024; ++var4) {
         var1.seek((long)(var4 * 4));
         int var5 = var1.read() << 16;
         var5 |= (var1.read() & 255) << 8;
         var5 |= var1.read() & 255;
         if (var1.readByte() != 0) {
            var1.seek((long)(4096 + var4 * 4));
            int var6 = var1.readInt();
            Chunk var7 = new Chunk(var6);
            var1.seek((long)(4096 * var5 + 4));
            var7.deserialize(var1, var2);
            this.chunks[var4] = var7;
         }
      }

   }

   public int serialize(RandomAccessFile var1) {
      return this.serialize(var1, false);
   }

   public int serialize(RandomAccessFile var1, boolean var2) {
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
               Chunk var12 = this.chunks[var11];
               if (var12 != null) {
                  var1.seek((long)(4096 * var3));
                  var4 = var12.serialize(var1, var7 + var9, var8 + var10);
                  if (var4 != 0) {
                     ++var6;
                     int var13 = (var4 >> 12) + (var4 % 4096 == 0 ? 0 : 1);
                     var1.seek((long)(var11 * 4));
                     var1.writeByte(var3 >>> 16);
                     var1.writeByte(var3 >> 8 & 255);
                     var1.writeByte(var3 & 255);
                     var1.writeByte(var13);
                     var1.seek((long)(var11 * 4 + 4096));
                     var1.writeInt(var2 ? var5 : var12.getLastMCAUpdate());
                     var3 += var13;
                  }
               }
            }
         }

         if (var4 % 4096 != 0) {
            var1.seek((long)(var3 * 4096 - 1));
            var1.write(0);
         }

         return var6;
      }
   }

   public void setChunk(int var1, Chunk var2) {
      this.checkIndex(var1);
      if (this.chunks == null) {
         this.chunks = new Chunk[1024];
      }

      this.chunks[var1] = var2;
   }

   public void setChunk(int var1, int var2, Chunk var3) {
      this.setChunk(getChunkIndex(var1, var2), var3);
   }

   public Chunk getChunk(int var1) {
      this.checkIndex(var1);
      return this.chunks == null ? null : this.chunks[var1];
   }

   public Chunk getChunk(int var1, int var2) {
      return this.getChunk(getChunkIndex(var1, var2));
   }

   public static int getChunkIndex(int var0, int var1) {
      return (var0 & 31) + (var1 & 31) * 32;
   }

   private int checkIndex(int var1) {
      if (var1 >= 0 && var1 <= 1023) {
         return var1;
      } else {
         throw new IndexOutOfBoundsException();
      }
   }

   private Chunk createChunkIfMissing(int var1, int var2) {
      int var3 = MCAUtil.blockToChunk(var1);
      int var4 = MCAUtil.blockToChunk(var2);
      Chunk var5 = this.getChunk(var3, var4);
      if (var5 == null) {
         var5 = Chunk.newChunk();
         this.setChunk(getChunkIndex(var3, var4), var5);
      }

      return var5;
   }

   /** @deprecated */
   @Deprecated
   public void setBiomeAt(int var1, int var2, int var3) {
      this.createChunkIfMissing(var1, var2).setBiomeAt(var1, var2, var3);
   }

   public void setBiomeAt(int var1, int var2, int var3, int var4) {
      this.createChunkIfMissing(var1, var3).setBiomeAt(var1, var2, var3, var4);
   }

   /** @deprecated */
   @Deprecated
   public int getBiomeAt(int var1, int var2) {
      int var3 = MCAUtil.blockToChunk(var1);
      int var4 = MCAUtil.blockToChunk(var2);
      Chunk var5 = this.getChunk(getChunkIndex(var3, var4));
      return var5 == null ? -1 : var5.getBiomeAt(var1, var2);
   }

   public int getBiomeAt(int var1, int var2, int var3) {
      int var4 = MCAUtil.blockToChunk(var1);
      int var5 = MCAUtil.blockToChunk(var3);
      Chunk var6 = this.getChunk(getChunkIndex(var4, var5));
      return var6 == null ? -1 : var6.getBiomeAt(var1, var2, var3);
   }

   public void setBlockStateAt(int var1, int var2, int var3, CompoundTag var4, boolean var5) {
      this.createChunkIfMissing(var1, var3).setBlockStateAt(var1, var2, var3, var4, var5);
   }

   public CompoundTag getBlockStateAt(int var1, int var2, int var3) {
      int var4 = MCAUtil.blockToChunk(var1);
      int var5 = MCAUtil.blockToChunk(var3);
      Chunk var6 = this.getChunk(var4, var5);
      return var6 == null ? null : var6.getBlockStateAt(var1, var2, var3);
   }

   public void cleanupPalettesAndBlockStates() {
      Chunk[] var1 = this.chunks;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         Chunk var4 = var1[var3];
         if (var4 != null) {
            var4.cleanupPalettesAndBlockStates();
         }
      }

   }
}
