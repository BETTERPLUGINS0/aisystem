package com.nisovin.shopkeepers.commands.lib.arguments;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.commands.lib.CommandInput;
import com.nisovin.shopkeepers.commands.lib.argument.ArgumentParseException;
import com.nisovin.shopkeepers.commands.lib.argument.ArgumentsReader;
import com.nisovin.shopkeepers.commands.lib.argument.CommandArgument;
import com.nisovin.shopkeepers.commands.lib.context.CommandContextView;
import com.nisovin.shopkeepers.util.java.ConversionUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EnumArgument<T extends Enum<T>> extends CommandArgument<T> {
   private final Class<T> clazz;

   public EnumArgument(String name, Class<T> clazz) {
      super(name);
      Validate.notNull(clazz, (String)"clazz is null");
      this.clazz = clazz;
   }

   public T parseValue(CommandInput input, CommandContextView context, ArgumentsReader argsReader) throws ArgumentParseException {
      if (!argsReader.hasNext()) {
         throw this.missingArgumentError();
      } else {
         String argument = argsReader.next();
         T value = ConversionUtils.parseEnum(this.clazz, argument);
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
         String partialArg = argsReader.next().toUpperCase();
         T[] enumValues = (Enum[])Unsafe.assertNonNull((Enum[])this.clazz.getEnumConstants());
         Enum[] var7 = enumValues;
         int var8 = enumValues.length;

         for(int var9 = 0; var9 < var8; ++var9) {
            T value = var7[var9];
            if (suggestions.size() >= 20) {
               break;
            }

            if (value.name().toUpperCase().startsWith(partialArg)) {
               suggestions.add(value.name());
            }
         }

         return Collections.unmodifiableList(suggestions);
      }
   }
}
