package com.nisovin.shopkeepers.commands.shopkeepers;

import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;
import com.nisovin.shopkeepers.api.ui.DefaultUITypes;
import com.nisovin.shopkeepers.commands.arguments.ShopkeeperArgument;
import com.nisovin.shopkeepers.commands.arguments.ShopkeeperFilter;
import com.nisovin.shopkeepers.commands.arguments.TargetShopkeeperFallback;
import com.nisovin.shopkeepers.commands.lib.Command;
import com.nisovin.shopkeepers.commands.lib.CommandException;
import com.nisovin.shopkeepers.commands.lib.CommandInput;
import com.nisovin.shopkeepers.commands.lib.arguments.PlayerArgument;
import com.nisovin.shopkeepers.commands.lib.arguments.SenderPlayerFallback;
import com.nisovin.shopkeepers.commands.lib.context.CommandContextView;
import com.nisovin.shopkeepers.commands.util.ShopkeeperArgumentUtils;
import com.nisovin.shopkeepers.lang.Messages;
import java.util.Arrays;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

class CommandRemote extends Command {
   private static final String ARGUMENT_SHOPKEEPER = "shopkeeper";
   private static final String ARGUMENT_PLAYER = "player";

   CommandRemote() {
      super("remote", Arrays.asList("open"));
      this.setPermission("shopkeeper.remote");
      this.setDescription(Messages.commandDescriptionRemote);
      this.addArgument(new TargetShopkeeperFallback(new ShopkeeperArgument("shopkeeper", ShopkeeperFilter.withAccess(DefaultUITypes.TRADING())), ShopkeeperArgumentUtils.TargetShopkeeperFilter.ANY));
      this.addArgument(new SenderPlayerFallback(new PlayerArgument("player")));
   }

   protected void execute(CommandInput input, CommandContextView context) throws CommandException {
      CommandSender sender = input.getSender();
      Shopkeeper shopkeeper = (Shopkeeper)context.get("shopkeeper");
      Player targetPlayer = (Player)context.get("player");

      assert targetPlayer != null;

      if (targetPlayer != sender) {
         this.checkPermission(sender, "shopkeeper.remote.otherplayers");
      }

      shopkeeper.openTradingWindow(targetPlayer);
   }
}
