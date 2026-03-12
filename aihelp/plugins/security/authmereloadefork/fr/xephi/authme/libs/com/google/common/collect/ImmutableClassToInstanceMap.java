package fr.xephi.authme.libs.com.google.common.collect;

import fr.xephi.authme.libs.com.google.common.annotations.GwtIncompatible;
import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import fr.xephi.authme.libs.com.google.common.primitives.Primitives;
import fr.xephi.authme.libs.com.google.errorprone.annotations.CanIgnoreReturnValue;
import fr.xephi.authme.libs.com.google.errorprone.annotations.DoNotCall;
import fr.xephi.authme.libs.com.google.errorprone.annotations.Immutable;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.CheckForNull;

@Immutable(
   containerOf = {"B"}
)
@ElementTypesAreNonnullByDefault
@GwtIncompatible
public final class ImmutableClassToInstanceMap<B> extends ForwardingMap<Class<? extends B>, B> implements ClassToInstanceMap<B>, Serializable {
   private static final ImmutableClassToInstanceMap<Object> EMPTY = new ImmutableClassToInstanceMap(ImmutableMap.of());
   private final ImmutableMap<Class<? extends B>, B> delegate;

   public static <B> ImmutableClassToInstanceMap<B> of() {
      return EMPTY;
   }

   public static <B, T extends B> ImmutableClassToInstanceMap<B> of(Class<T> type, T value) {
      ImmutableMap<Class<? extends B>, B> map = ImmutableMap.of(type, value);
      return new ImmutableClassToInstanceMap(map);
   }

   public static <B> ImmutableClassToInstanceMap.Builder<B> builder() {
      return new ImmutableClassToInstanceMap.Builder();
   }

   public static <B, S extends B> ImmutableClassToInstanceMap<B> copyOf(Map<? extends Class<? extends S>, ? extends S> map) {
      if (map instanceof ImmutableClassToInstanceMap) {
         ImmutableClassToInstanceMap<B> cast = (ImmutableClassToInstanceMap)map;
         return cast;
      } else {
         return (new ImmutableClassToInstanceMap.Builder()).putAll(map).build();
      }
   }

   private ImmutableClassToInstanceMap(ImmutableMap<Class<? extends B>, B> delegate) {
      this.delegate = delegate;
   }

   protected Map<Class<? extends B>, B> delegate() {
      return this.delegate;
   }

   @CheckForNull
   public <T extends B> T getInstance(Class<T> type) {
      return this.delegate.get(Preconditions.checkNotNull(type));
   }

   /** @deprecated */
   @Deprecated
   @CheckForNull
   @CanIgnoreReturnValue
   @DoNotCall("Always throws UnsupportedOperationException")
   public <T extends B> T putInstance(Class<T> type, T value) {
      throw new UnsupportedOperationException();
   }

   Object readResolve() {
      return this.isEmpty() ? of() : this;
   }

   // $FF: synthetic method
   ImmutableClassToInstanceMap(ImmutableMap x0, Object x1) {
      this(x0);
   }

   public static final class Builder<B> {
      private final ImmutableMap.Builder<Class<? extends B>, B> mapBuilder = ImmutableMap.builder();

      @CanIgnoreReturnValue
      public <T extends B> ImmutableClassToInstanceMap.Builder<B> put(Class<T> key, T value) {
         this.mapBuilder.put(key, value);
         return this;
      }

      @CanIgnoreReturnValue
      public <T extends B> ImmutableClassToInstanceMap.Builder<B> putAll(Map<? extends Class<? extends T>, ? extends T> map) {
         Iterator var2 = map.entrySet().iterator();

         while(var2.hasNext()) {
            Entry<? extends Class<? extends T>, ? extends T> entry = (Entry)var2.next();
            Class<? extends T> type = (Class)entry.getKey();
            T value = entry.getValue();
            this.mapBuilder.put(type, cast(type, value));
         }

         return this;
      }

      private static <B, T extends B> T cast(Class<T> type, B value) {
         return Primitives.wrap(type).cast(value);
      }

      public ImmutableClassToInstanceMap<B> build() {
         ImmutableMap<Class<? extends B>, B> map = this.mapBuilder.buildOrThrow();
         return map.isEmpty() ? ImmutableClassToInstanceMap.of() : new ImmutableClassToInstanceMap(map);
      }
   }
}
