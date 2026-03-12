package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.color.DyeColor;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.Objects;

public class TropicalFishBaseColorComponent {
   private DyeColor color;

   public TropicalFishBaseColorComponent(DyeColor color) {
      this.color = color;
   }

   public static TropicalFishBaseColorComponent read(PacketWrapper<?> wrapper) {
      DyeColor type = DyeColor.read(wrapper);
      return new TropicalFishBaseColorComponent(type);
   }

   public static void write(PacketWrapper<?> wrapper, TropicalFishBaseColorComponent component) {
      DyeColor.write(wrapper, component.color);
   }

   public DyeColor getDyeColor() {
      return this.color;
   }

   public void setDyeColor(DyeColor color) {
      this.color = color;
   }

   public boolean equals(Object obj) {
      if (!(obj instanceof TropicalFishBaseColorComponent)) {
         return false;
      } else {
         TropicalFishBaseColorComponent that = (TropicalFishBaseColorComponent)obj;
         return this.color.equals(that.color);
      }
   }

   public int hashCode() {
      return Objects.hashCode(this.color);
   }
}
