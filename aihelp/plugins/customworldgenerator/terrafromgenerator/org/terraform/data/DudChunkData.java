package org.terraform.data;

import org.apache.commons.lang.NotImplementedException;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.data.BlockData;
import org.bukkit.generator.ChunkGenerator.ChunkData;
import org.bukkit.material.MaterialData;
import org.jetbrains.annotations.NotNull;

public class DudChunkData implements ChunkData {
   public int getMinHeight() {
      return 0;
   }

   public int getMaxHeight() {
      return 0;
   }

   @NotNull
   public Biome getBiome(int i, int i1, int i2) {
      throw new NotImplementedException();
   }

   public void setBlock(int i, int i1, int i2, @NotNull Material material) {
   }

   public void setBlock(int i, int i1, int i2, @NotNull MaterialData materialData) {
   }

   public void setBlock(int i, int i1, int i2, @NotNull BlockData blockData) {
   }

   public void setRegion(int i, int i1, int i2, int i3, int i4, int i5, @NotNull Material material) {
   }

   public void setRegion(int i, int i1, int i2, int i3, int i4, int i5, @NotNull MaterialData materialData) {
   }

   public void setRegion(int i, int i1, int i2, int i3, int i4, int i5, @NotNull BlockData blockData) {
   }

   @NotNull
   public Material getType(int i, int i1, int i2) {
      throw new NotImplementedException();
   }

   @NotNull
   public MaterialData getTypeAndData(int i, int i1, int i2) {
      throw new NotImplementedException();
   }

   @NotNull
   public BlockData getBlockData(int i, int i1, int i2) {
      throw new NotImplementedException();
   }

   public byte getData(int i, int i1, int i2) {
      throw new NotImplementedException();
   }
}
