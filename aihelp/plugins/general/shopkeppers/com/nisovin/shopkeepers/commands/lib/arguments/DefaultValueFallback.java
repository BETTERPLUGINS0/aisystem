package com.nisovin.shopkeepers.commands.lib.arguments;

import com.nisovin.shopkeepers.commands.lib.argument.CommandArgument;
import com.nisovin.shopkeepers.util.java.Validate;

public class DefaultValueFallback<T> extends TypedFallbackArgument<T> {
   public DefaultValueFallback(CommandArgument<T> argument, T defaultValue) {
      super((CommandArgument)Validate.notNull(argument, (String)"argument is null"), new FixedValueArgument(argument.getName(), defaultValue));
   }
}
