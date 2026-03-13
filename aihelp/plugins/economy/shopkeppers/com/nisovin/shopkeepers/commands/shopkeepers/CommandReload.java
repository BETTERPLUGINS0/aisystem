package com.nisovin.shopkeepers.commands.shopkeepers;

import com.nisovin.shopkeepers.SKShopkeepersPlugin;
import com.nisovin.shopkeepers.commands.lib.Command;
import com.nisovin.shopkeepers.commands.lib.CommandException;
import com.nisovin.shopkeepers.commands.lib.CommandInput;
import com.nisovin.shopkeepers.commands.lib.context.CommandContextView;
import com.nisovin.shopkeepers.lang.Messages;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

class CommandReload extends Command {
   private final SKShopkeepersPlugin plugin;

   CommandReload(SKShopkeepersPlugin plugin) {
      super("reload");
      this.plugin = plugin;
      this.setPermission("shopkeeper.reload");
      this.setDescription(Messages.commandDescriptionReload);
   }

   protected void execute(CommandInput input, CommandContextView context) throws CommandException {
      CommandSender sender = input.getSender();
      this.plugin.reload();
      sender.sendMessage(String.valueOf(ChatColor.GREEN) + "Shopkeepers plugin reloaded!");
   }
}
