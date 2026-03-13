package org.terraform.structure.village.plains;

import java.util.Iterator;
import java.util.Random;
import java.util.Map.Entry;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.Bisected.Half;
import org.bukkit.block.data.type.Farmland;
import org.bukkit.block.data.type.Lantern;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.Wall;
import org.terraform.main.config.TConfig;
import org.terraform.structure.room.CubeRoom;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;
import org.terraform.utils.blockdata.AgeableBuilder;
import org.terraform.utils.blockdata.DirectionalBuilder;
import org.terraform.utils.blockdata.TrapdoorBuilder;

public class PlainsVillageCropFarmPopulator extends PlainsVillageAbstractRoomPopulator {
   private static final Material[] crops;
   private final PlainsVillagePopulator plainsVillagePopulator;

   public PlainsVillageCropFarmPopulator(PlainsVillagePopulator plainsVillagePopulator, Random rand, boolean forceSpawn, boolean unique) {
      super(rand, forceSpawn, unique);
      this.plainsVillagePopulator = plainsVillagePopulator;
   }

   public void populate(@NotNull PopulatorDataAbstract data, @NotNull CubeRoom room) {
      int roomY = super.calculateRoomY(data, room);
      boolean areaFailedTolerance = super.doesAreaFailTolerance(data, room);
      if (!areaFailedTolerance) {
         super.populate(data, room);
         BlockFace dir = ((DirectionalCubeRoom)room).getDirection();
         boolean hasScareCrow = false;
         int pad = GenUtils.randInt(1, 3);
         Iterator var8 = room.getFourWalls(data, pad).entrySet().iterator();

         int x;
         int z;
         while(var8.hasNext()) {
            Entry<Wall, Integer> entry = (Entry)var8.next();
            Wall w = ((Wall)entry.getKey()).getGroundOrSeaLevel().getUp();

            for(x = 0; x < (Integer)entry.getValue(); ++x) {
               if (Math.abs(w.getY() - roomY) <= TConfig.c.STRUCTURES_PLAINSVILLAGE_HEIGHT_TOLERANCE) {
                  if (w.getDirection().getOppositeFace() == dir) {
                     if (x > 1 && x < (Integer)entry.getValue() - 1) {
                        if (x != 2 && x != (Integer)entry.getValue() - 2) {
                           if (x == (Integer)entry.getValue() / 2) {
                              w.setType(Material.COMPOSTER);
                           }
                        } else {
                           w.setType(this.plainsVillagePopulator.woodFence);
                           w.getDown().downUntilSolid(this.rand, new Material[]{this.plainsVillagePopulator.woodLog});
                           w.CorrectMultipleFacing(1);
                           w.getUp().setType(Material.TORCH);
                        }
                     } else {
                        w.setType(this.plainsVillagePopulator.woodLog);
                        w.getDown().downUntilSolid(this.rand, new Material[]{this.plainsVillagePopulator.woodLog});
                        if (x == 1 || x == (Integer)entry.getValue() - 1) {
                           (new TrapdoorBuilder(this.plainsVillagePopulator.woodTrapdoor)).setFacing(dir).setOpen(true).setHalf(Half.BOTTOM).apply(w.getRear());
                           w.getUp().setType(this.plainsVillagePopulator.woodLeaves);
                        }
                     }
                  } else {
                     w.downUntilSolid(this.rand, new Material[]{this.plainsVillagePopulator.woodLog});
                     if (x % 3 == 0) {
                        w.getUp().setType(this.plainsVillagePopulator.woodLeaves);
                     } else {
                        w.getUp().setType(this.plainsVillagePopulator.woodFence);
                        w.getUp().CorrectMultipleFacing(1);
                        if (x > 1 && x < (Integer)entry.getValue() - 2 && GenUtils.chance(this.rand, 1, 13)) {
                           z = GenUtils.randInt(this.rand, 4, 6);
                           w.getUp(2).Pillar(z, this.rand, new Material[]{this.plainsVillagePopulator.woodFence});
                           Wall lampWall = w.getRelative(0, 1 + z, 0).getFront();

                           int j;
                           for(j = 0; j < GenUtils.randInt(this.rand, 1, 2); ++j) {
                              lampWall.setType(this.plainsVillagePopulator.woodFence);
                              lampWall.CorrectMultipleFacing(1);
                              lampWall = lampWall.getFront();
                           }

                           lampWall = lampWall.getRear().getDown();

                           for(j = 0; j < GenUtils.randInt(this.rand, 0, 1); ++j) {
                              lampWall.setType(Material.CHAIN);
                              lampWall = lampWall.getDown();
                           }

                           Lantern lantern = (Lantern)Bukkit.createBlockData(Material.LANTERN);
                           lantern.setHanging(true);
                           lampWall.setBlockData(lantern);
                        }
                     }
                  }
               }

               w = w.getLeft().getGroundOrSeaLevel().getUp();
            }
         }

         ++pad;
         int[] lowerCorner = room.getLowerCorner(pad);
         int[] upperCorner = room.getUpperCorner(pad);
         Material crop = crops[this.rand.nextInt(crops.length)];

         for(x = lowerCorner[0]; x <= upperCorner[0]; ++x) {
            for(z = lowerCorner[1]; z <= upperCorner[1]; ++z) {
               int height = GenUtils.getHighestGroundOrSeaLevel(data, x, z);
               if (Math.abs(height - roomY) <= TConfig.c.STRUCTURES_PLAINSVILLAGE_HEIGHT_TOLERANCE) {
                  BlockUtils.setDownUntilSolid(x, height - 1, z, data, Material.DIRT);
                  if (x % 4 == 0 && z % 4 == 0) {
                     BlockFace[] var30 = BlockUtils.directBlockFaces;
                     int var27 = var30.length;

                     for(int var29 = 0; var29 < var27; ++var29) {
                        BlockFace face = var30[var29];
                        data.setType(x + face.getModX(), height, z + face.getModZ(), Material.FARMLAND);
                        BlockUtils.setDownUntilSolid(x + face.getModX(), height - 1, z + face.getModZ(), data, Material.DIRT);
                     }

                     data.setType(x, height, z, Material.WATER);
                  } else if ((crop == Material.PUMPKIN_STEM || crop == Material.MELON_STEM) && !GenUtils.chance(this.rand, 1, 3)) {
                     if (GenUtils.chance(this.rand, 1, 3)) {
                        data.setType(x, height, z, Material.DIRT);
                        Material stem;
                        Material block;
                        if (crop == Material.PUMPKIN_STEM) {
                           block = Material.PUMPKIN;
                           stem = Material.ATTACHED_PUMPKIN_STEM;
                        } else {
                           block = Material.MELON;
                           stem = Material.ATTACHED_MELON_STEM;
                        }

                        SimpleBlock target = new SimpleBlock(data, x, height + 1, z);
                        BlockFace[] var17 = BlockUtils.directBlockFaces;
                        int var18 = var17.length;

                        for(int var19 = 0; var19 < var18; ++var19) {
                           BlockFace near = var17[var19];
                           if (target.getRelative(near).getBlockData() instanceof Ageable) {
                              target.setType(block);
                              (new DirectionalBuilder(stem)).setFacing(near.getOppositeFace()).apply(target.getRelative(near));
                              break;
                           }
                        }
                     } else {
                        data.setType(x, height, z, Material.COARSE_DIRT);
                     }
                  } else if (GenUtils.chance(this.rand, 1, 30) && !hasScareCrow) {
                     if (x > lowerCorner[0] + 1 && x < upperCorner[0] - 1 && z > lowerCorner[1] + 1 && z < upperCorner[1] - 1) {
                        hasScareCrow = true;
                        this.setScareCrow(data, x, height + 1, z);
                     }
                  } else {
                     Farmland land = (Farmland)Bukkit.createBlockData(Material.FARMLAND);
                     land.setMoisture(7);
                     data.setBlockData(x, height, z, land);
                     (new AgeableBuilder(crop)).setRandomAge(this.rand).apply(data, x, height + 1, z);
                  }
               }
            }
         }

      }
   }

