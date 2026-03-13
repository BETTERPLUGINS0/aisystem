package com.nisovin.shopkeepers.commands.shopkeepers;

import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;
import com.nisovin.shopkeepers.api.ui.DefaultUITypes;
import com.nisovin.shopkeepers.commands.arguments.ShopkeeperArgument;
import com.nisovin.shopkeepers.commands.arguments.ShopkeeperFilter;
import com.nisovin.shopkeepers.commands.arguments.TargetShopkeeperFallback;
import com.nisovin.shopkeepers.commands.lib.CommandException;
import com.nisovin.shopkeepers.commands.lib.CommandInput;
import com.nisovin.shopkeepers.commands.lib.commands.PlayerCommand;
import com.nisovin.shopkeepers.commands.lib.context.CommandContextView;
import com.nisovin.shopkeepers.commands.util.ShopkeeperArgumentUtils;
import com.nisovin.shopkeepers.lang.Messages;
import org.bukkit.entity.Player;

class CommandEdit extends PlayerCommand {
   private static final String ARGUMENT_SHOPKEEPER = "shopkeeper";

   CommandEdit() {
      super("edit");
      this.setPermission("shopkeeper.remoteedit");
      this.setDescription(Messages.commandDescriptionRemoteEdit);
      this.addArgument(new TargetShopkeeperFallback(new ShopkeeperArgument("shopkeeper", ShopkeeperFilter.withAccess(DefaultUITypes.EDITOR())), ShopkeeperArgumentUtils.TargetShopkeeperFilter.ANY));
   }

   protected void execute(CommandInput input, CommandContextView context) throws CommandException {
      assert input.getSender() instanceof Player;

      Player player = (Player)input.getSender();
      Shopkeeper shopkeeper = (Shopkeeper)context.get("shopkeeper");
      shopkeeper.openEditorWindow(player);
   }
}
