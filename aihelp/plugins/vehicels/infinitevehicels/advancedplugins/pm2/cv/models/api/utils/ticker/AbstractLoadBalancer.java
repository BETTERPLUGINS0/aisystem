package advancedplugins.pm2.cv.models.api.utils.ticker;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

public abstract class AbstractLoadBalancer<KEY, SER extends LoadBalancer.Server> implements LoadBalancer<KEY, SER> {
   protected final Set<SER> available = new HashSet();
   protected final Map<KEY, SER> reference = new ConcurrentHashMap();

   public AbstractLoadBalancer(int var1) {
      if (var1 <= 0) {
         throw new RuntimeException("Cannot create load balancer with less than 1 server: " + var1);
      } else {
         for(int var2 = 0; var2 < var1; ++var2) {
            this.available.add(this.supply());
         }

      }
   }

   public SER getOrRegister(KEY var1) {
      LoadBalancer.Server var2 = (LoadBalancer.Server)this.reference.get(var1);
      if (var2 != null) {
         return var2;
      } else {
         int var3 = Integer.MAX_VALUE;
         LoadBalancer.Server var4 = null;
         Iterator var5 = this.available.iterator();

         while(var5.hasNext()) {
            LoadBalancer.Server var6 = (LoadBalancer.Server)var5.next();
            if (var6.getLoad() < var3) {
               var4 = var6;
               var3 = var6.getLoad();
            }
         }

         if (var4 == null) {
            throw new RuntimeException("No server found.");
         } else {
            this.reference.put(var1, var4);
            return var4;
         }
      }
   }

   public SER get(KEY var1) {
      return (LoadBalancer.Server)this.reference.get(var1);
   }

   public void unregister(KEY var1) {
      this.reference.remove(var1);
   }

   public void execute(KEY var1, BiConsumer<KEY, SER> var2) {
      var2.accept(var1, this.getOrRegister(var1));
   }

   public Set<SER> getAvailable() {
      return this.available;
   }
}
