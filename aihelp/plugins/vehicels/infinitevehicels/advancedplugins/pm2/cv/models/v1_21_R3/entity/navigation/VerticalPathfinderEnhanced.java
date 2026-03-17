package advancedplugins.pm2.cv.models.v1_21_R3.entity.navigation;

import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.ai.navigation.NavigationSpider;
import net.minecraft.world.level.pathfinder.PathEntity;
import net.minecraft.world.phys.Vec3D;
import org.jetbrains.annotations.Nullable;

public class VerticalPathfinderEnhanced extends NavigationSpider {
   private final VerticalPathfinderEnhanced.ClimbingOptimizer climbOptimizer;

   public VerticalPathfinderEnhanced(EntityInsentient mob, NavigationSpider original) {
      super(var1, var1.dV());
      this.climbOptimizer = new VerticalPathfinderEnhanced.ClimbingOptimizer(var1);
      this.a(var2.o());
      var2.n().b(var2.n().e());
      var2.n().a(var2.n().d());
   }

   public boolean a(@Nullable PathEntity climbPath, double climbSpeed) {
      if (var1 != null && !var1.a(this.c)) {
         this.climbOptimizer.optimizeClimbRoute(var1);
      }

      return super.a(var1, var2);
   }

   protected void j() {
      this.climbOptimizer.updateClimbProgress();
   }

   private class ClimbingOptimizer {
      private final EntityInsentient climber;

      ClimbingOptimizer(EntityInsentient climber) {
         this.climber = var2;
      }

      void optimizeClimbRoute(PathEntity climbRoute) {
         int var2 = var1.f();
         double var3 = this.calculateNodeProximity(var1.a(this.climber));

         for(int var5 = var2 + 1; var5 < var1.e(); ++var5) {
            double var6 = this.calculateNodeProximity(var1.a(this.climber, var5));
            if (var6 < var3) {
               var3 = var6;
               var2 = var5;
            }
         }

         var1.c(var2);
      }

      void updateClimbProgress() {
         Vec3D var1 = VerticalPathfinderEnhanced.this.b();
         float var2 = this.computeArrivalThreshold();
         Vec3D var3 = VerticalPathfinderEnhanced.this.c.a(this.climber);
         if (this.hasArrivedAtPoint(var1, var3, var2) || this.canSkipToNextPoint(var1)) {
            VerticalPathfinderEnhanced.this.c.a();
         }

         VerticalPathfinderEnhanced.this.b(var1);
      }

      private double calculateNodeProximity(Vec3D nodeCoordinate) {
         double var2 = var1.d - this.climber.dA();
         double var4 = var1.e - this.climber.dC();
         double var6 = var1.f - this.climber.dG();
         return var2 * var2 + var4 * var4 + var6 * var6;
      }

      private float computeArrivalThreshold() {
         float var1 = this.climber.dq();
         return var1 > 0.75F ? var1 / 2.0F : 0.75F - var1 / 2.0F;
      }

      private boolean hasArrivedAtPoint(Vec3D location, Vec3D target, float threshold) {
         double var4 = Math.abs(this.climber.dA() - var2.d);
         double var6 = Math.abs(this.climber.dC() - var2.e);
         double var8 = Math.abs(this.climber.dG() - var2.f);
         return var4 < (double)var3 && var8 < (double)var3 && var6 < 1.0D;
      }

      private boolean canSkipToNextPoint(Vec3D location) {
         PathEntity var2 = VerticalPathfinderEnhanced.this.c;
         return VerticalPathfinderEnhanced.this.b(var2.h().l) && this.shouldProgressToNext(var1, var2);
      }

      private boolean shouldProgressToNext(Vec3D spot, PathEntity route) {
         if (var2.f() + 1 >= var2.e()) {
            return false;
         } else {
            Vec3D var3 = var2.a(this.climber);
            if (!var1.a(var3, 2.0D)) {
               return false;
            } else if (VerticalPathfinderEnhanced.this.a(var1, var3)) {
               return true;
            } else {
               Vec3D var4 = var2.a(this.climber, var2.f() + 1);
               Vec3D var5 = var4.d(var3);
               Vec3D var6 = var1.d(var3);
               return var5.b(var6) > 0.0D;
            }
         }
      }
   }
}
