package org.terraform.structure.room;

import java.util.Random;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.utils.BlockUtils;

public class CarvedRoom extends CubeRoom {
   private float xMultiplier = 1.0F;
   private float yMultiplier = 1.0F;
   private float zMultiplier = 1.0F;
   private float frequency = 0.09F;

   public CarvedRoom(int widthX, int widthZ, int height, int x, int y, int z) {
      super(widthX, widthZ, height, x, y, z);
   }

   public CarvedRoom(@NotNull CubeRoom room) {
      super(room.getWidthX(), room.getWidthZ(), room.getHeight(), room.getX(), room.getY(), room.getZ());
   }

   public void fillRoom(@NotNull PopulatorDataAbstract data, int tile, Material[] mat, Material fillMat) {
      int heightOffset = this.height - 2 * this.height / 3;
      BlockUtils.carveCaveAir((new Random()).nextInt(9999291), this.xMultiplier * ((float)this.widthX / 2.0F), this.yMultiplier * ((float)(2 * this.height) / 3.0F), this.zMultiplier * ((float)this.widthZ / 2.0F), this.frequency, new SimpleBlock(data, this.x, this.y + heightOffset, this.z), true, true, BlockUtils.caveCarveReplace);
   }

   public double getxMultiplier() {
      return (double)this.xMultiplier;
   }

   public void setxMultiplier(float xMultiplier) {
      this.xMultiplier = xMultiplier;
   }

   public float getyMultiplier() {
      return this.yMultiplier;
   }

   public void setyMultiplier(float yMultiplier) {
      this.yMultiplier = yMultiplier;
   }

   public float getzMultiplier() {
      return this.zMultiplier;
   }

   public void setzMultiplier(float zMultiplier) {
      this.zMultiplier = zMultiplier;
   }

   public float getFrequency() {
      return this.frequency;
   }

   public void setFrequency(float frequency) {
      this.frequency = frequency;
   }
}
