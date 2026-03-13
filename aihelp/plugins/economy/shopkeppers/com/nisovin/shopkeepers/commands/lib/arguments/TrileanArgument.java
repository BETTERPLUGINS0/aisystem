package com.nisovin.shopkeepers.commands.lib.arguments;

import com.nisovin.shopkeepers.commands.lib.CommandInput;
import com.nisovin.shopkeepers.commands.lib.argument.ArgumentParseException;
import com.nisovin.shopkeepers.commands.lib.argument.ArgumentsReader;
import com.nisovin.shopkeepers.commands.lib.argument.CommandArgument;
import com.nisovin.shopkeepers.commands.lib.context.CommandContextView;
import com.nisovin.shopkeepers.util.java.ConversionUtils;
import com.nisovin.shopkeepers.util.java.Trilean;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class TrileanArgument extends CommandArgument<Trilean> {
   public TrileanArgument(String name) {
      super(name);
   }

   public Trilean parseValue(CommandInput input, CommandContextView context, ArgumentsReader argsReader) throws ArgumentParseException {
      if (!argsReader.hasNext()) {
         throw this.missingArgumentError();
      } else {
         String argument = argsReader.next();
         Trilean value = ConversionUtils.parseTrilean(argument);
         if (value == null) {
            throw this.invalidArgumentError(argument);
         } else {
            return value;
         }
      }
   }

   public List<? extends String> complete(CommandInput input, CommandContextView context, ArgumentsReader argsReader) {
      if (argsReader.getRemainingSize() != 1) {
         return Collections.emptyList();
      } else {
         List<String> suggestions = new ArrayList();
         String partialArg = argsReader.next().toLowerCase(Locale.ROOT);
         Iterator var6 = ConversionUtils.TRILEAN_VALUES.keySet().iterator();

         while(var6.hasNext()) {
            String value = (String)var6.next();
            if (suggestions.size() >= 20) {
               break;
            }

            if (value.startsWith(partialArg)) {
               suggestions.add(value);
            }
         }

         return Collections.unmodifiableList(suggestions);
      }
   }
}
