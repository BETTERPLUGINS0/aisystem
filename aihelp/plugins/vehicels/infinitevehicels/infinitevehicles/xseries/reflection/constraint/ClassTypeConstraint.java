package me.PM2.infinitevehicles.xseries.reflection.constraint;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import me.PM2.infinitevehicles.xseries.reflection.ReflectiveHandle;
import me.PM2.infinitevehicles.xseries.reflection.XAccessFlag;
import org.jetbrains.annotations.ApiStatus.Experimental;

@Experimental
public enum ClassTypeConstraint implements ReflectiveConstraint {
   INTERFACE {
      protected boolean test(Class<?> var1) {
         return var1.isInterface();
      }
   },
   ABSTRACT {
      protected boolean test(Class<?> var1) {
         return XAccessFlag.ABSTRACT.isSet(var1.getModifiers());
      }
   },
   ENUM {
      protected boolean test(Class<?> var1) {
         return var1.isEnum();
      }
   },
   RECORD {
      private final MethodHandle isRecord;

      {
         MethodHandle var3;
         try {
            var3 = MethodHandles.lookup().findVirtual(Class.class, "isRecord", MethodType.methodType(Boolean.TYPE));
         } catch (IllegalAccessException | NoSuchMethodException var5) {
            var3 = null;
         }

         this.isRecord = var3;
      }

      protected boolean test(Class<?> var1) {
         try {
            return this.isRecord.invoke(var1);
         } catch (Throwable var3) {
            throw new IllegalStateException("Cannot use Class#isRecord", var3);
         }
      }
   },
   ANNOTATION {
      protected boolean test(Class<?> var1) {
         return var1.isAnnotation();
      }
   };

   private ClassTypeConstraint() {
   }

   protected abstract boolean test(Class<?> var1);

   public ReflectiveConstraint.Result appliesTo(ReflectiveHandle<?> var1, Object var2) {
      return var2 instanceof Class ? ReflectiveConstraint.Result.of(this.test((Class)var2)) : ReflectiveConstraint.Result.INCOMPATIBLE;
   }

   public String category() {
      return "ClassType";
   }

   public String toString() {
      return this.getClass().getSimpleName() + "::" + this.name();
   }

   // $FF: synthetic method
   private static ClassTypeConstraint[] $values() {
      return new ClassTypeConstraint[]{INTERFACE, ABSTRACT, ENUM, RECORD, ANNOTATION};
   }

   // $FF: synthetic method
   ClassTypeConstraint(Object var3) {
      this();
   }
}
