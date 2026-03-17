package advancedplugins.pm2.cv.models.v1_21_R5.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class OcclusionClipContext extends ClipContext {
   public OcclusionClipContext(Vec3 var1, Vec3 var2) {
      super(var1, var2, Block.VISUAL, Fluid.NONE, (Entity)null);
   }

   public VoxelShape getBlockShape(BlockState var1, BlockGetter var2, BlockPos var3) {
      return var1.canOcclude() && var1.getOcclusionShape() == Shapes.block() ? Shapes.block() : Shapes.empty();
   }
}
