package fr.xephi.authme.libs.ch.jalu.injector.handlers.instantiation;

import fr.xephi.authme.libs.ch.jalu.injector.exceptions.InjectorException;
import fr.xephi.authme.libs.ch.jalu.injector.utils.ReflectionUtils;
import fr.xephi.authme.libs.javax.inject.Inject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.Nullable;

public class StandardInjectionProvider extends DirectInstantiationProvider {
   public <T> Resolution<T> safeGet(Class<T> clazz) {
      Constructor<T> constructor = this.getInjectionConstructor(clazz);
      if (constructor == null) {
         return null;
      } else {
         List<Field> fields = this.getFieldsToInject(clazz);
         this.validateInjection(clazz, constructor, fields);
         return new StandardInjection(constructor, fields);
      }
   }

   @Nullable
   protected <T> Constructor<T> getInjectionConstructor(Class<T> clazz) {
      Constructor<?>[] constructors = clazz.getDeclaredConstructors();
      if (constructors.length == 1 && isSuitableNoArgsConstructor(constructors[0])) {
         return constructors[0];
      } else {
         Constructor<?> matchingConstructor = null;
         Constructor[] var4 = constructors;
         int var5 = constructors.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            Constructor<?> constructor = var4[var6];
            if (constructor.isAnnotationPresent(Inject.class)) {
               if (matchingConstructor != null) {
                  throw new InjectorException("Class '" + clazz + "' may not have multiple @Inject constructors");
               }

               matchingConstructor = constructor;
            }
         }

         if (matchingConstructor == null) {
            return getNoArgsConstructorIfHasInjectField(clazz);
         } else {
            return matchingConstructor;
         }
      }
   }

   private static boolean isSuitableNoArgsConstructor(Constructor<?> c) {
      if (c.getParameterTypes().length > 0) {
         return false;
      } else {
         return !Modifier.isPrivate(c.getModifiers()) || Modifier.isPrivate(c.getDeclaringClass().getModifiers());
      }
   }

   @Nullable
   private static <T> Constructor<T> getNoArgsConstructorIfHasInjectField(Class<T> clazz) {
      try {
         Constructor<?> constructor = clazz.getDeclaredConstructor();
         Field[] var2 = ReflectionUtils.safeGetDeclaredFields(clazz);
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            Field field = var2[var4];
            if (field.isAnnotationPresent(Inject.class)) {
               return constructor;
            }
         }
      } catch (NoSuchMethodException var6) {
      }

      return null;
   }

   protected List<Field> getFieldsToInject(Class<?> clazz) {
      List<Field> fields = new LinkedList();

      for(Class currentClass = clazz; currentClass != null; currentClass = currentClass.getSuperclass()) {
         Field[] var4 = ReflectionUtils.safeGetDeclaredFields(currentClass);
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            Field f = var4[var6];
            if (f.isAnnotationPresent(Inject.class)) {
               fields.add(f);
            }
         }
      }

      return fields;
   }

   protected void validateInjection(Class<?> clazz, Constructor<?> constructor, List<Field> fields) {
      this.validateHasNoFinalFields(fields);
      this.validateHasNoInjectMethods(clazz);
   }

   private void validateHasNoFinalFields(List<Field> fields) {
      Iterator var2 = fields.iterator();

      Field field;
      do {
         if (!var2.hasNext()) {
            return;
         }

         field = (Field)var2.next();
      } while(!Modifier.isFinal(field.getModifiers()));

      throw new InjectorException("Field '" + field + "' may not be final and have @Inject");
   }

   private void validateHasNoInjectMethods(Class<?> clazz) {
      for(Class currentClass = clazz; currentClass != null; currentClass = currentClass.getSuperclass()) {
         Method[] var3 = ReflectionUtils.safeGetDeclaredMethods(currentClass);
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            Method m = var3[var5];
            if (m.isAnnotationPresent(Inject.class)) {
               throw new InjectorException("@Inject on methods is not supported, but found it on '" + m + "' while trying to instantiate '" + currentClass + "'");
            }
         }
      }

   }
}
