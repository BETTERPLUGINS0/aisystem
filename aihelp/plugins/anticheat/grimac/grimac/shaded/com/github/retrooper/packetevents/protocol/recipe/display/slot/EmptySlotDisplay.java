package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.recipe.display.slot;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class EmptySlotDisplay extends SlotDisplay<EmptySlotDisplay> {
   public static final EmptySlotDisplay INSTANCE = new EmptySlotDisplay();

   private EmptySlotDisplay() {
      super(SlotDisplayTypes.EMPTY);
   }

   public static EmptySlotDisplay read(PacketWrapper<?> wrapper) {
      return INSTANCE;
   }

   public static void write(PacketWrapper<?> wrapper, EmptySlotDisplay display) {
   }
}
