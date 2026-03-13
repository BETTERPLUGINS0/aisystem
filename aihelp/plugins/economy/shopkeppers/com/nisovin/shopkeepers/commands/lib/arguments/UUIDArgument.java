package com.nisovin.shopkeepers.commands.lib.arguments;

import com.nisovin.shopkeepers.commands.lib.CommandInput;
import com.nisovin.shopkeepers.commands.lib.argument.ArgumentParseException;
import com.nisovin.shopkeepers.commands.lib.argument.ArgumentsReader;
import com.nisovin.shopkeepers.commands.lib.argument.CommandArgument;
import com.nisovin.shopkeepers.commands.lib.context.CommandContextView;
import com.nisovin.shopkeepers.util.java.ConversionUtils;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class UUIDArgument extends CommandArgument<UUID> {
   public UUIDArgument(String name) {
      super(name);
   }

   public UUID parseValue(CommandInput input, CommandContextView context, ArgumentsReader argsReader) throws ArgumentParseException {
      String uuidArg = argsReader.nextIfPresent();
      if (uuidArg == null) {
         throw this.missingArgumentError();
      } else {
         UUID uuid = ConversionUtils.parseUUID(uuidArg);
         if (uuid == null) {
            throw this.invalidArgumentError(uuidArg);
         } else {
            return uuid;
         }
      }
   }

   public List<? extends String> complete(CommandInput input, CommandContextView context, ArgumentsReader argsReader) {
      return Collections.emptyList();
   }
}
