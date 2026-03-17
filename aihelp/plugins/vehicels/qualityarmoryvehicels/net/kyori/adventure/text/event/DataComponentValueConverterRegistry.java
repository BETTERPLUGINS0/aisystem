/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.ApiStatus$NonExtendable
 *  org.jetbrains.annotations.Contract
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.kyori.adventure.text.event;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.event.DataComponentValue;
import net.kyori.adventure.text.event.DataComponentValueConversionImpl;
import net.kyori.adventure.util.Services;
import net.kyori.examination.Examinable;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class DataComponentValueConverterRegistry {
    private static final Set<Provider> PROVIDERS = Services.services(Provider.class);

    private DataComponentValueConverterRegistry() {
    }

    public static Set<Key> knownProviders() {
        return Collections.unmodifiableSet(PROVIDERS.stream().map(Provider::id).collect(Collectors.toSet()));
    }

    @NotNull
    public static <O extends DataComponentValue> O convert(@NotNull Class<O> clazz, @NotNull Key key, @NotNull DataComponentValue dataComponentValue) {
        if (clazz.isInstance(dataComponentValue)) {
            return (O)((DataComponentValue)clazz.cast(dataComponentValue));
        }
        @Nullable RegisteredConversion registeredConversion = ConversionCache.converter(dataComponentValue.getClass(), clazz);
        if (registeredConversion == null) {
            throw new IllegalArgumentException("There is no data holder converter registered to convert from a " + dataComponentValue.getClass() + " instance to a " + clazz + " (on field " + key + ")");
        }
        try {
            return (O)((DataComponentValue)registeredConversion.conversion.convert(key, dataComponentValue));
        } catch (Exception exception) {
            throw new IllegalStateException("Failed to convert data component value of type " + dataComponentValue.getClass() + " to type " + clazz + " due to an error in a converter provided by " + registeredConversion.provider.asString() + "!", exception);
        }
    }

    static final class RegisteredConversion {
        static final RegisteredConversion NONE = new RegisteredConversion(null, null);
        final Key provider;
        final Conversion<?, ?> conversion;

        RegisteredConversion(Key key, Conversion<?, ?> conversion) {
            this.provider = key;
            this.conversion = conversion;
        }
    }

    static final class ConversionCache {
        private static final ConcurrentMap<Class<?>, ConcurrentMap<Class<?>, RegisteredConversion>> CACHE = new ConcurrentHashMap();
        private static final Map<Class<?>, Set<RegisteredConversion>> CONVERSIONS = ConversionCache.collectConversions();

        ConversionCache() {
        }

        private static Map<Class<?>, Set<RegisteredConversion>> collectConversions() {
            ConcurrentHashMap<Class, Set> concurrentHashMap = new ConcurrentHashMap<Class, Set>();
            for (Provider object : PROVIDERS) {
                @NotNull Key key = Objects.requireNonNull(object.id(), () -> "ID of provider " + object + " is null");
                for (Conversion<?, ?> conversion : object.conversions()) {
                    concurrentHashMap.computeIfAbsent(conversion.source(), clazz -> ConcurrentHashMap.newKeySet()).add(new RegisteredConversion(key, conversion));
                }
            }
            for (Map.Entry entry : concurrentHashMap.entrySet()) {
                entry.setValue(Collections.unmodifiableSet((Set)entry.getValue()));
            }
            return new ConcurrentHashMap(concurrentHashMap);
        }

        static RegisteredConversion compute(Class<?> clazz, Class<?> clazz2) {
            Class clazz3;
            ArrayDeque arrayDeque = new ArrayDeque();
            arrayDeque.add(clazz);
            while ((clazz3 = (Class)arrayDeque.poll()) != null) {
                Set<RegisteredConversion> set = CONVERSIONS.get(clazz3);
                if (set != null) {
                    RegisteredConversion registeredConversion = null;
                    for (RegisteredConversion registeredConversion2 : set) {
                        Class<?> clazz4 = registeredConversion2.conversion.destination();
                        if (clazz2.equals(clazz4)) {
                            return registeredConversion2;
                        }
                        if (!clazz2.isAssignableFrom(clazz4) || registeredConversion != null && !clazz4.isAssignableFrom(registeredConversion.conversion.destination())) continue;
                        registeredConversion = registeredConversion2;
                    }
                    if (registeredConversion != null) {
                        return registeredConversion;
                    }
                }
                ConversionCache.addSupertypes(clazz3, arrayDeque);
            }
            return RegisteredConversion.NONE;
        }

        private static void addSupertypes(Class<?> clazz, Deque<Class<?>> deque) {
            if (clazz.getSuperclass() != null) {
                deque.add(clazz.getSuperclass());
            }
            deque.addAll(Arrays.asList(clazz.getInterfaces()));
        }

        @Nullable
        static RegisteredConversion converter(Class<? extends DataComponentValue> clazz2, Class<? extends DataComponentValue> clazz4) {
            RegisteredConversion registeredConversion = CACHE.computeIfAbsent(clazz2, clazz -> new ConcurrentHashMap()).computeIfAbsent(clazz4, clazz3 -> ConversionCache.compute(clazz2, clazz4));
            if (registeredConversion == RegisteredConversion.NONE) {
                return null;
            }
            return registeredConversion;
        }
    }

    @ApiStatus.NonExtendable
    public static interface Conversion<I, O>
    extends Examinable {
        @NotNull
        public static <I1, O1> Conversion<I1, O1> convert(@NotNull Class<I1> src, @NotNull Class<O1> dst, @NotNull BiFunction<Key, I1, O1> op) {
            return new DataComponentValueConversionImpl<I1, O1>(Objects.requireNonNull(src, "src"), Objects.requireNonNull(dst, "dst"), Objects.requireNonNull(op, "op"));
        }

        @Contract(pure=true)
        @NotNull
        public Class<I> source();

        @Contract(pure=true)
        @NotNull
        public Class<O> destination();

        @NotNull
        public O convert(@NotNull Key var1, @NotNull I var2);
    }

    public static interface Provider {
        @NotNull
        public Key id();

        @NotNull
        public Iterable<Conversion<?, ?>> conversions();
    }
}

