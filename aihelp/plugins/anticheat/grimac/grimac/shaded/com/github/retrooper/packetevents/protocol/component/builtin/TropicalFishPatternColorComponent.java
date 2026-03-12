package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.color.DyeColor;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.Objects;

public class TropicalFishPatternColorComponent {
   private DyeColor color;

   public TropicalFishPatternColorComponent(DyeColor color) {
      this.color = color;
   }

   public static TropicalFishPatternColorComponent read(PacketWrapper<?> wrapper) {
      DyeColor type = DyeColor.read(wrapper);
      return new TropicalFishPatternColorComponent(type);
   }

   public static void write(PacketWrapper<?> wrapper, TropicalFishPatternColorComponent component) {
      DyeColor.write(wrapper, component.color);
   }

   public DyeColor getDyeColor() {
      return this.color;
   }

   public void setDyeColor(DyeColor color) {
      this.color = color;
   }

   public boolean equals(Object obj) {
      if (!(obj instanceof TropicalFishPatternColorComponent)) {
         return false;
      } else {
         TropicalFishPatternColorComponent that = (TropicalFishPatternColorComponent)obj;
         return this.color.equals(that.color);
      }
   }

   public int hashCode() {
      return Objects.hashCode(this.color);
   }
}
