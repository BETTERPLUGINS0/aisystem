package org.terraform.structure.monument;

import java.util.Iterator;
import java.util.Random;
import java.util.Map.Entry;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.Wall;
import org.terraform.structure.room.CubeRoom;
import org.terraform.utils.GenUtils;

public class FishCageRoomPopulator extends LevelledRoomPopulator {
   public FishCageRoomPopulator(Random rand, MonumentDesign design, boolean forceSpawn, boolean unique) {
      super(rand, design, forceSpawn, unique);
   }

   public void populate(@NotNull PopulatorDataAbstract data, @NotNull CubeRoom room) {
      super.populate(data, room);
      CubeRoom cage = new CubeRoom(room.getWidthX() - 6, room.getWidthZ() - 6, room.getHeight() - 11, room.getX(), room.getY() + 7, room.getZ());
      Iterator var4 = cage.getFourWalls(data, 0).entrySet().iterator();

      int z;
      int i;
      while(var4.hasNext()) {
         Entry<Wall, Integer> entry = (Entry)var4.next();
         Wall w = (Wall)entry.getKey();
         z = (Integer)entry.getValue();

         for(i = 0; i < z; ++i) {
            if (i % 2 == 0) {
               w.Pillar(cage.getHeight(), this.rand, new Material[]{Material.PRISMARINE_WALL});
            } else {
               w.getFront().Pillar(cage.getHeight(), this.rand, new Material[]{Material.PRISMARINE_WALL});
            }

            Stairs stair = (Stairs)Bukkit.createBlockData(this.design.stairs());
            stair.setFacing(w.getDirection());
            w.getRelative(0, cage.getHeight(), 0).setBlockData(stair);
            w = w.getLeft();
         }
      }

      int[] cageLowerBounds = cage.getLowerCorner();
      int[] cageUpperBounds = cage.getUpperCorner();

      int i;
      for(i = cageLowerBounds[0]; i <= cageUpperBounds[0]; ++i) {
         for(z = cageLowerBounds[1]; z <= cageUpperBounds[1]; ++z) {
            data.setType(i, cage.getY(), z, this.design.mat(this.rand));
            data.setType(i, cage.getY() + cage.getHeight(), z, this.design.mat(this.rand));
         }
      }

      int[][] var13 = cage.getAllCorners();
      z = var13.length;

      for(i = 0; i < z; ++i) {
         int[] corner = var13[i];
         (new Wall(new SimpleBlock(data, corner[0], cage.getY() + cage.getHeight() + 1, corner[1]), BlockFace.NORTH)).Pillar(room.getHeight() - 8 - cage.getHeight(), this.rand, new Material[]{Material.PRISMARINE_WALL});
      }

      for(i = 0; i < GenUtils.randInt(3, 6); ++i) {
         data.addEntity(cage.getX(), cage.getY() + 1, cage.getZ(), EntityType.DOLPHIN);
      }

   }

   public boolean canPopulate(@NotNull CubeRoom room) {
      return room.getHeight() > 13;
   }
}
