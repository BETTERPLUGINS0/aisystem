package ac.grim.grimac.shaded.incendo.cloud.injection;

import ac.grim.grimac.shaded.geantyref.GenericTypeReflector;
import ac.grim.grimac.shaded.geantyref.TypeToken;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandContext;
import ac.grim.grimac.shaded.incendo.cloud.util.annotation.AnnotationAccessor;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.immutables.value.Value.Derived;
import org.immutables.value.Value.Immutable;

@API(
   status = Status.STABLE
)
@Immutable
public interface InjectionRequest<C> {
   @NonNull
   static <C> InjectionRequest<C> of(@NonNull final CommandContext<C> context, @NonNull final TypeToken<?> injectedType, @NonNull final AnnotationAccessor annotationAccessor) {
      return InjectionRequestImpl.of(context, injectedType, annotationAccessor);
   }

   @NonNull
   static <C> InjectionRequest<C> of(@NonNull final CommandContext<C> context, @NonNull final TypeToken<?> injectedType) {
      return InjectionRequestImpl.of(context, injectedType, AnnotationAccessor.empty());
   }

   @NonNull
   CommandContext<C> commandContext();

   @NonNull
   TypeToken<?> injectedType();

   @Derived
   @NonNull
   default Class<?> injectedClass() {
      return GenericTypeReflector.erase(this.injectedType().getType());
   }

   @NonNull
   AnnotationAccessor annotationAccessor();
}
