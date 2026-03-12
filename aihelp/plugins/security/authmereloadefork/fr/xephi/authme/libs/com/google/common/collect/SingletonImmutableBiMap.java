package fr.xephi.authme.libs.com.google.common.collect;

import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import fr.xephi.authme.libs.com.google.errorprone.annotations.concurrent.LazyInit;
import fr.xephi.authme.libs.com.google.j2objc.annotations.RetainedWith;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@GwtCompatible(
   serializable = true,
   emulated = true
)
final class SingletonImmutableBiMap<K, V> extends ImmutableBiMap<K, V> {
   final transient K singleKey;
   final transient V singleValue;
   @CheckForNull
   private final transient ImmutableBiMap<V, K> inverse;
   @LazyInit
   @CheckForNull
   @RetainedWith
   private transient ImmutableBiMap<V, K> lazyInverse;

   SingletonImmutableBiMap(K singleKey, V singleValue) {
      CollectPreconditions.checkEntryNotNull(singleKey, singleValue);
      this.singleKey = singleKey;
      this.singleValue = singleValue;
      this.inverse = null;
   }

   private SingletonImmutableBiMap(K singleKey, V singleValue, ImmutableBiMap<V, K> inverse) {
      this.singleKey = singleKey;
      this.singleValue = singleValue;
      this.inverse = inverse;
   }

   @CheckForNull
   public V get(@CheckForNull Object key) {
      return this.singleKey.equals(key) ? this.singleValue : null;
   }

   public int size() {
      return 1;
   }

   public void forEach(BiConsumer<? super K, ? super V> action) {
      ((BiConsumer)Preconditions.checkNotNull(action)).accept(this.singleKey, this.singleValue);
   }

   public boolean containsKey(@CheckForNull Object key) {
      return this.singleKey.equals(key);
   }

   public boolean containsValue(@CheckForNull Object value) {
      return this.singleValue.equals(value);
   }

   boolean isPartialView() {
      return false;
   }

   ImmutableSet<Entry<K, V>> createEntrySet() {
      return ImmutableSet.of(Maps.immutableEntry(this.singleKey, this.singleValue));
   }

   ImmutableSet<K> createKeySet() {
      return ImmutableSet.of(this.singleKey);
   }

   public ImmutableBiMap<V, K> inverse() {
      if (this.inverse != null) {
         return this.inverse;
      } else {
         ImmutableBiMap<V, K> result = this.lazyInverse;
         return result == null ? (this.lazyInverse = new SingletonImmutableBiMap(this.singleValue, this.singleKey, this)) : result;
      }
   }
}
