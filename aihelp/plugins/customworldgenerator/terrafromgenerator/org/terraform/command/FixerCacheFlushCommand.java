package org.terraform.command;

import java.util.Stack;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.terraform.command.contants.TerraCommand;
import org.terraform.coregen.bukkit.NativeGeneratorPatcherPopulator;
import org.terraform.coregen.bukkit.PhysicsUpdaterPopulator;
import org.terraform.main.TerraformGeneratorPlugin;

public class FixerCacheFlushCommand extends TerraCommand {
   public FixerCacheFlushCommand(TerraformGeneratorPlugin plugin, String... aliases) {
      super(plugin, aliases);
   }

   @NotNull
   public String getDefaultDescription() {
      return "Flushes the chunk fixer cache.";
   }

   public boolean canConsoleExec() {
      return true;
   }

   public boolean hasPermission(@NotNull CommandSender sender) {
      return sender.isOp();
   }

   public void execute(@NotNull CommandSender sender, Stack<String> args) {
      NativeGeneratorPatcherPopulator.flushChanges();
      PhysicsUpdaterPopulator.flushChanges();
      sender.sendMessage("Flushing changes.");
   }
}
