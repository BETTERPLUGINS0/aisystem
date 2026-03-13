package org.terraform.command;

import java.util.Stack;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.terraform.command.contants.TerraCommand;
import org.terraform.main.TerraformGeneratorPlugin;

public class CaveCommand extends TerraCommand {
   public CaveCommand(TerraformGeneratorPlugin plugin, String... aliases) {
      super(plugin, aliases);
   }

   @NotNull
   public String getDefaultDescription() {
      return "Digs a cave at your location";
   }

   public boolean canConsoleExec() {
      return false;
   }

   public boolean hasPermission(@NotNull CommandSender sender) {
      return sender.isOp();
   }

   public void execute(@NotNull CommandSender sender, Stack<String> args) {
      sender.sendMessage("Unimplemented.");
   }
}
