package org.terraform.data;

import java.util.Random;
import org.jetbrains.annotations.NotNull;
import org.terraform.biome.BiomeBank;
import org.terraform.biome.BiomeSection;
import org.terraform.main.config.TConfig;
import org.terraform.utils.GenUtils;

public class MegaChunk {
   public static final int megaChunkBlockWidth;
   private int x;
   private int z;

   public MegaChunk(@NotNull SimpleChunkLocation sLoc) {
      this(sLoc.getX(), sLoc.getZ());
   }

   public MegaChunk(int x, int y, int z) {
      this.x = blockCoordsToMega(x);
      this.z = blockCoordsToMega(z);
   }

   public MegaChunk(int chunkX, int chunkZ) {
      this(chunkX * 16, 0, chunkZ * 16);
   }

   private static int blockCoordsToMega(int coord) {
      return coord >= 0 ? coord / megaChunkBlockWidth : (int)(-1.0D * Math.ceil((double)Math.abs(coord) / (double)megaChunkBlockWidth));
   }

   private static int megaToBlockCoords(int coord) {
      return coord * megaChunkBlockWidth;
   }

   @NotNull
   public MegaChunk getRelative(int x, int z) {
      MegaChunk mc = new MegaChunk(0, 0);
      mc.x = this.x + x;
      mc.z = this.z + z;
      return mc;
   }

   public int[] getRandomCoords(@NotNull Random rand) {
      int lowX = megaToBlockCoords(this.x);
      int lowZ = megaToBlockCoords(this.z);
      int highX = lowX + megaChunkBlockWidth - 1;
      int highZ = lowZ + megaChunkBlockWidth - 1;
      int x = GenUtils.randInt(rand, lowX + megaChunkBlockWidth / 10, highX - megaChunkBlockWidth / 10);
      int z = GenUtils.randInt(rand, lowZ + megaChunkBlockWidth / 10, highZ - megaChunkBlockWidth / 10);
      return new int[]{x, z};
   }

   public int[] getRandomCenterChunkBlockCoords(@NotNull Random rand) {
      int lowX = this.getLowerCornerChunkCoords()[0];
      int lowZ = this.getLowerCornerChunkCoords()[1];
      int highX = this.getUpperCornerChunkCoords()[0];
      int highZ = this.getUpperCornerChunkCoords()[1];
      int x = GenUtils.randInt(rand, lowX, highX);
      int z = GenUtils.randInt(rand, lowZ, highZ);
      return new int[]{x * 16 + 7, z * 16 + 7};
   }

   public int[] getCenterBlockCoords() {
      int lowX = megaToBlockCoords(this.x);
      int lowZ = megaToBlockCoords(this.z);
      return new int[]{lowX + megaChunkBlockWidth / 2, lowZ + megaChunkBlockWidth / 2};
   }

   public int[] getCenterBiomeSectionBlockCoords() {
      int lowX = this.getCenterBlockCoords()[0];
      int lowZ = this.getCenterBlockCoords()[1];
      int sectionX = lowX >> BiomeSection.bitshifts;
      int sectionZ = lowZ >> BiomeSection.bitshifts;
      int centerOfSectionX = (sectionX << BiomeSection.bitshifts) + BiomeSection.sectionWidth / 2;
      int centerOfSectionZ = (sectionZ << BiomeSection.bitshifts) + BiomeSection.sectionWidth / 2;
      return new int[]{centerOfSectionX, centerOfSectionZ};
   }

   public int[] getCenterBiomeSectionChunkCoords() {
      int[] coords = this.getCenterBiomeSectionBlockCoords();
      return new int[]{coords[0] >> 4, coords[1] >> 4};
   }

   public int[] getUpperCornerBlockCoords() {
      int upperX = megaToBlockCoords(this.x) + megaChunkBlockWidth - 1;
      int upperZ = megaToBlockCoords(this.z) + megaChunkBlockWidth - 1;
      return new int[]{upperX, upperZ};
   }

   public int[] getLowerCornerBlockCoords() {
      int lowX = megaToBlockCoords(this.x);
      int lowZ = megaToBlockCoords(this.z);
      return new int[]{lowX, lowZ};
   }

   public int[] getCenterChunkCoords() {
      int[] coords = this.getCenterBlockCoords();
      return new int[]{coords[0] >> 4, coords[1] >> 4};
   }

   public int[] getLowerCornerChunkCoords() {
      int[] coords = this.getLowerCornerBlockCoords();
      return new int[]{coords[0] >> 4, coords[1] >> 4};
   }

   public int[] getUpperCornerChunkCoords() {
      int[] coords = this.getUpperCornerBlockCoords();
      return new int[]{coords[0] >> 4, coords[1] >> 4};
   }

   @NotNull
   public BiomeSection getCenterBiomeSection(TerraformWorld tw) {
      int[] coords = this.getCenterBiomeSectionBlockCoords();
      return BiomeBank.getBiomeSectionFromBlockCoords(tw, coords[0], coords[1]);
   }

   public boolean containsXZBlockCoords(int x, int z) {
      MegaChunk mc = new MegaChunk(x, 0, z);
      return mc.equals(this);
   }

   public boolean equals(Object obj) {
      if (!(obj instanceof MegaChunk)) {
         return false;
      } else {
         MegaChunk megaChunk = (MegaChunk)obj;
         return this.x == megaChunk.x && this.z == megaChunk.z;
      }
   }

   public int hashCode() {
      int prime = 31;
      int result = 5;
      int result = prime * result + this.x;
      result = prime * result + this.z;
      return result;
   }

   public int getX() {
      return this.x;
   }

   public int getZ() {
      return this.z;
   }

   @NotNull
   public String toString() {
      return this.x + "," + this.z;
   }

   static {
      megaChunkBlockWidth = BiomeSection.sectionWidth * TConfig.c.STRUCTURES_MEGACHUNK_NUMBIOMESECTIONS;
   }
}
