package fr.xephi.authme.libs.com.google.common.collect;

import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import fr.xephi.authme.libs.com.google.common.annotations.VisibleForTesting;
import fr.xephi.authme.libs.com.google.errorprone.annotations.concurrent.LazyInit;
import fr.xephi.authme.libs.com.google.j2objc.annotations.RetainedWith;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@GwtCompatible(
   emulated = true
)
final class JdkBackedImmutableBiMap<K, V> extends ImmutableBiMap<K, V> {
   private final transient ImmutableList<Entry<K, V>> entries;
   private final Map<K, V> forwardDelegate;
   private final Map<V, K> backwardDelegate;
   @LazyInit
   @CheckForNull
   @RetainedWith
   private transient JdkBackedImmutableBiMap<V, K> inverse;

   @VisibleForTesting
   static <K, V> ImmutableBiMap<K, V> create(int n, Entry<K, V>[] entryArray) {
      Map<K, V> forwardDelegate = Maps.newHashMapWithExpectedSize(n);
      Map<V, K> backwardDelegate = Maps.newHashMapWithExpectedSize(n);

      for(int i = 0; i < n; ++i) {
         Entry<K, V> e = RegularImmutableMap.makeImmutable((Entry)Objects.requireNonNull(entryArray[i]));
         entryArray[i] = e;
         V oldValue = forwardDelegate.putIfAbsent(e.getKey(), e.getValue());
         String var8;
         if (oldValue != null) {
            String var11 = String.valueOf(e.getKey());
            var8 = String.valueOf(oldValue);
            throw conflictException("key", (new StringBuilder(1 + String.valueOf(var11).length() + String.valueOf(var8).length())).append(var11).append("=").append(var8).toString(), entryArray[i]);
         }

         K oldKey = backwardDelegate.putIfAbsent(e.getValue(), e.getKey());
         if (oldKey != null) {
            var8 = String.valueOf(oldKey);
            String var9 = String.valueOf(e.getValue());
            throw conflictException("value", (new StringBuilder(1 + String.valueOf(var8).length() + String.valueOf(var9).length())).append(var8).append("=").append(var9).toString(), entryArray[i]);
         }
      }

      ImmutableList<Entry<K, V>> entryList = ImmutableList.asImmutableList(entryArray, n);
      return new JdkBackedImmutableBiMap(entryList, forwardDelegate, backwardDelegate);
   }

   private JdkBackedImmutableBiMap(ImmutableList<Entry<K, V>> entries, Map<K, V> forwardDelegate, Map<V, K> backwardDelegate) {
      this.entries = entries;
      this.forwardDelegate = forwardDelegate;
      this.backwardDelegate = backwardDelegate;
   }

   public int size() {
      return this.entries.size();
   }

   public ImmutableBiMap<V, K> inverse() {
      JdkBackedImmutableBiMap<V, K> result = this.inverse;
      if (result == null) {
         this.inverse = result = new JdkBackedImmutableBiMap(new JdkBackedImmutableBiMap.InverseEntries(), this.backwardDelegate, this.forwardDelegate);
         result.inverse = this;
      }

      return result;
   }

   @CheckForNull
   public V get(@CheckForNull Object key) {
      return this.forwardDelegate.get(key);
   }

   ImmutableSet<Entry<K, V>> createEntrySet() {
      return new ImmutableMapEntrySet.RegularEntrySet(this, this.entries);
   }

   ImmutableSet<K> createKeySet() {
      return new ImmutableMapKeySet(this);
   }

   boolean isPartialView() {
      return false;
   }

   private final class InverseEntries extends ImmutableList<Entry<V, K>> {
      private InverseEntries() {
      }

      public Entry<V, K> get(int index) {
         Entry<K, V> entry = (Entry)JdkBackedImmutableBiMap.this.entries.get(index);
         return Maps.immutableEntry(entry.getValue(), entry.getKey());
      }

      boolean isPartialView() {
         return false;
      }

      public int size() {
         return JdkBackedImmutableBiMap.this.entries.size();
      }

      // $FF: synthetic method
      InverseEntries(Object x1) {
         this();
      }
   }
}
