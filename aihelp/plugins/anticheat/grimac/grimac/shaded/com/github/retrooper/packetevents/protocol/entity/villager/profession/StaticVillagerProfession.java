package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.villager.profession;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;

public class StaticVillagerProfession extends AbstractMappedEntity implements VillagerProfession {
   @ApiStatus.Internal
   public StaticVillagerProfession(@Nullable TypesBuilderData data) {
      super(data);
   }

   public int getId() {
      ServerVersion version = PacketEvents.getAPI().getServerManager().getVersion();
      return this.getId(version.toClientVersion());
   }
}
