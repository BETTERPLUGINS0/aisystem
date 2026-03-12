package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.color.DyeColor;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.Objects;

public class WolfCollarComponent {
   private DyeColor color;

   public WolfCollarComponent(DyeColor color) {
      this.color = color;
   }

   public static WolfCollarComponent read(PacketWrapper<?> wrapper) {
      DyeColor type = DyeColor.read(wrapper);
      return new WolfCollarComponent(type);
   }

   public static void write(PacketWrapper<?> wrapper, WolfCollarComponent component) {
      DyeColor.write(wrapper, component.color);
   }

   public DyeColor getDyeColor() {
      return this.color;
   }

   public void setDyeColor(DyeColor color) {
      this.color = color;
   }

   public boolean equals(Object obj) {
      if (!(obj instanceof WolfCollarComponent)) {
         return false;
      } else {
         WolfCollarComponent that = (WolfCollarComponent)obj;
         return this.color.equals(that.color);
      }
   }

   public int hashCode() {
      return Objects.hashCode(this.color);
   }
}
