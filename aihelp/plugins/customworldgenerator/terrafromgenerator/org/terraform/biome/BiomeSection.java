package org.terraform.biome;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.terraform.data.SimpleLocation;
import org.terraform.data.TerraformWorld;
import org.terraform.main.TerraformGeneratorPlugin;
import org.terraform.main.config.TConfig;
import org.terraform.utils.GenUtils;
import org.terraform.utils.noise.FastNoise;

public class BiomeSection {
   public static final int bitshifts;
   public static final int sectionWidth;
   public static final int minSize;
   public static final int dominanceThreshold;
   public static final int dominanceThresholdSquared;
   private final int x;
   private final int z;
   private final TerraformWorld tw;
   private float temperature;
   private float moisture;
   private int radius;
   @Nullable
   private BiomeBank biome;
   private FastNoise shapeNoise;

   protected BiomeSection(TerraformWorld tw, int x, int z) {
      this.x = x >> bitshifts;
      this.z = z >> bitshifts;
      this.tw = tw;
   }

   protected BiomeSection(TerraformWorld tw, int x, int z, boolean useSectionCoords) {
      this.x = x;
      this.z = z;
      this.tw = tw;
   }

   @NotNull
   public static Collection<BiomeSection> getSurroundingSections(TerraformWorld tw, int width, int blockX, int blockZ) {
      BiomeSection homeSection = BiomeBank.getBiomeSectionFromBlockCoords(tw, blockX, blockZ);
      Collection<BiomeSection> sections = new ArrayList();
      SimpleLocation center = homeSection.getCenter();
      int startX;
      int startZ;
      if (width % 2 == 1) {
         startX = startZ = -width / 2;
      } else {
         startX = blockX >= center.getX() ? -width / 2 - 1 : -width / 2;
         startZ = blockZ >= center.getZ() ? -width / 2 - 1 : -width / 2;
      }

      for(int rx = startX; rx < startX + width; ++rx) {
         for(int rz = startZ; rz < startZ + width; ++rz) {
            sections.add(homeSection.getRelative(rx, rz));
         }
      }

      if (sections.size() != width * width) {
         TerraformGeneratorPlugin.logger.error("Section size was not " + width * width + ".");
      }

      return sections;
   }

   @NotNull
   public static Collection<BiomeSection> getSurroundingSections(TerraformWorld tw, int blockX, int blockZ) {
      Collection<BiomeSection> sections = new ArrayList();
      BiomeSection homeBiome = BiomeBank.getBiomeSectionFromBlockCoords(tw, blockX, blockZ);
      sections.add(homeBiome);
      SimpleLocation center = homeBiome.getCenter();
      if (blockX >= center.getX()) {
         if (blockZ >= center.getZ()) {
            sections.add(homeBiome.getRelative(1, 0));
            sections.add(homeBiome.getRelative(1, 1));
            sections.add(homeBiome.getRelative(0, 1));
         } else {
            sections.add(homeBiome.getRelative(1, 0));
            sections.add(homeBiome.getRelative(1, -1));
            sections.add(homeBiome.getRelative(0, -1));
         }
      } else if (blockZ >= center.getZ()) {
         sections.add(homeBiome.getRelative(-1, 0));
         sections.add(homeBiome.getRelative(-1, 1));
         sections.add(homeBiome.getRelative(0, 1));
      } else {
         sections.add(homeBiome.getRelative(-1, 0));
         sections.add(homeBiome.getRelative(-1, -1));
         sections.add(homeBiome.getRelative(0, -1));
      }

      return sections;
   }

   @NotNull
   public static BiomeSection getMostDominantSection(@NotNull TerraformWorld tw, int x, int z) {
      double dither = TConfig.c.BIOME_DITHER;
      Random locationBasedRandom = new Random((long)Objects.hash(new Object[]{tw.getSeed(), x, z}));
      SimpleLocation target = new SimpleLocation(x, 0, z);
      BiomeSection homeSection = BiomeBank.getBiomeSectionFromBlockCoords(tw, x, z);
      if (target.distanceSqr(homeSection.getCenter()) <= (float)dominanceThresholdSquared) {
         return homeSection;
      } else {
         Collection<BiomeSection> sections = getSurroundingSections(tw, x, z);
         BiomeSection mostDominant = homeSection;
         Iterator var10 = sections.iterator();

         while(var10.hasNext()) {
            BiomeSection sect = (BiomeSection)var10.next();
            float dom = (float)((double)sect.getDominance(target) + GenUtils.randDouble(locationBasedRandom, -dither, dither));
            if ((double)dom > (double)mostDominant.getDominance(target) + GenUtils.randDouble(locationBasedRandom, -dither, dither)) {
               mostDominant = sect;
            }
         }

         return mostDominant;
      }
   }

   protected void doCalculations() {
      this.radius = GenUtils.randInt(this.getSectionRandom(), minSize / 2, 5 * minSize / 4);
      this.shapeNoise = new FastNoise(Objects.hash(new Object[]{this.tw.getSeed(), this.x, this.z}));
      this.shapeNoise.SetNoiseType(FastNoise.NoiseType.SimplexFractal);
      this.shapeNoise.SetFractalOctaves(3);
      this.shapeNoise.SetFrequency(0.01F);
      this.biome = this.parseBiomeBank();
   }

   @NotNull
   public Random getSectionRandom() {
      return new Random((long)Objects.hash(new Object[]{this.tw.getSeed(), this.x, this.z}));
   }

   @NotNull
   public Random getSectionRandom(int multiplier) {
      return new Random((long)multiplier * (long)Objects.hash(new Object[]{this.tw.getSeed(), this.x, this.z}));
   }

   @NotNull
   public BiomeSection getRelative(int x, int z) {
      return BiomeBank.getBiomeSectionFromSectionCoords(this.tw, this.x + x, this.z + z, true);
   }

