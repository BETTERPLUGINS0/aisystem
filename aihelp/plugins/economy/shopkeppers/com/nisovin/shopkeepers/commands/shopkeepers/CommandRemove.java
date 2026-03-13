package com.nisovin.shopkeepers.commands.shopkeepers;

import com.nisovin.shopkeepers.api.events.PlayerDeleteShopkeeperEvent;
import com.nisovin.shopkeepers.api.shopkeeper.player.PlayerShopkeeper;
import com.nisovin.shopkeepers.api.ui.DefaultUITypes;
import com.nisovin.shopkeepers.commands.Confirmations;
import com.nisovin.shopkeepers.commands.arguments.ShopkeeperArgument;
import com.nisovin.shopkeepers.commands.arguments.ShopkeeperFilter;
import com.nisovin.shopkeepers.commands.arguments.TargetShopkeeperFallback;
import com.nisovin.shopkeepers.commands.lib.Command;
import com.nisovin.shopkeepers.commands.lib.CommandException;
import com.nisovin.shopkeepers.commands.lib.CommandInput;
import com.nisovin.shopkeepers.commands.lib.context.CommandContextView;
import com.nisovin.shopkeepers.commands.util.ShopkeeperArgumentUtils;
import com.nisovin.shopkeepers.event.ShopkeeperEventHelper;
import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.shopkeeper.AbstractShopkeeper;
import com.nisovin.shopkeepers.util.bukkit.PermissionUtils;
import com.nisovin.shopkeepers.util.bukkit.TextUtils;
import com.nisovin.shopkeepers.util.java.ObjectUtils;
import java.util.Arrays;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

class CommandRemove extends Command {
   private static final String ARGUMENT_SHOPKEEPER = "shopkeeper";
   private final Confirmations confirmations;

   CommandRemove(Confirmations confirmations) {
      super("remove", Arrays.asList("delete"));
      this.confirmations = confirmations;
      this.setDescription(Messages.commandDescriptionRemove);
      this.addArgument(new TargetShopkeeperFallback(new ShopkeeperArgument("shopkeeper", ShopkeeperFilter.withAccess(DefaultUITypes.EDITOR())), ShopkeeperArgumentUtils.TargetShopkeeperFilter.ANY));
   }

   public boolean testPermission(CommandSender sender) {
      if (!super.testPermission(sender)) {
         return false;
      } else {
         return PermissionUtils.hasPermission(sender, "shopkeeper.remove.own") || PermissionUtils.hasPermission(sender, "shopkeeper.remove.others") || PermissionUtils.hasPermission(sender, "shopkeeper.remove.admin");
      }
   }

   protected void execute(CommandInput input, CommandContextView context) throws CommandException {
      CommandSender sender = input.getSender();
      Player senderPlayer = (Player)ObjectUtils.castOrNull(sender, Player.class);
      AbstractShopkeeper shopkeeper = (AbstractShopkeeper)context.get("shopkeeper");
      if (shopkeeper.canEdit(sender, false)) {
         if (shopkeeper instanceof PlayerShopkeeper) {
            PlayerShopkeeper playerShop = (PlayerShopkeeper)shopkeeper;
            if (senderPlayer != null && playerShop.isOwner(senderPlayer)) {
               this.checkPermission(sender, "shopkeeper.remove.own");
            } else {
               this.checkPermission(sender, "shopkeeper.remove.others");
            }
         } else {
            this.checkPermission(sender, "shopkeeper.remove.admin");
         }

         this.confirmations.awaitConfirmation(sender, () -> {
            if (!shopkeeper.isValid()) {
               TextUtils.sendMessage(sender, Messages.shopAlreadyRemoved);
            } else {
               if (senderPlayer != null) {
                  PlayerDeleteShopkeeperEvent deleteEvent = ShopkeeperEventHelper.callPlayerDeleteShopkeeperEvent(shopkeeper, senderPlayer);
                  if (deleteEvent.isCancelled()) {
                     TextUtils.sendMessage(sender, Messages.shopRemovalCancelled);
                     return;
                  }
               }

               shopkeeper.delete(senderPlayer);
               shopkeeper.save();
               TextUtils.sendMessage(sender, Messages.shopRemoved);
            }
         });
         TextUtils.sendMessage(sender, Messages.confirmRemoveShop);
         TextUtils.sendMessage(sender, Messages.confirmationRequired);
      }
   }
}
