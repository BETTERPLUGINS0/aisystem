package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.particle.type;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.particle.data.ParticleData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;

public class StaticParticleType<T extends ParticleData> extends AbstractMappedEntity implements ParticleType<T> {
   private final PacketWrapper.Reader<T> reader;
   private final PacketWrapper.Writer<T> writer;
   private final ParticleTypes.Decoder<T> decoder;
   private final ParticleTypes.Encoder<T> encoder;

   @ApiStatus.Internal
   public StaticParticleType(@Nullable TypesBuilderData data, PacketWrapper.Reader<T> reader, PacketWrapper.Writer<T> writer, ParticleTypes.Decoder<T> decoder, ParticleTypes.Encoder<T> encoder) {
      super(data);
      this.reader = reader;
      this.writer = writer;
      this.decoder = decoder;
      this.encoder = encoder;
   }

   public T readData(PacketWrapper<?> wrapper) {
      return (ParticleData)this.reader.apply(wrapper);
   }

   public void writeData(PacketWrapper<?> wrapper, T data) {
      if (this.writer != null) {
         this.writer.accept(wrapper, data);
      } else if (!data.isEmpty()) {
         throw new UnsupportedOperationException("Trying to write non-empty data for " + this.getName());
      }

   }

   public T decodeData(NBTCompound compound, ClientVersion version) {
      return (ParticleData)this.decoder.decode(compound, version);
   }

   public void encodeData(T value, ClientVersion version, NBTCompound compound) {
      if (this.encoder != null) {
         this.encoder.encode(value, version, compound);
      } else if (!value.isEmpty()) {
         throw new UnsupportedOperationException("Trying to encode non-empty data for " + this.getName());
      }

   }
}
