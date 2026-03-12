package ac.grim.grimac.shaded.kyori.adventure.text.event;

import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.Contract;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.key.Key;
import ac.grim.grimac.shaded.kyori.adventure.util.Services;
import ac.grim.grimac.shaded.kyori.examination.Examinable;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public final class DataComponentValueConverterRegistry {
   private static final Set<DataComponentValueConverterRegistry.Provider> PROVIDERS = Services.services(DataComponentValueConverterRegistry.Provider.class);

   private DataComponentValueConverterRegistry() {
   }

   public static Set<Key> knownProviders() {
      return Collections.unmodifiableSet((Set)PROVIDERS.stream().map(DataComponentValueConverterRegistry.Provider::id).collect(Collectors.toSet()));
   }

   @NotNull
   public static <O extends DataComponentValue> O convert(@NotNull final Class<O> target, @NotNull final Key key, @NotNull final DataComponentValue in) {
      if (target.isInstance(in)) {
         return (DataComponentValue)target.cast(in);
      } else {
         DataComponentValueConverterRegistry.RegisteredConversion converter = DataComponentValueConverterRegistry.ConversionCache.converter(in.getClass(), target);
         if (converter == null) {
            throw new IllegalArgumentException("There is no data holder converter registered to convert from a " + in.getClass() + " instance to a " + target + " (on field " + key + ")");
         } else {
            try {
               return (DataComponentValue)converter.conversion.convert(key, in);
            } catch (Exception var5) {
               throw new IllegalStateException("Failed to convert data component value of type " + in.getClass() + " to type " + target + " due to an error in a converter provided by " + converter.provider.asString() + "!", var5);
            }
         }
      }
   }

   static final class ConversionCache {
      private static final ConcurrentMap<Class<?>, ConcurrentMap<Class<?>, DataComponentValueConverterRegistry.RegisteredConversion>> CACHE = new ConcurrentHashMap();
      private static final Map<Class<?>, Set<DataComponentValueConverterRegistry.RegisteredConversion>> CONVERSIONS = collectConversions();

      private static Map<Class<?>, Set<DataComponentValueConverterRegistry.RegisteredConversion>> collectConversions() {
         Map<Class<?>, Set<DataComponentValueConverterRegistry.RegisteredConversion>> collected = new ConcurrentHashMap();
         Iterator var1 = DataComponentValueConverterRegistry.PROVIDERS.iterator();

         while(var1.hasNext()) {
            DataComponentValueConverterRegistry.Provider provider = (DataComponentValueConverterRegistry.Provider)var1.next();
            Key id = (Key)Objects.requireNonNull(provider.id(), () -> {
               return "ID of provider " + provider + " is null";
            });
            Iterator var4 = provider.conversions().iterator();

            while(var4.hasNext()) {
               DataComponentValueConverterRegistry.Conversion<?, ?> conv = (DataComponentValueConverterRegistry.Conversion)var4.next();
               ((Set)collected.computeIfAbsent(conv.source(), ($) -> {
                  return ConcurrentHashMap.newKeySet();
               })).add(new DataComponentValueConverterRegistry.RegisteredConversion(id, conv));
            }
         }

         var1 = collected.entrySet().iterator();

         while(var1.hasNext()) {
            Entry<Class<?>, Set<DataComponentValueConverterRegistry.RegisteredConversion>> entry = (Entry)var1.next();
            entry.setValue(Collections.unmodifiableSet((Set)entry.getValue()));
         }

         return new ConcurrentHashMap(collected);
      }

      static DataComponentValueConverterRegistry.RegisteredConversion compute(final Class<?> src, final Class<?> dst) {
         Deque<Class<?>> sourceTypes = new ArrayDeque();
         sourceTypes.add(src);

         Class sourcePtr;
         for(; (sourcePtr = (Class)sourceTypes.poll()) != null; addSupertypes(sourcePtr, sourceTypes)) {
            Set<DataComponentValueConverterRegistry.RegisteredConversion> conversions = (Set)CONVERSIONS.get(sourcePtr);
            if (conversions != null) {
               DataComponentValueConverterRegistry.RegisteredConversion nearest = null;
               Iterator var6 = conversions.iterator();

               while(var6.hasNext()) {
                  DataComponentValueConverterRegistry.RegisteredConversion potential = (DataComponentValueConverterRegistry.RegisteredConversion)var6.next();
                  Class<?> potentialDst = potential.conversion.destination();
                  if (dst.equals(potentialDst)) {
                     return potential;
                  }

                  if (dst.isAssignableFrom(potentialDst) && (nearest == null || potentialDst.isAssignableFrom(nearest.conversion.destination()))) {
                     nearest = potential;
                  }
               }

               if (nearest != null) {
                  return nearest;
               }
            }
         }

         return DataComponentValueConverterRegistry.RegisteredConversion.NONE;
      }

      private static void addSupertypes(final Class<?> clazz, final Deque<Class<?>> queue) {
         if (clazz.getSuperclass() != null) {
            queue.add(clazz.getSuperclass());
         }

         queue.addAll(Arrays.asList(clazz.getInterfaces()));
      }

      @Nullable
      static DataComponentValueConverterRegistry.RegisteredConversion converter(final Class<? extends DataComponentValue> src, final Class<? extends DataComponentValue> dst) {
         DataComponentValueConverterRegistry.RegisteredConversion result = (DataComponentValueConverterRegistry.RegisteredConversion)((ConcurrentMap)CACHE.computeIfAbsent(src, ($) -> {
            return new ConcurrentHashMap();
         })).computeIfAbsent(dst, ($$) -> {
            return compute(src, dst);
         });
         return result == DataComponentValueConverterRegistry.RegisteredConversion.NONE ? null : result;
      }
   }

   static final class RegisteredConversion {
      static final DataComponentValueConverterRegistry.RegisteredConversion NONE = new DataComponentValueConverterRegistry.RegisteredConversion((Key)null, (DataComponentValueConverterRegistry.Conversion)null);
      final Key provider;
      final DataComponentValueConverterRegistry.Conversion<?, ?> conversion;

      RegisteredConversion(final Key provider, final DataComponentValueConverterRegistry.Conversion<?, ?> conversion) {
         this.provider = provider;
         this.conversion = conversion;
      }
   }

   @ApiStatus.NonExtendable
   public interface Conversion<I, O> extends Examinable {
      @NotNull
      static <I1, O1> DataComponentValueConverterRegistry.Conversion<I1, O1> convert(@NotNull final Class<I1> src, @NotNull final Class<O1> dst, @NotNull final BiFunction<Key, I1, O1> op) {
         return new DataComponentValueConversionImpl((Class)Objects.requireNonNull(src, "src"), (Class)Objects.requireNonNull(dst, "dst"), (BiFunction)Objects.requireNonNull(op, "op"));
      }

      @Contract(
         pure = true
      )
      @NotNull
      Class<I> source();

      @Contract(
         pure = true
      )
      @NotNull
      Class<O> destination();

      @NotNull
      O convert(@NotNull final Key key, @NotNull final I input);
   }

   public interface Provider {
      @NotNull
      Key id();

      @NotNull
      Iterable<DataComponentValueConverterRegistry.Conversion<?, ?>> conversions();
   }
}
