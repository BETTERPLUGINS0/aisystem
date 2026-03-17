package advancedplugins.pm2.cv.models.v1_21_R3.entity.navigation;

import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.ai.navigation.Navigation;
import net.minecraft.world.level.pathfinder.PathEntity;
import net.minecraft.world.phys.Vec3D;
import org.jetbrains.annotations.Nullable;

public class TerrainNavigatorOptimized extends Navigation {
   private final TerrainNavigatorOptimized.RouteProcessor routeProcessor;

   public TerrainNavigatorOptimized(EntityInsentient mob, Navigation original) {
      super(var1, var1.dV());
      this.routeProcessor = new TerrainNavigatorOptimized.RouteProcessor(var1);
      this.a(var2.o());
      var2.n().b(var2.n().e());
      var2.n().a(var2.n().d());
   }

   public boolean a(@Nullable PathEntity trajectory, double pace) {
      if (var1 != null && !var1.a(this.c)) {
         this.routeProcessor.calibrateStartingPoint(var1);
      }

      return super.a(var1, var2);
   }

   protected void j() {
      this.routeProcessor.advancePosition();
   }

   private class RouteProcessor {
      private final EntityInsentient navigator;

      RouteProcessor(EntityInsentient navigator) {
         this.navigator = var2;
      }

      void calibrateStartingPoint(PathEntity trajectory) {
         int var2 = var1.f();
         double var3 = this.computeNodeDistance(var1.a(this.navigator));

         for(int var5 = var2 + 1; var5 < var1.e(); ++var5) {
            double var6 = this.computeNodeDistance(var1.a(this.navigator, var5));
            if (var6 < var3) {
               var3 = var6;
               var2 = var5;
            }
         }

         var1.c(var2);
      }

      void advancePosition() {
         Vec3D var1 = TerrainNavigatorOptimized.this.b();
         float var2 = this.determineReachRadius();
         Vec3D var3 = TerrainNavigatorOptimized.this.c.a(this.navigator);
         if (this.isAtDestination(var1, var3, var2) || this.canProgressToFollowing(var1)) {
            TerrainNavigatorOptimized.this.c.a();
         }

         TerrainNavigatorOptimized.this.b(var1);
      }

      private double computeNodeDistance(Vec3D nodeLocation) {
         double var2 = var1.d - this.navigator.dA();
         double var4 = var1.e - this.navigator.dC();
         double var6 = var1.f - this.navigator.dG();
         return var2 * var2 + var4 * var4 + var6 * var6;
      }

      private float determineReachRadius() {
         float var1 = this.navigator.dq();
         return var1 > 0.75F ? var1 / 2.0F : 0.75F - var1 / 2.0F;
      }

      private boolean isAtDestination(Vec3D position, Vec3D destination, float radius) {
         double var4 = Math.abs(this.navigator.dA() - var2.d);
         double var6 = Math.abs(this.navigator.dC() - var2.e);
         double var8 = Math.abs(this.navigator.dG() - var2.f);
         return var4 < (double)var3 && var8 < (double)var3 && var6 < 1.0D;
      }

      private boolean canProgressToFollowing(Vec3D position) {
         PathEntity var2 = TerrainNavigatorOptimized.this.c;
         return TerrainNavigatorOptimized.this.b(var2.h().l) && this.shouldTransitionToNext(var1, var2);
      }

      private boolean shouldTransitionToNext(Vec3D location, PathEntity pathway) {
         if (var2.f() + 1 >= var2.e()) {
            return false;
         } else {
            Vec3D var3 = var2.a(this.navigator);
            if (!var1.a(var3, 2.0D)) {
               return false;
            } else if (TerrainNavigatorOptimized.this.a(var1, var3)) {
               return true;
            } else {
               Vec3D var4 = var2.a(this.navigator, var2.f() + 1);
               Vec3D var5 = var4.d(var3);
               Vec3D var6 = var1.d(var3);
               return var5.b(var6) > 0.0D;
            }
         }
      }
   }
}
