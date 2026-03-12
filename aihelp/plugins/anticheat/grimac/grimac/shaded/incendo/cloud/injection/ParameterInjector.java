package ac.grim.grimac.shaded.incendo.cloud.injection;

import ac.grim.grimac.shaded.incendo.cloud.context.CommandContext;
import ac.grim.grimac.shaded.incendo.cloud.util.annotation.AnnotationAccessor;
import java.util.Objects;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

@FunctionalInterface
@API(
   status = Status.STABLE
)
public interface ParameterInjector<C, T> {
   @API(
      status = Status.STABLE
   )
   @NonNull
   static <C, T> ParameterInjector<C, T> constantInjector(@NonNull final T value) {
      return new ParameterInjector.ConstantInjector(value);
   }

   @Nullable
   T create(@NonNull CommandContext<C> context, @NonNull AnnotationAccessor annotationAccessor);

   public static final class ConstantInjector<C, T> implements ParameterInjector<C, T> {
      private final T value;

      private ConstantInjector(@NonNull final T value) {
         this.value = value;
      }

      @NonNull
      public T create(@NonNull final CommandContext<C> context, @NonNull final AnnotationAccessor annotationAccessor) {
         return this.value;
      }

      public boolean equals(final Object o) {
         if (this == o) {
            return true;
         } else if (o != null && this.getClass() == o.getClass()) {
            ParameterInjector.ConstantInjector<?, ?> that = (ParameterInjector.ConstantInjector)o;
            return Objects.equals(this.value, that.value);
         } else {
            return false;
         }
      }

      public int hashCode() {
         return Objects.hash(new Object[]{this.value});
      }

      public String toString() {
         return "ConstantInjector{value=" + this.value + '}';
      }

      // $FF: synthetic method
      ConstantInjector(Object x0, Object x1) {
         this(x0);
      }
   }
}
