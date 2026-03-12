package com.nisovin.shopkeepers.commands.lib.arguments;

import com.nisovin.shopkeepers.commands.lib.argument.CommandArgument;
import com.nisovin.shopkeepers.util.java.Validate;

public class AnyStringFallback extends TypedFallbackArgument<String> {
   public AnyStringFallback(CommandArgument<String> argument) {
      this(argument, false);
   }

   public AnyStringFallback(CommandArgument<String> argument, boolean joinRemainingArgs) {
      super((CommandArgument)Validate.notNull(argument, (String)"argument is null"), new StringArgument(argument.getName(), joinRemainingArgs));
   }
}
