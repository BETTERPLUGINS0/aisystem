package com.nisovin.shopkeepers.commands.shopkeepers;

import com.nisovin.shopkeepers.api.ShopkeepersPlugin;
import com.nisovin.shopkeepers.api.events.PlayerDeleteShopkeeperEvent;
import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;
import com.nisovin.shopkeepers.api.shopkeeper.ShopkeeperRegistry;
import com.nisovin.shopkeepers.api.shopkeeper.admin.AdminShopkeeper;
import com.nisovin.shopkeepers.api.shopkeeper.player.PlayerShopkeeper;
import com.nisovin.shopkeepers.commands.Confirmations;
import com.nisovin.shopkeepers.commands.lib.Command;
import com.nisovin.shopkeepers.commands.lib.CommandException;
import com.nisovin.shopkeepers.commands.lib.CommandInput;
import com.nisovin.shopkeepers.commands.lib.arguments.FirstOfArgument;
import com.nisovin.shopkeepers.commands.lib.arguments.LiteralArgument;
import com.nisovin.shopkeepers.commands.lib.arguments.PlayerNameArgument;
import com.nisovin.shopkeepers.commands.lib.arguments.PlayerUUIDArgument;
import com.nisovin.shopkeepers.commands.lib.arguments.SenderPlayerNameFallback;
import com.nisovin.shopkeepers.commands.lib.context.CommandContextView;
import com.nisovin.shopkeepers.commands.lib.util.PlayerArgumentUtils;
import com.nisovin.shopkeepers.commands.util.ShopkeeperArgumentUtils;
import com.nisovin.shopkeepers.event.ShopkeeperEventHelper;
import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.util.bukkit.PermissionUtils;
import com.nisovin.shopkeepers.util.bukkit.TextUtils;
import com.nisovin.shopkeepers.util.java.ObjectUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

class CommandRemoveAll extends Command {
   private static final String ARGUMENT_PLAYER = "player";
   private static final String ARGUMENT_PLAYER_NAME = "player:name";
   private static final String ARGUMENT_PLAYER_UUID = "player:uuid";
   private static final String ARGUMENT_ALL_PLAYER_SHOPS = "all-player";
   private static final String ARGUMENT_ALL_PLAYER_SHOPS_DISPLAY_NAME = "player";
   private static final String ARGUMENT_ALL_ADMIN_SHOPS = "all-admin";
   private static final String ARGUMENT_ALL_ADMIN_SHOPS_DISPLAY_NAME = "admin";
   private final ShopkeepersPlugin plugin;
   private final ShopkeeperRegistry shopkeeperRegistry;
   private final Confirmations confirmations;

   CommandRemoveAll(ShopkeepersPlugin plugin, ShopkeeperRegistry shopkeeperRegistry, Confirmations confirmations) {
      super("removeAll", Arrays.asList("deleteAll"));
      this.plugin = plugin;
      this.shopkeeperRegistry = shopkeeperRegistry;
      this.confirmations = confirmations;
      this.setDescription(Messages.commandDescriptionRemoveAll);
      this.addArgument(new FirstOfArgument("target", Arrays.asList((new LiteralArgument("all-admin", Arrays.asList("admin"))).setDisplayName("admin"), (new LiteralArgument("all-player", Arrays.asList("player"))).setDisplayName("player"), new FirstOfArgument("player", Arrays.asList(new PlayerUUIDArgument("player:uuid"), new SenderPlayerNameFallback(new PlayerNameArgument("player:name"))), false)), true, true));
   }

   public boolean testPermission(CommandSender sender) {
      if (!super.testPermission(sender)) {
         return false;
      } else {
         return PermissionUtils.hasPermission(sender, "shopkeeper.remove-all.own") || PermissionUtils.hasPermission(sender, "shopkeeper.remove-all.others") || PermissionUtils.hasPermission(sender, "shopkeeper.remove-all.player") || PermissionUtils.hasPermission(sender, "shopkeeper.remove-all.admin");
      }
   }

