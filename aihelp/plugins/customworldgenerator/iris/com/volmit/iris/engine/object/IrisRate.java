package com.volmit.iris.engine.object;

import com.volmit.iris.engine.object.annotations.Desc;
import com.volmit.iris.engine.object.annotations.Snippet;
import com.volmit.iris.util.format.Form;
import com.volmit.iris.util.scheduling.ChronoLatch;
import lombok.Generated;

@Snippet("rate")
@Desc("Represents a count of something per time duration")
public class IrisRate {
   @Desc("The amount of things. Leave 0 for infinite (meaning always spawn whenever)")
   private int amount = 0;
   @Desc("The time interval. Leave blank for infinite 0 (meaning always spawn all the time)")
   private IrisDuration per = new IrisDuration();

   public String toString() {
      String var10000 = Form.f(this.amount);
      return var10000 + "/" + String.valueOf(this.per);
   }

   public long getInterval() {
      long var1 = this.per.toMilliseconds() / (long)(this.amount == 0 ? 1 : this.amount);
      return Math.abs(var1 <= 0L ? 1L : var1);
   }

   public ChronoLatch toChronoLatch() {
      return new ChronoLatch(this.getInterval());
   }

   public boolean isInfinite() {
      return this.per.toMilliseconds() == 0L;
   }

   @Generated
   public IrisRate() {
   }

   @Generated
   public IrisRate(final int amount, final IrisDuration per) {
      this.amount = var1;
      this.per = var2;
   }

   @Generated
   public int getAmount() {
      return this.amount;
   }

   @Generated
   public IrisDuration getPer() {
      return this.per;
   }

   @Generated
   public void setAmount(final int amount) {
      this.amount = var1;
   }

   @Generated
   public void setPer(final IrisDuration per) {
      this.per = var1;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisRate)) {
         return false;
      } else {
         IrisRate var2 = (IrisRate)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (this.getAmount() != var2.getAmount()) {
            return false;
         } else {
            IrisDuration var3 = this.getPer();
            IrisDuration var4 = var2.getPer();
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
      return var1 instanceof IrisRate;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      int var4 = var2 * 59 + this.getAmount();
      IrisDuration var3 = this.getPer();
      var4 = var4 * 59 + (var3 == null ? 43 : var3.hashCode());
      return var4;
   }
}
