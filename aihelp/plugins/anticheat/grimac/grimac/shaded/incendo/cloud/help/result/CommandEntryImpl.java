package ac.grim.grimac.shaded.incendo.cloud.help.result;

import ac.grim.grimac.shaded.incendo.cloud.Command;
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
   from = "CommandEntry",
   generator = "Immutables"
)
@Immutable
final class CommandEntryImpl<C> implements CommandEntry<C> {
   @NonNull
   private final Command<C> command;
   @NonNull
   private final String syntax;

   private CommandEntryImpl(@NonNull Command<C> command, @NonNull String syntax) {
      this.command = (Command)Objects.requireNonNull(command, "command");
      this.syntax = (String)Objects.requireNonNull(syntax, "syntax");
   }

   private CommandEntryImpl(CommandEntryImpl<C> original, @NonNull Command<C> command, @NonNull String syntax) {
      this.command = command;
      this.syntax = syntax;
   }

   @NonNull
   public Command<C> command() {
      return this.command;
   }

   @NonNull
   public String syntax() {
      return this.syntax;
   }

   public final CommandEntryImpl<C> withCommand(Command<C> value) {
      if (this.command == value) {
         return this;
      } else {
         Command<C> newValue = (Command)Objects.requireNonNull(value, "command");
         return new CommandEntryImpl(this, newValue, this.syntax);
      }
   }

   public final CommandEntryImpl<C> withSyntax(String value) {
      String newValue = (String)Objects.requireNonNull(value, "syntax");
      return this.syntax.equals(newValue) ? this : new CommandEntryImpl(this, this.command, newValue);
   }

   public boolean equals(@Nullable Object another) {
      if (this == another) {
         return true;
      } else {
         return another instanceof CommandEntryImpl && this.equalTo(0, (CommandEntryImpl)another);
      }
   }

   private boolean equalTo(int synthetic, CommandEntryImpl<?> another) {
      return this.command.equals(another.command) && this.syntax.equals(another.syntax);
   }

   public int hashCode() {
      int h = 5381;
      int h = h + (h << 5) + this.command.hashCode();
      h += (h << 5) + this.syntax.hashCode();
      return h;
   }

   public String toString() {
      return "CommandEntry{command=" + this.command + ", syntax=" + this.syntax + "}";
   }

   public static <C> CommandEntryImpl<C> of(@NonNull Command<C> command, @NonNull String syntax) {
      return new CommandEntryImpl(command, syntax);
   }

   public static <C> CommandEntryImpl<C> copyOf(CommandEntry<C> instance) {
      return instance instanceof CommandEntryImpl ? (CommandEntryImpl)instance : of(instance.command(), instance.syntax());
   }
}
