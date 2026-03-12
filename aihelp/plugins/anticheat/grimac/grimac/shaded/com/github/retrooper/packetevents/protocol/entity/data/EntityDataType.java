package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.data;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class EntityDataType<T> extends AbstractMappedEntity {
   private final PacketWrapper.Reader<? extends T> reader;
   private final PacketWrapper.Writer<T> writer;

   @ApiStatus.Internal
   public EntityDataType(@Nullable TypesBuilderData data, PacketWrapper.Reader<? extends T> reader, PacketWrapper.Writer<T> writer) {
      super(data);
      this.reader = reader;
      this.writer = writer;
   }

   public T read(PacketWrapper<?> wrapper) {
      return this.reader.apply(wrapper);
   }

   public void write(PacketWrapper<?> wrapper, T serializer) {
      this.writer.accept(wrapper, serializer);
   }

   /** @deprecated */
   @Deprecated
   public Function<PacketWrapper<?>, T> getDataDeserializer() {
      PacketWrapper.Reader var10000 = this.reader;
      Objects.requireNonNull(var10000);
      return var10000::apply;
   }

   /** @deprecated */
   @Deprecated
   public BiConsumer<PacketWrapper<?>, T> getDataSerializer() {
      return this.writer;
   }
}
