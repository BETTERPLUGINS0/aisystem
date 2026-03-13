package com.nisovin.shopkeepers.container.protection;

import com.nisovin.shopkeepers.SKShopkeepersPlugin;
import com.nisovin.shopkeepers.container.ShopContainers;
import java.util.List;
import org.bukkit.ExplosionResult;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

class RemoveShopOnContainerBreakListener implements Listener {
   private final SKShopkeepersPlugin plugin;
   private final RemoveShopOnContainerBreak removeShopOnContainerBreak;

   RemoveShopOnContainerBreakListener(SKShopkeepersPlugin plugin, RemoveShopOnContainerBreak removeShopOnContainerBreak) {
      assert plugin != null && removeShopOnContainerBreak != null;

      this.plugin = plugin;
      this.removeShopOnContainerBreak = removeShopOnContainerBreak;
   }

   @EventHandler(
      priority = EventPriority.MONITOR,
      ignoreCancelled = true
   )
   void onBlockBreak(BlockBreakEvent event) {
      Block block = event.getBlock();
      if (ShopContainers.isSupportedContainer(block.getType()) && this.removeShopOnContainerBreak.handleBlockBreakage(block)) {
         this.plugin.getShopkeeperStorage().save();
      }

   }

   @EventHandler(
      priority = EventPriority.MONITOR,
      ignoreCancelled = true
   )
   void onEntityExplosion(EntityExplodeEvent event) {
      if (isDestroyingBlocks(event.getExplosionResult())) {
         List<Block> blockList = event.blockList();
         this.removeShopOnContainerBreak.handleBlocksBreakage(blockList);
      }
   }

   @EventHandler(
      priority = EventPriority.MONITOR,
      ignoreCancelled = true
   )
   void onBlockExplosion(BlockExplodeEvent event) {
      if (isDestroyingBlocks(event.getExplosionResult())) {
         List<Block> blockList = event.blockList();
         this.removeShopOnContainerBreak.handleBlocksBreakage(blockList);
      }
   }

   private static boolean isDestroyingBlocks(ExplosionResult explosionResult) {
      return explosionResult == ExplosionResult.DESTROY || explosionResult == ExplosionResult.DESTROY_WITH_DECAY;
   }
}
