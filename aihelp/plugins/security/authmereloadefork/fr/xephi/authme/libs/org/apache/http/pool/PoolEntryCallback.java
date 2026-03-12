package fr.xephi.authme.libs.org.apache.http.pool;

public interface PoolEntryCallback<T, C> {
   void process(PoolEntry<T, C> var1);
}
