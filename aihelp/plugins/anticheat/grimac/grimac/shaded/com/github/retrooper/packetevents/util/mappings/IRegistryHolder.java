package ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;

@ApiStatus.Internal
public interface IRegistryHolder {
   default <T extends MappedEntity> IRegistry<T> getRegistryOr(IRegistry<T> fallbackRegistry) {
      return this.getRegistryOr(fallbackRegistry, PacketEvents.getAPI().getServerManager().getVersion().toClientVersion());
   }

   default <T extends MappedEntity> IRegistry<T> getRegistryOr(IRegistry<T> fallbackRegistry, ClientVersion version) {
      IRegistry<?> replacedRegistry = this.getRegistry(fallbackRegistry.getRegistryKey(), version);
      return replacedRegistry != null ? replacedRegistry : fallbackRegistry;
   }

   @Nullable
   default IRegistry<?> getRegistry(ResourceLocation registryKey) {
      return this.getRegistry(registryKey, PacketEvents.getAPI().getServerManager().getVersion().toClientVersion());
   }

   @Nullable
   IRegistry<?> getRegistry(ResourceLocation registryKey, ClientVersion version);
}
