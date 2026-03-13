package org.terraform.structure.catacombs;

import java.util.Random;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.Wall;
import org.terraform.structure.room.CubeRoom;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.blockdata.StairBuilder;

public class CatacombsPillarRoomPopulator extends CatacombsStandardPopulator {
   public CatacombsPillarRoomPopulator(Random rand, boolean forceSpawn, boolean unique) {
      super(rand, forceSpawn, unique);
   }

   public void populate(@NotNull PopulatorDataAbstract data, @NotNull CubeRoom room) {
      super.populate(data, room);
      SimpleBlock center = room.getCenterSimpleBlock(data).getUp();
      center.LPillar(room.getHeight(), new Random(), Material.BONE_BLOCK);
      if (this.rand.nextBoolean()) {
         center.getUp(2).setType(Material.GOLD_BLOCK);
      }

      BlockFace[] var4 = BlockUtils.directBlockFaces;
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         BlockFace face = var4[var6];
         Wall target = new Wall(center.getRelative(face), face);
         (new StairBuilder(new Material[]{Material.STONE_BRICK_STAIRS, Material.COBBLESTONE_STAIRS})).setFacing(face.getOppositeFace()).apply(target);
         target.getUp(2).setType(Material.ANDESITE_WALL);
         target.getUp(2).CorrectMultipleFacing(1);
         target.getUp(3).getFront().setType(Material.ANDESITE_WALL);
         target.getUp(3).getFront().CorrectMultipleFacing(1);
         target.getUp(3).setType(Material.BONE_BLOCK);
         target.getUp(4).getFront().LPillar(room.getHeight() - 4, new Random(), new Material[]{Material.BONE_BLOCK});
      }

      super.spawnHangingChains(data, room);
   }
}
