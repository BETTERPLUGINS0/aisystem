package com.nisovin.shopkeepers.commands.lib.argument.fallback;

import com.nisovin.shopkeepers.commands.lib.CommandInput;
import com.nisovin.shopkeepers.commands.lib.argument.ArgumentParseException;
import com.nisovin.shopkeepers.commands.lib.argument.ArgumentsReader;
import com.nisovin.shopkeepers.commands.lib.argument.CommandArgument;
import com.nisovin.shopkeepers.commands.lib.context.CommandContext;

public abstract class FallbackArgument<T> extends CommandArgument<T> {
   public FallbackArgument(String name) {
      super(name);
   }

   public abstract T parseFallback(CommandInput var1, CommandContext var2, ArgumentsReader var3, FallbackArgumentException var4, boolean var5) throws ArgumentParseException;
}
