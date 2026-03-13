package org.terraform.structure.pyramid;

import java.util.Iterator;
import java.util.Random;
import java.util.Map.Entry;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected.Half;
import org.bukkit.block.data.type.Stairs;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.Wall;
import org.terraform.structure.room.CubeRoom;
import org.terraform.utils.BannerUtils;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;

public class WarAntechamber extends Antechamber {
   public WarAntechamber(Random rand, boolean forceSpawn, boolean unique) {
      super(rand, forceSpawn, unique);
   }

   public void populate(@NotNull PopulatorDataAbstract data, @NotNull CubeRoom room) {
      super.populate(data, room);
      Iterator var3 = room.getFourWalls(data, 1).entrySet().iterator();

      int i;
      while(var3.hasNext()) {
         Entry<Wall, Integer> entry = (Entry)var3.next();
         Wall w = ((Wall)entry.getKey()).getUp(2);

         for(i = 0; i < (Integer)entry.getValue(); ++i) {
            if (w.getRear().isSolid() && !w.isSolid() && GenUtils.chance(this.rand, 3, 10)) {
               BannerUtils.generateBanner(this.rand, w.get(), w.getDirection(), true);
            }

            w = w.getLeft();
         }
      }

      Wall w = new Wall(new SimpleBlock(data, room.getX(), room.getY() + 1, room.getZ()));
      w.LPillar(room.getHeight(), this.rand, new Material[]{Material.CHISELED_SANDSTONE});
      BlockFace[] var10 = BlockUtils.directBlockFaces;
      int var11 = var10.length;

      for(i = 0; i < var11; ++i) {
         BlockFace face = var10[i];
         Stairs stair = (Stairs)Bukkit.createBlockData(Material.SANDSTONE_STAIRS);
         stair.setFacing(face.getOppositeFace());
         w.getRelative(face).setBlockData(stair);
         stair = (Stairs)Bukkit.createBlockData(Material.SANDSTONE_STAIRS);
         stair.setFacing(face.getOppositeFace());
         stair.setHalf(Half.TOP);
         w.getRelative(face).getRelative(0, room.getHeight() - 2, 0).setBlockData(stair);
      }

      w.getRelative(0, room.getHeight() / 2 - 1, 0).setType((Material)GenUtils.randChoice(this.rand, Material.GOLD_BLOCK, Material.LAPIS_BLOCK, Material.LAPIS_BLOCK, Material.EMERALD_BLOCK, Material.IRON_BLOCK));
   }

   public boolean canPopulate(@NotNull CubeRoom room) {
      return room.getWidthX() >= 6 && room.getWidthZ() >= 6;
   }
}
