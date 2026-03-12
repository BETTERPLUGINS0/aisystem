package ac.grim.grimac.shaded.incendo.cloud.component.preprocessor;

import ac.grim.grimac.shaded.incendo.cloud.context.CommandContext;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandInput;
import ac.grim.grimac.shaded.incendo.cloud.parser.ArgumentParseResult;
import java.util.Objects;
import java.util.function.BiFunction;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;

@API(
   status = Status.STABLE
)
@FunctionalInterface
public interface ComponentPreprocessor<C> {
   @NonNull
   static <C> ComponentPreprocessor<C> wrap(@NonNull final BiFunction<CommandContext<C>, CommandInput, ArgumentParseResult<Boolean>> function) {
      Objects.requireNonNull(function);
      return function::apply;
   }

   @NonNull
   ArgumentParseResult<Boolean> preprocess(@NonNull CommandContext<C> context, @NonNull CommandInput commandInput);
}
