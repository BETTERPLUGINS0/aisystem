package fr.xephi.authme.libs.com.google.common.collect;

import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.EnumMap;
import java.util.Spliterator;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@GwtCompatible(
   serializable = true,
   emulated = true
)
final class ImmutableEnumMap<K extends Enum<K>, V> extends ImmutableMap.IteratorBasedImmutableMap<K, V> {
   private final transient EnumMap<K, V> delegate;

   static <K extends Enum<K>, V> ImmutableMap<K, V> asImmutable(EnumMap<K, V> map) {
      switch(map.size()) {
      case 0:
         return ImmutableMap.of();
      case 1:
         Entry<K, V> entry = (Entry)Iterables.getOnlyElement(map.entrySet());
         return ImmutableMap.of((Enum)entry.getKey(), entry.getValue());
      default:
         return new ImmutableEnumMap(map);
      }
   }

   private ImmutableEnumMap(EnumMap<K, V> delegate) {
      this.delegate = delegate;
      Preconditions.checkArgument(!delegate.isEmpty());
   }

   UnmodifiableIterator<K> keyIterator() {
      return Iterators.unmodifiableIterator(this.delegate.keySet().iterator());
   }

   Spliterator<K> keySpliterator() {
      return this.delegate.keySet().spliterator();
   }

   public int size() {
      return this.delegate.size();
   }

   public boolean containsKey(@CheckForNull Object key) {
      return this.delegate.containsKey(key);
   }

   @CheckForNull
   public V get(@CheckForNull Object key) {
      return this.delegate.get(key);
   }

   public boolean equals(@CheckForNull Object object) {
      if (object == this) {
         return true;
      } else {
         if (object instanceof ImmutableEnumMap) {
            object = ((ImmutableEnumMap)object).delegate;
         }

         return this.delegate.equals(object);
      }
   }

   UnmodifiableIterator<Entry<K, V>> entryIterator() {
      return Maps.unmodifiableEntryIterator(this.delegate.entrySet().iterator());
   }

   Spliterator<Entry<K, V>> entrySpliterator() {
      return CollectSpliterators.map(this.delegate.entrySet().spliterator(), Maps::unmodifiableEntry);
   }

   public void forEach(BiConsumer<? super K, ? super V> action) {
      this.delegate.forEach(action);
   }

   boolean isPartialView() {
      return false;
   }

   Object writeReplace() {
      return new ImmutableEnumMap.EnumSerializedForm(this.delegate);
   }

   // $FF: synthetic method
   ImmutableEnumMap(EnumMap x0, Object x1) {
      this(x0);
   }

   private static class EnumSerializedForm<K extends Enum<K>, V> implements Serializable {
      final EnumMap<K, V> delegate;
      private static final long serialVersionUID = 0L;

      EnumSerializedForm(EnumMap<K, V> delegate) {
         this.delegate = delegate;
      }

      Object readResolve() {
         return new ImmutableEnumMap(this.delegate);
      }
   }
}
