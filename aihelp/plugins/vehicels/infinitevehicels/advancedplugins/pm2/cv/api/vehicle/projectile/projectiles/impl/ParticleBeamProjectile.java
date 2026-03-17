package advancedplugins.pm2.cv.api.vehicle.projectile.projectiles.impl;

import advancedplugins.pm2.cv.api.util.ConfigurationUtil;
import advancedplugins.pm2.cv.api.vehicle.projectile.ProjectileBuilderEditableField;
import advancedplugins.pm2.cv.api.vehicle.projectile.impacts.DefaultImpactEffects;
import advancedplugins.pm2.cv.api.vehicle.projectile.impacts.ImpactEffect;
import advancedplugins.pm2.cv.api.vehicle.projectile.projectiles.ParticleVehicleProjectile;
import advancedplugins.pm2.cv.api.vehicle.projectile.projectiles.VehicleProjectile;
import java.util.Collection;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

public class ParticleBeamProjectile extends ParticleVehicleProjectile {
   private Location initialLocation;
   private boolean finished;
   @ProjectileBuilderEditableField
   private long durationTicks;
   @ProjectileBuilderEditableField
   private long pierceAmount;
   private ImpactEffect impact;

   public ParticleBeamProjectile(Particle var1, Location var2, long var3, long var5, ImpactEffect var7) {
      this.finished = false;
      this.initialLocation = var2;
      if (var7 == null) {
         this.impact = DefaultImpactEffects.damageEntity(2.0D);
      } else {
         this.impact = var7;
      }

      this.pierceAmount = var5;
      this.withParticleType(var1);
      this.durationTicks = var3;
   }

   public ParticleBeamProjectile(Particle var1) {
      this(var1, (Location)null, 5L, 0L, (ImpactEffect)null);
   }

   public ParticleBeamProjectile() {
      this(Particle.ASH, new Location((World)Bukkit.getWorlds().get(0), 0.0D, 0.0D, 0.0D), 5L, 0L, (ImpactEffect)null);
   }

   public VehicleProjectile.VehicleProjectileType getType() {
      return VehicleProjectile.VehicleProjectileType.PARTICLE_BEAM;
   }

   public void tick(long var1) {
      int var3 = 0;
      Location var4 = this.initialLocation.clone();

      for(int var5 = 0; var5 < 100; ++var5) {
         Vector var6 = var4.getDirection().multiply(this.particleDistanceBlocks);
         this.spawnParticle(var4, var6);
         if (!var4.getBlock().getType().isAir()) {
            this.getImpact().apply(var4.getBlock());
            ++var3;
            if ((long)var3 > this.pierceAmount) {
               break;
            }
         }

         Collection var7 = var4.getWorld().getNearbyEntities(var4, 1.0D, 1.0D, 1.0D, (var0) -> {
            return var0 instanceof LivingEntity;
         });
         if (!var7.isEmpty()) {
            this.getImpact().apply(var7);
            ++var3;
            if ((long)var3 > this.pierceAmount) {
               break;
            }
         }
      }

      if (var1 > this.durationTicks) {
         this.finish();
      }

   }

   public void spawnParticle(Location var1, Vector var2) {
      var1.getWorld().spawnParticle(this.particleType, var1.add(var2), 5, 0.0D, 0.0D, 0.0D, 0.02D, (Void)null, true);
   }

   public void finish() {
      this.finished = true;
   }

   public boolean isFinished() {
      return this.finished;
   }

   public ImpactEffect getImpact() {
      return this.impact;
   }

   public void setLocation(Location var1) {
      this.initialLocation = var1;
   }

   public Location getLocation() {
      return this.initialLocation;
   }

   public void write(ConfigurationSection var1) {
      ConfigurationUtil.writeEnum(this.getType(), var1, "type");
      this.impact.write(var1.createSection("impact-type"));
      var1.set("duration", this.durationTicks);
      var1.set("pierce-amount", this.pierceAmount);
      var1.set("particle-speed", this.particleSpeed);
      var1.set("particle-amount", this.particleAmount);
      var1.set("particle-distance-blocks", this.particleDistanceBlocks);
      ConfigurationUtil.writeEnum(this.particleType, var1, "particle");
   }

   public void load(ConfigurationSection var1) {
      this.impact = ImpactEffect.read(var1.getConfigurationSection("impact-type"));
      this.durationTicks = (long)var1.getInt("duration");
      this.pierceAmount = (long)var1.getInt("pierce-amount");
      this.particleSpeed = (float)var1.getDouble("particle-speed");
      this.particleAmount = var1.getInt("particle-amount");
      this.particleDistanceBlocks = (float)var1.getDouble("particle-distance-blocks");
      this.particleType = (Particle)ConfigurationUtil.loadEnum(Particle.class, var1, "particle");
   }

   public void setImpactType(ImpactEffect var1) {
      this.impact = var1;
   }

   public ParticleBeamProjectile copy() {
      return (ParticleBeamProjectile)(new ParticleBeamProjectile(this.particleType, this.initialLocation, this.durationTicks, this.pierceAmount, this.impact)).withParticleSpeed(this.particleSpeed).withParticleAmount(this.particleAmount).withParticleDistanceBlocks(this.particleDistanceBlocks);
   }
}
