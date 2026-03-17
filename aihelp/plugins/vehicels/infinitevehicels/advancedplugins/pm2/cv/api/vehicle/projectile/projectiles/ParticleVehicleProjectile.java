package advancedplugins.pm2.cv.api.vehicle.projectile.projectiles;

import advancedplugins.pm2.cv.api.vehicle.projectile.ProjectileBuilderEditableField;
import org.bukkit.Particle;

public abstract class ParticleVehicleProjectile implements VehicleProjectile {
   @ProjectileBuilderEditableField
   protected Particle particleType;
   @ProjectileBuilderEditableField
   protected int particleAmount;
   @ProjectileBuilderEditableField
   protected float particleSpeed;
   @ProjectileBuilderEditableField
   protected float particleDistanceBlocks;

   public ParticleVehicleProjectile() {
      this.particleType = Particle.ASH;
      this.particleAmount = 1;
      this.particleSpeed = 0.0F;
      this.particleDistanceBlocks = 1.0F;
   }

   public ParticleVehicleProjectile withParticleType(Particle var1) {
      this.particleType = var1;
      return this;
   }

   public ParticleVehicleProjectile withParticleAmount(int var1) {
      this.particleAmount = var1;
      return this;
   }

   public ParticleVehicleProjectile withParticleSpeed(float var1) {
      this.particleSpeed = var1;
      return this;
   }

   public ParticleVehicleProjectile withParticleDistanceBlocks(float var1) {
      this.particleDistanceBlocks = var1;
      return this;
   }
}
