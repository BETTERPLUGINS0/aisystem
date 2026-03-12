package fr.xephi.authme.libs.ch.jalu.injector.utils;

import fr.xephi.authme.libs.ch.jalu.injector.annotations.NoFieldScan;
import fr.xephi.authme.libs.ch.jalu.injector.annotations.NoMethodScan;
import fr.xephi.authme.libs.ch.jalu.injector.exceptions.InjectorException;
import fr.xephi.authme.libs.ch.jalu.injector.exceptions.InjectorReflectionException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;

public final class ReflectionUtils {
   private ReflectionUtils() {
   }

   public static Object getFieldValue(Field field, Object instance) {
      field.setAccessible(true);

      try {
         return field.get(instance);
      } catch (IllegalArgumentException | IllegalAccessException var3) {
         throw new InjectorReflectionException("Could not get value of field '" + field.getName() + "' for " + instance, var3);
      }
   }

   public static void setField(Field field, Object instance, Object value) {
      field.setAccessible(true);

      try {
         field.set(instance, value);
      } catch (IllegalArgumentException | IllegalAccessException var4) {
         throw new InjectorReflectionException("Could not set field '" + field.getName() + "' for " + instance, var4);
      }
   }

   public static Object invokeMethod(Method method, Object instance, Object... parameters) {
      method.setAccessible(true);

      try {
         return method.invoke(instance, parameters);
      } catch (IllegalAccessException | InvocationTargetException var4) {
         throw new InjectorReflectionException("Could not invoke method '" + method.getName() + "' for " + instance, var4);
      }
   }

   public static <T> T newInstance(Constructor<T> constructor, Object... parameters) {
      constructor.setAccessible(true);

      try {
         return constructor.newInstance(parameters);
      } catch (InstantiationException | InvocationTargetException | IllegalAccessException var3) {
         throw new InjectorReflectionException("Could not invoke constructor of class '" + constructor.getDeclaringClass() + "'", var3);
      }
   }

   @Nullable
   public static Class<?> getGenericType(@Nullable Type genericType) {
      if (genericType != null && genericType instanceof ParameterizedType) {
         Type[] types = ((ParameterizedType)genericType).getActualTypeArguments();
         if (types.length > 0 && types[0] instanceof Class) {
            return (Class)types[0];
         }
      }

      return null;
   }

   @Nullable
   public static Class<?> getCollectionType(Class<?> mainType, @Nullable Type genericType) {
      if (mainType.isArray()) {
         return mainType.getComponentType();
      } else {
         return Iterable.class.isAssignableFrom(mainType) ? getGenericType(genericType) : null;
      }
   }

   public static <T> Object toSuitableCollectionType(Class<?> rawType, Set<T> result) {
      if (rawType.isArray()) {
         return Arrays.copyOf(result.toArray(), result.size(), rawType);
      } else if (rawType.isAssignableFrom(Set.class)) {
         return result;
      } else if (rawType.isAssignableFrom(List.class)) {
         return new ArrayList(result);
      } else {
         throw new InjectorException("Cannot convert @AllTypes result to '" + rawType + "'. Supported: Set, List, or any supertype thereof, and array");
      }
   }

   public static Method[] safeGetDeclaredMethods(Class<?> clazz) {
      return clazz.isAnnotationPresent(NoMethodScan.class) ? new Method[0] : clazz.getDeclaredMethods();
   }

   public static Field[] safeGetDeclaredFields(Class<?> clazz) {
      return clazz.isAnnotationPresent(NoFieldScan.class) ? new Field[0] : clazz.getDeclaredFields();
   }
}
