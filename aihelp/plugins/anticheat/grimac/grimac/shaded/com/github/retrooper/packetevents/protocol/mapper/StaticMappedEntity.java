package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;

public interface StaticMappedEntity extends MappedEntity {
   int getId();

   default int getId(ClientVersion version) {
      return this.getId();
   }
}
