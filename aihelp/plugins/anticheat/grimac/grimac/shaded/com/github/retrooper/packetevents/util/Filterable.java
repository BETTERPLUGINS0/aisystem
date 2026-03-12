package ac.grim.grimac.shaded.com.github.retrooper.packetevents.util;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.util.Objects;
import java.util.Optional;

public class Filterable<T> {
   private T raw;
   @Nullable
   private T filtered;

   public Filterable(T raw) {
      this(raw, (Object)null);
   }

   public Filterable(T raw, Optional<T> filtered) {
      this(raw, filtered.orElse((Object)null));
   }

   public Filterable(T raw, @Nullable T filtered) {
      this.raw = raw;
      this.filtered = filtered;
   }

   public static <T> Filterable<T> read(PacketWrapper<?> wrapper, PacketWrapper.Reader<T> reader) {
      T raw = reader.apply(wrapper);
      T filtered = wrapper.readOptional(reader);
      return new Filterable(raw, filtered);
   }

   public static <T> void write(PacketWrapper<?> wrapper, Filterable<T> filterable, PacketWrapper.Writer<T> writer) {
      writer.accept(wrapper, filterable.raw);
      wrapper.writeOptional(filterable.filtered, writer);
   }

   public T getRaw() {
      return this.raw;
   }

   public void setRaw(T raw) {
      this.raw = raw;
   }

   @Nullable
   public T getFiltered() {
      return this.filtered;
   }

   public void setFiltered(@Nullable T filtered) {
      this.filtered = filtered;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (!(obj instanceof Filterable)) {
         return false;
      } else {
         Filterable<?> that = (Filterable)obj;
         return !this.raw.equals(that.raw) ? false : Objects.equals(this.filtered, that.filtered);
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.raw, this.filtered});
   }
}
