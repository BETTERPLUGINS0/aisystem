package com.ryandw11.structure.commands.cstruct;

import com.ryandw11.structure.CustomStructures;
import com.ryandw11.structure.commands.SubCommand;
import java.util.Iterator;
import java.util.Objects;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CheckKeyCommand implements SubCommand {
   private final CustomStructures plugin;

   public CheckKeyCommand(CustomStructures plugin) {
      this.plugin = plugin;
   }

   public boolean subCommand(CommandSender sender, Command cmd, String s, String[] args) {
      if (!sender.hasPermission("customstructures.checkkey")) {
         sender.sendMessage(String.valueOf(ChatColor.RED) + "You do not have permission for this command!");
         return true;
      } else if (!(sender instanceof Player)) {
         sender.sendMessage(String.valueOf(ChatColor.RED) + "This command is for players only!");
         return true;
      } else {
         Player p = (Player)sender;
         ItemStack item = p.getInventory().getItemInMainHand();
         if (item.getType() == Material.AIR) {
            p.sendMessage(String.valueOf(ChatColor.RED) + "You must be holding an item to use this command!");
            return true;
         } else {
            Iterator var7 = ((ConfigurationSection)Objects.requireNonNull(this.plugin.getCustomItemManager().getConfig().getConfigurationSection(""))).getKeys(false).iterator();

            String items;
            do {
               if (!var7.hasNext()) {
                  p.sendMessage(String.valueOf(ChatColor.RED) + "That item was not found in the item list.");
                  return false;
               }

               items = (String)var7.next();
            } while(!item.isSimilar(this.plugin.getCustomItemManager().getItem(items)));

            String var10001 = String.valueOf(ChatColor.GREEN);
            p.sendMessage(var10001 + "The item you are holding has a key of: " + String.valueOf(ChatColor.GOLD) + items);
            return true;
         }
      }
   }
}
