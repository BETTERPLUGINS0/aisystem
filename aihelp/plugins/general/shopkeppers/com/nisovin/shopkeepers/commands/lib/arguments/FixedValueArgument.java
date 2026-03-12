package com.nisovin.shopkeepers.commands.lib.arguments;

import com.nisovin.shopkeepers.commands.lib.CommandInput;
import com.nisovin.shopkeepers.commands.lib.argument.ArgumentParseException;
import com.nisovin.shopkeepers.commands.lib.argument.ArgumentsReader;
import com.nisovin.shopkeepers.commands.lib.argument.CommandArgument;
import com.nisovin.shopkeepers.commands.lib.context.CommandContextView;
import java.util.Collections;
import java.util.List;

public class FixedValueArgument<T> extends CommandArgument<T> {
   private final T fixedValue;

   public FixedValueArgument(String name, T fixedValue) {
      super(name);
      this.fixedValue = fixedValue;
   }

   public boolean isOptional() {
      return true;
   }

   public T parseValue(CommandInput input, CommandContextView context, ArgumentsReader argsReader) throws ArgumentParseException {
      return this.fixedValue;
   }

   public List<? extends String> complete(CommandInput input, CommandContextView context, ArgumentsReader argsReader) {
      return Collections.emptyList();
   }
}
