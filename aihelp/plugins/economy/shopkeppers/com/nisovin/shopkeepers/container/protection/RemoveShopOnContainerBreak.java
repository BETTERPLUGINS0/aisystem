package com.nisovin.shopkeepers.container.protection;

import com.nisovin.shopkeepers.SKShopkeepersPlugin;
import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.shopkeeper.player.PlayerShopkeeper;
import com.nisovin.shopkeepers.config.Settings;
import com.nisovin.shopkeepers.container.ShopContainers;
import com.nisovin.shopkeepers.shopcreation.ShopCreationItem;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class RemoveShopOnContainerBreak {
   private final SKShopkeepersPlugin plugin;
   private final ProtectedContainers protectedContainers;
   private final RemoveShopOnContainerBreakListener removeShopOnContainerBreakListener;

   public RemoveShopOnContainerBreak(SKShopkeepersPlugin plugin, ProtectedContainers protectedContainers) {
      this.plugin = plugin;
      this.protectedContainers = protectedContainers;
      this.removeShopOnContainerBreakListener = new RemoveShopOnContainerBreakListener(plugin, (RemoveShopOnContainerBreak)Unsafe.initialized(this));
   }

   public void onEnable() {
      if (Settings.deleteShopkeeperOnBreakContainer) {
         Bukkit.getPluginManager().registerEvents(this.removeShopOnContainerBreakListener, this.plugin);
      }

   }

   public void onDisable() {
      HandlerList.unregisterAll(this.removeShopOnContainerBreakListener);
   }

   public boolean handleBlockBreakage(Block block) {
      List<? extends PlayerShopkeeper> shopkeepers = this.protectedContainers.getShopkeepers(block);
      if (shopkeepers.isEmpty()) {
         return false;
      } else {
         PlayerShopkeeper[] var3 = (PlayerShopkeeper[])shopkeepers.toArray(new PlayerShopkeeper[0]);
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            PlayerShopkeeper shopkeeper = var3[var5];
            if (shopkeeper.isValid()) {
               if (Settings.deletingPlayerShopReturnsCreationItem) {
                  ItemStack shopCreationItem = ShopCreationItem.create();
                  block.getWorld().dropItemNaturally(block.getLocation(), shopCreationItem);
               }

               shopkeeper.delete();
            }
         }

         return true;
      }
   }

   public void handleBlocksBreakage(List<? extends Block> blockList) {
      boolean dirty = false;
      Iterator var3 = blockList.iterator();

      while(var3.hasNext()) {
         Block block = (Block)var3.next();
         if (ShopContainers.isSupportedContainer(block.getType()) && this.handleBlockBreakage(block)) {
            dirty = true;
         }
      }

      if (dirty) {
         this.plugin.getShopkeeperStorage().save();
      }

   }
}
