package fr.xephi.authme.libs.com.google.common.collect;

import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import fr.xephi.authme.libs.com.google.common.annotations.VisibleForTesting;
import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import fr.xephi.authme.libs.com.google.errorprone.annotations.concurrent.LazyInit;
import fr.xephi.authme.libs.com.google.j2objc.annotations.RetainedWith;
import java.io.Serializable;
import java.util.Objects;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@GwtCompatible(
   serializable = true,
   emulated = true
)
class RegularImmutableBiMap<K, V> extends ImmutableBiMap<K, V> {
   static final RegularImmutableBiMap<Object, Object> EMPTY;
   static final double MAX_LOAD_FACTOR = 1.2D;
   @CheckForNull
   private final transient ImmutableMapEntry<K, V>[] keyTable;
   @CheckForNull
   private final transient ImmutableMapEntry<K, V>[] valueTable;
   @VisibleForTesting
   final transient Entry<K, V>[] entries;
   private final transient int mask;
   private final transient int hashCode;
   @LazyInit
   @CheckForNull
   @RetainedWith
   private transient ImmutableBiMap<V, K> inverse;

   static <K, V> ImmutableBiMap<K, V> fromEntries(Entry<K, V>... entries) {
      return fromEntryArray(entries.length, entries);
   }

   static <K, V> ImmutableBiMap<K, V> fromEntryArray(int n, Entry<K, V>[] entryArray) {
      Preconditions.checkPositionIndex(n, entryArray.length);
      int tableSize = Hashing.closedTableSize(n, 1.2D);
      int mask = tableSize - 1;
      ImmutableMapEntry<K, V>[] keyTable = ImmutableMapEntry.createEntryArray(tableSize);
      ImmutableMapEntry<K, V>[] valueTable = ImmutableMapEntry.createEntryArray(tableSize);
      Entry<K, V>[] entries = n == entryArray.length ? entryArray : ImmutableMapEntry.createEntryArray(n);
      int hashCode = 0;

      for(int i = 0; i < n; ++i) {
         Entry<K, V> entry = (Entry)Objects.requireNonNull(entryArray[i]);
         K key = entry.getKey();
         V value = entry.getValue();
         CollectPreconditions.checkEntryNotNull(key, value);
         int keyHash = key.hashCode();
         int valueHash = value.hashCode();
         int keyBucket = Hashing.smear(keyHash) & mask;
         int valueBucket = Hashing.smear(valueHash) & mask;
         ImmutableMapEntry<K, V> nextInKeyBucket = keyTable[keyBucket];
         ImmutableMapEntry nextInValueBucket = valueTable[valueBucket];

         try {
            RegularImmutableMap.checkNoConflictInKeyBucket(key, value, nextInKeyBucket, true);
            checkNoConflictInValueBucket(value, entry, nextInValueBucket);
         } catch (RegularImmutableMap.BucketOverflowException var19) {
            return JdkBackedImmutableBiMap.create(n, entryArray);
         }

         ImmutableMapEntry<K, V> newEntry = nextInValueBucket == null && nextInKeyBucket == null ? RegularImmutableMap.makeImmutable(entry, key, value) : new ImmutableMapEntry.NonTerminalImmutableBiMapEntry(key, value, nextInKeyBucket, nextInValueBucket);
         keyTable[keyBucket] = (ImmutableMapEntry)newEntry;
         valueTable[valueBucket] = (ImmutableMapEntry)newEntry;
         ((Object[])entries)[i] = newEntry;
         hashCode += keyHash ^ valueHash;
      }

      return new RegularImmutableBiMap(keyTable, valueTable, (Entry[])entries, mask, hashCode);
   }

   private RegularImmutableBiMap(@CheckForNull ImmutableMapEntry<K, V>[] keyTable, @CheckForNull ImmutableMapEntry<K, V>[] valueTable, Entry<K, V>[] entries, int mask, int hashCode) {
      this.keyTable = keyTable;
      this.valueTable = valueTable;
      this.entries = entries;
      this.mask = mask;
      this.hashCode = hashCode;
   }

   private static void checkNoConflictInValueBucket(Object value, Entry<?, ?> entry, @CheckForNull ImmutableMapEntry<?, ?> valueBucketHead) throws RegularImmutableMap.BucketOverflowException {
      for(int bucketSize = 0; valueBucketHead != null; valueBucketHead = valueBucketHead.getNextInValueBucket()) {
         checkNoConflict(!value.equals(valueBucketHead.getValue()), "value", entry, valueBucketHead);
         ++bucketSize;
         if (bucketSize > 8) {
            throw new RegularImmutableMap.BucketOverflowException();
         }
      }

   }

