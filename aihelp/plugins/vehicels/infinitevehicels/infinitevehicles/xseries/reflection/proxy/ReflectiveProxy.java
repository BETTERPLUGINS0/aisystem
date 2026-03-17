package me.PM2.infinitevehicles.xseries.reflection.proxy;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import me.PM2.infinitevehicles.xseries.reflection.XAccessFlag;
import me.PM2.infinitevehicles.xseries.reflection.jvm.objects.ReflectedObject;
import me.PM2.infinitevehicles.xseries.reflection.proxy.processors.MappedType;
import me.PM2.infinitevehicles.xseries.reflection.proxy.processors.ProxyMethodInfo;
import me.PM2.infinitevehicles.xseries.reflection.proxy.processors.ReflectiveAnnotationProcessor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
public final class ReflectiveProxy<T extends ReflectiveProxyObject> implements InvocationHandler {
   private static final Map<Class<?>, ReflectiveProxy<?>> PROXIFIED_CLASS_LOADER0 = new IdentityHashMap();
   private static final ClassLoader CLASS_LOADER = ReflectiveProxy.class.getClassLoader();
   private final Class<?> targetClass;
   private final Class<T> proxyClass;
   private T proxy;
   private final Object instance;
   private final Map<Method, ReflectiveProxy.ProxifiedObject> handles;
   private final ClassOverloadedMethods<ReflectiveProxy.ProxifiedObject> nameMapped;

   public static <T extends ReflectiveProxyObject> ReflectiveProxy<T> proxify(Class<T> var0) {
      ReflectiveProxy var1 = (ReflectiveProxy)PROXIFIED_CLASS_LOADER0.get(var0);
      if (var1 != null) {
         return var1;
      } else {
         ReflectiveAnnotationProcessor var2 = new ReflectiveAnnotationProcessor(var0);
         var2.process(ReflectiveProxy::descriptorProcessor);
         Set var3 = var2.getMapped().mappings().entrySet();
         IdentityHashMap var4 = new IdentityHashMap(var3.size());
         OverloadedMethod.Builder var5 = new OverloadedMethod.Builder(ReflectiveProxy::descriptorProcessor);
         ReflectiveProxy var6 = new ReflectiveProxy(var2.getTargetClass(), var0, (Object)null, var4, var5.build());
         PROXIFIED_CLASS_LOADER0.put(var0, var6);
         Iterator var7 = var3.iterator();

         while(var7.hasNext()) {
            Entry var8 = (Entry)var7.next();
            Iterator var9 = ((OverloadedMethod)var8.getValue()).getOverloads().iterator();

            while(var9.hasNext()) {
               ProxyMethodInfo var10 = (ProxyMethodInfo)var9.next();
               ReflectedObject var11 = (ReflectedObject)var10.handle.jvm().unreflect();
               MethodHandle var12 = (MethodHandle)var10.handle.unreflect();
               var12 = createDynamicProxy((Object)null, var12);
               ReflectiveProxy.ProxifiedObject var13 = new ReflectiveProxy.ProxifiedObject(var12, var10, var11.accessFlags().contains(XAccessFlag.STATIC), var11.type() == ReflectedObject.Type.CONSTRUCTOR, var10.rType.isDifferent() ? proxify(var10.rType.synthetic) : null, Arrays.stream(var10.pTypes).anyMatch(MappedType::isDifferent) ? (ReflectiveProxy[])Arrays.stream(var10.pTypes).map((var0x) -> {
                  return var0x.isDifferent() ? proxify(var0x.synthetic) : null;
               }).toArray((var0x) -> {
                  return new ReflectiveProxy[var0x];
               }) : null);
               var4.put(var10.interfaceMethod, var13);
               var5.add(var13, (String)var8.getKey());
            }
         }

         var5.build(var6.nameMapped.mappings());
         var6.proxy = var6.createProxy();
         Method[] var16 = var6.proxy.getClass().getDeclaredMethods();
         int var18 = var16.length;

         int var19;
         for(var19 = 0; var19 < var18; ++var19) {
            Method var20 = var16[var19];
            ReflectiveProxy.ProxifiedObject var22 = (ReflectiveProxy.ProxifiedObject)var6.nameMapped.get(var20.getName(), () -> {
               return descriptorProcessor(var20);
            }, true);
            if (var22 != null) {
               var4.put(var20, var22);
            }
         }

         Class[] var17 = var6.proxy.getClass().getInterfaces();
         var18 = var17.length;

         for(var19 = 0; var19 < var18; ++var19) {
            Class var21 = var17[var19];
            Method[] var23 = var21.getDeclaredMethods();
            int var24 = var23.length;

            for(int var25 = 0; var25 < var24; ++var25) {
               Method var14 = var23[var25];
               ReflectiveProxy.ProxifiedObject var15 = (ReflectiveProxy.ProxifiedObject)var6.nameMapped.get(var14.getName(), () -> {
                  return descriptorProcessor(var14);
               }, true);
               if (var15 != null) {
                  var4.put(var14, var15);
               }
            }
         }

         return var6;
      }
   }

