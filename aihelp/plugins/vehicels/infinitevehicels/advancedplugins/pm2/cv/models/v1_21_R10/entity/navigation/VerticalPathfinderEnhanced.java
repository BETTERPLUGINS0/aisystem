package advancedplugins.pm2.cv.models.v1_21_R10.entity.navigation;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.WallClimberNavigation;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class VerticalPathfinderEnhanced extends WallClimberNavigation {
   private final VerticalPathfinderEnhanced.ClimbingOptimizer climbOptimizer;

   public VerticalPathfinderEnhanced(Mob var1, WallClimberNavigation var2) {
      super(var1, var1.level());
      this.climbOptimizer = new VerticalPathfinderEnhanced.ClimbingOptimizer(var1);
      this.setCanFloat(var2.canFloat());
      var2.getNodeEvaluator().setCanOpenDoors(var2.getNodeEvaluator().canOpenDoors());
      var2.getNodeEvaluator().setCanPassDoors(var2.getNodeEvaluator().canPassDoors());
   }

   public boolean moveTo(@Nullable Path var1, double var2) {
      if (var1 != null && !var1.sameAs(this.path)) {
         this.climbOptimizer.optimizeClimbRoute(var1);
      }

      return super.moveTo(var1, var2);
   }

   protected void followThePath() {
      this.climbOptimizer.updateClimbProgress();
   }

   private class ClimbingOptimizer {
      private final Mob climber;

      ClimbingOptimizer(Mob param2) {
         this.climber = var2;
      }

      void optimizeClimbRoute(Path var1) {
         int var2 = var1.getNextNodeIndex();
         double var3 = this.calculateNodeProximity(var1.getNextEntityPos(this.climber));

         for(int var5 = var2 + 1; var5 < var1.getNodeCount(); ++var5) {
            double var6 = this.calculateNodeProximity(var1.getEntityPosAtNode(this.climber, var5));
            if (var6 < var3) {
               var3 = var6;
               var2 = var5;
            }
         }

         var1.setNextNodeIndex(var2);
      }

      void updateClimbProgress() {
         Vec3 var1 = VerticalPathfinderEnhanced.this.getTempMobPos();
         float var2 = this.computeArrivalThreshold();
         Vec3 var3 = VerticalPathfinderEnhanced.this.path.getNextEntityPos(this.climber);
         if (this.hasArrivedAtPoint(var1, var3, var2) || this.canSkipToNextPoint(var1)) {
            VerticalPathfinderEnhanced.this.path.advance();
         }

         VerticalPathfinderEnhanced.this.doStuckDetection(var1);
      }

      private double calculateNodeProximity(Vec3 var1) {
         double var2 = var1.x - this.climber.getX();
         double var4 = var1.y - this.climber.getY();
         double var6 = var1.z - this.climber.getZ();
         return var2 * var2 + var4 * var4 + var6 * var6;
      }

      private float computeArrivalThreshold() {
         float var1 = this.climber.getBbWidth();
         return var1 > 0.75F ? var1 / 2.0F : 0.75F - var1 / 2.0F;
      }

      private boolean hasArrivedAtPoint(Vec3 var1, Vec3 var2, float var3) {
         double var4 = Math.abs(this.climber.getX() - var2.x);
         double var6 = Math.abs(this.climber.getY() - var2.y);
         double var8 = Math.abs(this.climber.getZ() - var2.z);
         return var4 < (double)var3 && var8 < (double)var3 && var6 < 1.0D;
      }

      private boolean canSkipToNextPoint(Vec3 var1) {
         Path var2 = VerticalPathfinderEnhanced.this.path;
         return VerticalPathfinderEnhanced.this.canCutCorner(var2.getNextNode().type) && this.shouldProgressToNext(var1, var2);
      }

      private boolean shouldProgressToNext(Vec3 var1, Path var2) {
         if (var2.getNextNodeIndex() + 1 >= var2.getNodeCount()) {
            return false;
         } else {
            Vec3 var3 = var2.getNextEntityPos(this.climber);
            if (!var1.closerThan(var3, 2.0D)) {
               return false;
            } else if (VerticalPathfinderEnhanced.this.canMoveDirectly(var1, var3)) {
               return true;
            } else {
               Vec3 var4 = var2.getEntityPosAtNode(this.climber, var2.getNextNodeIndex() + 1);
               Vec3 var5 = var4.subtract(var3);
               Vec3 var6 = var1.subtract(var3);
               return var5.dot(var6) > 0.0D;
            }
         }
      }
   }
}
