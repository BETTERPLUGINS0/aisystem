package com.volmit.iris.engine.object;

import com.volmit.iris.Iris;
import com.volmit.iris.engine.data.cache.AtomicCache;
import com.volmit.iris.engine.framework.Engine;
import com.volmit.iris.engine.object.annotations.DependsOn;
import com.volmit.iris.engine.object.annotations.Desc;
import com.volmit.iris.engine.object.annotations.MaxNumber;
import com.volmit.iris.engine.object.annotations.MinNumber;
import com.volmit.iris.engine.object.annotations.Required;
import com.volmit.iris.engine.object.annotations.Snippet;
import com.volmit.iris.util.math.RNG;
import com.volmit.iris.util.scheduling.ChronoLatch;
import com.volmit.iris.util.scheduling.J;
import lombok.Generated;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

@Snippet("effect")
@Desc("An iris effect")
public class IrisEffect {
   private final transient AtomicCache<PotionEffectType> pt = new AtomicCache();
   private final transient AtomicCache<ChronoLatch> latch = new AtomicCache();
   @Desc("The potion effect to apply in this area")
   private String potionEffect = "";
   @Desc("The particle effect to apply in the area")
   private Particle particleEffect = null;
   @DependsOn({"particleEffect"})
   @MinNumber(-32.0D)
   @MaxNumber(32.0D)
   @Desc("Randomly offset from the surface to this surface+value")
   private int particleOffset = 0;
   @DependsOn({"particleEffect"})
   @MinNumber(-8.0D)
   @MaxNumber(8.0D)
   @Desc("The alt x, usually represents motion if the particle count is zero. Otherwise an offset.")
   private double particleAltX = 0.0D;
   @DependsOn({"particleEffect"})
   @MinNumber(-8.0D)
   @MaxNumber(8.0D)
   @Desc("The alt y, usually represents motion if the particle count is zero. Otherwise an offset.")
   private double particleAltY = 0.0D;
   @DependsOn({"particleEffect"})
   @MinNumber(-8.0D)
   @MaxNumber(8.0D)
   @Desc("The alt z, usually represents motion if the particle count is zero. Otherwise an offset.")
   private double particleAltZ = 0.0D;
   @DependsOn({"particleEffect"})
   @Desc("Randomize the altX by -altX to altX")
   private boolean randomAltX = true;
   @DependsOn({"particleEffect"})
   @Desc("Randomize the altY by -altY to altY")
   private boolean randomAltY = false;
   @DependsOn({"particleEffect"})
   @Desc("Randomize the altZ by -altZ to altZ")
   private boolean randomAltZ = true;
   @Desc("The sound to play")
   private Sound sound = null;
   @DependsOn({"sound"})
   @MinNumber(0.0D)
   @MaxNumber(512.0D)
   @Desc("The max distance from the player the sound will play")
   private int soundDistance = 12;
   @DependsOn({"sound", "maxPitch"})
   @MinNumber(0.01D)
   @MaxNumber(1.99D)
   @Desc("The minimum sound pitch")
   private double minPitch = 0.5D;
   @DependsOn({"sound", "minVolume"})
   @MinNumber(0.01D)
   @MaxNumber(1.99D)
   @Desc("The max sound pitch")
   private double maxPitch = 1.5D;
   @DependsOn({"sound"})
   @MinNumber(0.001D)
   @MaxNumber(512.0D)
   @Desc("The sound volume.")
   private double volume = 1.5D;
   @DependsOn({"particleEffect"})
   @MinNumber(0.0D)
   @MaxNumber(512.0D)
   @Desc("The particle count. Try setting to zero for using the alt xyz to a motion value instead of an offset")
   private int particleCount = 0;
   @DependsOn({"particleEffect"})
   @MinNumber(0.0D)
   @MaxNumber(64.0D)
   @Desc("How far away from the player particles can play")
   private int particleDistance = 20;
   @DependsOn({"particleEffect"})
   @MinNumber(0.0D)
   @MaxNumber(128.0D)
   @Desc("How wide the particles can play (player's view left and right) RADIUS")
   private int particleDistanceWidth = 24;
   @DependsOn({"particleEffect"})
   @Desc("An extra value for some particles... Which bukkit doesn't even document.")
   private double extra = 0.0D;
   @DependsOn({"potionEffect"})
   @MinNumber(-1.0D)
   @MaxNumber(1024.0D)
   @Desc("The Potion Strength or -1 to disable")
   private int potionStrength = -1;
   @DependsOn({"potionEffect", "potionTicksMin"})
   @MinNumber(1.0D)
   @Desc("The max time the potion will last for")
   private int potionTicksMax = 155;
   @DependsOn({"potionEffect", "potionTicksMax"})
   @MinNumber(1.0D)
   @Desc("The min time the potion will last for")
   private int potionTicksMin = 75;
   @Required
   @MinNumber(0.0D)
   @Desc("The effect interval in milliseconds")
   private int interval = 150;
   @DependsOn({"particleEffect"})
   @MinNumber(0.0D)
   @MaxNumber(16.0D)
   @Desc("The effect distance start away")
   private int particleAway = 5;
   @Required
   @MinNumber(1.0D)
   @Desc("The chance is 1 in CHANCE per interval")
   private int chance = 50;
   @Desc("Run commands, with configurable location parameters")
   private IrisCommandRegistry commandRegistry = null;

