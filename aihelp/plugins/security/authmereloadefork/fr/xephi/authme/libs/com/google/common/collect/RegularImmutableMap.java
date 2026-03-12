package fr.xephi.authme.libs.com.google.common.collect;

import [Ljava.util.Map.Entry;;
import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import fr.xephi.authme.libs.com.google.common.annotations.GwtIncompatible;
import fr.xephi.authme.libs.com.google.common.annotations.VisibleForTesting;
import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import fr.xephi.authme.libs.com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.io.Serializable;
import java.util.IdentityHashMap;
import java.util.Objects;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import javax.annotation.CheckForNull;
import org.checkerframework.checker.nullness.qual.Nullable;

@ElementTypesAreNonnullByDefault
@GwtCompatible(
   serializable = true,
   emulated = true
)
final class RegularImmutableMap<K, V> extends ImmutableMap<K, V> {
   static final ImmutableMap<Object, Object> EMPTY;
   @VisibleForTesting
   static final double MAX_LOAD_FACTOR = 1.2D;
   @VisibleForTesting
   static final double HASH_FLOODING_FPP = 0.001D;
   @VisibleForTesting
   static final int MAX_HASH_BUCKET_LENGTH = 8;
   @VisibleForTesting
   final transient Entry<K, V>[] entries;
   @CheckForNull
   private final transient ImmutableMapEntry<K, V>[] table;
   private final transient int mask;
   private static final long serialVersionUID = 0L;

   static <K, V> ImmutableMap<K, V> fromEntries(Entry<K, V>... entries) {
      return fromEntryArray(entries.length, entries, true);
   }

   static <K, V> ImmutableMap<K, V> fromEntryArray(int n, Entry<K, V>[] entryArray, boolean throwIfDuplicateKeys) {
      Preconditions.checkPositionIndex(n, entryArray.length);
      if (n == 0) {
         ImmutableMap<K, V> empty = EMPTY;
         return empty;
      } else {
         try {
            return fromEntryArrayCheckingBucketOverflow(n, entryArray, throwIfDuplicateKeys);
         } catch (RegularImmutableMap.BucketOverflowException var4) {
            return JdkBackedImmutableMap.create(n, entryArray, throwIfDuplicateKeys);
         }
      }
   }

   private static <K, V> ImmutableMap<K, V> fromEntryArrayCheckingBucketOverflow(int n, Entry<K, V>[] entryArray, boolean throwIfDuplicateKeys) throws RegularImmutableMap.BucketOverflowException {
      Entry<K, V>[] entries = n == entryArray.length ? entryArray : ImmutableMapEntry.createEntryArray(n);
      int tableSize = Hashing.closedTableSize(n, 1.2D);
      ImmutableMapEntry<K, V>[] table = ImmutableMapEntry.createEntryArray(tableSize);
      int mask = tableSize - 1;
      IdentityHashMap<Entry<K, V>, Boolean> duplicates = null;
      int dupCount = 0;

      int newTableSize;
      for(newTableSize = n - 1; newTableSize >= 0; --newTableSize) {
         Entry<K, V> entry = (Entry)Objects.requireNonNull(entryArray[newTableSize]);
         K key = entry.getKey();
         V value = entry.getValue();
         CollectPreconditions.checkEntryNotNull(key, value);
         int tableIndex = Hashing.smear(key.hashCode()) & mask;
         ImmutableMapEntry<K, V> keyBucketHead = table[tableIndex];
         ImmutableMapEntry<K, V> effectiveEntry = checkNoConflictInKeyBucket(key, value, keyBucketHead, throwIfDuplicateKeys);
         if (effectiveEntry == null) {
            effectiveEntry = keyBucketHead == null ? makeImmutable(entry, key, value) : new ImmutableMapEntry.NonTerminalImmutableMapEntry(key, value, keyBucketHead);
            table[tableIndex] = (ImmutableMapEntry)effectiveEntry;
         } else {
            if (duplicates == null) {
               duplicates = new IdentityHashMap();
            }

            duplicates.put(effectiveEntry, true);
            ++dupCount;
            if (entries == entryArray) {
               entries = (Entry[])((Entry;)entries).clone();
            }
         }

         ((Object[])entries)[newTableSize] = effectiveEntry;
      }

      if (duplicates != null) {
         entries = removeDuplicates((Entry[])entries, n, n - dupCount, duplicates);
         newTableSize = Hashing.closedTableSize(((Object[])entries).length, 1.2D);
         if (newTableSize != tableSize) {
            return fromEntryArrayCheckingBucketOverflow(((Object[])entries).length, (Entry[])entries, true);
         }
      }

      return new RegularImmutableMap((Entry[])entries, table, mask);
   }

   static <K, V> Entry<K, V>[] removeDuplicates(Entry<K, V>[] entries, int n, int newN, IdentityHashMap<Entry<K, V>, Boolean> duplicates) {
      Entry<K, V>[] newEntries = ImmutableMapEntry.createEntryArray(newN);
      int in = 0;

      for(int var6 = 0; in < n; ++in) {
         Entry<K, V> entry = entries[in];
         Boolean status = (Boolean)duplicates.get(entry);
         if (status != null) {
            if (!status) {
               continue;
            }

            duplicates.put(entry, false);
         }

         newEntries[var6++] = entry;
      }

      return newEntries;
   }

   static <K, V> ImmutableMapEntry<K, V> makeImmutable(Entry<K, V> entry, K key, V value) {
      boolean reusable = entry instanceof ImmutableMapEntry && ((ImmutableMapEntry)entry).isReusable();
      return reusable ? (ImmutableMapEntry)entry : new ImmutableMapEntry(key, value);
   }

   static <K, V> ImmutableMapEntry<K, V> makeImmutable(Entry<K, V> entry) {
      return makeImmutable(entry, entry.getKey(), entry.getValue());
   }

   private RegularImmutableMap(Entry<K, V>[] entries, @CheckForNull ImmutableMapEntry<K, V>[] table, int mask) {
      this.entries = entries;
      this.table = table;
      this.mask = mask;
   }

   @CanIgnoreReturnValue
   @Nullable
   static <K, V> ImmutableMapEntry<K, V> checkNoConflictInKeyBucket(Object key, Object newValue, @CheckForNull ImmutableMapEntry<K, V> keyBucketHead, boolean throwIfDuplicateKeys) throws RegularImmutableMap.BucketOverflowException {
      for(int bucketSize = 0; keyBucketHead != null; keyBucketHead = keyBucketHead.getNextInKeyBucket()) {
         if (keyBucketHead.getKey().equals(key)) {
            if (!throwIfDuplicateKeys) {
               return keyBucketHead;
            }

            String var5 = String.valueOf(key);
            String var6 = String.valueOf(newValue);
            checkNoConflict(false, "key", keyBucketHead, (new StringBuilder(1 + String.valueOf(var5).length() + String.valueOf(var6).length())).append(var5).append("=").append(var6).toString());
         }

         ++bucketSize;
         if (bucketSize > 8) {
            throw new RegularImmutableMap.BucketOverflowException();
         }
      }

      return null;
   }

   @CheckForNull
   public V get(@CheckForNull Object key) {
      return get(key, this.table, this.mask);
   }

   @CheckForNull
   static <V> V get(@CheckForNull Object key, @CheckForNull ImmutableMapEntry<?, V>[] keyTable, int mask) {
      if (key != null && keyTable != null) {
         int index = Hashing.smear(key.hashCode()) & mask;

         for(ImmutableMapEntry entry = keyTable[index]; entry != null; entry = entry.getNextInKeyBucket()) {
            Object candidateKey = entry.getKey();
            if (key.equals(candidateKey)) {
               return entry.getValue();
            }
         }

         return null;
      } else {
         return null;
      }
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

   public int size() {
      return this.entries.length;
   }

   boolean isPartialView() {
      return false;
   }

   ImmutableSet<Entry<K, V>> createEntrySet() {
      return new ImmutableMapEntrySet.RegularEntrySet(this, this.entries);
   }

   ImmutableSet<K> createKeySet() {
      return new RegularImmutableMap.KeySet(this);
   }

   ImmutableCollection<V> createValues() {
      return new RegularImmutableMap.Values(this);
   }

   static {
      EMPTY = new RegularImmutableMap(ImmutableMap.EMPTY_ENTRY_ARRAY, (ImmutableMapEntry[])null, 0);
   }

   @GwtCompatible(
      emulated = true
   )
   private static final class Values<K, V> extends ImmutableList<V> {
      final RegularImmutableMap<K, V> map;

      Values(RegularImmutableMap<K, V> map) {
         this.map = map;
      }

      public V get(int index) {
         return this.map.entries[index].getValue();
      }

      public int size() {
         return this.map.size();
      }

      boolean isPartialView() {
         return true;
      }

      @GwtIncompatible
      private static class SerializedForm<V> implements Serializable {
         final ImmutableMap<?, V> map;
         private static final long serialVersionUID = 0L;

         SerializedForm(ImmutableMap<?, V> map) {
            this.map = map;
         }

         Object readResolve() {
            return this.map.values();
         }
      }
   }

   @GwtCompatible(
      emulated = true
   )
   private static final class KeySet<K> extends IndexedImmutableSet<K> {
      private final RegularImmutableMap<K, ?> map;

      KeySet(RegularImmutableMap<K, ?> map) {
         this.map = map;
      }

      K get(int index) {
         return this.map.entries[index].getKey();
      }

      public boolean contains(@CheckForNull Object object) {
         return this.map.containsKey(object);
      }

      boolean isPartialView() {
         return true;
      }

      public int size() {
         return this.map.size();
      }

      @GwtIncompatible
      private static class SerializedForm<K> implements Serializable {
         final ImmutableMap<K, ?> map;
         private static final long serialVersionUID = 0L;

         SerializedForm(ImmutableMap<K, ?> map) {
            this.map = map;
         }

         Object readResolve() {
            return this.map.keySet();
         }
      }
   }

   static class BucketOverflowException extends Exception {
   }
}
