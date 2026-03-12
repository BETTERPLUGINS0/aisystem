package com.nisovin.shopkeepers.util.java;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class ClassUtils {
   private static final String CLASS_FILE_EXTENSION = ".class";
   private static final Map<Class<?>, Class<?>> PRIMITIVE_WRAPPERS;

   public static boolean isPrimitiveWrapperOf(Class<?> targetClass, Class<?> primitive) {
      Validate.isTrue(primitive.isPrimitive(), "primitive is not a primitive type");
      return PRIMITIVE_WRAPPERS.get(primitive) == targetClass;
   }

   public static boolean isAssignableFrom(Class<?> to, Class<?> from) {
      if (to.isAssignableFrom(from)) {
         return true;
      } else if (to.isPrimitive()) {
         return isPrimitiveWrapperOf(from, to);
      } else {
         return from.isPrimitive() ? isPrimitiveWrapperOf(to, from) : false;
      }
   }

   public static boolean isAssignableFrom(Class<?> to, @Nullable Object value) {
      if (value != null) {
         return isAssignableFrom(to, value.getClass());
      } else {
         return !to.isPrimitive();
      }
   }

   public static <T, U extends T> Class<U> parameterized(Class<T> clazz) {
      return clazz;
   }

   public static String getSimpleTypeName(Class<?> clazz) {
      Validate.notNull(clazz, (String)"clazz is null");
      if (clazz.isArray()) {
         Class<?> componentType = (Class)Unsafe.assertNonNull(clazz.getComponentType());
         return getSimpleTypeName(componentType) + "[]";
      } else if (clazz.isAnonymousClass()) {
         String name = clazz.getName();
         return name.substring(name.lastIndexOf(46) + 1);
      } else {
         return clazz.getSimpleName();
      }
   }

   public static boolean loadAllClassesFromJar(File jarFile, Predicate<String> filter, Logger logger) {
      Validate.notNull(jarFile, (String)"jarFile is null");
      Validate.notNull(filter, (String)"filter is null");
      Validate.notNull(logger, (String)"logger is null");

      try {
         ZipInputStream jar = new ZipInputStream(new FileInputStream(jarFile));

         try {
            for(ZipEntry entry = jar.getNextEntry(); entry != null; entry = jar.getNextEntry()) {
               if (!entry.isDirectory()) {
                  String entryName = entry.getName();
                  if (entryName.endsWith(".class")) {
                     String className = entryName.substring(0, entryName.length() - ".class".length()).replace('/', '.');
                     if (filter.test(className)) {
                        try {
                           Class.forName(className);
                        } catch (ClassNotFoundException | LinkageError var9) {
                           logger.log(Level.WARNING, "Could not load class '" + className + "' from jar file '" + jarFile.getPath() + "'.", var9);
                        }
                     }
                  }
               }
            }
         } catch (Throwable var10) {
            try {
               jar.close();
            } catch (Throwable var8) {
               var10.addSuppressed(var8);
            }

            throw var10;
         }

         jar.close();
         return true;
      } catch (IOException var11) {
         logger.log(Level.WARNING, "Could not load classes from jar file '" + jarFile.getPath() + "'.", var11);
         return false;
      }
   }

   public static ClassLoader getClassLoader(Class<?> clazz) {
      Validate.notNull(clazz, (String)"clazz is null");
      ClassLoader classLoader = clazz.getClassLoader();
      return (ClassLoader)Validate.notNull(classLoader, (String)"The ClassLoader of clazz is null");
   }

   public static InputStream getResource(Class<?> clazz, String resourcePath) {
      ClassLoader classLoader = getClassLoader(clazz);
      InputStream resource = classLoader.getResourceAsStream(resourcePath);
      if (resource == null) {
         throw new MissingResourceException("Missing resource: " + resourcePath, clazz.getName(), resourcePath);
      } else {
         return resource;
      }
   }

   @Nullable
   public static Class<?> getClassOrNull(String className) {
      try {
         return Class.forName(className);
      } catch (ClassNotFoundException var2) {
         return null;
      }
   }

   private ClassUtils() {
   }

   static {
      Map<Class<?>, Class<?>> primitiveWrappers = new HashMap();
      primitiveWrappers.put(Boolean.TYPE, Boolean.class);
      primitiveWrappers.put(Byte.TYPE, Byte.class);
      primitiveWrappers.put(Character.TYPE, Character.class);
      primitiveWrappers.put(Double.TYPE, Double.class);
      primitiveWrappers.put(Float.TYPE, Float.class);
      primitiveWrappers.put(Integer.TYPE, Integer.class);
      primitiveWrappers.put(Long.TYPE, Long.class);
      primitiveWrappers.put(Short.TYPE, Short.class);
      PRIMITIVE_WRAPPERS = Collections.unmodifiableMap(primitiveWrappers);
   }
}
