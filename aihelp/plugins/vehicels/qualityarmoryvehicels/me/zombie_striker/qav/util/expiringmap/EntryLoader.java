/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.util.expiringmap;

public interface EntryLoader<K, V> {
    public V load(K var1);
}

