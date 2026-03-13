package org.terraform.command;

import java.util.Random;
import java.util.Stack;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.terraform.command.contants.TerraCommand;
import org.terraform.data.TerraformWorld;
import org.terraform.main.TerraformGeneratorPlugin;
import org.terraform.utils.GenUtils;
import org.terraform.utils.noise.FastNoise;
import org.terraform.utils.noise.NoiseCacheHandler;

public class ValuesCommand extends TerraCommand {
   public ValuesCommand(TerraformGeneratorPlugin plugin, String... aliases) {
      super(plugin, aliases);
   }

   @NotNull
   public String getDefaultDescription() {
      return "Shows a range of values for stuff";
   }

   public boolean canConsoleExec() {
      return true;
   }

   public boolean hasPermission(@NotNull CommandSender sender) {
      return sender.isOp();
   }

   private double warpSine(double tempUnwarpedSineX, int period, int seed) {
      double warp = (double)GenUtils.randInt(new Random(3L * (long)seed), -3, 3);
      if (warp == 0.0D) {
         warp = 1.0D;
      }

      if (warp < 0.0D) {
         warp = (10.0D - 2.0D * warp) / 10.0D;
      }

      double warpedValue;
      if (tempUnwarpedSineX == 0.0D && warp == 0.0D) {
         warpedValue = 0.0D;
      } else {
         warpedValue = Math.pow(Math.abs(tempUnwarpedSineX), warp);
      }

      if (tempUnwarpedSineX < 0.0D) {
         warpedValue = -warpedValue;
      }

      return warpedValue;
   }

   public void execute(@NotNull CommandSender sender, Stack<String> args) {
      ValuesCommand.MathValues vals = new ValuesCommand.MathValues();
      ValuesCommand.MathValues unwarped = new ValuesCommand.MathValues();
      ValuesCommand.MathValues warped = new ValuesCommand.MathValues();
      TerraformWorld tw = TerraformWorld.get("world-1232341234", (long)(new Random()).nextInt(99999));
      FastNoise carverEntranceStandard = NoiseCacheHandler.getNoise(tw, NoiseCacheHandler.NoiseCacheEntry.CARVER_STANDARD, (world) -> {
         FastNoise n = new FastNoise((int)(world.getSeed() * 111L));
         n.SetNoiseType(FastNoise.NoiseType.SimplexFractal);
         n.SetFractalOctaves(4);
         n.SetFrequency(0.07F);
         return n;
      });
      int period = true;

      for(int x = 0; x < 9000000; ++x) {
         int y = GenUtils.randInt(0, 100);
         int z = GenUtils.randInt(-10000, 10000);
         vals.addValue((double)carverEntranceStandard.GetNoise((float)x, (float)y, (float)z));
      }

      sender.sendMessage("Finished");
      sender.sendMessage("Highest: " + vals.getHighest());
      sender.sendMessage("Lowest: " + vals.getLowest());
      sender.sendMessage("Mean: " + vals.avg());
      sender.sendMessage("Warped: " + String.valueOf(warped));
      sender.sendMessage("Unwarped" + String.valueOf(unwarped));
   }

   private static class MathValues {
      private double total = 0.0D;
      private double lowest = 99999.0D;
      private double highest = -99999.0D;
      private int count = 0;

      public MathValues() {
      }

      public void addValue(double value) {
         this.total += value;
         ++this.count;
         if (value < this.lowest) {
            this.lowest = value;
         }

         if (value > this.highest) {
            this.highest = value;
         }

      }

      public double avg() {
         return this.total / (double)this.count;
      }

      public double getLowest() {
         return this.lowest;
      }

      public double getHighest() {
         return this.highest;
      }

      @NotNull
      public String toString() {
         double var10000 = this.getLowest();
         return var10000 + " to " + this.getHighest() + ": " + this.avg();
      }
   }
}
