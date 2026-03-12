package ac.grim.grimac.shaded.incendo.cloud.execution.preprocessor;

import ac.grim.grimac.shaded.incendo.cloud.context.CommandContext;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandInput;
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
   from = "CommandPreprocessingContext",
   generator = "Immutables"
)
@Immutable
final class CommandPreprocessingContextImpl<C> implements CommandPreprocessingContext<C> {
   @NonNull
   private final CommandContext<C> commandContext;
   @NonNull
   private final CommandInput commandInput;

   private CommandPreprocessingContextImpl(@NonNull CommandContext<C> commandContext, @NonNull CommandInput commandInput) {
      this.commandContext = (CommandContext)Objects.requireNonNull(commandContext, "commandContext");
      this.commandInput = (CommandInput)Objects.requireNonNull(commandInput, "commandInput");
   }

   private CommandPreprocessingContextImpl(CommandPreprocessingContextImpl<C> original, @NonNull CommandContext<C> commandContext, @NonNull CommandInput commandInput) {
      this.commandContext = commandContext;
      this.commandInput = commandInput;
   }

   @NonNull
   public CommandContext<C> commandContext() {
      return this.commandContext;
   }

   @NonNull
   public CommandInput commandInput() {
      return this.commandInput;
   }

   public final CommandPreprocessingContextImpl<C> withCommandContext(CommandContext<C> value) {
      if (this.commandContext == value) {
         return this;
      } else {
         CommandContext<C> newValue = (CommandContext)Objects.requireNonNull(value, "commandContext");
         return new CommandPreprocessingContextImpl(this, newValue, this.commandInput);
      }
   }

   public final CommandPreprocessingContextImpl<C> withCommandInput(CommandInput value) {
      if (this.commandInput == value) {
         return this;
      } else {
         CommandInput newValue = (CommandInput)Objects.requireNonNull(value, "commandInput");
         return new CommandPreprocessingContextImpl(this, this.commandContext, newValue);
      }
   }

   public boolean equals(@Nullable Object another) {
      if (this == another) {
         return true;
      } else {
         return another instanceof CommandPreprocessingContextImpl && this.equalTo(0, (CommandPreprocessingContextImpl)another);
      }
   }

   private boolean equalTo(int synthetic, CommandPreprocessingContextImpl<?> another) {
      return this.commandContext.equals(another.commandContext) && this.commandInput.equals(another.commandInput);
   }

   public int hashCode() {
      int h = 5381;
      int h = h + (h << 5) + this.commandContext.hashCode();
      h += (h << 5) + this.commandInput.hashCode();
      return h;
   }

   public String toString() {
      return "CommandPreprocessingContext{commandContext=" + this.commandContext + ", commandInput=" + this.commandInput + "}";
   }

   public static <C> CommandPreprocessingContextImpl<C> of(@NonNull CommandContext<C> commandContext, @NonNull CommandInput commandInput) {
      return new CommandPreprocessingContextImpl(commandContext, commandInput);
   }

   public static <C> CommandPreprocessingContextImpl<C> copyOf(CommandPreprocessingContext<C> instance) {
      return instance instanceof CommandPreprocessingContextImpl ? (CommandPreprocessingContextImpl)instance : of(instance.commandContext(), instance.commandInput());
   }
}
