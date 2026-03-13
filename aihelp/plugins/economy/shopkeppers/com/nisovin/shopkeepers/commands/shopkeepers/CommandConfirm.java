package com.nisovin.shopkeepers.commands.shopkeepers;

import com.nisovin.shopkeepers.commands.Confirmations;
import com.nisovin.shopkeepers.commands.lib.Command;
import com.nisovin.shopkeepers.commands.lib.CommandException;
import com.nisovin.shopkeepers.commands.lib.CommandInput;
import com.nisovin.shopkeepers.commands.lib.context.CommandContextView;
import com.nisovin.shopkeepers.text.Text;

class CommandConfirm extends Command {
   private final Confirmations confirmations;

   CommandConfirm(Confirmations confirmations) {
      super("confirm");
      this.confirmations = confirmations;
      this.setDescription(Text.of("Confirms a potentially dangerous action."));
      this.setHiddenInParentHelp(true);
   }

   protected void execute(CommandInput input, CommandContextView context) throws CommandException {
      this.confirmations.handleConfirmation(input.getSender());
   }
}
