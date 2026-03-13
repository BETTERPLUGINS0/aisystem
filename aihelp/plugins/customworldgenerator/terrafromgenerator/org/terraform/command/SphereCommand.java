package org.terraform.command;

import java.util.Random;
import java.util.Stack;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.terraform.command.contants.TerraCommand;
import org.terraform.coregen.populatordata.PopulatorDataPostGen;
import org.terraform.data.SimpleBlock;
import org.terraform.main.TerraformGeneratorPlugin;
import org.terraform.utils.noise.FastNoise;

public class SphereCommand extends TerraCommand {
   public SphereCommand(TerraformGeneratorPlugin plugin, String... aliases) {
      super(plugin, aliases);
   }

   @NotNull
   public String getDefaultDescription() {
      return "Spawns a randomised simplex sphere";
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
      Random r = new Random();
      int seed = r.nextInt(9999);
      float trueRadius = 4.0F;
      FastNoise noise = new FastNoise(seed);
      noise.SetNoiseType(FastNoise.NoiseType.Simplex);
      noise.SetFrequency(0.09F);
      SimpleBlock block = new SimpleBlock(data, p.getLocation().getBlock());

      for(float x = -trueRadius; x <= trueRadius; ++x) {
         for(float y = -trueRadius; y <= trueRadius; ++y) {
            for(float z = -trueRadius; z <= trueRadius; ++z) {
               double radiusSquared = Math.pow((double)(trueRadius + noise.GetNoise(x, y, z) * 2.0F), 2.0D);
               SimpleBlock rel = block.getRelative(Math.round(x), Math.round(y), Math.round(z));
               if (rel.distanceSquared(block) <= radiusSquared) {
                  rel.setType(Material.STONE);
               }
            }
         }
      }

   }
}
