package com.nisovin.shopkeepers.shopkeeper.teleporting;

import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;
import com.nisovin.shopkeepers.api.shopobjects.ShopObject;
import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.util.bukkit.TeleportHelper;
import com.nisovin.shopkeepers.util.bukkit.TextUtils;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public final class ShopkeeperTeleporter {
   private static final double TELEPORT_DISTANCE = 2.0D;
   private static final TeleportHelper TELEPORT_HELPER;

   public static boolean teleport(Player player, Shopkeeper shopkeeper, boolean force, CommandSender sender) {
      if (shopkeeper.isVirtual()) {
         if (sender != null) {
            TextUtils.sendMessage(sender, Messages.teleportVirtualShopkeeper);
         }

         return false;
      } else {
         ShopObject shopObject = shopkeeper.getShopObject();
         Location shopkeeperLocation = shopObject.getLocation();
         if (shopkeeperLocation == null) {
            shopkeeperLocation = shopkeeper.getLocation();
            if (shopkeeperLocation == null) {
               if (sender != null) {
                  TextUtils.sendMessage(sender, Messages.teleportShopkeeperWorldNotLoaded);
               }

               return false;
            }
         }

         assert shopkeeperLocation != null;

         shopkeeperLocation.setYaw(shopkeeper.getYaw());
         shopkeeperLocation.setPitch(0.0F);
         Location destination = shopkeeperLocation.clone().add(shopkeeperLocation.getDirection().multiply(2.0D));
         int shopOffsetX = shopkeeperLocation.getBlockX() - destination.getBlockX();
         int shopOffsetZ = shopkeeperLocation.getBlockZ() - destination.getBlockZ();
         Location teleportLocation = TELEPORT_HELPER.findSafeDestination(destination, player, (offset) -> {
            return offset.getX() == shopOffsetX && offset.getZ() == shopOffsetZ ? (force ? 1 : Integer.MAX_VALUE) : 0;
         });
         if (teleportLocation == null) {
            if (!force) {
               if (sender != null) {
                  TextUtils.sendMessage(sender, Messages.teleportNoSafeLocationFound);
               }

               return false;
            }

            teleportLocation = shopkeeperLocation;
         }

         assert teleportLocation != null;

         Vector teleportPlayerEyeLocationVector = teleportLocation.toVector();
         teleportPlayerEyeLocationVector.setY(teleportPlayerEyeLocationVector.getY() + player.getEyeHeight(true));
         teleportLocation.setDirection(shopkeeperLocation.toVector().subtract(teleportPlayerEyeLocationVector));
         teleportLocation.setPitch(0.0F);
         if (!player.teleport(teleportLocation)) {
            if (sender != null) {
               TextUtils.sendMessage(sender, Messages.teleportFailed);
            }

            return false;
         } else {
            if (sender != null) {
               TextUtils.sendMessage(sender, Messages.teleportSuccess, "player", TextUtils.getPlayerText(player), "shop", TextUtils.getShopText(shopkeeper));
            }

            return true;
         }
      }
   }

   private ShopkeeperTeleporter() {
   }

   static {
      TELEPORT_HELPER = TeleportHelper.DEFAULT;
   }
}
