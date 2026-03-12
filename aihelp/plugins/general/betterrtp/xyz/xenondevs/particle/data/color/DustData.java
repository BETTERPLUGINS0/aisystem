package xyz.xenondevs.particle.data.color;

import java.awt.Color;
import xyz.xenondevs.particle.ParticleConstants;
import xyz.xenondevs.particle.ParticleEffect;
import xyz.xenondevs.particle.PropertyType;
import xyz.xenondevs.particle.utils.ReflectionUtils;

public class DustData extends RegularColor {
   private final float size;

   public DustData(Color color, float size) {
      super(color);
      this.size = size;
   }

   public DustData(int red, int green, int blue, float size) {
      super(red, green, blue);
      this.size = size;
   }

   public float getSize() {
      return this.size;
   }

   public Object toNMSData() {
      try {
         if (ReflectionUtils.MINECRAFT_VERSION < 13.0D || this.getEffect() == null || !this.getEffect().hasProperty(PropertyType.DUST)) {
            return new int[0];
         }

         if (ReflectionUtils.MINECRAFT_VERSION < 17.0D && this.getEffect() == ParticleEffect.REDSTONE) {
            return ParticleConstants.PARTICLE_PARAM_REDSTONE_CONSTRUCTOR.newInstance(this.getRed(), this.getGreen(), this.getBlue(), this.getSize());
         }

         if (ReflectionUtils.MINECRAFT_VERSION >= 17.0D) {
            Object colorVector = ReflectionUtils.createVector3fa(this.getRed(), this.getGreen(), this.getBlue());
            return this.getEffect() == ParticleEffect.REDSTONE ? ParticleConstants.PARTICLE_PARAM_REDSTONE_CONSTRUCTOR.newInstance(colorVector, this.getSize()) : ParticleConstants.PARTICLE_PARAM_DUST_COLOR_TRANSITION_CONSTRUCTOR.newInstance(colorVector, colorVector, this.getSize());
         }
      } catch (Exception var2) {
      }

      return null;
   }
}
