/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.util.expiringmap;

import me.zombie_striker.qav.util.expiringmap.ExpiringValue;

public interface ExpiringEntryLoader<K, V> {
    public ExpiringValue<V> load(K var1);
}

