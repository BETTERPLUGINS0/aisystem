package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface NbtCodec<T> extends NbtEncoder<T>, NbtDecoder<T> {
   static <T> NbtCodec<T> codec(NbtEncoder<T> encoder, NbtDecoder<T> decoder) {
      return new NbtCodec<T>() {
         public T decode(NBT nbt, PacketWrapper<?> wrapper) {
            return decoder.decode(nbt, wrapper);
         }

         public NBT encode(PacketWrapper<?> wrapper, T value) {
            return encoder.encode(wrapper, value);
         }
      };
   }

   default NbtCodec<T> validate(Predicate<T> predicate) {
      return new NbtCodec<T>() {
         public T decode(NBT nbt, PacketWrapper<?> wrapper) throws NbtCodecException {
            T val = NbtCodec.this.decode(nbt, wrapper);
            if (!predicate.test(val)) {
               throw new NbtCodecException("Decode predicate failed " + predicate);
            } else {
               return val;
            }
         }

         public NBT encode(PacketWrapper<?> wrapper, T value) throws NbtCodecException {
            if (!predicate.test(value)) {
               throw new NbtCodecException("Encode predicate failed " + predicate);
            } else {
               return NbtCodec.this.encode(wrapper, value);
            }
         }
      };
   }

   default <Z> NbtCodec<Z> apply(Function<T, Z> forward, Function<Z, T> back) {
      return new NbtCodec<Z>() {
         public Z decode(NBT nbt, PacketWrapper<?> wrapper) {
            return forward.apply(NbtCodec.this.decode(nbt, wrapper));
         }

         public NBT encode(PacketWrapper<?> wrapper, Z value) {
            return NbtCodec.this.encode(wrapper, back.apply(value));
         }
      };
   }

   default NbtCodec<List<T>> applyList() {
      return new NbtCodec<List<T>>() {
         public List<T> decode(NBT nbt, PacketWrapper<?> wrapper) throws NbtCodecException {
            try {
               List<? extends NBT> list = (List)NbtCodecs.GENERIC_LIST.decode(nbt, wrapper);
               List<T> ret = new ArrayList(list.size());
               Iterator var5 = list.iterator();

               while(var5.hasNext()) {
                  NBT tag = (NBT)var5.next();
                  ret.add(NbtCodec.this.decode(tag, wrapper));
               }

               return ret;
            } catch (NbtCodecException var8) {
               try {
                  T element = NbtCodec.this.decode(nbt, wrapper);
                  return Collections.singletonList(element);
               } catch (NbtCodecException var7) {
                  var8.addSuppressed(var7);
                  throw var8;
               }
            }
         }

         public NBT encode(PacketWrapper<?> wrapper, List<T> value) {
            List<NBT> list = new ArrayList(value.size());
            Iterator var4 = value.iterator();

            while(var4.hasNext()) {
               T ele = var4.next();
               list.add(NbtCodec.this.encode(wrapper, ele));
            }

            return NbtCodecs.GENERIC_LIST.encode(wrapper, list);
         }
      };
   }

   default NbtCodec<T> withAlternative(NbtDecoder<T> alternative) {
      return new NbtCodec<T>() {
         public T decode(NBT nbt, PacketWrapper<?> wrapper) throws NbtCodecException {
            try {
               return NbtCodec.this.decode(nbt, wrapper);
            } catch (NbtCodecException var6) {
               try {
                  return alternative.decode(nbt, wrapper);
               } catch (NbtCodecException var5) {
                  var6.addSuppressed(var5);
                  throw var6;
               }
            }
         }

         public NBT encode(PacketWrapper<?> wrapper, T value) throws NbtCodecException {
            return NbtCodec.this.encode(wrapper, value);
         }
      };
   }
}
