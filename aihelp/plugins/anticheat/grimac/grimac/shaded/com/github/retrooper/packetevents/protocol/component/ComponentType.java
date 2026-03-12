package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import java.util.function.Function;

public interface ComponentType<T> extends MappedEntity {
   T read(PacketWrapper<?> wrapper);

   void write(PacketWrapper<?> wrapper, T content);

   T decode(NBT nbt, ClientVersion version);

   NBT encode(T value, ClientVersion version);

   @ApiStatus.Internal
   <Z> ComponentType<Z> legacyMap(Function<T, Z> mapper, Function<Z, T> unmapper);

   public interface Encoder<T> {
      NBT encode(T value, ClientVersion version);
   }

   public interface Decoder<T> {
      T decode(NBT nbt, ClientVersion version);
   }
}
