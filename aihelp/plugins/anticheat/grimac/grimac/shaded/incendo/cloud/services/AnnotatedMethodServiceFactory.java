package ac.grim.grimac.shaded.incendo.cloud.services;

import ac.grim.grimac.shaded.geantyref.TypeToken;
import ac.grim.grimac.shaded.incendo.cloud.services.annotation.ServiceImplementation;
import ac.grim.grimac.shaded.incendo.cloud.services.type.Service;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import org.checkerframework.checker.nullness.qual.NonNull;

enum AnnotatedMethodServiceFactory {
   INSTANCE;

   @NonNull
   Map<? extends Service<?, ?>, TypeToken<? extends Service<?, ?>>> lookupServices(@NonNull final Object instance) throws Exception {
      Map<Service<?, ?>, TypeToken<? extends Service<?, ?>>> map = new HashMap();
      Class<?> clazz = instance.getClass();
      Method[] var4 = clazz.getDeclaredMethods();
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         Method method = var4[var6];
         ServiceImplementation serviceImplementation = (ServiceImplementation)method.getAnnotation(ServiceImplementation.class);
         if (serviceImplementation != null) {
            if (method.getParameterCount() != 1) {
               throw new IllegalArgumentException(String.format("Method '%s' in class '%s' has wrong parameter count. Expected 1, got %d", method.getName(), instance.getClass().getCanonicalName(), method.getParameterCount()));
            }

            map.put(new AnnotatedMethodService(instance, method), TypeToken.get(serviceImplementation.value()));
         }
      }

      return map;
   }

   // $FF: synthetic method
   private static AnnotatedMethodServiceFactory[] $values() {
      return new AnnotatedMethodServiceFactory[]{INSTANCE};
   }
}
