package advancedplugins.pm2.cv.models.v1_21_R10.entity.navigation;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class AerialNavigatorEnhanced extends FlyingPathNavigation {
   private final AerialNavigatorEnhanced.NavigationOptimizer routeOptimizer;

   public AerialNavigatorEnhanced(Mob var1, FlyingPathNavigation var2) {
      super(var1, var1.level());
      this.routeOptimizer = new AerialNavigatorEnhanced.NavigationOptimizer(var1);
      this.setCanFloat(var2.canFloat());
      var2.getNodeEvaluator().setCanOpenDoors(var2.getNodeEvaluator().canOpenDoors());
      var2.getNodeEvaluator().setCanPassDoors(var2.getNodeEvaluator().canPassDoors());
   }

   public boolean moveTo(@Nullable Path var1, double var2) {
      if (var1 != null && !var1.sameAs(this.path)) {
         this.routeOptimizer.findOptimalStartNode(var1);
      }

      return super.moveTo(var1, var2);
   }

   protected void followThePath() {
      this.routeOptimizer.progressAlongRoute();
   }

   private class NavigationOptimizer {
      private final Mob entity;

      NavigationOptimizer(Mob param2) {
         this.entity = var2;
      }

      void findOptimalStartNode(Path var1) {
         int var2 = var1.getNextNodeIndex();
         double var3 = this.getSquaredDistanceToNode(var1.getNextEntityPos(this.entity));

         for(int var5 = var2 + 1; var5 < var1.getNodeCount(); ++var5) {
            double var6 = this.getSquaredDistanceToNode(var1.getEntityPosAtNode(this.entity, var5));
            if (var6 < var3) {
               var3 = var6;
               var2 = var5;
            }
         }

         var1.setNextNodeIndex(var2);
      }

      void progressAlongRoute() {
         Vec3 var1 = AerialNavigatorEnhanced.this.getTempMobPos();
         float var2 = this.calculateReachThreshold();
         Vec3 var3 = AerialNavigatorEnhanced.this.path.getNextEntityPos(this.entity);
         if (this.hasReachedNode(var1, var3, var2) || this.canAdvanceToNext(var1)) {
            AerialNavigatorEnhanced.this.path.advance();
         }

         AerialNavigatorEnhanced.this.doStuckDetection(var1);
      }

      private double getSquaredDistanceToNode(Vec3 var1) {
         double var2 = var1.x - this.entity.getX();
         double var4 = var1.y - this.entity.getY();
         double var6 = var1.z - this.entity.getZ();
         return var2 * var2 + var4 * var4 + var6 * var6;
      }

      private float calculateReachThreshold() {
         float var1 = this.entity.getBbWidth();
         return var1 > 0.75F ? var1 / 2.0F : 0.75F - var1 / 2.0F;
      }

      private boolean hasReachedNode(Vec3 var1, Vec3 var2, float var3) {
         double var4 = Math.abs(this.entity.getX() - var2.x);
         double var6 = Math.abs(this.entity.getY() - var2.y);
         double var8 = Math.abs(this.entity.getZ() - var2.z);
         return var4 < (double)var3 && var8 < (double)var3 && var6 < 1.0D;
      }

      private boolean canAdvanceToNext(Vec3 var1) {
         Path var2 = AerialNavigatorEnhanced.this.path;
         return AerialNavigatorEnhanced.this.canCutCorner(var2.getNextNode().type) && this.shouldMoveToNextNode(var1, var2);
      }

      private boolean shouldMoveToNextNode(Vec3 var1, Path var2) {
         if (var2.getNextNodeIndex() + 1 >= var2.getNodeCount()) {
            return false;
         } else {
            Vec3 var3 = var2.getNextEntityPos(this.entity);
            if (!var1.closerThan(var3, 2.0D)) {
               return false;
            } else if (AerialNavigatorEnhanced.this.canMoveDirectly(var1, var3)) {
               return true;
            } else {
               Vec3 var4 = var2.getEntityPosAtNode(this.entity, var2.getNextNodeIndex() + 1);
               Vec3 var5 = var4.subtract(var3);
               Vec3 var6 = var1.subtract(var3);
               return var5.dot(var6) > 0.0D;
            }
         }
      }
   }
}
