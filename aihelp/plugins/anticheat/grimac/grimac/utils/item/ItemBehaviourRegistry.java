package ac.grim.grimac.utils.item;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.type.ItemType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import java.util.Map;

public class ItemBehaviourRegistry {
   private static final Map<ItemType, ItemBehaviour> ITEM_MAPPING;

   @NotNull
   public static ItemBehaviour getItemBehaviour(ItemType type) {
      return (ItemBehaviour)ITEM_MAPPING.getOrDefault(type, ItemBehaviour.INSTANCE);
   }

   static {
      ITEM_MAPPING = Map.of(ItemTypes.GOAT_HORN, AlwaysUseItem.INSTANCE, ItemTypes.SHIELD, AlwaysUseItem.INSTANCE, ItemTypes.SPYGLASS, AlwaysUseItem.INSTANCE, ItemTypes.CROSSBOW, UnsupportedItem.INSTANCE, ItemTypes.BOW, UnsupportedItem.INSTANCE, ItemTypes.TRIDENT, TridentItem.INSTANCE);
   }
}
