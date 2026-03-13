package org.terraform.command;

import java.util.Stack;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.terraform.command.contants.TerraCommand;
import org.terraform.main.TerraformGeneratorPlugin;

public class BiomeVisualiserCommand extends TerraCommand {
   public BiomeVisualiserCommand(TerraformGeneratorPlugin plugin, String... aliases) {
      super(plugin, aliases);
   }

   @NotNull
   public String getDefaultDescription() {
      return "Displays a test for biome distribution with the current configuration options";
   }

   public boolean canConsoleExec() {
      return false;
   }

   public boolean hasPermission(@NotNull CommandSender sender) {
      return sender.isOp();
   }

   public void execute(CommandSender sender, Stack<String> args) {
      Player p = (Player)sender;
      World world = Bukkit.getWorld("world");

      for(int nx = -10; nx <= 10; ++nx) {
         for(int nz = -10; nz <= 10; ++nz) {
            for(int y = 0; y < 130; ++y) {
               Location loc = new Location(world, p.getLocation().getX() + (double)nx, (double)y, p.getLocation().getZ() + (double)nz);
               Biome b = world.getBiome(loc);
               if (b.toString().equals("PLAINS") && !world.getType(loc).isSolid()) {
                  world.setType(loc, Material.RED_STAINED_GLASS);
               }
            }
         }
      }

   }
}
