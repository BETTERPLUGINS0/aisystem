package me.PM2.infinitevehicles.xseries.profiles.lock;

import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.ApiStatus.OverrideOnly;

@Internal
public interface KeyedLock<K, V> extends AutoCloseable {
   V getOrRetryValue();

   @OverrideOnly
   void lock();

   void unlock();

   void close();
}
