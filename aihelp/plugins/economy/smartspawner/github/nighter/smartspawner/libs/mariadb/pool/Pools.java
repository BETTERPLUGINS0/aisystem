package github.nighter.smartspawner.libs.mariadb.pool;

import github.nighter.smartspawner.libs.mariadb.Configuration;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public final class Pools {
   private static final AtomicInteger poolIndex = new AtomicInteger();
   private static final Map<Configuration, Pools.PoolHolder> poolMap = new ConcurrentHashMap();
   private static ScheduledThreadPoolExecutor poolExecutor = null;

   public static Pool retrievePool(Configuration conf) {
      Pools.PoolHolder holder = (Pools.PoolHolder)poolMap.get(conf);
      if (holder == null) {
         synchronized(poolMap) {
            holder = (Pools.PoolHolder)poolMap.get(conf);
            if (holder == null) {
               if (poolExecutor == null) {
                  poolExecutor = new ScheduledThreadPoolExecutor(1, new PoolThreadFactory("MariaDbPool-maxTimeoutIdle-checker"));
               }

               holder = new Pools.PoolHolder(conf, poolIndex.incrementAndGet(), poolExecutor);
               poolMap.put(conf, holder);
            }
         }
      }

      return holder.getPool();
   }

   public static void remove(Pool pool) {
      if (poolMap.containsKey(pool.getConf())) {
         synchronized(poolMap) {
            Pools.PoolHolder previous = (Pools.PoolHolder)poolMap.remove(pool.getConf());
            if (previous != null && poolMap.isEmpty()) {
               shutdownExecutor();
            }
         }
      }

   }

   public static void close() {
      synchronized(poolMap) {
         Iterator var1 = poolMap.values().iterator();

         while(var1.hasNext()) {
            Pools.PoolHolder holder = (Pools.PoolHolder)var1.next();

            try {
               holder.getPool().close();
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

            Pools.PoolHolder holder;
            do {
               if (!var2.hasNext()) {
                  return;
               }

               holder = (Pools.PoolHolder)var2.next();
            } while(!poolName.equals(holder.conf.poolName()));

            try {
               holder.getPool().close();
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

   static class PoolHolder {
      private final Configuration conf;
      private final int poolIndex;
      private final ScheduledThreadPoolExecutor executor;
      private Pool pool;

      PoolHolder(Configuration conf, int poolIndex, ScheduledThreadPoolExecutor executor) {
         this.conf = conf;
         this.poolIndex = poolIndex;
         this.executor = executor;
      }

      synchronized Pool getPool() {
         if (this.pool == null) {
            this.pool = new Pool(this.conf, this.poolIndex, this.executor);
         }

         return this.pool;
      }
   }
}
