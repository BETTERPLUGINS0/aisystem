package advancedplugins.pm2.cv.models.v1_21_R10.entity.navigation;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class TerrainNavigatorOptimized extends GroundPathNavigation {
   private final TerrainNavigatorOptimized.RouteProcessor routeProcessor;

   public TerrainNavigatorOptimized(Mob var1, GroundPathNavigation var2) {
      super(var1, var1.level());
      this.routeProcessor = new TerrainNavigatorOptimized.RouteProcessor(var1);
      this.setCanFloat(var2.canFloat());
      var2.getNodeEvaluator().setCanOpenDoors(var2.getNodeEvaluator().canOpenDoors());
      var2.getNodeEvaluator().setCanPassDoors(var2.getNodeEvaluator().canPassDoors());
   }

   public boolean moveTo(@Nullable Path var1, double var2) {
      if (var1 != null && !var1.sameAs(this.path)) {
         this.routeProcessor.calibrateStartingPoint(var1);
      }

      return super.moveTo(var1, var2);
   }

   protected void followThePath() {
      this.routeProcessor.advancePosition();
   }

   private class RouteProcessor {
      private final Mob navigator;

      RouteProcessor(Mob param2) {
         this.navigator = var2;
      }

      void calibrateStartingPoint(Path var1) {
         int var2 = var1.getNextNodeIndex();
         double var3 = this.computeNodeDistance(var1.getNextEntityPos(this.navigator));

         for(int var5 = var2 + 1; var5 < var1.getNodeCount(); ++var5) {
            double var6 = this.computeNodeDistance(var1.getEntityPosAtNode(this.navigator, var5));
            if (var6 < var3) {
               var3 = var6;
               var2 = var5;
            }
         }

         var1.setNextNodeIndex(var2);
      }

      void advancePosition() {
         Vec3 var1 = TerrainNavigatorOptimized.this.getTempMobPos();
         float var2 = this.determineReachRadius();
         Vec3 var3 = TerrainNavigatorOptimized.this.path.getNextEntityPos(this.navigator);
         if (this.isAtDestination(var1, var3, var2) || this.canProgressToFollowing(var1)) {
            TerrainNavigatorOptimized.this.path.advance();
         }

         TerrainNavigatorOptimized.this.doStuckDetection(var1);
      }

      private double computeNodeDistance(Vec3 var1) {
         double var2 = var1.x - this.navigator.getX();
         double var4 = var1.y - this.navigator.getY();
         double var6 = var1.z - this.navigator.getZ();
         return var2 * var2 + var4 * var4 + var6 * var6;
      }

      private float determineReachRadius() {
         float var1 = this.navigator.getBbWidth();
         return var1 > 0.75F ? var1 / 2.0F : 0.75F - var1 / 2.0F;
      }

      private boolean isAtDestination(Vec3 var1, Vec3 var2, float var3) {
         double var4 = Math.abs(this.navigator.getX() - var2.x);
         double var6 = Math.abs(this.navigator.getY() - var2.y);
         double var8 = Math.abs(this.navigator.getZ() - var2.z);
         return var4 < (double)var3 && var8 < (double)var3 && var6 < 1.0D;
      }

      private boolean canProgressToFollowing(Vec3 var1) {
         Path var2 = TerrainNavigatorOptimized.this.path;
         return TerrainNavigatorOptimized.this.canCutCorner(var2.getNextNode().type) && this.shouldTransitionToNext(var1, var2);
      }

      private boolean shouldTransitionToNext(Vec3 var1, Path var2) {
         if (var2.getNextNodeIndex() + 1 >= var2.getNodeCount()) {
            return false;
         } else {
            Vec3 var3 = var2.getNextEntityPos(this.navigator);
            if (!var1.closerThan(var3, 2.0D)) {
               return false;
            } else if (TerrainNavigatorOptimized.this.canMoveDirectly(var1, var3)) {
               return true;
            } else {
               Vec3 var4 = var2.getEntityPosAtNode(this.navigator, var2.getNextNodeIndex() + 1);
               Vec3 var5 = var4.subtract(var3);
               Vec3 var6 = var1.subtract(var3);
               return var5.dot(var6) > 0.0D;
            }
         }
      }
   }
}
