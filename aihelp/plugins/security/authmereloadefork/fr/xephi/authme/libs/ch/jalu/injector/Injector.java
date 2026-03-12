package fr.xephi.authme.libs.ch.jalu.injector;

import fr.xephi.authme.libs.javax.inject.Provider;
import java.lang.annotation.Annotation;
import java.util.Collection;
import javax.annotation.Nullable;

public interface Injector {
   <T> void register(Class<? super T> var1, T var2);

   <T> void registerProvider(Class<T> var1, Provider<? extends T> var2);

   <T, P extends Provider<? extends T>> void registerProvider(Class<T> var1, Class<P> var2);

   void provide(Class<? extends Annotation> var1, @Nullable Object var2);

   <T> T getSingleton(Class<T> var1);

   <T> T newInstance(Class<T> var1);

   @Nullable
   <T> T getIfAvailable(Class<T> var1);

   @Nullable
   <T> T createIfHasDependencies(Class<T> var1);

   <T> Collection<T> retrieveAllOfType(Class<T> var1);
}
