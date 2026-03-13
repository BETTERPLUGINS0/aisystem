package com.volmit.iris.engine.object;

import com.volmit.iris.Iris;
import com.volmit.iris.engine.data.cache.AtomicCache;
import com.volmit.iris.engine.object.annotations.Desc;
import com.volmit.iris.engine.object.annotations.MaxNumber;
import com.volmit.iris.engine.object.annotations.MinNumber;
import com.volmit.iris.engine.object.annotations.Required;
import com.volmit.iris.engine.object.annotations.Snippet;
import lombok.Generated;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@Snippet("potion-effect")
@Desc("An iris potion effect")
public class IrisPotionEffect {
   private final transient AtomicCache<PotionEffectType> pt = new AtomicCache();
   @Required
   @Desc("The potion effect to apply in this area")
   private String potionEffect = "";
   @Required
   @MinNumber(-1.0D)
   @MaxNumber(1024.0D)
   @Desc("The Potion Strength or -1 to disable")
   private int strength = -1;
   @Required
   @MinNumber(1.0D)
   @Desc("The time the potion will last for")
   private int ticks = 200;
   @Desc("Is the effect ambient")
   private boolean ambient = false;
   @Desc("Is the effect showing particles")
   private boolean particles = true;

   public PotionEffectType getRealType() {
      return (PotionEffectType)this.pt.aquire(() -> {
         PotionEffectType var1 = PotionEffectType.LUCK;
         if (this.getPotionEffect().isEmpty()) {
            return var1;
         } else {
            try {
               PotionEffectType[] var2 = PotionEffectType.values();
               int var3 = var2.length;

               for(int var4 = 0; var4 < var3; ++var4) {
                  PotionEffectType var5 = var2[var4];
                  if (var5.getName().toUpperCase().replaceAll("\\Q \\E", "_").equals(this.getPotionEffect())) {
                     return var5;
                  }
               }
            } catch (Throwable var6) {
               Iris.reportError(var6);
            }

            Iris.warn("Unknown Potion Effect Type: " + this.getPotionEffect());
            return var1;
         }
      });
   }

   public void apply(LivingEntity p) {
      if (this.strength > -1) {
         if (var1.hasPotionEffect(this.getRealType())) {
            PotionEffect var2 = var1.getPotionEffect(this.getRealType());
            if (var2.getAmplifier() > this.strength) {
               return;
            }

            var1.removePotionEffect(this.getRealType());
         }

         var1.addPotionEffect(new PotionEffect(this.getRealType(), this.ticks, this.strength, this.ambient, this.particles, false));
      }

   }

   @Generated
   public IrisPotionEffect() {
   }

   @Generated
   public IrisPotionEffect(final String potionEffect, final int strength, final int ticks, final boolean ambient, final boolean particles) {
      this.potionEffect = var1;
      this.strength = var2;
      this.ticks = var3;
      this.ambient = var4;
      this.particles = var5;
   }

   @Generated
   public AtomicCache<PotionEffectType> getPt() {
      return this.pt;
   }

   @Generated
   public String getPotionEffect() {
      return this.potionEffect;
   }

   @Generated
   public int getStrength() {
      return this.strength;
   }

   @Generated
   public int getTicks() {
      return this.ticks;
   }

   @Generated
   public boolean isAmbient() {
      return this.ambient;
   }

   @Generated
   public boolean isParticles() {
      return this.particles;
   }

   @Generated
   public IrisPotionEffect setPotionEffect(final String potionEffect) {
      this.potionEffect = var1;
      return this;
   }

   @Generated
   public IrisPotionEffect setStrength(final int strength) {
      this.strength = var1;
      return this;
   }

   @Generated
   public IrisPotionEffect setTicks(final int ticks) {
      this.ticks = var1;
      return this;
   }

   @Generated
   public IrisPotionEffect setAmbient(final boolean ambient) {
      this.ambient = var1;
      return this;
   }

   @Generated
   public IrisPotionEffect setParticles(final boolean particles) {
      this.particles = var1;
      return this;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisPotionEffect)) {
         return false;
      } else {
         IrisPotionEffect var2 = (IrisPotionEffect)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (this.getStrength() != var2.getStrength()) {
            return false;
         } else if (this.getTicks() != var2.getTicks()) {
            return false;
         } else if (this.isAmbient() != var2.isAmbient()) {
            return false;
         } else if (this.isParticles() != var2.isParticles()) {
            return false;
         } else {
            String var3 = this.getPotionEffect();
            String var4 = var2.getPotionEffect();
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
      return var1 instanceof IrisPotionEffect;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      int var4 = var2 * 59 + this.getStrength();
      var4 = var4 * 59 + this.getTicks();
      var4 = var4 * 59 + (this.isAmbient() ? 79 : 97);
      var4 = var4 * 59 + (this.isParticles() ? 79 : 97);
      String var3 = this.getPotionEffect();
      var4 = var4 * 59 + (var3 == null ? 43 : var3.hashCode());
      return var4;
   }

   @Generated
   public String toString() {
      String var10000 = String.valueOf(this.getPt());
      return "IrisPotionEffect(pt=" + var10000 + ", potionEffect=" + this.getPotionEffect() + ", strength=" + this.getStrength() + ", ticks=" + this.getTicks() + ", ambient=" + this.isAmbient() + ", particles=" + this.isParticles() + ")";
   }
}
