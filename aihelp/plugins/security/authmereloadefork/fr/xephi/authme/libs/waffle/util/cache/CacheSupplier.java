package fr.xephi.authme.libs.waffle.util.cache;

public interface CacheSupplier {
   <K, V> Cache<K, V> newCache(long var1);
}