   private void setScareCrow(@NotNull PopulatorDataAbstract data, int x, int y, int z) {
      BlockFace facing = BlockUtils.getDirectBlockFace(this.rand);
      Wall w = new Wall(new SimpleBlock(data, x, y, z), facing);
      w.setType(new Material[]{Material.COBBLESTONE, Material.MOSSY_COBBLESTONE});
      w.getUp().setType(this.plainsVillagePopulator.woodFence);
      w.getUp(2).setType(this.plainsVillagePopulator.woodFence);
      w.getLeft().getUp(2).setType(this.plainsVillagePopulator.woodFence);
      w.getRight().getUp(2).setType(this.plainsVillagePopulator.woodFence);
      w.getUp(2).CorrectMultipleFacing(1);
      (new DirectionalBuilder(new Material[]{Material.CARVED_PUMPKIN, Material.JACK_O_LANTERN})).setFacing(facing).apply(w.getUp(3));
   }

   public boolean canPopulate(@NotNull CubeRoom room) {
      return room.getWidthX() >= 15 && (room.getWidthX() < 18 || room.getWidthZ() < 18);
   }

   static {
      crops = new Material[]{Material.WHEAT, Material.CARROTS, Material.POTATOES, Material.BEETROOTS, Material.PUMPKIN_STEM, Material.MELON_STEM};
   }
}
