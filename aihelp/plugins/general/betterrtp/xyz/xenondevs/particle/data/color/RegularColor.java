package xyz.xenondevs.particle.data.color;

import java.awt.Color;
import xyz.xenondevs.particle.ParticleConstants;
import xyz.xenondevs.particle.ParticleEffect;
import xyz.xenondevs.particle.utils.MathUtils;
import xyz.xenondevs.particle.utils.ReflectionUtils;

public class RegularColor extends ParticleColor {
   public RegularColor(Color color) {
      super(color.getRed(), color.getGreen(), color.getBlue());
   }

   public RegularColor(int red, int green, int blue) {
      super(MathUtils.getMaxOrMin(red, 255, 0), MathUtils.getMaxOrMin(green, 255, 0), MathUtils.getMaxOrMin(blue, 255, 0));
   }

   public float getRed() {
      return super.getRed() / 255.0F;
   }

   public float getGreen() {
      return super.getGreen() / 255.0F;
   }

   public float getBlue() {
      return super.getBlue() / 255.0F;
   }

   public Object toNMSData() {
      if (!(ReflectionUtils.MINECRAFT_VERSION < 13.0D) && (this.getEffect() == ParticleEffect.REDSTONE || this.getEffect() == ParticleEffect.DUST_COLOR_TRANSITION)) {
         try {
            if (this.getEffect() == ParticleEffect.REDSTONE) {
               return ReflectionUtils.MINECRAFT_VERSION < 17.0D ? ParticleConstants.PARTICLE_PARAM_REDSTONE_CONSTRUCTOR.newInstance(this.getRed(), this.getGreen(), this.getBlue(), 1.0F) : ParticleConstants.PARTICLE_PARAM_REDSTONE_CONSTRUCTOR.newInstance(ReflectionUtils.createVector3fa(this.getRed(), this.getGreen(), this.getBlue()), 1.0F);
            } else if (ReflectionUtils.MINECRAFT_VERSION < 17.0D) {
               return null;
            } else {
               Object colorVector = ReflectionUtils.createVector3fa(this.getRed(), this.getGreen(), this.getBlue());
               return ParticleConstants.PARTICLE_PARAM_DUST_COLOR_TRANSITION_CONSTRUCTOR.newInstance(colorVector, colorVector, 1.0F);
            }
         } catch (Exception var2) {
            return null;
         }
      } else {
         return new int[0];
      }
   }

   public static RegularColor random() {
      return random(true);
   }

   public static RegularColor random(boolean highSaturation) {
      return highSaturation ? fromHSVHue(MathUtils.generateRandomInteger(0, 360)) : new RegularColor(new Color(MathUtils.RANDOM.nextInt(256), MathUtils.RANDOM.nextInt(256), MathUtils.RANDOM.nextInt(256)));
   }

   public static RegularColor fromHSVHue(int hue) {
      return new RegularColor(new Color(Color.HSBtoRGB((float)hue / 360.0F, 1.0F, 1.0F)));
   }
}
