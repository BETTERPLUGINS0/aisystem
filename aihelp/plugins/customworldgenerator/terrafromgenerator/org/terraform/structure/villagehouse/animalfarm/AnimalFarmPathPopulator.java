package org.terraform.structure.villagehouse.animalfarm;

import java.util.Iterator;
import java.util.Random;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.terraform.data.Wall;
import org.terraform.structure.room.CubeRoom;
import org.terraform.structure.room.PathPopulatorAbstract;
import org.terraform.structure.room.PathPopulatorData;
import org.terraform.structure.room.RoomLayoutGenerator;
import org.terraform.utils.GenUtils;

public class AnimalFarmPathPopulator extends PathPopulatorAbstract {
   private final Random rand;
   private final RoomLayoutGenerator gen;

   public AnimalFarmPathPopulator(RoomLayoutGenerator gen, Random rand) {
      this.gen = gen;
      this.rand = rand;
   }

   public void populate(@NotNull PathPopulatorData ppd) {
      Wall w = new Wall(ppd.base, ppd.dir);
      Iterator var3 = this.gen.getRooms().iterator();

      CubeRoom room;
      do {
         if (!var3.hasNext()) {
            if (GenUtils.chance(this.rand, 1, 50)) {
               w.getLeft().getGround().getUp().setType(Material.CAMPFIRE);
            }

            if (GenUtils.chance(this.rand, 2, 10)) {
               w.getGround().setType((Material)GenUtils.randChoice((Object[])(Material.COBBLESTONE, Material.COARSE_DIRT, Material.MOSSY_COBBLESTONE)));
            }

            if (GenUtils.chance(this.rand, 2, 10)) {
               w.getLeft().getGround().setType((Material)GenUtils.randChoice((Object[])(Material.COBBLESTONE, Material.COARSE_DIRT, Material.MOSSY_COBBLESTONE)));
            }

            if (GenUtils.chance(this.rand, 2, 10)) {
               w.getRight().getGround().setType((Material)GenUtils.randChoice((Object[])(Material.COBBLESTONE, Material.COARSE_DIRT, Material.MOSSY_COBBLESTONE)));
            }

            return;
         }

         room = (CubeRoom)var3.next();
      } while(!room.isPointInside(new int[]{w.get().getX(), w.get().getZ()}));

   }
}
