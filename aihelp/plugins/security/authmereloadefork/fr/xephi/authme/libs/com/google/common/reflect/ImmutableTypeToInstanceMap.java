package fr.xephi.authme.libs.com.google.common.reflect;

import fr.xephi.authme.libs.com.google.common.collect.ForwardingMap;
import fr.xephi.authme.libs.com.google.common.collect.ImmutableMap;
import fr.xephi.authme.libs.com.google.errorprone.annotations.CanIgnoreReturnValue;
import fr.xephi.authme.libs.com.google.errorprone.annotations.DoNotCall;
import java.util.Map;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
public final class ImmutableTypeToInstanceMap<B> extends ForwardingMap<TypeToken<? extends B>, B> implements TypeToInstanceMap<B> {
   private final ImmutableMap<TypeToken<? extends B>, B> delegate;

   public static <B> ImmutableTypeToInstanceMap<B> of() {
      return new ImmutableTypeToInstanceMap(ImmutableMap.of());
   }

   public static <B> ImmutableTypeToInstanceMap.Builder<B> builder() {
      return new ImmutableTypeToInstanceMap.Builder();
   }

   private ImmutableTypeToInstanceMap(ImmutableMap<TypeToken<? extends B>, B> delegate) {
      this.delegate = delegate;
   }

   @CheckForNull
   public <T extends B> T getInstance(TypeToken<T> type) {
      return this.trustedGet(type.rejectTypeVariables());
   }

   @CheckForNull
   public <T extends B> T getInstance(Class<T> type) {
      return this.trustedGet(TypeToken.of(type));
   }

   /** @deprecated */
   @Deprecated
   @CheckForNull
   @CanIgnoreReturnValue
   @DoNotCall("Always throws UnsupportedOperationException")
   public <T extends B> T putInstance(TypeToken<T> type, T value) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   @CheckForNull
   @CanIgnoreReturnValue
   @DoNotCall("Always throws UnsupportedOperationException")
   public <T extends B> T putInstance(Class<T> type, T value) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   @CheckForNull
   @CanIgnoreReturnValue
   @DoNotCall("Always throws UnsupportedOperationException")
   public B put(TypeToken<? extends B> key, B value) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   @DoNotCall("Always throws UnsupportedOperationException")
   public void putAll(Map<? extends TypeToken<? extends B>, ? extends B> map) {
      throw new UnsupportedOperationException();
   }

   protected Map<TypeToken<? extends B>, B> delegate() {
      return this.delegate;
   }

   @CheckForNull
   private <T extends B> T trustedGet(TypeToken<T> type) {
      return this.delegate.get(type);
   }

   // $FF: synthetic method
   ImmutableTypeToInstanceMap(ImmutableMap x0, Object x1) {
      this(x0);
   }

   public static final class Builder<B> {
      private final ImmutableMap.Builder<TypeToken<? extends B>, B> mapBuilder;

      private Builder() {
         this.mapBuilder = ImmutableMap.builder();
      }

      @CanIgnoreReturnValue
      public <T extends B> ImmutableTypeToInstanceMap.Builder<B> put(Class<T> key, T value) {
         this.mapBuilder.put(TypeToken.of(key), value);
         return this;
      }

      @CanIgnoreReturnValue
      public <T extends B> ImmutableTypeToInstanceMap.Builder<B> put(TypeToken<T> key, T value) {
         this.mapBuilder.put(key.rejectTypeVariables(), value);
         return this;
      }

      public ImmutableTypeToInstanceMap<B> build() {
         return new ImmutableTypeToInstanceMap(this.mapBuilder.buildOrThrow());
      }

      // $FF: synthetic method
      Builder(Object x0) {
         this();
      }
   }
}
