package xyz.xenondevs.particle.data.color;

import xyz.xenondevs.particle.ParticleEffect;
import xyz.xenondevs.particle.utils.MathUtils;

public final class NoteColor extends ParticleColor {
   public NoteColor(int note) {
      super(MathUtils.getMaxOrMin(note, 24, 0), 0, 0);
      this.setEffect(ParticleEffect.NOTE);
   }

   public void setEffect(ParticleEffect effect) {
      super.setEffect(ParticleEffect.NOTE);
   }

   public float getRed() {
      return super.getRed() / 24.0F;
   }

   public float getGreen() {
      return 0.0F;
   }

   public float getBlue() {
      return 0.0F;
   }

   public Object toNMSData() {
      return new int[0];
   }

   public static NoteColor random() {
      return new NoteColor(MathUtils.generateRandomInteger(0, 24));
   }
}
