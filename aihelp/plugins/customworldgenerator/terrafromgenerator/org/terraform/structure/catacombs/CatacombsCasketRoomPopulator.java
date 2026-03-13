package org.terraform.structure.catacombs;

import java.util.Random;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.TerraLootTable;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.Wall;
import org.terraform.main.config.TConfig;
import org.terraform.structure.room.CubeRoom;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.blockdata.ChestBuilder;
import org.terraform.utils.blockdata.RotatableBuilder;
import org.terraform.utils.blockdata.TrapdoorBuilder;

public class CatacombsCasketRoomPopulator extends CatacombsStandardPopulator {
   public CatacombsCasketRoomPopulator(Random rand, boolean forceSpawn, boolean unique) {
      super(rand, forceSpawn, unique);
   }

   public void populate(@NotNull PopulatorDataAbstract data, @NotNull CubeRoom room) {
      super.populate(data, room);
      SimpleBlock center = room.getCenterSimpleBlock(data).getUp();
      this.spawnCasket(new Wall(center, BlockUtils.getDirectBlockFace(this.rand)), this.rand);
      super.spawnHangingChains(data, room);
   }

   private void spawnCasket(@NotNull Wall target, @NotNull Random rand) {
      BlockFace[] var3 = BlockUtils.directBlockFaces;
      int var4 = var3.length;

      int var5;
      BlockFace face;
      for(var5 = 0; var5 < var4; ++var5) {
         face = var3[var5];
         if (face != target.getDirection()) {
            (new TrapdoorBuilder(Material.DARK_OAK_TRAPDOOR)).setOpen(true).setFacing(face).apply(target.getRelative(face));
         }
      }

      var3 = BlockUtils.directBlockFaces;
      var4 = var3.length;

      for(var5 = 0; var5 < var4; ++var5) {
         face = var3[var5];
         if (face != target.getDirection().getOppositeFace()) {
            (new TrapdoorBuilder(Material.DARK_OAK_TRAPDOOR)).setOpen(true).setFacing(face).apply(target.getFront().getRelative(face));
         }
      }

      (new TrapdoorBuilder(Material.DARK_OAK_TRAPDOOR)).setFacing(BlockUtils.getLeft(target.getDirection())).apply(target.getUp()).apply(target.getFront().getUp());
      switch(rand.nextInt(3)) {
      case 0:
         if (TConfig.areDecorationsEnabled()) {
            (new ChestBuilder(Material.CHEST)).setFacing(BlockUtils.getLeft(target.getDirection())).setLootTable(TerraLootTable.SIMPLE_DUNGEON).extend(target, target.getFront(), true);
         }
         break;
      case 1:
         if (TConfig.areDecorationsEnabled()) {
            (new RotatableBuilder(Material.SKELETON_SKULL)).setRotation(BlockUtils.getXZPlaneBlockFace(rand)).apply(target);
            target.getFront().setType(Material.REDSTONE_WIRE);
         }
         break;
      default:
         if (TConfig.areAnimalsEnabled()) {
            target.addEntity(EntityType.CAVE_SPIDER);
            target.getFront().addEntity(EntityType.CAVE_SPIDER);
         }
      }

   }
}
