package com.nisovin.shopkeepers.commands.lib.arguments;

import com.nisovin.shopkeepers.commands.lib.CommandInput;
import com.nisovin.shopkeepers.commands.lib.argument.ArgumentParseException;
import com.nisovin.shopkeepers.commands.lib.argument.ArgumentsReader;
import com.nisovin.shopkeepers.commands.lib.argument.CommandArgument;
import com.nisovin.shopkeepers.commands.lib.context.CommandContextView;
import com.nisovin.shopkeepers.util.java.CollectionUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class FixedValuesArgument extends CommandArgument<Object> {
   private final Map<? extends String, ?> values;

   public FixedValuesArgument(String name, Map<? extends String, ?> values) {
      super(name);
      Validate.notNull(values, (String)"values is null");
      Validate.isTrue(!CollectionUtils.containsNull(values.keySet()), "values contains a null key");
      Validate.isTrue(!CollectionUtils.containsNull(values.values()), "values contains a null value");
      this.values = values;
   }

   public Object parseValue(CommandInput input, CommandContextView context, ArgumentsReader argsReader) throws ArgumentParseException {
      if (!argsReader.hasNext()) {
         throw this.missingArgumentError();
      } else {
         String argument = argsReader.next();
         Object value = this.values.get(argument);
         if (value == null) {
            value = this.values.get(argument.toLowerCase(Locale.ROOT));
            if (value == null) {
               value = this.values.get(argument.toUpperCase(Locale.ROOT));
               if (value == null) {
                  throw this.invalidArgumentError(argument);
               }
            }
         }

         return value;
      }
   }

   public List<? extends String> complete(CommandInput input, CommandContextView context, ArgumentsReader argsReader) {
      if (argsReader.getRemainingSize() != 1) {
         return Collections.emptyList();
      } else {
         List<String> suggestions = new ArrayList();
         String partialArg = argsReader.next().toLowerCase(Locale.ROOT);
         Iterator var6 = this.values.keySet().iterator();

         while(var6.hasNext()) {
            String valueKey = (String)var6.next();
            if (suggestions.size() >= 20) {
               break;
            }

            if (valueKey.toLowerCase(Locale.ROOT).startsWith(partialArg)) {
               suggestions.add(valueKey);
            }
         }

         return Collections.unmodifiableList(suggestions);
      }
   }
}
