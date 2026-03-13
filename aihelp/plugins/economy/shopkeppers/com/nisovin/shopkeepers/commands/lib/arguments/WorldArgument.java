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
import org.bukkit.Bukkit;
import org.bukkit.World;

public class WorldArgument extends CommandArgument<World> {
   public WorldArgument(String name) {
      super(name);
   }

   public World parseValue(CommandInput input, CommandContextView context, ArgumentsReader argsReader) throws ArgumentParseException {
      if (!argsReader.hasNext()) {
         throw this.missingArgumentError();
      } else {
         String argument = argsReader.next();
         World value = Bukkit.getWorld(argument);
         if (value == null) {
            UUID uuid = ConversionUtils.parseUUID(argument);
            if (uuid != null) {
               value = Bukkit.getWorld(uuid);
            }

            if (value == null) {
               throw this.invalidArgumentError(argument);
            }
         }

         return value;
      }
   }

   public List<? extends String> complete(CommandInput input, CommandContextView context, ArgumentsReader argsReader) {
      return Collections.emptyList();
   }
}