   public boolean canTick() {
      return ((ChronoLatch)this.latch.aquire(() -> {
         return new ChronoLatch((long)this.interval);
      })).flip();
   }

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

   public void apply(Player p, Engine g) {
      if (this.canTick()) {
         if (RNG.r.nextInt(this.chance) == 0) {
            Location var3;
            if (this.sound != null) {
               var3 = var1.getLocation().clone().add((double)RNG.r.i(-this.soundDistance, this.soundDistance), (double)RNG.r.i(-this.soundDistance, this.soundDistance), (double)RNG.r.i(-this.soundDistance, this.soundDistance));
               J.s(() -> {
                  var1.playSound(var3, this.getSound(), (float)this.volume, (float)RNG.r.d(this.minPitch, this.maxPitch));
               });
            }

            if (this.particleEffect != null) {
               var3 = var1.getLocation().clone().add(var1.getLocation().getDirection().clone().multiply(RNG.r.i(this.particleDistance) + this.particleAway)).clone().add(var1.getLocation().getDirection().clone().rotateAroundY(Math.toRadians(90.0D)).multiply(RNG.r.d((double)(-this.particleDistanceWidth), (double)this.particleDistanceWidth)));
               var3.setY((double)(Math.round((float)var2.getHeight(var3.getBlockX(), var3.getBlockZ())) + 1));
               var3.add(RNG.r.d(), 0.0D, RNG.r.d());
               int var4 = var1.getWorld().getMinHeight();
               if (this.extra != 0.0D) {
                  J.s(() -> {
                     var1.spawnParticle(this.particleEffect, var3.getX(), var3.getY() + (double)var4 + (double)RNG.r.i(this.particleOffset), var3.getZ(), this.particleCount, this.randomAltX ? RNG.r.d(-this.particleAltX, this.particleAltX) : this.particleAltX, this.randomAltY ? RNG.r.d(-this.particleAltY, this.particleAltY) : this.particleAltY, this.randomAltZ ? RNG.r.d(-this.particleAltZ, this.particleAltZ) : this.particleAltZ, this.extra);
                  });
               } else {
                  J.s(() -> {
                     var1.spawnParticle(this.particleEffect, var3.getX(), var3.getY() + (double)var4 + (double)RNG.r.i(this.particleOffset), var3.getZ(), this.particleCount, this.randomAltX ? RNG.r.d(-this.particleAltX, this.particleAltX) : this.particleAltX, this.randomAltY ? RNG.r.d(-this.particleAltY, this.particleAltY) : this.particleAltY, this.randomAltZ ? RNG.r.d(-this.particleAltZ, this.particleAltZ) : this.particleAltZ);
                  });
               }
            }

            if (this.commandRegistry != null) {
               this.commandRegistry.run(var1);
            }

            if (this.potionStrength > -1) {
               if (var1.hasPotionEffect(this.getRealType())) {
                  PotionEffect var5 = var1.getPotionEffect(this.getRealType());
                  if (var5.getAmplifier() > this.getPotionStrength()) {
                     return;
                  }

                  J.s(() -> {
                     var1.removePotionEffect(this.getRealType());
                  });
               }

               J.s(() -> {
                  var1.addPotionEffect(new PotionEffect(this.getRealType(), RNG.r.i(Math.min(this.potionTicksMax, this.potionTicksMin), Math.max(this.potionTicksMax, this.potionTicksMin)), this.getPotionStrength(), true, false, false));
               });
            }

         }
      }
   }

