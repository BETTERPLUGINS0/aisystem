package com.nisovin.shopkeepers.commands.lib.commands;

import com.nisovin.shopkeepers.commands.lib.Command;
import com.nisovin.shopkeepers.commands.lib.CommandException;
import com.nisovin.shopkeepers.commands.lib.CommandInput;
import com.nisovin.shopkeepers.commands.lib.context.CommandContextView;
import com.nisovin.shopkeepers.util.java.Validate;
import java.util.Collections;
import java.util.List;

public class HelpCommand extends Command {
   private final Command helpSource;

   public HelpCommand(String name, Command helpSource) {
      this(name, Collections.emptyList(), helpSource);
   }

   public HelpCommand(String name, List<String> aliases, Command helpSource) {
      super(name, aliases);
      Validate.notNull(helpSource, (String)"helpSource is null");
      this.helpSource = helpSource;
   }

   protected void execute(CommandInput input, CommandContextView context) throws CommandException {
      this.helpSource.sendHelp(input.getSender());
   }
}
