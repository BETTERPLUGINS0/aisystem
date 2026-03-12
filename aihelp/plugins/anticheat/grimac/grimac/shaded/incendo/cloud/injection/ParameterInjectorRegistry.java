package ac.grim.grimac.shaded.incendo.cloud.injection;

import ac.grim.grimac.shaded.geantyref.GenericTypeReflector;
import ac.grim.grimac.shaded.geantyref.TypeToken;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandContext;
import ac.grim.grimac.shaded.incendo.cloud.exception.InjectionException;
import ac.grim.grimac.shaded.incendo.cloud.services.ServicePipeline;
import ac.grim.grimac.shaded.incendo.cloud.type.tuple.Pair;
import ac.grim.grimac.shaded.incendo.cloud.util.annotation.AnnotationAccessor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.common.returnsreceiver.qual.This;

@API(
   status = Status.STABLE
)
public final class ParameterInjectorRegistry<C> implements InjectionService<C> {
   private final List<Pair<Predicate<TypeToken<?>>, ParameterInjector<C, ?>>> injectors = new ArrayList();
   private final ServicePipeline servicePipeline = ServicePipeline.builder().build();

   public ParameterInjectorRegistry() {
      this.servicePipeline.registerServiceType(new TypeToken<InjectionService<C>>() {
      }, this);
   }

   @This
   @NonNull
   public synchronized <T> ParameterInjectorRegistry<C> registerInjector(@NonNull final Class<T> clazz, @NonNull final ParameterInjector<C, T> injector) {
      return this.registerInjector(TypeToken.get(clazz), injector);
   }

   @API(
      status = Status.STABLE
   )
   @This
   @NonNull
   public synchronized <T> ParameterInjectorRegistry<C> registerInjector(@NonNull final TypeToken<T> type, @NonNull final ParameterInjector<C, T> injector) {
      return this.registerInjector((cl) -> {
         return GenericTypeReflector.isSuperType(cl.getType(), type.getType());
      }, injector);
   }

   @API(
      status = Status.STABLE
   )
   @This
   @NonNull
   public synchronized <T> ParameterInjectorRegistry<C> registerInjector(@NonNull final Predicate<TypeToken<?>> predicate, @NonNull final ParameterInjector<C, T> injector) {
      this.injectors.add(Pair.of(predicate, injector));
      return this;
   }

   @Nullable
   public Object handle(@NonNull final InjectionRequest<C> request) {
      Iterator var2 = this.injectors(request.injectedType()).iterator();

      Object value;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         ParameterInjector<C, ?> injector = (ParameterInjector)var2.next();
         value = injector.create(request.commandContext(), request.annotationAccessor());
      } while(value == null);

      return value;
   }

   @API(
      status = Status.STABLE
   )
   @NonNull
   public <T> Optional<T> getInjectable(@NonNull final Class<T> clazz, @NonNull final CommandContext<C> context, @NonNull final AnnotationAccessor annotationAccessor) {
      return this.getInjectable(TypeToken.get(clazz), context, annotationAccessor);
   }

   @API(
      status = Status.STABLE
   )
   @NonNull
   public <T> Optional<T> getInjectable(@NonNull final TypeToken<T> type, @NonNull final CommandContext<C> context, @NonNull final AnnotationAccessor annotationAccessor) {
      InjectionRequest request = InjectionRequest.of(context, type, annotationAccessor);

      try {
         Object rawResult = this.servicePipeline.pump(request).through(new TypeToken<InjectionService<C>>() {
         }).complete();
         if (!request.injectedClass().isInstance(rawResult)) {
            throw new IllegalStateException(String.format("Injector returned type %s which is not an instance of %s", rawResult.getClass().getName(), request.injectedClass().getName()));
         } else {
            return Optional.of(rawResult);
         }
      } catch (IllegalStateException var7) {
         return Optional.empty();
      } catch (InjectionException var8) {
         throw var8;
      } catch (Exception var9) {
         throw new InjectionException(String.format("Failed to inject type %s", type.getType().getTypeName()), var9);
      }
   }

   @API(
      status = Status.STABLE
   )
   @This
   @NonNull
   public ParameterInjectorRegistry<C> registerInjectionService(final InjectionService<C> service) {
      this.servicePipeline.registerServiceImplementation((TypeToken)(new TypeToken<InjectionService<C>>() {
      }), service, Collections.emptyList());
      return this;
   }

   @NonNull
   private synchronized <T> Collection<ParameterInjector<C, ?>> injectors(@NonNull final TypeToken<T> type) {
      return Collections.unmodifiableCollection((Collection)this.injectors.stream().filter((pair) -> {
         return ((Predicate)pair.first()).test(type);
      }).map(Pair::second).collect(Collectors.toList()));
   }
}
