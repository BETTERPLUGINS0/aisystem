package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.data;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import java.util.List;

public interface EntityMetadataProvider {
   List<EntityData<?>> entityData(ClientVersion version);

   /** @deprecated */
   @Deprecated
   default List<EntityData<?>> entityData() {
      return this.entityData(ClientVersion.getLatest());
   }
}
