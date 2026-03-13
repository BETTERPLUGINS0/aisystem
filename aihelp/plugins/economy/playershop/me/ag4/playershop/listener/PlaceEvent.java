package me.ag4.playershop.listener;

import java.util.UUID;
import me.ag4.playershop.PlayerShop;
import me.ag4.playershop.api.PlayerShopAPI;
import me.ag4.playershop.events.ShopCreateEvent;
import me.ag4.playershop.events.ShopPlaceEvent;
import me.ag4.playershop.files.Lang;
import me.ag4.playershop.hooks.FloodgateAPI;
import me.ag4.playershop.objects.Shop;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Chest;
import org.bukkit.block.data.type.Chest.Type;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public class PlaceEvent implements Listener {
   @EventHandler(
      priority = EventPriority.HIGHEST
   )
   public void onPlace(BlockPlaceEvent e) {
      if (!e.isCancelled()) {
         Player player = e.getPlayer();
         UUID uuid = player.getUniqueId();
         Block block = e.getBlock();
         ItemStack placedItem = e.getItemInHand();
         boolean craftingEnable = PlayerShop.getInstance().config().getBoolean("Craft.Enable");
         if ((!craftingEnable || PlayerShopAPI.isShopChest(placedItem)) && placedItem.getType().equals(Material.CHEST)) {
            if (!PlayerShopAPI.isEnabledWorld(player)) {
               if (craftingEnable) {
                  player.sendMessage(Lang.Error_Place.toString());
                  e.setCancelled(true);
               }

            } else if (PlayerShopAPI.getWorldGuardFlagState(block, player)) {
               player.sendMessage(Lang.Error_Place_WorldGuard_Flag.toString());
               e.setCancelled(true);
            } else if (PlayerShopAPI.isPermissionEnabled() && !player.hasPermission("playershop.use")) {
               player.sendMessage(Lang.Error_Permission.toString());
               e.setCancelled(true);
            } else if (PlayerShopAPI.getPlayerShopCount(uuid) >= PlayerShopAPI.getPlayerShopMax(player)) {
               player.sendMessage(Lang.Error_Place_Max.toString().replace("{max}", Integer.toString(PlayerShopAPI.getPlayerShopMax(player))));
               e.setCancelled(true);
            } else {
               if (PlayerShopAPI.isPlaceMerge(block)) {
                  Chest chestData = (Chest)block.getBlockData();
                  chestData.setType(Type.SINGLE);
                  block.setBlockData(chestData);
               }

               ShopPlaceEvent shopPlaceEvent = new ShopPlaceEvent(player, block);
               Bukkit.getPluginManager().callEvent(shopPlaceEvent);
               e.setCancelled(shopPlaceEvent.isCancelled());
            }
         }
      }
   }

   @EventHandler(
      priority = EventPriority.HIGHEST
   )
   public void onShopPlace(ShopPlaceEvent e) {
      if (!e.isCancelled()) {
         Player player = e.getPlayer();
         Block block = e.getBlock();
         PlayerShopAPI.updateBlock(block, player);
         Shop shop = PlayerShopAPI.createShop(player, block.getLocation());
         Bukkit.getPluginManager().callEvent(new ShopCreateEvent(player, block, shop));
      }
   }

   @EventHandler(
      priority = EventPriority.HIGHEST
   )
   public void onShopCreate(ShopCreateEvent e) {
      if (!e.isCancelled()) {
         Player player = e.getPlayer();
         Block block = e.getBlock();
         Shop shop = e.getShop();
         boolean isFloodgateEnabled = PlayerShop.getInstance().getHookManager().isFloodgateEnabled();
         PlayerShopAPI.updateBlock(block, player);
         player.sendMessage(Lang.Place_Message.toString());
         if (isFloodgateEnabled) {
            FloodgateAPI.openSetPriceMenu(player, shop);
         } else {
            PlayerShopAPI.openSign(player, shop);
         }

      }
   }
}
