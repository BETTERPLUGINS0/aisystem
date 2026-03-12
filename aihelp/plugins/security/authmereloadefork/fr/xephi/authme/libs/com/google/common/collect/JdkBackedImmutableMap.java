package fr.xephi.authme.libs.com.google.common.collect;

import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@GwtCompatible(
   emulated = true
)
final class JdkBackedImmutableMap<K, V> extends ImmutableMap<K, V> {
   private final transient Map<K, V> delegateMap;
   private final transient ImmutableList<Entry<K, V>> entries;

   static <K, V> ImmutableMap<K, V> create(int n, Entry<K, V>[] entryArray, boolean throwIfDuplicateKeys) {
      Map<K, V> delegateMap = Maps.newHashMapWithExpectedSize(n);
      Map<K, V> duplicates = null;
      int dupCount = 0;

      Object entry;
      for(int i = 0; i < n; ++i) {
         entryArray[i] = RegularImmutableMap.makeImmutable((Entry)Objects.requireNonNull(entryArray[i]));
         K key = entryArray[i].getKey();
         V value = entryArray[i].getValue();
         entry = delegateMap.put(key, value);
         if (entry != null) {
            if (throwIfDuplicateKeys) {
               Entry var10001 = entryArray[i];
               String var10 = String.valueOf(entryArray[i].getKey());
               String var11 = String.valueOf(entry);
               throw conflictException("key", var10001, (new StringBuilder(1 + String.valueOf(var10).length() + String.valueOf(var11).length())).append(var10).append("=").append(var11).toString());
            }

            if (duplicates == null) {
               duplicates = new HashMap();
            }

            duplicates.put(key, value);
            ++dupCount;
         }
      }

      if (duplicates != null) {
         Entry<K, V>[] newEntryArray = new Entry[n - dupCount];
         int inI = 0;

         for(int var14 = 0; inI < n; ++inI) {
            entry = (Entry)Objects.requireNonNull(entryArray[inI]);
            K key = ((Entry)entry).getKey();
            if (duplicates.containsKey(key)) {
               V value = duplicates.get(key);
               if (value == null) {
                  continue;
               }

               entry = new ImmutableMapEntry(key, value);
               duplicates.put(key, (Object)null);
            }

            newEntryArray[var14++] = (Entry)entry;
         }

         entryArray = newEntryArray;
      }

      return new JdkBackedImmutableMap(delegateMap, ImmutableList.asImmutableList(entryArray, n));
   }

   JdkBackedImmutableMap(Map<K, V> delegateMap, ImmutableList<Entry<K, V>> entries) {
      this.delegateMap = delegateMap;
      this.entries = entries;
   }

   public int size() {
      return this.entries.size();
   }

   @CheckForNull
   public V get(@CheckForNull Object key) {
      return this.delegateMap.get(key);
   }

   ImmutableSet<Entry<K, V>> createEntrySet() {
      return new ImmutableMapEntrySet.RegularEntrySet(this, this.entries);
   }

   public void forEach(BiConsumer<? super K, ? super V> action) {
      Preconditions.checkNotNull(action);
      this.entries.forEach((e) -> {
         action.accept(e.getKey(), e.getValue());
      });
   }

   ImmutableSet<K> createKeySet() {
      return new ImmutableMapKeySet(this);
   }

   ImmutableCollection<V> createValues() {
      return new ImmutableMapValues(this);
   }

   boolean isPartialView() {
      return false;
   }
}
