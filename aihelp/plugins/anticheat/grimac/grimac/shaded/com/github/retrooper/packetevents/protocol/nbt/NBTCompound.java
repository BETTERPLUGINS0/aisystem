package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtCodecException;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtDecoder;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtEncoder;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.Contract;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Supplier;

public class NBTCompound extends NBT {
   protected final Map<String, NBT> tags = new LinkedHashMap();

   public NBTType<NBTCompound> getType() {
      return NBTType.COMPOUND;
   }

   public boolean isEmpty() {
      return this.tags.isEmpty();
   }

   public boolean contains(String key) {
      return this.tags.containsKey(key);
   }

   public Set<String> getTagNames() {
      return Collections.unmodifiableSet(this.tags.keySet());
   }

   public Map<String, NBT> getTags() {
      return Collections.unmodifiableMap(this.tags);
   }

   public int size() {
      return this.tags.size();
   }

   public NBT getTagOrThrow(String key) throws NbtCodecException {
      NBT tag = this.getTagOrNull(key);
      if (tag == null) {
         throw new NbtCodecException("Tag " + key + " doesn't exist");
      } else {
         return tag;
      }
   }

   @Nullable
   public NBT getTagOrNull(String key) {
      return (NBT)this.tags.get(key);
   }

   public <T extends NBT> T getTagOfTypeOrThrow(String key, Class<T> type) throws NbtCodecException {
      NBT tag = this.getTagOrThrow(key);
      if (type.isInstance(tag)) {
         return tag;
      } else {
         throw new NbtCodecException(MessageFormat.format("NBT {0} has unexpected type, expected {1}, but got {2}", key, type, tag.getClass()));
      }
   }

   @Nullable
   public <T extends NBT> T getTagOfTypeOrNull(String key, Class<T> type) {
      NBT tag = this.getTagOrNull(key);
      return type.isInstance(tag) ? tag : null;
   }

   public <T extends NBT> NBTList<T> getTagListOfTypeOrThrow(String key, Class<T> type) throws NbtCodecException {
      NBTList<? extends NBT> list = (NBTList)this.getTagOfTypeOrThrow(key, NBTList.class);
      if (!type.isAssignableFrom(list.getTagsType().getNBTClass())) {
         throw new NbtCodecException(MessageFormat.format("NBTList {0} tags type has unexpected type, expected {1}, but got {2}", key, type, list.getTagsType().getNBTClass()));
      } else {
         return list;
      }
   }

   @Nullable
   public <T extends NBT> NBTList<T> getTagListOfTypeOrNull(String key, Class<T> type) {
      NBTList<? extends NBT> list = (NBTList)this.getTagOfTypeOrNull(key, NBTList.class);
      return list != null && type.isAssignableFrom(list.getTagsType().getNBTClass()) ? list : null;
   }

   public NBTCompound getCompoundTagOrThrow(String key) {
      return (NBTCompound)this.getTagOfTypeOrThrow(key, NBTCompound.class);
   }

   @Nullable
   public NBTCompound getCompoundTagOrNull(String key) {
      return (NBTCompound)this.getTagOfTypeOrNull(key, NBTCompound.class);
   }

   public Number getNumberTagValueOrThrow(String key) {
      return this.getNumberTagOrThrow(key).getAsNumber();
   }

   @Nullable
   public Number getNumberTagValueOrNull(String key) {
      return this.getNumberTagValueOrDefault(key, (Number)null);
   }

   @Contract("_, !null -> !null")
   @Nullable
   public Number getNumberTagValueOrDefault(String key, @Nullable Number number) {
      NBTNumber tag = this.getNumberTagOrNull(key);
      return tag != null ? tag.getAsNumber() : number;
   }

   public NBTNumber getNumberTagOrThrow(String key) {
      return (NBTNumber)this.getTagOfTypeOrThrow(key, NBTNumber.class);
   }

