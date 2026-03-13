package com.ryandw11.structure.commands.cstruct;

import com.ryandw11.structure.CustomStructures;
import com.ryandw11.structure.commands.SubCommand;
import com.ryandw11.structure.structure.StructureBuilder;
import com.ryandw11.structure.structure.properties.BlockLevelLimit;
import com.ryandw11.structure.structure.properties.StructureLimitations;
import com.ryandw11.structure.structure.properties.StructureLocation;
import com.ryandw11.structure.structure.properties.StructureProperties;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class CreateCommand implements SubCommand {
   private final CustomStructures plugin;

   public CreateCommand(CustomStructures plugin) {
      this.plugin = plugin;
   }

   public boolean subCommand(CommandSender sender, Command cmd, String s, String[] args) {
      if (args.length < 2) {
         if (!sender.hasPermission("customstructures.create")) {
            sender.sendMessage(String.valueOf(ChatColor.RED) + "You do not have permission for this command!");
            return true;
         }

         sender.sendMessage(String.valueOf(ChatColor.RED) + "You must specify the name and schematic of a structure!");
      } else if (args.length == 2) {
         if (!sender.hasPermission("customstructures.create")) {
            sender.sendMessage(String.valueOf(ChatColor.RED) + "You do not have permission for this command!");
            return true;
         }

         String name = args[0];
         String schematic = args[1].replace(".schem", "");
         if (schematic.equals("")) {
            sender.sendMessage(String.valueOf(ChatColor.RED) + "Invalid schematic!");
            return true;
         }

         if (this.plugin.getStructureHandler().getStructure(name) != null) {
            sender.sendMessage(String.valueOf(ChatColor.RED) + "A structure with that name already exists!");
            return true;
         }

         String var10002 = String.valueOf(this.plugin.getDataFolder());
         File f = new File(var10002 + File.separator + "structures" + File.separator + name + ".yml");

         try {
            if (!f.exists()) {
               f.createNewFile();
            }
         } catch (IOException var11) {
            sender.sendMessage(String.valueOf(ChatColor.RED) + "An error occurred while creating a structure file!");
            this.plugin.getLogger().severe("Could not create structure file!");
            if (this.plugin.isDebug()) {
               var11.printStackTrace();
            }

            return true;
         }

         StructureBuilder builder = new StructureBuilder(name, schematic + ".schem");
         builder.setProbability(1, 1000);
         var10002 = String.valueOf(this.plugin.getDataFolder());
         if ((new File(var10002 + "/schematics/" + schematic + ".cschem")).exists()) {
            builder.setCompiledSchematic(schematic + ".cschem");
         }

         builder.setStructureProperties(new StructureProperties());
         builder.setStructureLocation(new StructureLocation());
         builder.setStructureLimitations(new StructureLimitations(new ArrayList(), new ArrayList(), new BlockLevelLimit(), new HashMap()));

         try {
            builder.save(f);
         } catch (IOException var10) {
            sender.sendMessage(String.valueOf(ChatColor.RED) + "An error occurred while saving the structure file!");
            this.plugin.getLogger().severe("Could not save structure file!");
            if (this.plugin.isDebug()) {
               var10.printStackTrace();
            }

            return true;
         }

         List<String> structs = this.plugin.getConfig().getStringList("Structures");
         structs.add(name);
         this.plugin.getConfig().set("Structures", structs);
         this.plugin.saveConfig();
         String var10001 = String.valueOf(ChatColor.GREEN);
         sender.sendMessage(var10001 + "Successfully created the structure " + String.valueOf(ChatColor.GOLD) + name + String.valueOf(ChatColor.GREEN) + "!");
         sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aRun the &6/cstructure reload &ato load in the new structure and changes."));
      }

      return false;
   }
}
