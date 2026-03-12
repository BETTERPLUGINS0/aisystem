package fr.xephi.authme.libs.ch.jalu.injector.handlers.instantiation;

import fr.xephi.authme.libs.ch.jalu.injector.context.ObjectIdentifier;
import fr.xephi.authme.libs.ch.jalu.injector.context.ResolutionContext;
import fr.xephi.authme.libs.ch.jalu.injector.context.StandardResolutionType;
import fr.xephi.authme.libs.ch.jalu.injector.exceptions.InjectorException;
import fr.xephi.authme.libs.ch.jalu.injector.handlers.Handler;
import fr.xephi.authme.libs.ch.jalu.injector.utils.InjectorUtils;
import fr.xephi.authme.libs.ch.jalu.injector.utils.ReflectionUtils;
import fr.xephi.authme.libs.javax.inject.Provider;
import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;

public class ProviderHandler implements Handler {
   protected Map<Class<?>, ProviderHandler.ProviderBasedInstantiation<?>> providers = new HashMap();

   public <T> void onProvider(Class<T> clazz, Provider<? extends T> provider) {
      InjectorUtils.checkArgument(!this.providers.containsKey(clazz), "Provider already registered for " + clazz);
      this.providers.put(clazz, new ProviderHandler.InstantiationByProvider(provider));
   }

   public <T, P extends Provider<? extends T>> void onProviderClass(Class<T> clazz, Class<P> providerClass) {
      InjectorUtils.checkArgument(!this.providers.containsKey(clazz), "Provider already registered for " + clazz);
      this.providers.put(clazz, new ProviderHandler.InstantiationByProviderClass(providerClass));
   }

   public Resolution<?> resolve(ResolutionContext context) {
      return Provider.class.equals(context.getIdentifier().getTypeAsClass()) ? this.handleProviderRequest(context) : (Resolution)this.providers.get(context.getIdentifier().getTypeAsClass());
   }

   @Nullable
   private Resolution<?> handleProviderRequest(ResolutionContext context) {
      Class<?> genericType = ReflectionUtils.getGenericType(context.getIdentifier().getType());
      if (genericType == null) {
         throw new InjectorException("Injection of a provider was requested but no generic type was given");
      } else {
         ProviderHandler.ProviderBasedInstantiation<?> givenInstantiation = (ProviderHandler.ProviderBasedInstantiation)this.providers.get(genericType);
         if (givenInstantiation == null) {
            Provider<?> defaultProvider = () -> {
               return context.getInjector().newInstance(genericType);
            };
            return new SimpleResolution(defaultProvider);
         } else {
            return givenInstantiation.createProviderResolution();
         }
      }
   }

   private static final class InstantiationByProviderClass<T> implements ProviderHandler.ProviderBasedInstantiation<T> {
      private final Class<? extends Provider<? extends T>> providerClass;

      InstantiationByProviderClass(Class<? extends Provider<? extends T>> providerClass) {
         this.providerClass = providerClass;
      }

      public List<ObjectIdentifier> getDependencies() {
         return Collections.singletonList(new ObjectIdentifier(StandardResolutionType.SINGLETON, this.providerClass, new Annotation[0]));
      }

      public T instantiateWith(Object... values) {
         InjectorUtils.checkArgument(values.length == 1 && this.providerClass.isInstance(values[0]), "Expected one dependency of type " + this.providerClass);
         return ((Provider)values[0]).get();
      }

      public boolean isInstantiation() {
         return true;
      }

      public Resolution<Provider<? extends T>> createProviderResolution() {
         return new Resolution<Provider<? extends T>>() {
            public List<ObjectIdentifier> getDependencies() {
               return Collections.singletonList(new ObjectIdentifier(StandardResolutionType.SINGLETON, InstantiationByProviderClass.this.providerClass, new Annotation[0]));
            }

            public Provider<? extends T> instantiateWith(Object... values) {
               InjectorUtils.checkArgument(values.length == 1 && InstantiationByProviderClass.this.providerClass.isInstance(values[0]), "Expected one dependency of type " + InstantiationByProviderClass.this.providerClass);
               return (Provider)values[0];
            }
         };
      }
   }

   private static final class InstantiationByProvider<T> implements ProviderHandler.ProviderBasedInstantiation<T> {
      private final Provider<? extends T> provider;

      InstantiationByProvider(Provider<? extends T> provider) {
         this.provider = provider;
      }

      public List<ObjectIdentifier> getDependencies() {
         return Collections.emptyList();
      }

      public T instantiateWith(Object... values) {
         InjectorUtils.checkArgument(values.length == 0, "No dependencies expected");
         return this.provider.get();
      }

      public boolean isInstantiation() {
         return true;
      }

      public Resolution<Provider<? extends T>> createProviderResolution() {
         return new SimpleResolution(this.provider);
      }
   }

   private interface ProviderBasedInstantiation<T> extends Resolution<T> {
      Resolution<Provider<? extends T>> createProviderResolution();
   }
}
