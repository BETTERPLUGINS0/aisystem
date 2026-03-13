package org.terraform.command;

import java.util.Stack;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.terraform.command.contants.TerraCommand;
import org.terraform.coregen.populatordata.PopulatorDataPostGen;
import org.terraform.main.TerraformGeneratorPlugin;
import org.terraform.structure.monument.MonumentDesign;

public class LargeMonumentLampCommand extends TerraCommand {
   public LargeMonumentLampCommand(TerraformGeneratorPlugin plugin, String... aliases) {
      super(plugin, aliases);
   }

   @NotNull
   public String getDefaultDescription() {
      return "Spawntest for monument lamps";
   }

   public boolean canConsoleExec() {
      return false;
   }

   public boolean hasPermission(@NotNull CommandSender sender) {
      return sender.isOp();
   }

   public void execute(CommandSender sender, Stack<String> args) {
      Player p = (Player)sender;
      PopulatorDataPostGen data = new PopulatorDataPostGen(p.getLocation().getChunk());
      int x = p.getLocation().getBlockX();
      int y = p.getLocation().getBlockY();
      int z = p.getLocation().getBlockZ();
      MonumentDesign.PRISMARINE_LANTERNS.spawnLargeLight(data, x, y, z);
      MonumentDesign.DARK_PRISMARINE_CORNERS.spawnLargeLight(data, x + 10, y, z);
      MonumentDesign.DARK_LIGHTLESS.spawnLargeLight(data, x - 10, y, z);
   }
}
