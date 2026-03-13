package com.ryandw11.structure.commands.cstruct;

import com.ryandw11.structure.CustomStructures;
import com.ryandw11.structure.commands.SubCommand;
import com.ryandw11.structure.structure.Structure;
import java.util.Iterator;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class ListCommand implements SubCommand {
   private final CustomStructures plugin;

   public ListCommand(CustomStructures plugin) {
      this.plugin = plugin;
   }

   public boolean subCommand(CommandSender sender, Command cmd, String s, String[] args) {
      sender.sendMessage(String.valueOf(ChatColor.GREEN) + "Currently Active Structures:");
      Iterator var5 = this.plugin.getStructureHandler().getStructures().iterator();

      while(var5.hasNext()) {
         Structure st = (Structure)var5.next();
         String var10001 = String.valueOf(ChatColor.GREEN);
         sender.sendMessage(var10001 + " - " + String.valueOf(ChatColor.BLUE) + st.getName());
      }

      return false;
   }
}