   public void apply(Entity p) {
      if (this.canTick()) {
         if (RNG.r.nextInt(this.chance) == 0) {
            Location var2;
            if (this.sound != null) {
               var2 = var1.getLocation().clone().add((double)RNG.r.i(-this.soundDistance, this.soundDistance), (double)RNG.r.i(-this.soundDistance, this.soundDistance), (double)RNG.r.i(-this.soundDistance, this.soundDistance));
               J.s(() -> {
                  var1.getWorld().playSound(var2, this.getSound(), (float)this.volume, (float)RNG.r.d(this.minPitch, this.maxPitch));
               });
            }

            if (this.particleEffect != null) {
               var2 = var1.getLocation().clone().add(0.0D, 0.25D, 0.0D).add((new Vector(1, 1, 1)).multiply(RNG.r.d())).subtract((new Vector(1, 1, 1)).multiply(RNG.r.d()));
               var2.add(RNG.r.d(), 0.0D, RNG.r.d());
               int var3 = var1.getWorld().getMinHeight();
               if (this.extra != 0.0D) {
                  J.s(() -> {
                     var1.getWorld().spawnParticle(this.particleEffect, var2.getX(), var2.getY() + (double)var3 + (double)RNG.r.i(this.particleOffset), var2.getZ(), this.particleCount, this.randomAltX ? RNG.r.d(-this.particleAltX, this.particleAltX) : this.particleAltX, this.randomAltY ? RNG.r.d(-this.particleAltY, this.particleAltY) : this.particleAltY, this.randomAltZ ? RNG.r.d(-this.particleAltZ, this.particleAltZ) : this.particleAltZ, this.extra);
                  });
               } else {
                  J.s(() -> {
                     var1.getWorld().spawnParticle(this.particleEffect, var2.getX(), var2.getY() + (double)var3 + (double)RNG.r.i(this.particleOffset), var2.getZ(), this.particleCount, this.randomAltX ? RNG.r.d(-this.particleAltX, this.particleAltX) : this.particleAltX, this.randomAltY ? RNG.r.d(-this.particleAltY, this.particleAltY) : this.particleAltY, this.randomAltZ ? RNG.r.d(-this.particleAltZ, this.particleAltZ) : this.particleAltZ);
                  });
               }
            }

         }
      }
   }

   @Generated
   public IrisEffect() {
   }

   @Generated
   public IrisEffect(final String potionEffect, final Particle particleEffect, final int particleOffset, final double particleAltX, final double particleAltY, final double particleAltZ, final boolean randomAltX, final boolean randomAltY, final boolean randomAltZ, final Sound sound, final int soundDistance, final double minPitch, final double maxPitch, final double volume, final int particleCount, final int particleDistance, final int particleDistanceWidth, final double extra, final int potionStrength, final int potionTicksMax, final int potionTicksMin, final int interval, final int particleAway, final int chance, final IrisCommandRegistry commandRegistry) {
      this.potionEffect = var1;
      this.particleEffect = var2;
      this.particleOffset = var3;
      this.particleAltX = var4;
      this.particleAltY = var6;
      this.particleAltZ = var8;
      this.randomAltX = var10;
      this.randomAltY = var11;
      this.randomAltZ = var12;
      this.sound = var13;
      this.soundDistance = var14;
      this.minPitch = var15;
      this.maxPitch = var17;
      this.volume = var19;
      this.particleCount = var21;
      this.particleDistance = var22;
      this.particleDistanceWidth = var23;
      this.extra = var24;
      this.potionStrength = var26;
      this.potionTicksMax = var27;
      this.potionTicksMin = var28;
      this.interval = var29;
      this.particleAway = var30;
      this.chance = var31;
      this.commandRegistry = var32;
   }

   @Generated
   public AtomicCache<PotionEffectType> getPt() {
      return this.pt;
   }

