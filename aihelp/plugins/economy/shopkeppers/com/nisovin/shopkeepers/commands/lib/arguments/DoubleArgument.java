package com.nisovin.shopkeepers.commands.lib.arguments;

import com.nisovin.shopkeepers.commands.lib.CommandInput;
import com.nisovin.shopkeepers.commands.lib.argument.ArgumentParseException;
import com.nisovin.shopkeepers.commands.lib.argument.ArgumentsReader;
import com.nisovin.shopkeepers.commands.lib.argument.CommandArgument;
import com.nisovin.shopkeepers.commands.lib.context.CommandContextView;
import com.nisovin.shopkeepers.util.java.ConversionUtils;
import java.util.Collections;
import java.util.List;

public class DoubleArgument extends CommandArgument<Double> {
   public DoubleArgument(String name) {
      super(name);
   }

   public Double parseValue(CommandInput input, CommandContextView context, ArgumentsReader argsReader) throws ArgumentParseException {
      if (!argsReader.hasNext()) {
         throw this.missingArgumentError();
      } else {
         String argument = argsReader.next();
         Double value = ConversionUtils.parseDouble(argument);
         if (value == null) {
            throw this.invalidArgumentError(argument);
         } else {
            return value;
         }
      }
   }

   public List<? extends String> complete(CommandInput input, CommandContextView context, ArgumentsReader argsReader) {
      return Collections.emptyList();
   }
}
