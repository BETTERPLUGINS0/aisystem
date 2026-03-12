package fr.xephi.authme.libs.org.jboss.security;

import java.util.Set;

public interface CacheableManager<T, K> {
   void setCache(T var1);

   void flushCache();

   void flushCache(K var1);

   boolean containsKey(K var1);

   Set<K> getCachedKeys();
}
