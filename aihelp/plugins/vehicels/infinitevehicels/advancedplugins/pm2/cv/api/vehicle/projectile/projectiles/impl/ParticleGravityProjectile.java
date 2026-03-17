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

public class ParticleGravityProjectile extends ParticleVehicleProjectile {
   private Location currentPosition;
   private Location initialPosition;
   private boolean finished;
   @ProjectileBuilderEditableField
   private float gravity;
   @ProjectileBuilderEditableField
   private float yForce;
   private ImpactEffect impact;

   public ParticleGravityProjectile(Particle var1, Location var2, float var3, float var4, ImpactEffect var5) {
      this.finished = false;
      this.currentPosition = var2;
      this.initialPosition = var2;
      this.gravity = var3;
      this.yForce = var4;
      if (var5 == null) {
         this.impact = DefaultImpactEffects.damageEntity(2.0D);
      } else {
         this.impact = var5;
      }

      this.withParticleType(var1);
   }

   public ParticleGravityProjectile(Particle var1) {
      this(var1, (Location)null, -0.006F, 0.0F, (ImpactEffect)null);
   }

   public ParticleGravityProjectile() {
      this(Particle.ASH, new Location((World)Bukkit.getWorlds().get(0), 0.0D, 0.0D, 0.0D), -0.006F, 0.0F, (ImpactEffect)null);
   }

   public ParticleGravityProjectile withGravity(float var1) {
      this.gravity = var1;
      return this;
   }

   public ParticleGravityProjectile withYForce(float var1) {
      this.yForce = var1;
      return this;
   }

   public VehicleProjectile.VehicleProjectileType getType() {
      return VehicleProjectile.VehicleProjectileType.PARTICLE_GRAVITY;
   }

   public void tick(long var1) {
      Vector var3 = this.currentPosition.getDirection();
      this.currentPosition.add(var3.add(new Vector(0.0F, this.yForce += this.gravity, 0.0F)));
      this.currentPosition.getWorld().spawnParticle(this.particleType, this.currentPosition, 5, 0.0D, 0.0D, 0.0D, (double)this.particleSpeed, (Void)null, true);
      if (!this.currentPosition.getBlock().getType().isAir()) {
         this.getImpact().apply(this.currentPosition.getBlock());
         this.finish();
      }

      Collection var4 = this.currentPosition.getWorld().getNearbyEntities(this.currentPosition, 1.0D, 1.0D, 1.0D, (var0) -> {
         return var0 instanceof LivingEntity;
      });
      if (!var4.isEmpty()) {
         this.getImpact().apply(var4);
         this.finish();
      }

      if (var1 > 1200L) {
         this.finish();
      }

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
      this.currentPosition = var1;
      this.initialPosition = var1;
   }

   public Location getLocation() {
      return this.initialPosition;
   }

   public void write(ConfigurationSection var1) {
      ConfigurationUtil.writeEnum(this.getType(), var1, "type");
      this.impact.write(var1.createSection("impact-type"));
      var1.set("gravity", this.gravity);
      var1.set("y-force", this.yForce);
      var1.set("particle-speed", this.particleSpeed);
      var1.set("particle-amount", this.particleAmount);
      var1.set("particle-distance-blocks", this.particleDistanceBlocks);
      ConfigurationUtil.writeEnum(this.particleType, var1, "particle");
   }

   public void load(ConfigurationSection var1) {
      this.impact = ImpactEffect.read(var1.getConfigurationSection("impact-type"));
      this.gravity = (float)var1.getDouble("gravity");
      this.yForce = (float)var1.getDouble("y-force");
      this.particleSpeed = (float)var1.getDouble("particle-speed");
      this.particleAmount = var1.getInt("particle-amount");
      this.particleDistanceBlocks = (float)var1.getDouble("particle-distance-blocks");
      this.particleType = (Particle)ConfigurationUtil.loadEnum(Particle.class, var1, "particle");
   }

   public void setImpactType(ImpactEffect var1) {
      this.impact = var1;
   }

   public ParticleGravityProjectile copy() {
      return (ParticleGravityProjectile)(new ParticleGravityProjectile(this.particleType, this.initialPosition, this.gravity, this.yForce, this.impact)).withParticleAmount(this.particleAmount).withParticleSpeed(this.particleSpeed).withParticleDistanceBlocks(this.particleDistanceBlocks);
   }
}
