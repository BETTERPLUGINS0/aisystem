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

public class SphereBuilder {
   private final Random random;
   private final int seed;
   private final SimpleBlock core;
   @NotNull
   private final Collection<Material> replaceWhitelist = new ArrayList();
   private boolean isSmooth = false;
   private float rX = 1.0F;
   private float rY = 1.0F;
   private float rZ = 1.0F;
   private float padding = 0.0F;
   private double minRadius = 0.0D;
   private double maxRadius = 100.0D;
   private boolean hardReplace = false;
   private Material[] types;
   private Material[] containmentMaterial;
   private Material[] upperType;
   private Material[] lowerType;
   private int staticWaterLevel;
   private float sphereFrequency;
   private boolean doLiquidContainment;
   private SphereBuilder.SphereType sphereType;

   public SphereBuilder(@NotNull Random random, SimpleBlock core, Material... types) {
      this.containmentMaterial = new Material[]{Material.STONE};
      this.staticWaterLevel = -9999;
      this.sphereFrequency = 0.09F;
      this.doLiquidContainment = false;
      this.sphereType = SphereBuilder.SphereType.FULL_SPHERE;
      this.random = random;
      this.seed = random.nextInt(99999999);
      this.types = types;
      this.core = core;
   }

   @NotNull
   public SphereBuilder setSphereType(SphereBuilder.SphereType sphereType) {
      this.sphereType = sphereType;
      return this;
   }

   @NotNull
   public SphereBuilder setUpperType(Material... upperType) {
      this.upperType = upperType;
      return this;
   }

   @NotNull
   public SphereBuilder setLowerType(Material... lowerType) {
      this.lowerType = lowerType;
      return this;
   }

   @NotNull
   public SphereBuilder setStaticWaterLevel(int staticWaterLevel) {
      this.staticWaterLevel = staticWaterLevel;
      return this;
   }

   @NotNull
   public SphereBuilder addToWhitelist(@NotNull Material... mats) {
      this.replaceWhitelist.addAll(Arrays.asList(mats));
      return this;
   }

   @NotNull
   public SphereBuilder setRadius(float radius) {
      this.rX = radius;
      this.rY = radius;
      this.rZ = radius;
      return this;
   }

   @NotNull
   public SphereBuilder setSphereFrequency(float sphereFrequency) {
      this.sphereFrequency = sphereFrequency;
      return this;
   }

   @NotNull
   public SphereBuilder setRX(float rX) {
      this.rX = rX;
      return this;
   }

   @NotNull
   public SphereBuilder setRZ(float rZ) {
      this.rZ = rZ;
      return this;
   }

   @NotNull
   public SphereBuilder setRY(float rY) {
      this.rY = rY;
      return this;
   }

   @NotNull
   public SphereBuilder setSnowy() {
      this.upperType = new Material[]{Material.SNOW};
      return this;
   }

   @NotNull
   public SphereBuilder setHardReplace(boolean hardReplace) {
      this.hardReplace = hardReplace;
      return this;
   }

   @NotNull
   public SphereBuilder setDoLiquidContainment(boolean doLiquidContainment) {
      this.doLiquidContainment = doLiquidContainment;
      return this;
   }

   @NotNull
   public SphereBuilder setCointainmentMaterials(Material... containmentMaterial) {
      this.containmentMaterial = containmentMaterial;
      return this;
   }

   @NotNull
   public SphereBuilder setMinRadius(double minRadius) {
      this.minRadius = minRadius;
      return this;
   }

   @NotNull
   public SphereBuilder setMaxRadius(double maxRadius) {
      this.maxRadius = maxRadius;
      return this;
   }

   @NotNull
   public SphereBuilder setSmooth(boolean isSmooth) {
      this.isSmooth = isSmooth;
      return this;
   }

   @NotNull
   public SphereBuilder setPadding(int padding) {
      this.padding = (float)padding;
      return this;
   }

   public void build() {
      if (!(this.rX <= 0.0F) || !(this.rY <= 0.0F) || !(this.rZ <= 0.0F)) {
         if ((double)this.rX <= 0.5D && (double)this.rY <= 0.5D && (double)this.rZ <= 0.5D) {
            this.unitReplace(this.core, this.core.getY());
         } else {
            FastNoise noise = new FastNoise(this.seed);
            noise.SetNoiseType(FastNoise.NoiseType.Simplex);
            noise.SetFrequency(this.sphereFrequency);
            float effectiveRYLower = -this.rY;
            if (this.sphereType == SphereBuilder.SphereType.UPPER_SEMISPHERE) {
               effectiveRYLower = 0.0F;
            }

            float effectiveRYUpper = this.rY;
            if (this.sphereType == SphereBuilder.SphereType.LOWER_SEMISPHERE) {
               effectiveRYUpper = 0.0F;
            }

            for(float x = -this.rX - this.padding; x <= this.rX + this.padding; ++x) {
               for(float y = effectiveRYLower; y <= effectiveRYUpper; ++y) {
                  for(float z = -this.rZ - this.padding; z <= this.rZ + this.padding; ++z) {
                     SimpleBlock rel = this.core.getRelative(Math.round(x), Math.round(y), Math.round(z));
                     double equationResult = Math.pow((double)x, 2.0D) / Math.pow((double)this.rX, 2.0D) + Math.pow((double)y, 2.0D) / Math.pow((double)this.rY, 2.0D) + Math.pow((double)z, 2.0D) / Math.pow((double)this.rZ, 2.0D);
                     double noiseVal;
                     if (!this.isSmooth) {
                        noiseVal = 1.0D + 0.7D * (double)noise.GetNoise((float)rel.getX(), (float)rel.getY(), (float)rel.getZ());
                     } else {
                        noiseVal = 1.0D;
                     }

                     if (noiseVal < this.minRadius) {
                        noiseVal = this.minRadius;
                     }

                     if (noiseVal > this.maxRadius) {
                        noiseVal = this.maxRadius;
                     }

                     if (equationResult <= noiseVal) {
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

                        this.unitReplace(rel, (int)((float)this.core.getY() + effectiveRYUpper));
                        this.types = original;
                     }
                  }
               }
            }

         }
      }
   }

   private void unitReplace(@NotNull SimpleBlock rel, int effectiveRYUpper) {
      if (this.replaceWhitelist.isEmpty()) {
         if (this.hardReplace || !rel.isSolid()) {
            rel.setType((Material)GenUtils.randChoice(this.random, this.types));
            if (this.doLiquidContainment) {
               rel.replaceAdjacentNonLiquids(new BlockFace[]{BlockFace.DOWN, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST}, this.types[0], this.containmentMaterial);
            }
         }
      } else if (this.replaceWhitelist.contains(rel.getType())) {
         rel.setType((Material)GenUtils.randChoice(this.random, this.types));
         if (this.doLiquidContainment) {
            rel.replaceAdjacentNonLiquids(new BlockFace[]{BlockFace.DOWN, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST}, this.types[0], this.containmentMaterial);
         }
      }

      if (rel.isSolid()) {
         if (this.upperType != null && rel.getY() == effectiveRYUpper) {
            rel.getUp().lsetType(this.upperType);
         }

         if (this.lowerType != null) {
            rel.getDown().setType(this.lowerType);
         }
      }

   }

   public static enum SphereType {
      UPPER_SEMISPHERE,
      LOWER_SEMISPHERE,
      FULL_SPHERE;

      // $FF: synthetic method
      private static SphereBuilder.SphereType[] $values() {
         return new SphereBuilder.SphereType[]{UPPER_SEMISPHERE, LOWER_SEMISPHERE, FULL_SPHERE};
      }
   }
}
