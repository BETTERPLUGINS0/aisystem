package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.debug;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class StaticDebugSubscription<T> extends AbstractMappedEntity implements DebugSubscription<T> {
   @Nullable
   private final PacketWrapper.Reader<T> reader;
   @Nullable
   private final PacketWrapper.Writer<T> writer;

   @ApiStatus.Internal
   public StaticDebugSubscription(@Nullable TypesBuilderData data, @Nullable PacketWrapper.Reader<T> reader, @Nullable PacketWrapper.Writer<T> writer) {
      super(data);
      this.reader = reader;
      this.writer = writer;
   }

   public T read(PacketWrapper<?> wrapper) {
      if (this.reader == null) {
         throw new IllegalStateException(this + " doesn't support network reading");
      } else {
         return this.reader.apply(wrapper);
      }
   }

   public void write(PacketWrapper<?> wrapper, T value) {
      if (this.writer == null) {
         throw new IllegalStateException(this + " doesn't support network writing");
      } else {
         this.writer.accept(wrapper, value);
      }
   }
}
