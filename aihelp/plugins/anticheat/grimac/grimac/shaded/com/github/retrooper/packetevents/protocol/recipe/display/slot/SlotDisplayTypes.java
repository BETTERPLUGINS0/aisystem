package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.recipe.display.slot;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public final class SlotDisplayTypes {
   private static final VersionedRegistry<SlotDisplayType<?>> REGISTRY = new VersionedRegistry("slot_display");
   public static final SlotDisplayType<EmptySlotDisplay> EMPTY = register("empty", EmptySlotDisplay::read, EmptySlotDisplay::write);
   public static final SlotDisplayType<AnyFuelSlotDisplay> ANY_FUEL = register("any_fuel", AnyFuelSlotDisplay::read, AnyFuelSlotDisplay::write);
   public static final SlotDisplayType<ItemSlotDisplay> ITEM = register("item", ItemSlotDisplay::read, ItemSlotDisplay::write);
   public static final SlotDisplayType<ItemStackSlotDisplay> ITEM_STACK = register("item_stack", ItemStackSlotDisplay::read, ItemStackSlotDisplay::write);
   public static final SlotDisplayType<TagSlotDisplay> TAG = register("tag", TagSlotDisplay::read, TagSlotDisplay::write);
   public static final SlotDisplayType<SmithingTrimSlotDisplay> SMITHING_TRIM = register("smithing_trim", SmithingTrimSlotDisplay::read, SmithingTrimSlotDisplay::write);
   public static final SlotDisplayType<WithRemainderSlotDisplay> WITH_REMAINDER = register("with_remainder", WithRemainderSlotDisplay::read, WithRemainderSlotDisplay::write);
   public static final SlotDisplayType<CompositeSlotDisplay> COMPOSITE = register("composite", CompositeSlotDisplay::read, CompositeSlotDisplay::write);

   private SlotDisplayTypes() {
   }

   private static <T extends SlotDisplay<?>> SlotDisplayType<T> register(String id, PacketWrapper.Reader<T> reader, PacketWrapper.Writer<T> writer) {
      return (SlotDisplayType)REGISTRY.define(id, (data) -> {
         return new StaticSlotDisplayType(data, reader, writer);
      });
   }

   public static VersionedRegistry<SlotDisplayType<?>> getRegistry() {
      return REGISTRY;
   }

   static {
      REGISTRY.unloadMappings();
   }
}
