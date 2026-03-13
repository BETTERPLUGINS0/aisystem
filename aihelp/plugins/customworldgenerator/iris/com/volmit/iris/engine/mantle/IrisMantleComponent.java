package com.volmit.iris.engine.mantle;

import com.volmit.iris.util.mantle.flag.MantleFlag;
import lombok.Generated;

public abstract class IrisMantleComponent implements MantleComponent {
   private final EngineMantle engineMantle;
   private final MantleFlag flag;
   private final int priority;
   private volatile int radius = -1;
   private final Object lock = new Object();
   private boolean enabled = true;

   protected abstract int computeRadius();

   public void hotload() {
      synchronized(this.lock) {
         this.radius = -1;
      }
   }

   public final int getRadius() {
      int var1 = this.radius;
      if (var1 != -1) {
         return var1;
      } else {
         synchronized(this.lock) {
            if ((var1 = this.radius) != -1) {
               return var1;
            } else {
               var1 = this.computeRadius();
               if (var1 < 0) {
                  var1 = 0;
               }

               return this.radius = var1;
            }
         }
      }
   }

   @Generated
   public IrisMantleComponent(final EngineMantle engineMantle, final MantleFlag flag, final int priority) {
      this.engineMantle = var1;
      this.flag = var2;
      this.priority = var3;
   }

   @Generated
   public EngineMantle getEngineMantle() {
      return this.engineMantle;
   }

   @Generated
   public MantleFlag getFlag() {
      return this.flag;
   }

   @Generated
   public int getPriority() {
      return this.priority;
   }

   @Generated
   public Object getLock() {
      return this.lock;
   }

   @Generated
   public boolean isEnabled() {
      return this.enabled;
   }

   @Generated
   public void setRadius(final int radius) {
      this.radius = var1;
   }

   @Generated
   public void setEnabled(final boolean enabled) {
      this.enabled = var1;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisMantleComponent)) {
         return false;
      } else {
         IrisMantleComponent var2 = (IrisMantleComponent)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (this.getPriority() != var2.getPriority()) {
            return false;
         } else if (this.getRadius() != var2.getRadius()) {
            return false;
         } else if (this.isEnabled() != var2.isEnabled()) {
            return false;
         } else {
            MantleFlag var3 = this.getFlag();
            MantleFlag var4 = var2.getFlag();
            if (var3 == null) {
               if (var4 != null) {
                  return false;
               }
            } else if (!var3.equals(var4)) {
               return false;
            }

            Object var5 = this.getLock();
            Object var6 = var2.getLock();
            if (var5 == null) {
               if (var6 != null) {
                  return false;
               }
            } else if (!var5.equals(var6)) {
               return false;
            }

            return true;
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof IrisMantleComponent;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      int var5 = var2 * 59 + this.getPriority();
      var5 = var5 * 59 + this.getRadius();
      var5 = var5 * 59 + (this.isEnabled() ? 79 : 97);
      MantleFlag var3 = this.getFlag();
      var5 = var5 * 59 + (var3 == null ? 43 : var3.hashCode());
      Object var4 = this.getLock();
      var5 = var5 * 59 + (var4 == null ? 43 : var4.hashCode());
      return var5;
   }

   @Generated
   public String toString() {
      String var10000 = String.valueOf(this.getFlag());
      return "IrisMantleComponent(flag=" + var10000 + ", priority=" + this.getPriority() + ", radius=" + this.getRadius() + ", lock=" + String.valueOf(this.getLock()) + ", enabled=" + this.isEnabled() + ")";
   }
}
