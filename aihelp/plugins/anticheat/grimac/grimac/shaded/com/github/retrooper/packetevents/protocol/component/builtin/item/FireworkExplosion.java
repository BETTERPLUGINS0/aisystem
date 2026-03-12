package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.List;
import java.util.Objects;

public class FireworkExplosion {
   private FireworkExplosion.Shape shape;
   private List<Integer> colors;
   private List<Integer> fadeColors;
   private boolean hasTrail;
   private boolean hasTwinkle;

   public FireworkExplosion(FireworkExplosion.Shape shape, List<Integer> colors, List<Integer> fadeColors, boolean hasTrail, boolean hasTwinkle) {
      this.shape = shape;
      this.colors = colors;
      this.fadeColors = fadeColors;
      this.hasTrail = hasTrail;
      this.hasTwinkle = hasTwinkle;
   }

   public static FireworkExplosion read(PacketWrapper<?> wrapper) {
      FireworkExplosion.Shape shape = (FireworkExplosion.Shape)wrapper.readEnum((Enum[])FireworkExplosion.Shape.values());
      List<Integer> colors = wrapper.readList(PacketWrapper::readInt);
      List<Integer> fadeColors = wrapper.readList(PacketWrapper::readInt);
      boolean hasTrail = wrapper.readBoolean();
      boolean hasTwinkle = wrapper.readBoolean();
      return new FireworkExplosion(shape, colors, fadeColors, hasTrail, hasTwinkle);
   }

   public static void write(PacketWrapper<?> wrapper, FireworkExplosion explosion) {
      wrapper.writeEnum(explosion.shape);
      wrapper.writeList(explosion.colors, PacketWrapper::writeInt);
      wrapper.writeList(explosion.fadeColors, PacketWrapper::writeInt);
      wrapper.writeBoolean(explosion.hasTrail);
      wrapper.writeBoolean(explosion.hasTwinkle);
   }

   public FireworkExplosion.Shape getShape() {
      return this.shape;
   }

   public void setShape(FireworkExplosion.Shape shape) {
      this.shape = shape;
   }

   public void addColor(int color) {
      this.colors.add(color);
   }

   public List<Integer> getColors() {
      return this.colors;
   }

   public void setColors(List<Integer> colors) {
      this.colors = colors;
   }

   public void addFadeColor(int color) {
      this.fadeColors.add(color);
   }

   public List<Integer> getFadeColors() {
      return this.fadeColors;
   }

   public void setFadeColors(List<Integer> fadeColors) {
      this.fadeColors = fadeColors;
   }

   public boolean isHasTrail() {
      return this.hasTrail;
   }

   public void setHasTrail(boolean hasTrail) {
      this.hasTrail = hasTrail;
   }

   public boolean isHasTwinkle() {
      return this.hasTwinkle;
   }

   public void setHasTwinkle(boolean hasTwinkle) {
      this.hasTwinkle = hasTwinkle;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (!(obj instanceof FireworkExplosion)) {
         return false;
      } else {
         FireworkExplosion that = (FireworkExplosion)obj;
         if (this.hasTrail != that.hasTrail) {
            return false;
         } else if (this.hasTwinkle != that.hasTwinkle) {
            return false;
         } else if (this.shape != that.shape) {
            return false;
         } else {
            return !this.colors.equals(that.colors) ? false : this.fadeColors.equals(that.fadeColors);
         }
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.shape, this.colors, this.fadeColors, this.hasTrail, this.hasTwinkle});
   }

   public static enum Shape {
      SMALL_BALL,
      LARGE_BALL,
      STAR,
      CREEPER,
      BURST;

      // $FF: synthetic method
      private static FireworkExplosion.Shape[] $values() {
         return new FireworkExplosion.Shape[]{SMALL_BALL, LARGE_BALL, STAR, CREEPER, BURST};
      }
   }
}
