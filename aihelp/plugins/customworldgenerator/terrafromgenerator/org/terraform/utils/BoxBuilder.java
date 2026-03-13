package org.terraform.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Random;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import org.terraform.data.SimpleBlock;
import org.terraform.utils.noise.FastNoise;

public class BoxBuilder {
   private final Random random;
   private final int seed;
   private final SimpleBlock core;
   @NotNull
   private final Collection<Material> replaceWhitelist = new ArrayList();
   private float rX = 1.0F;
   private float rY = 1.0F;
   private float rZ = 1.0F;
   private boolean hardReplace = false;
   private Material[] types;
   private Material[] upperType;
   private Material[] lowerType;
   private int staticWaterLevel = -9999;
   private BoxBuilder.BoxType boxType;

   public BoxBuilder(@NotNull Random random, SimpleBlock core, Material... types) {
      this.boxType = BoxBuilder.BoxType.FULL_BOX;
      this.random = random;
      this.seed = random.nextInt(99999999);
      this.types = types;
      this.core = core;
   }

   @NotNull
   public BoxBuilder setBoxType(BoxBuilder.BoxType sphereType) {
      this.boxType = sphereType;
      return this;
   }

   @NotNull
   public BoxBuilder setUpperType(Material... upperType) {
      this.upperType = upperType;
      return this;
   }

   @NotNull
   public BoxBuilder setLowerType(Material... lowerType) {
      this.lowerType = lowerType;
      return this;
   }

   @NotNull
   public BoxBuilder setStaticWaterLevel(int staticWaterLevel) {
      this.staticWaterLevel = staticWaterLevel;
      return this;
   }

   @NotNull
   public BoxBuilder addToWhitelist(@NotNull Material... mats) {
      this.replaceWhitelist.addAll(Arrays.asList(mats));
      return this;
   }

   @NotNull
   public BoxBuilder setRadius(float radius) {
      this.rX = radius;
      this.rY = radius;
      this.rZ = radius;
      return this;
   }

   @NotNull
   public BoxBuilder setRX(float rX) {
      this.rX = rX;
      return this;
   }

   @NotNull
   public BoxBuilder setRZ(float rZ) {
      this.rZ = rZ;
      return this;
   }

   @NotNull
   public BoxBuilder setRY(float rY) {
      this.rY = rY;
      return this;
   }

   @NotNull
   public BoxBuilder setSnowy() {
      this.upperType = new Material[]{Material.SNOW};
      return this;
   }

   @NotNull
   public BoxBuilder setHardReplace(boolean hardReplace) {
      this.hardReplace = hardReplace;
      return this;
   }

   public void build() {
      if (!(this.rX <= 0.0F) || !(this.rY <= 0.0F) || !(this.rZ <= 0.0F)) {
         if ((double)this.rX <= 0.5D && (double)this.rY <= 0.5D && (double)this.rZ <= 0.5D) {
            this.unitReplace(this.core);
         } else {
            FastNoise noise = new FastNoise(this.seed);
            noise.SetNoiseType(FastNoise.NoiseType.Simplex);
            noise.SetFrequency(0.12F);
            float effectiveRYLower = -this.rY;
            if (this.boxType == BoxBuilder.BoxType.UPPER_SEMIBOX) {
               effectiveRYLower = 0.0F;
            }

            float effectiveRYUpper = this.rY;
            if (this.boxType == BoxBuilder.BoxType.LOWER_SEMIBOX) {
               effectiveRYUpper = 0.0F;
            }

            float fuzzMultiplier = 0.2F;

            for(float y = effectiveRYLower * (1.0F + fuzzMultiplier); y <= effectiveRYUpper * (1.0F + fuzzMultiplier); ++y) {
               float yMultiplier = 1.0F - Math.abs(y) / this.rY;

               for(float x = -this.rX * (1.0F + fuzzMultiplier) * yMultiplier; x <= this.rX * (1.0F + fuzzMultiplier) * yMultiplier; ++x) {
                  for(float z = -this.rZ * (1.0F + fuzzMultiplier) * yMultiplier; z <= this.rZ * (1.0F + fuzzMultiplier) * yMultiplier; ++z) {
                     SimpleBlock rel = this.core.getRelative(Math.round(x), Math.round(y), Math.round(z));
                     double noiseVal = (double)Math.abs(noise.GetNoise((float)rel.getX(), (float)rel.getY(), (float)rel.getZ()));
                     if ((double)Math.abs(x) <= (double)this.rX * (1.0D + noiseVal * (double)fuzzMultiplier) && (double)Math.abs(y) <= (double)this.rY * (1.0D + noiseVal * (double)fuzzMultiplier) && (double)Math.abs(z) <= (double)this.rZ * (1.0D + noiseVal * (double)fuzzMultiplier)) {
                        Material[] original = this.types;
                        if (rel.getY() <= this.staticWaterLevel) {
                           this.types = new Material[]{Material.WATER};
                           BlockFace[] var13 = new BlockFace[]{BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST, BlockFace.DOWN};
                           int var14 = var13.length;

                           for(int var15 = 0; var15 < var14; ++var15) {
                              BlockFace face = var13[var15];
                              if (BlockUtils.isAir(rel.getRelative(face).getType())) {
                                 this.types = new Material[]{Material.STONE};
                              }
                           }
                        }

                        this.unitReplace(rel);
                        this.types = original;
                     }
                  }
               }
            }

         }
      }
   }

   private void unitReplace(@NotNull SimpleBlock rel) {
      if (this.replaceWhitelist.isEmpty()) {
         if (!this.hardReplace && rel.isSolid()) {
            return;
         }

         rel.setType((Material)GenUtils.randChoice(this.random, this.types));
      } else {
         if (!this.replaceWhitelist.contains(rel.getType())) {
            return;
         }

         rel.setType((Material)GenUtils.randChoice(this.random, this.types));
      }

      if (rel.getDown().isSolid()) {
         if (this.upperType != null) {
            rel.getUp().lsetType(this.upperType);
         }

         if (this.lowerType != null) {
            rel.getDown().setType(this.lowerType);
         }
      }

   }

   public static enum BoxType {
      UPPER_SEMIBOX,
      LOWER_SEMIBOX,
      FULL_BOX;

      // $FF: synthetic method
      private static BoxBuilder.BoxType[] $values() {
         return new BoxBuilder.BoxType[]{UPPER_SEMIBOX, LOWER_SEMIBOX, FULL_BOX};
      }
   }
}
