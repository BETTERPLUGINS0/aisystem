package com.volmit.iris.util.nbt.tag;

import com.volmit.iris.engine.data.io.MaxDepthIO;
import com.volmit.iris.util.collection.KMap;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.BiConsumer;

public class CompoundTag extends Tag<Map<String, Tag<?>>> implements Iterable<Entry<String, Tag<?>>>, Comparable<CompoundTag>, MaxDepthIO {
   public static final byte ID = 10;

   public CompoundTag() {
      super(createEmptyValue());
   }

   private static Map<String, Tag<?>> createEmptyValue() {
      return new KMap();
   }

   public byte getID() {
      return 10;
   }

   public int size() {
      return ((Map)this.getValue()).size();
   }

   public Tag<?> remove(String key) {
      return (Tag)((Map)this.getValue()).remove(var1);
   }

   public void clear() {
      ((Map)this.getValue()).clear();
   }

   public boolean containsKey(String key) {
      return ((Map)this.getValue()).containsKey(var1);
   }

   public boolean containsValue(Tag<?> value) {
      return ((Map)this.getValue()).containsValue(var1);
   }

   public Collection<Tag<?>> values() {
      return ((Map)this.getValue()).values();
   }

   public Set<String> keySet() {
      return ((Map)this.getValue()).keySet();
   }

   public Set<Entry<String, Tag<?>>> entrySet() {
      return new NonNullEntrySet(((Map)this.getValue()).entrySet());
   }

   public Iterator<Entry<String, Tag<?>>> iterator() {
      return this.entrySet().iterator();
   }

   public void forEach(BiConsumer<String, Tag<?>> action) {
      ((Map)this.getValue()).forEach(var1);
   }

   public <C extends Tag<?>> C get(String key, Class<C> type) {
      Tag var3 = (Tag)((Map)this.getValue()).get(var1);
      return var3 != null ? (Tag)var2.cast(var3) : null;
   }

   public Tag<?> get(String key) {
      return (Tag)((Map)this.getValue()).get(var1);
   }

   public ByteTag getByteTag(String key) {
      return (ByteTag)this.get(var1, ByteTag.class);
   }

   public ShortTag getShortTag(String key) {
      return (ShortTag)this.get(var1, ShortTag.class);
   }

   public IntTag getIntTag(String key) {
      return (IntTag)this.get(var1, IntTag.class);
   }

   public LongTag getLongTag(String key) {
      return (LongTag)this.get(var1, LongTag.class);
   }

   public FloatTag getFloatTag(String key) {
      return (FloatTag)this.get(var1, FloatTag.class);
   }

   public DoubleTag getDoubleTag(String key) {
      return (DoubleTag)this.get(var1, DoubleTag.class);
   }

   public StringTag getStringTag(String key) {
      return (StringTag)this.get(var1, StringTag.class);
   }

   public ByteArrayTag getByteArrayTag(String key) {
      return (ByteArrayTag)this.get(var1, ByteArrayTag.class);
   }

   public IntArrayTag getIntArrayTag(String key) {
      return (IntArrayTag)this.get(var1, IntArrayTag.class);
   }

   public LongArrayTag getLongArrayTag(String key) {
      return (LongArrayTag)this.get(var1, LongArrayTag.class);
   }

   public ListTag<?> getListTag(String key) {
      return (ListTag)this.get(var1, ListTag.class);
   }

   public CompoundTag getCompoundTag(String key) {
      return (CompoundTag)this.get(var1, CompoundTag.class);
   }

   public boolean getBoolean(String key) {
      Tag var2 = this.get(var1);
      return var2 instanceof ByteTag && ((ByteTag)var2).asByte() > 0;
   }

   public byte getByte(String key) {
      ByteTag var2 = this.getByteTag(var1);
      return var2 == null ? 0 : var2.asByte();
   }

   public short getShort(String key) {
      ShortTag var2 = this.getShortTag(var1);
      return var2 == null ? 0 : var2.asShort();
   }

   public int getInt(String key) {
      IntTag var2 = this.getIntTag(var1);
      return var2 == null ? 0 : var2.asInt();
   }

   public long getLong(String key) {
      LongTag var2 = this.getLongTag(var1);
      return var2 == null ? 0L : var2.asLong();
   }

