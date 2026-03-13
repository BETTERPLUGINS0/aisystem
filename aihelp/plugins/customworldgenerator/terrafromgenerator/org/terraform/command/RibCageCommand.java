package org.terraform.command;

import java.util.Random;
import java.util.Stack;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.terraform.biome.flat.DesertHandler;
import org.terraform.command.contants.TerraCommand;
import org.terraform.coregen.populatordata.PopulatorDataPostGen;
import org.terraform.data.SimpleBlock;
import org.terraform.main.TerraformGeneratorPlugin;

public class RibCageCommand extends TerraCommand {
   public RibCageCommand(TerraformGeneratorPlugin plugin, String... aliases) {
      super(plugin, aliases);
   }

   @NotNull
   public String getDefaultDescription() {
      return "Spawns a rib cage";
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
      (new DesertHandler()).spawnRibCage(new Random(), new SimpleBlock(data, x, y, z));
   }
}
