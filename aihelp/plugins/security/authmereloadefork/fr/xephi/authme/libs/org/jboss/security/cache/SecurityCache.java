package fr.xephi.authme.libs.org.jboss.security.cache;

import java.util.Map;

public interface SecurityCache<T> {
   void addCacheEntry(T var1, Map<String, Object> var2) throws SecurityCacheException;

   boolean cacheHit(T var1);

   void cacheOperation(T var1, Map<String, Object> var2) throws SecurityCacheException;

   <Y> Y get(T var1) throws SecurityCacheException;
}
