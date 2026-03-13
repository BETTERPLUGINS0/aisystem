package com.nisovin.shopkeepers.commands.lib.argument;

import com.nisovin.shopkeepers.text.Text;
import org.checkerframework.checker.nullness.qual.Nullable;

public class InvalidArgumentException extends ArgumentParseException {
   private static final long serialVersionUID = -5970457037035687469L;

   public InvalidArgumentException(CommandArgument<?> argument, Text message) {
      this(argument, message, (Throwable)null);
   }

   public InvalidArgumentException(CommandArgument<?> argument, Text message, @Nullable Throwable cause) {
      super(argument, message, cause);
   }
}