   private static MethodHandle createDynamicProxy(@Nullable Object var0, MethodHandle var1) {
      int var2 = var1.type().parameterCount();
      int var3 = var0 != null ? 1 : 0;
      if (var0 != null) {
         var1 = var1.bindTo(var0);
      }

      return var2 == var3 ? var1.asType(MethodType.methodType(Object.class)) : var1.asSpreader(Object[].class, var2 - var3).asType(MethodType.methodType(Object.class, Object[].class));
   }

   private static String descriptorProcessor(ReflectiveProxy.ProxifiedObject var0) {
      return OverloadedMethod.getParameterDescriptor(MappedType.getRealTypes(var0.proxyMethodInfo.pTypes));
   }

   private static String descriptorProcessor(ProxyMethodInfo var0) {
      return OverloadedMethod.getParameterDescriptor(MappedType.getRealTypes(var0.pTypes));
   }

   private static String descriptorProcessor(Method var0) {
      return OverloadedMethod.getParameterDescriptor(var0.getParameterTypes());
   }

   private ReflectiveProxy(Class<?> var1, Class<T> var2, Object var3, Map<Method, ReflectiveProxy.ProxifiedObject> var4, ClassOverloadedMethods<ReflectiveProxy.ProxifiedObject> var5) {
      this.targetClass = var1;
      this.proxyClass = var2;
      this.instance = var3;
      this.handles = var4;
      this.nameMapped = var5;
   }

   public static void checkInterfaceClass(Class<?> var0) {
      Objects.requireNonNull(var0, "Interface class is null");
      if (!var0.isInterface()) {
         throw new IllegalArgumentException("Cannot proxify non-interface class: " + var0);
      } else if (!ReflectiveProxyObject.class.isAssignableFrom(var0)) {
         throw new IllegalArgumentException("The provided interface class must extend ReflectiveProxyObject interface");
      }
   }

   @Internal
   @NotNull
   public T createProxy() {
      return (ReflectiveProxyObject)Proxy.newProxyInstance(CLASS_LOADER, new Class[]{this.proxyClass}, this);
   }

   @NotNull
   public T proxy() {
      return this.proxy;
   }

   @Nullable
   public Object instance() {
      return this.instance;
   }