   @Generated
   public AtomicCache<ChronoLatch> getLatch() {
      return this.latch;
   }

   @Generated
   public String getPotionEffect() {
      return this.potionEffect;
   }

   @Generated
   public Particle getParticleEffect() {
      return this.particleEffect;
   }

   @Generated
   public int getParticleOffset() {
      return this.particleOffset;
   }

   @Generated
   public double getParticleAltX() {
      return this.particleAltX;
   }

   @Generated
   public double getParticleAltY() {
      return this.particleAltY;
   }

   @Generated
   public double getParticleAltZ() {
      return this.particleAltZ;
   }

   @Generated
   public boolean isRandomAltX() {
      return this.randomAltX;
   }

   @Generated
   public boolean isRandomAltY() {
      return this.randomAltY;
   }

   @Generated
   public boolean isRandomAltZ() {
      return this.randomAltZ;
   }

   @Generated
   public Sound getSound() {
      return this.sound;
   }

   @Generated
   public int getSoundDistance() {
      return this.soundDistance;
   }

   @Generated
   public double getMinPitch() {
      return this.minPitch;
   }

   @Generated
   public double getMaxPitch() {
      return this.maxPitch;
   }

   @Generated
   public double getVolume() {
      return this.volume;
   }

   @Generated
   public int getParticleCount() {
      return this.particleCount;
   }

   @Generated
   public int getParticleDistance() {
      return this.particleDistance;
   }

   @Generated
   public int getParticleDistanceWidth() {
      return this.particleDistanceWidth;
   }

   @Generated
   public double getExtra() {
      return this.extra;
   }

   @Generated
   public int getPotionStrength() {
      return this.potionStrength;
   }

   @Generated
   public int getPotionTicksMax() {
      return this.potionTicksMax;
   }

   @Generated
   public int getPotionTicksMin() {
      return this.potionTicksMin;
   }

   @Generated
   public int getInterval() {
      return this.interval;
   }

   @Generated
   public int getParticleAway() {
      return this.particleAway;
   }

   @Generated
   public int getChance() {
      return this.chance;
   }

   @Generated
   public IrisCommandRegistry getCommandRegistry() {
      return this.commandRegistry;
   }

   @Generated
   public IrisEffect setPotionEffect(final String potionEffect) {
      this.potionEffect = var1;
      return this;
   }

   @Generated
   public IrisEffect setParticleEffect(final Particle particleEffect) {
      this.particleEffect = var1;
      return this;
   }

   @Generated
   public IrisEffect setParticleOffset(final int particleOffset) {
      this.particleOffset = var1;
      return this;
   }

   @Generated
   public IrisEffect setParticleAltX(final double particleAltX) {
      this.particleAltX = var1;
      return this;
   }

   @Generated
   public IrisEffect setParticleAltY(final double particleAltY) {
      this.particleAltY = var1;
      return this;
   }

   @Generated
   public IrisEffect setParticleAltZ(final double particleAltZ) {
      this.particleAltZ = var1;
      return this;
   }

   @Generated
   public IrisEffect setRandomAltX(final boolean randomAltX) {
      this.randomAltX = var1;
      return this;
   }

   @Generated
   public IrisEffect setRandomAltY(final boolean randomAltY) {
      this.randomAltY = var1;
      return this;
   }

   @Generated
   public IrisEffect setRandomAltZ(final boolean randomAltZ) {
      this.randomAltZ = var1;
      return this;
   }

   @Generated
   public IrisEffect setSound(final Sound sound) {
      this.sound = var1;
      return this;
   }

   @Generated
   public IrisEffect setSoundDistance(final int soundDistance) {
      this.soundDistance = var1;
      return this;
   }

   @Generated
   public IrisEffect setMinPitch(final double minPitch) {
      this.minPitch = var1;
      return this;
   }

   @Generated
   public IrisEffect setMaxPitch(final double maxPitch) {
      this.maxPitch = var1;
      return this;
   }

   @Generated
   public IrisEffect setVolume(final double volume) {
      this.volume = var1;
      return this;
   }

   @Generated
   public IrisEffect setParticleCount(final int particleCount) {
      this.particleCount = var1;
      return this;
   }

