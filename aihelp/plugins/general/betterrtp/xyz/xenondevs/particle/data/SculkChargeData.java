package xyz.xenondevs.particle.data;

import xyz.xenondevs.particle.ParticleConstants;
import xyz.xenondevs.particle.ParticleEffect;
import xyz.xenondevs.particle.utils.ReflectionUtils;

public final class SculkChargeData extends ParticleData {
   private final float roll;

   public SculkChargeData(float roll) {
      this.roll = roll;
   }

   public float getRoll() {
      return this.roll;
   }

   public Object toNMSData() {
      if (!(ReflectionUtils.MINECRAFT_VERSION < 19.0D) && this.getEffect() == ParticleEffect.SCULK_CHARGE) {
         try {
            return ParticleConstants.PARTICLE_PARAM_SCULK_CHARGE_CONSTRUCTOR.newInstance(this.getRoll());
         } catch (Exception var2) {
            return null;
         }
      } else {
         return null;
      }
   }
}
