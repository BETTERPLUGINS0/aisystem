package com.bergerkiller.bukkit.tc.debug;

import com.bergerkiller.bukkit.common.inventory.CommonItemStack;
import com.bergerkiller.bukkit.common.nbt.CommonTagCompound;
import com.bergerkiller.bukkit.tc.TrainCarts;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface DebugToolType {
   String getIdentifier();

   String getTitle();

   String getDescription();

   String getInstructions();

   default boolean handlesLeftClick() {
      return false;
   }

   void onBlockInteract(TrainCarts var1, Player var2, Block var3, CommonItemStack var4, boolean var5);

   default void giveToPlayer(Player player) {
      CommonItemStack item = CommonItemStack.create(Material.STICK, 1);
      item.updateCustomData((tag) -> {
         tag.putValue("TrainCartsDebug", this.getIdentifier());
         this.saveMetadata(tag);
      });
      item.setCustomNameMessage(this.getTitle());
      item.addLoreMessage(this.getDescription());
      if (DebugTool.updateToolItem(player, item.toBukkit())) {
         player.sendMessage(ChatColor.GREEN + "Debug tool updates to a " + this.getTitle());
         player.sendMessage(ChatColor.YELLOW + this.getDescription());
      } else {
         player.getInventory().addItem(new ItemStack[]{item.toBukkit()});
         player.sendMessage(ChatColor.GREEN + "Given a " + this.getTitle());
         player.sendMessage(ChatColor.YELLOW + this.getDescription());
      }
   }

   default void loadMetadata(CommonTagCompound metadata) {
   }

   default void saveMetadata(CommonTagCompound metadata) {
   }
}
