package fr.xephi.authme.libs.ch.jalu.injector.handlers.postconstruct;

import fr.xephi.authme.libs.ch.jalu.injector.context.ResolutionContext;
import fr.xephi.authme.libs.ch.jalu.injector.exceptions.InjectorException;
import fr.xephi.authme.libs.ch.jalu.injector.handlers.Handler;
import fr.xephi.authme.libs.ch.jalu.injector.handlers.instantiation.Resolution;
import fr.xephi.authme.libs.ch.jalu.injector.utils.ReflectionUtils;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import javax.annotation.PostConstruct;

public class PostConstructMethodInvoker implements Handler {
   public <T> T postProcess(T object, ResolutionContext context, Resolution<?> resolution) {
      Class<?> clazz = object.getClass();
      List<Method> postConstructMethods = getPostConstructMethods(clazz);

      for(int i = postConstructMethods.size() - 1; i >= 0; --i) {
         ReflectionUtils.invokeMethod((Method)postConstructMethods.get(i), object);
      }

      return null;
   }

   private static List<Method> getPostConstructMethods(Class<?> clazz) {
      List<Method> postConstructMethods = new ArrayList();

      for(Class currentClass = clazz; currentClass != null; currentClass = currentClass.getSuperclass()) {
         Method postConstruct = getAndValidatePostConstructMethod(currentClass);
         if (postConstruct != null) {
            postConstructMethods.add(postConstruct);
         }
      }

      return postConstructMethods;
   }

   @Nullable
   private static Method getAndValidatePostConstructMethod(Class<?> clazz) {
      Method postConstructMethod = null;
      Method[] var2 = ReflectionUtils.safeGetDeclaredMethods(clazz);
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Method method = var2[var4];
         if (method.isAnnotationPresent(PostConstruct.class)) {
            if (postConstructMethod != null) {
               throw new InjectorException("Multiple methods with @PostConstruct in " + clazz);
            }

            if (method.getParameterTypes().length > 0 || Modifier.isStatic(method.getModifiers())) {
               throw new InjectorException("@PostConstruct method may not be static or have any parameters. Invalid method in " + clazz);
            }

            if (method.getReturnType() != Void.TYPE) {
               throw new InjectorException("@PostConstruct method must have return type void. Offending class: " + clazz);
            }

            postConstructMethod = method;
         }
      }

      return postConstructMethod;
   }
}
