package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTString;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface NbtMapCodec<T> extends NbtMapEncoder<T>, NbtMapDecoder<T> {
   static <K, V> NbtMapCodec<Map<K, V>> codecOfMap(NbtCodec<K> keyCodec, Function<K, NbtCodec<? extends V>> valueCodec) {
      return new NbtMapCodec<Map<K, V>>() {
         public Map<K, V> decode(NBTCompound compound, PacketWrapper<?> wrapper) throws NbtCodecException {
            Map<K, V> map = new HashMap(compound.size());
            Iterator var4 = compound.getTags().entrySet().iterator();

            while(var4.hasNext()) {
               Entry<String, NBT> entry = (Entry)var4.next();
               K key = keyCodec.decode(new NBTString((String)entry.getKey()), wrapper);
               V value = ((NbtCodec)valueCodec.apply(key)).decode((NBT)entry.getValue(), wrapper);
               map.put(key, value);
            }

            return Collections.unmodifiableMap(map);
         }

         public void encode(NBTCompound compound, PacketWrapper<?> wrapper, Map<K, V> value) throws NbtCodecException {
            Iterator var4 = value.entrySet().iterator();

            while(var4.hasNext()) {
               Entry<K, V> entry = (Entry)var4.next();
               String name = ((NBTString)keyCodec.encode(wrapper, entry.getKey()).castOrThrow(NBTString.class)).getValue();
               NbtCodec<V> codec = (NbtCodec)valueCodec.apply(entry.getKey());
               NBT tag = codec.encode(wrapper, entry.getValue());
               compound.setTag(name, tag);
            }

         }
      };
   }

   default NbtCodec<T> codec() {
      return new NbtCodec<T>() {
         public T decode(NBT nbt, PacketWrapper<?> wrapper) throws NbtCodecException {
            NBTCompound compound = (NBTCompound)nbt.castOrThrow(NBTCompound.class);
            return NbtMapCodec.this.decode(compound, wrapper);
         }

         public NBT encode(PacketWrapper<?> wrapper, T value) throws NbtCodecException {
            NBTCompound compound = new NBTCompound();
            NbtMapCodec.this.encode(compound, wrapper, value);
            return compound;
         }
      };
   }

   default <Z> NbtMapCodec<Z> apply(Function<T, Z> forward, Function<Z, T> back) {
      return new NbtMapCodec<Z>() {
         public Z decode(NBTCompound compound, PacketWrapper<?> wrapper) throws NbtCodecException {
            return forward.apply(NbtMapCodec.this.decode(compound, wrapper));
         }

         public void encode(NBTCompound compound, PacketWrapper<?> wrapper, Z value) throws NbtCodecException {
            NbtMapCodec.this.encode(compound, wrapper, back.apply(value));
         }
      };
   }
}
