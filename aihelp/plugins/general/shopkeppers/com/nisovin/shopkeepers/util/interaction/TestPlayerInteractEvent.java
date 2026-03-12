package com.nisovin.shopkeepers.util.interaction;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.Nullable;

public class TestPlayerInteractEvent extends PlayerInteractEvent {
   public TestPlayerInteractEvent(Player who, Action action, @Nullable ItemStack item, @Nullable Block clickedBlock, BlockFace clickedFace) {
      super(who, action, item, clickedBlock, clickedFace);
   }
}
