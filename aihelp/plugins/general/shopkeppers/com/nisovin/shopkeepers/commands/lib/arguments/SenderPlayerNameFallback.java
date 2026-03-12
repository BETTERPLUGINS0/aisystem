package com.nisovin.shopkeepers.commands.lib.arguments;

import com.nisovin.shopkeepers.commands.lib.CommandInput;
import com.nisovin.shopkeepers.commands.lib.argument.ArgumentParseException;
import com.nisovin.shopkeepers.commands.lib.argument.ArgumentsReader;
import com.nisovin.shopkeepers.commands.lib.argument.CommandArgument;
import com.nisovin.shopkeepers.commands.lib.context.CommandContextView;
import com.nisovin.shopkeepers.util.java.Validate;
import java.util.Collections;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SenderPlayerNameFallback extends TypedFallbackArgument<String> {
   public SenderPlayerNameFallback(CommandArgument<String> argument) {
      super((CommandArgument)Validate.notNull(argument, (String)"argument is null"), new SenderPlayerNameFallback.SenderPlayerNameArgument(argument.getName()));
   }

   public static class SenderPlayerNameArgument extends CommandArgument<String> {
      public SenderPlayerNameArgument(String name) {
         super(name);
      }

      public boolean isOptional() {
         return true;
      }

      public String parseValue(CommandInput input, CommandContextView context, ArgumentsReader argsReader) throws ArgumentParseException {
         CommandSender sender = input.getSender();
         if (!(sender instanceof Player)) {
            throw this.requiresPlayerError();
         } else {
            return sender.getName();
         }
      }

      public List<? extends String> complete(CommandInput input, CommandContextView context, ArgumentsReader argsReader) {
         return Collections.emptyList();
      }
   }
}
