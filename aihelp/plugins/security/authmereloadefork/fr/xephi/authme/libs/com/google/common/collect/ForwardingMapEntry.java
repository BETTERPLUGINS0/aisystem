package fr.xephi.authme.libs.com.google.common.collect;

import fr.xephi.authme.libs.com.google.common.annotations.Beta;
import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import fr.xephi.authme.libs.com.google.common.base.Objects;
import java.util.Map.Entry;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@GwtCompatible
public abstract class ForwardingMapEntry<K, V> extends ForwardingObject implements Entry<K, V> {
   protected ForwardingMapEntry() {
   }

   protected abstract Entry<K, V> delegate();

   @ParametricNullness
   public K getKey() {
      return this.delegate().getKey();
   }

   @ParametricNullness
   public V getValue() {
      return this.delegate().getValue();
   }

   @ParametricNullness
   public V setValue(@ParametricNullness V value) {
      return this.delegate().setValue(value);
   }

   public boolean equals(@CheckForNull Object object) {
      return this.delegate().equals(object);
   }

   public int hashCode() {
      return this.delegate().hashCode();
   }

   protected boolean standardEquals(@CheckForNull Object object) {
      if (!(object instanceof Entry)) {
         return false;
      } else {
         Entry<?, ?> that = (Entry)object;
         return Objects.equal(this.getKey(), that.getKey()) && Objects.equal(this.getValue(), that.getValue());
      }
   }

   protected int standardHashCode() {
      K k = this.getKey();
      V v = this.getValue();
      return (k == null ? 0 : k.hashCode()) ^ (v == null ? 0 : v.hashCode());
   }

   @Beta
   protected String standardToString() {
      String var1 = String.valueOf(this.getKey());
      String var2 = String.valueOf(this.getValue());
      return (new StringBuilder(1 + String.valueOf(var1).length() + String.valueOf(var2).length())).append(var1).append("=").append(var2).toString();
   }
}
