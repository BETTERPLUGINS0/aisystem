package ac.grim.grimac.shaded.incendo.cloud.execution.postprocessor;

import ac.grim.grimac.shaded.incendo.cloud.Command;
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
   from = "CommandPostprocessingContext",
   generator = "Immutables"
)
@Immutable
final class CommandPostprocessingContextImpl<C> implements CommandPostprocessingContext<C> {
   @NonNull
   private final CommandContext<C> commandContext;
   @NonNull
   private final Command<C> command;

   private CommandPostprocessingContextImpl(@NonNull CommandContext<C> commandContext, @NonNull Command<C> command) {
      this.commandContext = (CommandContext)Objects.requireNonNull(commandContext, "commandContext");
      this.command = (Command)Objects.requireNonNull(command, "command");
   }

   private CommandPostprocessingContextImpl(CommandPostprocessingContextImpl<C> original, @NonNull CommandContext<C> commandContext, @NonNull Command<C> command) {
      this.commandContext = commandContext;
      this.command = command;
   }

   @NonNull
   public CommandContext<C> commandContext() {
      return this.commandContext;
   }

   @NonNull
   public Command<C> command() {
      return this.command;
   }

   public final CommandPostprocessingContextImpl<C> withCommandContext(CommandContext<C> value) {
      if (this.commandContext == value) {
         return this;
      } else {
         CommandContext<C> newValue = (CommandContext)Objects.requireNonNull(value, "commandContext");
         return new CommandPostprocessingContextImpl(this, newValue, this.command);
      }
   }

   public final CommandPostprocessingContextImpl<C> withCommand(Command<C> value) {
      if (this.command == value) {
         return this;
      } else {
         Command<C> newValue = (Command)Objects.requireNonNull(value, "command");
         return new CommandPostprocessingContextImpl(this, this.commandContext, newValue);
      }
   }

   public boolean equals(@Nullable Object another) {
      if (this == another) {
         return true;
      } else {
         return another instanceof CommandPostprocessingContextImpl && this.equalTo(0, (CommandPostprocessingContextImpl)another);
      }
   }

   private boolean equalTo(int synthetic, CommandPostprocessingContextImpl<?> another) {
      return this.commandContext.equals(another.commandContext) && this.command.equals(another.command);
   }

   public int hashCode() {
      int h = 5381;
      int h = h + (h << 5) + this.commandContext.hashCode();
      h += (h << 5) + this.command.hashCode();
      return h;
   }

   public String toString() {
      return "CommandPostprocessingContext{commandContext=" + this.commandContext + ", command=" + this.command + "}";
   }

   public static <C> CommandPostprocessingContextImpl<C> of(@NonNull CommandContext<C> commandContext, @NonNull Command<C> command) {
      return new CommandPostprocessingContextImpl(commandContext, command);
   }

   public static <C> CommandPostprocessingContextImpl<C> copyOf(CommandPostprocessingContext<C> instance) {
      return instance instanceof CommandPostprocessingContextImpl ? (CommandPostprocessingContextImpl)instance : of(instance.commandContext(), instance.command());
   }
}
