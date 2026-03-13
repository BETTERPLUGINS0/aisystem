package org.terraform.command;

import java.util.Stack;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.terraform.command.contants.TerraCommand;
import org.terraform.main.TerraformGeneratorPlugin;
import org.terraform.schematic.SchematicListener;

public class WandCommand extends TerraCommand {
   public WandCommand(TerraformGeneratorPlugin plugin, String... aliases) {
      super(plugin, aliases);
   }

   @NotNull
   public String getDefaultDescription() {
      return "Gives a schematic wand";
   }

   public boolean canConsoleExec() {
      return false;
   }

   public boolean hasPermission(@NotNull CommandSender sender) {
      return sender.isOp();
   }

   public void execute(CommandSender sender, Stack<String> args) {
      Player p = (Player)sender;
      p.getInventory().addItem(new ItemStack[]{SchematicListener.getWand()});
      p.sendMessage(String.valueOf(ChatColor.GREEN) + "Wand added.");
   }
}
