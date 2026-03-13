package org.terraform.command;

import java.util.Stack;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.terraform.command.contants.TerraCommand;
import org.terraform.main.TerraformGeneratorPlugin;

public class NMSChunkQueryCommand extends TerraCommand {
   public NMSChunkQueryCommand(TerraformGeneratorPlugin plugin, String... aliases) {
      super(plugin, aliases);
   }

   @NotNull
   public String getDefaultDescription() {
      return "Queries NMS Chunk information";
   }

   public boolean canConsoleExec() {
      return false;
   }

   public boolean hasPermission(@NotNull CommandSender sender) {
      return sender.isOp();
   }

   public void execute(CommandSender sender, Stack<String> args) {
   }
}
