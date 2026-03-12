package fr.xephi.authme.libs.ch.jalu.injector.handlers.instantiation;

import fr.xephi.authme.libs.ch.jalu.injector.exceptions.InjectorException;
import fr.xephi.authme.libs.javax.inject.Inject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Iterator;
import java.util.List;

public class DefaultInjectionProvider extends StandardInjectionProvider {
   private final String rootPackage;

   public DefaultInjectionProvider(String rootPackage) {
      this.rootPackage = rootPackage;
   }

   public <T> Resolution<T> safeGet(Class<T> clazz) {
      this.verifyIsClassPackageAllowed(clazz);
      return super.safeGet(clazz);
   }

   protected void validateInjection(Class<?> clazz, Constructor<?> constructor, List<Field> fields) {
      super.validateInjection(clazz, constructor, fields);
      this.verifyIsClassPackageAllowed(clazz);
      boolean hasConstructionInjection = constructor.isAnnotationPresent(Inject.class);
      Iterator var5 = fields.iterator();

      Field field;
      do {
         if (!var5.hasNext()) {
            return;
         }

         field = (Field)var5.next();
         if (hasConstructionInjection && field.getDeclaringClass() == clazz) {
            throw new InjectorException(clazz + " may not have @Inject constructor and @Inject fields. Pass the fields via the constructor as well, remove the @Inject constructor, or use " + StandardInjectionProvider.class.getSimpleName() + " instead");
         }
      } while(!Modifier.isStatic(field.getModifiers()));

      throw new InjectorException("@Inject may not be placed on static fields (found violation: '" + field + "')");
   }

   protected void verifyIsClassPackageAllowed(Class<?> clazz) {
      String packageName = clazz.getPackage().getName();
      if (!packageName.startsWith(this.rootPackage)) {
         throw new InjectorException("Class '" + clazz + "' with package '" + packageName + "' is outside of the allowed packages. It must be provided explicitly or the package must be passed to the constructor.");
      }
   }
}
