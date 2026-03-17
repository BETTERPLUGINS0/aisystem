package advancedplugins.pm2.cv.models.v1_21_R10.entity.navigation;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public abstract class EnhancedPathfinder {
   protected final Mob controlledEntity;
   protected final PathNavigation wrappedNavigation;

   protected EnhancedPathfinder(Mob var1, PathNavigation var2) {
      this.controlledEntity = var1;
      this.wrappedNavigation = var2;
   }

   public boolean navigateToPath(@Nullable Path var1, double var2) {
      if (var1 != null && !var1.sameAs(this.getCurrentPath())) {
         this.optimizePathProgress(var1);
      }

      return this.performNavigation(var1, var2);
   }

   protected abstract Path getCurrentPath();

   protected abstract boolean performNavigation(Path var1, double var2);

   private void optimizePathProgress(Path var1) {
      int var2 = this.findNearestPathNode(var1);
      var1.setNextNodeIndex(var2);
   }

   private int findNearestPathNode(Path var1) {
      int var2 = var1.getNextNodeIndex();
      double var3 = this.calculateDistanceToNode(var1.getNextEntityPos(this.controlledEntity));

      for(int var5 = var2 + 1; var5 < var1.getNodeCount(); ++var5) {
         double var6 = this.calculateDistanceToNode(var1.getEntityPosAtNode(this.controlledEntity, var5));
         if (var6 < var3) {
            var3 = var6;
            var2 = var5;
         }
      }

      return var2;
   }

   private double calculateDistanceToNode(Vec3 var1) {
      double var2 = var1.x - this.controlledEntity.getX();
      double var4 = var1.y - this.controlledEntity.getY();
      double var6 = var1.z - this.controlledEntity.getZ();
      return var2 * var2 + var4 * var4 + var6 * var6;
   }

   protected void updatePathProgress() {
      Vec3 var1 = this.getEntityTemporaryPosition();
      float var2 = this.calculateProximityThreshold();
      Vec3 var3 = this.getCurrentPath().getNextEntityPos(this.controlledEntity);
      boolean var4 = this.isWithinNodeProximity(var1, var3, var2);
      boolean var5 = var4 || this.canSkipToNextNode(var1);
      if (var5) {
         this.getCurrentPath().advance();
      }

      this.checkForStuckEntity(var1);
   }

   protected abstract Vec3 getEntityTemporaryPosition();

   protected abstract void checkForStuckEntity(Vec3 var1);

   private float calculateProximityThreshold() {
      float var1 = this.controlledEntity.getBbWidth();
      return var1 > 0.75F ? var1 / 2.0F : 0.75F - var1 / 2.0F;
   }

   private boolean isWithinNodeProximity(Vec3 var1, Vec3 var2, float var3) {
      double var4 = Math.abs(this.controlledEntity.getX() - var2.x);
      double var6 = Math.abs(this.controlledEntity.getY() - var2.y);
      double var8 = Math.abs(this.controlledEntity.getZ() - var2.z);
      return var4 < (double)var3 && var8 < (double)var3 && var6 < 1.0D;
   }

   private boolean canSkipToNextNode(Vec3 var1) {
      return this.canCutPathCorner() && this.shouldAdvanceToNextNode(var1);
   }

   protected abstract boolean canCutPathCorner();

   private boolean shouldAdvanceToNextNode(Vec3 var1) {
      if (this.getCurrentPath().getNextNodeIndex() + 1 >= this.getCurrentPath().getNodeCount()) {
         return false;
      } else {
         Vec3 var2 = this.getCurrentPath().getNextEntityPos(this.controlledEntity);
         if (!var1.closerThan(var2, 2.0D)) {
            return false;
         } else {
            return this.hasDirectPath(var1, var2) ? true : this.isMovingTowardsNextNode(var1, var2);
         }
      }
   }

   protected abstract boolean hasDirectPath(Vec3 var1, Vec3 var2);

   private boolean isMovingTowardsNextNode(Vec3 var1, Vec3 var2) {
      Vec3 var3 = this.getCurrentPath().getEntityPosAtNode(this.controlledEntity, this.getCurrentPath().getNextNodeIndex() + 1);
      Vec3 var4 = var3.subtract(var2);
      Vec3 var5 = var1.subtract(var2);
      return var4.dot(var5) > 0.0D;
   }
}
