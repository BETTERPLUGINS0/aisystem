package me.PM2.infinitevehicles.xseries.reflection.jvm;

import me.PM2.infinitevehicles.xseries.reflection.XAccessFlag;
import me.PM2.infinitevehicles.xseries.reflection.XReflection;
import me.PM2.infinitevehicles.xseries.reflection.jvm.classes.ClassHandle;

public abstract class FlaggedNamedMemberHandle extends NamedMemberHandle {
   protected ClassHandle returnType;

   protected FlaggedNamedMemberHandle(ClassHandle var1) {
      super(var1);
   }

   public FlaggedNamedMemberHandle asStatic() {
      this.accessFlags.add(XAccessFlag.STATIC);
      return this;
   }

   public FlaggedNamedMemberHandle returns(Class<?> var1) {
      this.returnType = XReflection.of(var1);
      return this;
   }

   public FlaggedNamedMemberHandle returns(ClassHandle var1) {
      this.returnType = var1;
      return this;
   }

   public static Class<?>[] getParameters(Object var0, ClassHandle[] var1) {
      Class[] var2 = new Class[var1.length];
      int var3 = 0;
      ClassHandle[] var4 = var1;
      int var5 = var1.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         ClassHandle var7 = var4[var6];

         try {
            var2[var3++] = (Class)var7.unreflect();
         } catch (Throwable var9) {
            throw XReflection.throwCheckedException(new ReflectiveOperationException("Unknown parameter " + var7 + " for " + var0, var9));
         }
      }

      return var2;
   }

   protected Class<?> getReturnType() {
      try {
         return (Class)this.returnType.unreflect();
      } catch (Throwable var2) {
         throw XReflection.throwCheckedException(new ReflectiveOperationException("Unknown return type " + this.returnType + " for " + this));
      }
   }
}
