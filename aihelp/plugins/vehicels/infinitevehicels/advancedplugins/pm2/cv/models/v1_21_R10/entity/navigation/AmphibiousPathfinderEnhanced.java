package advancedplugins.pm2.cv.models.v1_21_R10.entity.navigation;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.AmphibiousPathNavigation;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class AmphibiousPathfinderEnhanced extends AmphibiousPathNavigation {
   private final AmphibiousPathNavigation originalPathfinder;
   private final AmphibiousPathfinderEnhanced.PathOptimizer optimizer;

   public AmphibiousPathfinderEnhanced(Mob var1, AmphibiousPathNavigation var2) {
      super(var1, var1.level());
      this.originalPathfinder = var2;
      this.optimizer = new AmphibiousPathfinderEnhanced.PathOptimizer(var1, this);
      this.setCanFloat(var2.canFloat());
   }

   public boolean moveTo(@Nullable Path var1, double var2) {
      if (var1 != null && !var1.sameAs(this.path)) {
         this.optimizer.optimizePath(var1);
      }

      return super.moveTo(var1, var2);
   }

   protected void followThePath() {
      this.optimizer.advanceAlongPath();
   }

   public boolean isStableDestination(BlockPos var1) {
      return this.originalPathfinder.isStableDestination(var1);
   }

   private class PathOptimizer extends EnhancedPathfinder {
      private float maxDistanceToWaypoint;

      PathOptimizer(Mob param2, AmphibiousPathNavigation param3) {
         super(var2, var3);
      }

      void optimizePath(Path var1) {
         super.navigateToPath(var1, 0.0D);
      }

      void advanceAlongPath() {
         super.updatePathProgress();
      }

      protected Path getCurrentPath() {
         return AmphibiousPathfinderEnhanced.this.path;
      }

      protected boolean performNavigation(Path var1, double var2) {
         return true;
      }

      protected Vec3 getEntityTemporaryPosition() {
         return AmphibiousPathfinderEnhanced.this.getTempMobPos();
      }

      protected void checkForStuckEntity(Vec3 var1) {
         AmphibiousPathfinderEnhanced.this.doStuckDetection(var1);
      }

      protected boolean canCutPathCorner() {
         return AmphibiousPathfinderEnhanced.this.canCutCorner(AmphibiousPathfinderEnhanced.this.path.getNextNode().type);
      }

      protected boolean hasDirectPath(Vec3 var1, Vec3 var2) {
         return AmphibiousPathfinderEnhanced.this.canMoveDirectly(var1, var2);
      }
   }
}
