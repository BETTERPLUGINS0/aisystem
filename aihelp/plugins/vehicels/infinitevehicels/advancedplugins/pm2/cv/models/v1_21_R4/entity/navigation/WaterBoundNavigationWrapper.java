package advancedplugins.pm2.cv.models.v1_21_R4.entity.navigation;

import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.ai.navigation.NavigationGuardian;
import net.minecraft.world.level.pathfinder.PathEntity;
import net.minecraft.world.phys.Vec3D;
import org.jetbrains.annotations.Nullable;

public class WaterBoundNavigationWrapper extends NavigationGuardian {
   public WaterBoundNavigationWrapper(EntityInsentient mob) {
      super(var1, var1.dV());
   }

   public boolean a(@Nullable PathEntity newPath, double speed) {
      if (var1 != null && !var1.a(this.c)) {
         int var4 = var1.f();
         double var5 = this.getNodeDistanceSquared(var1.a(this.a));

         for(int var7 = var4 + 1; var7 < var1.e(); ++var7) {
            double var8 = this.getNodeDistanceSquared(var1.a(this.a, var7));
            if (var8 < var5) {
               var5 = var8;
               var4 = var7;
            }
         }

         var1.c(var4);
      }

      return super.a(var1, var2);
   }

   private double getNodeDistanceSquared(Vec3D pos) {
      double var2 = var1.d - this.a.dA();
      double var4 = var1.e - this.a.dC();
      double var6 = var1.f - this.a.dG();
      return var2 * var2 + var4 * var4 + var6 * var6;
   }

   protected void j() {
      Vec3D var1 = this.b();
      this.l = this.a.dq() > 0.75F ? this.a.dq() / 2.0F : 0.75F - this.a.dq() / 2.0F;
      Vec3D var2 = this.c.a(this.a);
      double var3 = Math.abs(this.a.dA() - var2.d);
      double var5 = Math.abs(this.a.dC() - var2.e);
      double var7 = Math.abs(this.a.dG() - var2.f);
      boolean var9 = var3 < (double)this.l && var7 < (double)this.l && var5 < 1.0D;
      boolean var10 = var9 || this.b(this.c.h().l) && this.shouldTargetNextNodeInDirection(var1);
      if (var10) {
         this.c.a();
      }

      this.b(var1);
   }

   private boolean shouldTargetNextNodeInDirection(Vec3D mobPosition) {
      if (this.c.f() + 1 >= this.c.e()) {
         return false;
      } else {
         Vec3D var2 = this.c.a(this.a);
         if (!var1.a(var2, 2.0D)) {
            return false;
         } else if (this.a(var1, this.c.a(this.a))) {
            return true;
         } else {
            Vec3D var3 = this.c.a(this.a, this.c.f() + 1);
            Vec3D var4 = var3.d(var2);
            Vec3D var5 = var1.d(var2);
            return var4.b(var5) > 0.0D;
         }
      }
   }
}
