package advancedplugins.pm2.cv.api.vehicle.projectile.projectiles;

import advancedplugins.pm2.cv.api.vehicle.projectile.impacts.ImpactEffect;
import advancedplugins.pm2.cv.api.vehicle.projectile.projectiles.impl.BlockProjectile;
import advancedplugins.pm2.cv.api.vehicle.projectile.projectiles.impl.EntityProjectile;
import advancedplugins.pm2.cv.api.vehicle.projectile.projectiles.impl.ParticleBeamProjectile;
import advancedplugins.pm2.cv.api.vehicle.projectile.projectiles.impl.ParticleGravityProjectile;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.EntityType;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DefaultProjectiles {
   public static ParticleGravityProjectile particleWithGravity(Particle var0) {
      return new ParticleGravityProjectile(var0);
   }

   public static ParticleGravityProjectile particleWithGravity(Particle var0, Location var1, float var2, float var3, ImpactEffect var4) {
      return new ParticleGravityProjectile(var0, var1, var2, var3, var4);
   }

   public static ParticleBeamProjectile particleBeamProjectile(Particle var0) {
      return new ParticleBeamProjectile(var0);
   }

   public static ParticleBeamProjectile particleBeamProjectile(Particle var0, Location var1, long var2, long var4, ImpactEffect var6) {
      return new ParticleBeamProjectile(var0, var1, var2, var4, var6);
   }

   public static BlockProjectile blockProjectile(Location var0, @Nullable Vector var1, @NotNull BlockData var2, @Nullable ImpactEffect var3) {
      return new BlockProjectile(var0, var1, var2, var3);
   }

   public static EntityProjectile entityProjectile(Location var0, @Nullable Vector var1, @NotNull EntityType var2, @Nullable ImpactEffect var3) {
      return new EntityProjectile(var0, var1, var2, var3);
   }
}
