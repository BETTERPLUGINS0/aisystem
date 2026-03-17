/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package net.kyori.adventure.util;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
import java.util.Set;
import net.kyori.adventure.internal.properties.AdventureProperties;
import net.kyori.adventure.util.Services0;
import org.jetbrains.annotations.NotNull;

public final class Services {
    private static final boolean SERVICE_LOAD_FAILURES_ARE_FATAL = Boolean.TRUE.equals(AdventureProperties.SERVICE_LOAD_FAILURES_ARE_FATAL.value());

    private Services() {
    }

    @NotNull
    public static <P> Optional<P> service(@NotNull Class<P> clazz) {
        ServiceLoader<P> serviceLoader = Services0.loader(clazz);
        Iterator<P> iterator = serviceLoader.iterator();
        while (iterator.hasNext()) {
            P p;
            try {
                p = iterator.next();
            } catch (Throwable throwable) {
                if (!SERVICE_LOAD_FAILURES_ARE_FATAL) continue;
                throw new IllegalStateException("Encountered an exception loading service " + clazz, throwable);
            }
            if (iterator.hasNext()) {
                throw new IllegalStateException("Expected to find one service " + clazz + ", found multiple");
            }
            return Optional.of(p);
        }
        return Optional.empty();
    }

    @NotNull
    public static <P> Optional<P> serviceWithFallback(@NotNull Class<P> clazz) {
        ServiceLoader<P> serviceLoader = Services0.loader(clazz);
        Iterator<P> iterator = serviceLoader.iterator();
        Object t = null;
        while (iterator.hasNext()) {
            P p;
            try {
                p = iterator.next();
            } catch (Throwable throwable) {
                if (!SERVICE_LOAD_FAILURES_ARE_FATAL) continue;
                throw new IllegalStateException("Encountered an exception loading service " + clazz, throwable);
            }
            if (p instanceof Fallback) {
                if (t != null) continue;
                t = p;
                continue;
            }
            return Optional.of(p);
        }
        return Optional.ofNullable(t);
    }

    public static <P> Set<P> services(Class<? extends P> clazz) {
        ServiceLoader<P> serviceLoader = Services0.loader(clazz);
        HashSet<P> hashSet = new HashSet<P>();
        Iterator<P> iterator = serviceLoader.iterator();
        while (iterator.hasNext()) {
            P p;
            try {
                p = iterator.next();
            } catch (ServiceConfigurationError serviceConfigurationError) {
                if (!SERVICE_LOAD_FAILURES_ARE_FATAL) continue;
                throw new IllegalStateException("Encountered an exception loading a provider for " + clazz + ": ", serviceConfigurationError);
            }
            hashSet.add(p);
        }
        return Collections.unmodifiableSet(hashSet);
    }

    public static interface Fallback {
    }
}

