package fr.xephi.authme.libs.com.google.common.collect;

import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import fr.xephi.authme.libs.com.google.common.annotations.GwtIncompatible;
import java.io.Serializable;
import java.util.Spliterator;
import java.util.Map.Entry;
import java.util.function.Consumer;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@GwtCompatible(
   emulated = true
)
abstract class ImmutableMapEntrySet<K, V> extends ImmutableSet.CachingAsList<Entry<K, V>> {
   abstract ImmutableMap<K, V> map();

   public int size() {
      return this.map().size();
   }

   public boolean contains(@CheckForNull Object object) {
      if (!(object instanceof Entry)) {
         return false;
      } else {
         Entry<?, ?> entry = (Entry)object;
         V value = this.map().get(entry.getKey());
         return value != null && value.equals(entry.getValue());
      }
   }

   boolean isPartialView() {
      return this.map().isPartialView();
   }

   @GwtIncompatible
   boolean isHashCodeFast() {
      return this.map().isHashCodeFast();
   }

   public int hashCode() {
      return this.map().hashCode();
   }

   @GwtIncompatible
   Object writeReplace() {
      return new ImmutableMapEntrySet.EntrySetSerializedForm(this.map());
   }

   @GwtIncompatible
   private static class EntrySetSerializedForm<K, V> implements Serializable {
      final ImmutableMap<K, V> map;
      private static final long serialVersionUID = 0L;

      EntrySetSerializedForm(ImmutableMap<K, V> map) {
         this.map = map;
      }

      Object readResolve() {
         return this.map.entrySet();
      }
   }

   static final class RegularEntrySet<K, V> extends ImmutableMapEntrySet<K, V> {
      private final transient ImmutableMap<K, V> map;
      private final transient ImmutableList<Entry<K, V>> entries;

      RegularEntrySet(ImmutableMap<K, V> map, Entry<K, V>[] entries) {
         this(map, ImmutableList.asImmutableList(entries));
      }

      RegularEntrySet(ImmutableMap<K, V> map, ImmutableList<Entry<K, V>> entries) {
         this.map = map;
         this.entries = entries;
      }

      ImmutableMap<K, V> map() {
         return this.map;
      }

      @GwtIncompatible("not used in GWT")
      int copyIntoArray(Object[] dst, int offset) {
         return this.entries.copyIntoArray(dst, offset);
      }

      public UnmodifiableIterator<Entry<K, V>> iterator() {
         return this.entries.iterator();
      }

      public Spliterator<Entry<K, V>> spliterator() {
         return this.entries.spliterator();
      }

      public void forEach(Consumer<? super Entry<K, V>> action) {
         this.entries.forEach(action);
      }

      ImmutableList<Entry<K, V>> createAsList() {
         return new RegularImmutableAsList(this, this.entries);
      }
   }
}
