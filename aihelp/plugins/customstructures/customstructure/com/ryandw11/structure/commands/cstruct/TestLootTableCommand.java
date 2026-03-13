package com.ryandw11.structure.commands.cstruct;

import com.ryandw11.structure.CustomStructures;
import com.ryandw11.structure.commands.SubCommand;
import com.ryandw11.structure.loottables.LootTable;
import java.util.Random;
import java.util.Set;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Container;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TestLootTableCommand implements SubCommand {
   private final CustomStructures plugin;

   public TestLootTableCommand(CustomStructures plugin) {
      this.plugin = plugin;
   }

   public boolean subCommand(CommandSender sender, Command cmd, String s, String[] args) {
      if (args.length == 0) {
         if (!sender.hasPermission("customstructures.test.loottable")) {
            sender.sendMessage(String.valueOf(ChatColor.RED) + "You do not have permission for this command!");
            return true;
         }

         sender.sendMessage(String.valueOf(ChatColor.RED) + "You must specify the loot table for the chest to have.");
      } else if (args.length == 1) {
         if (!sender.hasPermission("customstructures.test.loottable")) {
            sender.sendMessage(String.valueOf(ChatColor.RED) + "You do not have permission for this command!");
            return true;
         }

         if (!(sender instanceof Player)) {
            sender.sendMessage(String.valueOf(ChatColor.RED) + "This command is for players only!");
            return true;
         }

         Player p = (Player)sender;
         LootTable lootTable = this.plugin.getLootTableHandler().getLootTableByName(args[0]);
         if (lootTable == null) {
            sender.sendMessage(String.valueOf(ChatColor.RED) + "Cannot find specified loot table. Check to make sure that it exists.");
            return true;
         }

         Block block = p.getTargetBlock((Set)null, 20);
         BlockState var9 = block.getState();
         if (!(var9 instanceof Container)) {
            sender.sendMessage(String.valueOf(ChatColor.RED) + "You must be looking at a container to set its loot table.");
            return true;
         }

         Container container = (Container)var9;
         container.getInventory().clear();
         lootTable.fillContainerInventory(container.getInventory(), new Random(), container.getLocation());
         sender.sendMessage(String.valueOf(ChatColor.GREEN) + "The loot table has been applied to the container!");
      }

      return false;
   }
}
