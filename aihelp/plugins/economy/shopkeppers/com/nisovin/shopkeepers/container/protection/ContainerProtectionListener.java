package com.nisovin.shopkeepers.container.protection;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.util.bukkit.TextUtils;
import com.nisovin.shopkeepers.util.inventory.ItemUtils;
import com.nisovin.shopkeepers.util.logging.Log;
import java.util.List;
import java.util.Objects;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

class ContainerProtectionListener implements Listener {
   private final ProtectedContainers protectedContainers;

   ContainerProtectionListener(ProtectedContainers protectedContainers) {
      this.protectedContainers = protectedContainers;
   }

   @EventHandler(
      priority = EventPriority.LOW,
      ignoreCancelled = true
   )
   void onPlayerInteract(PlayerInteractEvent event) {
      if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
         Block block = (Block)Unsafe.assertNonNull(event.getClickedBlock());
         Player player = event.getPlayer();
         if (this.protectedContainers.isProtectedContainer(block, player)) {
            Log.debug(() -> {
               String var10000 = player.getName();
               return "Cancelled container opening by '" + var10000 + "' at '" + TextUtils.getLocationString(block) + "': Protected container.";
            });
            event.setCancelled(true);
         }

      }
   }

   @EventHandler(
      priority = EventPriority.LOW,
      ignoreCancelled = true
   )
   void onBlockBreak(BlockBreakEvent event) {
      Block block = event.getBlock();
      Player player = event.getPlayer();
      if (this.protectedContainers.isProtectedContainer(block, player)) {
         Log.debug(() -> {
            String var10000 = player.getName();
            return "Cancelled breaking of container block by '" + var10000 + "' at '" + TextUtils.getLocationString(block) + "': Protected container.";
         });
         event.setCancelled(true);
      }

   }

   @EventHandler(
      priority = EventPriority.LOW,
      ignoreCancelled = true
   )
   void onBlockPlace(BlockPlaceEvent event) {
      Block block = event.getBlock();
      Material type = block.getType();
      Player player = event.getPlayer();
      if (ItemUtils.isChest(type)) {
         if (this.protectedContainers.isProtectedContainer(block, player)) {
            Log.debug(() -> {
               String var10000 = player.getName();
               return "Cancelled placing of (double) chest block by '" + var10000 + "' at '" + TextUtils.getLocationString(block) + "': Protected chest nearby.";
            });
            event.setCancelled(true);
         }
      } else {
         Block upperBlock;
         if (type == Material.HOPPER) {
            upperBlock = block.getRelative(BlockFace.UP);
            if (this.protectedContainers.isProtectedContainer(upperBlock, player) || this.protectedContainers.isProtectedContainer(this.getFacedBlock(block), player)) {
               Log.debug(() -> {
                  String var10000 = player.getName();
                  return "Cancelled placing of hopper block by '" + var10000 + "' at '" + TextUtils.getLocationString(block) + "': Protected container nearby.";
               });
               event.setCancelled(true);
            }
         } else if (type == Material.DROPPER) {
            if (this.protectedContainers.isProtectedContainer(this.getFacedBlock(block), player)) {
               Log.debug(() -> {
                  String var10000 = player.getName();
                  return "Cancelled placing of dropper block by '" + var10000 + "' at '" + TextUtils.getLocationString(block) + "': Protected container nearby.";
               });
               event.setCancelled(true);
            }
         } else if (ItemUtils.isRail(type)) {
            upperBlock = block.getRelative(BlockFace.UP);
            if (this.protectedContainers.isProtectedContainer(upperBlock, player)) {
               Log.debug(() -> {
                  String var10000 = player.getName();
                  return "Cancelled placing of rail block by '" + var10000 + "' at '" + TextUtils.getLocationString(block) + "': Protected container nearby.";
               });
               event.setCancelled(true);
            }
         }
      }

   }

   private Block getFacedBlock(Block directionalBlock) {
      Directional directionalData = (Directional)directionalBlock.getBlockData();
      BlockFace facing = directionalData.getFacing();
      return directionalBlock.getRelative(facing);
   }

   @EventHandler(
      priority = EventPriority.LOW,
      ignoreCancelled = true
   )
   void onEntityExplosion(EntityExplodeEvent event) {
      List<Block> blockList = event.blockList();
      this.removeProtectedChests(blockList);
   }

   @EventHandler(
      priority = EventPriority.LOW,
      ignoreCancelled = true
   )
   void onBlockExplosion(BlockExplodeEvent event) {
      List<Block> blockList = event.blockList();
      this.removeProtectedChests(blockList);
   }

   private void removeProtectedChests(List<? extends Block> blockList) {
      ProtectedContainers var10001 = this.protectedContainers;
      Objects.requireNonNull(var10001);
      blockList.removeIf(var10001::isProtectedContainer);
   }
}
