package advancedplugins.pm2.cv.models.v1_21_R5_spigot.entity.navigation;

import net.minecraft.core.BlockPosition;
import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.ai.navigation.AmphibiousPathNavigation;
import net.minecraft.world.level.pathfinder.PathEntity;
import net.minecraft.world.phys.Vec3D;
import org.jetbrains.annotations.Nullable;

public class AmphibiousPathfinderEnhanced extends AmphibiousPathNavigation {
   private final AmphibiousPathNavigation originalPathfinder;
   private final AmphibiousPathfinderEnhanced.PathOptimizer optimizer;

   public AmphibiousPathfinderEnhanced(EntityInsentient var1, AmphibiousPathNavigation var2) {
      super(var1, var1.ai());
      this.originalPathfinder = var2;
      this.optimizer = new AmphibiousPathfinderEnhanced.PathOptimizer(var1, this);
      this.a(var2.p());
   }

   public boolean a(@Nullable PathEntity var1, double var2) {
      if (var1 != null && !var1.a(this.c)) {
         this.optimizer.optimizePath(var1);
      }

      return super.a(var1, var2);
   }

   protected void k() {
      this.optimizer.advanceAlongPath();
   }

   public boolean a(BlockPosition var1) {
      return this.originalPathfinder.a(var1);
   }

   private class PathOptimizer extends EnhancedPathfinder {
      private float maxDistanceToWaypoint;

      PathOptimizer(EntityInsentient param2, AmphibiousPathNavigation param3) {
         super(var2, var3);
      }

      void optimizePath(PathEntity var1) {
         super.navigateToPath(var1, 0.0D);
      }

      void advanceAlongPath() {
         super.updatePathProgress();
      }

      protected PathEntity getCurrentPath() {
         return AmphibiousPathfinderEnhanced.this.c;
      }

      protected boolean performNavigation(PathEntity var1, double var2) {
         return true;
      }

      protected Vec3D getEntityTemporaryPosition() {
         return AmphibiousPathfinderEnhanced.this.b();
      }

      protected void checkForStuckEntity(Vec3D var1) {
         AmphibiousPathfinderEnhanced.this.b(var1);
      }

      protected boolean canCutPathCorner() {
         return AmphibiousPathfinderEnhanced.this.b(AmphibiousPathfinderEnhanced.this.c.h().l);
      }

      protected boolean hasDirectPath(Vec3D var1, Vec3D var2) {
         return AmphibiousPathfinderEnhanced.this.a(var1, var2);
      }
   }
}
