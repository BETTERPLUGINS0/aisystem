package xyz.xenondevs.particle.data.color;

import java.awt.Color;
import xyz.xenondevs.particle.ParticleConstants;
import xyz.xenondevs.particle.ParticleEffect;
import xyz.xenondevs.particle.utils.ReflectionUtils;

public final class DustColorTransitionData extends DustData {
   private final int fadeRed;
   private final int fadeGreen;
   private final int fadeBlue;

   public DustColorTransitionData(Color color, Color fadeColor, float size) {
      super(color, size);
      this.fadeRed = fadeColor.getRed();
      this.fadeGreen = fadeColor.getGreen();
      this.fadeBlue = fadeColor.getBlue();
   }

   public DustColorTransitionData(int red, int green, int blue, int fadeRed, int fadeGreen, int fadeBlue, float size) {
      super(red, green, blue, size);
      this.fadeRed = fadeRed;
      this.fadeGreen = fadeGreen;
      this.fadeBlue = fadeBlue;
   }

   public float getFadeRed() {
      return (float)this.fadeRed / 255.0F;
   }

   public float getFadeGreen() {
      return (float)this.fadeGreen / 255.0F;
   }

   public float getFadeBlue() {
      return (float)this.fadeBlue / 255.0F;
   }

   public Object toNMSData() {
      if (!(ReflectionUtils.MINECRAFT_VERSION < 17.0D) && this.getEffect() == ParticleEffect.DUST_COLOR_TRANSITION) {
         Object fadeStart = ReflectionUtils.createVector3fa(this.getRed(), this.getGreen(), this.getBlue());
         Object fadeEnd = ReflectionUtils.createVector3fa(this.getFadeRed(), this.getFadeGreen(), this.getFadeBlue());

         try {
            return ParticleConstants.PARTICLE_PARAM_DUST_COLOR_TRANSITION_CONSTRUCTOR.newInstance(fadeStart, fadeEnd, this.getSize());
         } catch (Exception var4) {
            return null;
         }
      } else {
         return null;
      }
   }
}
