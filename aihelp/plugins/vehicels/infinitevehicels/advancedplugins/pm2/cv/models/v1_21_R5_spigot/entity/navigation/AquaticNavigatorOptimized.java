package advancedplugins.pm2.cv.models.v1_21_R5_spigot.entity.navigation;

import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.ai.navigation.NavigationGuardian;
import net.minecraft.world.level.pathfinder.PathEntity;
import net.minecraft.world.phys.Vec3D;
import org.jetbrains.annotations.Nullable;

public class AquaticNavigatorOptimized extends NavigationGuardian {
   private final AquaticNavigatorOptimized.SwimPathProcessor swimProcessor;

   public AquaticNavigatorOptimized(EntityInsentient var1) {
      super(var1, var1.ai());
      this.swimProcessor = new AquaticNavigatorOptimized.SwimPathProcessor(var1);
   }

   public boolean a(@Nullable PathEntity var1, double var2) {
      if (var1 != null && !var1.a(this.c)) {
         this.swimProcessor.optimizeSwimPath(var1);
      }

      return super.a(var1, var2);
   }

   protected void k() {
      this.swimProcessor.progressThroughWater();
   }

   private class SwimPathProcessor {
      private final EntityInsentient swimmer;

      SwimPathProcessor(EntityInsentient param2) {
         this.swimmer = var2;
      }

      void optimizeSwimPath(PathEntity var1) {
         int var2 = var1.f();
         double var3 = this.measureDistanceToPoint(var1.a(this.swimmer));

         for(int var5 = var2 + 1; var5 < var1.e(); ++var5) {
            double var6 = this.measureDistanceToPoint(var1.a(this.swimmer, var5));
            if (var6 < var3) {
               var3 = var6;
               var2 = var5;
            }
         }

         var1.c(var2);
      }

      void progressThroughWater() {
         Vec3D var1 = AquaticNavigatorOptimized.this.b();
         float var2 = this.calculateSwimReach();
         Vec3D var3 = AquaticNavigatorOptimized.this.c.a(this.swimmer);
         if (this.isWithinReach(var1, var3, var2) || this.canMoveToNextPoint(var1)) {
            AquaticNavigatorOptimized.this.c.a();
         }

         AquaticNavigatorOptimized.this.b(var1);
      }

      private double measureDistanceToPoint(Vec3D var1) {
         double var2 = var1.d - this.swimmer.dC();
         double var4 = var1.e - this.swimmer.dE();
         double var6 = var1.f - this.swimmer.dI();
         return var2 * var2 + var4 * var4 + var6 * var6;
      }

      private float calculateSwimReach() {
         float var1 = this.swimmer.ds();
         return var1 > 0.75F ? var1 / 2.0F : 0.75F - var1 / 2.0F;
      }

      private boolean isWithinReach(Vec3D var1, Vec3D var2, float var3) {
         double var4 = Math.abs(this.swimmer.dC() - var2.d);
         double var6 = Math.abs(this.swimmer.dE() - var2.e);
         double var8 = Math.abs(this.swimmer.dI() - var2.f);
         return var4 < (double)var3 && var8 < (double)var3 && var6 < 1.0D;
      }

      private boolean canMoveToNextPoint(Vec3D var1) {
         PathEntity var2 = AquaticNavigatorOptimized.this.c;
         return AquaticNavigatorOptimized.this.b(var2.h().l) && this.shouldSwimToNext(var1, var2);
      }

      private boolean shouldSwimToNext(Vec3D var1, PathEntity var2) {
         if (var2.f() + 1 >= var2.e()) {
            return false;
         } else {
            Vec3D var3 = var2.a(this.swimmer);
            if (!var1.a(var3, 2.0D)) {
               return false;
            } else if (AquaticNavigatorOptimized.this.a(var1, var3)) {
               return true;
            } else {
               Vec3D var4 = var2.a(this.swimmer, var2.f() + 1);
               Vec3D var5 = var4.d(var3);
               Vec3D var6 = var1.d(var3);
               return var5.b(var6) > 0.0D;
            }
         }
      }
   }
}
