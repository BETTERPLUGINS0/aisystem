package com.nisovin.shopkeepers.commands.lib.arguments;

import com.nisovin.shopkeepers.commands.lib.CommandInput;
import com.nisovin.shopkeepers.commands.lib.argument.ArgumentParseException;
import com.nisovin.shopkeepers.commands.lib.argument.ArgumentsReader;
import com.nisovin.shopkeepers.commands.lib.argument.CommandArgument;
import com.nisovin.shopkeepers.commands.lib.context.CommandContextView;
import com.nisovin.shopkeepers.util.java.ObjectUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import java.util.Collections;
import java.util.List;
import org.bukkit.entity.Player;

public class SenderPlayerFallback extends TypedFallbackArgument<Player> {
   public SenderPlayerFallback(CommandArgument<Player> argument) {
      super((CommandArgument)Validate.notNull(argument, (String)"argument is null"), new SenderPlayerFallback.SenderPlayerArgument(argument.getName()));
   }

   public static class SenderPlayerArgument extends CommandArgument<Player> {
      public SenderPlayerArgument(String name) {
         super(name);
      }

      public boolean isOptional() {
         return true;
      }

      public Player parseValue(CommandInput input, CommandContextView context, ArgumentsReader argsReader) throws ArgumentParseException {
         Player player = (Player)ObjectUtils.castOrNull(input.getSender(), Player.class);
         if (player == null) {
            throw this.requiresPlayerError();
         } else {
            return player;
         }
      }

      public List<? extends String> complete(CommandInput input, CommandContextView context, ArgumentsReader argsReader) {
         return Collections.emptyList();
      }
   }
}
