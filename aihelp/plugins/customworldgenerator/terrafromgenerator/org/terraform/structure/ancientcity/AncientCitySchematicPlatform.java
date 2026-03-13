package org.terraform.structure.ancientcity;

import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Random;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.SimpleLocation;
import org.terraform.data.TerraformWorld;
import org.terraform.main.TerraformGeneratorPlugin;
import org.terraform.schematic.TerraSchematic;
import org.terraform.structure.room.CubeRoom;
import org.terraform.structure.room.RoomLayoutGenerator;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;
import org.terraform.utils.SphereBuilder;

public class AncientCitySchematicPlatform extends AncientCityAbstractRoomPopulator {
   @NotNull
   private final String[] smallSchematics = new String[]{"ancient-city/ancient-city-wood-tower-1", "ancient-city/ancient-city-rock-tower-1", "ancient-city/ancient-city-lamp"};
   @NotNull
   private final String[] mediumSchematics = new String[]{"ancient-city/ancient-city-hot-tub", "ancient-city/ancient-city-warehouse"};
   @NotNull
   private final String[] largeSchematics = new String[]{"ancient-city/ancient-city-pantheon"};

   public AncientCitySchematicPlatform(TerraformWorld tw, HashSet<SimpleLocation> occupied, RoomLayoutGenerator gen, Random rand, boolean forceSpawn, boolean unique) {
      super(tw, gen, rand, forceSpawn, unique);
   }

   public void populate(@NotNull PopulatorDataAbstract data, @NotNull CubeRoom room) {
      super.populate(data, room);
      int platformSize = 0;
      if (this.effectiveRoom.getWidthX() >= 10 && this.effectiveRoom.getWidthZ() >= 10) {
         platformSize = 1;
      }

      if (this.effectiveRoom.getWidthX() >= 16 && this.effectiveRoom.getWidthZ() >= 16) {
         platformSize = 2;
      }

      String var10000;
      switch(platformSize) {
      case 0:
         TerraformGeneratorPlugin.logger.info("Small Schematic");
         var10000 = this.smallSchematics[this.rand.nextInt(this.smallSchematics.length)];
         break;
      case 1:
         TerraformGeneratorPlugin.logger.info("Medium Schematic");
         var10000 = this.mediumSchematics[this.rand.nextInt(this.mediumSchematics.length)];
         break;
      case 2:
         TerraformGeneratorPlugin.logger.info("Large Schematic");
         var10000 = this.largeSchematics[this.rand.nextInt(this.largeSchematics.length)];
         break;
      default:
         var10000 = null;
      }

      String chosenSchema = var10000;

      try {
         SimpleBlock center = this.effectiveRoom.getCenterSimpleBlock(data).getUp();
         TerraSchematic schema = TerraSchematic.load(chosenSchema, center);
         schema.parser = new AncientCitySchematicParser();
         schema.setFace(BlockUtils.getDirectBlockFace(this.rand));
         schema.apply();
         if (GenUtils.chance(this.rand, 1, 2)) {
            (new SphereBuilder(new Random(), center.getRelative(BlockUtils.getXZPlaneBlockFace(this.rand), 4).getUp(11), new Material[]{Material.CAVE_AIR})).setHardReplace(true).setRadius((float)GenUtils.randDouble(this.rand, 3.0D, 5.0D)).build();
         }
      } catch (FileNotFoundException var7) {
         TerraformGeneratorPlugin.logger.stackTrace(var7);
      }

      super.sculkUp(this.tw, data, this.effectiveRoom);
   }

   public boolean canPopulate(CubeRoom room) {
      return true;
   }
}
