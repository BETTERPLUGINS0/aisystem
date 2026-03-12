package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.positionsource;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;

public class StaticPositionSourceType<T extends PositionSource> extends AbstractMappedEntity implements PositionSourceType<T> {
   private final PacketWrapper.Reader<T> reader;
   private final PacketWrapper.Writer<T> writer;
   private final PositionSourceTypes.Decoder<T> decoder;
   private final PositionSourceTypes.Encoder<T> encoder;

   @ApiStatus.Internal
   public StaticPositionSourceType(@Nullable TypesBuilderData data, PacketWrapper.Reader<T> reader, PacketWrapper.Writer<T> writer, PositionSourceTypes.Decoder<T> decoder, PositionSourceTypes.Encoder<T> encoder) {
      super(data);
      this.reader = reader;
      this.writer = writer;
      this.decoder = decoder;
      this.encoder = encoder;
   }

   public T read(PacketWrapper<?> wrapper) {
      return (PositionSource)this.reader.apply(wrapper);
   }

   public void write(PacketWrapper<?> wrapper, T source) {
      this.writer.accept(wrapper, source);
   }

   public T decode(NBTCompound compound, ClientVersion version) {
      return (PositionSource)this.decoder.decode(compound, version);
   }

   public void encode(T source, ClientVersion version, NBTCompound compound) {
      this.encoder.encode(source, version, compound);
   }
}
