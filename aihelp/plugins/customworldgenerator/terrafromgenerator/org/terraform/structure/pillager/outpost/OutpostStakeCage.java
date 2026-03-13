package org.terraform.structure.pillager.outpost;

import java.util.Iterator;
import java.util.Random;
import java.util.Map.Entry;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import org.terraform.biome.BiomeBank;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.Wall;
import org.terraform.structure.room.CubeRoom;
import org.terraform.structure.room.RoomPopulatorAbstract;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;
import org.terraform.utils.WoodUtils;
import org.terraform.utils.version.V_1_19;
import org.terraform.utils.version.Version;

public class OutpostStakeCage extends RoomPopulatorAbstract {
   private final BiomeBank biome;
   private final Material[] stakeGravel;

   public OutpostStakeCage(Random rand, boolean forceSpawn, boolean unique, BiomeBank biome, Material... stakeGravel) {
      super(rand, forceSpawn, unique);
      this.biome = biome;
      this.stakeGravel = stakeGravel;
   }

   public void populate(@NotNull PopulatorDataAbstract data, @NotNull CubeRoom room) {
      Material fenceMat = WoodUtils.getWoodForBiome(this.biome, WoodUtils.WoodType.FENCE);
      Material plankMat = WoodUtils.getWoodForBiome(this.biome, WoodUtils.WoodType.PLANKS);
      int[] lowerCorner = room.getLowerCorner(2);
      int[] upperCorner = room.getUpperCorner(2);
      int highestHeight = 0;

      int nx;
      int nz;
      int i;
      for(nx = lowerCorner[0]; nx <= upperCorner[0]; ++nx) {
         for(nz = lowerCorner[1]; nz <= upperCorner[1]; ++nz) {
            SimpleBlock target = new SimpleBlock(data, nx, 0, nz);
            i = target.getGroundOrSeaLevel().getY();
            if (i > highestHeight) {
               highestHeight = i;
            }

            target.lsetType(plankMat);
            (new Wall(target)).downUntilSolid(this.rand, new Material[]{fenceMat});
         }
      }

      Iterator var14 = room.getFourWalls(data, 2).entrySet().iterator();

      while(var14.hasNext()) {
         Entry<Wall, Integer> entry = (Entry)var14.next();
         Wall w = ((Wall)entry.getKey()).getGroundOrSeaLevel().getUp();

         for(i = 0; i < (Integer)entry.getValue(); ++i) {
            int baseHeight = 4 + highestHeight - w.getY();
            if (i % 2 == 0) {
               this.spawnOneStake(this.rand, baseHeight, w.get());
            } else {
               int fenceHeight = baseHeight + 2 + this.rand.nextInt(3);
               w.RPillar(fenceHeight, this.rand, new Material[]{fenceMat});
               w.CorrectMultipleFacing(fenceHeight);
            }

            w = w.getLeft().getGroundOrSeaLevel().getUp();
         }
      }

      if (Version.VERSION.isAtLeast(Version.v1_19_4)) {
         switch(this.rand.nextInt(3)) {
         case 0:
            data.addEntity(room.getX(), (new SimpleBlock(data, room.getX(), room.getY(), room.getZ())).getGroundOrDry().getY() + 1, room.getZ(), EntityType.IRON_GOLEM);
            break;
         case 1:
            for(nx = 0; nx < 1 + this.rand.nextInt(3); ++nx) {
               data.addEntity(room.getX(), (new SimpleBlock(data, room.getX(), room.getY(), room.getZ())).getGroundOrDry().getY() + 1, room.getZ(), V_1_19.ALLAY);
            }

            for(nx = lowerCorner[0]; nx <= upperCorner[0]; ++nx) {
               for(nz = lowerCorner[1]; nz <= upperCorner[1]; ++nz) {
                  int baseHeight = 6 + highestHeight;
                  SimpleBlock target = new SimpleBlock(data, nx, baseHeight, nz);
                  target.setType(plankMat);
               }
            }
         case 2:
         }
      } else if (this.rand.nextBoolean()) {
         data.addEntity(room.getX(), (new SimpleBlock(data, room.getX(), room.getY(), room.getZ())).getGroundOrDry().getY() + 1, room.getZ(), EntityType.IRON_GOLEM);
      }

   }

   public void spawnOneStake(@NotNull Random rand, int baseHeight, @NotNull SimpleBlock base) {
      WoodUtils.WoodType type = (new WoodUtils.WoodType[]{WoodUtils.WoodType.LOG, WoodUtils.WoodType.STRIPPED_LOG})[rand.nextInt(2)];
      int h = baseHeight + GenUtils.randInt(1, 3);
      BlockFace[] var6 = BlockUtils.directBlockFaces;
      int var7 = var6.length;

      for(int var8 = 0; var8 < var7; ++var8) {
         BlockFace face = var6[var8];
         if (rand.nextBoolean()) {
            base.getRelative(face).setType(this.stakeGravel);
         }
      }

      (new Wall(base)).Pillar(h, rand, new Material[]{WoodUtils.getWoodForBiome(this.biome, type)});
      (new Wall(base.getRelative(0, h, 0))).Pillar(GenUtils.randInt(2, 3), rand, new Material[]{WoodUtils.getWoodForBiome(this.biome, WoodUtils.WoodType.FENCE)});
   }

   public boolean canPopulate(CubeRoom room) {
      return true;
   }
}
