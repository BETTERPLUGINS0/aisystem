package advancedplugins.pm2.cv.api.vehicle.projectile.impacts;

import advancedplugins.pm2.cv.api.vehicle.projectile.impacts.impl.BreakBlockImpact;
import advancedplugins.pm2.cv.api.vehicle.projectile.impacts.impl.DamageEntityImpact;
import advancedplugins.pm2.cv.api.vehicle.projectile.impacts.impl.ExplosionImpact;

public class DefaultImpactEffects {
   public static ImpactEffect damageEntity(double var0) {
      return new DamageEntityImpact(var0);
   }

   public static ImpactEffect explode() {
      return new ExplosionImpact();
   }

   public static ImpactEffect explode(float var0) {
      return new ExplosionImpact((double)var0);
   }

   public static ImpactEffect breakBlock() {
      return new BreakBlockImpact();
   }
}
