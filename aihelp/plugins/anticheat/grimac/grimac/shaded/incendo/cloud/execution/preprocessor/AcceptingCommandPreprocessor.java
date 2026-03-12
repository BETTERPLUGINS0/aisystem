package ac.grim.grimac.shaded.incendo.cloud.execution.preprocessor;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;

@API(
   status = Status.INTERNAL,
   consumers = {"ac.grim.grimac.shaded.incendo.cloud.*"}
)
public final class AcceptingCommandPreprocessor<C> implements CommandPreprocessor<C> {
   public static final String PROCESSED_INDICATOR_KEY = "__COMMAND_PRE_PROCESSED__";

   public void accept(@NonNull final CommandPreprocessingContext<C> context) {
      context.commandContext().store((String)"__COMMAND_PRE_PROCESSED__", "true");
   }
}
