/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package co.aikar.commands.lib.expiringmap;

public interface ExpirationListener<K, V> {
    public void expired(K var1, V var2);
}

