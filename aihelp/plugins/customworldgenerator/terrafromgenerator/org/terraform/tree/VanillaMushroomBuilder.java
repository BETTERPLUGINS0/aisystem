package org.terraform.tree;

import java.io.FileNotFoundException;
import java.util.Random;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.TerraformWorld;
import org.terraform.data.Wall;
import org.terraform.main.TerraformGeneratorPlugin;
import org.terraform.main.config.TConfig;
import org.terraform.schematic.TerraSchematic;
import org.terraform.utils.GenUtils;

public class VanillaMushroomBuilder {
   public static final String RED_MUSHROOM_CAP = "redmushroomcap";
   public static final String BROWN_MUSHROOM_CAP = "brownmushroomcap";

   public static void buildVanillaMushroom(@NotNull TerraformWorld tw, @NotNull PopulatorDataAbstract data, int x, int y, int z, String capSchematic) {
      if (TConfig.areTallMushroomsEnabled()) {
         Random rand = tw.getRand(256L * (long)x + 16L * (long)y + (long)z);
         int height = GenUtils.randInt(rand, 5, 7);
         (new Wall(new SimpleBlock(data, x, y, z))).Pillar(height, new Material[]{Material.MUSHROOM_STEM});

         try {
            TerraSchematic capSchem = TerraSchematic.load(capSchematic, new SimpleBlock(data, x, y + height - 2, z));
            capSchem.apply();
         } catch (FileNotFoundException var9) {
            TerraformGeneratorPlugin.logger.stackTrace(var9);
         }

      }
   }
}
