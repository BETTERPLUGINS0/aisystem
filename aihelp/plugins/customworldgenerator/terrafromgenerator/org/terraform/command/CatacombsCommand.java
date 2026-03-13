package org.terraform.command;

import java.util.Stack;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.terraform.command.contants.TerraCommand;
import org.terraform.coregen.populatordata.PopulatorDataPostGen;
import org.terraform.data.TerraformWorld;
import org.terraform.main.TerraformGeneratorPlugin;

public class CatacombsCommand extends TerraCommand {
   public CatacombsCommand(TerraformGeneratorPlugin plugin, String... aliases) {
      super(plugin, aliases);
   }

   @NotNull
   public String getDefaultDescription() {
      return "Spawntest for catacombs";
   }

   public boolean canConsoleExec() {
      return false;
   }

   public boolean hasPermission(@NotNull CommandSender sender) {
      return sender.isOp();
   }

   public void execute(CommandSender sender, Stack<String> args) {
      Player p = (Player)sender;
      new PopulatorDataPostGen(p.getLocation().getChunk());
      int x = p.getLocation().getBlockX();
      int y = p.getLocation().getBlockY();
      int z = p.getLocation().getBlockZ();
      TerraformWorld tw = TerraformWorld.get(p.getWorld());
      p.sendMessage("Unimplemented.");
   }
}
