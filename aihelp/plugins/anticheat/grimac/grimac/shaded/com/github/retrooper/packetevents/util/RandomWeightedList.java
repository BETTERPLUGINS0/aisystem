package ac.grim.grimac.shaded.com.github.retrooper.packetevents.util;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.ComponentType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTInt;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTList;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtCodec;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtCodecException;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtMapCodec;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class RandomWeightedList<T> implements Iterable<RandomWeightedList.Entry<T>> {
   private List<RandomWeightedList.Entry<T>> entries;

   public RandomWeightedList() {
      this((List)(new ArrayList()));
   }

   public RandomWeightedList(List<RandomWeightedList.Entry<T>> entries) {
      this.entries = entries;
   }

   public RandomWeightedList(T entry, int weight) {
      this(new RandomWeightedList.Entry(entry, weight));
   }

   public RandomWeightedList(RandomWeightedList.Entry<T> entry) {
      this.entries = new ArrayList(1);
      this.entries.add(entry);
   }

   public static <T> NbtCodec<RandomWeightedList<T>> codec(NbtCodec<T> codec) {
      return RandomWeightedList.Entry.codec(codec).applyList().apply(RandomWeightedList::new, RandomWeightedList::getEntries);
   }

   /** @deprecated */
   @Deprecated
   public static <T> RandomWeightedList<T> decode(NBT nbt, ClientVersion version, ComponentType.Decoder<T> decoder) {
      ArrayList entries;
      if (nbt instanceof NBTCompound) {
         entries = new ArrayList(1);
         entries.add(RandomWeightedList.Entry.decode(nbt, version, decoder));
      } else {
         if (!(nbt instanceof NBTList)) {
            throw new UnsupportedOperationException("Can't decode " + nbt + " as random weighted list");
         }

         NBTList<?> list = (NBTList)nbt;
         entries = new ArrayList(list.size());
         Iterator var5 = list.getTags().iterator();

         while(var5.hasNext()) {
            NBT tag = (NBT)var5.next();
            entries.add(RandomWeightedList.Entry.decode(tag, version, decoder));
         }
      }

      return new RandomWeightedList(entries);
   }

   /** @deprecated */
   @Deprecated
   public static <T> NBT encode(RandomWeightedList<T> list, ClientVersion version, ComponentType.Encoder<T> encoder) {
      NBTList<NBTCompound> nbt = new NBTList(NBTType.COMPOUND, list.entries.size());
      Iterator var4 = list.entries.iterator();

      while(var4.hasNext()) {
         RandomWeightedList.Entry<T> entry = (RandomWeightedList.Entry)var4.next();
         nbt.addTag(RandomWeightedList.Entry.encode(entry, version, encoder));
      }

      return nbt;
   }

   public List<RandomWeightedList.Entry<T>> getEntries() {
      return this.entries;
   }

   public void setEntries(List<RandomWeightedList.Entry<T>> entries) {
      this.entries = entries;
   }

   public int size() {
      return this.entries.size();
   }

   public boolean isEmpty() {
      return this.entries.isEmpty();
   }

   public Iterator<RandomWeightedList.Entry<T>> iterator() {
      return this.entries.iterator();
   }

   public static final class Entry<T> {
      private final T data;
      private final int weight;

      public Entry(T data, int weight) {
         this.data = data;
         this.weight = weight;
      }

      public static <T> NbtCodec<RandomWeightedList.Entry<T>> codec(NbtCodec<T> codec) {
         return (new NbtMapCodec<RandomWeightedList.Entry<T>>() {
            public RandomWeightedList.Entry<T> decode(NBTCompound compound, PacketWrapper<?> wrapper) throws NbtCodecException {
               int weight = compound.getNumberTagOrThrow("weight").getAsInt();
               T data = compound.getOrThrow("data", codec, wrapper);
               return new RandomWeightedList.Entry(data, weight);
            }

            public void encode(NBTCompound compound, PacketWrapper<?> wrapper, RandomWeightedList.Entry<T> value) throws NbtCodecException {
               compound.setTag("weight", new NBTInt(value.weight));
               compound.set("data", value.data, codec, wrapper);
            }
         }).codec();
      }

      /** @deprecated */
      @Deprecated
      public static <T> RandomWeightedList.Entry<T> decode(NBT nbt, ClientVersion version, ComponentType.Decoder<T> decoder) {
         NBTCompound compound = (NBTCompound)nbt;
         int weight = compound.getNumberTagOrThrow("weight").getAsInt();
         T data = decoder.decode(compound.getTagOrThrow("data"), version);
         return new RandomWeightedList.Entry(data, weight);
      }

      /** @deprecated */
      @Deprecated
      public static <T> NBTCompound encode(RandomWeightedList.Entry<T> entry, ClientVersion version, ComponentType.Encoder<T> encoder) {
         NBTCompound compound = new NBTCompound();
         compound.setTag("weight", new NBTInt(entry.weight));
         compound.setTag("data", encoder.encode(entry.data, version));
         return compound;
      }

      public T getData() {
         return this.data;
      }

      public int getWeight() {
         return this.weight;
      }
   }
}
