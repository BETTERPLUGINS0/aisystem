package advancedplugins.pm2.cv.models.v1_21_R2.entity.navigation;

import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.ai.navigation.NavigationFlying;
import net.minecraft.world.level.pathfinder.PathEntity;
import net.minecraft.world.phys.Vec3D;
import org.jetbrains.annotations.Nullable;

public class AerialNavigatorEnhanced extends NavigationFlying {
   private final AerialNavigatorEnhanced.NavigationOptimizer routeOptimizer;

   public AerialNavigatorEnhanced(EntityInsentient mob, NavigationFlying original) {
      super(var1, var1.dW());
      this.routeOptimizer = new AerialNavigatorEnhanced.NavigationOptimizer(var1);
      this.a(var2.q());
      var2.p().b(var2.p().e());
      var2.p().a(var2.p().d());
   }

   public boolean a(@Nullable PathEntity route, double velocity) {
      if (var1 != null && !var1.a(this.c)) {
         this.routeOptimizer.findOptimalStartNode(var1);
      }

      return super.a(var1, var2);
   }

   protected void l() {
      this.routeOptimizer.progressAlongRoute();
   }

   private class NavigationOptimizer {
      private final EntityInsentient entity;

      NavigationOptimizer(EntityInsentient entity) {
         this.entity = var2;
      }

      void findOptimalStartNode(PathEntity route) {
         int var2 = var1.f();
         double var3 = this.getSquaredDistanceToNode(var1.a(this.entity));

         for(int var5 = var2 + 1; var5 < var1.e(); ++var5) {
            double var6 = this.getSquaredDistanceToNode(var1.a(this.entity, var5));
            if (var6 < var3) {
               var3 = var6;
               var2 = var5;
            }
         }

         var1.c(var2);
      }

      void progressAlongRoute() {
         Vec3D var1 = AerialNavigatorEnhanced.this.b();
         float var2 = this.calculateReachThreshold();
         Vec3D var3 = AerialNavigatorEnhanced.this.c.a(this.entity);
         if (this.hasReachedNode(var1, var3, var2) || this.canAdvanceToNext(var1)) {
            AerialNavigatorEnhanced.this.c.a();
         }

         AerialNavigatorEnhanced.this.b(var1);
      }

      private double getSquaredDistanceToNode(Vec3D nodePos) {
         double var2 = var1.d - this.entity.dB();
         double var4 = var1.e - this.entity.dD();
         double var6 = var1.f - this.entity.dH();
         return var2 * var2 + var4 * var4 + var6 * var6;
      }

      private float calculateReachThreshold() {
         float var1 = this.entity.dr();
         return var1 > 0.75F ? var1 / 2.0F : 0.75F - var1 / 2.0F;
      }

      private boolean hasReachedNode(Vec3D pos, Vec3D target, float threshold) {
         double var4 = Math.abs(this.entity.dB() - var2.d);
         double var6 = Math.abs(this.entity.dD() - var2.e);
         double var8 = Math.abs(this.entity.dH() - var2.f);
         return var4 < (double)var3 && var8 < (double)var3 && var6 < 1.0D;
      }

      private boolean canAdvanceToNext(Vec3D currentPos) {
         PathEntity var2 = AerialNavigatorEnhanced.this.c;
         return AerialNavigatorEnhanced.this.b(var2.h().l) && this.shouldMoveToNextNode(var1, var2);
      }

      private boolean shouldMoveToNextNode(Vec3D pos, PathEntity route) {
         if (var2.f() + 1 >= var2.e()) {
            return false;
         } else {
            Vec3D var3 = var2.a(this.entity);
            if (!var1.a(var3, 2.0D)) {
               return false;
            } else if (AerialNavigatorEnhanced.this.a(var1, var3)) {
               return true;
            } else {
               Vec3D var4 = var2.a(this.entity, var2.f() + 1);
               Vec3D var5 = var4.d(var3);
               Vec3D var6 = var1.d(var3);
               return var5.b(var6) > 0.0D;
            }
         }
      }
   }
}
