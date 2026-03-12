package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.IRegistry;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.IRegistryHolder;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface MappedEntityRefSet<T extends MappedEntity> {
   default MappedEntitySet<T> resolve(PacketWrapper<?> wrapper, IRegistry<T> registry) {
      ClientVersion version = wrapper.getServerVersion().toClientVersion();
      IRegistry<T> replacedRegistry = wrapper.getRegistryHolder().getRegistryOr(registry, version);
      return this.resolve(version, replacedRegistry);
   }

   default MappedEntitySet<T> resolve(ClientVersion version, IRegistryHolder registryHolder, IRegistry<T> registry) {
      IRegistry<T> replacedRegistry = registryHolder.getRegistryOr(registry, version);
      return this.resolve(version, replacedRegistry);
   }

   MappedEntitySet<T> resolve(ClientVersion version, IRegistry<T> registry);

   boolean isEmpty();
}
