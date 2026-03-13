package org.terraform.utils.version;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.populatordata.IPopulatorDataBeehiveEditor;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.main.TerraformGeneratorPlugin;
import org.terraform.main.config.TConfig;

public class BeeHiveSpawner {
   public static void spawnFullBeeNest(@NotNull SimpleBlock block) {
      if (TConfig.areAnimalsEnabled()) {
         try {
            PopulatorDataAbstract var2 = block.getPopData();
            if (var2 instanceof IPopulatorDataBeehiveEditor) {
               IPopulatorDataBeehiveEditor ipopdata = (IPopulatorDataBeehiveEditor)var2;
               ipopdata.setBeehiveWithBee(block.getX(), block.getY(), block.getZ());
            } else {
               block.setType(Material.BEE_NEST);
               block.getPopData().addEntity(block.getX(), block.getY(), block.getZ(), EntityType.BEE);
            }
         } catch (NullPointerException var3) {
            TerraformGeneratorPlugin.logger.info("Beehive null silently ignored");
         }

      }
   }
}
