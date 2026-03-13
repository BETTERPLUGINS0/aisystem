package org.terraform.structure.catacombs;

import java.util.Random;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected.Half;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.TerraLootTable;
import org.terraform.data.SimpleBlock;
import org.terraform.data.Wall;
import org.terraform.main.config.TConfig;
import org.terraform.structure.room.PathPopulatorAbstract;
import org.terraform.structure.room.PathPopulatorData;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;
import org.terraform.utils.blockdata.ChestBuilder;
import org.terraform.utils.blockdata.DirectionalBuilder;
import org.terraform.utils.blockdata.StairBuilder;

public class CatacombsPathPopulator extends PathPopulatorAbstract {
   public static final Material[] pathMaterial;
   private final Random rand;

   public CatacombsPathPopulator(Random rand) {
      this.rand = rand;
   }

   public int getPathMaxBend() {
      return 15;
   }

   public void populate(@NotNull PathPopulatorData ppd) {
      Wall core = new Wall(ppd.base, ppd.dir);
      if (core.getType() == Material.CAVE_AIR) {
         Wall floor = core.getDown();
         if (floor.isSolid()) {
            core.setType(pathMaterial);
            BlockFace[] var4 = BlockUtils.xzPlaneBlockFaces;
            int i = var4.length;

            int var6;
            for(var6 = 0; var6 < i; ++var6) {
               BlockFace face = var4[var6];
               if (this.rand.nextBoolean()) {
                  core.getRelative(face).setType(pathMaterial);
               }
            }

            if (ppd.dir != BlockFace.UP) {
               boolean spawnSupports = true;
               BlockFace[] var12 = BlockUtils.getAdjacentFaces(core.getDirection());
               var6 = var12.length;

               int var14;
               for(var14 = 0; var14 < var6; ++var14) {
                  BlockFace dir = var12[var14];
                  Wall relPillar = core.getUp().findDir(dir, 2);
                  if (relPillar != null && relPillar.getDown().isSolid() && relPillar.getUp().isSolid() && relPillar.getUp(3).getRelative(dir.getOppositeFace()).isSolid()) {
                     if (core.getDirection().getModX() != 0) {
                        if (core.getX() % 5 != 0) {
                           spawnSupports = false;
                        }
                     } else if (core.getDirection().getModZ() != 0 && core.getZ() % 5 != 0) {
                        spawnSupports = false;
                     }
                  } else {
                     spawnSupports = false;
                  }

                  if (spawnSupports) {
                     relPillar.Pillar(3, BlockUtils.stoneBricks);
                     relPillar.getUp().setType(Material.CHISELED_STONE_BRICKS);
                     (new StairBuilder(Material.STONE_BRICK_STAIRS)).setFacing(dir).setHalf(Half.TOP).apply(relPillar.getUp(2).getRelative(dir.getOppositeFace()));
                     (new StairBuilder(Material.STONE_BRICK_STAIRS)).setFacing(core.getDirection().getOppositeFace()).apply(relPillar.getFront()).setFacing(core.getDirection()).apply(relPillar.getRear());
                  }
               }

               for(i = 1; i <= 3; ++i) {
                  BlockFace[] var13 = BlockUtils.getAdjacentFaces(core.getDirection());
                  var14 = var13.length;

                  for(int var15 = 0; var15 < var14; ++var15) {
                     BlockFace dir = var13[var15];
                     Wall rel = core.getUp(i).findDir(dir, 3);
                     if (rel != null) {
                        if (this.rand.nextBoolean() && rel.getType() == Material.STONE) {
                           rel.setType(new Material[]{Material.ANDESITE, Material.COBBLESTONE});
                        }

                        if (rel.getAtY(core.getY()).distance(core) > 1.0D && BlockUtils.isStoneLike(rel.getType()) && this.rand.nextBoolean()) {
                           (new DirectionalBuilder(Material.SKELETON_WALL_SKULL)).setFacing(dir.getOppositeFace()).apply(rel.getRelative(dir.getOppositeFace()));
                        }

                        if (i == 1) {
                           if (GenUtils.chance(this.rand, 1, 60)) {
                              if (TConfig.areDecorationsEnabled()) {
                                 (new ChestBuilder(Material.CHEST)).setFacing(dir.getOppositeFace()).setLootTable(TerraLootTable.SIMPLE_DUNGEON).apply(rel.getRelative(dir.getOppositeFace()));
                              }
                           } else if (GenUtils.chance(this.rand, 1, 20)) {
                              (new StairBuilder(new Material[]{Material.STONE_BRICK_STAIRS, Material.MOSSY_STONE_BRICK_STAIRS, Material.COBBLESTONE_STAIRS})).setHalf(Half.TOP).setFacing(dir).apply(rel.getRelative(dir.getOppositeFace()));
                              BlockUtils.placeCandle(rel.getRelative(dir.getOppositeFace()).getUp(), GenUtils.randInt(1, 4), true);
                           }
                        } else if (rel.getRelative(dir.getOppositeFace()).getUp().isSolid() && GenUtils.chance(this.rand, 1, 10) && TConfig.areDecorationsEnabled()) {
                           rel.getRelative(dir.getOppositeFace()).setType(Material.COBWEB);
                        }
                     }
                  }
               }

            }
         }
      }
   }

   public boolean customCarve(@NotNull SimpleBlock base, BlockFace dir, int pathWidth) {
      Wall core = new Wall(base.getUp(2), dir);
      int seed = 2293 + 5471 * core.getX() + 9817 * core.getY() ^ 2 + 1049 * core.getZ() ^ 3;
      BlockUtils.carveCaveAir(seed, (float)pathWidth, (float)pathWidth, (float)pathWidth, core.get(), false, BlockUtils.badlandsStoneLike);
      return true;
   }

   public int getPathWidth() {
      return 2;
   }

   static {
      pathMaterial = new Material[]{Material.DIRT, Material.COARSE_DIRT, Material.ROOTED_DIRT, Material.DRIPSTONE_BLOCK};
   }
}