   @NotNull
   public T bindTo(@NotNull Object var1) {
      if (this.instance != null) {
         throw new IllegalStateException("This proxy object already has an instance bound to it: " + this);
      } else {
         Objects.requireNonNull(var1, "Instance cannot be null");
         if (!this.targetClass.isAssignableFrom(var1.getClass())) {
            throw new IllegalArgumentException("The given instance doesn't match the target class: " + var1 + " -> " + this);
         } else {
            IdentityHashMap var2 = new IdentityHashMap(this.handles.size());
            IdentityHashMap var3 = new IdentityHashMap(this.nameMapped.mappings().size());
            OverloadedMethod.Builder var4 = new OverloadedMethod.Builder(ReflectiveProxy::descriptorProcessor);
            Iterator var5 = this.nameMapped.mappings().entrySet().iterator();

            Entry var6;
            ReflectiveProxy.ProxifiedObject var8;
            while(var5.hasNext()) {
               var6 = (Entry)var5.next();
               Iterator var7 = ((OverloadedMethod)var6.getValue()).getOverloads().iterator();

               while(var7.hasNext()) {
                  var8 = (ReflectiveProxy.ProxifiedObject)var7.next();
                  ReflectiveProxy.ProxifiedObject var9 = var8;
                  ReflectiveProxy.ProxifiedObject var10 = (ReflectiveProxy.ProxifiedObject)var3.get(var8);
                  if (var10 == null) {
                     if (var8.isStatic || var8.isConstructor) {
                        var4.add(var8, (String)var6.getKey());
                        continue;
                     }

                     MethodHandle var11;
                     try {
                        var11 = (MethodHandle)var9.proxyMethodInfo.handle.unreflect();
                        if (var11.type().parameterCount() == 0) {
                           throw new IllegalStateException("Non-static, non-constructor with 0 arguments found: " + var11);
                        }

                        var11 = createDynamicProxy(var1, var11);
                     } catch (Exception var13) {
                        throw new IllegalStateException("Failed to bind " + var1 + " to " + (String)var6.getKey() + " -> " + var8.handle + " (static=" + var8.isStatic + ", constructor=" + var8.isConstructor + ')', var13);
                     }

                     var10 = new ReflectiveProxy.ProxifiedObject(var11, var8.proxyMethodInfo, var8.isStatic, var8.isConstructor, var8.rType, var8.pTypes);
                     var3.put(var8, var10);
                  }

                  var4.add(var10, (String)var6.getKey());
               }
            }

            var5 = this.handles.entrySet().iterator();

            while(true) {
               while(var5.hasNext()) {
                  var6 = (Entry)var5.next();
                  ReflectiveProxy.ProxifiedObject var15 = (ReflectiveProxy.ProxifiedObject)var6.getValue();
                  if (!var15.isStatic && !var15.isConstructor) {
                     var8 = (ReflectiveProxy.ProxifiedObject)var3.get(var15);
                     if (var8 == null) {
                        throw new IllegalStateException("Cannot find bound method for " + var6.getKey() + " (" + var15 + "::" + var15.hashCode() + ") in " + var4.build() + " - " + var3.entrySet().stream().map((var0) -> {
                           return var0.getKey() + "::" + var0.hashCode();
                        }).collect(Collectors.toList()));
                     }

                     var2.put((Method)var6.getKey(), var8);
                  } else {
                     var2.put((Method)var6.getKey(), var15);
                  }
               }

               ReflectiveProxy var14 = new ReflectiveProxy(this.targetClass, this.proxyClass, var1, var2, var4.build());
               return var14.createProxy();
            }
         }
      }
   }

   private static String getMethodList(Class<?> var0, boolean var1) {
      return ((List)Arrays.stream(var1 ? var0.getDeclaredMethods() : var0.getMethods()).map((var0x) -> {
         return var0x.getName() + "::" + System.identityHashCode(var0x);
      }).collect(Collectors.toList())).toString();
   }

