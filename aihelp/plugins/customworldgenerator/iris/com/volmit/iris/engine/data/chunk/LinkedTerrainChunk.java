package com.volmit.iris.engine.data.chunk;

import com.volmit.iris.core.nms.BiomeBaseInjector;
import com.volmit.iris.core.nms.INMS;
import com.volmit.iris.util.data.IrisBiomeStorage;
import com.volmit.iris.util.data.IrisCustomData;
import lombok.Generated;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.data.BlockData;
import org.bukkit.generator.ChunkGenerator.BiomeGrid;
import org.bukkit.generator.ChunkGenerator.ChunkData;
import org.bukkit.material.MaterialData;

public class LinkedTerrainChunk implements TerrainChunk {
   private final IrisBiomeStorage biome3D;
   private final BiomeGrid storage;
   private ChunkData rawChunkData;
   private boolean unsafe;

   public LinkedTerrainChunk(World world) {
      this((BiomeGrid)null, (ChunkData)Bukkit.createChunkData(var1));
   }

   public LinkedTerrainChunk(World world, BiomeGrid storage) {
      this(var2, Bukkit.createChunkData(var1));
   }

   public LinkedTerrainChunk(BiomeGrid storage, ChunkData data) {
      this.unsafe = true;
      this.storage = var1;
      this.rawChunkData = var2;
      this.biome3D = var1 != null ? null : new IrisBiomeStorage();
   }

   public BiomeBaseInjector getBiomeBaseInjector() {
      return this.unsafe ? (var0, var1, var2, var3) -> {
      } : (var1, var2, var3, var4) -> {
         INMS.get().forceBiomeInto(var1, var2, var3, var4, this.storage);
      };
   }

   public Biome getBiome(int x, int z) {
      return this.storage != null ? this.storage.getBiome(var1, var2) : this.biome3D.getBiome(var1, 0, var2);
   }

   public Biome getBiome(int x, int y, int z) {
      return this.storage != null ? this.storage.getBiome(var1, var2, var3) : this.biome3D.getBiome(var1, var2, var3);
   }

   public void setBiome(int x, int z, Biome bio) {
      if (this.storage != null) {
         this.storage.setBiome(var1, var2, var3);
      } else {
         this.biome3D.setBiome(var1, 0, var2, var3);
      }
   }

   public BiomeGrid getRawBiome() {
      return this.storage;
   }

   public void setBiome(int x, int y, int z, Biome bio) {
      if (this.storage != null) {
         this.storage.setBiome(var1, var2, var3, var4);
      } else {
         this.biome3D.setBiome(var1, var2, var3, var4);
      }
   }

   public int getMinHeight() {
      return this.rawChunkData.getMinHeight();
   }

   public int getMaxHeight() {
      return this.rawChunkData.getMaxHeight();
   }

   public synchronized void setBlock(int x, int y, int z, BlockData blockData) {
      if (var4 instanceof IrisCustomData) {
         IrisCustomData var5 = (IrisCustomData)var4;
         var4 = var5.getBase();
      }

      this.rawChunkData.setBlock(var1, var2, var3, var4);
   }

   public BlockData getBlockData(int x, int y, int z) {
      return this.rawChunkData.getBlockData(var1, var2, var3);
   }

   /** @deprecated */
   @Deprecated
   public synchronized void setBlock(int x, int y, int z, Material material) {
      this.rawChunkData.setBlock(var1, var2, var3, var4);
   }

   /** @deprecated */
   @Deprecated
   public synchronized void setBlock(int x, int y, int z, MaterialData material) {
      this.rawChunkData.setBlock(var1, var2, var3, var4);
   }

   /** @deprecated */
   @Deprecated
   public synchronized void setRegion(int xMin, int yMin, int zMin, int xMax, int yMax, int zMax, Material material) {
      this.rawChunkData.setRegion(var1, var2, var3, var4, var5, var6, var7);
   }

   /** @deprecated */
   @Deprecated
   public synchronized void setRegion(int xMin, int yMin, int zMin, int xMax, int yMax, int zMax, MaterialData material) {
      this.rawChunkData.setRegion(var1, var2, var3, var4, var5, var6, var7);
   }

   public synchronized void setRegion(int xMin, int yMin, int zMin, int xMax, int yMax, int zMax, BlockData blockData) {
      this.rawChunkData.setRegion(var1, var2, var3, var4, var5, var6, var7);
   }

   /** @deprecated */
   @Deprecated
   public synchronized Material getType(int x, int y, int z) {
      return this.rawChunkData.getType(var1, var2, var3);
   }

   /** @deprecated */
   @Deprecated
   public MaterialData getTypeAndData(int x, int y, int z) {
      return this.rawChunkData.getTypeAndData(var1, var2, var3);
   }

   /** @deprecated */
   @Deprecated
   public byte getData(int x, int y, int z) {
      return this.rawChunkData.getData(var1, var2, var3);
   }

   public ChunkData getRaw() {
      return this.rawChunkData;
   }

   public void setRaw(ChunkData data) {
      this.rawChunkData = var1;
   }

   public void inject(BiomeGrid biome) {
      if (this.biome3D != null) {
         this.biome3D.inject(var1);
      }

   }

   @Generated
   public void setUnsafe(final boolean unsafe) {
      this.unsafe = var1;
   }
}
