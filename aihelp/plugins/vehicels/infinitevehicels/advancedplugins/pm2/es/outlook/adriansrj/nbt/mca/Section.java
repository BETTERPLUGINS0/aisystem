package es.outlook.adriansrj.nbt.mca;

import es.outlook.adriansrj.nbt.nbt.tag.ByteArrayTag;
import es.outlook.adriansrj.nbt.nbt.tag.CompoundTag;
import es.outlook.adriansrj.nbt.nbt.tag.ListTag;
import es.outlook.adriansrj.nbt.nbt.tag.LongArrayTag;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Section {
   private CompoundTag data;
   private Map<String, List<Section.PaletteIndex>> valueIndexedPalette;
   private ListTag<CompoundTag> palette;
   private byte[] blockLight;
   private long[] blockStates;
   private byte[] skyLight;
   int dataVersion;

   public Section(CompoundTag var1, int var2) {
      this(var1, var2, -1L);
   }

   public Section(CompoundTag var1, int var2, long var3) {
      this.valueIndexedPalette = new HashMap();
      this.data = var1;
      this.dataVersion = var2;
      ListTag var5 = var1.getListTag("Palette");
      if (var5 != null) {
         this.palette = var5.asCompoundTagList();

         for(int var6 = 0; var6 < this.palette.size(); ++var6) {
            CompoundTag var7 = (CompoundTag)this.palette.get(var6);
            this.putValueIndexedPalette(var7, var6);
         }

         ByteArrayTag var9 = var1.getByteArrayTag("BlockLight");
         LongArrayTag var10 = var1.getLongArrayTag("BlockStates");
         ByteArrayTag var8 = var1.getByteArrayTag("SkyLight");
         if ((var3 & 2048L) != 0L) {
            this.blockLight = var9 != null ? (byte[])var9.getValue() : null;
         }

         if ((var3 & 4096L) != 0L) {
            this.blockStates = var10 != null ? (long[])var10.getValue() : null;
         }

         if ((var3 & 8192L) != 0L) {
            this.skyLight = var8 != null ? (byte[])var8.getValue() : null;
         }

      }
   }

   Section() {
      this.valueIndexedPalette = new HashMap();
   }

   void putValueIndexedPalette(CompoundTag var1, int var2) {
      Section.PaletteIndex var3 = new Section.PaletteIndex(var1, var2);
      String var4 = var1.getString("Name");
      List var5 = (List)this.valueIndexedPalette.get(var4);
      if (var5 == null) {
         ArrayList var8 = new ArrayList(1);
         var8.add(var3);
         this.valueIndexedPalette.put(var4, var8);
      } else {
         Iterator var6 = var5.iterator();

         while(var6.hasNext()) {
            Section.PaletteIndex var7 = (Section.PaletteIndex)var6.next();
            if (var7.data.equals(var1)) {
               return;
            }
         }

         var5.add(var3);
      }

   }

   Section.PaletteIndex getValueIndexedPalette(CompoundTag var1) {
      List var2 = (List)this.valueIndexedPalette.get(var1.getString("Name"));
      if (var2 == null) {
         return null;
      } else {
         Iterator var3 = var2.iterator();

         Section.PaletteIndex var4;
         do {
            if (!var3.hasNext()) {
               return null;
            }

            var4 = (Section.PaletteIndex)var3.next();
         } while(!var4.data.equals(var1));

         return var4;
      }
   }

   public boolean isEmpty() {
      return this.data == null;
   }

   public CompoundTag getBlockStateAt(int var1, int var2, int var3) {
      int var4 = this.getBlockIndex(var1, var2, var3);
      int var5 = this.getPaletteIndex(var4);
      return (CompoundTag)this.palette.get(var5);
   }

   public void setBlockStateAt(int var1, int var2, int var3, CompoundTag var4, boolean var5) {
      int var6 = this.palette.size();
      int var7 = this.addToPalette(var4);
      if (var6 != this.palette.size() && (var7 & var7 - 1) == 0) {
         this.adjustBlockStateBits((Map)null, this.blockStates);
         var5 = true;
      }

      this.setPaletteIndex(this.getBlockIndex(var1, var2, var3), var7, this.blockStates);
      if (var5) {
         this.cleanupPaletteAndBlockStates();
      }

   }

   public int getPaletteIndex(int var1) {
      int var2 = this.blockStates.length >> 6;
      int var5;
      if (this.dataVersion < 2527) {
         double var11 = (double)var1 / (4096.0D / (double)this.blockStates.length);
         var5 = (int)var11;
         int var6 = (int)((var11 - Math.floor(var11)) * 64.0D);
         if (var6 + var2 > 64) {
            long var7 = bitRange(this.blockStates[var5], var6, 64);
            long var9 = bitRange(this.blockStates[var5 + 1], 0, var6 + var2 - 64);
            return (int)((var9 << 64 - var6) + var7);
         } else {
            return (int)bitRange(this.blockStates[var5], var6, var6 + var2);
         }
      } else {
         int var3 = (int)(64.0D / (double)var2);
         int var4 = var1 / var3;
         var5 = var1 % var3 * var2;
         return (int)bitRange(this.blockStates[var4], var5, var5 + var2);
      }
   }

   public void setPaletteIndex(int var1, int var2, long[] var3) {
      int var4 = var3.length >> 6;
      int var7;
      if (this.dataVersion < 2527) {
         double var5 = (double)var1 / (4096.0D / (double)var3.length);
         var7 = (int)var5;
         int var8 = (int)((var5 - Math.floor((double)var7)) * 64.0D);
         if (var8 + var4 > 64) {
            var3[var7] = updateBits(var3[var7], (long)var2, var8, 64);
            var3[var7 + 1] = updateBits(var3[var7 + 1], (long)var2, var8 - 64, var8 + var4 - 64);
         } else {
            var3[var7] = updateBits(var3[var7], (long)var2, var8, var8 + var4);
         }
      } else {
         int var9 = (int)(64.0D / (double)var4);
         int var6 = var1 / var9;
         var7 = var1 % var9 * var4;
         var3[var6] = updateBits(var3[var6], (long)var2, var7, var7 + var4);
      }

   }

   public ListTag<CompoundTag> getPalette() {
      return this.palette;
   }

   int addToPalette(CompoundTag var1) {
      Section.PaletteIndex var2;
      if ((var2 = this.getValueIndexedPalette(var1)) != null) {
         return var2.index;
      } else {
         this.palette.add(var1);
         this.putValueIndexedPalette(var1, this.palette.size() - 1);
         return this.palette.size() - 1;
      }
   }

   int getBlockIndex(int var1, int var2, int var3) {
      return (var2 & 15) * 256 + (var3 & 15) * 16 + (var1 & 15);
   }

   static long updateBits(long var0, long var2, int var4, int var5) {
      long var6 = var4 > 0 ? (var2 & (1L << var5 - var4) - 1L) << var4 : (var2 & (1L << var5 - var4) - 1L) >>> -var4;
      return var0 & ((var5 > 63 ? 0L : -1L << var5) | (var4 < 0 ? 0L : (1L << var4) - 1L)) | var6;
   }

   static long bitRange(long var0, int var2, int var3) {
      int var4 = 64 - var3;
      return var0 << var4 >>> var4 + var2;
   }

   public void cleanupPaletteAndBlockStates() {
      Map var1 = this.cleanupPalette();
      this.adjustBlockStateBits(var1, this.blockStates);
   }

   private Map<Integer, Integer> cleanupPalette() {
      HashMap var1 = new HashMap();

      int var2;
      int var3;
      for(var2 = 0; var2 < 4096; ++var2) {
         var3 = this.getPaletteIndex(var2);
         var1.put(var3, var3);
      }

      var2 = 1;
      this.valueIndexedPalette = new HashMap(this.valueIndexedPalette.size());
      this.putValueIndexedPalette((CompoundTag)this.palette.get(0), 0);

      for(var3 = 1; var3 < this.palette.size(); ++var3) {
         if (!var1.containsKey(var2)) {
            this.palette.remove(var3);
            --var3;
         } else {
            this.putValueIndexedPalette((CompoundTag)this.palette.get(var3), var3);
            var1.put(var2, var3);
         }

         ++var2;
      }

      return var1;
   }

   void adjustBlockStateBits(Map<Integer, Integer> var1, long[] var2) {
      int var3 = 32 - Integer.numberOfLeadingZeros(this.palette.size() - 1);
      var3 = Math.max(var3, 4);
      long[] var4;
      int var5;
      if (this.dataVersion < 2527) {
         var4 = var3 == var2.length / 64 ? var2 : new long[var3 * 64];
      } else {
         var5 = (int)Math.ceil(4096.0D / Math.floor(64.0D / (double)var3));
         var4 = var3 == var2.length / 64 ? var2 : new long[var5];
      }

      if (var1 != null) {
         for(var5 = 0; var5 < 4096; ++var5) {
            this.setPaletteIndex(var5, (Integer)var1.get(this.getPaletteIndex(var5)), var4);
         }
      } else {
         for(var5 = 0; var5 < 4096; ++var5) {
            this.setPaletteIndex(var5, this.getPaletteIndex(var5), var4);
         }
      }

      this.blockStates = var4;
   }

   public byte[] getBlockLight() {
      return this.blockLight;
   }

   public void setBlockLight(byte[] var1) {
      if (var1 != null && var1.length != 2048) {
         throw new IllegalArgumentException("BlockLight array must have a length of 2048");
      } else {
         this.blockLight = var1;
      }
   }

   public long[] getBlockStates() {
      return this.blockStates;
   }

   public void setBlockStates(long[] var1) {
      if (var1 == null) {
         throw new NullPointerException("BlockStates cannot be null");
      } else if (var1.length % 64 == 0 && var1.length >= 256 && var1.length <= 4096) {
         this.blockStates = var1;
      } else {
         throw new IllegalArgumentException("BlockStates must have a length > 255 and < 4097 and must be divisible by 64");
      }
   }

   public byte[] getSkyLight() {
      return this.skyLight;
   }

   public void setSkyLight(byte[] var1) {
      if (var1 != null && var1.length != 2048) {
         throw new IllegalArgumentException("SkyLight array must have a length of 2048");
      } else {
         this.skyLight = var1;
      }
   }

   public static Section newSection() {
      Section var0 = new Section();
      var0.blockStates = new long[256];
      var0.palette = new ListTag(CompoundTag.class);
      CompoundTag var1 = new CompoundTag();
      var1.putString("Name", "minecraft:air");
      var0.palette.add(var1);
      var0.data = new CompoundTag();
      return var0;
   }

   public CompoundTag updateHandle(int var1) {
      this.data.putByte("Y", (byte)var1);
      if (this.palette != null) {
         this.data.put("Palette", this.palette);
      }

      if (this.blockLight != null) {
         this.data.putByteArray("BlockLight", this.blockLight);
      }

      if (this.blockStates != null) {
         this.data.putLongArray("BlockStates", this.blockStates);
      }

      if (this.skyLight != null) {
         this.data.putByteArray("SkyLight", this.skyLight);
      }

      return this.data;
   }

   private static class PaletteIndex {
      CompoundTag data;
      int index;

      PaletteIndex(CompoundTag var1, int var2) {
         this.data = var1;
         this.index = var2;
      }
   }
}
