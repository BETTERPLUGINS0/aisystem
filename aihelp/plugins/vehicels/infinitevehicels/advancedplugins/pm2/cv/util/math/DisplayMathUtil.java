package advancedplugins.pm2.cv.util.math;

import advancedplugins.pm2.cv.api.util.inventory.ItemStackUtil;
import advancedplugins.pm2.cv.enums.EnumDisplayEntity;
import advancedplugins.pm2.cv.util.ConvertUtil;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import me.PM2.infinitevehicles.math.geometry.euclidean.threed.Vector3D;
import org.bukkit.Material;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public final class DisplayMathUtil {
   public static ConvexPolyhedralBounds getDisplayEntityBounds(@NotNull EnumDisplayEntity kind, @Nullable Material material, @NotNull Vector3D location, @NotNull Matrix4f transformation) {
      ConvexPolyhedralBounds var4 = new ConvexPolyhedralBounds();
      BoundingBox var5 = getBaseDisplayEntityBounds(var0, var1, var2);
      List var6 = (List)Arrays.stream(BoundingBoxUtil.getCorners(var5)).map(ConvertUtil::toVector3D).collect(Collectors.toList());
      Iterator var7 = var6.iterator();

      while(var7.hasNext()) {
         Vector3D var8 = (Vector3D)var7.next();
         var4.addVertex(var2.add(transform(var8.subtract(var2), var3)));
      }

      return var4;
   }

   public static BoundingBox getBaseDisplayEntityBounds(@NotNull EnumDisplayEntity kind, @Nullable Material material, @NotNull Vector3D location) {
      if (var0 == EnumDisplayEntity.BLOCK) {
         return new BoundingBox(var2.getX(), var2.getY(), var2.getZ(), var2.getX() + 1.0D, var2.getY() + 1.0D, var2.getZ() + 1.0D);
      } else {
         return var1 != null && ItemStackUtil.isBanner(var1) ? new BoundingBox(var2.getX() - 0.5D, var2.getY(), var2.getZ() - 0.35D, var2.getX() + 0.5D, var2.getY() + 3.0D, var2.getZ() + 0.0D) : new BoundingBox(var2.getX() - 0.5D, var2.getY() - 0.5D, var2.getZ() - 0.5D, var2.getX() + 0.5D, var2.getY() + 0.5D, var2.getZ() + 0.5D);
      }
   }

   public static Vector3f migrate(Vector3D vector3D) {
      return new Vector3f((float)var0.getX(), (float)var0.getY(), (float)var0.getZ());
   }

   public static Vector3D migrate(Vector3f vector3f) {
      return var0 == null ? new Vector3D(0.0D, 0.0D, 0.0D) : new Vector3D((double)var0.x, (double)var0.y, (double)var0.z);
   }

   public static Vector3D transform(Vector3D vector, Matrix4f matrix) {
      return migrate(migrate(var0).mulProject(var1));
   }
}
