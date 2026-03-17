/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.util.expiringmap;

public interface ExpirationListener<K, V> {
    public void expired(K var1, V var2);
}

