package org.terraform.utils;

import java.util.Random;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.terraform.data.SimpleBlock;
import org.terraform.utils.noise.FastNoise;

public class CylinderBuilder {
   private final Random random;
   private final int seed;
   private final SimpleBlock core;
   private final Material[] types;
   private float rX = 1.0F;
   private float rY = 1.0F;
   private float rZ = 1.0F;
   private float minRadius = 0.0F;
   private boolean singleBlockY = false;
   private boolean startFromZero = false;
   private boolean hardReplace = false;
   private Material[] upperType;
   private Material[] lowerType;
   private float noiseMagnitude = 0.7F;

   public CylinderBuilder(@NotNull Random random, SimpleBlock core, Material... types) {
      this.random = random;
      this.seed = random.nextInt(99999999);
      this.types = types;
      this.core = core;
   }

   @NotNull
   public CylinderBuilder setStartFromZero(boolean startFromZero) {
      this.startFromZero = startFromZero;
      return this;
   }

   @NotNull
   public CylinderBuilder setNoiseMagnitude(float mag) {
      this.noiseMagnitude = mag;
      return this;
   }

   @NotNull
   public CylinderBuilder setUpperType(Material... upperType) {
      this.upperType = upperType;
      return this;
   }

   @NotNull
   public CylinderBuilder setLowerType(Material... lowerType) {
      this.lowerType = lowerType;
      return this;
   }

   @NotNull
   public CylinderBuilder setRadius(float radius) {
      this.rX = radius;
      this.rY = radius;
      this.rZ = radius;
      return this;
   }

   @NotNull
   public CylinderBuilder setMinRadius(float minRadius) {
      this.minRadius = minRadius;
      return this;
   }

   @NotNull
   public CylinderBuilder setRX(float rX) {
      this.rX = rX;
      return this;
   }

   @NotNull
   public CylinderBuilder setRZ(float rZ) {
      this.rZ = rZ;
      return this;
   }

   @NotNull
   public CylinderBuilder setRY(float rY) {
      this.rY = rY;
      return this;
   }

   @NotNull
   public CylinderBuilder setSnowy() {
      this.upperType = new Material[]{Material.SNOW};
      return this;
   }

   @NotNull
   public CylinderBuilder setHardReplace(boolean hardReplace) {
      this.hardReplace = hardReplace;
      return this;
   }

   @NotNull
   public CylinderBuilder setSingleBlockY(boolean singleBlockY) {
      this.singleBlockY = singleBlockY;
      return this;
   }

   public void build() {
      if (!(this.rX <= 0.0F) || !(this.rY <= 0.0F) || !(this.rZ <= 0.0F)) {
         if ((double)this.rX <= 0.5D && (double)this.rY <= 0.5D && (double)this.rZ <= 0.5D) {
            this.unitReplace(this.core);
         } else {
            FastNoise noise = new FastNoise(this.seed);
            noise.SetNoiseType(FastNoise.NoiseType.Simplex);
            noise.SetFrequency(0.09F);
            float effectiveRY = this.rY;
            if (this.singleBlockY) {
               effectiveRY = 0.0F;
            }

            for(float x = -this.rX; x <= this.rX; ++x) {
               for(float y = this.startFromZero ? 0.0F : -effectiveRY; y <= effectiveRY; ++y) {
                  for(float z = -this.rZ; z <= this.rZ; ++z) {
                     SimpleBlock rel = this.core.getRelative(Math.round(x), Math.round(y), Math.round(z));
                     float effectiveY = y;
                     if ((double)(Math.abs(y) / this.rY) <= 0.7D) {
                        effectiveY = 0.0F;
                     }

                     double equationResult = Math.pow((double)x, 2.0D) / Math.pow((double)this.rX, 2.0D) + Math.pow((double)effectiveY, 2.0D) / Math.pow((double)this.rY, 2.0D) + Math.pow((double)z, 2.0D) / Math.pow((double)this.rZ, 2.0D);
                     double noiseFuzz;
                     if (this.noiseMagnitude > 0.0F) {
                        noiseFuzz = (double)(1.0F + this.noiseMagnitude * noise.GetNoise((float)rel.getX(), (float)rel.getY(), (float)rel.getZ()));
                     } else {
                        noiseFuzz = 1.0D;
                     }

                     if (noiseFuzz < (double)this.minRadius) {
                        noiseFuzz = (double)this.minRadius;
                     }

                     if (equationResult <= noiseFuzz) {
                        this.unitReplace(rel);
                     }
                  }
               }
            }

         }
      }
   }

   private void unitReplace(@NotNull SimpleBlock rel) {
      if (this.hardReplace || !rel.isSolid()) {
         rel.setType((Material)GenUtils.randChoice(this.random, this.types));
         if (this.upperType != null) {
            rel.getUp().lsetType(this.upperType);
         }

         if (this.lowerType != null && rel.getDown().isSolid()) {
            rel.getDown().setType(this.lowerType);
         }

      }
   }
}
