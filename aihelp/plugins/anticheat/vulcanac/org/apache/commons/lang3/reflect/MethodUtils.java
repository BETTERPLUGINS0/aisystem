package org.apache.commons.lang3.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.Validate;

public class MethodUtils {
   private static final Comparator<Method> METHOD_BY_SIGNATURE = Comparator.comparing(Method::toString);

   public static Object invokeMethod(Object var0, String var1) {
      return invokeMethod(var0, var1, ArrayUtils.EMPTY_OBJECT_ARRAY, (Class[])null);
   }

   public static Object invokeMethod(Object var0, boolean var1, String var2) {
      return invokeMethod(var0, var1, var2, ArrayUtils.EMPTY_OBJECT_ARRAY, (Class[])null);
   }

   public static Object invokeMethod(Object var0, String var1, Object... var2) {
      var2 = ArrayUtils.nullToEmpty(var2);
      Class[] var3 = ClassUtils.toClass(var2);
      return invokeMethod(var0, var1, var2, var3);
   }

   public static Object invokeMethod(Object var0, boolean var1, String var2, Object... var3) {
      var3 = ArrayUtils.nullToEmpty(var3);
      Class[] var4 = ClassUtils.toClass(var3);
      return invokeMethod(var0, var1, var2, var3, var4);
   }

   public static Object invokeMethod(Object var0, boolean var1, String var2, Object[] var3, Class<?>[] var4) {
      var4 = ArrayUtils.nullToEmpty(var4);
      var3 = ArrayUtils.nullToEmpty(var3);
      Method var6 = null;
      String var5;
      if (var1) {
         var5 = "No such method: ";
         var6 = getMatchingMethod(var0.getClass(), var2, var4);
         if (var6 != null && !var6.isAccessible()) {
            var6.setAccessible(true);
         }
      } else {
         var5 = "No such accessible method: ";
         var6 = getMatchingAccessibleMethod(var0.getClass(), var2, var4);
      }

      if (var6 == null) {
         throw new NoSuchMethodException(var5 + var2 + "() on object: " + var0.getClass().getName());
      } else {
         var3 = toVarArgs(var6, var3);
         return var6.invoke(var0, var3);
      }
   }

   public static Object invokeMethod(Object var0, String var1, Object[] var2, Class<?>[] var3) {
      return invokeMethod(var0, false, var1, var2, var3);
   }

   public static Object invokeExactMethod(Object var0, String var1) {
      return invokeExactMethod(var0, var1, ArrayUtils.EMPTY_OBJECT_ARRAY, (Class[])null);
   }

   public static Object invokeExactMethod(Object var0, String var1, Object... var2) {
      var2 = ArrayUtils.nullToEmpty(var2);
      Class[] var3 = ClassUtils.toClass(var2);
      return invokeExactMethod(var0, var1, var2, var3);
   }

   public static Object invokeExactMethod(Object var0, String var1, Object[] var2, Class<?>[] var3) {
      var2 = ArrayUtils.nullToEmpty(var2);
      var3 = ArrayUtils.nullToEmpty(var3);
      Method var4 = getAccessibleMethod(var0.getClass(), var1, var3);
      if (var4 == null) {
         throw new NoSuchMethodException("No such accessible method: " + var1 + "() on object: " + var0.getClass().getName());
      } else {
         return var4.invoke(var0, var2);
      }
   }

   public static Object invokeExactStaticMethod(Class<?> var0, String var1, Object[] var2, Class<?>[] var3) {
      var2 = ArrayUtils.nullToEmpty(var2);
      var3 = ArrayUtils.nullToEmpty(var3);
      Method var4 = getAccessibleMethod(var0, var1, var3);
      if (var4 == null) {
         throw new NoSuchMethodException("No such accessible method: " + var1 + "() on class: " + var0.getName());
      } else {
         return var4.invoke((Object)null, var2);
      }
   }

   public static Object invokeStaticMethod(Class<?> var0, String var1, Object... var2) {
      var2 = ArrayUtils.nullToEmpty(var2);
      Class[] var3 = ClassUtils.toClass(var2);
      return invokeStaticMethod(var0, var1, var2, var3);
   }

