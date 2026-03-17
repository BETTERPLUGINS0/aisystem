package advancedplugins.pm2.cv.models.v1_21_R10.entity.navigation;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class AquaticNavigatorOptimized extends WaterBoundPathNavigation {
   private final AquaticNavigatorOptimized.SwimPathProcessor swimProcessor;

   public AquaticNavigatorOptimized(Mob var1) {
      super(var1, var1.level());
      this.swimProcessor = new AquaticNavigatorOptimized.SwimPathProcessor(var1);
   }

   public boolean moveTo(@Nullable Path var1, double var2) {
      if (var1 != null && !var1.sameAs(this.path)) {
         this.swimProcessor.optimizeSwimPath(var1);
      }

      return super.moveTo(var1, var2);
   }

   protected void followThePath() {
      this.swimProcessor.progressThroughWater();
   }

   private class SwimPathProcessor {
      private final Mob swimmer;

      SwimPathProcessor(Mob param2) {
         this.swimmer = var2;
      }

      void optimizeSwimPath(Path var1) {
         int var2 = var1.getNextNodeIndex();
         double var3 = this.measureDistanceToPoint(var1.getNextEntityPos(this.swimmer));

         for(int var5 = var2 + 1; var5 < var1.getNodeCount(); ++var5) {
            double var6 = this.measureDistanceToPoint(var1.getEntityPosAtNode(this.swimmer, var5));
            if (var6 < var3) {
               var3 = var6;
               var2 = var5;
            }
         }

         var1.setNextNodeIndex(var2);
      }

      void progressThroughWater() {
         Vec3 var1 = AquaticNavigatorOptimized.this.getTempMobPos();
         float var2 = this.calculateSwimReach();
         Vec3 var3 = AquaticNavigatorOptimized.this.path.getNextEntityPos(this.swimmer);
         if (this.isWithinReach(var1, var3, var2) || this.canMoveToNextPoint(var1)) {
            AquaticNavigatorOptimized.this.path.advance();
         }

         AquaticNavigatorOptimized.this.doStuckDetection(var1);
      }

      private double measureDistanceToPoint(Vec3 var1) {
         double var2 = var1.x - this.swimmer.getX();
         double var4 = var1.y - this.swimmer.getY();
         double var6 = var1.z - this.swimmer.getZ();
         return var2 * var2 + var4 * var4 + var6 * var6;
      }

      private float calculateSwimReach() {
         float var1 = this.swimmer.getBbWidth();
         return var1 > 0.75F ? var1 / 2.0F : 0.75F - var1 / 2.0F;
      }

      private boolean isWithinReach(Vec3 var1, Vec3 var2, float var3) {
         double var4 = Math.abs(this.swimmer.getX() - var2.x);
         double var6 = Math.abs(this.swimmer.getY() - var2.y);
         double var8 = Math.abs(this.swimmer.getZ() - var2.z);
         return var4 < (double)var3 && var8 < (double)var3 && var6 < 1.0D;
      }

      private boolean canMoveToNextPoint(Vec3 var1) {
         Path var2 = AquaticNavigatorOptimized.this.path;
         return AquaticNavigatorOptimized.this.canCutCorner(var2.getNextNode().type) && this.shouldSwimToNext(var1, var2);
      }

      private boolean shouldSwimToNext(Vec3 var1, Path var2) {
         if (var2.getNextNodeIndex() + 1 >= var2.getNodeCount()) {
            return false;
         } else {
            Vec3 var3 = var2.getNextEntityPos(this.swimmer);
            if (!var1.closerThan(var3, 2.0D)) {
               return false;
            } else if (AquaticNavigatorOptimized.this.canMoveDirectly(var1, var3)) {
               return true;
            } else {
               Vec3 var4 = var2.getEntityPosAtNode(this.swimmer, var2.getNextNodeIndex() + 1);
               Vec3 var5 = var4.subtract(var3);
               Vec3 var6 = var1.subtract(var3);
               return var5.dot(var6) > 0.0D;
            }
         }
      }
   }
}
