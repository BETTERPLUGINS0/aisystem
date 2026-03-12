package fr.xephi.authme.libs.ch.jalu.injector.handlers;

import fr.xephi.authme.libs.ch.jalu.injector.context.ResolutionContext;
import fr.xephi.authme.libs.ch.jalu.injector.handlers.instantiation.Resolution;
import fr.xephi.authme.libs.javax.inject.Provider;
import java.lang.annotation.Annotation;
import javax.annotation.Nullable;

public interface Handler {
   @Nullable
   default Resolution<?> resolve(ResolutionContext context) throws Exception {
      return null;
   }

   @Nullable
   default <T> T postProcess(T object, ResolutionContext context, Resolution<?> resolution) throws Exception {
      return null;
   }

   default void onAnnotation(Class<? extends Annotation> annotationType, @Nullable Object object) throws Exception {
   }

   default <T> void onProvider(Class<T> clazz, Provider<? extends T> provider) throws Exception {
   }

   default <T, P extends Provider<? extends T>> void onProviderClass(Class<T> clazz, Class<P> providerClass) throws Exception {
   }
}
