package org.terraform.structure.room.carver;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.structure.room.CubeRoom;
import org.terraform.utils.BlockUtils;

public class CaveRoomCarver extends RoomCarver {
   private float xMultiplier = 1.0F;
   private float yMultiplier = 1.0F;
   private float zMultiplier = 1.0F;
   private float frequency = 0.09F;
   private float largeRoomFrequency = 0.03F;

   public CaveRoomCarver() {
   }

   public CaveRoomCarver(float xMultiplier, float yMultiplier, float zMultiplier, float frequency, float largeRoomFrequency) {
      this.xMultiplier = xMultiplier;
      this.yMultiplier = yMultiplier;
      this.zMultiplier = zMultiplier;
      this.frequency = frequency;
      this.largeRoomFrequency = largeRoomFrequency;
   }

   public void carveRoom(@NotNull PopulatorDataAbstract data, @NotNull CubeRoom room, Material... wallMaterial) {
      int heightOffset = room.getHeight() - 2 * room.getHeight() / 3;
      BlockUtils.carveCaveAir(data.getTerraformWorld().getHashedRand((long)room.getX(), room.getY(), room.getZ()).nextInt(9999291), this.xMultiplier * ((float)room.getWidthX() / 2.0F), this.yMultiplier * ((float)(2 * room.getHeight()) / 3.0F), this.zMultiplier * ((float)room.getWidthZ() / 2.0F), room.largerThanVolume(40000) ? this.largeRoomFrequency : this.frequency, new SimpleBlock(data, room.getX(), room.getY() + heightOffset, room.getZ()), true, true, BlockUtils.caveCarveReplace);
   }
}
