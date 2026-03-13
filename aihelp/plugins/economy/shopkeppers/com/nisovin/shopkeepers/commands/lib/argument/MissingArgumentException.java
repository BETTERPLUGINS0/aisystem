package com.nisovin.shopkeepers.commands.lib.argument;

import com.nisovin.shopkeepers.text.Text;
import org.checkerframework.checker.nullness.qual.Nullable;

public class MissingArgumentException extends ArgumentParseException {
   private static final long serialVersionUID = -3269722516077651284L;

   public MissingArgumentException(CommandArgument<?> argument, Text message) {
      this(argument, message, (Throwable)null);
   }

   public MissingArgumentException(CommandArgument<?> argument, Text message, @Nullable Throwable cause) {
      super(argument, message, cause);
   }
}
