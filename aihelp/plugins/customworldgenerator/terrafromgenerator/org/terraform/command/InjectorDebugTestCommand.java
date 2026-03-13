package org.terraform.command;

import java.util.Stack;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.terraform.command.contants.TerraCommand;
import org.terraform.main.TerraformGeneratorPlugin;

public class InjectorDebugTestCommand extends TerraCommand {
   public InjectorDebugTestCommand(TerraformGeneratorPlugin plugin, String... aliases) {
      super(plugin, aliases);
   }

   @NotNull
   public String getDefaultDescription() {
      return "Invokes NMSInjector.debugTest(Player)";
   }

   public boolean canConsoleExec() {
      return false;
   }

   public boolean hasPermission(@NotNull CommandSender sender) {
      return sender.isOp();
   }

   public void execute(CommandSender sender, Stack<String> args) {
      Player p = (Player)sender;
      World w = p.getWorld();

      for(int y = -64; y < 320; ++y) {
         w.setBiome(p.getLocation().getBlockX(), y, p.getLocation().getBlockZ(), Biome.BADLANDS);
      }

      p.sendMessage("Finished setting biome.");
   }
}
