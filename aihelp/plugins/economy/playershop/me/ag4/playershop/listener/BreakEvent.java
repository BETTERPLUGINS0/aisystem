package me.ag4.playershop.listener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import me.ag4.playershop.api.PlayerShopAPI;
import me.ag4.playershop.events.ShopBreakEvent;
import me.ag4.playershop.files.Lang;
import me.ag4.playershop.objects.Shop;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class BreakEvent implements Listener {
   @EventHandler(
      priority = EventPriority.HIGHEST
   )
   public void onBreak(BlockBreakEvent e) {
      if (!e.isCancelled()) {
         Player p = e.getPlayer();
         Block block = e.getBlock();
         Shop shop = PlayerShopAPI.getPlayerShop(block.getLocation());
         if (shop != null) {
            Bukkit.getPluginManager().callEvent(new ShopBreakEvent(p, block, shop));
         }
      }
   }

   @EventHandler(
      priority = EventPriority.HIGHEST
   )
   public void onShopBreak(ShopBreakEvent e) {
      if (!e.isCancelled()) {
         Player p = e.getPlayer();
         Block block = e.getBlock();
         Shop shop = e.getShop();
         p.sendMessage(Lang.Break_Message.toString());
         List<ItemStack> itemsToDrop = new ArrayList();
         ItemStack[] var6 = shop.getInventory().getContents();
         int var7 = var6.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            ItemStack items = var6[var8];
            if (items != null) {
               itemsToDrop.add(items);
            }
         }

         Iterator var11 = itemsToDrop.iterator();

         while(var11.hasNext()) {
            ItemStack item = (ItemStack)var11.next();

            try {
               block.getWorld().dropItem(block.getLocation(), item);
            } catch (IllegalArgumentException var10) {
               var10.printStackTrace();
            }
         }

         shop.removeShop();
      }
   }
}
