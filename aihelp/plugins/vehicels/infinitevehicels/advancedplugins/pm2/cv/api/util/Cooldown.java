package advancedplugins.pm2.cv.api.util;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class Cooldown<A> {
   private Map<A, Long> cooldowns = new HashMap();

   public boolean hasCooldown(A var1) {
      this.cooldowns.entrySet().removeIf((var0) -> {
         return (Long)var0.getValue() <= System.currentTimeMillis();
      });
      return this.cooldowns.containsKey(var1);
   }

   public void addCooldown(A var1, Long var2) {
      this.cooldowns.put(var1, var2);
   }

   public void addCooldown(A var1, Duration var2) {
      this.cooldowns.put(var1, System.currentTimeMillis() + var2.toMillis());
   }

   public void clearCooldown(A var1) {
      this.cooldowns.remove(var1);
   }
}
