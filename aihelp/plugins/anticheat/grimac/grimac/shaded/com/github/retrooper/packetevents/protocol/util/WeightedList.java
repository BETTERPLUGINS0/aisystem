package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.ArrayList;
import java.util.List;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class WeightedList<T> {
   private final List<WeightedList.Entry<T>> entries;

   public WeightedList() {
      this(new ArrayList());
   }

   public WeightedList(List<WeightedList.Entry<T>> entries) {
      this.entries = entries;
   }

   public static <T> WeightedList<T> read(PacketWrapper<?> wrapper, PacketWrapper.Reader<T> reader) {
      List<WeightedList.Entry<T>> entries = wrapper.readList((ew) -> {
         return WeightedList.Entry.read(wrapper, reader);
      });
      return new WeightedList(entries);
   }

   public static <T> void write(PacketWrapper<?> wrapper, WeightedList<T> list, PacketWrapper.Writer<T> writer) {
      wrapper.writeList(list.entries, (ew, entry) -> {
         WeightedList.Entry.write(ew, entry, writer);
      });
   }

   public List<WeightedList.Entry<T>> getEntries() {
      return this.entries;
   }

   public static final class Entry<T> {
      private final int weight;
      private final T value;

      public Entry(int weight, T value) {
         this.weight = weight;
         this.value = value;
      }

      public static <T> WeightedList.Entry<T> read(PacketWrapper<?> wrapper, PacketWrapper.Reader<T> reader) {
         int weight = wrapper.readVarInt();
         T value = reader.apply(wrapper);
         return new WeightedList.Entry(weight, value);
      }

      public static <T> void write(PacketWrapper<?> wrapper, WeightedList.Entry<T> entry, PacketWrapper.Writer<T> writer) {
         wrapper.writeVarInt(entry.weight);
         writer.accept(wrapper, entry.value);
      }

      public int getWeight() {
         return this.weight;
      }

      public T getValue() {
         return this.value;
      }
   }
}
