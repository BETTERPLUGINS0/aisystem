package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.recipe.display.slot;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.IRegistry;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public abstract class SlotDisplay<T extends SlotDisplay<?>> {
   protected final SlotDisplayType<T> type;

   public SlotDisplay(SlotDisplayType<T> type) {
      this.type = type;
   }

   public static SlotDisplay<?> read(PacketWrapper<?> wrapper) {
      return ((SlotDisplayType)wrapper.readMappedEntity((IRegistry)SlotDisplayTypes.getRegistry())).read(wrapper);
   }

   public static <T extends SlotDisplay<?>> void write(PacketWrapper<?> wrapper, T display) {
      wrapper.writeMappedEntity(display.getType());
      display.getType().write(wrapper, display);
   }

   public SlotDisplayType<T> getType() {
      return this.type;
   }
}
