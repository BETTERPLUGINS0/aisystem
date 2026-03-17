package advancedplugins.pm2.cv.models.v1_21_R1.entity;

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
   public VisibilityCheckContext(Vec3D origin, Vec3D target) {
      super(var1, var2, BlockCollisionOption.c, FluidCollisionOption.a, (Entity)null);
   }

   public VoxelShape a(IBlockData state, IBlockAccess world, BlockPosition pos) {
      if (!var1.p()) {
         return VoxelShapes.a();
      } else {
         VoxelShape var4 = var1.c(var2, var3);
         return var4 == VoxelShapes.b() ? VoxelShapes.b() : VoxelShapes.a();
      }
   }
}
