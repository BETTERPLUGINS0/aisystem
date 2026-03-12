package fr.xephi.authme.libs.org.mariadb.jdbc.pool;

import fr.xephi.authme.libs.org.mariadb.jdbc.Configuration;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public final class Pools {
   private static final AtomicInteger poolIndex = new AtomicInteger();
   private static final Map<Configuration, Pool> poolMap = new ConcurrentHashMap();
   private static ScheduledThreadPoolExecutor poolExecutor = null;

   public static Pool retrievePool(Configuration conf) {
      if (!poolMap.containsKey(conf)) {
         synchronized(poolMap) {
            if (!poolMap.containsKey(conf)) {
               if (poolExecutor == null) {
                  poolExecutor = new ScheduledThreadPoolExecutor(1, new PoolThreadFactory("MariaDbPool-maxTimeoutIdle-checker"));
               }

               Pool pool = new Pool(conf, poolIndex.incrementAndGet(), poolExecutor);
               poolMap.put(conf, pool);
               return pool;
            }
         }
      }

      return (Pool)poolMap.get(conf);
   }

   public static void remove(Pool pool) {
      if (poolMap.containsKey(pool.getConf())) {
         synchronized(poolMap) {
            if (poolMap.containsKey(pool.getConf())) {
               poolMap.remove(pool.getConf());
               if (poolMap.isEmpty()) {
                  shutdownExecutor();
               }
            }
         }
      }

   }

   public static void close() {
      synchronized(poolMap) {
         Iterator var1 = poolMap.values().iterator();

         while(var1.hasNext()) {
            Pool pool = (Pool)var1.next();

            try {
               pool.close();
            } catch (Exception var5) {
            }
         }

         shutdownExecutor();
         poolMap.clear();
      }
   }

   public static void close(String poolName) {
      if (poolName != null) {
         synchronized(poolMap) {
            Iterator var2 = poolMap.values().iterator();

            Pool pool;
            do {
               if (!var2.hasNext()) {
                  return;
               }

               pool = (Pool)var2.next();
            } while(!poolName.equals(pool.getConf().poolName()));

            try {
               pool.close();
            } catch (Exception var6) {
            }

         }
      }
   }

   private static void shutdownExecutor() {
      if (poolExecutor != null) {
         poolExecutor.shutdown();

         try {
            poolExecutor.awaitTermination(10L, TimeUnit.SECONDS);
         } catch (InterruptedException var1) {
         }

         poolExecutor = null;
      }

   }
}
