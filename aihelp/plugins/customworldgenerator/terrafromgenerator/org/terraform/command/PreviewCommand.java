package org.terraform.command;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Stack;
import java.util.UUID;
import javax.imageio.ImageIO;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.block.Biome;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.CommandSender;
import org.bukkit.generator.WorldInfo;
import org.bukkit.generator.ChunkGenerator.ChunkData;
import org.bukkit.material.MaterialData;
import org.jetbrains.annotations.NotNull;
import org.terraform.biome.BiomeBank;
import org.terraform.biome.BiomeType;
import org.terraform.command.contants.TerraCommand;
import org.terraform.coregen.bukkit.TerraformGenerator;
import org.terraform.data.TerraformWorld;
import org.terraform.main.TerraformGeneratorPlugin;

public class PreviewCommand extends TerraCommand {
   public PreviewCommand(TerraformGeneratorPlugin plugin, String... aliases) {
      super(plugin, aliases);
   }

   @NotNull
   public String getDefaultDescription() {
      return "Shows a preview of a specified generation technique";
   }

   public boolean canConsoleExec() {
      return true;
   }

   public boolean hasPermission(@NotNull CommandSender sender) {
      return sender.isOp();
   }

   public void execute(CommandSender sender, Stack<String> args) {
      int maxX = 160;
      int maxY = TerraformGeneratorPlugin.injector.getMaxY() - TerraformGeneratorPlugin.injector.getMinY();
      TerraformWorld tw = TerraformWorld.get("test-world-" + (new Random()).nextInt(99999), (long)(new Random()).nextInt(99999));
      PreviewCommand.ImageWorldInfo iwi = new PreviewCommand.ImageWorldInfo(tw.getName(), tw.getSeed());
      BufferedImage img = new BufferedImage(maxX, maxY + maxX, 1);
      File f = new File("terra-preview.png");
      if (f.exists()) {
         f.delete();
      }

      TerraformGenerator generator = new TerraformGenerator();

      for(int x = -maxX / 2 >> 4; x < maxX / 2 >> 4; ++x) {
         for(int z = -maxX / 2 >> 4; z < maxX / 2 >> 4; ++z) {
            PreviewCommand.ImageChunkData icd = new PreviewCommand.ImageChunkData(img, x, z, maxX, maxY);
            generator.generateNoise(iwi, tw.getHashedRand(1L, x, z), x, z, icd);
            generator.generateSurface(iwi, tw.getHashedRand(1L, x, z), x, z, icd);
            generator.generateCaves(iwi, tw.getHashedRand(1L, x, z), x, z, icd);
         }
      }

      try {
         f = new File("terra-preview.png");
         ImageIO.write(img, "png", f);
      } catch (IOException var13) {
         System.out.println(var13);
      }

      System.out.println("Done.");
   }

   private Color getClimateColor(@NotNull BiomeBank bank) {
      if (bank.getType() != BiomeType.OCEANIC && bank.getType() != BiomeType.DEEP_OCEANIC) {
         Color var10000;
         switch(bank.getClimate()) {
         case HUMID_VEGETATION:
            var10000 = new Color(118, 163, 3);
            break;
         case DRY_VEGETATION:
            var10000 = new Color(172, 187, 2);
            break;
         case HOT_BARREN:
            var10000 = Color.red;
            break;
         case COLD:
            var10000 = new Color(59, 255, 150);
            break;
         case SNOWY:
            var10000 = Color.white;
            break;
         case TRANSITION:
            var10000 = new Color(59, 255, 59);
            break;
         default:
            throw new IncompatibleClassChangeError();
         }

         return var10000;
      } else {
         return Color.blue;
      }
   }

   private Color getBiomeColor(@NotNull BiomeBank bank) {
      switch(bank) {
      case SNOWY_WASTELAND:
         return Color.white;
      case SNOWY_TAIGA:
         return new Color(217, 234, 211);
      case ICE_SPIKES:
         return new Color(207, 226, 243);
      case TAIGA:
         return new Color(56, 118, 29);
      case PLAINS:
         return new Color(59, 255, 59);
      case ERODED_PLAINS:
         return new Color(59, 255, 150);
      case DARK_FOREST:
         return new Color(39, 78, 19);
      case SAVANNA:
         return new Color(172, 187, 2);
      case FOREST:
         return new Color(106, 168, 79);
      case JUNGLE:
         return new Color(118, 163, 3);
      case BAMBOO_FOREST:
         return new Color(0, 255, 186);
      case DESERT:
         return Color.yellow;
      case BADLANDS:
         return Color.red;
      default:
         return bank.getType() != BiomeType.OCEANIC && bank.getType() != BiomeType.DEEP_OCEANIC ? Color.pink : Color.blue;
      }
   }

