package com.volmit.iris.engine.object;

import com.volmit.iris.engine.framework.Engine;
import com.volmit.iris.util.math.M;
import lombok.Generated;

public class IrisEngineSpawnerCooldown {
   private long lastSpawn = 0L;
   private String spawner;

   public void spawn(Engine engine) {
      this.lastSpawn = M.ms();
   }

   public boolean canSpawn(IrisRate s) {
      return M.ms() - this.lastSpawn > var1.getInterval();
   }

   @Generated
   public long getLastSpawn() {
      return this.lastSpawn;
   }

   @Generated
   public String getSpawner() {
      return this.spawner;
   }

   @Generated
   public void setLastSpawn(final long lastSpawn) {
      this.lastSpawn = var1;
   }

   @Generated
   public void setSpawner(final String spawner) {
      this.spawner = var1;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisEngineSpawnerCooldown)) {
         return false;
      } else {
         IrisEngineSpawnerCooldown var2 = (IrisEngineSpawnerCooldown)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (this.getLastSpawn() != var2.getLastSpawn()) {
            return false;
         } else {
            String var3 = this.getSpawner();
            String var4 = var2.getSpawner();
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
      return var1 instanceof IrisEngineSpawnerCooldown;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      long var3 = this.getLastSpawn();
      int var6 = var2 * 59 + (int)(var3 >>> 32 ^ var3);
      String var5 = this.getSpawner();
      var6 = var6 * 59 + (var5 == null ? 43 : var5.hashCode());
      return var6;
   }

   @Generated
   public String toString() {
      long var10000 = this.getLastSpawn();
      return "IrisEngineSpawnerCooldown(lastSpawn=" + var10000 + ", spawner=" + this.getSpawner() + ")";
   }
}
