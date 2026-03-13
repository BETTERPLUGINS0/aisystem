package com.volmit.iris.engine.data.chunk;

import com.volmit.iris.Iris;
import com.volmit.iris.core.nms.BiomeBaseInjector;
import com.volmit.iris.util.data.IrisCustomData;
import com.volmit.iris.util.nbt.mca.Chunk;
import com.volmit.iris.util.nbt.mca.NBTWorld;
import lombok.Generated;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.data.BlockData;
import org.bukkit.generator.ChunkGenerator.BiomeGrid;
import org.bukkit.generator.ChunkGenerator.ChunkData;
import org.bukkit.material.MaterialData;

public class MCATerrainChunk implements TerrainChunk {
   private final NBTWorld writer;
   private final BiomeBaseInjector injector;
   private final int ox;
   private final int oz;
   private final int minHeight;
   private final int maxHeight;
   private final Chunk mcaChunk;

   public BiomeBaseInjector getBiomeBaseInjector() {
      return this.injector;
   }

   public Biome getBiome(int x, int z) {
      return Biome.THE_VOID;
   }

   public Biome getBiome(int x, int y, int z) {
      return Biome.THE_VOID;
   }

   public void setBiome(int x, int z, Biome bio) {
      this.setBiome(this.ox + var1, 0, this.oz + var2, var3);
   }

   public void setBiome(int x, int y, int z, Biome bio) {
      this.mcaChunk.setBiomeAt(this.ox + var1 & 15, var2, this.oz + var3 & 15, this.writer.getBiomeId(var4));
   }

   public int getMinHeight() {
      return this.minHeight;
   }

   public int getMaxHeight() {
      return this.maxHeight;
   }

   public void setBlock(int x, int y, int z, BlockData blockData) {
      int var5 = var1 + this.ox & 15;
      int var6 = var3 + this.oz & 15;
      if (var2 <= this.getMaxHeight() && var2 >= this.getMinHeight()) {
         if (var4 == null) {
            Iris.error("NULL BD");
         }

         if (var4 instanceof IrisCustomData) {
            IrisCustomData var7 = (IrisCustomData)var4;
            var4 = var7.getBase();
         }

         this.mcaChunk.setBlockStateAt(var5, var2, var6, NBTWorld.getCompound(var4), false);
      }
   }

   public BlockData getBlockData(int x, int y, int z) {
      if (var2 > this.getMaxHeight()) {
         var2 = this.getMaxHeight();
      }

      if (var2 < this.getMinHeight()) {
         var2 = this.getMinHeight();
      }

      return NBTWorld.getBlockData(this.mcaChunk.getBlockStateAt(var1 + this.ox & 15, var2, var3 + this.oz & 15));
   }

   public ChunkData getRaw() {
      return null;
   }

   public void setRaw(ChunkData data) {
   }

   public void inject(BiomeGrid biome) {
   }

   public void setBlock(int x, int y, int z, Material material) {
   }

   public void setBlock(int x, int y, int z, MaterialData material) {
   }

   public void setRegion(int xMin, int yMin, int zMin, int xMax, int yMax, int zMax, Material material) {
   }

   public void setRegion(int xMin, int yMin, int zMin, int xMax, int yMax, int zMax, MaterialData material) {
   }

   public void setRegion(int xMin, int yMin, int zMin, int xMax, int yMax, int zMax, BlockData blockData) {
   }

   public Material getType(int x, int y, int z) {
      return null;
   }

   public MaterialData getTypeAndData(int x, int y, int z) {
      return null;
   }

   public byte getData(int x, int y, int z) {
      return 0;
   }

   @Generated
   public static MCATerrainChunk.MCATerrainChunkBuilder builder() {
      return new MCATerrainChunk.MCATerrainChunkBuilder();
   }

   @Generated
   public MCATerrainChunk(final NBTWorld writer, final BiomeBaseInjector injector, final int ox, final int oz, final int minHeight, final int maxHeight, final Chunk mcaChunk) {
      this.writer = var1;
      this.injector = var2;
      this.ox = var3;
      this.oz = var4;
      this.minHeight = var5;
      this.maxHeight = var6;
      this.mcaChunk = var7;
   }

   @Generated
   public static class MCATerrainChunkBuilder {
      @Generated
      private NBTWorld writer;
      @Generated
      private BiomeBaseInjector injector;
      @Generated
      private int ox;
      @Generated
      private int oz;
      @Generated
      private int minHeight;
      @Generated
      private int maxHeight;
      @Generated
      private Chunk mcaChunk;

      @Generated
      MCATerrainChunkBuilder() {
      }

      @Generated
      public MCATerrainChunk.MCATerrainChunkBuilder writer(final NBTWorld writer) {
         this.writer = var1;
         return this;
      }

      @Generated
      public MCATerrainChunk.MCATerrainChunkBuilder injector(final BiomeBaseInjector injector) {
         this.injector = var1;
         return this;
      }

      @Generated
      public MCATerrainChunk.MCATerrainChunkBuilder ox(final int ox) {
         this.ox = var1;
         return this;
      }

      @Generated
      public MCATerrainChunk.MCATerrainChunkBuilder oz(final int oz) {
         this.oz = var1;
         return this;
      }

      @Generated
      public MCATerrainChunk.MCATerrainChunkBuilder minHeight(final int minHeight) {
         this.minHeight = var1;
         return this;
      }

      @Generated
      public MCATerrainChunk.MCATerrainChunkBuilder maxHeight(final int maxHeight) {
         this.maxHeight = var1;
         return this;
      }

      @Generated
      public MCATerrainChunk.MCATerrainChunkBuilder mcaChunk(final Chunk mcaChunk) {
         this.mcaChunk = var1;
         return this;
      }

      @Generated
      public MCATerrainChunk build() {
         return new MCATerrainChunk(this.writer, this.injector, this.ox, this.oz, this.minHeight, this.maxHeight, this.mcaChunk);
      }

      @Generated
      public String toString() {
         String var10000 = String.valueOf(this.writer);
         return "MCATerrainChunk.MCATerrainChunkBuilder(writer=" + var10000 + ", injector=" + String.valueOf(this.injector) + ", ox=" + this.ox + ", oz=" + this.oz + ", minHeight=" + this.minHeight + ", maxHeight=" + this.maxHeight + ", mcaChunk=" + String.valueOf(this.mcaChunk) + ")";
      }
   }
}
