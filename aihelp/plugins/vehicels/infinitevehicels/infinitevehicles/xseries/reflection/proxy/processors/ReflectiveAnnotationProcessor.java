package me.PM2.infinitevehicles.xseries.reflection.proxy.processors;

import java.lang.annotation.Annotation;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Iterator;
import java.util.function.Function;
import me.PM2.infinitevehicles.xseries.reflection.ReflectiveHandle;
import me.PM2.infinitevehicles.xseries.reflection.StaticReflectiveHandle;
import me.PM2.infinitevehicles.xseries.reflection.XAccessFlag;
import me.PM2.infinitevehicles.xseries.reflection.XReflection;
import me.PM2.infinitevehicles.xseries.reflection.aggregate.VersionHandle;
import me.PM2.infinitevehicles.xseries.reflection.jvm.FieldMemberHandle;
import me.PM2.infinitevehicles.xseries.reflection.jvm.MemberHandle;
import me.PM2.infinitevehicles.xseries.reflection.jvm.MethodMemberHandle;
import me.PM2.infinitevehicles.xseries.reflection.jvm.NameableReflectiveHandle;
import me.PM2.infinitevehicles.xseries.reflection.jvm.classes.ClassHandle;
import me.PM2.infinitevehicles.xseries.reflection.jvm.classes.DynamicClassHandle;
import me.PM2.infinitevehicles.xseries.reflection.jvm.objects.ReflectedObject;
import me.PM2.infinitevehicles.xseries.reflection.minecraft.MinecraftClassHandle;
import me.PM2.infinitevehicles.xseries.reflection.proxy.ClassOverloadedMethods;
import me.PM2.infinitevehicles.xseries.reflection.proxy.OverloadedMethod;
import me.PM2.infinitevehicles.xseries.reflection.proxy.ReflectiveProxy;
import me.PM2.infinitevehicles.xseries.reflection.proxy.ReflectiveProxyObject;
import me.PM2.infinitevehicles.xseries.reflection.proxy.annotations.Constructor;
import me.PM2.infinitevehicles.xseries.reflection.proxy.annotations.Field;
import me.PM2.infinitevehicles.xseries.reflection.proxy.annotations.Final;
import me.PM2.infinitevehicles.xseries.reflection.proxy.annotations.Ignore;
import me.PM2.infinitevehicles.xseries.reflection.proxy.annotations.MappedMinecraftName;
import me.PM2.infinitevehicles.xseries.reflection.proxy.annotations.Private;
import me.PM2.infinitevehicles.xseries.reflection.proxy.annotations.Protected;
import me.PM2.infinitevehicles.xseries.reflection.proxy.annotations.Proxify;
import me.PM2.infinitevehicles.xseries.reflection.proxy.annotations.ReflectMinecraftPackage;
import me.PM2.infinitevehicles.xseries.reflection.proxy.annotations.ReflectName;
import me.PM2.infinitevehicles.xseries.reflection.proxy.annotations.Static;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
public final class ReflectiveAnnotationProcessor {
   private final Class<? extends ReflectiveProxyObject> interfaceClass;
   private ClassOverloadedMethods<ProxyMethodInfo> mapped;
   private OverloadedMethod.Builder<ProxyMethodInfo> mappedHandles;
   private Class<?> targetClass;

   public ReflectiveAnnotationProcessor(Class<? extends ReflectiveProxyObject> var1) {
      ReflectiveProxy.checkInterfaceClass(var1);
      this.interfaceClass = var1;
   }

   private void error(String var1) {
      this.error(var1, (Throwable)null);
   }

   private void error(String var1, Throwable var2) {
      throw new IllegalStateException(var1 + " (Proxified Interface: " + this.interfaceClass + ')', var2);
   }

   protected static boolean isAnnotationInherited(Class<?> var0, Method var1, Class<? extends Annotation> var2) {
      try {
         Method var3 = var0.getDeclaredMethod(var1.getName(), var1.getParameterTypes());
         if (var3.isAnnotationPresent(var2)) {
            return true;
         }
      } catch (NoSuchMethodException var7) {
      }

      Class[] var8 = var0.getInterfaces();
      int var4 = var8.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Class var6 = var8[var5];
         if (isAnnotationInherited(var6, var1, var2)) {
            return true;
         }
      }

