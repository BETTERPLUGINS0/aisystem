package me.ag4.playershop.listener;

import me.ag4.playershop.PlayerShop;
import me.ag4.playershop.api.PlayerShopAPI;
import me.ag4.playershop.events.ShopOpenEvent;
import me.ag4.playershop.files.Lang;
import me.ag4.playershop.hooks.FloodgateAPI;
import me.ag4.playershop.hooks.ProtocolLibAPI;
import me.ag4.playershop.objects.Shop;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class InteractEvent implements Listener {
   @EventHandler
   public void onOpen(PlayerInteractEvent e) {
      if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
         Player p = e.getPlayer();
         Block block = e.getClickedBlock();
         if (block != null) {
            Shop shop = PlayerShopAPI.getPlayerShop(block.getLocation());
            if (shop != null) {
               e.setCancelled(true);
               Bukkit.getPluginManager().callEvent(new ShopOpenEvent(p, block, shop));
            }
         }
      }
   }

   @EventHandler(
      priority = EventPriority.HIGHEST
   )
   public void onShopOpen(ShopOpenEvent e) {
      if (!e.isCancelled()) {
         Player player = e.getPlayer();
         Block block = e.getBlock();
         Shop shop = e.getShop();
         boolean isFloodgateEnable = PlayerShop.getInstance().getHookManager().isFloodgateEnabled();
         if (!shop.isReady()) {
            if (!shop.getOwner().equals(player.getUniqueId())) {
               player.sendMessage(Lang.Error_Shop_Not_Ready_Default.toString());
            } else if (player.isSneaking()) {
               if (isFloodgateEnable) {
                  FloodgateAPI.openSetPriceMenu(player, shop);
               } else {
                  PlayerShopAPI.openSign(player, shop);
               }
            } else {
               player.sendMessage(Lang.Error_Shop_Not_Ready_Owner.toString());
            }
         } else if (player.isSneaking() && shop.getOwner().equals(player.getUniqueId())) {
            if (isFloodgateEnable) {
               FloodgateAPI.openSetPriceMenu(player, shop);
            } else {
               PlayerShopAPI.openSign(player, shop);
            }
         } else {
            PlayerShopAPI.openShop(player, shop);
            ProtocolLibAPI.chestOpenAnimation(player, block.getLocation());
         }
      }
   }
}
