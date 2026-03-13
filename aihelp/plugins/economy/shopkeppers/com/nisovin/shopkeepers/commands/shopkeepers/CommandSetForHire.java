package com.nisovin.shopkeepers.commands.shopkeepers;

import com.nisovin.shopkeepers.api.ShopkeepersPlugin;
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
import com.nisovin.shopkeepers.shopkeeper.player.AbstractPlayerShopkeeper;
import com.nisovin.shopkeepers.text.Text;
import com.nisovin.shopkeepers.util.bukkit.TextUtils;
import com.nisovin.shopkeepers.util.inventory.ItemUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

class CommandSetForHire extends PlayerCommand {
   private static final String ARGUMENT_SHOPKEEPER = "shopkeeper";

   CommandSetForHire() {
      super("setForHire");
      this.setPermission("shopkeeper.setforhire");
      this.setDescription(Messages.commandDescriptionSetforhire);
      this.addArgument(new TargetShopkeeperFallback(new ShopkeeperArgument("shopkeeper", ShopkeeperFilter.PLAYER.and(ShopkeeperFilter.withAccess(DefaultUITypes.EDITOR()))), ShopkeeperArgumentUtils.TargetShopkeeperFilter.PLAYER));
   }

   protected void execute(CommandInput input, CommandContextView context) throws CommandException {
      assert input.getSender() instanceof Player;

      Player player = (Player)input.getSender();
      AbstractPlayerShopkeeper shopkeeper = (AbstractPlayerShopkeeper)context.get("shopkeeper");
      ItemStack hireCost = player.getInventory().getItemInMainHand();
      if (ItemUtils.isEmpty(hireCost)) {
         TextUtils.sendMessage(player, (Text)Messages.mustHoldHireItem);
      } else if (shopkeeper.canEdit(player, false)) {
         shopkeeper.setForHire(hireCost);
         TextUtils.sendMessage(player, (Text)Messages.setForHire);
         ShopkeepersPlugin.getInstance().getShopkeeperStorage().save();
      }
   }
}
