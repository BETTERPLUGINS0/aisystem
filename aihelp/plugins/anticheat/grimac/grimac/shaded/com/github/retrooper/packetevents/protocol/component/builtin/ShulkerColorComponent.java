package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.color.DyeColor;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.Objects;

public class ShulkerColorComponent {
   private DyeColor color;

   public ShulkerColorComponent(DyeColor color) {
      this.color = color;
   }

   public static ShulkerColorComponent read(PacketWrapper<?> wrapper) {
      DyeColor type = DyeColor.read(wrapper);
      return new ShulkerColorComponent(type);
   }

   public static void write(PacketWrapper<?> wrapper, ShulkerColorComponent component) {
      DyeColor.write(wrapper, component.color);
   }

   public DyeColor getDyeColor() {
      return this.color;
   }

   public void setDyeColor(DyeColor color) {
      this.color = color;
   }

   public boolean equals(Object obj) {
      if (!(obj instanceof ShulkerColorComponent)) {
         return false;
      } else {
         ShulkerColorComponent that = (ShulkerColorComponent)obj;
         return this.color.equals(that.color);
      }
   }

   public int hashCode() {
      return Objects.hashCode(this.color);
   }
}
