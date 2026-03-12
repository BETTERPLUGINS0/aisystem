package com.nisovin.shopkeepers.commands.shopkeepers;

import com.nisovin.shopkeepers.SKShopkeepersPlugin;
import com.nisovin.shopkeepers.commands.lib.Command;
import com.nisovin.shopkeepers.commands.lib.CommandException;
import com.nisovin.shopkeepers.commands.lib.CommandInput;
import com.nisovin.shopkeepers.commands.lib.context.CommandContextView;
import com.nisovin.shopkeepers.shopobjects.citizens.CitizensShops;
import com.nisovin.shopkeepers.text.Text;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

class CommandCleanupCitizenShopkeepers extends Command {
   CommandCleanupCitizenShopkeepers() {
      super("cleanupCitizenShopkeepers");
      this.setPermission("shopkeeper.cleanup-citizen-shopkeepers");
      this.setDescription(Text.of("Deletes invalid Citizen shopkeepers."));
      this.setHiddenInParentHelp(true);
   }

   protected void execute(CommandInput input, CommandContextView context) throws CommandException {
      CommandSender sender = input.getSender();
      CitizensShops citizensShops = SKShopkeepersPlugin.getInstance().getCitizensShops();
      int deleted = citizensShops.validateCitizenShopkeepers(true, false);
      String var10001 = String.valueOf(ChatColor.GREEN);
      sender.sendMessage(var10001 + "Deleted " + String.valueOf(ChatColor.YELLOW) + deleted + String.valueOf(ChatColor.GREEN) + " invalid Citizen shopkeepers!");
   }
}
