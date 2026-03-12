package com.nisovin.shopkeepers.commands.shopkeepers;

import com.nisovin.shopkeepers.api.ui.DefaultUITypes;
import com.nisovin.shopkeepers.commands.arguments.ShopkeeperArgument;
import com.nisovin.shopkeepers.commands.arguments.ShopkeeperFilter;
import com.nisovin.shopkeepers.commands.arguments.TargetShopkeeperFallback;
import com.nisovin.shopkeepers.commands.lib.Command;
import com.nisovin.shopkeepers.commands.lib.CommandException;
import com.nisovin.shopkeepers.commands.lib.CommandInput;
import com.nisovin.shopkeepers.commands.lib.arguments.FirstOfArgument;
import com.nisovin.shopkeepers.commands.lib.arguments.LiteralArgument;
import com.nisovin.shopkeepers.commands.lib.arguments.StringArgument;
import com.nisovin.shopkeepers.commands.lib.context.CommandContextView;
import com.nisovin.shopkeepers.commands.util.ShopkeeperArgumentUtils;
import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.shopkeeper.admin.AbstractAdminShopkeeper;
import com.nisovin.shopkeepers.util.bukkit.TextUtils;
import java.util.Arrays;
import org.bukkit.command.CommandSender;

class CommandSetTradePerm extends Command {
   private static final String ARGUMENT_SHOPKEEPER = "shopkeeper";
   private static final String ARGUMENT_NEW_PERMISSION = "perm";
   private static final String ARGUMENT_REMOVE_PERMISSION = "-";
   private static final String ARGUMENT_QUERY_PERMISSION = "?";

   CommandSetTradePerm() {
      super("setTradePerm");
      this.setPermission("shopkeeper.settradeperm");
      this.setDescription(Messages.commandDescriptionSettradeperm);
      this.addArgument(new TargetShopkeeperFallback(new ShopkeeperArgument("shopkeeper", ShopkeeperFilter.ADMIN.and(ShopkeeperFilter.withAccess(DefaultUITypes.EDITOR()))), ShopkeeperArgumentUtils.TargetShopkeeperFilter.ADMIN));
      this.addArgument(new FirstOfArgument("permArg", Arrays.asList((new LiteralArgument("?")).orDefaultValue("?"), new LiteralArgument("-"), new StringArgument("perm")), true, true));
   }

   protected void execute(CommandInput input, CommandContextView context) throws CommandException {
      CommandSender sender = input.getSender();
      AbstractAdminShopkeeper shopkeeper = (AbstractAdminShopkeeper)context.get("shopkeeper");
      if (shopkeeper.canEdit(sender, false)) {
         String newTradePerm = (String)context.getOrNull("perm");
         boolean removePerm = context.has("-");
         String currentTradePerm = shopkeeper.getTradePermission();
         if (currentTradePerm == null) {
            currentTradePerm = "-";
         }

         if (removePerm) {
            assert newTradePerm == null;

            TextUtils.sendMessage(sender, Messages.tradePermRemoved, "perm", currentTradePerm);
         } else {
            if (newTradePerm == null) {
               TextUtils.sendMessage(sender, Messages.tradePermView, "perm", currentTradePerm);
               return;
            }

            TextUtils.sendMessage(sender, Messages.tradePermSet, "perm", newTradePerm);
         }

         shopkeeper.setTradePermission(newTradePerm);
         shopkeeper.save();
      }
   }
}
