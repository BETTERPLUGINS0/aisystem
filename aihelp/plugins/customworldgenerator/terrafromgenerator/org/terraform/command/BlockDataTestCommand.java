package org.terraform.command;

import java.util.Stack;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.data.type.Wall;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.terraform.command.contants.TerraCommand;
import org.terraform.main.TerraformGeneratorPlugin;

public class BlockDataTestCommand extends TerraCommand {
   public BlockDataTestCommand(TerraformGeneratorPlugin plugin, String... aliases) {
      super(plugin, aliases);
   }

   @NotNull
   public String getDefaultDescription() {
      return "Shows some new blockdata values in 1.16";
   }

   public boolean canConsoleExec() {
      return true;
   }

   public boolean hasPermission(@NotNull CommandSender sender) {
      return sender.isOp();
   }

   public void execute(@NotNull CommandSender sender, Stack<String> args) {
      Wall facing = (Wall)Bukkit.createBlockData(Material.COBBLESTONE_WALL);
      sender.sendMessage(facing.getAsString());
   }
}