   public float getFloat(String key) {
      FloatTag var2 = this.getFloatTag(var1);
      return var2 == null ? 0.0F : var2.asFloat();
   }

   public double getDouble(String key) {
      DoubleTag var2 = this.getDoubleTag(var1);
      return var2 == null ? 0.0D : var2.asDouble();
   }

   public String getString(String key) {
      StringTag var2 = this.getStringTag(var1);
      return var2 == null ? "" : var2.getValue();
   }

   public byte[] getByteArray(String key) {
      ByteArrayTag var2 = this.getByteArrayTag(var1);
      return var2 == null ? ByteArrayTag.ZERO_VALUE : (byte[])var2.getValue();
   }

   public int[] getIntArray(String key) {
      IntArrayTag var2 = this.getIntArrayTag(var1);
      return var2 == null ? IntArrayTag.ZERO_VALUE : (int[])var2.getValue();
   }

   public long[] getLongArray(String key) {
      LongArrayTag var2 = this.getLongArrayTag(var1);
      return var2 == null ? LongArrayTag.ZERO_VALUE : (long[])var2.getValue();
   }

   public Tag<?> put(String key, Tag<?> tag) {
      return (Tag)((Map)this.getValue()).put((String)Objects.requireNonNull(var1), (Tag)Objects.requireNonNull(var2));
   }

   public Tag<?> putBoolean(String key, boolean value) {
      return this.put(var1, new ByteTag(var2));
   }

   public Tag<?> putByte(String key, byte value) {
      return this.put(var1, new ByteTag(var2));
   }

   public Tag<?> putShort(String key, short value) {
      return this.put(var1, new ShortTag(var2));
   }

   public Tag<?> putInt(String key, int value) {
      return this.put(var1, new IntTag(var2));
   }

   public Tag<?> putLong(String key, long value) {
      return this.put(var1, new LongTag(var2));
   }

   public Tag<?> putFloat(String key, float value) {
      return this.put(var1, new FloatTag(var2));
   }

   public Tag<?> putDouble(String key, double value) {
      return this.put(var1, new DoubleTag(var2));
   }

   public Tag<?> putString(String key, String value) {
      return this.put(var1, new StringTag(var2));
   }

   public Tag<?> putByteArray(String key, byte[] value) {
      return this.put(var1, new ByteArrayTag(var2));
   }

   public Tag<?> putIntArray(String key, int[] value) {
      return this.put(var1, new IntArrayTag(var2));
   }

   public Tag<?> putLongArray(String key, long[] value) {
      return this.put(var1, new LongArrayTag(var2));
   }

   public String valueToString(int maxDepth) {
      StringBuilder var2 = new StringBuilder("{");
      boolean var3 = true;

      for(Iterator var4 = ((Map)this.getValue()).entrySet().iterator(); var4.hasNext(); var3 = false) {
         Entry var5 = (Entry)var4.next();
         var2.append(var3 ? "" : ",").append(escapeString((String)var5.getKey(), false)).append(":").append(((Tag)var5.getValue()).toString(this.decrementMaxDepth(var1)));
      }

      var2.append("}");
      return var2.toString();
   }

   public boolean equals(Object other) {
      if (this == var1) {
         return true;
      } else if (super.equals(var1) && this.size() == ((CompoundTag)var1).size()) {
         Iterator var2 = ((Map)this.getValue()).entrySet().iterator();

         Entry var3;
         Tag var4;
         do {
            if (!var2.hasNext()) {
               return true;
            }

            var3 = (Entry)var2.next();
         } while((var4 = ((CompoundTag)var1).get((String)var3.getKey())) != null && ((Tag)var3.getValue()).equals(var4));

         return false;
      } else {
         return false;
      }
   }

   public int compareTo(CompoundTag o) {
      return Integer.compare(this.size(), ((Map)var1.getValue()).size());
   }

   public CompoundTag clone() {
      CompoundTag var1 = new CompoundTag();
      Iterator var2 = ((Map)this.getValue()).entrySet().iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         var1.put((String)var3.getKey(), ((Tag)var3.getValue()).clone());
      }

      return var1;
   }
}
