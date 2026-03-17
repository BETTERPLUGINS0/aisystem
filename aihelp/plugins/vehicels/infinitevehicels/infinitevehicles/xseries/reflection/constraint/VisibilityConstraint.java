package me.PM2.infinitevehicles.xseries.reflection.constraint;

import java.lang.reflect.Member;
import me.PM2.infinitevehicles.xseries.reflection.ReflectiveHandle;
import me.PM2.infinitevehicles.xseries.reflection.XAccessFlag;
import org.jetbrains.annotations.ApiStatus.Experimental;
import org.jetbrains.annotations.ApiStatus.Internal;

@Experimental
public enum VisibilityConstraint implements ReflectiveConstraint {
   PUBLIC(XAccessFlag.PUBLIC),
   PRIVATE(XAccessFlag.PRIVATE),
   PROTECTED(XAccessFlag.PROTECTED);

   private final XAccessFlag accessFlag;

   private VisibilityConstraint(XAccessFlag param3) {
      this.accessFlag = var3;
   }

   @Internal
   public XAccessFlag getAccessFlag() {
      return this.accessFlag;
   }

   public ReflectiveConstraint.Result appliesTo(ReflectiveHandle<?> var1, Object var2) {
      int var3;
      if (var2 instanceof Class) {
         var3 = ((Class)var2).getModifiers();
         if (this == PRIVATE) {
            return ReflectiveConstraint.Result.INCOMPATIBLE;
         }

         if (this == PROTECTED) {
            return ReflectiveConstraint.Result.of(!XAccessFlag.PUBLIC.isSet(var3));
         }
      } else {
         if (!(var2 instanceof Member)) {
            return ReflectiveConstraint.Result.INCOMPATIBLE;
         }

         var3 = ((Member)var2).getModifiers();
      }

      return ReflectiveConstraint.Result.of(this.accessFlag.isSet(var3));
   }

   public String category() {
      return "Visibility";
   }

   public String toString() {
      return this.getClass().getSimpleName() + "::" + this.name();
   }

   // $FF: synthetic method
   private static VisibilityConstraint[] $values() {
      return new VisibilityConstraint[]{PUBLIC, PRIVATE, PROTECTED};
   }
}