   protected void execute(CommandInput input, CommandContextView context) throws CommandException {
      CommandSender sender = input.getSender();
      Player senderPlayer = (Player)ObjectUtils.castOrNull(sender, Player.class);
      boolean allPlayers = context.has("all-player");
      boolean allAdmin = context.has("all-admin");
      UUID targetPlayerUUID = (UUID)context.getOrNull("player:uuid");
      String targetPlayerName = (String)context.getOrNull("player:name");

      assert allPlayers ^ allAdmin ^ targetPlayerUUID != null ^ targetPlayerName != null;

      boolean targetOwnShops = false;
      if (targetPlayerUUID != null || targetPlayerName != null) {
         String senderName = (String)Unsafe.assertNonNull(sender.getName());
         if (senderPlayer != null && (senderPlayer.getUniqueId().equals(targetPlayerUUID) || senderName.equalsIgnoreCase(targetPlayerName))) {
            targetOwnShops = true;
            targetPlayerUUID = senderPlayer.getUniqueId();
            targetPlayerName = senderPlayer.getName();
         } else if (targetPlayerName != null) {
            Player onlinePlayer = Bukkit.getPlayerExact(targetPlayerName);
            if (onlinePlayer != null) {
               targetPlayerUUID = onlinePlayer.getUniqueId();
               targetPlayerName = onlinePlayer.getName();
            }
         }
      }

      if (allAdmin) {
         this.checkPermission(sender, "shopkeeper.remove-all.admin");
      } else if (allPlayers) {
         this.checkPermission(sender, "shopkeeper.remove-all.player");
      } else if (targetOwnShops) {
         this.checkPermission(sender, "shopkeeper.remove-all.own");
      } else {
         this.checkPermission(sender, "shopkeeper.remove-all.others");
      }

      Iterator var12;
      Shopkeeper shopkeeper;
      Object affectedShops;
      ArrayList playerShops;
      if (allAdmin) {
         playerShops = new ArrayList();
         var12 = this.shopkeeperRegistry.getAllShopkeepers().iterator();

         while(var12.hasNext()) {
            shopkeeper = (Shopkeeper)var12.next();
            if (shopkeeper instanceof AdminShopkeeper) {
               playerShops.add(shopkeeper);
            }
         }

         affectedShops = playerShops;
      } else if (allPlayers) {
         playerShops = new ArrayList();
         var12 = this.shopkeeperRegistry.getAllShopkeepers().iterator();

         while(var12.hasNext()) {
            shopkeeper = (Shopkeeper)var12.next();
            if (shopkeeper instanceof PlayerShopkeeper) {
               playerShops.add(shopkeeper);
            }
         }

         affectedShops = playerShops;
      } else {
         assert targetPlayerUUID != null ^ targetPlayerName != null;

         ShopkeeperArgumentUtils.OwnedPlayerShopsResult ownedPlayerShopsResult = ShopkeeperArgumentUtils.getOwnedPlayerShops(targetPlayerUUID, targetPlayerName);

         assert ownedPlayerShopsResult != null;

         Map<? extends UUID, ? extends String> matchingShopOwners = ownedPlayerShopsResult.getMatchingShopOwners();

         assert matchingShopOwners != null;

         if (matchingShopOwners.size() > 1) {
            assert targetPlayerName != null;

            boolean ambiguous = PlayerArgumentUtils.handleAmbiguousPlayerName(sender, targetPlayerName, matchingShopOwners.entrySet());
            if (ambiguous) {
               return;
            }
         }

         targetPlayerUUID = ownedPlayerShopsResult.getPlayerUUID();
         targetPlayerName = ownedPlayerShopsResult.getPlayerName();
         affectedShops = ownedPlayerShopsResult.getShops();
      }

      assert affectedShops != null;

      int shopsCount = ((List)affectedShops).size();
      if (shopsCount == 0) {
         TextUtils.sendMessage(sender, Messages.noShopsFound);
      } else {
         this.confirmations.awaitConfirmation(sender, () -> {
            int invalidShops = 0;
            int cancelledDeletions = 0;
            int actualShopCount = 0;
            Iterator var11 = affectedShops.iterator();

            while(true) {
               while(var11.hasNext()) {
                  Shopkeeper shopkeeper = (Shopkeeper)var11.next();
                  if (!shopkeeper.isValid()) {
                     ++invalidShops;
                  } else {
                     if (senderPlayer != null) {
                        PlayerDeleteShopkeeperEvent deleteEvent = ShopkeeperEventHelper.callPlayerDeleteShopkeeperEvent(shopkeeper, senderPlayer);
                        if (deleteEvent.isCancelled()) {
                           ++cancelledDeletions;
                           continue;
                        }
                     }

                     shopkeeper.delete(senderPlayer);
                     ++actualShopCount;
                  }
               }

               this.plugin.getShopkeeperStorage().save();
               if (invalidShops > 0) {
                  TextUtils.sendMessage(sender, Messages.shopsAlreadyRemoved, "shopsCount", invalidShops);
               }

               if (cancelledDeletions > 0) {
                  TextUtils.sendMessage(sender, Messages.shopRemovalsCancelled, "shopsCount", cancelledDeletions);
               }

               if (allAdmin) {
                  TextUtils.sendMessage(sender, Messages.adminShopsRemoved, "shopsCount", actualShopCount);
               } else if (allPlayers) {
                  TextUtils.sendMessage(sender, Messages.playerShopsRemoved, "shopsCount", actualShopCount);
               } else {
                  TextUtils.sendMessage(sender, Messages.shopsOfPlayerRemoved, "player", TextUtils.getPlayerText(targetPlayerName, targetPlayerUUID), "shopsCount", actualShopCount);
               }

               return;
            }
         });
         if (allAdmin) {
            TextUtils.sendMessage(sender, Messages.confirmRemoveAllAdminShops, "shopsCount", shopsCount);
         } else if (allPlayers) {
            TextUtils.sendMessage(sender, Messages.confirmRemoveAllPlayerShops, "shopsCount", shopsCount);
         } else if (targetOwnShops) {
            TextUtils.sendMessage(sender, Messages.confirmRemoveAllOwnShops, "shopsCount", shopsCount);
         } else {
            TextUtils.sendMessage(sender, Messages.confirmRemoveAllShopsOfPlayer, "player", TextUtils.getPlayerText(targetPlayerName, targetPlayerUUID), "shopsCount", shopsCount);
         }

         TextUtils.sendMessage(sender, Messages.confirmationRequired);
      }
   }
}