   @Nullable
   public NBTNumber getNumberTagOrNull(String key) {
      return (NBTNumber)this.getTagOfTypeOrNull(key, NBTNumber.class);
   }

   public NBTString getStringTagOrThrow(String key) {
      return (NBTString)this.getTagOfTypeOrThrow(key, NBTString.class);
   }

   @Nullable
   public NBTString getStringTagOrNull(String key) {
      return (NBTString)this.getTagOfTypeOrNull(key, NBTString.class);
   }

   public NBTList<NBTCompound> getCompoundListTagOrThrow(String key) {
      return this.getTagListOfTypeOrThrow(key, NBTCompound.class);
   }

   @Nullable
   public NBTList<NBTCompound> getCompoundListTagOrNull(String key) {
      return this.getTagListOfTypeOrNull(key, NBTCompound.class);
   }

   public NBTList<NBTNumber> getNumberTagListTagOrThrow(String key) {
      return this.getTagListOfTypeOrThrow(key, NBTNumber.class);
   }

   @Nullable
   public NBTList<NBTNumber> getNumberListTagOrNull(String key) {
      return this.getTagListOfTypeOrNull(key, NBTNumber.class);
   }

   public NBTList<NBTString> getStringListTagOrThrow(String key) {
      return this.getTagListOfTypeOrThrow(key, NBTString.class);
   }

   @Nullable
   public NBTList<NBTString> getStringListTagOrNull(String key) {
      return this.getTagListOfTypeOrNull(key, NBTString.class);
   }

   public String getStringTagValueOrThrow(String key) {
      return this.getStringTagOrThrow(key).getValue();
   }

   @Nullable
   public String getStringTagValueOrNull(String key) {
      NBT tag = this.getTagOrNull(key);
      return tag instanceof NBTString ? ((NBTString)tag).getValue() : null;
   }

   public String getStringTagValueOrDefault(String key, String defaultValue) {
      NBT tag = this.getTagOrNull(key);
      return tag instanceof NBTString ? ((NBTString)tag).getValue() : defaultValue;
   }

   public NBT removeTag(String key) {
      return (NBT)this.tags.remove(key);
   }

   public <T extends NBT> T removeTagAndReturnIfType(String key, Class<T> type) {
      NBT tag = this.removeTag(key);
      return type.isInstance(tag) ? tag : null;
   }

   public <T extends NBT> NBTList<T> removeTagAndReturnIfListType(String key, Class<T> type) {
      NBTList<?> list = (NBTList)this.removeTagAndReturnIfType(key, NBTList.class);
      return list != null && type.isAssignableFrom(list.getTagsType().getNBTClass()) ? list : null;
   }

   public void setTag(String key, NBT tag) {
      if (tag != null) {
         this.tags.put(key, tag);
      } else {
         this.tags.remove(key);
      }

   }

   public NBTCompound copy() {
      NBTCompound clone = new NBTCompound();
      Iterator var2 = this.tags.entrySet().iterator();

      while(var2.hasNext()) {
         Entry<String, NBT> entry = (Entry)var2.next();
         clone.setTag((String)entry.getKey(), ((NBT)entry.getValue()).copy());
      }

      return clone;
   }

   public boolean getBoolean(String string) {
      return this.getBooleanOr(string, false);
   }

   public boolean getBooleanOr(String string, boolean defaultValue) {
      NBTNumber nbtByte = (NBTNumber)this.getTagOfTypeOrNull(string, NBTNumber.class);
      return nbtByte != null ? nbtByte.getAsByte() != 0 : defaultValue;
   }

   public boolean getBooleanOrThrow(String string) {
      return ((NBTNumber)this.getTagOfTypeOrThrow(string, NBTNumber.class)).getAsByte() != 0;
   }

   @Contract("_, _, !null, _ -> !null")
   @Nullable
   public <T> T getOr(String key, NbtDecoder<T> decoder, @Nullable T def, PacketWrapper<?> wrapper) {
      NBT tag = this.getTagOrNull(key);
      return tag != null ? decoder.decode(tag, wrapper) : def;
   }

