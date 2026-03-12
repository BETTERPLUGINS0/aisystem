package ac.grim.grimac.shaded.incendo.cloud.bukkit.parser.selector;

import ac.grim.grimac.shaded.incendo.cloud.bukkit.BukkitCaptionKeys;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandContext;
import ac.grim.grimac.shaded.incendo.cloud.exception.parsing.ParserException;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;

@API(
   status = Status.STABLE,
   since = "2.0.0"
)
public final class SelectorUnsupportedException extends ParserException {
   public SelectorUnsupportedException(@NonNull final CommandContext<?> context, @NonNull final Class<?> parser) {
      super(parser, context, BukkitCaptionKeys.ARGUMENT_PARSE_FAILURE_SELECTOR_UNSUPPORTED);
   }
}
