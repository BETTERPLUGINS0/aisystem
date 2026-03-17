package advancedplugins.pm2.cv.models.v1_21_R1.entity.navigation;

import net.minecraft.core.BlockPosition;
import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.ai.navigation.AmphibiousPathNavigation;
import net.minecraft.world.level.pathfinder.PathEntity;
import net.minecraft.world.phys.Vec3D;
import org.jetbrains.annotations.Nullable;

public class AmphibiousPathfinderEnhanced extends AmphibiousPathNavigation {
   private final AmphibiousPathNavigation originalPathfinder;
   private final AmphibiousPathfinderEnhanced.PathOptimizer optimizer;

   public AmphibiousPathfinderEnhanced(EntityInsentient mob, AmphibiousPathNavigation original) {
      super(var1, var1.dO());
      this.originalPathfinder = var2;
      this.optimizer = new AmphibiousPathfinderEnhanced.PathOptimizer(var1, this);
      this.a(var2.p());
   }

   public boolean a(@Nullable PathEntity path, double speed) {
      if (var1 != null && !var1.a(this.c)) {
         this.optimizer.optimizePath(var1);
      }

      return super.a(var1, var2);
   }

   protected void k() {
      this.optimizer.advanceAlongPath();
   }

   public boolean a(BlockPosition pos) {
      return this.originalPathfinder.a(var1);
   }

   private class PathOptimizer extends EnhancedPathfinder {
      private float maxDistanceToWaypoint;

      PathOptimizer(EntityInsentient entity, AmphibiousPathNavigation navigation) {
         super(var2, var3);
      }

      void optimizePath(PathEntity path) {
         super.navigateToPath(var1, 0.0D);
      }

      void advanceAlongPath() {
         super.updatePathProgress();
      }

      protected PathEntity getCurrentPath() {
         return AmphibiousPathfinderEnhanced.this.c;
      }

      protected boolean performNavigation(PathEntity path, double speed) {
         return true;
      }

      protected Vec3D getEntityTemporaryPosition() {
         return AmphibiousPathfinderEnhanced.this.b();
      }

      protected void checkForStuckEntity(Vec3D position) {
         AmphibiousPathfinderEnhanced.this.b(var1);
      }

      protected boolean canCutPathCorner() {
         return AmphibiousPathfinderEnhanced.this.b(AmphibiousPathfinderEnhanced.this.c.h().l);
      }

      protected boolean hasDirectPath(Vec3D from, Vec3D to) {
         return AmphibiousPathfinderEnhanced.this.a(var1, var2);
      }
   }
}
