package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.ComponentType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.ComponentTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.IRegistry;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

public class ItemTooltipDisplay {
   private boolean hideTooltip;
   private Set<ComponentType<?>> hiddenComponents;

   public ItemTooltipDisplay(boolean hideTooltip, Set<ComponentType<?>> hiddenComponents) {
      this.hideTooltip = hideTooltip;
      this.hiddenComponents = hiddenComponents;
   }

   public static ItemTooltipDisplay read(PacketWrapper<?> wrapper) {
      boolean hideTooltip = wrapper.readBoolean();
      Set<ComponentType<?>> hiddenComponents = (Set)wrapper.readCollection(LinkedHashSet::new, (ew) -> {
         return (ComponentType)ew.readMappedEntity((IRegistry)ComponentTypes.getRegistry());
      });
      return new ItemTooltipDisplay(hideTooltip, hiddenComponents);
   }

   public static void write(PacketWrapper<?> wrapper, ItemTooltipDisplay tooltipDisplay) {
      wrapper.writeBoolean(tooltipDisplay.hideTooltip);
      wrapper.writeCollection(tooltipDisplay.hiddenComponents, PacketWrapper::writeMappedEntity);
   }

   public boolean isHideTooltip() {
      return this.hideTooltip;
   }

   public void setHideTooltip(boolean hideTooltip) {
      this.hideTooltip = hideTooltip;
   }

   public Set<ComponentType<?>> getHiddenComponents() {
      return this.hiddenComponents;
   }

   public void setHiddenComponents(Set<ComponentType<?>> hiddenComponents) {
      this.hiddenComponents = hiddenComponents;
   }

   public boolean equals(Object obj) {
      if (!(obj instanceof ItemTooltipDisplay)) {
         return false;
      } else {
         ItemTooltipDisplay that = (ItemTooltipDisplay)obj;
         return this.hideTooltip != that.hideTooltip ? false : this.hiddenComponents.equals(that.hiddenComponents);
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.hideTooltip, this.hiddenComponents});
   }
}
