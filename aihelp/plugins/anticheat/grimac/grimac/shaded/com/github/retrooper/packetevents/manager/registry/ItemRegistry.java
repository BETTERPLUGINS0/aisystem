package ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.registry;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.type.ItemType;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;

public interface ItemRegistry {
   @Nullable
   ItemType getByName(String name);

   @Nullable
   ItemType getById(int id);
}
