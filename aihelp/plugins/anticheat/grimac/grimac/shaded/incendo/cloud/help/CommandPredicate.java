package ac.grim.grimac.shaded.incendo.cloud.help;

import ac.grim.grimac.shaded.incendo.cloud.Command;
import java.util.function.Predicate;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;

@FunctionalInterface
@API(
   status = Status.STABLE
)
public interface CommandPredicate<C> extends Predicate<Command<C>> {
   @NonNull
   static <C> CommandPredicate<C> acceptAll() {
      return (cmd) -> {
         return true;
      };
   }

   boolean test(@NonNull Command<C> command);
}