   @NotNull
   private Color getHeightColorFromNoise(int noise) {
      if (noise <= 62) {
         return new Color(50, 50, 100 + noise * 2);
      } else if (noise < 66) {
         return new Color(240, 238, 108);
      } else {
         return noise < 92 ? new Color(37, 70 + noise * 2, 2) : new Color(255, 255, 255);
      }
   }

   private static class ImageWorldInfo implements WorldInfo {
      private final String name;
      private final long seed;

      private ImageWorldInfo(String name, long seed) {
         this.name = name;
         this.seed = seed;
      }

      @NotNull
      public String getName() {
         return this.name;
      }

      @NotNull
      public UUID getUID() {
         return null;
      }

      @NotNull
      public Environment getEnvironment() {
         return null;
      }

      public long getSeed() {
         return this.seed;
      }

      public int getMinHeight() {
         return 0;
      }

      public int getMaxHeight() {
         return 0;
      }
   }

   private static class ImageChunkData implements ChunkData {
      final BufferedImage img;
      final int chunkX;
      final int chunkZ;
      final int maxX;
      final int maxY;
      private final int[][] maxHeights = new int[16][16];

      private ImageChunkData(BufferedImage img, int chunkX, int chunkZ, int maxX, int maxY) {
         this.img = img;
         this.chunkX = chunkX;
         this.chunkZ = chunkZ;
         this.maxY = maxY;
         this.maxX = maxX;
      }

      public void setBlock(int x, int y, int z, @NotNull Material material) {
         Color col;
         switch(material) {
         case STONE:
            col = Color.LIGHT_GRAY;
            break;
         case DEEPSLATE:
            col = Color.GRAY;
            break;
         case WATER:
            col = Color.CYAN;
            break;
         case CAVE_AIR:
            col = Color.RED.darker();
            break;
         default:
            col = Color.GREEN.darker();
         }

         if (z == 0 && this.chunkZ == 0) {
            this.img.setRGB(this.maxX / 2 + x + this.chunkX * 16, this.maxY - (y - TerraformGeneratorPlugin.injector.getMinY()) - 1, col.getRGB());
         }

         if (y >= this.maxHeights[x][z]) {
            this.maxHeights[x][z] = y;
            if ((y - TerraformGenerator.seaLevel) % 3 == 0) {
               if (y > TerraformGenerator.seaLevel) {
                  col = col.brighter();
               } else {
                  col = col.darker();
               }
            }

            this.img.setRGB(this.maxX / 2 + x + this.chunkX * 16, this.maxY + this.maxX / 2 + z + this.chunkZ * 16, col.getRGB());
         }

      }

      public void setBlock(int i, int i1, int i2, @NotNull BlockData blockData) {
      }

      public int getMinHeight() {
         return 0;
      }

      public int getMaxHeight() {
         return 0;
      }

      @NotNull
      public Material getType(int i, int i1, int i2) {
         return null;
      }

      @NotNull
      public BlockData getBlockData(int i, int i1, int i2) {
         return null;
      }

      @NotNull
      public Biome getBiome(int i, int i1, int i2) {
         return null;
      }

      public void setBlock(int i, int i1, int i2, @NotNull MaterialData materialData) {
      }

      public void setRegion(int i, int i1, int i2, int i3, int i4, int i5, @NotNull Material material) {
      }

      public void setRegion(int i, int i1, int i2, int i3, int i4, int i5, @NotNull MaterialData materialData) {
      }

      public void setRegion(int i, int i1, int i2, int i3, int i4, int i5, @NotNull BlockData blockData) {
      }

      @NotNull
      public MaterialData getTypeAndData(int i, int i1, int i2) {
         return null;
      }

      public byte getData(int i, int i1, int i2) {
         return 0;
      }
   }
}
