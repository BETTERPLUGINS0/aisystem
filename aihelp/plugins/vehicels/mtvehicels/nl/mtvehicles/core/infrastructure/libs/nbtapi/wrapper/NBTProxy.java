/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package nl.mtvehicles.core.infrastructure.libs.nbtapi.wrapper;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.iface.NBTHandler;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.wrapper.Casing;

public interface NBTProxy {
    public static final Map<Class<?>, NBTHandler<Object>> handlers = new HashMap();

    default public void init() {
    }

    default public Casing getCasing() {
        return Casing.PascalCase;
    }

    default public <T> NBTHandler<T> getHandler(Class<T> clazz) {
        return handlers.get(clazz);
    }

    default public Collection<NBTHandler<Object>> getHandlers() {
        return handlers.values();
    }

    default public <T> void registerHandler(Class<T> clazz, NBTHandler<T> handler) {
        handlers.put(clazz, handler);
    }
}

