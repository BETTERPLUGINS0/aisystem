package fr.xephi.authme.libs.ch.jalu.injector.handlers.dependency;

import fr.xephi.authme.libs.ch.jalu.injector.Injector;
import fr.xephi.authme.libs.ch.jalu.injector.context.ResolutionContext;
import fr.xephi.authme.libs.ch.jalu.injector.exceptions.InjectorException;
import fr.xephi.authme.libs.ch.jalu.injector.factory.Factory;
import fr.xephi.authme.libs.ch.jalu.injector.handlers.Handler;
import fr.xephi.authme.libs.ch.jalu.injector.handlers.instantiation.Resolution;
import fr.xephi.authme.libs.ch.jalu.injector.handlers.instantiation.SimpleResolution;
import fr.xephi.authme.libs.ch.jalu.injector.utils.ReflectionUtils;

public class FactoryDependencyHandler implements Handler {
   public Resolution<?> resolve(ResolutionContext context) {
      Class<?> clazz = context.getIdentifier().getTypeAsClass();
      if (Factory.class.equals(clazz)) {
         Class<?> genericType = ReflectionUtils.getGenericType(context.getIdentifier().getType());
         if (genericType == null) {
            throw new InjectorException("Factory fields must have concrete generic type. Cannot get generic type for field in '" + context.getIdentifier().getTypeAsClass() + "'");
         } else {
            return new SimpleResolution(new FactoryDependencyHandler.FactoryImpl(genericType, context.getInjector()));
         }
      } else {
         return null;
      }
   }

   private static final class FactoryImpl<P> implements Factory<P> {
      private final Injector injector;
      private final Class<P> parentClass;

      FactoryImpl(Class<P> parentClass, Injector injector) {
         this.parentClass = parentClass;
         this.injector = injector;
      }

      public <C extends P> C newInstance(Class<C> clazz) {
         if (this.parentClass.isAssignableFrom(clazz)) {
            return this.injector.newInstance(clazz);
         } else {
            throw new InjectorException(clazz + " not child of " + this.parentClass);
         }
      }
   }
}
