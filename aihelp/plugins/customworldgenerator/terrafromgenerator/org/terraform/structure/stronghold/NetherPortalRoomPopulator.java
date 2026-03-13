package org.terraform.structure.stronghold;

import java.util.Iterator;
import java.util.Random;
import java.util.Map.Entry;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.Wall;
import org.terraform.structure.room.CubeRoom;
import org.terraform.structure.room.RoomPopulatorAbstract;
import org.terraform.utils.BlockUtils;

public class NetherPortalRoomPopulator extends RoomPopulatorAbstract {
   public NetherPortalRoomPopulator(Random rand, boolean forceSpawn, boolean unique) {
      super(rand, forceSpawn, unique);
   }

   public void populate(@NotNull PopulatorDataAbstract data, @NotNull CubeRoom room) {
      Iterator var3 = room.getFourWalls(data, 0).entrySet().iterator();

      int y;
      int x;
      while(var3.hasNext()) {
         Entry<Wall, Integer> entry = (Entry)var3.next();
         Wall wall = ((Wall)entry.getKey()).clone();
         y = (Integer)entry.getValue();

         for(x = 0; x < y; ++x) {
            wall.RPillar(room.getHeight(), this.rand, new Material[]{Material.OAK_PLANKS});
            wall = wall.getLeft();
         }
      }

      int rX = (room.getWidthX() - 2) / 2;
      int rZ = (room.getWidthZ() - 2) / 2;
      int rY = 2;
      y = room.getY();
      x = room.getX();
      int z = room.getZ();
      BlockUtils.replaceUpperSphere(this.rand.nextInt(123), (float)rX, (float)rY, (float)rZ, new SimpleBlock(data, x, y, z), true, Material.NETHERRACK, Material.NETHERRACK, Material.SOUL_SAND, Material.NETHERRACK, Material.NETHERRACK, Material.MAGMA_BLOCK);

      while(data.getType(x, y, z).isSolid() && y < room.getY() + room.getHeight() - 5) {
         ++y;
      }

      --y;
      Wall wall = new Wall(new SimpleBlock(data, x, y, z), BlockUtils.getXZPlaneBlockFace(this.rand));
      Material[] blocks = new Material[]{Material.OBSIDIAN, Material.OBSIDIAN, Material.OBSIDIAN, Material.OBSIDIAN, Material.OBSIDIAN, Material.OBSIDIAN, Material.OBSIDIAN, Material.CAVE_AIR};
      wall.getRight().getRight().Pillar(5, this.rand, blocks);
      wall.Pillar(5, this.rand, blocks);
      wall.getRight().Pillar(5, this.rand, blocks);
      wall.getLeft().Pillar(5, this.rand, blocks);
      wall.getUp(4).getRight().getRight().setType(Material.CHISELED_STONE_BRICKS);
      wall.getUp(4).getLeft().setType(Material.CHISELED_STONE_BRICKS);
      wall.getRight().getRight().setType(Material.CHISELED_STONE_BRICKS);
      wall.getLeft().setType(Material.CHISELED_STONE_BRICKS);
      wall = wall.getUp();
      wall.Pillar(3, this.rand, new Material[]{Material.CAVE_AIR});
      wall.getRight().Pillar(3, this.rand, new Material[]{Material.CAVE_AIR});
   }

   public boolean canPopulate(@NotNull CubeRoom room) {
      return room.isBig() && !room.isHuge() && room.getHeight() > 8;
   }
}
