package com.nisovin.shopkeepers.commands.shopkeepers;

import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;
import com.nisovin.shopkeepers.api.ui.DefaultUITypes;
import com.nisovin.shopkeepers.commands.arguments.ShopkeeperArgument;
import com.nisovin.shopkeepers.commands.arguments.ShopkeeperFilter;
import com.nisovin.shopkeepers.commands.arguments.TargetShopkeeperFallback;
import com.nisovin.shopkeepers.commands.lib.Command;
import com.nisovin.shopkeepers.commands.lib.CommandException;
import com.nisovin.shopkeepers.commands.lib.CommandInput;
import com.nisovin.shopkeepers.commands.lib.arguments.LiteralArgument;
import com.nisovin.shopkeepers.commands.lib.arguments.PlayerArgument;
import com.nisovin.shopkeepers.commands.lib.arguments.SenderPlayerFallback;
import com.nisovin.shopkeepers.commands.lib.context.CommandContextView;
import com.nisovin.shopkeepers.commands.util.ShopkeeperArgumentUtils;
import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.shopkeeper.teleporting.ShopkeeperTeleporter;
import java.util.Arrays;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

class CommandTeleport extends Command {
   private static final String ARGUMENT_SHOPKEEPER = "shopkeeper";
   private static final String ARGUMENT_FORCE = "force";
   private static final String ARGUMENT_PLAYER = "player";

   CommandTeleport() {
      super("teleport", Arrays.asList("tp"));
      this.setPermission("shopkeeper.teleport");
      this.setDescription(Messages.commandDescriptionTeleport);
      this.addArgument(new TargetShopkeeperFallback(new ShopkeeperArgument("shopkeeper", ShopkeeperFilter.withAccess(DefaultUITypes.TRADING())), ShopkeeperArgumentUtils.TargetShopkeeperFilter.ANY));
      this.addArgument(new SenderPlayerFallback(new PlayerArgument("player")));
      this.addArgument((new LiteralArgument("force")).optional());
   }

   protected void execute(CommandInput input, CommandContextView context) throws CommandException {
      CommandSender sender = input.getSender();
      Shopkeeper shopkeeper = (Shopkeeper)context.get("shopkeeper");
      boolean force = context.has("force");
      Player targetPlayer = (Player)context.get("player");

      assert targetPlayer != null;

      if (targetPlayer != sender) {
         this.checkPermission(sender, "shopkeeper.teleport.others");
      }

      ShopkeeperTeleporter.teleport(targetPlayer, shopkeeper, force, sender);
   }
}
