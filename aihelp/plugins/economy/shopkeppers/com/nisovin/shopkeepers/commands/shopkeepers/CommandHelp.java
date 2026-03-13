package com.nisovin.shopkeepers.commands.shopkeepers;

import com.nisovin.shopkeepers.commands.lib.Command;
import com.nisovin.shopkeepers.commands.lib.commands.HelpCommand;
import com.nisovin.shopkeepers.lang.Messages;
import java.util.Arrays;

class CommandHelp extends HelpCommand {
   CommandHelp(Command helpSource) {
      super("help", Arrays.asList("?"), helpSource);
      this.setPermission("shopkeeper.help");
      this.setDescription(Messages.commandDescriptionHelp);
   }
}
