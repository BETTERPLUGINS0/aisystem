package com.volmit.iris.engine.framework;

import com.volmit.iris.Iris;
import com.volmit.iris.util.math.RollingSequence;
import lombok.Generated;

public class EngineAssignedComponent implements EngineComponent {
   private final Engine engine;
   private final RollingSequence metrics;
   private final String name;

   public EngineAssignedComponent(Engine engine, String name) {
      int var10000 = var1.getCacheID();
      Iris.debug("Engine: " + var10000 + " Starting " + var2);
      this.engine = var1;
      this.metrics = new RollingSequence(16);
      this.name = var2;
   }

   @Generated
   public Engine getEngine() {
      return this.engine;
   }

   @Generated
   public RollingSequence getMetrics() {
      return this.metrics;
   }

   @Generated
   public String getName() {
      return this.name;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof EngineAssignedComponent)) {
         return false;
      } else {
         EngineAssignedComponent var2 = (EngineAssignedComponent)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else {
            RollingSequence var3 = this.getMetrics();
            RollingSequence var4 = var2.getMetrics();
            if (var3 == null) {
               if (var4 != null) {
                  return false;
               }
            } else if (!var3.equals(var4)) {
               return false;
            }

            String var5 = this.getName();
            String var6 = var2.getName();
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
      return var1 instanceof EngineAssignedComponent;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      RollingSequence var3 = this.getMetrics();
      int var5 = var2 * 59 + (var3 == null ? 43 : var3.hashCode());
      String var4 = this.getName();
      var5 = var5 * 59 + (var4 == null ? 43 : var4.hashCode());
      return var5;
   }

   @Generated
   public String toString() {
      String var10000 = String.valueOf(this.getMetrics());
      return "EngineAssignedComponent(metrics=" + var10000 + ", name=" + this.getName() + ")";
   }
}
