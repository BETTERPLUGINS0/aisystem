package org.terraform.command;

import java.util.Random;
import java.util.Stack;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.terraform.command.contants.TerraCommand;
import org.terraform.coregen.populatordata.PopulatorDataPostGen;
import org.terraform.data.TerraformWorld;
import org.terraform.main.TerraformGeneratorPlugin;
import org.terraform.structure.small.DesertWellPopulator;

public class DesertWellCommand extends TerraCommand {
   public DesertWellCommand(TerraformGeneratorPlugin plugin, String... aliases) {
      super(plugin, aliases);
   }

   @NotNull
   public String getDefaultDescription() {
      return "Spawns a desert well";
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
      (new DesertWellPopulator()).spawnDesertWell(TerraformWorld.get(p.getWorld()), new Random(), data, p.getLocation().getBlockX(), p.getLocation().getBlockY(), p.getLocation().getBlockZ(), (new Random()).nextBoolean());
   }
}
