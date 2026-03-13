package com.volmit.iris.engine.data.cache;

public interface Multicache {
   <V> Cache<V> getCache(int id);

   <V> Cache<V> createCache();
}
