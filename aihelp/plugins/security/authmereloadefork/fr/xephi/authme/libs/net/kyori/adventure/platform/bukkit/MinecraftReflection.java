package fr.xephi.authme.libs.net.kyori.adventure.platform.bukkit;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Objects;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class MinecraftReflection {
   private static final Lookup LOOKUP = MethodHandles.lookup();
   private static final String PREFIX_NMS = "net.minecraft.server";
   private static final String PREFIX_MC = "net.minecraft.";
   private static final String PREFIX_CRAFTBUKKIT = "org.bukkit.craftbukkit";
   private static final String CRAFT_SERVER = "CraftServer";
   @Nullable
   private static final String VERSION;

   private MinecraftReflection() {
   }

   @Nullable
   public static Class<?> findClass(@Nullable @NotNull final String... classNames) {
      String[] var1 = classNames;
      int var2 = classNames.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         String clazz = var1[var3];
         if (clazz != null) {
            try {
               Class<?> classObj = Class.forName(clazz);
               return classObj;
            } catch (ClassNotFoundException var6) {
            }
         }
      }

      return null;
   }

   @NotNull
   public static Class<?> needClass(@Nullable @NotNull final String... className) {
      return (Class)Objects.requireNonNull(findClass(className), "Could not find class from candidates" + Arrays.toString(className));
   }

   public static boolean hasClass(@NotNull final String... classNames) {
      return findClass(classNames) != null;
   }

   @Nullable
   public static MethodHandle findMethod(@Nullable final Class<?> holderClass, final String methodName, @Nullable final Class<?> returnClass, final Class<?>... parameterClasses) {
      return findMethod(holderClass, new String[]{methodName}, returnClass, parameterClasses);
   }

   @Nullable
   public static MethodHandle findMethod(@Nullable final Class<?> holderClass, @Nullable @NotNull final String[] methodNames, @Nullable final Class<?> returnClass, final Class<?>... parameterClasses) {
      if (holderClass != null && returnClass != null) {
         Class[] var4 = parameterClasses;
         int var5 = parameterClasses.length;

         int var6;
         for(var6 = 0; var6 < var5; ++var6) {
            Class<?> parameterClass = var4[var6];
            if (parameterClass == null) {
               return null;
            }
         }

         String[] var10 = methodNames;
         var5 = methodNames.length;

         for(var6 = 0; var6 < var5; ++var6) {
            String methodName = var10[var6];
            if (methodName != null) {
               try {
                  return LOOKUP.findVirtual(holderClass, methodName, MethodType.methodType(returnClass, parameterClasses));
               } catch (IllegalAccessException | NoSuchMethodException var9) {
               }
            }
         }

         return null;
      } else {
         return null;
      }
   }

   public static MethodHandle searchMethod(@Nullable final Class<?> holderClass, @Nullable final Integer modifier, final String methodName, @Nullable final Class<?> returnClass, final Class<?>... parameterClasses) {
      return searchMethod(holderClass, modifier, new String[]{methodName}, returnClass, parameterClasses);
   }

   public static MethodHandle searchMethod(@Nullable final Class<?> holderClass, @Nullable final Integer modifier, @Nullable @NotNull final String[] methodNames, @Nullable final Class<?> returnClass, final Class<?>... parameterClasses) {
      if (holderClass != null && returnClass != null) {
         Class[] var5 = parameterClasses;
         int var6 = parameterClasses.length;

         int var7;
         for(var7 = 0; var7 < var6; ++var7) {
            Class<?> parameterClass = var5[var7];
            if (parameterClass == null) {
               return null;
            }
         }

         String[] var12 = methodNames;
         var6 = methodNames.length;

         for(var7 = 0; var7 < var6; ++var7) {
            String methodName = var12[var7];
            if (methodName != null) {
               try {
                  if (modifier != null && Modifier.isStatic(modifier)) {
                     return LOOKUP.findStatic(holderClass, methodName, MethodType.methodType(returnClass, parameterClasses));
                  }

                  return LOOKUP.findVirtual(holderClass, methodName, MethodType.methodType(returnClass, parameterClasses));
               } catch (IllegalAccessException | NoSuchMethodException var11) {
               }
            }
         }

         Method[] var13 = holderClass.getDeclaredMethods();
         var6 = var13.length;

         for(var7 = 0; var7 < var6; ++var7) {
            Method method = var13[var7];
            if (modifier != null && (method.getModifiers() & modifier) != 0 && Arrays.equals(method.getParameterTypes(), parameterClasses)) {
               try {
                  if (Modifier.isStatic(modifier)) {
                     return LOOKUP.findStatic(holderClass, method.getName(), MethodType.methodType(returnClass, parameterClasses));
                  }

                  return LOOKUP.findVirtual(holderClass, method.getName(), MethodType.methodType(returnClass, parameterClasses));
               } catch (IllegalAccessException | NoSuchMethodException var10) {
               }
            }
         }

         return null;
      } else {
         return null;
      }
   }

   @Nullable
   public static MethodHandle findStaticMethod(@Nullable final Class<?> holderClass, final String methodNames, @Nullable final Class<?> returnClass, final Class<?>... parameterClasses) {
      return findStaticMethod(holderClass, new String[]{methodNames}, returnClass, parameterClasses);
   }

   @Nullable
   public static MethodHandle findStaticMethod(@Nullable final Class<?> holderClass, final String[] methodNames, @Nullable final Class<?> returnClass, final Class<?>... parameterClasses) {
      if (holderClass != null && returnClass != null) {
         Class[] var4 = parameterClasses;
         int var5 = parameterClasses.length;

         int var6;
         for(var6 = 0; var6 < var5; ++var6) {
            Class<?> parameterClass = var4[var6];
            if (parameterClass == null) {
               return null;
            }
         }

         String[] var10 = methodNames;
         var5 = methodNames.length;
         var6 = 0;

         while(var6 < var5) {
            String methodName = var10[var6];

            try {
               return LOOKUP.findStatic(holderClass, methodName, MethodType.methodType(returnClass, parameterClasses));
            } catch (IllegalAccessException | NoSuchMethodException var9) {
               ++var6;
            }
         }

         return null;
      } else {
         return null;
      }
   }

   public static boolean hasField(@Nullable final Class<?> holderClass, final Class<?> type, final String... names) {
      if (holderClass == null) {
         return false;
      } else {
         String[] var3 = names;
         int var4 = names.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            String name = var3[var5];

            try {
               Field field = holderClass.getDeclaredField(name);
               if (field.getType() == type) {
                  return true;
               }
            } catch (NoSuchFieldException var8) {
            }
         }

         return false;
      }
   }

   public static boolean hasMethod(@Nullable final Class<?> holderClass, final String methodName, final Class<?>... parameterClasses) {
      return hasMethod(holderClass, new String[]{methodName}, parameterClasses);
   }

   public static boolean hasMethod(@Nullable final Class<?> holderClass, final String[] methodNames, final Class<?>... parameterClasses) {
      if (holderClass == null) {
         return false;
      } else {
         Class[] var3 = parameterClasses;
         int var4 = parameterClasses.length;

         int var5;
         for(var5 = 0; var5 < var4; ++var5) {
            Class<?> parameterClass = var3[var5];
            if (parameterClass == null) {
               return false;
            }
         }

         String[] var9 = methodNames;
         var4 = methodNames.length;
         var5 = 0;

         while(var5 < var4) {
            String methodName = var9[var5];

            try {
               holderClass.getMethod(methodName, parameterClasses);
               return true;
            } catch (NoSuchMethodException var8) {
               ++var5;
            }
         }

         return false;
      }
   }

   @Nullable
   public static MethodHandle findConstructor(@Nullable final Class<?> holderClass, @Nullable final Class<?>... parameterClasses) {
      if (holderClass == null) {
         return null;
      } else {
         Class[] var2 = parameterClasses;
         int var3 = parameterClasses.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            Class<?> parameterClass = var2[var4];
            if (parameterClass == null) {
               return null;
            }
         }

         try {
            return LOOKUP.findConstructor(holderClass, MethodType.methodType(Void.TYPE, parameterClasses));
         } catch (IllegalAccessException | NoSuchMethodException var6) {
            return null;
         }
      }
   }

   @NotNull
   public static Field needField(@NotNull final Class<?> holderClass, @NotNull final String fieldName) throws NoSuchFieldException {
      Field field = holderClass.getDeclaredField(fieldName);
      field.setAccessible(true);
      return field;
   }

   @Nullable
   public static Field findField(@Nullable final Class<?> holderClass, @NotNull final String... fieldName) {
      return findField(holderClass, (Class)null, fieldName);
   }

   @Nullable
   public static Field findField(@Nullable final Class<?> holderClass, @Nullable final Class<?> expectedType, @NotNull final String... fieldNames) {
      if (holderClass == null) {
         return null;
      } else {
         String[] var4 = fieldNames;
         int var5 = fieldNames.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            String fieldName = var4[var6];

            Field field;
            try {
               field = holderClass.getDeclaredField(fieldName);
            } catch (NoSuchFieldException var9) {
               continue;
            }

            field.setAccessible(true);
            if (expectedType == null || expectedType.isAssignableFrom(field.getType())) {
               return field;
            }
         }

         return null;
      }
   }

   @Nullable
   public static MethodHandle findSetterOf(@Nullable final Field field) {
      if (field == null) {
         return null;
      } else {
         try {
            return LOOKUP.unreflectSetter(field);
         } catch (IllegalAccessException var2) {
            return null;
         }
      }
   }

   @Nullable
   public static MethodHandle findGetterOf(@Nullable final Field field) {
      if (field == null) {
         return null;
      } else {
         try {
            return LOOKUP.unreflectGetter(field);
         } catch (IllegalAccessException var2) {
            return null;
         }
      }
   }

   @Nullable
   public static Object findEnum(@Nullable final Class<?> enumClass, @NotNull final String enumName) {
      return findEnum(enumClass, enumName, Integer.MAX_VALUE);
   }

   @Nullable
   public static Object findEnum(@Nullable final Class<?> enumClass, @NotNull final String enumName, final int enumFallbackOrdinal) {
      if (enumClass != null && Enum.class.isAssignableFrom(enumClass)) {
         try {
            return Enum.valueOf(enumClass.asSubclass(Enum.class), enumName);
         } catch (IllegalArgumentException var5) {
            Object[] constants = enumClass.getEnumConstants();
            return constants.length > enumFallbackOrdinal ? constants[enumFallbackOrdinal] : null;
         }
      } else {
         return null;
      }
   }

   public static boolean isCraftBukkit() {
      return VERSION != null;
   }

   @Nullable
   public static String findCraftClassName(@NotNull final String className) {
      return isCraftBukkit() ? "org.bukkit.craftbukkit" + VERSION + className : null;
   }

   @Nullable
   public static Class<?> findCraftClass(@NotNull final String className) {
      String craftClassName = findCraftClassName(className);
      return craftClassName == null ? null : findClass(craftClassName);
   }

   @Nullable
   public static <T> Class<? extends T> findCraftClass(@NotNull final String className, @NotNull final Class<T> superClass) {
      Class<?> craftClass = findCraftClass(className);
      return craftClass != null && ((Class)Objects.requireNonNull(superClass, "superClass")).isAssignableFrom(craftClass) ? craftClass.asSubclass(superClass) : null;
   }

   @NotNull
   public static Class<?> needCraftClass(@NotNull final String className) {
      return (Class)Objects.requireNonNull(findCraftClass(className), "Could not find org.bukkit.craftbukkit class " + className);
   }

   @Nullable
   public static String findNmsClassName(@NotNull final String className) {
      return isCraftBukkit() ? "net.minecraft.server" + VERSION + className : null;
   }

   @Nullable
   public static Class<?> findNmsClass(@NotNull final String className) {
      String nmsClassName = findNmsClassName(className);
      return nmsClassName == null ? null : findClass(nmsClassName);
   }

   @NotNull
   public static Class<?> needNmsClass(@NotNull final String className) {
      return (Class)Objects.requireNonNull(findNmsClass(className), "Could not find net.minecraft.server class " + className);
   }

   @Nullable
   public static String findMcClassName(@NotNull final String className) {
      return isCraftBukkit() ? "net.minecraft." + className : null;
   }

   @Nullable
   public static Class<?> findMcClass(@NotNull final String... classNames) {
      String[] var1 = classNames;
      int var2 = classNames.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         String clazz = var1[var3];
         String nmsClassName = findMcClassName(clazz);
         if (nmsClassName != null) {
            Class<?> candidate = findClass(nmsClassName);
            if (candidate != null) {
               return candidate;
            }
         }
      }

      return null;
   }

   @NotNull
   public static Class<?> needMcClass(@NotNull final String... className) {
      return (Class)Objects.requireNonNull(findMcClass(className), "Could not find net.minecraft class from candidates" + Arrays.toString(className));
   }

   @NotNull
   public static Lookup lookup() {
      return LOOKUP;
   }

   static {
      Class<?> serverClass = Bukkit.getServer().getClass();
      if (!serverClass.getSimpleName().equals("CraftServer")) {
         VERSION = null;
      } else if (serverClass.getName().equals("org.bukkit.craftbukkit.CraftServer")) {
         VERSION = ".";
      } else {
         String name = serverClass.getName();
         name = name.substring("org.bukkit.craftbukkit".length());
         name = name.substring(0, name.length() - "CraftServer".length());
         VERSION = name;
      }

   }
}