   @CheckForNull
   public V get(@CheckForNull Object key) {
      return RegularImmutableMap.get(key, this.keyTable, this.mask);
   }

   ImmutableSet<Entry<K, V>> createEntrySet() {
      return (ImmutableSet)(this.isEmpty() ? ImmutableSet.of() : new ImmutableMapEntrySet.RegularEntrySet(this, this.entries));
   }

   ImmutableSet<K> createKeySet() {
      return new ImmutableMapKeySet(this);
   }

   public void forEach(BiConsumer<? super K, ? super V> action) {
      Preconditions.checkNotNull(action);
      Entry[] var2 = this.entries;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Entry<K, V> entry = var2[var4];
         action.accept(entry.getKey(), entry.getValue());
      }

   }

   boolean isHashCodeFast() {
      return true;
   }

   public int hashCode() {
      return this.hashCode;
   }

   boolean isPartialView() {
      return false;
   }

   public int size() {
      return this.entries.length;
   }

   public ImmutableBiMap<V, K> inverse() {
      if (this.isEmpty()) {
         return ImmutableBiMap.of();
      } else {
         ImmutableBiMap<V, K> result = this.inverse;
         return result == null ? (this.inverse = new RegularImmutableBiMap.Inverse()) : result;
      }
   }

   static {
      EMPTY = new RegularImmutableBiMap((ImmutableMapEntry[])null, (ImmutableMapEntry[])null, ImmutableMap.EMPTY_ENTRY_ARRAY, 0, 0);
   }

   private static class InverseSerializedForm<K, V> implements Serializable {
      private final ImmutableBiMap<K, V> forward;
      private static final long serialVersionUID = 1L;

      InverseSerializedForm(ImmutableBiMap<K, V> forward) {
         this.forward = forward;
      }

      Object readResolve() {
         return this.forward.inverse();
      }
   }

   private final class Inverse extends ImmutableBiMap<V, K> {
      private Inverse() {
      }

      public int size() {
         return this.inverse().size();
      }

      public ImmutableBiMap<K, V> inverse() {
         return RegularImmutableBiMap.this;
      }

      public void forEach(BiConsumer<? super V, ? super K> action) {
         Preconditions.checkNotNull(action);
         RegularImmutableBiMap.this.forEach((k, v) -> {
            action.accept(v, k);
         });
      }

      @CheckForNull
      public K get(@CheckForNull Object value) {
         if (value != null && RegularImmutableBiMap.this.valueTable != null) {
            int bucket = Hashing.smear(value.hashCode()) & RegularImmutableBiMap.this.mask;

            for(ImmutableMapEntry entry = RegularImmutableBiMap.this.valueTable[bucket]; entry != null; entry = entry.getNextInValueBucket()) {
               if (value.equals(entry.getValue())) {
                  return entry.getKey();
               }
            }

            return null;
         } else {
            return null;
         }
      }

      ImmutableSet<V> createKeySet() {
         return new ImmutableMapKeySet(this);
      }

      ImmutableSet<Entry<V, K>> createEntrySet() {
         return new RegularImmutableBiMap.Inverse.InverseEntrySet();
      }

      boolean isPartialView() {
         return false;
      }

      Object writeReplace() {
         return new RegularImmutableBiMap.InverseSerializedForm(RegularImmutableBiMap.this);
      }

      // $FF: synthetic method
      Inverse(Object x1) {
         this();
      }

      final class InverseEntrySet extends ImmutableMapEntrySet<V, K> {
         ImmutableMap<V, K> map() {
            return Inverse.this;
         }

         boolean isHashCodeFast() {
            return true;
         }

         public int hashCode() {
            return RegularImmutableBiMap.this.hashCode;
         }

         public UnmodifiableIterator<Entry<V, K>> iterator() {
            return this.asList().iterator();
         }

         public void forEach(Consumer<? super Entry<V, K>> action) {
            this.asList().forEach(action);
         }

         ImmutableList<Entry<V, K>> createAsList() {
            return new ImmutableAsList<Entry<V, K>>() {
               public Entry<V, K> get(int index) {
                  Entry<K, V> entry = RegularImmutableBiMap.this.entries[index];
                  return Maps.immutableEntry(entry.getValue(), entry.getKey());
               }

               ImmutableCollection<Entry<V, K>> delegateCollection() {
                  return InverseEntrySet.this;
               }
            };
         }
      }
   }
}
