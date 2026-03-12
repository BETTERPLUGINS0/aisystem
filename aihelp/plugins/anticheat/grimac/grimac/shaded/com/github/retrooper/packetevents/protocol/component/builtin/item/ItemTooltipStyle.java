package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.Objects;

public class ItemTooltipStyle {
   private ResourceLocation tooltipLoc;

   public ItemTooltipStyle(ResourceLocation tooltipLoc) {
      this.tooltipLoc = tooltipLoc;
   }

   public static ItemTooltipStyle read(PacketWrapper<?> wrapper) {
      return new ItemTooltipStyle(wrapper.readIdentifier());
   }

   public static void write(PacketWrapper<?> wrapper, ItemTooltipStyle style) {
      wrapper.writeIdentifier(style.tooltipLoc);
   }

   public ResourceLocation getTooltipLoc() {
      return this.tooltipLoc;
   }

   public void setTooltipLoc(ResourceLocation tooltipLoc) {
      this.tooltipLoc = tooltipLoc;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (!(obj instanceof ItemTooltipStyle)) {
         return false;
      } else {
         ItemTooltipStyle that = (ItemTooltipStyle)obj;
         return this.tooltipLoc.equals(that.tooltipLoc);
      }
   }

   public int hashCode() {
      return Objects.hashCode(this.tooltipLoc);
   }

   public String toString() {
      return "ItemTooltipStyle{tooltipLoc=" + this.tooltipLoc + '}';
   }
}
