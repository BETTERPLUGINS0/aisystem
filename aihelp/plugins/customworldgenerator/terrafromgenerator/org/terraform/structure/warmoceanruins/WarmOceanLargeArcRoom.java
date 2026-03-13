package org.terraform.structure.warmoceanruins;

import java.util.Iterator;
import java.util.Random;
import java.util.Map.Entry;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Bisected.Half;
import org.bukkit.block.data.type.Slab;
import org.bukkit.block.data.type.Slab.Type;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.TerraLootTable;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.Wall;
import org.terraform.main.config.TConfig;
import org.terraform.structure.room.CubeRoom;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;
import org.terraform.utils.blockdata.ChestBuilder;
import org.terraform.utils.blockdata.SlabBuilder;
import org.terraform.utils.blockdata.StairBuilder;

public class WarmOceanLargeArcRoom extends WarmOceanBaseRoom {
   public WarmOceanLargeArcRoom(Random rand, boolean forceSpawn, boolean unique) {
      super(rand, forceSpawn, unique);
   }

   public void populate(@NotNull PopulatorDataAbstract data, @NotNull CubeRoom room) {
      super.populate(data, room);
      BlockFace facing = BlockUtils.getDirectBlockFace(this.rand);
      Wall chestTarget = (new Wall(room.getCenterSimpleBlock(data), facing)).getLeft(GenUtils.getSign(this.rand) * this.rand.nextInt(2)).getFront(GenUtils.getSign(this.rand) * this.rand.nextInt(6)).getGround().getUp();
      (new ChestBuilder(Material.CHEST)).setFacing(BlockUtils.getDirectBlockFace(this.rand)).setLootTable(TerraLootTable.UNDERWATER_RUIN_BIG).setWaterlogged(chestTarget.getY() <= TConfig.c.HEIGHT_MAP_SEA_LEVEL).apply(chestTarget);
      Material[] centrePattern = new Material[]{(Material)GenUtils.randChoice((Object[])(Material.POLISHED_DIORITE, Material.POLISHED_GRANITE, Material.POLISHED_ANDESITE)), (Material)GenUtils.randChoice((Object[])(Material.POLISHED_DIORITE, Material.POLISHED_GRANITE, Material.POLISHED_ANDESITE)), (Material)GenUtils.randChoice((Object[])(Material.POLISHED_DIORITE, Material.POLISHED_GRANITE, Material.POLISHED_ANDESITE))};
      Material[] sidePattern = new Material[]{(Material)GenUtils.randChoice((Object[])(Material.POLISHED_DIORITE, Material.POLISHED_GRANITE, Material.POLISHED_ANDESITE)), (Material)GenUtils.randChoice((Object[])(Material.POLISHED_DIORITE, Material.POLISHED_GRANITE, Material.POLISHED_ANDESITE)), (Material)GenUtils.randChoice((Object[])(Material.POLISHED_DIORITE, Material.POLISHED_GRANITE, Material.POLISHED_ANDESITE))};
      BlockData[] centreSlabs = new BlockData[3];
      BlockData[] sideSlabs = new BlockData[3];

      for(int i = 0; i < 3; ++i) {
         centreSlabs[i] = Bukkit.createBlockData(Material.CUT_SANDSTONE_SLAB);
         if (this.rand.nextBoolean()) {
            ((Slab)centreSlabs[i]).setType(Type.TOP);
         }

         if (GenUtils.chance(this.rand, 1, 4)) {
            centreSlabs[i] = Bukkit.createBlockData(Material.CHISELED_SANDSTONE);
         }

         sideSlabs[i] = Bukkit.createBlockData(Material.CUT_SANDSTONE_SLAB);
         if (this.rand.nextBoolean()) {
            ((Slab)sideSlabs[i]).setType(Type.TOP);
         }
      }

      BlockFace[] var24 = BlockUtils.getAdjacentFaces(facing);
      int lr = var24.length;

      int fb;
      int j;
      for(fb = 0; fb < lr; ++fb) {
         BlockFace pillarDir = var24[fb];
         SimpleBlock pillarCore = room.getCenterSimpleBlock(data).getRelative(pillarDir, 5);
         pillarCore.LPillar(8, Material.CUT_SANDSTONE);
         BlockFace[] var14 = BlockUtils.getAdjacentFaces(pillarDir);
         j = var14.length;

         int var16;
         BlockFace face;
         for(var16 = 0; var16 < j; ++var16) {
            face = var14[var16];
            (new StairBuilder(Material.SANDSTONE_STAIRS)).setHalf(Half.TOP).setFacing(pillarDir).setWaterlogged(pillarCore.getY() + 6 <= TConfig.c.HEIGHT_MAP_SEA_LEVEL).apply(pillarCore.getUp(6).getRelative(face, 2).getRelative(pillarDir.getOppositeFace(), 3)).setWaterlogged(pillarCore.getY() + 7 <= TConfig.c.HEIGHT_MAP_SEA_LEVEL).apply(pillarCore.getUp(7).getRelative(face, 2).getRelative(pillarDir.getOppositeFace(), 4));
            pillarCore.getUp(7).getRelative(face, 2).getRelative(pillarDir.getOppositeFace(), 3).setType(Material.SMOOTH_SANDSTONE);
         }

         var14 = BlockUtils.xzDiagonalPlaneBlockFaces;
         j = var14.length;

         for(var16 = 0; var16 < j; ++var16) {
            face = var14[var16];
            pillarCore.getRelative(face, 3).setType(Material.CUT_SANDSTONE_SLAB);
            pillarCore.getRelative(face, 2).Pillar(8, Material.CUT_SANDSTONE);
            pillarCore.getUp(4).getRelative(face, 2).setType(Material.CHISELED_SANDSTONE);
         }

         int[] var32 = new int[]{0, 4};
         j = var32.length;

         int nz;
         for(var16 = 0; var16 < j; ++var16) {
            int ny = var32[var16];

            for(int nx = -2; nx <= 2; ++nx) {
               for(nz = -2; nz <= 2; ++nz) {
                  pillarCore.getRelative(nx, ny, nz).setType(Material.CHISELED_SANDSTONE);
                  if (ny == 0) {
                     pillarCore.getDown().downLPillar(new Random(), 10 + this.rand.nextInt(5), Material.SANDSTONE);
                  }
               }
            }
         }

         var14 = BlockUtils.directBlockFaces;
         j = var14.length;

         for(var16 = 0; var16 < j; ++var16) {
            face = var14[var16];
            BlockFace[] var35 = BlockUtils.getAdjacentFaces(face);
            nz = var35.length;

            int var20;
            for(var20 = 0; var20 < nz; ++var20) {
               BlockFace dir = var35[var20];
               pillarCore.getRelative(face, 3).getRelative(dir).setType(Material.CUT_SANDSTONE_SLAB);
               (new StairBuilder(Material.SANDSTONE_STAIRS)).setFacing(dir).setWaterlogged(pillarCore.getY() <= TConfig.c.HEIGHT_MAP_SEA_LEVEL).apply(pillarCore.getRelative(face, 3).getRelative(dir, 2));
            }

            (new StairBuilder(Material.SANDSTONE_STAIRS)).setFacing(face.getOppositeFace()).setWaterlogged(pillarCore.getY() <= TConfig.c.HEIGHT_MAP_SEA_LEVEL).apply(pillarCore.getRelative(face, 3));
            int[] var36 = new int[]{1, 5};
            nz = var36.length;

            for(var20 = 0; var20 < nz; ++var20) {
               int up = var36[var20];
               Wall patternCore = new Wall(pillarCore.getUp(up).getRelative(face, 1), face);

               for(int i = 0; i < 3; ++i) {
                  patternCore.getUp(i).setType(centrePattern[i]);
                  patternCore.getUp(i).getLeft().setType(sidePattern[i]);
                  patternCore.getUp(i).getRight().setType(sidePattern[i]);
                  patternCore.getUp(i).getFront().setBlockData(centreSlabs[i]);
                  patternCore.getUp(i).getFront().getLeft().setBlockData(sideSlabs[i]);
                  patternCore.getUp(i).getFront().getRight().setBlockData(sideSlabs[i]);
               }
            }
         }
      }

      Wall core = new Wall(new SimpleBlock(data, room.getX(), room.getY() + 8, room.getZ()), facing);

      for(lr = -9; lr <= 9; ++lr) {
         for(fb = -4; fb <= 4; ++fb) {
            if (Math.abs(lr) < 9 && Math.abs(fb) < 4) {
               core.getLeft(lr).getFront(fb).setType(Material.CUT_SANDSTONE);
               if (Math.abs(lr) < 7 && Math.abs(fb) < 2) {
                  core.getLeft(lr).getFront(fb).getUp(4).setType(Material.CUT_SANDSTONE_SLAB);
               } else if (Math.abs(lr) != 7 && Math.abs(fb) != 2) {
                  (new SlabBuilder(Material.CUT_SANDSTONE_SLAB)).setType(Type.TOP).setWaterlogged(core.getY() + 4 <= TConfig.c.HEIGHT_MAP_SEA_LEVEL).apply(core.getLeft(lr).getFront(fb).getUp(4));
               } else {
                  core.getLeft(lr).getFront(fb).getUp(4).setType(Material.CUT_SANDSTONE);
               }
            } else {
               (new SlabBuilder(Material.CUT_SANDSTONE_SLAB)).setType(Type.TOP).setWaterlogged(core.getY() <= TConfig.c.HEIGHT_MAP_SEA_LEVEL).apply(core.getLeft(lr).getFront(fb));
            }
         }
      }

      CubeRoom topCoreRoom = new CubeRoom(facing.getModX() == 0 ? 15 : 5, facing.getModZ() == 0 ? 15 : 5, 15, room.getX(), room.getY() + 8, room.getZ());
      Iterator var27 = topCoreRoom.getFourWalls(data, 0).entrySet().iterator();

      while(var27.hasNext()) {
         Entry<Wall, Integer> entry = (Entry)var27.next();
         Wall w = (Wall)entry.getKey();

         for(int i = 0; i < (Integer)entry.getValue(); ++i) {
            if (i != 0 && i != (Integer)entry.getValue() - 1) {
               if (i != 2 && i != 7 && i != 12) {
                  for(j = 0; j < 3; ++j) {
                     w.getFront().getUp(j).setType(sidePattern[j]);
                  }
               } else {
                  for(j = 0; j < 3; ++j) {
                     w.getFront().getUp(j).setType(centrePattern[j]);
                  }
               }
            }

            if (i == 0) {
               w.Pillar(3, new Material[]{Material.CHISELED_SANDSTONE});
               (new StairBuilder(Material.SANDSTONE_STAIRS)).setFacing(BlockUtils.getRight(w.getDirection())).apply(w.getLeft()).setHalf(Half.TOP).setWaterlogged(w.getY() + 2 <= TConfig.c.HEIGHT_MAP_SEA_LEVEL).apply(w.getLeft().getUp(2));
            } else if (i == (Integer)entry.getValue() - 1) {
               w.Pillar(3, new Material[]{Material.CHISELED_SANDSTONE});
               (new StairBuilder(Material.SANDSTONE_STAIRS)).setFacing(BlockUtils.getLeft(w.getDirection())).apply(w.getRight()).setHalf(Half.TOP).setWaterlogged(w.getY() + 2 <= TConfig.c.HEIGHT_MAP_SEA_LEVEL).apply(w.getRight().getUp(2));
            } else if (i == 4 || i == 10) {
               w.Pillar(3, new Material[]{Material.CHISELED_SANDSTONE});
               (new StairBuilder(Material.SANDSTONE_STAIRS)).setFacing(BlockUtils.getLeft(w.getDirection())).apply(w.getRight()).setHalf(Half.TOP).setWaterlogged(w.getY() + 2 <= TConfig.c.HEIGHT_MAP_SEA_LEVEL).apply(w.getRight().getUp(2));
               (new StairBuilder(Material.SANDSTONE_STAIRS)).setFacing(BlockUtils.getRight(w.getDirection())).apply(w.getLeft()).setHalf(Half.TOP).setWaterlogged(w.getY() + 2 <= TConfig.c.HEIGHT_MAP_SEA_LEVEL).apply(w.getLeft().getUp(2));
            }

            w = w.getLeft();
         }
      }

      Wall centre = new Wall(room.getCenterSimpleBlock(data), facing);

      int i;
      for(i = 0; i < 1 + this.rand.nextInt(3); ++i) {
         BlockUtils.replaceWaterSphere(i * room.getX() * room.getZ(), (float)GenUtils.randInt(3, 4), centre.getUp(6).getLeft(GenUtils.getSign(this.rand) * this.rand.nextInt(7)).getFront(GenUtils.getSign(this.rand) * this.rand.nextInt(3)));
      }

      for(i = 0; i < 1 + this.rand.nextInt(3); ++i) {
         centre.getUp().addEntity(EntityType.DROWNED);
      }

   }

   public boolean canPopulate(@NotNull CubeRoom room) {
      return room.getWidthX() == 25;
   }
}
