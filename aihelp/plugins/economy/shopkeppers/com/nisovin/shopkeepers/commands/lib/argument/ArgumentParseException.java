package com.nisovin.shopkeepers.commands.lib.argument;

import com.nisovin.shopkeepers.commands.lib.CommandException;
import com.nisovin.shopkeepers.text.Text;
import org.checkerframework.checker.nullness.qual.Nullable;

public class ArgumentParseException extends CommandException {
   private static final long serialVersionUID = -4968777515685479426L;
   @Nullable
   private final CommandArgument<?> argument;

   public ArgumentParseException(@Nullable CommandArgument<?> argument, Text message) {
      this(argument, message, (Throwable)null);
   }

   public ArgumentParseException(@Nullable CommandArgument<?> argument, Text message, @Nullable Throwable cause) {
      super(message, cause);
      this.argument = argument;
   }

   @Nullable
   public CommandArgument<?> getArgument() {
      return this.argument;
   }
}
