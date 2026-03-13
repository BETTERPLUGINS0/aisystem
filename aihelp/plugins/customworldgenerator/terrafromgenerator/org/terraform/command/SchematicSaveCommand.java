package org.terraform.command;

import java.io.IOException;
import java.util.Iterator;
import java.util.Stack;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.terraform.command.contants.FilenameArgument;
import org.terraform.command.contants.InvalidArgumentException;
import org.terraform.command.contants.TerraCommand;
import org.terraform.main.TerraformGeneratorPlugin;
import org.terraform.schematic.SchematicListener;
import org.terraform.schematic.TerraRegion;
import org.terraform.schematic.TerraSchematic;

public class SchematicSaveCommand extends TerraCommand {
   public SchematicSaveCommand(TerraformGeneratorPlugin plugin, String... aliases) {
      super(plugin, aliases);
      this.parameters.add(new FilenameArgument("schem-name", false));
   }

   @NotNull
   public String getDefaultDescription() {
      return "Saves a schematic in the schematics folder in plugins/TerraformGenerator";
   }

   public boolean canConsoleExec() {
      return false;
   }

   public boolean hasPermission(@NotNull CommandSender sender) {
      return sender.isOp();
   }

   public void execute(CommandSender sender, @NotNull Stack<String> args) throws InvalidArgumentException {
      Player p = (Player)sender;
      TerraRegion rg = (TerraRegion)SchematicListener.rgs.get(p.getUniqueId());
      if (args.size() != 1) {
         p.sendMessage(String.valueOf(ChatColor.RED) + "Usage: /terra save [schematic name]");
      } else {
         String name = (String)this.parseArguments(sender, args).get(0);
         if (rg != null && rg.isComplete()) {
            TerraSchematic s = new TerraSchematic(p.getLocation().clone());
            Iterator var7 = rg.getBlocks().iterator();

            while(var7.hasNext()) {
               Block b = (Block)var7.next();
               if (b.getType() != Material.AIR) {
                  if (b.getType() == Material.BARRIER) {
                     b.setType(Material.AIR);
                  }

                  s.registerBlock(b);
               }
            }

            try {
               s.export(name + ".terra");
               String var10001 = String.valueOf(ChatColor.GREEN);
               p.sendMessage(var10001 + "Schematic saved with name " + name);
            } catch (IOException var9) {
               p.sendMessage(String.valueOf(ChatColor.RED) + "A problem occurred.");
               TerraformGeneratorPlugin.logger.stackTrace(var9);
            }

         } else {
            p.sendMessage(String.valueOf(ChatColor.RED) + "Selection not ready.");
         }
      }
   }
}
