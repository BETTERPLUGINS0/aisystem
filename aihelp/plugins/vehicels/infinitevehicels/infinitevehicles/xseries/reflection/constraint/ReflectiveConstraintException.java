package me.PM2.infinitevehicles.xseries.reflection.constraint;

import me.PM2.infinitevehicles.xseries.reflection.ReflectiveHandle;
import me.PM2.infinitevehicles.xseries.reflection.XAccessFlag;

public class ReflectiveConstraintException extends RuntimeException {
   private final ReflectiveConstraint constraint;
   private final ReflectiveConstraint.Result result;

   private ReflectiveConstraintException(ReflectiveConstraint var1, ReflectiveConstraint.Result var2, String var3) {
      super(var3);
      this.constraint = var1;
      this.result = var2;
   }

   public ReflectiveConstraint getConstraint() {
      return this.constraint;
   }

   public ReflectiveConstraint.Result getResult() {
      return this.result;
   }

   public static ReflectiveConstraintException create(ReflectiveConstraint var0, ReflectiveConstraint.Result var1, ReflectiveHandle<?> var2, Object var3) {
      String var4;
      switch(var1) {
      case MATCHED:
         throw new IllegalArgumentException("Cannot create an exception if results are successful: " + var0 + " -> MATCHED");
      case INCOMPATIBLE:
         var4 = "The constraint " + var0 + " cannot be applied to " + var2;
         break;
      case NOT_MATCHED:
         var4 = "Found " + var2 + " with JVM " + var3 + ", however it doesn't match the constraint: " + var0 + " - " + (String)XAccessFlag.getModifiers(var3).map(XAccessFlag::toString).orElse("[NO MODIFIER]");
         break;
      default:
         throw new AssertionError("Unknown reflective constraint result: " + var1);
      }

      return new ReflectiveConstraintException(var0, var1, var4);
   }
}
