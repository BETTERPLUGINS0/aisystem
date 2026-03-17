package me.PM2.infinitevehicles.xseries.reflection.proxy.processors;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Set;
import me.PM2.infinitevehicles.xseries.reflection.ReflectiveHandle;
import me.PM2.infinitevehicles.xseries.reflection.XAccessFlag;
import me.PM2.infinitevehicles.xseries.reflection.jvm.ConstructorMemberHandle;
import me.PM2.infinitevehicles.xseries.reflection.jvm.FieldMemberHandle;
import me.PM2.infinitevehicles.xseries.reflection.jvm.MethodMemberHandle;
import me.PM2.infinitevehicles.xseries.reflection.jvm.classes.ClassHandle;
import me.PM2.infinitevehicles.xseries.reflection.proxy.ReflectiveProxy;
import me.PM2.infinitevehicles.xseries.reflection.proxy.ReflectiveProxyObject;
import me.PM2.infinitevehicles.xseries.reflection.proxy.annotations.Ignore;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
public class ReflectiveHandleProxyProcessor {
   public static <T extends ReflectiveProxyObject> T proxify(@NotNull Class<T> var0, @NotNull ClassHandle var1, @NotNull ReflectiveHandle<?>... var2) {
      Set var3 = Collections.newSetFromMap(new IdentityHashMap());
      var3.addAll(Arrays.asList(var2));
      Method[] var4 = var0.getMethods();
      IdentityHashMap var5 = new IdentityHashMap(var4.length);
      Method[] var6 = var4;
      int var7 = var4.length;

      label77:
      for(int var8 = 0; var8 < var7; ++var8) {
         Method var9 = var6[var8];
         if (!ReflectiveAnnotationProcessor.isAnnotationInherited(var0, var9, Ignore.class)) {
            String var10 = var9.getName();
            Iterator var11 = var3.iterator();

            FieldMemberHandle var22;
            do {
               while(true) {
                  if (!var11.hasNext()) {
                     continue label77;
                  }

                  ReflectiveHandle var12 = (ReflectiveHandle)var11.next();
                  if (var12 instanceof FieldMemberHandle) {
                     var22 = (FieldMemberHandle)var12;
                     break;
                  }

                  if (var12 instanceof MethodMemberHandle) {
                     MethodMemberHandle var21 = (MethodMemberHandle)var12;
                     if (var21.getPossibleNames().stream().anyMatch((var1x) -> {
                        return var1x.equals(var10);
                     })) {
                        var11.remove();
                        var5.put(var9, new ReflectiveProxy.ProxifiedObject((MethodHandle)var21.unreflect(), (ProxyMethodInfo)null, var21.getAccessFlags().contains(XAccessFlag.STATIC), false, (ReflectiveProxy)null, (ReflectiveProxy[])null));
                        continue label77;
                     }
                  } else if (var12 instanceof ConstructorMemberHandle && var9.getReturnType() == var0 && var10.equals(var0.getName())) {
                     ConstructorMemberHandle var13 = (ConstructorMemberHandle)var12;
                     if (var13.getParameterTypes().length == var9.getParameterCount()) {
                        Constructor var14;
                        try {
                           var14 = var13.reflectJvm();
                        } catch (ReflectiveOperationException var20) {
                           throw new IllegalStateException("Failed to map " + var9, var20);
                        }

                        int var15 = 0;
                        Parameter[] var16 = var9.getParameters();
                        int var17 = var16.length;

                        for(int var18 = 0; var18 < var17; ++var18) {
                           Parameter var19 = var16[var18];
                           if (var14.getParameters()[var15].getType() != var19.getType()) {
                              var15 = -1;
                              break;
                           }

                           ++var15;
                        }

                        if (var15 != -1) {
                           var11.remove();
                           var5.put(var9, new ReflectiveProxy.ProxifiedObject((MethodHandle)var13.unreflect(), (ProxyMethodInfo)null, false, true, (ReflectiveProxy)null, (ReflectiveProxy[])null));
                           continue label77;
                        }
                     }
                  }
               }
            } while(!var22.getPossibleNames().stream().anyMatch((var1x) -> {
               return var1x.equals(var10);
            }));

            var11.remove();
            var5.put(var9, new ReflectiveProxy.ProxifiedObject((MethodHandle)var22.unreflect(), (ProxyMethodInfo)null, var22.getAccessFlags().contains(XAccessFlag.STATIC), false, (ReflectiveProxy)null, (ReflectiveProxy[])null));
         }
      }

      return null;
   }
}
