package fr.xephi.authme.libs.ch.jalu.injector.handlers.dependency;

import fr.xephi.authme.libs.ch.jalu.injector.Injector;
import fr.xephi.authme.libs.ch.jalu.injector.context.ResolutionContext;
import fr.xephi.authme.libs.ch.jalu.injector.exceptions.InjectorException;
import fr.xephi.authme.libs.ch.jalu.injector.factory.SingletonStore;
import fr.xephi.authme.libs.ch.jalu.injector.handlers.Handler;
import fr.xephi.authme.libs.ch.jalu.injector.handlers.instantiation.Resolution;
import fr.xephi.authme.libs.ch.jalu.injector.handlers.instantiation.SimpleResolution;
import fr.xephi.authme.libs.ch.jalu.injector.utils.ReflectionUtils;
import java.util.Collection;

public class SingletonStoreDependencyHandler implements Handler {
   public Resolution<?> resolve(ResolutionContext context) {
      if (SingletonStore.class.equals(context.getIdentifier().getTypeAsClass())) {
         Class<?> genericType = ReflectionUtils.getGenericType(context.getIdentifier().getType());
         if (genericType == null) {
            throw new InjectorException("Singleton store fields must have concrete generic type. Cannot get generic type for field in '" + context.getIdentifier().getTypeAsClass() + "'");
         } else {
            return new SimpleResolution(new SingletonStoreDependencyHandler.SingletonStoreImpl(genericType, context.getInjector()));
         }
      } else {
         return null;
      }
   }

   private static final class SingletonStoreImpl<P> implements SingletonStore<P> {
      private final Injector injector;
      private final Class<P> parentClass;

      SingletonStoreImpl(Class<P> parentClass, Injector injector) {
         this.parentClass = parentClass;
         this.injector = injector;
      }

      public <C extends P> C getSingleton(Class<C> clazz) {
         if (this.parentClass.isAssignableFrom(clazz)) {
            return this.injector.getSingleton(clazz);
         } else {
            throw new InjectorException(clazz + " not child of " + this.parentClass);
         }
      }

      public Collection<P> retrieveAllOfType() {
         return this.retrieveAllOfType(this.parentClass);
      }

      public <C extends P> Collection<C> retrieveAllOfType(Class<C> clazz) {
         if (this.parentClass.isAssignableFrom(clazz)) {
            return this.injector.retrieveAllOfType(clazz);
         } else {
            throw new InjectorException(clazz + " not child of " + this.parentClass);
         }
      }
   }
}
