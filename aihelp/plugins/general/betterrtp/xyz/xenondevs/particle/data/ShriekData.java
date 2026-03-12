package xyz.xenondevs.particle.data;

import xyz.xenondevs.particle.ParticleConstants;
import xyz.xenondevs.particle.ParticleEffect;
import xyz.xenondevs.particle.utils.ReflectionUtils;

public final class ShriekData extends ParticleData {
   private final int delay;

   public ShriekData(int delay) {
      this.delay = delay;
   }

   public int getDelay() {
      return this.delay;
   }

   public Object toNMSData() {
      if (!(ReflectionUtils.MINECRAFT_VERSION < 19.0D) && this.getEffect() == ParticleEffect.SHRIEK) {
         try {
            return ParticleConstants.PARTICLE_PARAM_SHRIEK_CONSTRUCTOR.newInstance(this.getDelay());
         } catch (Exception var2) {
            return null;
         }
      } else {
         return null;
      }
   }
}
