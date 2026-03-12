package fr.xephi.authme.libs.com.google.common.collect;

import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import fr.xephi.authme.libs.com.google.errorprone.annotations.CanIgnoreReturnValue;
import fr.xephi.authme.libs.com.google.errorprone.annotations.CompatibleWith;
import fr.xephi.authme.libs.com.google.errorprone.annotations.DoNotMock;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import javax.annotation.CheckForNull;

@DoNotMock("Use ImmutableMultimap, HashMultimap, or another implementation")
@ElementTypesAreNonnullByDefault
@GwtCompatible
public interface Multimap<K, V> {
   int size();

   boolean isEmpty();

   boolean containsKey(@CheckForNull @CompatibleWith("K") Object var1);

   boolean containsValue(@CheckForNull @CompatibleWith("V") Object var1);

   boolean containsEntry(@CheckForNull @CompatibleWith("K") Object var1, @CheckForNull @CompatibleWith("V") Object var2);

   @CanIgnoreReturnValue
   boolean put(@ParametricNullness K var1, @ParametricNullness V var2);

   @CanIgnoreReturnValue
   boolean remove(@CheckForNull @CompatibleWith("K") Object var1, @CheckForNull @CompatibleWith("V") Object var2);

   @CanIgnoreReturnValue
   boolean putAll(@ParametricNullness K var1, Iterable<? extends V> var2);

   @CanIgnoreReturnValue
   boolean putAll(Multimap<? extends K, ? extends V> var1);

   @CanIgnoreReturnValue
   Collection<V> replaceValues(@ParametricNullness K var1, Iterable<? extends V> var2);

   @CanIgnoreReturnValue
   Collection<V> removeAll(@CheckForNull @CompatibleWith("K") Object var1);

   void clear();

   Collection<V> get(@ParametricNullness K var1);

   Set<K> keySet();

   Multiset<K> keys();

   Collection<V> values();

   Collection<Entry<K, V>> entries();

   default void forEach(BiConsumer<? super K, ? super V> action) {
      Preconditions.checkNotNull(action);
      this.entries().forEach((entry) -> {
         action.accept(entry.getKey(), entry.getValue());
      });
   }

   Map<K, Collection<V>> asMap();

   boolean equals(@CheckForNull Object var1);

   int hashCode();
}
