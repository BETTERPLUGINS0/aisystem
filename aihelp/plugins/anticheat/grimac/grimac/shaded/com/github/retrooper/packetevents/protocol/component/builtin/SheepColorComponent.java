package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.color.DyeColor;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.Objects;

public class SheepColorComponent {
   private DyeColor color;

   public SheepColorComponent(DyeColor color) {
      this.color = color;
   }

   public static SheepColorComponent read(PacketWrapper<?> wrapper) {
      DyeColor type = DyeColor.read(wrapper);
      return new SheepColorComponent(type);
   }

   public static void write(PacketWrapper<?> wrapper, SheepColorComponent component) {
      DyeColor.write(wrapper, component.color);
   }

   public DyeColor getDyeColor() {
      return this.color;
   }

   public void setDyeColor(DyeColor color) {
      this.color = color;
   }

   public boolean equals(Object obj) {
      if (!(obj instanceof SheepColorComponent)) {
         return false;
      } else {
         SheepColorComponent that = (SheepColorComponent)obj;
         return this.color.equals(that.color);
      }
   }

   public int hashCode() {
      return Objects.hashCode(this.color);
   }
}
