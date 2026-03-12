package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.type;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.StaticComponentMap;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateType;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.util.Set;

public interface ItemType extends MappedEntity {
   int getMaxAmount();

   int getMaxDurability();

   default boolean isMusicDisc() {
      return this.hasAttribute(ItemTypes.ItemAttribute.MUSIC_DISC);
   }

   ItemType getCraftRemainder();

   @Nullable
   StateType getPlacedType();

   Set<ItemTypes.ItemAttribute> getAttributes();

   default boolean hasAttribute(ItemTypes.ItemAttribute attribute) {
      return this.getAttributes().contains(attribute);
   }

   /** @deprecated */
   @Deprecated
   default StaticComponentMap getComponents() {
      return this.getComponents(PacketEvents.getAPI().getServerManager().getVersion().toClientVersion());
   }

   default StaticComponentMap getComponents(ClientVersion clientVersion) {
      return StaticComponentMap.EMPTY;
   }
}
