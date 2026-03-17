package me.PM2.infinitevehicles.commands;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Map;

class Annotations<M extends CommandManager> extends AnnotationLookups {
   public static final int NOTHING = 0;
   public static final int REPLACEMENTS = 1;
   public static final int LOWERCASE = 2;
   public static final int UPPERCASE = 4;
   public static final int NO_EMPTY = 8;
   public static final int DEFAULT_EMPTY = 16;
   private final M manager;
   private final Map<Class<? extends Annotation>, Method> valueMethods = new IdentityHashMap();
   private final Map<Class<? extends Annotation>, Void> noValueAnnotations = new IdentityHashMap();

   Annotations(M manager) {
      this.manager = var1;
   }

   String getAnnotationValue(AnnotatedElement object, Class<? extends Annotation> annoClass, int options) {
      Annotation var4 = getAnnotationRecursive(var1, var2, new HashSet());
      if (var4 == null) {
         if (var1 instanceof Class) {
            var4 = getAnnotationFromParentClasses((Class)var1, var2);
         } else if (var1 instanceof Method) {
            var4 = getAnnotationFromParentMethods((Method)var1, var2);
         } else if (var1 instanceof Parameter) {
            var4 = getAnnotationFromParentParameters((Parameter)var1, var2);
         }
      }

      String var5 = null;
      if (var4 != null) {
         Method var6 = (Method)this.valueMethods.get(var2);
         if (this.noValueAnnotations.containsKey(var2)) {
            var5 = "";
         } else {
            try {
               if (var6 == null) {
                  var6 = var2.getMethod("value");
                  var6.setAccessible(true);
                  this.valueMethods.put(var2, var6);
               }

               var5 = (String)var6.invoke(var4);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException var8) {
               if (!(var8 instanceof NoSuchMethodException)) {
                  this.manager.log(LogLevel.ERROR, "Error getting annotation value", var8);
               }

               this.noValueAnnotations.put(var2, (Object)null);
               var5 = "";
            }
         }
      }

      if (var5 == null) {
         if (!hasOption(var3, 16)) {
            return null;
         }

         var5 = "";
      }

      if (hasOption(var3, 1)) {
         var5 = this.manager.getCommandReplacements().replace(var5);
      }

      if (hasOption(var3, 2)) {
         var5 = var5.toLowerCase(this.manager.getLocales().getDefaultLocale());
      } else if (hasOption(var3, 4)) {
         var5 = var5.toUpperCase(this.manager.getLocales().getDefaultLocale());
      }

      if (var5.isEmpty() && hasOption(var3, 8)) {
         var5 = null;
      }

      return var5;
   }

   private static Annotation getAnnotationFromParentClasses(Class<?> clazz, Class<? extends Annotation> annoClass) {
      for(Class var2 = var0.getSuperclass(); var2 != null && !var2.equals(BaseCommand.class) && !var2.equals(Object.class); var2 = var2.getSuperclass()) {
         Annotation var3 = getAnnotationRecursive(var2, var1, new HashSet());
         if (var3 != null) {
            return var3;
         }
      }

      return null;
   }

   private static Annotation getAnnotationFromParentMethods(Method method, Class<? extends Annotation> annoClass) {
      for(Class var2 = var0.getDeclaringClass().getSuperclass(); var2 != null && !var2.equals(BaseCommand.class) && !var2.equals(Object.class); var2 = var2.getSuperclass()) {
         try {
            Method var3 = var2.getDeclaredMethod(var0.getName(), var0.getParameterTypes());
            Annotation var4 = getAnnotationRecursive(var3, var1, new HashSet());
            if (var4 != null) {
               return var4;
            }
         } catch (NoSuchMethodException var5) {
            return null;
         }
      }

      return null;
   }

   private static Annotation getAnnotationFromParentParameters(Parameter parameter, Class<? extends Annotation> annoClass) {
      for(Class var2 = var0.getDeclaringExecutable().getDeclaringClass().getSuperclass(); var2 != null && !var2.equals(BaseCommand.class) && !var2.equals(Object.class); var2 = var2.getSuperclass()) {
         try {
            Method var3 = var2.getDeclaredMethod(var0.getDeclaringExecutable().getName(), var0.getDeclaringExecutable().getParameterTypes());
            Annotation var4 = (Annotation)Arrays.stream(var3.getParameters()).filter((var1x) -> {
               return var1x.getName().equals(var0.getName()) && var1x.getType().equals(var0.getType());
            }).findFirst().map((var1x) -> {
               return getAnnotationRecursive(var1x, var1, new HashSet());
            }).orElse((Object)null);
            if (var4 != null) {
               return var4;
            }
         } catch (NoSuchMethodException var5) {
            return null;
         }
      }

      return null;
   }

   private static Annotation getAnnotationRecursive(AnnotatedElement object, Class<? extends Annotation> annoClass, Collection<Annotation> checked) {
      if (var0.isAnnotationPresent(var1)) {
         return var0.getAnnotation(var1);
      } else {
         Annotation[] var3 = var0.getDeclaredAnnotations();
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            Annotation var6 = var3[var5];
            if (!var6.annotationType().getPackage().getName().startsWith("java.")) {
               if (var2.contains(var6)) {
                  return null;
               }

               var2.add(var6);
               Annotation var7 = getAnnotationRecursive(var6.annotationType(), var1, var2);
               if (var7 != null) {
                  return var7;
               }
            }
         }

         return null;
      }
   }

   private static boolean hasOption(int options, int option) {
      return (var0 & var1) == var1;
   }
}
