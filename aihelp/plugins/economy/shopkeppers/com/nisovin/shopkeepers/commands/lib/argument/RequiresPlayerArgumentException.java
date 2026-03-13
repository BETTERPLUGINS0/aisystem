package com.nisovin.shopkeepers.commands.lib.argument;

import com.nisovin.shopkeepers.text.Text;
import org.checkerframework.checker.nullness.qual.Nullable;

public class RequiresPlayerArgumentException extends ArgumentParseException {
   private static final long serialVersionUID = 8158065171648348988L;

   public RequiresPlayerArgumentException(CommandArgument<?> argument, Text message) {
      this(argument, message, (Throwable)null);
   }

   public RequiresPlayerArgumentException(CommandArgument<?> argument, Text message, @Nullable Throwable cause) {
      super(argument, message, cause);
   }
}
