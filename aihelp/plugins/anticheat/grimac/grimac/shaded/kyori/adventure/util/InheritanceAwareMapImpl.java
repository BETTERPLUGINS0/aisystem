package ac.grim.grimac.shaded.kyori.adventure.util;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

final class InheritanceAwareMapImpl<C, V> implements InheritanceAwareMap<C, V> {
   private static final Object NONE = new Object();
   static final InheritanceAwareMapImpl EMPTY = new InheritanceAwareMapImpl(false, Collections.emptyMap());
   private final Map<Class<? extends C>, V> declaredValues;
   private final boolean strict;
   private final transient ConcurrentMap<Class<? extends C>, Object> cache = new ConcurrentHashMap();

   InheritanceAwareMapImpl(final boolean strict, final Map<Class<? extends C>, V> declaredValues) {
      this.strict = strict;
      this.declaredValues = declaredValues;
   }

   public boolean containsKey(@NotNull final Class<? extends C> clazz) {
      return this.get(clazz) != null;
   }

   @Nullable
   public V get(@NotNull final Class<? extends C> clazz) {
      Object ret = this.cache.computeIfAbsent(clazz, (c) -> {
         V value = this.declaredValues.get(c);
         if (value != null) {
            return value;
         } else {
            Iterator var3 = this.declaredValues.entrySet().iterator();

            Entry entry;
            do {
               if (!var3.hasNext()) {
                  return NONE;
               }

               entry = (Entry)var3.next();
            } while(!((Class)entry.getKey()).isAssignableFrom(c));

            return entry.getValue();
         }
      });
      return ret == NONE ? null : ret;
   }

   @NotNull
   public InheritanceAwareMap<C, V> with(@NotNull final Class<? extends C> clazz, @NotNull final V value) {
      if (Objects.equals(this.declaredValues.get(clazz), value)) {
         return this;
      } else {
         if (this.strict) {
            validateNoneInHierarchy(clazz, this.declaredValues);
         }

         Map<Class<? extends C>, V> newValues = new LinkedHashMap(this.declaredValues);
         newValues.put(clazz, value);
         return new InheritanceAwareMapImpl(this.strict, Collections.unmodifiableMap(newValues));
      }
   }

   @NotNull
   public InheritanceAwareMap<C, V> without(@NotNull final Class<? extends C> clazz) {
      if (!this.declaredValues.containsKey(clazz)) {
         return this;
      } else {
         Map<Class<? extends C>, V> newValues = new LinkedHashMap(this.declaredValues);
         newValues.remove(clazz);
         return new InheritanceAwareMapImpl(this.strict, Collections.unmodifiableMap(newValues));
      }
   }

   private static void validateNoneInHierarchy(final Class<?> beingRegistered, final Map<? extends Class<?>, ?> entries) {
      Iterator var2 = entries.keySet().iterator();

      while(var2.hasNext()) {
         Class<?> clazz = (Class)var2.next();
         testHierarchy(clazz, beingRegistered);
      }

   }

   private static void testHierarchy(final Class<?> existing, final Class<?> beingRegistered) {
      if (!existing.equals(beingRegistered) && (existing.isAssignableFrom(beingRegistered) || beingRegistered.isAssignableFrom(existing))) {
         throw new IllegalArgumentException("Conflict detected between already registered type " + existing + " and newly registered type " + beingRegistered + "! Types in a strict inheritance-aware map must not share a common hierarchy!");
      }
   }

   static final class BuilderImpl<C, V> implements InheritanceAwareMap.Builder<C, V> {
      private boolean strict;
      private final Map<Class<? extends C>, V> values = new LinkedHashMap();

      @NotNull
      public InheritanceAwareMap<C, V> build() {
         return new InheritanceAwareMapImpl(this.strict, Collections.unmodifiableMap(new LinkedHashMap(this.values)));
      }

      @NotNull
      public InheritanceAwareMap.Builder<C, V> strict(final boolean strict) {
         if (strict && !this.strict) {
            Iterator var2 = this.values.keySet().iterator();

            while(var2.hasNext()) {
               Class<? extends C> clazz = (Class)var2.next();
               InheritanceAwareMapImpl.validateNoneInHierarchy(clazz, this.values);
            }
         }

         this.strict = strict;
         return this;
      }

      @NotNull
      public InheritanceAwareMap.Builder<C, V> put(@NotNull final Class<? extends C> clazz, @NotNull final V value) {
         if (this.strict) {
            InheritanceAwareMapImpl.validateNoneInHierarchy(clazz, this.values);
         }

         this.values.put((Class)Objects.requireNonNull(clazz, "clazz"), Objects.requireNonNull(value, "value"));
         return this;
      }

      @NotNull
      public InheritanceAwareMap.Builder<C, V> remove(@NotNull final Class<? extends C> clazz) {
         this.values.remove(Objects.requireNonNull(clazz, "clazz"));
         return this;
      }

      @NotNull
      public InheritanceAwareMap.Builder<C, V> putAll(@NotNull final InheritanceAwareMap<? extends C, ? extends V> map) {
         InheritanceAwareMapImpl<?, V> impl = (InheritanceAwareMapImpl)map;
         if (!this.strict || this.values.isEmpty() && impl.strict) {
            this.values.putAll(impl.declaredValues);
            return this;
         } else {
            Iterator var3 = impl.declaredValues.entrySet().iterator();

            while(var3.hasNext()) {
               Entry<? extends Class<?>, V> entry = (Entry)var3.next();
               InheritanceAwareMapImpl.validateNoneInHierarchy((Class)entry.getKey(), this.values);
               this.values.put((Class)entry.getKey(), entry.getValue());
            }

            return this;
         }
      }
   }
}