   public static Object invokeStaticMethod(Class<?> var0, String var1, Object[] var2, Class<?>[] var3) {
      var2 = ArrayUtils.nullToEmpty(var2);
      var3 = ArrayUtils.nullToEmpty(var3);
      Method var4 = getMatchingAccessibleMethod(var0, var1, var3);
      if (var4 == null) {
         throw new NoSuchMethodException("No such accessible method: " + var1 + "() on class: " + var0.getName());
      } else {
         var2 = toVarArgs(var4, var2);
         return var4.invoke((Object)null, var2);
      }
   }

   private static Object[] toVarArgs(Method var0, Object[] var1) {
      if (var0.isVarArgs()) {
         Class[] var2 = var0.getParameterTypes();
         var1 = getVarArgs(var1, var2);
      }

      return var1;
   }

   static Object[] getVarArgs(Object[] var0, Class<?>[] var1) {
      if (var0.length != var1.length || var0[var0.length - 1] != null && !var0[var0.length - 1].getClass().equals(var1[var1.length - 1])) {
         Object[] var2 = new Object[var1.length];
         System.arraycopy(var0, 0, var2, 0, var1.length - 1);
         Class var3 = var1[var1.length - 1].getComponentType();
         int var4 = var0.length - var1.length + 1;
         Object var5 = Array.newInstance(ClassUtils.primitiveToWrapper(var3), var4);
         System.arraycopy(var0, var1.length - 1, var5, 0, var4);
         if (var3.isPrimitive()) {
            var5 = ArrayUtils.toPrimitive(var5);
         }

         var2[var1.length - 1] = var5;
         return var2;
      } else {
         return var0;
      }
   }

   public static Object invokeExactStaticMethod(Class<?> var0, String var1, Object... var2) {
      var2 = ArrayUtils.nullToEmpty(var2);
      Class[] var3 = ClassUtils.toClass(var2);
      return invokeExactStaticMethod(var0, var1, var2, var3);
   }

   public static Method getAccessibleMethod(Class<?> var0, String var1, Class<?>... var2) {
      try {
         return getAccessibleMethod(var0.getMethod(var1, var2));
      } catch (NoSuchMethodException var4) {
         return null;
      }
   }

   public static Method getAccessibleMethod(Method var0) {
      if (!MemberUtils.isAccessible(var0)) {
         return null;
      } else {
         Class var1 = var0.getDeclaringClass();
         if (Modifier.isPublic(var1.getModifiers())) {
            return var0;
         } else {
            String var2 = var0.getName();
            Class[] var3 = var0.getParameterTypes();
            var0 = getAccessibleMethodFromInterfaceNest(var1, var2, var3);
            if (var0 == null) {
               var0 = getAccessibleMethodFromSuperclass(var1, var2, var3);
            }

            return var0;
         }
      }
   }

   private static Method getAccessibleMethodFromSuperclass(Class<?> var0, String var1, Class<?>... var2) {
      for(Class var3 = var0.getSuperclass(); var3 != null; var3 = var3.getSuperclass()) {
         if (Modifier.isPublic(var3.getModifiers())) {
            try {
               return var3.getMethod(var1, var2);
            } catch (NoSuchMethodException var5) {
               return null;
            }
         }
      }

      return null;
   }

   private static Method getAccessibleMethodFromInterfaceNest(Class<?> var0, String var1, Class<?>... var2) {
      while(var0 != null) {
         Class[] var3 = var0.getInterfaces();
         Class[] var4 = var3;
         int var5 = var3.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            Class var7 = var4[var6];
            if (Modifier.isPublic(var7.getModifiers())) {
               try {
                  return var7.getDeclaredMethod(var1, var2);
               } catch (NoSuchMethodException var9) {
                  Method var8 = getAccessibleMethodFromInterfaceNest(var7, var1, var2);
                  if (var8 != null) {
                     return var8;
                  }
               }
            }
         }

         var0 = var0.getSuperclass();
      }

