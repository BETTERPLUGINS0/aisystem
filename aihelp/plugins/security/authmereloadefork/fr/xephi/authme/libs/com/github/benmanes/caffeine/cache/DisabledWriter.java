package fr.xephi.authme.libs.com.github.benmanes.caffeine.cache;

import org.checkerframework.checker.nullness.qual.Nullable;

enum DisabledWriter implements CacheWriter<Object, Object> {
   INSTANCE;

   public void write(Object key, Object value) {
   }

   public void delete(Object key, @Nullable Object value, RemovalCause cause) {
   }
}
