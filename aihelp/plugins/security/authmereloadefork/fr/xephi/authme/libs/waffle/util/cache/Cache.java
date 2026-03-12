package fr.xephi.authme.libs.waffle.util.cache;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.ServiceLoader;

public interface Cache<K, V> {
   static <K, V> Cache<K, V> newCache(int timeout) throws NoSuchElementException {
      NoSuchElementException exception = new NoSuchElementException();
      Iterator var2 = ServiceLoader.load(CacheSupplier.class).iterator();

      while(var2.hasNext()) {
         CacheSupplier cacheSupplier = (CacheSupplier)var2.next();

         try {
            return cacheSupplier.newCache((long)timeout);
         } catch (Exception var5) {
            exception.addSuppressed(var5);
         }
      }

      throw exception;
   }

   V get(K var1);

   void put(K var1, V var2);

   void remove(K var1);

   int size();
}
