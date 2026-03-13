package org.terraform.structure.pillager.mansion.secondfloor;

import java.util.HashMap;
import java.util.Random;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected.Half;
import org.bukkit.block.data.type.Slab.Type;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.Wall;
import org.terraform.structure.pillager.mansion.MansionInternalWallState;
import org.terraform.structure.pillager.mansion.MansionRoomPopulator;
import org.terraform.structure.pillager.mansion.MansionRoomSize;
import org.terraform.structure.room.CubeRoom;
import org.terraform.utils.ArmorStandUtils;
import org.terraform.utils.BannerUtils;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.blockdata.OrientableBuilder;
import org.terraform.utils.blockdata.SlabBuilder;
import org.terraform.utils.blockdata.StairBuilder;

public class MansionSecondFloorGrandStairwayPopulator extends MansionRoomPopulator {
   public MansionSecondFloorGrandStairwayPopulator(CubeRoom room, HashMap<BlockFace, MansionInternalWallState> internalWalls) {
      super(room, internalWalls);
   }

   public void decorateRoom(PopulatorDataAbstract data, Random random) {
   }

   public void decorateWall(@NotNull Random rand, @NotNull Wall w) {
      w.getLeft(3).Pillar(6, new Material[]{Material.DARK_OAK_LOG});
      w.getUp(5).setType(Material.DARK_OAK_PLANKS);
      w.getRight(3).Pillar(6, new Material[]{Material.DARK_OAK_LOG});
      (new StairBuilder(Material.DARK_OAK_STAIRS)).setHalf(Half.TOP).setFacing(BlockUtils.getLeft(w.getDirection())).apply(w.getUp(4).getLeft(2)).apply(w.getUp(5).getLeft(2)).apply(w.getUp(5).getLeft()).setFacing(BlockUtils.getRight(w.getDirection())).apply(w.getUp(4).getRight(2)).apply(w.getUp(5).getRight(2)).apply(w.getUp(5).getRight());
      int choice = rand.nextInt(2);
      if (choice == 0) {
         w.getRear().Pillar(5, new Material[]{Material.DARK_OAK_LOG});
         (new OrientableBuilder(Material.DARK_OAK_LOG)).setAxis(BlockUtils.getAxisFromBlockFace(w.getDirection())).apply(w.getRear().getUp(2)).apply(w.getRear().getUp().getLeft()).apply(w.getRear().getUp().getRight()).apply(w.getRear().getUp(3).getLeft()).apply(w.getRear().getUp(3).getRight()).setAxis(BlockUtils.getAxisFromBlockFace(BlockUtils.getLeft(w.getDirection()))).apply(w.getRear().getUp(2).getLeft()).apply(w.getRear().getUp(2).getLeft(2)).apply(w.getRear().getUp(2).getRight()).apply(w.getRear().getUp(2).getRight(2));
         BlockFace[] var4 = BlockUtils.directBlockFaces;
         int var5 = var4.length;

         int var6;
         BlockFace face;
         for(var6 = 0; var6 < var5; ++var6) {
            face = var4[var6];
            (new StairBuilder(Material.POLISHED_ANDESITE_STAIRS)).setFacing(face.getOppositeFace()).lapply(w.getLeft(3).getRelative(face));
         }

         var4 = BlockUtils.directBlockFaces;
         var5 = var4.length;

         for(var6 = 0; var6 < var5; ++var6) {
            face = var4[var6];
            (new StairBuilder(Material.POLISHED_ANDESITE_STAIRS)).setFacing(face.getOppositeFace()).lapply(w.getRight(3).getRelative(face));
         }
      } else {
         BannerUtils.generatePillagerBanner(w.getUp(4).get(), w.getDirection(), true);
         (new SlabBuilder(Material.POLISHED_ANDESITE_SLAB)).setType(Type.TOP).apply(w).apply(w.getLeft()).apply(w.getLeft(2)).apply(w.getRight()).apply(w.getRight(2));
         ArmorStandUtils.placeArmorStand(w.getUp(2).get(), w.getDirection(), rand);
         ArmorStandUtils.placeArmorStand(w.getUp(2).getLeft(2).get(), w.getDirection(), rand);
         ArmorStandUtils.placeArmorStand(w.getUp(2).getRight(2).get(), w.getDirection(), rand);
      }

   }

   public void decorateWindow(Random rand, @NotNull Wall w) {
      w.getRear().Pillar(6, new Material[]{Material.DARK_OAK_PLANKS});
      w.Pillar(6, new Material[]{Material.DARK_OAK_LOG});
      w.getLeft(3).Pillar(6, new Material[]{Material.DARK_OAK_LOG});
      w.getRight(3).Pillar(6, new Material[]{Material.DARK_OAK_LOG});
      (new StairBuilder(Material.DARK_OAK_STAIRS)).setFacing(w.getDirection().getOppositeFace()).apply(w.getFront()).apply(w.getLeft(3).getFront()).apply(w.getRight(3).getFront());
      (new StairBuilder(Material.POLISHED_ANDESITE_STAIRS)).setFacing(w.getDirection().getOppositeFace()).apply(w.getLeft()).apply(w.getLeft(2)).apply(w.getRight()).apply(w.getRight(2));
   }

   @NotNull
   public MansionRoomSize getSize() {
      return new MansionRoomSize(3, 3);
   }

   public int[] getSpawnLocation() {
      return new int[]{this.getRoom().getX(), this.getRoom().getY() - 7, this.getRoom().getZ()};
   }
}