      return null;
   }

   public static Method getMatchingAccessibleMethod(Class<?> var0, String var1, Class<?>... var2) {
      try {
         Method var13 = var0.getMethod(var1, var2);
         MemberUtils.setAccessibleWorkaround(var13);
         return var13;
      } catch (NoSuchMethodException var12) {
         Method[] var3 = var0.getMethods();
         ArrayList var4 = new ArrayList();
         Method[] var5 = var3;
         int var6 = var3.length;

         Method var8;
         for(int var7 = 0; var7 < var6; ++var7) {
            var8 = var5[var7];
            if (var8.getName().equals(var1) && MemberUtils.isMatchingMethod(var8, var2)) {
               var4.add(var8);
            }
         }

         var4.sort(METHOD_BY_SIGNATURE);
         Method var14 = null;
         Iterator var15 = var4.iterator();

         while(true) {
            do {
               do {
                  if (!var15.hasNext()) {
                     if (var14 != null) {
                        MemberUtils.setAccessibleWorkaround(var14);
                     }

                     if (var14 != null && var14.isVarArgs() && var14.getParameterTypes().length > 0 && var2.length > 0) {
                        Class[] var16 = var14.getParameterTypes();
                        Class var18 = var16[var16.length - 1].getComponentType();
                        String var19 = ClassUtils.primitiveToWrapper(var18).getName();
                        Class var9 = var2[var2.length - 1];
                        String var10 = var9 == null ? null : var9.getName();
                        String var11 = var9 == null ? null : var9.getSuperclass().getName();
                        if (var10 != null && var11 != null && !var19.equals(var10) && !var19.equals(var11)) {
                           return null;
                        }
                     }

                     return var14;
                  }

                  Method var17 = (Method)var15.next();
                  var8 = getAccessibleMethod(var17);
               } while(var8 == null);
            } while(var14 != null && MemberUtils.compareMethodFit(var8, var14, var2) >= 0);

            var14 = var8;
         }
      }
   }

   public static Method getMatchingMethod(Class<?> var0, String var1, Class<?>... var2) {
      Validate.notNull(var0, "cls");
      Validate.notEmpty((CharSequence)var1, "methodName");
      List var3 = (List)Arrays.stream(var0.getDeclaredMethods()).filter((var1x) -> {
         return var1x.getName().equals(var1);
      }).collect(Collectors.toList());
      ClassUtils.getAllSuperclasses(var0).stream().map(Class::getDeclaredMethods).flatMap(Arrays::stream).filter((var1x) -> {
         return var1x.getName().equals(var1);
      }).forEach(var3::add);
      Iterator var4 = var3.iterator();

      Method var5;
      do {
         if (!var4.hasNext()) {
            TreeMap var6 = new TreeMap();
            var3.stream().filter((var1x) -> {
               return ClassUtils.isAssignable(var2, var1x.getParameterTypes(), true);
            }).forEach((var2x) -> {
               int var3 = distance(var2, var2x.getParameterTypes());
               List var4 = (List)var6.computeIfAbsent(var3, (var0) -> {
                  return new ArrayList();
               });
               var4.add(var2x);
            });
            if (var6.isEmpty()) {
               return null;
            }

            List var7 = (List)var6.values().iterator().next();
            if (var7.size() == 1) {
               return (Method)var7.get(0);
            }

            throw new IllegalStateException(String.format("Found multiple candidates for method %s on class %s : %s", var1 + (String)Arrays.stream(var2).map(String::valueOf).collect(Collectors.joining(",", "(", ")")), var0.getName(), var7.stream().map(Method::toString).collect(Collectors.joining(",", "[", "]"))));
         }

         var5 = (Method)var4.next();
      } while(!Arrays.deepEquals(var5.getParameterTypes(), var2));

      return var5;
   }

   private static int distance(Class<?>[] var0, Class<?>[] var1) {
      int var2 = 0;
      if (!ClassUtils.isAssignable(var0, var1, true)) {
         return -1;
      } else {
         for(int var3 = 0; var3 < var0.length; ++var3) {
            Class var4 = var0[var3];
            Class var5 = var1[var3];
            if (var4 != null && !var4.equals(var5)) {
               if (ClassUtils.isAssignable(var4, var5, true) && !ClassUtils.isAssignable(var4, var5, false)) {
                  ++var2;
               } else {
                  var2 += 2;
               }
            }
         }

         return var2;
      }
   }

   public static Set<Method> getOverrideHierarchy(Method var0, ClassUtils.Interfaces var1) {
      Validate.notNull(var0);
      LinkedHashSet var2 = new LinkedHashSet();
      var2.add(var0);
      Class[] var3 = var0.getParameterTypes();
      Class var4 = var0.getDeclaringClass();
      Iterator var5 = ClassUtils.hierarchy(var4, var1).iterator();
      var5.next();

      while(true) {
         label32:
         while(true) {
            Method var7;
            do {
               if (!var5.hasNext()) {
                  return var2;
               }

               Class var6 = (Class)var5.next();
               var7 = getMatchingAccessibleMethod(var6, var0.getName(), var3);
            } while(var7 == null);

            if (Arrays.equals(var7.getParameterTypes(), var3)) {
               var2.add(var7);
            } else {
               Map var8 = TypeUtils.getTypeArguments(var4, var7.getDeclaringClass());

               for(int var9 = 0; var9 < var3.length; ++var9) {
                  Type var10 = TypeUtils.unrollVariables(var8, var0.getGenericParameterTypes()[var9]);
                  Type var11 = TypeUtils.unrollVariables(var8, var7.getGenericParameterTypes()[var9]);
                  if (!TypeUtils.equals(var10, var11)) {
                     continue label32;
                  }
               }

               var2.add(var7);
            }
         }
      }
   }

   public static Method[] getMethodsWithAnnotation(Class<?> var0, Class<? extends Annotation> var1) {
      return getMethodsWithAnnotation(var0, var1, false, false);
   }

   public static List<Method> getMethodsListWithAnnotation(Class<?> var0, Class<? extends Annotation> var1) {
      return getMethodsListWithAnnotation(var0, var1, false, false);
   }

   public static Method[] getMethodsWithAnnotation(Class<?> var0, Class<? extends Annotation> var1, boolean var2, boolean var3) {
      List var4 = getMethodsListWithAnnotation(var0, var1, var2, var3);
      return (Method[])var4.toArray(ArrayUtils.EMPTY_METHOD_ARRAY);
   }

   public static List<Method> getMethodsListWithAnnotation(Class<?> var0, Class<? extends Annotation> var1, boolean var2, boolean var3) {
      Validate.notNull(var0, "cls");
      Validate.notNull(var1, "annotationCls");
      Object var4 = var2 ? getAllSuperclassesAndInterfaces(var0) : new ArrayList();
      ((List)var4).add(0, var0);
      ArrayList var5 = new ArrayList();
      Iterator var6 = ((List)var4).iterator();

      while(var6.hasNext()) {
         Class var7 = (Class)var6.next();
         Method[] var8 = var3 ? var7.getDeclaredMethods() : var7.getMethods();
         Method[] var9 = var8;
         int var10 = var8.length;

         for(int var11 = 0; var11 < var10; ++var11) {
            Method var12 = var9[var11];
            if (var12.getAnnotation(var1) != null) {
               var5.add(var12);
            }
         }
      }

      return var5;
   }

   public static <A extends Annotation> A getAnnotation(Method var0, Class<A> var1, boolean var2, boolean var3) {
      Validate.notNull(var0, "method");
      Validate.notNull(var1, "annotationCls");
      if (!var3 && !MemberUtils.isAccessible(var0)) {
         return null;
      } else {
         Annotation var4 = var0.getAnnotation(var1);
         if (var4 == null && var2) {
            Class var5 = var0.getDeclaringClass();
            List var6 = getAllSuperclassesAndInterfaces(var5);
            Iterator var7 = var6.iterator();

            while(var7.hasNext()) {
               Class var8 = (Class)var7.next();
               Method var9 = var3 ? getMatchingMethod(var8, var0.getName(), var0.getParameterTypes()) : getMatchingAccessibleMethod(var8, var0.getName(), var0.getParameterTypes());
               if (var9 != null) {
                  var4 = var9.getAnnotation(var1);
                  if (var4 != null) {
                     break;
                  }
               }
            }
         }

         return var4;
      }
   }

   private static List<Class<?>> getAllSuperclassesAndInterfaces(Class<?> var0) {
      if (var0 == null) {
         return null;
      } else {
         ArrayList var1 = new ArrayList();
         List var2 = ClassUtils.getAllSuperclasses(var0);
         int var3 = 0;
         List var4 = ClassUtils.getAllInterfaces(var0);

         Class var6;
         for(int var5 = 0; var5 < var4.size() || var3 < var2.size(); var1.add(var6)) {
            if (var5 >= var4.size()) {
               var6 = (Class)var2.get(var3++);
            } else if (var3 < var2.size() && var5 >= var3 && var3 < var5) {
               var6 = (Class)var2.get(var3++);
            } else {
               var6 = (Class)var4.get(var5++);
            }
         }

         return var1;
      }
   }
}
