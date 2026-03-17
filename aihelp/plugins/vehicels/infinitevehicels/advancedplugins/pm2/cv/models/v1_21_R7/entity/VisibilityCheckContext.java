package advancedplugins.pm2.cv.models.v1_21_R7.entity;

import net.minecraft.core.BlockPosition;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.IBlockAccess;
import net.minecraft.world.level.RayTrace;
import net.minecraft.world.level.RayTrace.BlockCollisionOption;
import net.minecraft.world.level.RayTrace.FluidCollisionOption;
import net.minecraft.world.level.block.state.IBlockData;
import net.minecraft.world.phys.Vec3D;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.VoxelShapes;

public class VisibilityCheckContext extends RayTrace {
   public VisibilityCheckContext(Vec3D var1, Vec3D var2) {
      super(var1, var2, BlockCollisionOption.c, FluidCollisionOption.a, (Entity)null);
   }

   public VoxelShape a(IBlockData var1, IBlockAccess var2, BlockPosition var3) {
      return var1.t() && var1.h() == VoxelShapes.b() ? VoxelShapes.b() : VoxelShapes.a();
   }
}
