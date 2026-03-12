package fr.xephi.authme.libs.ch.jalu.injector;

import fr.xephi.authme.libs.ch.jalu.injector.context.ObjectIdentifier;
import fr.xephi.authme.libs.ch.jalu.injector.context.ResolutionContext;
import fr.xephi.authme.libs.ch.jalu.injector.context.ResolutionType;
import fr.xephi.authme.libs.ch.jalu.injector.context.StandardResolutionType;
import fr.xephi.authme.libs.ch.jalu.injector.exceptions.InjectorException;
import fr.xephi.authme.libs.ch.jalu.injector.handlers.Handler;
import fr.xephi.authme.libs.ch.jalu.injector.handlers.instantiation.Resolution;
import fr.xephi.authme.libs.ch.jalu.injector.utils.InjectorUtils;
import fr.xephi.authme.libs.javax.inject.Provider;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;

public class InjectorImpl implements Injector {
   protected Map<Class<?>, Object> objects;
   protected InjectorConfig config;

   protected InjectorImpl(InjectorConfig config) {
      this.config = config;
      this.objects = new HashMap();
      this.objects.put(Injector.class, this);
   }

   public <T> void register(Class<? super T> clazz, T object) {
      if (this.objects.containsKey(clazz)) {
         throw new InjectorException("There is already an object present for " + clazz);
      } else {
         InjectorUtils.checkNotNull(object);
         this.objects.put(clazz, object);
      }
   }

   public void provide(Class<? extends Annotation> clazz, Object object) {
      InjectorUtils.checkNotNull(clazz, "Provided annotation may not be null");

      try {
         Iterator var3 = this.config.getHandlers().iterator();

         while(var3.hasNext()) {
            Handler handler = (Handler)var3.next();
            handler.onAnnotation(clazz, object);
         }
      } catch (Exception var5) {
         InjectorUtils.rethrowException(var5);
      }

   }

   public <T> T getSingleton(Class<T> clazz) {
      return this.resolve(StandardResolutionType.SINGLETON, clazz);
   }

   public <T> T newInstance(Class<T> clazz) {
      return this.resolve(StandardResolutionType.REQUEST_SCOPED, clazz);
   }

   public <T> T getIfAvailable(Class<T> clazz) {
      return clazz.cast(this.objects.get(clazz));
   }

   public <T> T createIfHasDependencies(Class<T> clazz) {
      return this.resolve(StandardResolutionType.REQUEST_SCOPED_IF_HAS_DEPENDENCIES, clazz);
   }

   public <T> Collection<T> retrieveAllOfType(Class<T> clazz) {
      List<T> instances = new ArrayList();
      Iterator var3 = this.objects.values().iterator();

      while(var3.hasNext()) {
         Object object = var3.next();
         if (clazz.isInstance(object)) {
            instances.add(clazz.cast(object));
         }
      }

      return instances;
   }

   public <T> void registerProvider(Class<T> clazz, Provider<? extends T> provider) {
      InjectorUtils.checkNotNull(clazz, "Class may not be null");
      InjectorUtils.checkNotNull(provider, "Provider may not be null");

      try {
         Iterator var3 = this.config.getHandlers().iterator();

         while(var3.hasNext()) {
            Handler handler = (Handler)var3.next();
            handler.onProvider(clazz, provider);
         }
      } catch (Exception var5) {
         InjectorUtils.rethrowException(var5);
      }

   }

   public <T, P extends Provider<? extends T>> void registerProvider(Class<T> clazz, Class<P> providerClass) {
      InjectorUtils.checkNotNull(clazz, "Class may not be null");
      InjectorUtils.checkNotNull(providerClass, "Provider class may not be null");

      try {
         Iterator var3 = this.config.getHandlers().iterator();

         while(var3.hasNext()) {
            Handler handler = (Handler)var3.next();
            handler.onProviderClass(clazz, providerClass);
         }
      } catch (Exception var5) {
         InjectorUtils.rethrowException(var5);
      }

   }

   public InjectorConfig getConfig() {
      return this.config;
   }

   private <T> T resolve(ResolutionType resolutionType, Class<?> clazz) {
      return this.resolveContext(new ResolutionContext(this, new ObjectIdentifier(resolutionType, clazz, new Annotation[0])));
   }

