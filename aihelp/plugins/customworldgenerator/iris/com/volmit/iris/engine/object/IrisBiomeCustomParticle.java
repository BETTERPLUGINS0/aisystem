package com.volmit.iris.engine.object;

import com.volmit.iris.engine.object.annotations.Desc;
import com.volmit.iris.engine.object.annotations.MaxNumber;
import com.volmit.iris.engine.object.annotations.MinNumber;
import com.volmit.iris.engine.object.annotations.Required;
import com.volmit.iris.engine.object.annotations.Snippet;
import lombok.Generated;
import org.bukkit.Particle;

@Snippet("custom-biome-particle")
@Desc("A custom biome ambient particle")
public class IrisBiomeCustomParticle {
   @Required
   @Desc("The biome's particle type")
   private Particle particle;
   @MinNumber(1.0D)
   @MaxNumber(10000.0D)
   @Desc("The rarity")
   private int rarity;

   @Generated
   public IrisBiomeCustomParticle() {
      this.particle = Particle.FLASH;
      this.rarity = 35;
   }

   @Generated
   public IrisBiomeCustomParticle(final Particle particle, final int rarity) {
      this.particle = Particle.FLASH;
      this.rarity = 35;
      this.particle = var1;
      this.rarity = var2;
   }

   @Generated
   public Particle getParticle() {
      return this.particle;
   }

   @Generated
   public int getRarity() {
      return this.rarity;
   }

   @Generated
   public IrisBiomeCustomParticle setParticle(final Particle particle) {
      this.particle = var1;
      return this;
   }

   @Generated
   public IrisBiomeCustomParticle setRarity(final int rarity) {
      this.rarity = var1;
      return this;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisBiomeCustomParticle)) {
         return false;
      } else {
         IrisBiomeCustomParticle var2 = (IrisBiomeCustomParticle)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (this.getRarity() != var2.getRarity()) {
            return false;
         } else {
            Particle var3 = this.getParticle();
            Particle var4 = var2.getParticle();
            if (var3 == null) {
               if (var4 != null) {
                  return false;
               }
            } else if (!var3.equals(var4)) {
               return false;
            }

            return true;
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof IrisBiomeCustomParticle;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      int var4 = var2 * 59 + this.getRarity();
      Particle var3 = this.getParticle();
      var4 = var4 * 59 + (var3 == null ? 43 : var3.hashCode());
      return var4;
   }

   @Generated
   public String toString() {
      String var10000 = String.valueOf(this.getParticle());
      return "IrisBiomeCustomParticle(particle=" + var10000 + ", rarity=" + this.getRarity() + ")";
   }
}
