package org.terraform.command;

import java.util.Arrays;
import java.util.Stack;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.terraform.biome.BiomeBank;
import org.terraform.biome.BiomeBlender;
import org.terraform.biome.BiomeSection;
import org.terraform.command.contants.TerraCommand;
import org.terraform.coregen.HeightMap;
import org.terraform.coregen.populatordata.PopulatorDataPostGen;
import org.terraform.data.MegaChunk;
import org.terraform.data.TerraformWorld;
import org.terraform.main.TerraformGeneratorPlugin;
import org.terraform.structure.SingleMegaChunkStructurePopulator;
import org.terraform.structure.StructureBufferDistanceHandler;
import org.terraform.structure.StructureRegistry;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;

public class CheckHeightCommand extends TerraCommand {
   public CheckHeightCommand(TerraformGeneratorPlugin plugin, String... aliases) {
      super(plugin, aliases);
   }

   @NotNull
   public String getDefaultDescription() {
      return "Checks the heights of various noise maps";
   }

   public boolean canConsoleExec() {
      return false;
   }

   public boolean hasPermission(@NotNull CommandSender sender) {
      return sender.isOp();
   }

   public void execute(CommandSender sender, Stack<String> args) {
      Player p = (Player)sender;
      int x = p.getLocation().getBlockX();
      int z = p.getLocation().getBlockZ();
      TerraformWorld tw = TerraformWorld.get(p.getWorld());
      MegaChunk mc = new MegaChunk(x, 0, z);
      BiomeBank.debugPrint = true;
      BiomeBank biome = tw.getBiomeBank(x, z);
      BiomeBank.debugPrint = false;
      BiomeSection section = BiomeBank.getBiomeSectionFromBlockCoords(tw, x, z);
      PopulatorDataPostGen data = new PopulatorDataPostGen(p.getLocation().getChunk());
      p.sendMessage("[CH]===============================");
      double var10001 = HeightMap.CORE.getHeight(tw, x, z);
      p.sendMessage("Core Height: " + var10001);
      var10001 = HeightMap.ATTRITION.getHeight(tw, x, z);
      p.sendMessage("Attrition Height: " + var10001);
      var10001 = HeightMap.getNoiseGradient(tw, x, z, 2);
      p.sendMessage("Gradient (2,3,4): " + var10001 + "," + HeightMap.getNoiseGradient(tw, x, z, 3) + "," + HeightMap.getNoiseGradient(tw, x, z, 4));
      var10001 = HeightMap.getTrueHeightGradient(data, x, z, 2);
      p.sendMessage("True Gradient (2,3,4): " + var10001 + "," + HeightMap.getTrueHeightGradient(data, x, z, 3) + "," + HeightMap.getTrueHeightGradient(data, x, z, 4));
      int var20 = HeightMap.getBlockHeight(tw, x, z);
      p.sendMessage("Result height: " + var20);
      var10001 = HeightMap.getRawRiverDepth(tw, x, z);
      p.sendMessage("River Depth: " + var10001);
      var20 = mc.getX();
      p.sendMessage("Mega Chunk: " + var20 + "," + mc.getZ());
      var20 = mc.getCenterBlockCoords()[0];
      p.sendMessage("Mega Chunk Center: " + var20 + "," + mc.getCenterBlockCoords()[1]);
      var20 = mc.getCenterBiomeSectionBlockCoords()[0];
      p.sendMessage("Mega Chunk BiomeSection Center: " + var20 + "," + mc.getCenterBiomeSectionBlockCoords()[1]);
      p.sendMessage("Biome Section: " + String.valueOf(section));
      p.sendMessage("Biome Section Climate: " + String.valueOf(section.getClimate()));
      p.sendMessage("Biome Section Elevation: " + section.getOceanLevel());
      p.sendMessage("Surrounding Sections:");
      BlockFace[] var11 = BlockUtils.directBlockFaces;
      int var12 = var11.length;

      int var13;
      String var21;
      for(var13 = 0; var13 < var12; ++var13) {
         BlockFace face = var11[var13];
         BiomeSection rel = section.getRelative(face.getModX(), face.getModZ());
         var21 = String.valueOf(rel);
         p.sendMessage("    - " + var21 + "(" + String.valueOf(rel.getBiomeBank()) + ")");
      }

      SingleMegaChunkStructurePopulator[] var17 = StructureRegistry.getLargeStructureForMegaChunk(tw, mc);
      var12 = var17.length;

      for(var13 = 0; var13 < var12; ++var13) {
         SingleMegaChunkStructurePopulator spop = var17[var13];
         if (spop != null) {
            int[] coords = mc.getCenterBlockCoords();
            int dist = (int)Math.sqrt(Math.pow((double)(x - coords[0]), 2.0D) + Math.pow((double)(z - coords[1]), 2.0D));
            var21 = spop.getClass().getSimpleName();
            p.sendMessage(" - Structure Registered: " + var21 + "(" + coords[0] + "," + coords[1] + ") " + dist + " blocks away");
         }
      }

      boolean[] var22 = StructureBufferDistanceHandler.canDecorateChunk(tw, x >> 4, z >> 4);
      p.sendMessage("Can decorate chunk: " + Arrays.toString(var22));
      BiomeSection var23 = BiomeBank.getBiomeSectionFromBlockCoords(tw, x, z);
      p.sendMessage("Temperature: " + var23.getTemperature());
      var23 = BiomeBank.getBiomeSectionFromBlockCoords(tw, x, z);
      p.sendMessage("Moisture: " + var23.getMoisture());
      var10001 = (new BiomeBlender(tw, true, true)).setGridBlendingFactor(2.0D).setSmoothBlendTowardsRivers(7).getEdgeFactor(biome, x, z);
      p.sendMessage("Biome edge factor (Gorge): " + var10001);
      p.sendMessage("Result Biome: " + String.valueOf(biome));
      var20 = GenUtils.getHighestGround(data, x, z);
      p.sendMessage("Highest Ground: " + var20);
      var20 = GenUtils.getTransformedHeight(data.getTerraformWorld(), x, z);
      p.sendMessage("Transformed Height: " + var20);
   }
}
