package ac.grim.grimac.shaded.incendo.cloud.services;

import ac.grim.grimac.shaded.geantyref.TypeToken;
import ac.grim.grimac.shaded.incendo.cloud.services.type.Service;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Executor;
import java.util.function.Predicate;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class ServicePipeline {
   private final Object lock = new Object();
   private final Map<Type, ServiceRepository<?, ?>> repositories = new HashMap();
   private final Executor executor;

   ServicePipeline(@NonNull final Executor executor) {
      this.executor = executor;
   }

   @NonNull
   public static ServicePipelineBuilder builder() {
      return new ServicePipelineBuilder();
   }

   @NonNull
   public <Context, Result> ServicePipeline registerServiceType(@NonNull final TypeToken<? extends Service<Context, Result>> type, @NonNull final Service<Context, Result> defaultImplementation) {
      synchronized(this.lock) {
         if (this.repositories.containsKey(type.getType())) {
            throw new IllegalArgumentException(String.format("Service of type '%s' has already been registered", type.getType().getTypeName()));
         } else {
            ServiceRepository<Context, Result> repository = new ServiceRepository(type);
            repository.registerImplementation(defaultImplementation, Collections.emptyList());
            this.repositories.put(type.getType(), repository);
            return this;
         }
      }
   }

   @NonNull
   public <T> ServicePipeline registerMethods(@NonNull final T instance) throws Exception {
      synchronized(this.lock) {
         Map<? extends Service<?, ?>, TypeToken<? extends Service<?, ?>>> services = AnnotatedMethodServiceFactory.INSTANCE.lookupServices(instance);
         Iterator var4 = services.entrySet().iterator();

         while(var4.hasNext()) {
            Entry<? extends Service<?, ?>, TypeToken<? extends Service<?, ?>>> serviceEntry = (Entry)var4.next();
            TypeToken<? extends Service<?, ?>> type = (TypeToken)serviceEntry.getValue();
            ServiceRepository<?, ?> repository = (ServiceRepository)this.repositories.get(type.getType());
            if (repository == null) {
               throw new IllegalArgumentException(String.format("No service registered for type '%s'", type.getType().getTypeName()));
            }

            repository.registerImplementation((Service)serviceEntry.getKey(), Collections.emptyList());
         }

         return this;
      }
   }

   public <Context, Result> ServicePipeline registerServiceImplementation(@NonNull final TypeToken<? extends Service<Context, Result>> type, @NonNull final Service<Context, Result> implementation, @NonNull final Collection<Predicate<Context>> filters) {
      synchronized(this.lock) {
         ServiceRepository<Context, Result> repository = this.getRepository(type);
         repository.registerImplementation(implementation, filters);
         return this;
      }
   }

   public <Context, Result> ServicePipeline registerServiceImplementation(@NonNull final Class<? extends Service<Context, Result>> type, @NonNull final Service<Context, Result> implementation, @NonNull final Collection<Predicate<Context>> filters) {
      return this.registerServiceImplementation(TypeToken.get(type), implementation, filters);
   }

   @NonNull
   public <Context> ServicePump<Context> pump(@NonNull final Context context) {
      return new ServicePump(this, context);
   }

   @NonNull
   <Context, Result> ServiceRepository<Context, Result> getRepository(@NonNull final TypeToken<? extends Service<Context, Result>> type) {
      ServiceRepository<Context, Result> repository = (ServiceRepository)this.repositories.get(type.getType());
      if (repository == null) {
         throw new IllegalArgumentException(String.format("No service registered for type '%s'", type.getType().getTypeName()));
      } else {
         return repository;
      }
   }

   @NonNull
   public Collection<Type> recognizedTypes() {
      return Collections.unmodifiableCollection(this.repositories.keySet());
   }

   @NonNull
   public <Context, Result, S extends Service<Context, Result>> Collection<TypeToken<? extends S>> getImplementations(@NonNull final TypeToken<S> type) {
      ServiceRepository<Context, Result> repository = this.getRepository(type);
      List<TypeToken<? extends S>> collection = new LinkedList();
      LinkedList<? extends ServiceRepository<Context, Result>.ServiceWrapper<? extends Service<Context, Result>>> queue = repository.queue();
      queue.sort((Comparator)null);
      Collections.reverse(queue);
      Iterator var5 = queue.iterator();

      while(var5.hasNext()) {
         ServiceRepository<Context, Result>.ServiceWrapper<? extends Service<Context, Result>> wrapper = (ServiceRepository.ServiceWrapper)var5.next();
         collection.add(TypeToken.get(wrapper.implementation().getClass()));
      }

      return Collections.unmodifiableList(collection);
   }

   @NonNull
   Executor executor() {
      return this.executor;
   }
}
