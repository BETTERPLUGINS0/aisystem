package fr.xephi.authme.libs.com.google.common.cache;

import fr.xephi.authme.libs.com.google.common.annotations.GwtIncompatible;
import fr.xephi.authme.libs.com.google.common.collect.ImmutableMap;
import fr.xephi.authme.libs.com.google.common.collect.Maps;
import fr.xephi.authme.libs.com.google.common.util.concurrent.UncheckedExecutionException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@ElementTypesAreNonnullByDefault
@GwtIncompatible
public abstract class AbstractLoadingCache<K, V> extends AbstractCache<K, V> implements LoadingCache<K, V> {
   protected AbstractLoadingCache() {
   }

   public V getUnchecked(K key) {
      try {
         return this.get(key);
      } catch (ExecutionException var3) {
         throw new UncheckedExecutionException(var3.getCause());
      }
   }

   public ImmutableMap<K, V> getAll(Iterable<? extends K> keys) throws ExecutionException {
      Map<K, V> result = Maps.newLinkedHashMap();
      Iterator var3 = keys.iterator();

      while(var3.hasNext()) {
         K key = var3.next();
         if (!result.containsKey(key)) {
            result.put(key, this.get(key));
         }
      }

      return ImmutableMap.copyOf((Map)result);
   }

   public final V apply(K key) {
      return this.getUnchecked(key);
   }

   public void refresh(K key) {
      throw new UnsupportedOperationException();
   }
}
