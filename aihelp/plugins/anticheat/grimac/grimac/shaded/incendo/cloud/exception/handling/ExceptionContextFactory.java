package ac.grim.grimac.shaded.incendo.cloud.exception.handling;

import ac.grim.grimac.shaded.incendo.cloud.context.CommandContext;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;

@API(
   status = Status.INTERNAL
)
public final class ExceptionContextFactory<C> {
   private final ExceptionController<C> controller;

   public ExceptionContextFactory(@NonNull final ExceptionController<C> controller) {
      this.controller = controller;
   }

   @NonNull
   public <T extends Throwable> ExceptionContext<C, T> createContext(@NonNull final CommandContext<C> context, @NonNull final T exception) {
      return new ExceptionContext.ExceptionContextImpl(exception, context, this.controller);
   }
}
