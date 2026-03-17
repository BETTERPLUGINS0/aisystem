package advancedplugins.pm2.cv.util;

import advancedplugins.pm2.cv.api.vehicle.VehicleHitBox;
import advancedplugins.pm2.cv.util.math.BoundingBoxUtil;
import advancedplugins.pm2.cv.util.math.ConvexPolyhedralBounds;
import java.util.Iterator;
import java.util.Objects;
import me.PM2.infinitevehicles.math.geometry.euclidean.threed.Vector3D;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.Particle.DustOptions;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Consumer;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public final class ParticleUtil {
   public static void lineEffect(@NotNull Location from, @NotNull Location to, double definition, @NotNull Consumer<Location> displayer) {
      Vector var5 = var1.clone().subtract(var0).toVector().normalize();
      double var6 = 0.0D;
      double var8 = var0.distanceSquared(var1);

      while(var6 * var6 < var8) {
         var4.accept(var0.clone().add(var5.clone().multiply(var6 += var2)));
      }

   }

   public static void resdtoneLineEffect(@NotNull Location from, @NotNull Location to, double definition, float size, @NotNull Color color) {
      lineEffect(var0, var1, var2, (var3) -> {
         ((World)Objects.requireNonNull(var0.getWorld())).spawnParticle(Particle.DUST, var3, 1, 0.0D, 0.0D, 0.0D, 0.0D, new DustOptions(var5, var4));
      });
   }

   public static void displayBoundingBox(@NotNull World world, @NotNull BoundingBox boundingBox, double definition, @NotNull Consumer<Location> displayer) {
      Location var5 = BoundingBoxUtil.getCorner111(var1).toLocation(var0);
      Location var6 = BoundingBoxUtil.getCorner010(var1).toLocation(var0);
      Location var7 = BoundingBoxUtil.getCorner011(var1).toLocation(var0);
      Location var8 = BoundingBoxUtil.getCorner110(var1).toLocation(var0);
      lineEffect(var5, var7, var2, var4);
      lineEffect(var5, var8, var2, var4);
      lineEffect(var6, var7, var2, var4);
      lineEffect(var6, var8, var2, var4);
      Location var9 = BoundingBoxUtil.getCorner101(var1).toLocation(var0);
      Location var10 = BoundingBoxUtil.getCorner000(var1).toLocation(var0);
      Location var11 = BoundingBoxUtil.getCorner001(var1).toLocation(var0);
      Location var12 = BoundingBoxUtil.getCorner100(var1).toLocation(var0);
      lineEffect(var9, var11, var2, var4);
      lineEffect(var9, var12, var2, var4);
      lineEffect(var10, var11, var2, var4);
      lineEffect(var10, var12, var2, var4);
      lineEffect(var10, var6, var2, var4);
      lineEffect(var9, var5, var2, var4);
      lineEffect(var11, var7, var2, var4);
      lineEffect(var12, var8, var2, var4);
      var4.accept(var10);
      var4.accept(var5);
   }

   public static void displayHitbox(@NotNull World world, @NotNull VehicleHitBox hitbox, double definition, @NotNull Consumer<Location> displayer) {
      Location var5 = ConvertUtil.toLocation(var1.getCorner111(), var0);
      Location var6 = ConvertUtil.toLocation(var1.getCorner010(), var0);
      Location var7 = ConvertUtil.toLocation(var1.getCorner011(), var0);
      Location var8 = ConvertUtil.toLocation(var1.getCorner110(), var0);
      lineEffect(var5, var7, var2, var4);
      lineEffect(var5, var8, var2, var4);
      lineEffect(var6, var7, var2, var4);
      lineEffect(var6, var8, var2, var4);
      Location var9 = ConvertUtil.toLocation(var1.getCorner101(), var0);
      Location var10 = ConvertUtil.toLocation(var1.getCorner000(), var0);
      Location var11 = ConvertUtil.toLocation(var1.getCorner001(), var0);
      Location var12 = ConvertUtil.toLocation(var1.getCorner100(), var0);
      lineEffect(var9, var11, var2, var4);
      lineEffect(var9, var12, var2, var4);
      lineEffect(var10, var11, var2, var4);
      lineEffect(var10, var12, var2, var4);
      lineEffect(var10, var6, var2, var4);
      lineEffect(var9, var5, var2, var4);
      lineEffect(var11, var7, var2, var4);
      lineEffect(var12, var8, var2, var4);
      var4.accept(var10);
      var4.accept(var5);
   }

   public static void displayHitbox(@NotNull World world, double originX, double originY, double originZ, double hitboxWidth, double hitboxHeight, double hitboxDepth, double definition, @NotNull Consumer<Location> displayer) {
      VehicleHitBox var16 = new VehicleHitBox(var7, var9, var11);
      var16.setOrigin(var1, var3, var5);
      displayHitbox(var0, var16, var13, var15);
   }

   public static void displayBounds(@NotNull World world, @NotNull ConvexPolyhedralBounds bounds, @NotNull Consumer<Location> displayer) {
      Iterator var3 = var1.getVertices().iterator();

      while(var3.hasNext()) {
         Vector3D var4 = (Vector3D)var3.next();
         var2.accept(ConvertUtil.toLocation(var4, var0));
      }

   }

   public static void displayBounds(@NotNull World world, @NotNull BoundingBox bounds, @NotNull Consumer<Location> displayer) {
      Vector[] var3 = BoundingBoxUtil.getCorners(var1);
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Vector var6 = var3[var5];
         var2.accept(var6.toLocation(var0));
      }

   }
}
