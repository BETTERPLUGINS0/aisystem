/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package nl.sbdeveloper.vehiclesplus.libs.inventory;

import java.util.function.Consumer;

public class InventoryListener<T> {
    private Class<T> type;
    private Consumer<T> consumer;

    public InventoryListener(Class<T> clazz, Consumer<T> consumer) {
        this.type = clazz;
        this.consumer = consumer;
    }

    public void accept(T t) {
        this.consumer.accept(t);
    }

    public Class<T> getType() {
        return this.type;
    }
}

