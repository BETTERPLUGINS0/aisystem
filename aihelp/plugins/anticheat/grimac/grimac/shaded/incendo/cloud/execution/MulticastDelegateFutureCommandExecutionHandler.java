package ac.grim.grimac.shaded.incendo.cloud.execution;

import ac.grim.grimac.shaded.incendo.cloud.context.CommandContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;

@API(
   status = Status.INTERNAL
)
final class MulticastDelegateFutureCommandExecutionHandler<C> implements CommandExecutionHandler.FutureCommandExecutionHandler<C> {
   private final List<CommandExecutionHandler<C>> handlers;

   MulticastDelegateFutureCommandExecutionHandler(@NonNull final List<CommandExecutionHandler<C>> handlers) {
      List<CommandExecutionHandler<C>> unwrappedHandlers = new ArrayList();
      Iterator var3 = handlers.iterator();

      while(var3.hasNext()) {
         CommandExecutionHandler<C> handler = (CommandExecutionHandler)var3.next();
         if (handler instanceof MulticastDelegateFutureCommandExecutionHandler) {
            unwrappedHandlers.addAll(((MulticastDelegateFutureCommandExecutionHandler)handler).handlers);
         } else {
            unwrappedHandlers.add(handler);
         }
      }

      this.handlers = Collections.unmodifiableList(unwrappedHandlers);
   }

   public CompletableFuture<Void> executeFuture(final CommandContext<C> commandContext) {
      CompletableFuture<Void> composedHandler = null;
      if (this.handlers.isEmpty()) {
         composedHandler = CompletableFuture.completedFuture((Object)null);
      } else {
         Iterator var3 = this.handlers.iterator();

         while(var3.hasNext()) {
            CommandExecutionHandler<C> handler = (CommandExecutionHandler)var3.next();
            if (composedHandler == null) {
               composedHandler = handler.executeFuture(commandContext);
            } else {
               composedHandler = composedHandler.thenCompose((ignore) -> {
                  return handler.executeFuture(commandContext);
               });
            }
         }
      }

      return composedHandler;
   }
}
