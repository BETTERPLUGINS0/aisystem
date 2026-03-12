package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.Objects;

public class ItemPotionDurationScale {
   private float scale;

   public ItemPotionDurationScale(float scale) {
      this.scale = scale;
   }

   public static ItemPotionDurationScale read(PacketWrapper<?> wrapper) {
      float scale = wrapper.readFloat();
      return new ItemPotionDurationScale(scale);
   }

   public static void write(PacketWrapper<?> wrapper, ItemPotionDurationScale scale) {
      wrapper.writeFloat(scale.scale);
   }

   public float getScale() {
      return this.scale;
   }

   public void setScale(float scale) {
      this.scale = scale;
   }

   public boolean equals(Object obj) {
      if (!(obj instanceof ItemPotionDurationScale)) {
         return false;
      } else {
         ItemPotionDurationScale that = (ItemPotionDurationScale)obj;
         return Float.compare(that.scale, this.scale) == 0;
      }
   }

   public int hashCode() {
      return Objects.hashCode(this.scale);
   }
}
