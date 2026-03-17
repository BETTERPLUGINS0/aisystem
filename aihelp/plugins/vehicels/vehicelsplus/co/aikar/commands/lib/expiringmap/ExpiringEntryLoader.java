/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package co.aikar.commands.lib.expiringmap;

import co.aikar.commands.lib.expiringmap.ExpiringValue;

public interface ExpiringEntryLoader<K, V> {
    public ExpiringValue<V> load(K var1);
}

