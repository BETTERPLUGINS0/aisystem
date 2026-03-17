package advancedplugins.pm2.cv.models.v1_21_R5.entity.navigation;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class GroundNavigationWrapper extends GroundPathNavigation {
   public GroundNavigationWrapper(Mob var1, GroundPathNavigation var2) {
      super(var1, var1.level());
      this.setCanFloat(var2.canFloat());
      var2.getNodeEvaluator().setCanOpenDoors(var2.getNodeEvaluator().canOpenDoors());
      var2.getNodeEvaluator().setCanPassDoors(var2.getNodeEvaluator().canPassDoors());
   }

   public boolean moveTo(@Nullable Path var1, double var2) {
      if (var1 != null && !var1.sameAs(this.path)) {
         int var4 = var1.getNextNodeIndex();
         double var5 = this.getNodeDistanceSquared(var1.getNextEntityPos(this.mob));

         for(int var7 = var4 + 1; var7 < var1.getNodeCount(); ++var7) {
            double var8 = this.getNodeDistanceSquared(var1.getEntityPosAtNode(this.mob, var7));
            if (var8 < var5) {
               var5 = var8;
               var4 = var7;
            }
         }

         var1.setNextNodeIndex(var4);
      }

      return super.moveTo(var1, var2);
   }

   private double getNodeDistanceSquared(Vec3 var1) {
      double var2 = var1.x - this.mob.getX();
      double var4 = var1.y - this.mob.getY();
      double var6 = var1.z - this.mob.getZ();
      return var2 * var2 + var4 * var4 + var6 * var6;
   }

   protected void followThePath() {
      Vec3 var1 = this.getTempMobPos();
      this.maxDistanceToWaypoint = this.mob.getBbWidth() > 0.75F ? this.mob.getBbWidth() / 2.0F : 0.75F - this.mob.getBbWidth() / 2.0F;
      Vec3 var2 = this.path.getNextEntityPos(this.mob);
      double var3 = Math.abs(this.mob.getX() - var2.x);
      double var5 = Math.abs(this.mob.getY() - var2.y);
      double var7 = Math.abs(this.mob.getZ() - var2.z);
      boolean var9 = var3 < (double)this.maxDistanceToWaypoint && var7 < (double)this.maxDistanceToWaypoint && var5 < 1.0D;
      boolean var10 = var9 || this.canCutCorner(this.path.getNextNode().type) && this.shouldTargetNextNodeInDirection(var1);
      if (var10) {
         this.path.advance();
      }

      this.doStuckDetection(var1);
   }

   private boolean shouldTargetNextNodeInDirection(Vec3 var1) {
      if (this.path.getNextNodeIndex() + 1 >= this.path.getNodeCount()) {
         return false;
      } else {
         Vec3 var2 = this.path.getNextEntityPos(this.mob);
         if (!var1.closerThan(var2, 2.0D)) {
            return false;
         } else if (this.canMoveDirectly(var1, this.path.getNextEntityPos(this.mob))) {
            return true;
         } else {
            Vec3 var3 = this.path.getEntityPosAtNode(this.mob, this.path.getNextNodeIndex() + 1);
            Vec3 var4 = var3.subtract(var2);
            Vec3 var5 = var1.subtract(var2);
            return var4.dot(var5) > 0.0D;
         }
      }
   }
}
