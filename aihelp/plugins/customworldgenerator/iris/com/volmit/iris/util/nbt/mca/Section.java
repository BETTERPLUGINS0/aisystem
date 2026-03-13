package com.volmit.iris.util.nbt.mca;

import com.volmit.iris.core.nms.INMS;
import com.volmit.iris.util.nbt.mca.palette.MCAPaletteAccess;
import com.volmit.iris.util.nbt.tag.ByteArrayTag;
import com.volmit.iris.util.nbt.tag.CompoundTag;
import com.volmit.iris.util.nbt.tag.ListTag;

public class Section {
   private CompoundTag data;
   private MCAPaletteAccess palette;
   private byte[] blockLight;
   private byte[] skyLight;

   public Section(CompoundTag sectionRoot, int dataVersion) {
      this(var1, var2, -1L);
   }

   public Section(CompoundTag sectionRoot, int dataVersion, long loadFlags) {
      this.data = var1;
      ListTag var5 = var1.getListTag("Palette");
      if (var5 != null) {
         this.palette = INMS.get().createPalette();
         this.palette.readFromSection(var1);
         ByteArrayTag var6 = var1.getByteArrayTag("BlockLight");
         ByteArrayTag var7 = var1.getByteArrayTag("SkyLight");
         this.blockLight = var6 != null ? (byte[])var6.getValue() : null;
         this.skyLight = var7 != null ? (byte[])var7.getValue() : null;
      }
   }

   Section() {
   }

   public static Section newSection() {
      Section var0 = new Section();
      var0.data = new CompoundTag();
      var0.palette = INMS.get().createPalette();
      return var0;
   }

   public boolean isEmpty() {
      return this.data == null;
   }

   public synchronized CompoundTag getBlockStateAt(int blockX, int blockY, int blockZ) {
      synchronized(this.palette) {
         return this.palette.getBlock(var1 & 15, var2 & 15, var3 & 15);
      }
   }

   public synchronized void setBlockStateAt(int blockX, int blockY, int blockZ, CompoundTag state, boolean cleanup) {
      synchronized(this.palette) {
         this.palette.setBlock(var1 & 15, var2 & 15, var3 & 15, var4);
      }
   }

   public void cleanupPaletteAndBlockStates() {
   }

   public synchronized byte[] getBlockLight() {
      return this.blockLight;
   }

   public synchronized void setBlockLight(byte[] blockLight) {
      if (var1 != null && var1.length != 2048) {
         throw new IllegalArgumentException("BlockLight array must have a length of 2048");
      } else {
         this.blockLight = var1;
      }
   }

   public synchronized byte[] getSkyLight() {
      return this.skyLight;
   }

   public synchronized void setSkyLight(byte[] skyLight) {
      if (var1 != null && var1.length != 2048) {
         throw new IllegalArgumentException("SkyLight array must have a length of 2048");
      } else {
         this.skyLight = var1;
      }
   }

   public synchronized CompoundTag updateHandle(int y) {
      this.data.putByte("Y", (byte)var1);
      if (this.palette != null) {
         synchronized(this.palette) {
            this.palette.writeToSection(this.data);
         }
      }

      if (this.blockLight != null) {
         this.data.putByteArray("BlockLight", this.blockLight);
      }

      if (this.skyLight != null) {
         this.data.putByteArray("SkyLight", this.skyLight);
      }

      return this.data;
   }
}
