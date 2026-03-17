package me.PM2.infinitevehicles.xseries.reflection.asm;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

final class ASMClassLoader extends ClassLoader {
   private static final String DEFINE_CLASS = "defineClass";

   protected ASMClassLoader() {
   }

   protected Class<?> defineClass(String var1, byte[] var2) {
      return super.defineClass(asmTypeToBinary(var1), var2, 0, var2.length);
   }

   private static Class<?> defineClassLombockTransplanter(String var0, byte[] var1, ClassLoader var2) {
      try {
         Class var3 = Class.forName("java.lang.invoke.MethodHandles");
         Class var4 = Class.forName("java.lang.invoke.MethodHandle");
         Class var5 = Class.forName("java.lang.invoke.MethodType");
         Class var6 = Class.forName("java.lang.invoke.MethodHandles$Lookup");
         Method var7 = var3.getDeclaredMethod("lookup");
         Method var8 = var5.getDeclaredMethod("methodType", Class.class, Class[].class);
         Method var9 = var6.getDeclaredMethod("findVirtual", Class.class, String.class, var5);
         Method var10 = var4.getDeclaredMethod("invokeWithArguments", Object[].class);
         Object var11 = var7.invoke((Object)null);
         Object var12 = var8.invoke((Object)null, Class.class, new Class[]{String.class, byte[].class, Integer.TYPE, Integer.TYPE});
         Object var13 = var9.invoke(var11, var2.getClass(), "defineClass", var12);
         return (Class)var10.invoke(var13, (Object)new Object[]{var2, var0, var1, 0, var1.length});
      } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | ClassNotFoundException var14) {
         throw new IllegalStateException(var14);
      }
   }

   private static Class<?> defineClassJLA(String var0, byte[] var1) {
      try {
         Class var2 = Class.forName("jdk.internal.reflect.ClassDefiner");
         Method var3 = var2.getDeclaredMethod("defineClass", String.class, byte[].class, Integer.TYPE, Integer.TYPE, ClassLoader.class);
         return (Class)var3.invoke((Object)null, var0, var1, 0, var1.length, XReflectASM.class.getClassLoader());
      } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | ClassNotFoundException var4) {
         throw new IllegalStateException(var4);
      }
   }

   private static String asmTypeToBinary(String var0) {
      return var0.replace('/', '.');
   }

   private static Class<?> methodHandleLoadClass(byte[] var0) {
      Lookup var1 = MethodHandles.lookup();

      try {
         MethodHandle var2 = var1.findVirtual(Lookup.class, "defineClass", MethodType.methodType(Class.class, byte[].class));
         return var2.invoke(var1, var0);
      } catch (Throwable var3) {
         throw new IllegalStateException(var3);
      }
   }

   private static Class<?> loadClass(String var0, byte[] var1) {
      try {
         ClassLoader var3 = ClassLoader.getSystemClassLoader();
         Class var4 = Class.forName("java.lang.ClassLoader");
         Method var5 = var4.getDeclaredMethod("defineClass", String.class, byte[].class, Integer.TYPE, Integer.TYPE);
         var5.setAccessible(true);

         Class var2;
         try {
            Object[] var6 = new Object[]{var0, var1, 0, var1.length};
            var2 = (Class)var5.invoke(var3, var6);
         } finally {
            var5.setAccessible(false);
         }

         return var2;
      } catch (Exception var11) {
         throw new IllegalStateException("Failed to load class " + var0 + " into the system class loader", var11);
      }
   }
}
