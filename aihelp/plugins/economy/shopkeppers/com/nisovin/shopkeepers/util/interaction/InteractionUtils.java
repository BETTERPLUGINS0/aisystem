package com.nisovin.shopkeepers.util.interaction;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public final class InteractionUtils {
   public static boolean checkBlockInteract(Player player, Block block, boolean checkChestInteraction) {
      PlayerInventory playerInventory = player.getInventory();
      ItemStack itemInMainHand = playerInventory.getItemInMainHand();
      ItemStack itemInOffHand = playerInventory.getItemInOffHand();
      playerInventory.setItemInMainHand((ItemStack)null);
      playerInventory.setItemInOffHand((ItemStack)null);
      Material blockType = block.getType();
      BlockState capturedBlockState = null;
      if (blockType != Material.CHEST && checkChestInteraction) {
         capturedBlockState = block.getState();
         block.setType(Material.CHEST, false);
      }

      TestPlayerInteractEvent dummyInteractEvent = new TestPlayerInteractEvent(player, Action.RIGHT_CLICK_BLOCK, (ItemStack)null, block, BlockFace.UP);
      Bukkit.getPluginManager().callEvent(dummyInteractEvent);
      boolean canAccessBlock = dummyInteractEvent.useInteractedBlock() != Result.DENY;
      if (capturedBlockState != null) {
         capturedBlockState.update(true, false);
      }

      playerInventory.setItemInMainHand(itemInMainHand);
      playerInventory.setItemInOffHand(itemInOffHand);
      return canAccessBlock;
   }

   public static boolean checkEntityInteract(Player player, Entity entity) {
      PlayerInventory playerInventory = player.getInventory();
      ItemStack itemInMainHand = playerInventory.getItemInMainHand();
      ItemStack itemInOffHand = playerInventory.getItemInOffHand();
      playerInventory.setItemInMainHand((ItemStack)null);
      playerInventory.setItemInOffHand((ItemStack)null);
      TestPlayerInteractEntityEvent dummyInteractEvent = new TestPlayerInteractEntityEvent(player, entity);
      Bukkit.getPluginManager().callEvent(dummyInteractEvent);
      boolean canAccessEntity = !dummyInteractEvent.isCancelled();
      playerInventory.setItemInMainHand(itemInMainHand);
      playerInventory.setItemInOffHand(itemInOffHand);
      return canAccessEntity;
   }

   private InteractionUtils() {
   }
}
