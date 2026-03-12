package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.util.function.Function;

public class StaticComponentType<T> extends AbstractMappedEntity implements ComponentType<T> {
   @Nullable
   private final PacketWrapper.Reader<T> reader;
   @Nullable
   private final PacketWrapper.Writer<T> writer;
   @Nullable
   private final ComponentType.Decoder<T> decoder;
   @Nullable
   private final ComponentType.Encoder<T> encoder;

   @ApiStatus.Internal
   public StaticComponentType(@Nullable TypesBuilderData data, @Nullable PacketWrapper.Reader<T> reader, @Nullable PacketWrapper.Writer<T> writer) {
      this(data, reader, writer, (ComponentType.Decoder)null, (ComponentType.Encoder)null);
   }

   @ApiStatus.Internal
   public StaticComponentType(@Nullable TypesBuilderData data, @Nullable ComponentType.Decoder<T> decoder, @Nullable ComponentType.Encoder<T> encoder) {
      this(data, (PacketWrapper.Reader)null, (PacketWrapper.Writer)null, decoder, encoder);
   }

   @ApiStatus.Internal
   public StaticComponentType(@Nullable TypesBuilderData data, @Nullable PacketWrapper.Reader<T> reader, @Nullable PacketWrapper.Writer<T> writer, @Nullable ComponentType.Decoder<T> decoder, @Nullable ComponentType.Encoder<T> encoder) {
      super(data);
      this.reader = reader;
      this.writer = writer;
      this.decoder = decoder;
      this.encoder = encoder;
   }

   public T read(PacketWrapper<?> wrapper) {
      return this.reader != null ? this.reader.apply(wrapper) : null;
   }

   public void write(PacketWrapper<?> wrapper, T content) {
      if (this.writer != null) {
         this.writer.accept(wrapper, content);
      }

   }

   public T decode(NBT nbt, ClientVersion version) {
      if (this.decoder != null) {
         return this.decoder.decode(nbt, version);
      } else {
         throw new UnsupportedOperationException();
      }
   }

   public NBT encode(T value, ClientVersion version) {
      if (this.encoder != null) {
         return this.encoder.encode(value, version);
      } else {
         throw new UnsupportedOperationException();
      }
   }

   public <Z> ComponentType<Z> legacyMap(Function<T, Z> mapper, Function<Z, T> unmapper) {
      PacketWrapper.Reader<Z> reader = this.reader != null ? (wrapper) -> {
         return mapper.apply(this.reader.apply(wrapper));
      } : null;
      PacketWrapper.Writer<Z> writer = this.writer != null ? (wrapper, value) -> {
         this.writer.accept(wrapper, unmapper.apply(value));
      } : null;
      ComponentType.Decoder<Z> decoder = this.decoder != null ? (nbt, version) -> {
         return mapper.apply(this.decoder.decode(nbt, version));
      } : null;
      ComponentType.Encoder<Z> encoder = this.encoder != null ? (value, version) -> {
         return this.encoder.encode(unmapper.apply(value), version);
      } : null;
      return new StaticComponentType(this.data, reader, writer, decoder, encoder);
   }
}
