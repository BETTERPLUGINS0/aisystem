package org.terraform.coregen.bukkit;

import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.data.BlockData;
import org.bukkit.generator.ChunkGenerator.ChunkData;
import org.bukkit.material.MaterialData;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.populatordata.IPopulatorDataBaseHeightAccess;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;

public class TerraformChunkData implements ChunkData {
   private final PopulatorDataAbstract popData;

   public TerraformChunkData(PopulatorDataAbstract popData) {
      this.popData = popData;
   }

   public int getBaseHeight(int x, int z) {
      int height = -64;
      if (this.popData instanceof IPopulatorDataBaseHeightAccess) {
         height = ((IPopulatorDataBaseHeightAccess)this.popData).getBaseHeight(x + this.popData.getChunkX() * 16, z + this.popData.getChunkZ() * 16);
      }

      return height;
   }

   @NotNull
   public Biome getBiome(int x, int y, int z) {
      return this.popData.getBiome(x + this.popData.getChunkX() * 16, z + this.popData.getChunkZ() * 16);
   }

   @NotNull
   public BlockData getBlockData(int x, int y, int z) {
      return this.popData.getBlockData(x + this.popData.getChunkX() * 16, y, z + this.popData.getChunkZ() * 16);
   }

   public byte getData(int x, int y, int z) {
      throw new UnsupportedOperationException("getData was called on TerraformChunkData!");
   }

   public int getMaxHeight() {
      return this.popData.getTerraformWorld().maxY;
   }

   public int getMinHeight() {
      return this.popData.getTerraformWorld().minY;
   }

   @NotNull
   public Material getType(int x, int y, int z) {
      return this.popData.getType(x + this.popData.getChunkX() * 16, y, z + this.popData.getChunkZ() * 16);
   }

   @NotNull
   public MaterialData getTypeAndData(int x, int y, int z) {
      throw new UnsupportedOperationException("getTypeAndData was called on TerraformChunkData with MaterialData!");
   }

   public void setBlock(int x, int y, int z, @NotNull Material arg3) {
      this.popData.setType(x + this.popData.getChunkX() * 16, y, z + this.popData.getChunkZ() * 16, arg3);
   }

   public void setBlock(int x, int y, int z, @NotNull MaterialData arg3) {
      throw new UnsupportedOperationException("setBlock was called on TerraformChunkData with MaterialData!");
   }

   public void setBlock(int x, int y, int z, @NotNull BlockData arg3) {
      this.popData.setBlockData(x + this.popData.getChunkX() * 16, y, z + this.popData.getChunkZ() * 16, arg3);
   }

   public void setRegion(int x, int y, int z, int x2, int y2, int z2, @NotNull Material arg6) {
      throw new UnsupportedOperationException("setRegion was called on TerraformChunkData!");
   }

   public void setRegion(int x, int y, int z, int x2, int y2, int z2, @NotNull MaterialData arg6) {
      throw new UnsupportedOperationException("setRegion was called on TerraformChunkData with MaterialData!");
   }

   public void setRegion(int x, int y, int z, int x2, int y2, int z2, @NotNull BlockData arg6) {
      throw new UnsupportedOperationException("setRegion was called on TerraformChunkData!");
   }
}
