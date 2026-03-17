package me.PM2.infinitevehicles.commands.lib.expiringmap;

public interface ExpiringEntryLoader<K, V> {
   ExpiringValue<V> load(K var1);
}
