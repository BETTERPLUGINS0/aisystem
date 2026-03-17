package me.PM2.infinitevehicles.xseries.profiles.objects.cache;

import java.time.Duration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
public abstract class TimedCacheableProfileable extends CacheableProfileable {
   private long lastUpdate;

   @NotNull
   protected Duration expiresAfter() {
      return Duration.ofHours(6L);
   }

   public final boolean hasExpired(boolean var1) {
      Duration var2 = this.expiresAfter();
      if (var2.isZero()) {
         return false;
      } else if (super.hasExpired(var1)) {
         return true;
      } else if (this.cache == null && this.lastError == null) {
         return true;
      } else {
         long var3 = System.currentTimeMillis();
         if (this.lastUpdate == 0L) {
            if (var1) {
               this.lastUpdate = var3;
            }

            return true;
         } else {
            long var5 = var3 - this.lastUpdate;
            if (var5 >= var2.toMillis()) {
               if (var1) {
                  this.lastUpdate = var3;
               }

               return true;
            } else {
               return false;
            }
         }
      }
   }
}
