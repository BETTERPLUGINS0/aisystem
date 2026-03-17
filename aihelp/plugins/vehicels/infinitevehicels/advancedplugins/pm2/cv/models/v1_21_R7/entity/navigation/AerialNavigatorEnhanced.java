package advancedplugins.pm2.cv.models.v1_21_R7.entity.navigation;

import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.ai.navigation.NavigationFlying;
import net.minecraft.world.level.pathfinder.PathEntity;
import net.minecraft.world.phys.Vec3D;
import org.jetbrains.annotations.Nullable;

public class AerialNavigatorEnhanced extends NavigationFlying {
   private final AerialNavigatorEnhanced.NavigationOptimizer routeOptimizer;

   public AerialNavigatorEnhanced(EntityInsentient var1, NavigationFlying var2) {
      super(var1, var1.an());
      this.routeOptimizer = new AerialNavigatorEnhanced.NavigationOptimizer(var1);
      this.a(var2.p());
      var2.o().b(var2.o().e());
      var2.o().a(var2.o().d());
   }

   public boolean a(@Nullable PathEntity var1, double var2) {
      if (var1 != null && !var1.a(this.c)) {
         this.routeOptimizer.findOptimalStartNode(var1);
      }

      return super.a(var1, var2);
   }

   protected void k() {
      this.routeOptimizer.progressAlongRoute();
   }

   private class NavigationOptimizer {
      private final EntityInsentient entity;

      NavigationOptimizer(EntityInsentient param2) {
         this.entity = var2;
      }

      void findOptimalStartNode(PathEntity var1) {
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

      private double getSquaredDistanceToNode(Vec3D var1) {
         double var2 = var1.g - this.entity.dK();
         double var4 = var1.h - this.entity.dM();
         double var6 = var1.i - this.entity.dQ();
         return var2 * var2 + var4 * var4 + var6 * var6;
      }

      private float calculateReachThreshold() {
         float var1 = this.entity.dA();
         return var1 > 0.75F ? var1 / 2.0F : 0.75F - var1 / 2.0F;
      }

      private boolean hasReachedNode(Vec3D var1, Vec3D var2, float var3) {
         double var4 = Math.abs(this.entity.dK() - var2.g);
         double var6 = Math.abs(this.entity.dM() - var2.h);
         double var8 = Math.abs(this.entity.dQ() - var2.i);
         return var4 < (double)var3 && var8 < (double)var3 && var6 < 1.0D;
      }

      private boolean canAdvanceToNext(Vec3D var1) {
         PathEntity var2 = AerialNavigatorEnhanced.this.c;
         return AerialNavigatorEnhanced.this.b(var2.h().l) && this.shouldMoveToNextNode(var1, var2);
      }

      private boolean shouldMoveToNextNode(Vec3D var1, PathEntity var2) {
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
