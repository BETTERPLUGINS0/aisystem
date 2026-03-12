package ac.grim.grimac.shaded.incendo.cloud.execution;

import ac.grim.grimac.shaded.incendo.cloud.context.CommandContext;
import java.util.Objects;
import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.Immutable;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.immutables.value.Generated;

@ParametersAreNonnullByDefault
@CheckReturnValue
@API(
   status = Status.INTERNAL,
   consumers = {"ac.grim.grimac.shaded.incendo.cloud.*"}
)
@Generated(
   from = "CommandResult",
   generator = "Immutables"
)
@Immutable
final class CommandResultImpl<C> implements CommandResult<C> {
   @NonNull
   private final CommandContext<C> commandContext;

   private CommandResultImpl(@NonNull CommandContext<C> commandContext) {
      this.commandContext = (CommandContext)Objects.requireNonNull(commandContext, "commandContext");
   }

   private CommandResultImpl(CommandResultImpl<C> original, @NonNull CommandContext<C> commandContext) {
      this.commandContext = commandContext;
   }

   @NonNull
   public CommandContext<C> commandContext() {
      return this.commandContext;
   }

   public final CommandResultImpl<C> withCommandContext(CommandContext<C> value) {
      if (this.commandContext == value) {
         return this;
      } else {
         CommandContext<C> newValue = (CommandContext)Objects.requireNonNull(value, "commandContext");
         return new CommandResultImpl(this, newValue);
      }
   }

   public boolean equals(@Nullable Object another) {
      if (this == another) {
         return true;
      } else {
         return another instanceof CommandResultImpl && this.equalTo(0, (CommandResultImpl)another);
      }
   }

   private boolean equalTo(int synthetic, CommandResultImpl<?> another) {
      return this.commandContext.equals(another.commandContext);
   }

   public int hashCode() {
      int h = 5381;
      int h = h + (h << 5) + this.commandContext.hashCode();
      return h;
   }

   public String toString() {
      return "CommandResult{commandContext=" + this.commandContext + "}";
   }

   public static <C> CommandResultImpl<C> of(@NonNull CommandContext<C> commandContext) {
      return new CommandResultImpl(commandContext);
   }

   public static <C> CommandResultImpl<C> copyOf(CommandResult<C> instance) {
      return instance instanceof CommandResultImpl ? (CommandResultImpl)instance : of(instance.commandContext());
   }
}
