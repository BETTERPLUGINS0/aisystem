package org.terraform.command;

import java.util.Stack;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.terraform.command.contants.FractalTreeTypeArgument;
import org.terraform.command.contants.InvalidArgumentException;
import org.terraform.command.contants.TerraCommand;
import org.terraform.coregen.populatordata.PopulatorDataPostGen;
import org.terraform.data.TerraformWorld;
import org.terraform.main.TerraformGeneratorPlugin;
import org.terraform.tree.FractalTreeBuilder;
import org.terraform.tree.FractalTypes;

public class FractalTreeCommand extends TerraCommand {
   public FractalTreeCommand(TerraformGeneratorPlugin plugin, String... aliases) {
      super(plugin, aliases);
      this.parameters.add(new FractalTreeTypeArgument("type", false));
   }

   @NotNull
   public String getDefaultDescription() {
      return "Spawns a fractal tree";
   }

   public boolean canConsoleExec() {
      return false;
   }

   public boolean hasPermission(@NotNull CommandSender sender) {
      return sender.isOp();
   }

   public void execute(@NotNull CommandSender sender, @NotNull Stack<String> args) throws InvalidArgumentException {
      Player p = (Player)sender;
      PopulatorDataPostGen data = new PopulatorDataPostGen(p.getLocation().getChunk());
      TerraformWorld tw = TerraformWorld.get(p.getWorld());
      int x = p.getLocation().getBlockX();
      int y = p.getLocation().getBlockY();
      int z = p.getLocation().getBlockZ();

      try {
         (new FractalTreeBuilder((FractalTypes.Tree)this.parseArguments(sender, args).get(0))).build(tw, data, x, y, z);
      } catch (IllegalArgumentException var17) {
         sender.sendMessage(String.valueOf(ChatColor.RED) + "Invalid tree type.");
         sender.sendMessage(String.valueOf(ChatColor.RED) + "Valid types:");
         StringBuilder types = new StringBuilder();
         boolean b = true;
         FractalTypes.Tree[] var12 = FractalTypes.Tree.values();
         int var13 = var12.length;

         for(int var14 = 0; var14 < var13; ++var14) {
            FractalTypes.Tree type = var12[var14];
            ChatColor col = ChatColor.RED;
            if (b) {
               col = ChatColor.DARK_RED;
            }

            b = !b;
            types.append(col).append(type).append(' ');
         }

         sender.sendMessage(types.toString());
      }

   }
}
