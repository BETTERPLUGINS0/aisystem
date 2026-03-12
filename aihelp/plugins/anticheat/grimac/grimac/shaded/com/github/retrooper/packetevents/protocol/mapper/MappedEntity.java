package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;

public interface MappedEntity {
   ResourceLocation getName();

   int getId(ClientVersion version);

   default boolean isRegistered() {
      return true;
   }
}