      return false;
   }

   public void loadDependencies(Function<Class<?>, Boolean> var1) {
      Iterator var2 = this.mapped.mappings().values().iterator();

      while(var2.hasNext()) {
         OverloadedMethod var3 = (OverloadedMethod)var2.next();
         Iterator var4 = var3.getOverloads().iterator();

         while(var4.hasNext()) {
            ProxyMethodInfo var5 = (ProxyMethodInfo)var4.next();
            this.loadDependency(var5.rType, var1);
            MappedType[] var6 = var5.pTypes;
            int var7 = var6.length;

            for(int var8 = 0; var8 < var7; ++var8) {
               MappedType var9 = var6[var8];
               this.loadDependency(var9, var1);
            }
         }
      }

   }

   private void loadDependency(MappedType var1, Function<Class<?>, Boolean> var2) {
      if (ReflectiveProxyObject.class.isAssignableFrom(var1.synthetic) && var1.synthetic != this.interfaceClass && !(Boolean)var2.apply(var1.synthetic)) {
         XReflection.proxify(var1.synthetic);
      }

   }

   public void process(Function<ProxyMethodInfo, String> var1) {
      ClassHandle var2 = this.processTargetClass();
      Method[] var3 = this.interfaceClass.getMethods();
      this.mappedHandles = new OverloadedMethod.Builder(var1);
      Method[] var4 = var3;
      int var5 = var3.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         Method var7 = var4[var6];
         if (!isAnnotationInherited(this.interfaceClass, var7, Ignore.class)) {
            boolean var8 = var7.isAnnotationPresent(Static.class);
            boolean var9 = var7.isAnnotationPresent(Final.class);
            MappedType[] var11 = new MappedType[0];
            MappedType var10;
            Object var12;
            if (var7.isAnnotationPresent(Constructor.class)) {
               Class var13 = var7.getReturnType();
               if (var13 != this.targetClass && var13 != this.interfaceClass && var13 != Object.class) {
                  this.error("Method marked with @Constructor must return Object.class, " + this.targetClass + " or " + this.interfaceClass);
               }

               var10 = this.unwrap(var13);
               var11 = this.unwrap(var7.getParameterTypes());
               var12 = var2.constructor(MappedType.getRealTypes(var11));
               if (var8) {
                  this.error("Constructor cannot be static: " + var7);
               }

               if (var9) {
                  this.error("Constructor cannot be final: " + var7);
               }
            } else if (var7.isAnnotationPresent(Field.class)) {
               FieldMemberHandle var17 = var2.field();
               if (var7.getReturnType() == Void.TYPE) {
                  var17.setter();
                  if (var7.getParameterCount() != 1) {
                     this.error("Field setter method must have only one parameter: " + var7);
                  }

                  MappedType var14 = this.unwrap(var7.getParameterTypes()[0]);
                  var10 = new MappedType(Void.TYPE, Void.TYPE);
                  var11 = new MappedType[]{var14};
                  var17.returns(var14.real);
               } else {
                  var17.getter();
                  if (var7.getParameterCount() != 0) {
                     this.error("Field getter method must not have any parameters: " + var7);
                  }

                  var10 = this.unwrap(var7.getReturnType());
                  var17.returns(var10.real);
               }

               if (var8) {
                  var17.asStatic();
               }

               if (var9) {
                  var17.asFinal();
               }

               var12 = var17;
            } else {
               var10 = this.unwrap(var7.getReturnType());
               var11 = this.unwrap(var7.getParameterTypes());
               MethodMemberHandle var18 = var2.method().returns(var10.real).parameters(MappedType.getRealTypes(var11));
               if (var8) {
                  var18 = var18.asStatic();
               }

               if (var9) {
                  this.error("Declaring method as final has no effect: " + var7);
               }

               var12 = var18;
            }

            boolean var20 = false;
            if (var7.isAnnotationPresent(Private.class)) {
               var20 = true;
               ((MemberHandle)var12).getAccessFlags().add(XAccessFlag.PRIVATE);
            }

            if (var7.isAnnotationPresent(Protected.class)) {
               if (var20) {
                  this.error("Cannot have two visibility modifier private and protected for " + var7);
               }

               ((MemberHandle)var12).getAccessFlags().add(XAccessFlag.PRIVATE);
            }

            if (var12 instanceof NameableReflectiveHandle) {
               ((NameableReflectiveHandle)var12).named(var7.getName());
               this.reflectNames((NameableReflectiveHandle)var12, var7);
            }

            ReflectiveHandle var19 = ((MemberHandle)var12).cached();

            try {
               var19.reflect();
            } catch (ReflectiveOperationException var16) {
               this.error("Failed to map " + var7, var16);
            }

            ProxyMethodInfo var15 = new ProxyMethodInfo(var19, var7, var10, var11);
            this.mappedHandles.add(var15, var7.getName());
         }
      }

      this.mapped = this.mappedHandles.build();
   }

   @NotNull
   public ClassHandle processTargetClass() {
      Proxify var1 = (Proxify)this.interfaceClass.getAnnotation(Proxify.class);
      ReflectMinecraftPackage var2 = (ReflectMinecraftPackage)this.interfaceClass.getAnnotation(ReflectMinecraftPackage.class);
      if (var1 == null && var2 == null) {
         this.error("Proxy interface is not annotated with @Class or @ReflectMinecraftPackage");
      }

      if (var1 != null && var2 != null) {
         this.error("Proxy interface cannot contain both @Class or @ReflectMinecraftPackage");
      }

      Object var3;
      boolean var4;
      DynamicClassHandle var5;
      if (var1 != null) {
         if (var1.target() != Void.TYPE) {
            var3 = XReflection.of(var1.target());
            var4 = true;
         } else {
            var5 = XReflection.classHandle();
            var3 = var5;
            var5.inPackage(var1.packageName());
            var4 = var1.ignoreCurrentName();
         }
      } else {
         MinecraftClassHandle var6 = XReflection.ofMinecraft();
         var3 = var6;
         var6.inPackage(var2.type(), var2.packageName());
         var4 = var2.ignoreCurrentName();
      }

      if (var3 instanceof DynamicClassHandle) {
         var5 = (DynamicClassHandle)var3;
         if (!var4) {
            var5.named(this.interfaceClass.getSimpleName());
         }

         this.reflectNames(var5, this.interfaceClass);
      }

      this.targetClass = (Class)((ClassHandle)var3).unreflect();
      MappedType.LOOK_AHEAD.put(this.interfaceClass, this.targetClass);
      return (ClassHandle)var3;
   }

   public Class<?> getTargetClass() {
      return this.targetClass;
   }

   public ClassOverloadedMethods<ProxyMethodInfo> getMapped() {
      return this.mapped;
   }

   private void reflectNames(NameableReflectiveHandle var1, AnnotatedElement var2) {
      MappedMinecraftName[] var3 = (MappedMinecraftName[])var2.getDeclaredAnnotationsByType(MappedMinecraftName.class);
      ReflectName[] var4 = (ReflectName[])var2.getDeclaredAnnotationsByType(ReflectName.class);
      MappedMinecraftName[] var5 = var3;
      int var6 = var3.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         MappedMinecraftName var8 = var5[var7];
         this.reflectNames0(var1, var8.names());
      }

      this.reflectNames0(var1, var4);
   }

   private void reflectDefaults() {
      try {
         MethodHandle var1 = (MethodHandle)XReflection.of(MethodHandles.class).method("public static Lookup privateLookupIn(Class<?> targetClass, Lookup caller) throws IllegalAccessException").reflectOrNull();
         int var5;
         if (var1 == null) {
            java.lang.reflect.Constructor var2 = Lookup.class.getDeclaredConstructor(Class.class);
            var2.setAccessible(true);
            Lookup var3 = ((Lookup)var2.newInstance(this.interfaceClass)).in(this.interfaceClass);
            Method[] var4 = this.interfaceClass.getMethods();
            var5 = var4.length;

            for(int var6 = 0; var6 < var5; ++var6) {
               Method var7 = var4[var6];
               if (var7.isDefault()) {
                  this.handleDefaultMethod(var7, var3.unreflectSpecial(var7, this.interfaceClass));
               }
            }
         }

         Lookup var9 = var1.invokeExact(this.interfaceClass, MethodHandles.lookup());
         Method[] var10 = this.interfaceClass.getMethods();
         int var11 = var10.length;

         for(var5 = 0; var5 < var11; ++var5) {
            Method var12 = var10[var5];
            if (var12.isDefault()) {
               MethodHandle var13 = var9.findSpecial(this.interfaceClass, var12.getName(), MethodType.methodType(var12.getReturnType(), var12.getParameterTypes()), this.interfaceClass);
               this.handleDefaultMethod(var12, var13);
            }
         }
      } catch (Throwable var8) {
         var8.printStackTrace();
      }

   }

   private void handleDefaultMethod(Method var1, MethodHandle var2) {
      this.mappedHandles.add(new ProxyMethodInfo(new StaticReflectiveHandle(var2, ReflectedObject.of(var1)), var1, this.unwrap(var1.getReturnType()), this.unwrap(var1.getParameterTypes())), var1.getName());
   }

   private void reflectNames0(NameableReflectiveHandle var1, ReflectName[] var2) {
      if (var2.length != 0) {
         VersionHandle var3 = null;
         String[] var4 = null;
         int var5 = 0;
         ReflectName[] var6 = var2;
         int var7 = var2.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            ReflectName var9 = var6[var8];
            ++var5;
            if (var4 != null) {
               this.error("Cannot contain more tha one @ReflectName if no version is specified");
            }

            if (!var9.version().isEmpty()) {
               if (var5 == var2.length) {
                  this.error("Last @ReflectName should not contain version");
               }

               int[] var10 = Arrays.stream(var9.version().split("\\.")).mapToInt(Integer::parseInt).toArray();
               if (var3 == null) {
                  if (var10.length == 1) {
                     var3 = XReflection.v(var10[0], (Object)var9.value());
                  }

                  if (var10.length == 2) {
                     var3 = XReflection.v(var10[1], (Object)var9.value());
                  }

                  if (var10.length == 3) {
                     var3 = XReflection.v(var10[1], var10[2], (Object)var9.value());
                  }
               } else {
                  if (var10.length == 1) {
                     var3.v(var10[0], var9.value());
                  }

                  if (var10.length == 2) {
                     var3.v(var10[1], var9.value());
                  }

                  if (var10.length == 3) {
                     var3.v(var10[1], var10[2], (Object)var9.value());
                  }
               }
            } else if (var3 != null) {
               if (var5 != var2.length) {
                  this.error("One of @ReflectName doesn't contain a version.");
               } else {
                  var4 = (String[])var3.orElse((Object)var9.value());
               }
            } else {
               var4 = var9.value();
            }
         }

         var1.named(var4);
      }
   }

   private MappedType[] unwrap(Class<?>[] var1) {
      MappedType[] var2 = new MappedType[var1.length];

      for(int var3 = 0; var3 < var1.length; ++var3) {
         Class var4 = var1[var3];
         var2[var3] = this.unwrap(var4);
      }

      return var2;
   }

   private MappedType unwrap(Class<?> var1) {
      if (var1 != this.interfaceClass && var1 != ReflectiveProxyObject.class) {
         if (ReflectiveProxyObject.class.isAssignableFrom(var1)) {
            Class var2 = MappedType.getMappedTypeOrCreate(var1);
            return new MappedType(var1, var2);
         } else {
            return new MappedType(var1, var1);
         }
      } else {
         return new MappedType(this.interfaceClass, this.targetClass);
      }
   }
}
