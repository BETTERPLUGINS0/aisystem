package org.terraform.command;

import java.util.Random;
import java.util.Stack;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.terraform.command.contants.InvalidArgumentException;
import org.terraform.command.contants.MushroomTypeArgument;
import org.terraform.command.contants.TerraCommand;
import org.terraform.coregen.populatordata.PopulatorDataPostGen;
import org.terraform.data.TerraformWorld;
import org.terraform.main.TerraformGeneratorPlugin;
import org.terraform.tree.FractalTypes;
import org.terraform.tree.MushroomBuilder;

public class MushroomCommand extends TerraCommand {
   public MushroomCommand(TerraformGeneratorPlugin plugin, String... aliases) {
      super(plugin, aliases);
      this.parameters.add(new MushroomTypeArgument("type", true));
   }

   @NotNull
   public String getDefaultDescription() {
      return "Spawns a mushroom";
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
      int x = p.getLocation().getBlockX();
      int y = p.getLocation().getBlockY();
      int z = p.getLocation().getBlockZ();
      TerraformWorld tw = TerraformWorld.get(p.getWorld());
      if (!args.isEmpty()) {
         try {
            (new MushroomBuilder((FractalTypes.Mushroom)this.parseArguments(sender, args).get(0))).build(tw, data, x, y, z);
         } catch (IllegalArgumentException var17) {
            sender.sendMessage(String.valueOf(ChatColor.RED) + "Invalid mushroom type.");
            sender.sendMessage(String.valueOf(ChatColor.RED) + "Valid types:");
            StringBuilder types = new StringBuilder();
            boolean b = true;
            FractalTypes.Mushroom[] var12 = FractalTypes.Mushroom.values();
            int var13 = var12.length;

            for(int var14 = 0; var14 < var13; ++var14) {
               FractalTypes.Mushroom type = var12[var14];
               ChatColor col = ChatColor.RED;
               if (b) {
                  col = ChatColor.DARK_RED;
               }

               b = !b;
               types.append(col).append(type).append(' ');
            }

            sender.sendMessage(types.toString());
         }

      } else {
         if ((new Random()).nextBoolean()) {
            (new MushroomBuilder(FractalTypes.Mushroom.GIANT_RED_MUSHROOM)).build(TerraformWorld.get(p.getWorld()), data, x, y, z);
         } else {
            (new MushroomBuilder(FractalTypes.Mushroom.GIANT_BROWN_MUSHROOM)).build(TerraformWorld.get(p.getWorld()), data, x, y, z);
         }

      }
   }
}
