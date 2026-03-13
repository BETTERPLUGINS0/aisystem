package org.terraform.structure.mineshaft;

import java.io.FileNotFoundException;
import java.util.EnumSet;
import java.util.Random;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.Wall;
import org.terraform.main.TerraformGeneratorPlugin;
import org.terraform.schematic.TerraSchematic;
import org.terraform.structure.room.CubeRoom;
import org.terraform.structure.room.RoomPopulatorAbstract;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;

public class ShaftTopPopulator extends RoomPopulatorAbstract {
   public ShaftTopPopulator(Random rand, boolean forceSpawn, boolean unique) {
      super(rand, forceSpawn, unique);
   }

   public void populate(@NotNull PopulatorDataAbstract data, @NotNull CubeRoom room) {
      int[] lowerCorner = room.getLowerCorner(1);
      int[] upperCorner = room.getUpperCorner(1);
      int y = room.getY();

      for(int x = lowerCorner[0]; x <= upperCorner[0]; ++x) {
         for(int z = lowerCorner[1]; z <= upperCorner[1]; ++z) {
            SimpleBlock b = new SimpleBlock(data, x, y, z);
            if (b.getDown().getType() == Material.OAK_FENCE) {
               while(b.getDown().getType() == Material.OAK_FENCE) {
                  b = b.getDown();
               }

               while(b.getY() <= y) {
                  b.setType(Material.SCAFFOLDING);
                  b = b.getUp();
               }
            }
         }
      }

      Wall w = new Wall(new SimpleBlock(data, room.getX(), room.getY() + 3, room.getZ()));
      w = w.findCeiling(10);
      if (w != null) {
         try {
            Wall target = w.getRelative(0, -GenUtils.randInt(this.rand, 8, 10), 0);
            BlockUtils.carveCaveAir((new Random()).nextInt(777123), 3.0F, 5.0F, 3.0F, new SimpleBlock(data, target.getX(), room.getY(), target.getZ()), false, EnumSet.of(Material.BARRIER));
            TerraSchematic schema = TerraSchematic.load("ore-lift", target.get().getRelative(-1, 0, -1));
            schema.parser = new OreLiftSchematicParser();
            schema.setFace(BlockFace.NORTH);
            schema.apply();
            target.LPillar(w.getY() - target.getY(), this.rand, new Material[]{Material.OAK_FENCE});
         } catch (FileNotFoundException var9) {
            TerraformGeneratorPlugin.logger.stackTrace(var9);
         }
      }

   }

   public boolean canPopulate(CubeRoom room) {
      return false;
   }
}
