package com.nisovin.shopkeepers.commands.shopkeepers;

import com.nisovin.shopkeepers.api.ShopkeepersAPI;
import com.nisovin.shopkeepers.commands.lib.Command;
import com.nisovin.shopkeepers.commands.lib.CommandException;
import com.nisovin.shopkeepers.commands.lib.CommandInput;
import com.nisovin.shopkeepers.commands.lib.context.CommandContextView;
import com.nisovin.shopkeepers.debug.DebugOptions;
import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.util.bukkit.TextUtils;
import com.nisovin.shopkeepers.util.logging.Log;
import java.util.concurrent.TimeUnit;
import org.bukkit.command.CommandSender;

class CommandUpdateItems extends Command {
   CommandUpdateItems() {
      super("updateItems");
      this.setPermission("shopkeeper.updateitems");
      this.setDescription(Messages.commandDescriptionUpdateItems);
   }

   protected void execute(CommandInput input, CommandContextView context) throws CommandException {
      CommandSender sender = input.getSender();
      long startNanos = System.nanoTime();
      int updatedItems = ShopkeepersAPI.updateItems();
      long durationMillis = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNanos);
      Log.debug(DebugOptions.itemUpdates, "Updated " + updatedItems + " items (" + durationMillis + " ms).");
      TextUtils.sendMessage(sender, Messages.itemsUpdated, "count", updatedItems);
   }
}