   @Generated
   public IrisEffect setParticleDistance(final int particleDistance) {
      this.particleDistance = var1;
      return this;
   }

   @Generated
   public IrisEffect setParticleDistanceWidth(final int particleDistanceWidth) {
      this.particleDistanceWidth = var1;
      return this;
   }

   @Generated
   public IrisEffect setExtra(final double extra) {
      this.extra = var1;
      return this;
   }

   @Generated
   public IrisEffect setPotionStrength(final int potionStrength) {
      this.potionStrength = var1;
      return this;
   }

   @Generated
   public IrisEffect setPotionTicksMax(final int potionTicksMax) {
      this.potionTicksMax = var1;
      return this;
   }

   @Generated
   public IrisEffect setPotionTicksMin(final int potionTicksMin) {
      this.potionTicksMin = var1;
      return this;
   }

   @Generated
   public IrisEffect setInterval(final int interval) {
      this.interval = var1;
      return this;
   }

   @Generated
   public IrisEffect setParticleAway(final int particleAway) {
      this.particleAway = var1;
      return this;
   }

   @Generated
   public IrisEffect setChance(final int chance) {
      this.chance = var1;
      return this;
   }

   @Generated
   public IrisEffect setCommandRegistry(final IrisCommandRegistry commandRegistry) {
      this.commandRegistry = var1;
      return this;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisEffect)) {
         return false;
      } else {
         IrisEffect var2 = (IrisEffect)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (this.getParticleOffset() != var2.getParticleOffset()) {
            return false;
         } else if (Double.compare(this.getParticleAltX(), var2.getParticleAltX()) != 0) {
            return false;
         } else if (Double.compare(this.getParticleAltY(), var2.getParticleAltY()) != 0) {
            return false;
         } else if (Double.compare(this.getParticleAltZ(), var2.getParticleAltZ()) != 0) {
            return false;
         } else if (this.isRandomAltX() != var2.isRandomAltX()) {
            return false;
         } else if (this.isRandomAltY() != var2.isRandomAltY()) {
            return false;
         } else if (this.isRandomAltZ() != var2.isRandomAltZ()) {
            return false;
         } else if (this.getSoundDistance() != var2.getSoundDistance()) {
            return false;
         } else if (Double.compare(this.getMinPitch(), var2.getMinPitch()) != 0) {
            return false;
         } else if (Double.compare(this.getMaxPitch(), var2.getMaxPitch()) != 0) {
            return false;
         } else if (Double.compare(this.getVolume(), var2.getVolume()) != 0) {
            return false;
         } else if (this.getParticleCount() != var2.getParticleCount()) {
            return false;
         } else if (this.getParticleDistance() != var2.getParticleDistance()) {
            return false;
         } else if (this.getParticleDistanceWidth() != var2.getParticleDistanceWidth()) {
            return false;
         } else if (Double.compare(this.getExtra(), var2.getExtra()) != 0) {
            return false;
         } else if (this.getPotionStrength() != var2.getPotionStrength()) {
            return false;
         } else if (this.getPotionTicksMax() != var2.getPotionTicksMax()) {
            return false;
         } else if (this.getPotionTicksMin() != var2.getPotionTicksMin()) {
            return false;
         } else if (this.getInterval() != var2.getInterval()) {
            return false;
         } else if (this.getParticleAway() != var2.getParticleAway()) {
            return false;
         } else if (this.getChance() != var2.getChance()) {
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

            label104: {
               Particle var5 = this.getParticleEffect();
               Particle var6 = var2.getParticleEffect();
               if (var5 == null) {
                  if (var6 == null) {
                     break label104;
                  }
               } else if (var5.equals(var6)) {
                  break label104;
               }

               return false;
            }

            label97: {
               Sound var7 = this.getSound();
               Sound var8 = var2.getSound();
               if (var7 == null) {
                  if (var8 == null) {
                     break label97;
                  }
               } else if (var7.equals(var8)) {
                  break label97;
               }

               return false;
            }

            IrisCommandRegistry var9 = this.getCommandRegistry();
            IrisCommandRegistry var10 = var2.getCommandRegistry();
            if (var9 == null) {
               if (var10 != null) {
                  return false;
               }
            } else if (!var9.equals(var10)) {
               return false;
            }

            return true;
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof IrisEffect;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      int var21 = var2 * 59 + this.getParticleOffset();
      long var3 = Double.doubleToLongBits(this.getParticleAltX());
      var21 = var21 * 59 + (int)(var3 >>> 32 ^ var3);
      long var5 = Double.doubleToLongBits(this.getParticleAltY());
      var21 = var21 * 59 + (int)(var5 >>> 32 ^ var5);
      long var7 = Double.doubleToLongBits(this.getParticleAltZ());
      var21 = var21 * 59 + (int)(var7 >>> 32 ^ var7);
      var21 = var21 * 59 + (this.isRandomAltX() ? 79 : 97);
      var21 = var21 * 59 + (this.isRandomAltY() ? 79 : 97);
      var21 = var21 * 59 + (this.isRandomAltZ() ? 79 : 97);
      var21 = var21 * 59 + this.getSoundDistance();
      long var9 = Double.doubleToLongBits(this.getMinPitch());
      var21 = var21 * 59 + (int)(var9 >>> 32 ^ var9);
      long var11 = Double.doubleToLongBits(this.getMaxPitch());
      var21 = var21 * 59 + (int)(var11 >>> 32 ^ var11);
      long var13 = Double.doubleToLongBits(this.getVolume());
      var21 = var21 * 59 + (int)(var13 >>> 32 ^ var13);
      var21 = var21 * 59 + this.getParticleCount();
      var21 = var21 * 59 + this.getParticleDistance();
      var21 = var21 * 59 + this.getParticleDistanceWidth();
      long var15 = Double.doubleToLongBits(this.getExtra());
      var21 = var21 * 59 + (int)(var15 >>> 32 ^ var15);
      var21 = var21 * 59 + this.getPotionStrength();
      var21 = var21 * 59 + this.getPotionTicksMax();
      var21 = var21 * 59 + this.getPotionTicksMin();
      var21 = var21 * 59 + this.getInterval();
      var21 = var21 * 59 + this.getParticleAway();
      var21 = var21 * 59 + this.getChance();
      String var17 = this.getPotionEffect();
      var21 = var21 * 59 + (var17 == null ? 43 : var17.hashCode());
      Particle var18 = this.getParticleEffect();
      var21 = var21 * 59 + (var18 == null ? 43 : var18.hashCode());
      Sound var19 = this.getSound();
      var21 = var21 * 59 + (var19 == null ? 43 : var19.hashCode());
      IrisCommandRegistry var20 = this.getCommandRegistry();
      var21 = var21 * 59 + (var20 == null ? 43 : var20.hashCode());
      return var21;
   }

   @Generated
   public String toString() {
      String var10000 = String.valueOf(this.getPt());
      return "IrisEffect(pt=" + var10000 + ", latch=" + String.valueOf(this.getLatch()) + ", potionEffect=" + this.getPotionEffect() + ", particleEffect=" + String.valueOf(this.getParticleEffect()) + ", particleOffset=" + this.getParticleOffset() + ", particleAltX=" + this.getParticleAltX() + ", particleAltY=" + this.getParticleAltY() + ", particleAltZ=" + this.getParticleAltZ() + ", randomAltX=" + this.isRandomAltX() + ", randomAltY=" + this.isRandomAltY() + ", randomAltZ=" + this.isRandomAltZ() + ", sound=" + String.valueOf(this.getSound()) + ", soundDistance=" + this.getSoundDistance() + ", minPitch=" + this.getMinPitch() + ", maxPitch=" + this.getMaxPitch() + ", volume=" + this.getVolume() + ", particleCount=" + this.getParticleCount() + ", particleDistance=" + this.getParticleDistance() + ", particleDistanceWidth=" + this.getParticleDistanceWidth() + ", extra=" + this.getExtra() + ", potionStrength=" + this.getPotionStrength() + ", potionTicksMax=" + this.getPotionTicksMax() + ", potionTicksMin=" + this.getPotionTicksMin() + ", interval=" + this.getInterval() + ", particleAway=" + this.getParticleAway() + ", chance=" + this.getChance() + ", commandRegistry=" + String.valueOf(this.getCommandRegistry()) + ")";
   }
}
