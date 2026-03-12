package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.recipe.display.slot;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class AnyFuelSlotDisplay extends SlotDisplay<AnyFuelSlotDisplay> {
   public static final AnyFuelSlotDisplay INSTANCE = new AnyFuelSlotDisplay();

   private AnyFuelSlotDisplay() {
      super(SlotDisplayTypes.ANY_FUEL);
   }

   public static AnyFuelSlotDisplay read(PacketWrapper<?> wrapper) {
      return INSTANCE;
   }

   public static void write(PacketWrapper<?> wrapper, AnyFuelSlotDisplay display) {
   }
}