   @NotNull
   public BiomeSection getRelative(BiomeSubSection subSect) {
      return this.getRelative(subSect.relX, subSect.relZ);
   }

   @NotNull
   public BiomeBank getBiomeBank() {
      assert this.biome != null;

      return this.biome;
   }

   @NotNull
   private BiomeBank parseBiomeBank() {
      this.temperature = 7.5F * this.tw.getTemperatureOctave().GetNoise((float)this.x, (float)this.z);
      this.moisture = 7.5F * this.tw.getMoistureOctave().GetNoise((float)this.x, (float)this.z);
      return BiomeBank.selectBiome(this, (double)this.temperature, (double)this.moisture);
   }

   public float getDominance(@NotNull SimpleLocation target) {
      return this.getDominanceBasedOnRadius(target.getX(), target.getZ());
   }

   public float getDominanceBasedOnRadius(int blockX, int blockZ) {
      SimpleLocation center = this.getCenter();
      int xOffset = center.getX() - blockX;
      int zOffset = center.getZ() - blockZ;
      double equationResult = Math.pow((double)xOffset, 2.0D) / Math.pow((double)this.radius, 2.0D) + Math.pow((double)zOffset, 2.0D) / Math.pow((double)this.radius, 2.0D) + 0.7D * (double)this.shapeNoise.GetNoise((float)xOffset, (float)zOffset);
      return (float)(1.0D - 1.0D * equationResult);
   }

   @NotNull
   public SimpleLocation getCenter() {
      int x = (this.x << bitshifts) + sectionWidth / 2;
      int z = (this.z << bitshifts) + sectionWidth / 2;
      return new SimpleLocation(x, 0, z);
   }

   @NotNull
   public SimpleLocation getLowerBounds() {
      int x = this.x << bitshifts;
      int z = this.z << bitshifts;
      return new SimpleLocation(x, 0, z);
   }

   @NotNull
   public SimpleLocation getUpperBounds() {
      int x = (this.x << bitshifts) + sectionWidth;
      int z = (this.z << bitshifts) + sectionWidth;
      return new SimpleLocation(x, 0, z);
   }

   @NotNull
   public Collection<BiomeSection> getRelativeSurroundingSections(int radius) {
      if (radius == 0) {
         return List.of(this);
      } else {
         ArrayList<BiomeSection> candidates = new ArrayList();
         int[] var3 = new int[]{-radius, radius};
         int var4 = var3.length;

         int var5;
         int rz;
         int rx;
         for(var5 = 0; var5 < var4; ++var5) {
            rz = var3[var5];

            for(rx = -radius; rx <= radius; ++rx) {
               candidates.add(this.getRelative(rz, rx));
            }
         }

         var3 = new int[]{-radius, radius};
         var4 = var3.length;

         for(var5 = 0; var5 < var4; ++var5) {
            rz = var3[var5];

            for(rx = 1 - radius; rx <= radius - 1; ++rx) {
               candidates.add(this.getRelative(rx, rz));
            }
         }

         return candidates;
      }
   }

   @NotNull
   public BiomeSubSection getSubSection(int rawX, int rawZ) {
      SimpleLocation sectionCenter = this.getCenter();
      int relXFromCenter = rawX - sectionCenter.getX();
      int relZFromCenter = rawZ - sectionCenter.getZ();
      if (relXFromCenter > 0 && relXFromCenter >= Math.abs(relZFromCenter)) {
         return BiomeSubSection.POSITIVE_X;
      } else if (relXFromCenter <= 0 && Math.abs(relXFromCenter) >= Math.abs(relZFromCenter)) {
         return BiomeSubSection.NEGATIVE_X;
      } else if (relZFromCenter > 0 && relZFromCenter >= Math.abs(relXFromCenter)) {
         return BiomeSubSection.POSITIVE_Z;
      } else {
         return relZFromCenter <= 0 && Math.abs(relZFromCenter) >= Math.abs(relXFromCenter) ? BiomeSubSection.NEGATIVE_Z : BiomeSubSection.NONE;
      }
   }

   public int hashCode() {
      int prime = 13;
      int result = 5;
      int result = prime * result + this.x;
      result = prime * result + this.z;
      result = prime * result + this.tw.getName().hashCode();
      return result;
   }

   public boolean equals(Object obj) {
      if (!(obj instanceof BiomeSection)) {
         return false;
      } else {
         BiomeSection other = (BiomeSection)obj;
         return this.tw.getName().equals(other.tw.getName()) && this.x == other.x && this.z == other.z;
      }
   }

   public int getX() {
      return this.x;
   }

   public int getZ() {
      return this.z;
   }

   @NotNull
   public String toString() {
      return "(" + this.x + "," + this.z + ")";
   }

   @NotNull
   public BiomeClimate getClimate() {
      return BiomeClimate.selectClimate((double)this.temperature, (double)this.moisture);
   }

   public float getTemperature() {
      return this.temperature;
   }

   public float getMoisture() {
      return this.moisture;
   }

   public TerraformWorld getTw() {
      return this.tw;
   }

   public double getOceanLevel() {
      return (double)this.tw.getOceanicNoise().GetNoise((float)this.x, (float)this.z) * 50.0D;
   }

   public double getMountainLevel() {
      return (double)this.tw.getMountainousNoise().GetNoise((float)this.x, (float)this.z) * 50.0D;
   }

   static {
      bitshifts = TConfig.c.BIOME_SECTION_BITSHIFTS;
      sectionWidth = 1 << bitshifts;
      minSize = sectionWidth;
      dominanceThreshold = (int)(0.35D * (double)sectionWidth);
      dominanceThresholdSquared = dominanceThreshold * dominanceThreshold;
   }
}
