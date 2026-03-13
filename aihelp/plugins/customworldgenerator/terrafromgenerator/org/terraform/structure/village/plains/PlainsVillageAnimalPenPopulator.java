package org.terraform.structure.village.plains;

import java.util.Iterator;
import java.util.Random;
import java.util.Map.Entry;
import org.bukkit.Material;
import org.bukkit.block.data.Bisected.Half;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.Wall;
import org.terraform.main.config.TConfig;
import org.terraform.small_items.PlantBuilder;
import org.terraform.structure.room.CubeRoom;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;
import org.terraform.utils.blockdata.DirectionalBuilder;
import org.terraform.utils.blockdata.SlabBuilder;
import org.terraform.utils.blockdata.StairBuilder;

public class PlainsVillageAnimalPenPopulator extends PlainsVillageAbstractRoomPopulator {
   private static final EntityType[] farmAnimals;
   private final PlainsVillagePopulator plainsVillagePopulator;

   public PlainsVillageAnimalPenPopulator(PlainsVillagePopulator plainsVillagePopulator, Random rand, boolean forceSpawn, boolean unique) {
      super(rand, forceSpawn, unique);
      this.plainsVillagePopulator = plainsVillagePopulator;
   }

   public void populate(@NotNull PopulatorDataAbstract data, @NotNull CubeRoom room) {
      if (!super.doesAreaFailTolerance(data, room)) {
         int roomY = super.calculateRoomY(data, room);
         SimpleBlock jobBlock = null;
         boolean spawnedWater = false;
         Iterator var6 = room.getFourWalls(data, 2).entrySet().iterator();

         int z;
         Wall core;
         int wallHeight;
         while(var6.hasNext()) {
            Entry<Wall, Integer> entry = (Entry)var6.next();
            Wall w = (Wall)entry.getKey();

            for(z = 0; z < (Integer)entry.getValue(); ++z) {
               core = w.getAtY(roomY).findNearestAirPocket(15);
               if (core != null) {
                  if (core.getDown().getType() != Material.COBBLESTONE_SLAB && core.getDown().getType() != this.plainsVillagePopulator.woodFence) {
                     wallHeight = 3;
                     if (core.getY() < roomY) {
                        wallHeight = 2 + (roomY - core.getY());
                     }

                     if (z % 2 == 0) {
                        core.Pillar(wallHeight, new Material[]{this.plainsVillagePopulator.woodLog});
                        core.getUp(wallHeight).setType(Material.COBBLESTONE_SLAB);
                        core.getDown(2).getRight().CorrectMultipleFacing(wallHeight + 2);
                        core.getDown(2).getLeft().CorrectMultipleFacing(wallHeight + 2);
                     } else {
                        core.Pillar(wallHeight, new Material[]{this.plainsVillagePopulator.woodFence});
                        core.CorrectMultipleFacing(wallHeight);
                     }
                  }

                  if (w.getDirection() == ((DirectionalCubeRoom)room).getDirection().getOppositeFace() && z == (Integer)entry.getValue() / 2) {
                     jobBlock = core.getRear();
                  }
               }

               w = w.getLeft();
            }
         }

         int[] lowerCorner = room.getLowerCorner(3);
         int[] upperCorner = room.getUpperCorner(3);

         int x;
         int highest;
         for(x = lowerCorner[0]; x <= upperCorner[0]; ++x) {
            for(z = lowerCorner[1]; z <= upperCorner[1]; ++z) {
               highest = GenUtils.getHighestGroundOrSeaLevel(data, x, z);
               if (Math.abs(highest - roomY) <= TConfig.c.STRUCTURES_PLAINSVILLAGE_HEIGHT_TOLERANCE) {
                  BlockUtils.setDownUntilSolid(x, highest, z, data, Material.DIRT);
                  if (this.rand.nextBoolean()) {
                     data.setType(x, highest, z, (Material)GenUtils.randChoice((Object[])(Material.PODZOL, Material.COARSE_DIRT, Material.GRASS_BLOCK)));
                  } else if (this.rand.nextBoolean() && !data.getType(x, highest + 1, z).isSolid()) {
                     PlantBuilder.TALL_GRASS.build(data, x, highest + 1, z);
                  }
               }
            }
         }

         lowerCorner = room.getLowerCorner(5);
         upperCorner = room.getUpperCorner(5);

         for(x = lowerCorner[0]; x <= upperCorner[0]; ++x) {
            for(z = lowerCorner[1]; z <= upperCorner[1]; ++z) {
               if (GenUtils.chance(this.rand, 1, 70)) {
                  if (!spawnedWater && this.rand.nextBoolean()) {
                     spawnedWater = true;
                     core = new Wall(new SimpleBlock(data, x, 0, z), BlockUtils.getDirectBlockFace(this.rand));
                     core = core.getGroundOrSeaLevel().getUp();
                     if (Math.abs(core.getY() - roomY) <= TConfig.c.STRUCTURES_PLAINSVILLAGE_HEIGHT_TOLERANCE) {
                        (new StairBuilder(Material.COBBLESTONE_STAIRS)).setHalf(Half.TOP).setFacing(core.getDirection()).apply(core.getRear()).setFacing(core.getDirection().getOppositeFace()).apply(core.getFront(2)).setFacing(BlockUtils.getRight(core.getDirection())).apply(core.getFront().getLeft()).apply(core.getLeft()).setFacing(BlockUtils.getLeft(core.getDirection())).apply(core.getFront().getRight()).apply(core.getRight());
                        (new SlabBuilder(Material.COBBLESTONE_SLAB)).setWaterlogged(true).apply(core).apply(core.getFront());
                        core.getDown().downUntilSolid(new Random(), new Material[]{Material.DIRT});
                        core.getFront().getDown().downUntilSolid(new Random(), new Material[]{Material.DIRT});
                        break;
                     }
                  } else {
                     SimpleBlock core = (new SimpleBlock(data, x, roomY, z)).findAirPocket(15);
                     if (core != null) {
                        BlockUtils.replaceUpperSphere(x + 7 * z + 289, 1.5F, 2.5F, 1.5F, core, false, Material.HAY_BLOCK);
                        break;
                     }
                  }
               }
            }
         }

         EntityType animal = farmAnimals[this.rand.nextInt(farmAnimals.length)];
         int[] coords = new int[]{room.getX(), 0, room.getZ()};
         highest = GenUtils.getTrueHighestBlock(data, coords[0], coords[2]);

         for(wallHeight = 0; data.getType(coords[0], highest + 1, coords[2]).isSolid() && wallHeight < 6; ++highest) {
            ++wallHeight;
         }

         if (wallHeight < 6 && Math.abs(highest - roomY) <= TConfig.c.STRUCTURES_PLAINSVILLAGE_HEIGHT_TOLERANCE) {
            for(int i = 0; i < GenUtils.randInt(3, 7); ++i) {
               data.addEntity(coords[0], highest + 1, coords[2], animal);
            }
         }

         if (jobBlock != null) {
            switch(animal) {
            case PIG:
            case CHICKEN:
               (new DirectionalBuilder(Material.SMOKER)).setFacing(((DirectionalCubeRoom)room).getDirection()).apply((SimpleBlock)jobBlock);
               break;
            case SHEEP:
               jobBlock.setType(Material.LOOM);
               break;
            case COW:
            case HORSE:
               jobBlock.setType(Material.CAULDRON);
            }
         }

      }
   }

   public boolean canPopulate(@NotNull CubeRoom room) {
      return room.getWidthX() >= 15 && (room.getWidthX() < 18 || room.getWidthZ() < 18);
   }

   static {
      farmAnimals = new EntityType[]{EntityType.PIG, EntityType.SHEEP, EntityType.COW, EntityType.HORSE, EntityType.CHICKEN};
   }
}
