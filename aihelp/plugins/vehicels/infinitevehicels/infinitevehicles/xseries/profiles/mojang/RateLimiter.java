package me.PM2.infinitevehicles.xseries.profiles.mojang;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.time.Duration;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.jetbrains.annotations.Unmodifiable;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
public final class RateLimiter {
   private final ConcurrentLinkedQueue<Long> requests = new ConcurrentLinkedQueue();
   private final int maxRequests;
   private final long per;

   RateLimiter(int var1, Duration var2) {
      this.maxRequests = var1;
      this.per = var2.toMillis();
   }

   @CanIgnoreReturnValue
   @Unmodifiable
   private ConcurrentLinkedQueue<Long> getRequests() {
      if (this.requests.isEmpty()) {
         return this.requests;
      } else {
         long var1 = System.currentTimeMillis();
         Iterator var3 = this.requests.iterator();

         while(var3.hasNext()) {
            long var4 = (Long)var3.next();
            long var6 = var1 - var4;
            if (var6 <= this.per) {
               break;
            }

            var3.remove();
         }

         return this.requests;
      }
   }

   public int getRemainingRequests() {
      return Math.max(0, this.maxRequests - this.getRequests().size());
   }

   public int getEffectiveRequestsCount() {
      return this.getRequests().size();
   }

   public void instantRateLimit() {
      long var1 = System.currentTimeMillis();

      for(int var3 = 0; var3 < this.getRemainingRequests(); ++var3) {
         this.requests.add(var1);
      }

   }

   public boolean acquire() {
      if (this.getRemainingRequests() <= 0) {
         return false;
      } else {
         this.requests.add(System.currentTimeMillis());
         return true;
      }
   }

   public Duration timeUntilNextFreeRequest() {
      if (this.getRemainingRequests() == 0) {
         long var1 = System.currentTimeMillis();
         long var3 = (Long)this.requests.peek();
         long var5 = var1 - var3;
         return Duration.ofMillis(this.per - var5);
      } else {
         return Duration.ZERO;
      }
   }

   public synchronized void acquireOrWait() {
      long var1 = this.timeUntilNextFreeRequest().toMillis();
      if (var1 != 0L) {
         try {
            Thread.sleep(var1);
         } catch (InterruptedException var4) {
            throw new IllegalStateException("RateLimiter lock was interrupted unexpectedly", var4);
         }
      }
   }

   public String toString() {
      return this.getClass().getSimpleName() + "[total=" + this.getRequests().size() + ", remaining=" + this.getRemainingRequests() + ", maxRequests=" + this.maxRequests + ", per=" + this.per + ']';
   }
}