   @Nullable
   protected Object resolveContext(ResolutionContext context) {
      if (context.getIdentifier().getResolutionType() == StandardResolutionType.SINGLETON) {
         Object knownSingleton = this.objects.get(context.getIdentifier().getTypeAsClass());
         if (knownSingleton != null) {
            return knownSingleton;
         }
      }

      Resolution<?> resolution = this.findResolutionOrFail(context);
      if (isContextChildOfOptionalRequest(context) && resolution.isInstantiation()) {
         return null;
      } else {
         Object[] resolvedDependencies = this.resolveDependencies(context, resolution);
         if (InjectorUtils.containsNullValue(resolvedDependencies)) {
            this.throwForUnexpectedNullDependency(context);
            return null;
         } else {
            Object object = this.runPostConstructHandlers(resolution.instantiateWith(resolvedDependencies), context, resolution);
            if (resolution.isInstantiation() && context.getIdentifier().getResolutionType() == StandardResolutionType.SINGLETON) {
               this.register(context.getOriginalIdentifier().getTypeAsClass(), object);
            }

            return object;
         }
      }
   }

   protected Object[] resolveDependencies(ResolutionContext context, Resolution<?> resolution) {
      int totalDependencies = resolution.getDependencies().size();
      Object[] resolvedDependencies = new Object[totalDependencies];
      int index = 0;

      for(Iterator var6 = resolution.getDependencies().iterator(); var6.hasNext(); ++index) {
         ObjectIdentifier dependencyId = (ObjectIdentifier)var6.next();
         Object dependency = this.resolveContext(context.createChildContext(dependencyId));
         if (dependency == null) {
            break;
         }

         resolvedDependencies[index] = dependency;
      }

      return resolvedDependencies;
   }

   protected void throwForUnexpectedNullDependency(ResolutionContext context) {
      if (context.getIdentifier().getResolutionType() != StandardResolutionType.REQUEST_SCOPED_IF_HAS_DEPENDENCIES && !isContextChildOfOptionalRequest(context)) {
         throw new InjectorException("Found null returned as dependency while resolving '" + context.getIdentifier() + "'");
      }
   }

   private static boolean isContextChildOfOptionalRequest(ResolutionContext context) {
      return !context.getParents().isEmpty() && ((ResolutionContext)context.getParents().get(0)).getIdentifier().getResolutionType() == StandardResolutionType.REQUEST_SCOPED_IF_HAS_DEPENDENCIES;
   }

   protected Resolution<?> findResolutionOrFail(ResolutionContext context) {
      try {
         Iterator var2 = this.config.getHandlers().iterator();

         while(var2.hasNext()) {
            Handler handler = (Handler)var2.next();
            Resolution<?> resolution = handler.resolve(context);
            if (resolution != null) {
               return resolution;
            }
         }
      } catch (Exception var5) {
         InjectorUtils.rethrowException(var5);
      }

      Class<?> clazz = context.getIdentifier().getTypeAsClass();
      if (!InjectorUtils.canInstantiate(clazz)) {
         String hint = clazz.isPrimitive() ? "Primitive types must be provided by default. " : (clazz.isArray() ? "By default, arrays cannot be injected. " : "");
         throw new InjectorException(String.format("Did not find instantiation method for '%s'. %sThis class cannot be instantiated; please check the class or your handlers.", clazz, hint));
      } else {
         throw new InjectorException("Did not find instantiation method for '" + context.getIdentifier().getTypeAsClass() + "'. Make sure your class conforms to one of the registered instantiations. If default: make sure you have a constructor with @Inject or fields with @Inject. Fields with @Inject require the default constructor");
      }
   }

   protected <T> T runPostConstructHandlers(T instance, ResolutionContext context, Resolution<?> resolution) {
      if (!resolution.isInstantiation()) {
         return instance;
      } else {
         Object object = instance;

         Handler handler;
         try {
            for(Iterator var5 = this.config.getHandlers().iterator(); var5.hasNext(); object = InjectorUtils.firstNotNull(handler.postProcess(object, context, resolution), object)) {
               handler = (Handler)var5.next();
            }
         } catch (Exception var7) {
            InjectorUtils.rethrowException(var7);
         }

         return object;
      }
   }
}
