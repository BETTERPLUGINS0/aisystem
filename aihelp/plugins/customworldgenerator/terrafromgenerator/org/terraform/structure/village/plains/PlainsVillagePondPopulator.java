package org.terraform.structure.village.plains;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.main.config.TConfig;
import org.terraform.small_items.PlantBuilder;
import org.terraform.structure.room.CubeRoom;
import org.terraform.structure.room.RoomPopulatorAbstract;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.CoralGenerator;
import org.terraform.utils.GenUtils;

public class PlainsVillagePondPopulator extends RoomPopulatorAbstract {
   public PlainsVillagePondPopulator(Random rand, boolean forceSpawn, boolean unique) {
      super(rand, forceSpawn, unique);
   }

   public void populate(@NotNull PopulatorDataAbstract data, @NotNull CubeRoom room) {
      int[] lowerCorner = room.getLowerCorner();
      int[] upperCorner = room.getUpperCorner();
      int lowest = 256;
      int highest = -1;

      int depth;
      int x;
      for(int x = lowerCorner[0]; x <= upperCorner[0]; ++x) {
         for(depth = lowerCorner[1]; depth <= upperCorner[1]; ++depth) {
            x = GenUtils.getHighestGround(data, x, depth);
            if (x < lowest) {
               lowest = x;
            }

            if (x > highest) {
               highest = x;
            }
         }
      }

      if (highest - lowest < 5) {
         SimpleBlock core = new SimpleBlock(data, room.getX(), 0, room.getZ());
         core = core.getGround();
         depth = GenUtils.randInt(3, 5);
         BlockUtils.replaceLowerSphere(this.rand.nextInt(12222), (float)room.getWidthX() / 2.0F - 1.5F, (float)depth, (float)room.getWidthZ() / 2.0F - 1.5F, core, true, Material.AIR);

         int pondSurface;
         for(x = lowerCorner[0]; x <= upperCorner[0]; ++x) {
            for(pondSurface = lowerCorner[1]; pondSurface <= upperCorner[1]; ++pondSurface) {
               int ground = GenUtils.getHighestGround(data, x, pondSurface);
               if (ground < lowest) {
                  lowest = ground;
               }

               if (ground > highest) {
                  highest = ground;
               }
            }
         }

         ArrayList<SimpleBlock> lakeWaterBlocks = this.getLakeWaterBlocks(core, lowerCorner, upperCorner, lowest);
         if (!lakeWaterBlocks.isEmpty()) {
            pondSurface = -1;
            Iterator var22 = lakeWaterBlocks.iterator();

            while(var22.hasNext()) {
               SimpleBlock s = (SimpleBlock)var22.next();
               s.setType(Material.WATER);
               if (s.getY() > pondSurface) {
                  pondSurface = s.getY();
               }
            }

            boolean placedJobBlock = false;

            for(int x = lowerCorner[0]; x <= upperCorner[0]; ++x) {
               for(int z = lowerCorner[1]; z <= upperCorner[1]; ++z) {
                  SimpleBlock target = (new SimpleBlock(core.getPopData(), x, 0, z)).getGround();
                  if (target.getUp().getType() != Material.AIR) {
                     if (target.getUp().getType() == Material.WATER) {
                        target = target.getUp();
                        if (GenUtils.chance(1, 5)) {
                           PlantBuilder.LILY_PAD.build(target.getAtY(pondSurface).getUp());
                        } else if (GenUtils.chance(1, 5)) {
                           CoralGenerator.generateKelpGrowth(data, x, target.getY(), z);
                        } else if (GenUtils.chance(1, 7)) {
                           CoralGenerator.generateSeaPickles(data, x, target.getY(), z);
                        }

                        if (TConfig.areAnimalsEnabled() && GenUtils.chance(1, 20)) {
                           core.getPopData().addEntity(target.getX(), target.getY(), target.getZ(), EntityType.TROPICAL_FISH);
                        }
                     }
                  } else {
                     boolean valid = false;
                     BlockFace[] var16 = BlockUtils.directBlockFaces;
                     int var17 = var16.length;

                     for(int var18 = 0; var18 < var17; ++var18) {
                        BlockFace face = var16[var18];
                        if (target.getRelative(face).getType() == Material.WATER) {
                           valid = true;
                        }
                     }

                     if (valid) {
                        target = target.getUp();
                        if (GenUtils.chance(1, 4)) {
                           PlantBuilder.SUGAR_CANE.build(target, this.rand, 2, 5);
                        } else if (GenUtils.chance(1, 4)) {
                           PlantBuilder.OAK_LEAVES.build(target);
                        } else if (GenUtils.chance(1, 4)) {
                           PlantBuilder.build(core.getPopData(), target.getX(), target.getY(), target.getZ(), PlantBuilder.LARGE_FERN, PlantBuilder.TALL_GRASS);
                        } else if (!placedJobBlock && TConfig.areDecorationsEnabled() && GenUtils.chance(2, 5)) {
                           target.setType(Material.BARREL);
                           placedJobBlock = true;
                        }
                     }
                  }
               }
            }

         }
      }
   }

   @NotNull
   private ArrayList<SimpleBlock> getLakeWaterBlocks(@NotNull SimpleBlock core, @NotNull int[] lowerCorner, @NotNull int[] upperCorner, int lowestPoint) {
      int layer = 0;
      ArrayList lakeBlocks = new ArrayList();

      while(true) {
         boolean layerValid = true;
         int[] var8 = new int[]{lowerCorner[0], upperCorner[0]};
         int z = var8.length;

         int var10;
         int z;
         int x;
         for(var10 = 0; var10 < z; ++var10) {
            z = var8[var10];

            for(x = lowerCorner[1]; x <= upperCorner[1]; ++x) {
               if (!core.getPopData().getType(z, lowestPoint + layer, x).isSolid()) {
                  layerValid = false;
                  break;
               }

               if (!layerValid) {
                  break;
               }
            }
         }

         if (layerValid) {
            var8 = new int[]{lowerCorner[1], upperCorner[1]};
            z = var8.length;

            for(var10 = 0; var10 < z; ++var10) {
               z = var8[var10];

               for(x = lowerCorner[0]; x <= upperCorner[0]; ++x) {
                  if (!core.getPopData().getType(x, lowestPoint + layer, z).isSolid()) {
                     layerValid = false;
                     break;
                  }

                  if (!layerValid) {
                     break;
                  }
               }
            }
         }

         if (!layerValid) {
            return lakeBlocks;
         }

         for(int x = lowerCorner[0]; x <= upperCorner[0]; ++x) {
            for(z = lowerCorner[1]; z <= upperCorner[1]; ++z) {
               if (!core.getPopData().getType(x, lowestPoint + layer, z).isSolid()) {
                  lakeBlocks.add(new SimpleBlock(core.getPopData(), x, lowestPoint + layer, z));
               }
            }
         }

         ++layer;
      }
   }

   public boolean canPopulate(@NotNull CubeRoom room) {
      return room.getWidthX() <= 10 && room.getWidthZ() > 5 && room.getWidthX() > 5;
   }
}
