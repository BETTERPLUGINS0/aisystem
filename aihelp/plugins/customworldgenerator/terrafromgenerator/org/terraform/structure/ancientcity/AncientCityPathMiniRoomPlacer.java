package org.terraform.structure.ancientcity;

import java.util.Random;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected.Half;
import org.jetbrains.annotations.NotNull;
import org.terraform.data.SimpleBlock;
import org.terraform.data.Wall;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.blockdata.StairBuilder;

public class AncientCityPathMiniRoomPlacer {
   public static void placeAltar(@NotNull Wall origin) {
      Material[] deepSlateBricks = new Material[]{Material.DEEPSLATE_BRICKS, Material.CRACKED_DEEPSLATE_BRICKS};
      cylinderDown(origin, 4, deepSlateBricks);
      cylinderDown(origin.getUp(), 3, deepSlateBricks);
      (new StairBuilder(Material.DEEPSLATE_BRICK_STAIRS)).setFacing(origin.getDirection()).apply(origin.getRear(2).getUp()).apply(origin.getRear().getLeft().getUp()).apply(origin.getRear().getRight().getUp());
      origin.getRear(3).getUp().fluidize();
      origin.getRear(2).getUp().getLeft().fluidize();
      origin.getRear(2).getUp().getRight().fluidize();
      Wall altarCore = origin.getFront(2).getUp(2);
      altarCore.setType(deepSlateBricks);
      altarCore.getLeft().setType(deepSlateBricks);
      altarCore.getRight().setType(deepSlateBricks);
      (new StairBuilder(Material.DEEPSLATE_BRICK_STAIRS)).setFacing(altarCore.getDirection()).apply(altarCore.getRear()).setFacing(altarCore.getDirection().getOppositeFace()).apply(altarCore.getFront());
      altarCore.getFront().getDown().setType(deepSlateBricks);
      altarCore.getRear().getLeft().setType(Material.DEEPSLATE_BRICK_SLAB);
      altarCore.getRear().getRight().setType(Material.DEEPSLATE_BRICK_SLAB);
      altarCore.getLeft(2).setType(Material.DEEPSLATE_BRICK_SLAB);
      altarCore.getRight(2).setType(Material.DEEPSLATE_BRICK_SLAB);
      altarCore.getUp().Pillar(2, new Material[]{Material.COBBLED_DEEPSLATE_WALL});
      altarCore.getUp().getLeft().Pillar(2, new Material[]{Material.COBBLED_DEEPSLATE_WALL});
      altarCore.getUp().getRight().Pillar(2, new Material[]{Material.COBBLED_DEEPSLATE_WALL});
      altarCore.getUp(3).setType(Material.DEEPSLATE_BRICK_SLAB);
      altarCore.getUp(3).getLeft().setType(Material.DEEPSLATE_BRICK_SLAB);
      altarCore.getUp(3).getRight().setType(Material.DEEPSLATE_BRICK_SLAB);
      BlockFace[] var3 = BlockUtils.getAdjacentFaces(altarCore.getDirection());
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         BlockFace adj = var3[var5];
         (new StairBuilder(Material.DEEPSLATE_BRICK_STAIRS)).setFacing(adj.getOppositeFace()).apply(altarCore.getRelative(adj, 2).getUp(2)).setHalf(Half.TOP).apply(altarCore.getRelative(adj, 2).getUp());
      }

   }

   private static void cylinderDown(@NotNull SimpleBlock core, int radius, Material... mat) {
      for(float x = (float)(-radius); x <= (float)radius; ++x) {
         for(float z = (float)(-radius); z <= (float)radius; ++z) {
            SimpleBlock rel = core.getRelative(Math.round(x), 0, Math.round(z));
            double equationResult = Math.pow((double)x, 2.0D) / Math.pow((double)radius, 2.0D) + Math.pow((double)z, 2.0D) / Math.pow((double)radius, 2.0D);
            if (equationResult <= 1.0D) {
               rel.downUntilSolid(new Random(), mat);
            }
         }
      }

   }
}
