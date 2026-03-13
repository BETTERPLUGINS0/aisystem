package org.terraform.command;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Stack;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.terraform.biome.BiomeBank;
import org.terraform.biome.BiomeClimate;
import org.terraform.biome.BiomeSection;
import org.terraform.biome.BiomeType;
import org.terraform.command.contants.TerraCommand;
import org.terraform.data.TerraformWorld;
import org.terraform.main.TerraformGeneratorPlugin;

public class BiomeDistribCommand extends TerraCommand {
   public BiomeDistribCommand(TerraformGeneratorPlugin plugin, String... aliases) {
      super(plugin, aliases);
   }

   @NotNull
   public String getDefaultDescription() {
      return "Displays a test for biome distribution with the current configuration options";
   }

   public boolean canConsoleExec() {
      return true;
   }

   public boolean hasPermission(@NotNull CommandSender sender) {
      return sender.isOp();
   }

   public void execute(@NotNull CommandSender sender, Stack<String> args) {
      HashMap<BiomeBank, Integer> counts = new HashMap();
      HashMap<BiomeClimate, Integer> climates = new HashMap();
      BiomeDistribCommand.MathValues temperature = new BiomeDistribCommand.MathValues();
      BiomeDistribCommand.MathValues moisture = new BiomeDistribCommand.MathValues();
      double numOceans = 0.0D;
      double numMountains = 0.0D;
      double total = 0.0D;
      TerraformWorld tw = TerraformWorld.get("world-" + (new Random()).nextInt(99999), (long)(new Random()).nextInt(99999));

      int val;
      for(int nx = -50; nx < 50; ++nx) {
         for(val = -50; val < 50; ++val) {
            BiomeSection sect = BiomeBank.getBiomeSectionFromSectionCoords(tw, nx, val, true);
            if (sect.getBiomeBank().getType() == BiomeType.OCEANIC || sect.getBiomeBank().getType() == BiomeType.DEEP_OCEANIC) {
               ++numOceans;
            }

            if (sect.getBiomeBank().getType() == BiomeType.MOUNTAINOUS || sect.getBiomeBank().getType() == BiomeType.HIGH_MOUNTAINOUS) {
               ++numMountains;
            }

            temperature.addValue((double)sect.getTemperature());
            moisture.addValue((double)sect.getMoisture());
            if (!counts.containsKey(sect.getBiomeBank())) {
               counts.put(sect.getBiomeBank(), 1);
            } else {
               counts.put(sect.getBiomeBank(), (Integer)counts.get(sect.getBiomeBank()) + 1);
            }

            if (!climates.containsKey(sect.getBiomeBank().getClimate())) {
               climates.put(sect.getBiomeBank().getClimate(), 1);
            } else {
               climates.put(sect.getBiomeBank().getClimate(), (Integer)climates.get(sect.getBiomeBank().getClimate()) + 1);
            }
         }
      }

      sender.sendMessage("Temperature: " + String.valueOf(temperature));
      sender.sendMessage("Moisture: " + String.valueOf(moisture));

      Iterator var25;
      for(var25 = counts.values().iterator(); var25.hasNext(); total += (double)val) {
         val = (Integer)var25.next();
      }

      BiomeBank[] var26 = BiomeBank.values();
      val = var26.length;

      Object var10000;
      String count;
      String percent;
      int var28;
      long var29;
      String var30;
      for(var28 = 0; var28 < val; ++var28) {
         BiomeBank b = var26[var28];
         if (b.getType() != BiomeType.BEACH && b.getType() != BiomeType.RIVER) {
            var10000 = counts.getOrDefault(b, 0);
            count = String.valueOf(var10000).makeConcatWithConstants<invokedynamic>(String.valueOf(var10000));
            var29 = Math.round((double)(100 * (Integer)counts.getOrDefault(b, 0)) / total);
            percent = "(" + var29;
            if (count.equals("0")) {
               var30 = String.valueOf(ChatColor.RED);
               count = var30 + count;
            }

            if ((double)(100 * (Integer)counts.getOrDefault(b, 0)) / total < 5.0D) {
               var30 = String.valueOf(ChatColor.RED);
               percent = var30 + percent;
            }

            sender.sendMessage("%-35s(%-10s, %-10s): %-10s%s)".formatted(new Object[]{b.toString(), b.getClimate().getTemperatureRange(), b.getClimate().getMoistureRange(), count, percent + "%)"}));
         }
      }

      sender.sendMessage("=====================================");
      sender.sendMessage("Percent Ocean: " + 100.0D * numOceans / total + "%");
      sender.sendMessage("Percent Mountain: " + 100.0D * numMountains / total + "%");
      sender.sendMessage("===================================");
      total = 0.0D;

      for(var25 = climates.values().iterator(); var25.hasNext(); total += (double)val) {
         val = (Integer)var25.next();
      }

      BiomeClimate[] var27 = BiomeClimate.values();
      val = var27.length;

      for(var28 = 0; var28 < val; ++var28) {
         BiomeClimate c = var27[var28];
         var10000 = climates.getOrDefault(c, 0);
         count = String.valueOf(var10000).makeConcatWithConstants<invokedynamic>(String.valueOf(var10000));
         var29 = Math.round((double)(100 * (Integer)climates.getOrDefault(c, 0)) / total);
         percent = "(" + var29;
         if (count.equals("0")) {
            var30 = String.valueOf(ChatColor.RED);
            count = var30 + count;
         }

         if ((double)(100 * (Integer)climates.getOrDefault(c, 0)) / total < 5.0D) {
            var30 = String.valueOf(ChatColor.RED);
            percent = var30 + percent;
         }

         int biomeTypes = 0;
         BiomeBank[] var21 = BiomeBank.values();
         int var22 = var21.length;

         for(int var23 = 0; var23 < var22; ++var23) {
            BiomeBank b = var21[var23];
            if (b.getClimate() == c && (b.getType() == BiomeType.FLAT || b.getType() == BiomeType.MOUNTAINOUS || b.getType() == BiomeType.HIGH_MOUNTAINOUS)) {
               ++biomeTypes;
            }
         }

         float biomesPerPercent = (float)((double)biomeTypes / ((double)(100 * (Integer)climates.getOrDefault(c, 0)) / total));
         sender.sendMessage("%-30s%-10s %-10s (%d registered biomes) (numBiomes/percent: %.2f)".formatted(new Object[]{c.toString(), count, percent + "%)", biomeTypes, biomesPerPercent}));
      }

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
