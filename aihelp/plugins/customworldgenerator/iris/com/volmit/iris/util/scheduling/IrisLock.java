package com.volmit.iris.util.scheduling;

import com.volmit.iris.Iris;
import java.util.concurrent.locks.ReentrantLock;
import lombok.Generated;

public class IrisLock {
   private final transient ReentrantLock lock;
   private final transient String name;
   private transient boolean disabled = false;

   public IrisLock(String name) {
      this.name = var1;
      this.lock = new ReentrantLock(false);
   }

   public void lock() {
      if (!this.disabled) {
         this.lock.lock();
      }
   }

   public void unlock() {
      if (!this.disabled) {
         try {
            this.lock.unlock();
         } catch (Throwable var2) {
            Iris.reportError(var2);
         }

      }
   }

   @Generated
   public ReentrantLock getLock() {
      return this.lock;
   }

   @Generated
   public String getName() {
      return this.name;
   }

   @Generated
   public boolean isDisabled() {
      return this.disabled;
   }

   @Generated
   public IrisLock setDisabled(final boolean disabled) {
      this.disabled = var1;
      return this;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisLock)) {
         return false;
      } else {
         IrisLock var2 = (IrisLock)var1;
         return var2.canEqual(this);
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof IrisLock;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      return 1;
   }

   @Generated
   public String toString() {
      String var10000 = String.valueOf(this.getLock());
      return "IrisLock(lock=" + var10000 + ", name=" + this.getName() + ", disabled=" + this.isDisabled() + ")";
   }
}