   @Contract("_, _, !null, _ -> !null")
   @Nullable
   public <T> T getOrSupply(String key, NbtDecoder<T> decoder, Supplier<T> def, PacketWrapper<?> wrapper) {
      NBT tag = this.getTagOrNull(key);
      return tag != null ? decoder.decode(tag, wrapper) : def.get();
   }

   @Nullable
   public <T> T getOrNull(String key, NbtDecoder<T> decoder, PacketWrapper<?> wrapper) {
      return this.getOr(key, decoder, (Object)null, wrapper);
   }

   public <T> T getOrThrow(String key, NbtDecoder<T> decoder, PacketWrapper<?> wrapper) {
      return decoder.decode(this.getTagOrThrow(key), wrapper);
   }

   @Contract("_, _, !null, _ -> !null")
   @Nullable
   public <T> List<T> getListOr(String key, NbtDecoder<T> decoder, @Nullable List<T> def, PacketWrapper<?> wrapper) {
      NBT tag = this.getTagOrNull(key);
      if (!(tag instanceof NBTList)) {
         if (tag != null) {
            List<T> list = new ArrayList(1);
            list.add(decoder.decode(tag, wrapper));
            return list;
         } else {
            return def;
         }
      } else {
         List<? extends NBT> tags = ((NBTList)tag).getTags();
         List<T> list = new ArrayList(tags.size());
         Iterator var8 = tags.iterator();

         while(var8.hasNext()) {
            NBT element = (NBT)var8.next();
            list.add(decoder.decode(element, wrapper));
         }

         return list;
      }
   }

   @Nullable
   public <T> List<T> getListOrNull(String key, NbtDecoder<T> decoder, PacketWrapper<?> wrapper) {
      return this.getListOr(key, decoder, (List)null, wrapper);
   }

   public <T> List<T> getListOrEmpty(String key, NbtDecoder<T> decoder, PacketWrapper<?> wrapper) {
      return this.getListOr(key, decoder, Collections.emptyList(), wrapper);
   }

   public <T> List<T> getListOrThrow(String key, NbtDecoder<T> decoder, PacketWrapper<?> wrapper) {
      List<T> list = this.getListOrNull(key, decoder, wrapper);
      if (list == null) {
         throw new IllegalStateException(MessageFormat.format("NBT {0} does not exist", key));
      } else {
         return list;
      }
   }

   public <T> void set(String key, T value, NbtEncoder<T> encoder, PacketWrapper<?> wrapper) {
      this.setTag(key, encoder.encode(wrapper, value));
   }

   public <T> void setList(String key, List<T> value, NbtEncoder<T> encoder, PacketWrapper<?> wrapper) {
      if (value.isEmpty()) {
         this.setTag(key, new NBTList(NBTType.END, 0));
      } else {
         NBT firstVal = encoder.encode(wrapper, value.get(0));
         int size = value.size();
         NBTList<?> list = new NBTList(firstVal.getType(), size);
         list.addTagUnsafe(firstVal);

         for(int i = 1; i < size; ++i) {
            list.addTagUnsafe(encoder.encode(wrapper, value.get(i)));
         }

         this.setTag(key, list);
      }

   }

   public <T> void setCompactList(String key, List<T> value, NbtEncoder<T> encoder, PacketWrapper<?> wrapper) {
      if (value.size() == 1) {
         this.set(key, value.get(0), encoder, wrapper);
      } else {
         this.setList(key, value, encoder, wrapper);
      }

   }

   public boolean equals(Object other) {
      if (other instanceof NBTCompound) {
         return this.isEmpty() && ((NBTCompound)other).isEmpty() ? true : this.tags.equals(((NBTCompound)other).tags);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.tags.hashCode();
   }

   public String toString() {
      return "Compound{" + this.tags + "}";
   }
}
