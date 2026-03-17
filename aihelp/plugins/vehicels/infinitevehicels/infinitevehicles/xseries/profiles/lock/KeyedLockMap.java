package me.PM2.infinitevehicles.xseries.profiles.lock;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
public final class KeyedLockMap<K> {
   private final Map<K, NulledKeyedLock<K, ?>> locks = new HashMap();

   private NulledKeyedLock<K, ?> createLock(K var1) {
      return new NulledKeyedLock(this, var1);
   }

   public <V> KeyedLock<K, V> lock(K var1, Supplier<V> var2) {
      Objects.requireNonNull(var2, "Value fetcher is null");
      return this.lock(var1, (var1x) -> {
         return var2.get();
      });
   }

   public <V> KeyedLock<K, V> lock(K var1, Function<K, V> var2) {
      Objects.requireNonNull(var2, "Value fetcher is null");
      Objects.requireNonNull(var1, "Key is null");
      Object var3 = var2.apply(var1);
      if (var3 != null) {
         return new FinalizedKeyedLock(var3);
      } else {
         NulledKeyedLock var4;
         synchronized(this.locks) {
            var4 = (NulledKeyedLock)this.locks.computeIfAbsent(var1, this::createLock);
            boolean var6 = var4.pendingTasks == 0;
            ++var4.pendingTasks;
            if (var6) {
               if (var4.tryLock()) {
                  return var4;
               }

               throw new IllegalStateException("Expected first lock holder to be free: " + var4);
            }
         }

         var4.lock();
         return new ReferentialKeyedLock(var4, var2);
      }
   }

   void unlock(NulledKeyedLock<K, ?> var1) {
      synchronized(this.locks) {
         --var1.pendingTasks;
         if (var1.pendingTasks <= 0) {
            this.locks.remove(var1.key);
         }
      }

      var1.lock.unlock();
   }

   public String toString() {
      return this.getClass().getSimpleName() + '(' + this.locks + ')';
   }
}
