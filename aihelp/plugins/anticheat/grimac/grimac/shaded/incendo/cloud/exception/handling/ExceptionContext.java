package ac.grim.grimac.shaded.incendo.cloud.exception.handling;

import ac.grim.grimac.shaded.incendo.cloud.context.CommandContext;
import java.util.Objects;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;

@API(
   status = Status.STABLE
)
public interface ExceptionContext<C, T extends Throwable> {
   @NonNull
   T exception();

   @NonNull
   CommandContext<C> context();

   @NonNull
   ExceptionController<C> controller();

   @API(
      status = Status.INTERNAL
   )
   public static final class ExceptionContextImpl<C, T extends Throwable> implements ExceptionContext<C, T> {
      private final T exception;
      private final CommandContext<C> context;
      private final ExceptionController<C> controller;

      ExceptionContextImpl(@NonNull final T exception, @NonNull final CommandContext<C> context, @NonNull final ExceptionController<C> controller) {
         this.exception = exception;
         this.context = context;
         this.controller = controller;
      }

      @NonNull
      public T exception() {
         return this.exception;
      }

      @NonNull
      public CommandContext<C> context() {
         return this.context;
      }

      @NonNull
      public ExceptionController<C> controller() {
         return this.controller;
      }

      public boolean equals(final Object object) {
         if (this == object) {
            return true;
         } else if (object != null && this.getClass() == object.getClass()) {
            ExceptionContext.ExceptionContextImpl<?, ?> that = (ExceptionContext.ExceptionContextImpl)object;
            return Objects.equals(this.exception, that.exception) && Objects.equals(this.context, that.context) && Objects.equals(this.controller, that.controller);
         } else {
            return false;
         }
      }

      public int hashCode() {
         return Objects.hash(new Object[]{this.exception, this.context, this.controller});
      }
   }
}
