package com.volmit.iris.engine.object;

import com.volmit.iris.engine.framework.Engine;
import com.volmit.iris.util.collection.KMap;
import lombok.Generated;
import lombok.NonNull;

public class IrisSpawnerCooldowns {
   private final KMap<String, IrisEngineSpawnerCooldown> cooldowns = new KMap();

   public IrisEngineSpawnerCooldown getCooldown(@NonNull IrisSpawner spawner) {
      if (var1 == null) {
         throw new NullPointerException("spawner is marked non-null but is null");
      } else {
         return this.getCooldown(var1.getLoadKey());
      }
   }

   public IrisEngineSpawnerCooldown getCooldown(@NonNull String loadKey) {
      if (var1 == null) {
         throw new NullPointerException("loadKey is marked non-null but is null");
      } else {
         return (IrisEngineSpawnerCooldown)this.cooldowns.computeIfAbsent(var1, (var1x) -> {
            IrisEngineSpawnerCooldown var2 = new IrisEngineSpawnerCooldown();
            var2.setSpawner(var1);
            return var2;
         });
      }
   }

   public void cleanup(Engine engine) {
      this.cooldowns.values().removeIf((var1x) -> {
         IrisSpawner var2 = (IrisSpawner)var1.getData().getSpawnerLoader().load(var1x.getSpawner());
         return var2 == null || var1x.canSpawn(var2.getMaximumRate());
      });
   }

   public boolean isEmpty() {
      return this.cooldowns.isEmpty();
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisSpawnerCooldowns)) {
         return false;
      } else {
         IrisSpawnerCooldowns var2 = (IrisSpawnerCooldowns)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else {
            KMap var3 = this.cooldowns;
            KMap var4 = var2.cooldowns;
            if (var3 == null) {
               if (var4 != null) {
                  return false;
               }
            } else if (!var3.equals(var4)) {
               return false;
            }

            return true;
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof IrisSpawnerCooldowns;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      KMap var3 = this.cooldowns;
      int var4 = var2 * 59 + (var3 == null ? 43 : var3.hashCode());
      return var4;
   }
}