   public Object invoke(Object var1, Method var2, @Nullable Object[] var3) {
      int var4 = var2.getParameterCount();
      String var5 = var2.getName();
      byte var7;
      if (var4 == 0) {
         var7 = -1;
         switch(var5.hashCode()) {
         case -1776922004:
            if (var5.equals("toString")) {
               var7 = 1;
            }
            break;
         case -1039689911:
            if (var5.equals("notify")) {
               var7 = 3;
            }
            break;
         case 3641717:
            if (var5.equals("wait")) {
               var7 = 5;
            }
            break;
         case 147696667:
            if (var5.equals("hashCode")) {
               var7 = 2;
            }
            break;
         case 555127957:
            if (var5.equals("instance")) {
               var7 = 0;
            }
            break;
         case 1544020273:
            if (var5.equals("getTargetClass")) {
               var7 = 6;
            }
            break;
         case 1902066072:
            if (var5.equals("notifyAll")) {
               var7 = 4;
            }
         }

         switch(var7) {
         case 0:
            return this.instance;
         case 1:
            return this.instance == null ? this.proxyClass.toString() : this.instance.toString();
         case 2:
            return this.instance == null ? this.proxyClass.hashCode() : this.instance.hashCode();
         case 3:
            if (this.instance == null) {
               this.proxyClass.notify();
            } else {
               this.instance.notify();
            }

            return null;
         case 4:
            if (this.instance == null) {
               this.proxyClass.notifyAll();
            } else {
               this.instance.notifyAll();
            }

            return null;
         case 5:
            if (this.instance == null) {
               this.proxyClass.wait();
            } else {
               this.instance.wait();
            }

            return null;
         case 6:
            return this.targetClass;
         }
      } else if (var4 == 1) {
         var7 = -1;
         switch(var5.hashCode()) {
         case -1388964968:
            if (var5.equals("bindTo")) {
               var7 = 0;
            }
            break;
         case -1295482945:
            if (var5.equals("equals")) {
               var7 = 2;
            }
            break;
         case -238142497:
            if (var5.equals("isInstance")) {
               var7 = 1;
            }
            break;
         case 3641717:
            if (var5.equals("wait")) {
               var7 = 3;
            }
         }

         switch(var7) {
         case 0:
            return this.bindTo(var3[0]);
         case 1:
            return this.targetClass.isInstance(var3[0]);
         case 2:
            return this.instance == null ? this.proxyClass == var3[0] : this.instance.equals(var3[0]);
         case 3:
            if (this.instance == null) {
               this.proxyClass.wait((Long)var3[0]);
            } else {
               this.instance.wait((Long)var3[0]);
            }

            return null;
         }
      } else if (var4 == 2 && var5.equals("wait")) {
         if (this.instance == null) {
            this.proxyClass.wait((Long)var3[0], (Integer)var3[1]);
         } else {
            this.instance.wait((Long)var3[0], (Integer)var3[1]);
         }

         return null;
      }

      ReflectiveProxy.ProxifiedObject var9 = (ReflectiveProxy.ProxifiedObject)this.handles.get(var2);
      if (var9 == null) {
         var9 = (ReflectiveProxy.ProxifiedObject)this.nameMapped.get(var2.getName(), () -> {
            return descriptorProcessor(var2);
         });
         this.handles.put(var2, var9);
      }

      if (!var9.isStatic && !var9.isConstructor && this.instance == null) {
         throw new IllegalStateException("Cannot invoke non-static non-constructor member handle with when no instance is set");
      } else if (var9.isConstructor && this.instance != null) {
         throw new IllegalStateException("Cannot invoke constructor twice");
      } else {
         if (var9.pTypes != null && var3 != null) {
            for(int var6 = 0; var6 < var3.length; ++var6) {
               Object var11 = var3[var6];
               if (var11 instanceof ReflectiveProxyObject) {
                  var3[var6] = ((ReflectiveProxyObject)var11).instance();
               }
            }
         }

         Object var10;
         try {
            if (var3 == null) {
               var10 = var9.handle.invokeExact();
            } else {
               var10 = var9.handle.invoke(var3);
            }
         } catch (Throwable var8) {
            throw new IllegalStateException("Failed to execute " + var2 + " -> " + var9.handle + " with args " + (var3 == null ? "null" : Arrays.stream(var3).map((var0) -> {
               return var0 == null ? "null" : var0 + " (" + var0.getClass().getSimpleName() + ')';
            })), var8);
         }

         if (var9.rType != null) {
            var10 = var9.rType.bindTo(var10);
         }

         return var10;
      }
   }

   public String toString() {
      return "ReflectiveProxy(proxyClass=" + this.proxyClass + ", proxy=" + this.proxy + ", instance=" + this.instance + ", nameMapped=" + this.nameMapped + ')';
   }

   public static final class ProxifiedObject {
      private final MethodHandle handle;
      private final ProxyMethodInfo proxyMethodInfo;
      private final boolean isStatic;
      private final boolean isConstructor;
      private final ReflectiveProxy<?> rType;
      private final ReflectiveProxy<?>[] pTypes;

      public ProxifiedObject(MethodHandle var1, ProxyMethodInfo var2, boolean var3, boolean var4, ReflectiveProxy<?> var5, ReflectiveProxy<?>[] var6) {
         this.handle = var1;
         this.proxyMethodInfo = var2;
         this.isStatic = var3;
         this.isConstructor = var4;
         this.rType = var5;
         this.pTypes = var6;
      }

      public String toString() {
         return this.getClass().getSimpleName() + '(' + this.proxyMethodInfo.interfaceMethod + ')';
      }
   }
}
