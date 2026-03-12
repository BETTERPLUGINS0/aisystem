package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.positionsource;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.positionsource.builtin.BlockPositionSource;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.positionsource.builtin.EntityPositionSource;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;

public final class PositionSourceTypes {
   private static final VersionedRegistry<PositionSourceType<?>> REGISTRY = new VersionedRegistry("position_source_type");
   public static final PositionSourceType<BlockPositionSource> BLOCK = define("block", BlockPositionSource::read, BlockPositionSource::write, BlockPositionSource::decodeSource, BlockPositionSource::encodeSource);
   public static final PositionSourceType<EntityPositionSource> ENTITY = define("entity", EntityPositionSource::read, EntityPositionSource::write, EntityPositionSource::decodeSource, EntityPositionSource::encodeSource);

   private PositionSourceTypes() {
   }

   public static VersionedRegistry<PositionSourceType<?>> getRegistry() {
      return REGISTRY;
   }

   @ApiStatus.Internal
   public static <T extends PositionSource> PositionSourceType<T> define(String name, PacketWrapper.Reader<T> reader, PacketWrapper.Writer<T> writer, PositionSourceTypes.Decoder<T> decoder, PositionSourceTypes.Encoder<T> encoder) {
      return (PositionSourceType)REGISTRY.define(name, (data) -> {
         return new StaticPositionSourceType(data, reader, writer, decoder, encoder);
      });
   }

   @Nullable
   public static PositionSourceType<?> getByName(String name) {
      return (PositionSourceType)REGISTRY.getByName(name);
   }

   public static PositionSourceType<?> getById(ClientVersion version, int id) {
      return (PositionSourceType)REGISTRY.getById(version, id);
   }

   static {
      REGISTRY.unloadMappings();
   }

   @FunctionalInterface
   public interface Decoder<T> {
      T decode(NBTCompound compound, ClientVersion version);
   }

   @FunctionalInterface
   public interface Encoder<T> {
      void encode(T value, ClientVersion version, NBTCompound compound);
   }
}
