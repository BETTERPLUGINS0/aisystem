package es.outlook.adriansrj.nbt.nbt.tag;

import es.outlook.adriansrj.nbt.io.MaxDepthIO;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class ListTag<T extends Tag<?>> extends Tag<List<T>> implements Iterable<T>, Comparable<ListTag<T>>, MaxDepthIO {
   public static final byte ID = 9;
   private Class<?> typeClass = null;

   private ListTag() {
      super(createEmptyValue(3));
   }

   public byte getID() {
      return 9;
   }

   public static ListTag<?> createUnchecked(Class<?> var0) {
      ListTag var1 = new ListTag();
      var1.typeClass = var0;
      return var1;
   }

   private static <T> List<T> createEmptyValue(int var0) {
      return new ArrayList(var0);
   }

   public ListTag(Class<? super T> var1) {
      super(createEmptyValue(3));
      if (var1 == EndTag.class) {
         throw new IllegalArgumentException("cannot create ListTag with EndTag elements");
      } else {
         this.typeClass = (Class)Objects.requireNonNull(var1);
      }
   }

   public Class<?> getTypeClass() {
      return this.typeClass == null ? EndTag.class : this.typeClass;
   }

   public int size() {
      return ((List)this.getValue()).size();
   }

   public T remove(int var1) {
      return (Tag)((List)this.getValue()).remove(var1);
   }

   public void clear() {
      ((List)this.getValue()).clear();
   }

   public boolean contains(T var1) {
      return ((List)this.getValue()).contains(var1);
   }

   public boolean containsAll(Collection<Tag<?>> var1) {
      return ((List)this.getValue()).containsAll(var1);
   }

   public void sort(Comparator<T> var1) {
      ((List)this.getValue()).sort(var1);
   }

   public Iterator<T> iterator() {
      return ((List)this.getValue()).iterator();
   }

   public void forEach(Consumer<? super T> var1) {
      ((List)this.getValue()).forEach(var1);
   }

   public T set(int var1, T var2) {
      return (Tag)((List)this.getValue()).set(var1, Objects.requireNonNull(var2));
   }

   public void add(T var1) {
      this.add(this.size(), var1);
   }

   public void add(int var1, T var2) {
      Objects.requireNonNull(var2);
      if (this.getTypeClass() == EndTag.class) {
         this.typeClass = var2.getClass();
      } else if (this.typeClass != var2.getClass()) {
         throw new ClassCastException(String.format("cannot add %s to ListTag<%s>", var2.getClass().getSimpleName(), this.typeClass.getSimpleName()));
      }

      ((List)this.getValue()).add(var1, var2);
   }

   public void addAll(Collection<T> var1) {
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         Tag var3 = (Tag)var2.next();
         this.add(var3);
      }

   }

   public void addAll(int var1, Collection<T> var2) {
      int var3 = 0;

      for(Iterator var4 = var2.iterator(); var4.hasNext(); ++var3) {
         Tag var5 = (Tag)var4.next();
         this.add(var1 + var3, var5);
      }

   }

   public void addBoolean(boolean var1) {
      this.addUnchecked(new ByteTag(var1));
   }

   public void addByte(byte var1) {
      this.addUnchecked(new ByteTag(var1));
   }

   public void addShort(short var1) {
      this.addUnchecked(new ShortTag(var1));
   }

   public void addInt(int var1) {
      this.addUnchecked(new IntTag(var1));
   }

   public void addLong(long var1) {
      this.addUnchecked(new LongTag(var1));
   }

   public void addFloat(float var1) {
      this.addUnchecked(new FloatTag(var1));
   }

   public void addDouble(double var1) {
      this.addUnchecked(new DoubleTag(var1));
   }

   public void addString(String var1) {
      this.addUnchecked(new StringTag(var1));
   }

   public void addByteArray(byte[] var1) {
      this.addUnchecked(new ByteArrayTag(var1));
   }

   public void addIntArray(int[] var1) {
      this.addUnchecked(new IntArrayTag(var1));
   }

   public void addLongArray(long[] var1) {
      this.addUnchecked(new LongArrayTag(var1));
   }

   public T get(int var1) {
      return (Tag)((List)this.getValue()).get(var1);
   }

   public int indexOf(T var1) {
      return ((List)this.getValue()).indexOf(var1);
   }

   public <L extends Tag<?>> ListTag<L> asTypedList(Class<L> var1) {
      this.checkTypeClass(var1);
      return this;
   }

   public ListTag<ByteTag> asByteTagList() {
      return this.asTypedList(ByteTag.class);
   }

   public ListTag<ShortTag> asShortTagList() {
      return this.asTypedList(ShortTag.class);
   }

   public ListTag<IntTag> asIntTagList() {
      return this.asTypedList(IntTag.class);
   }

   public ListTag<LongTag> asLongTagList() {
      return this.asTypedList(LongTag.class);
   }

   public ListTag<FloatTag> asFloatTagList() {
      return this.asTypedList(FloatTag.class);
   }

   public ListTag<DoubleTag> asDoubleTagList() {
      return this.asTypedList(DoubleTag.class);
   }

   public ListTag<StringTag> asStringTagList() {
      return this.asTypedList(StringTag.class);
   }

   public ListTag<ByteArrayTag> asByteArrayTagList() {
      return this.asTypedList(ByteArrayTag.class);
   }

   public ListTag<IntArrayTag> asIntArrayTagList() {
      return this.asTypedList(IntArrayTag.class);
   }

   public ListTag<LongArrayTag> asLongArrayTagList() {
      return this.asTypedList(LongArrayTag.class);
   }

   public ListTag<ListTag<?>> asListTagList() {
      this.checkTypeClass(ListTag.class);
      this.typeClass = ListTag.class;
      return this;
   }

   public ListTag<CompoundTag> asCompoundTagList() {
      return this.asTypedList(CompoundTag.class);
   }

   public String valueToString(int var1) {
      StringBuilder var2 = (new StringBuilder("{\"type\":\"")).append(this.getTypeClass().getSimpleName()).append("\",\"list\":[");

      for(int var3 = 0; var3 < this.size(); ++var3) {
         var2.append(var3 > 0 ? "," : "").append(this.get(var3).valueToString(this.decrementMaxDepth(var1)));
      }

      var2.append("]}");
      return var2.toString();
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (super.equals(var1) && this.size() == ((ListTag)var1).size() && this.getTypeClass() == ((ListTag)var1).getTypeClass()) {
         for(int var2 = 0; var2 < this.size(); ++var2) {
            if (!this.get(var2).equals(((ListTag)var1).get(var2))) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.getTypeClass().hashCode(), ((List)this.getValue()).hashCode()});
   }

   public int compareTo(ListTag<T> var1) {
      return Integer.compare(this.size(), ((List)var1.getValue()).size());
   }

   public ListTag<T> clone() {
      ListTag var1 = new ListTag();
      var1.typeClass = this.typeClass;
      Iterator var2 = ((List)this.getValue()).iterator();

      while(var2.hasNext()) {
         Tag var3 = (Tag)var2.next();
         var1.add(var3.clone());
      }

      return var1;
   }

   public void addUnchecked(Tag<?> var1) {
      if (this.getTypeClass() != EndTag.class && this.typeClass != var1.getClass()) {
         throw new IllegalArgumentException(String.format("cannot add %s to ListTag<%s>", var1.getClass().getSimpleName(), this.typeClass.getSimpleName()));
      } else {
         this.add(this.size(), var1);
      }
   }

   private void checkTypeClass(Class<?> var1) {
      if (this.getTypeClass() != EndTag.class && this.typeClass != var1) {
         throw new ClassCastException(String.format("cannot cast ListTag<%s> to ListTag<%s>", this.typeClass.getSimpleName(), var1.getSimpleName()));
      }
   }
}
