package advancedplugins.pm2.cv.models.v1_21_R6.entity.navigation;

import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.ai.navigation.NavigationAbstract;
import net.minecraft.world.level.pathfinder.PathEntity;
import net.minecraft.world.phys.Vec3D;
import org.jetbrains.annotations.Nullable;

public abstract class EnhancedPathfinder {
   protected final EntityInsentient controlledEntity;
   protected final NavigationAbstract wrappedNavigation;

   protected EnhancedPathfinder(EntityInsentient var1, NavigationAbstract var2) {
      this.controlledEntity = var1;
      this.wrappedNavigation = var2;
   }

   public boolean navigateToPath(@Nullable PathEntity var1, double var2) {
      if (var1 != null && !var1.a(this.getCurrentPath())) {
         this.optimizePathProgress(var1);
      }

      return this.performNavigation(var1, var2);
   }

   protected abstract PathEntity getCurrentPath();

   protected abstract boolean performNavigation(PathEntity var1, double var2);

   private void optimizePathProgress(PathEntity var1) {
      int var2 = this.findNearestPathNode(var1);
      var1.c(var2);
   }

   private int findNearestPathNode(PathEntity var1) {
      int var2 = var1.f();
      double var3 = this.calculateDistanceToNode(var1.a(this.controlledEntity));

      for(int var5 = var2 + 1; var5 < var1.e(); ++var5) {
         double var6 = this.calculateDistanceToNode(var1.a(this.controlledEntity, var5));
         if (var6 < var3) {
            var3 = var6;
            var2 = var5;
         }
      }

      return var2;
   }

   private double calculateDistanceToNode(Vec3D var1) {
      double var2 = var1.d - this.controlledEntity.dC();
      double var4 = var1.e - this.controlledEntity.dE();
      double var6 = var1.f - this.controlledEntity.dI();
      return var2 * var2 + var4 * var4 + var6 * var6;
   }

   protected void updatePathProgress() {
      Vec3D var1 = this.getEntityTemporaryPosition();
      float var2 = this.calculateProximityThreshold();
      Vec3D var3 = this.getCurrentPath().a(this.controlledEntity);
      boolean var4 = this.isWithinNodeProximity(var1, var3, var2);
      boolean var5 = var4 || this.canSkipToNextNode(var1);
      if (var5) {
         this.getCurrentPath().a();
      }

      this.checkForStuckEntity(var1);
   }

   protected abstract Vec3D getEntityTemporaryPosition();

   protected abstract void checkForStuckEntity(Vec3D var1);

   private float calculateProximityThreshold() {
      float var1 = this.controlledEntity.ds();
      return var1 > 0.75F ? var1 / 2.0F : 0.75F - var1 / 2.0F;
   }

   private boolean isWithinNodeProximity(Vec3D var1, Vec3D var2, float var3) {
      double var4 = Math.abs(this.controlledEntity.dC() - var2.d);
      double var6 = Math.abs(this.controlledEntity.dE() - var2.e);
      double var8 = Math.abs(this.controlledEntity.dI() - var2.f);
      return var4 < (double)var3 && var8 < (double)var3 && var6 < 1.0D;
   }

   private boolean canSkipToNextNode(Vec3D var1) {
      return this.canCutPathCorner() && this.shouldAdvanceToNextNode(var1);
   }

   protected abstract boolean canCutPathCorner();

   private boolean shouldAdvanceToNextNode(Vec3D var1) {
      if (this.getCurrentPath().f() + 1 >= this.getCurrentPath().e()) {
         return false;
      } else {
         Vec3D var2 = this.getCurrentPath().a(this.controlledEntity);
         if (!var1.a(var2, 2.0D)) {
            return false;
         } else {
            return this.hasDirectPath(var1, var2) ? true : this.isMovingTowardsNextNode(var1, var2);
         }
      }
   }

   protected abstract boolean hasDirectPath(Vec3D var1, Vec3D var2);

   private boolean isMovingTowardsNextNode(Vec3D var1, Vec3D var2) {
      Vec3D var3 = this.getCurrentPath().a(this.controlledEntity, this.getCurrentPath().f() + 1);
      Vec3D var4 = var3.d(var2);
      Vec3D var5 = var1.d(var2);
      return var4.b(var5) > 0.0D;
   }
}
