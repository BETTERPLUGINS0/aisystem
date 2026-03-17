package advancedplugins.pm2.cv.util;

import advancedplugins.pm2.cv.api.InfiniteVehicles;
import advancedplugins.pm2.cv.enums.EnumAxisOrder;
import advancedplugins.pm2.cv.util.math.Euler;
import java.util.concurrent.TimeUnit;
import me.PM2.infinitevehicles.math.geometry.euclidean.threed.Vector3D;
import me.PM2.infinitevehicles.math.geometry.euclidean.twod.Vector2D;
import org.bukkit.NamespacedKey;

public class Constants extends advancedplugins.pm2.cv.api.util.Constants {
   public static final Vector2D TOWARD_Z_2D = new Vector2D(0.0D, 1.0D);
   public static final long FORCE_TELEPORT_PERIOD;

   static {
      FORCE_TELEPORT_PERIOD = TimeUnit.SECONDS.toMillis(20L);
   }

   public static class Physics {
      public static final double MOMENTUM_ZERO_THRESHOLD = 0.001D;
      public static final double SAFE_COLLISION_DETECTION_GAP = 0.1D;
   }

   public static class Rotation {
      public static final Euler ARMOR_STAND_HAND_BOX_BASE_ROTATION;

      static {
         ARMOR_STAND_HAND_BOX_BASE_ROTATION = new Euler(15.0D, 45.0D, 15.0D, EnumAxisOrder.ZYX);
      }
   }

   public static class Offset {
      public static final double ARMOR_STAND_HEAD_BOX_Y_OFFSET = 1.40625D;
      public static final double SMALL_ARMOR_STAND_HEAD_BOX_Y_OFFSET = 0.703125D;
      public static final Vector3D ARMOR_STAND_RIGHT_HAND_BOX_OFFSET = new Vector3D(-0.375D, 0.5625D, 0.1953125D);
      public static final Vector3D ARMOR_STAND_LEFT_HAND_BOX_OFFSET = new Vector3D(0.375D, 0.5625D, 0.1953125D);
      public static final Vector3D SMALL_ARMOR_STAND_RIGHT_HAND_BOX_OFFSET;
      public static final Vector3D SMALL_ARMOR_STAND_LEFT_HAND_BOX_OFFSET;
      public static final double MARKER_ARMOR_STAND_NAME_BOX_Y_OFFSET = 0.3D;
      public static final double ARMOR_STAND_NAME_BOX_Y_OFFSET = 2.34375D;

      static {
         SMALL_ARMOR_STAND_RIGHT_HAND_BOX_OFFSET = ARMOR_STAND_RIGHT_HAND_BOX_OFFSET.scalarMultiply(0.5D);
         SMALL_ARMOR_STAND_LEFT_HAND_BOX_OFFSET = ARMOR_STAND_LEFT_HAND_BOX_OFFSET.scalarMultiply(0.5D);
      }
   }

   public static class NamespacedKeys extends advancedplugins.pm2.cv.api.util.Constants.NamespacedKeys {
      public static final NamespacedKey VEHICLE_UNIQUE_ID = new NamespacedKey(InfiniteVehicles.getPlugin(), "infinite-vehicle-unique-id");
      public static final NamespacedKey FUEL_ITEM_AMOUNT = new NamespacedKey(InfiniteVehicles.getPlugin(), "infinite-vehicle-fuel-item-amount");
      public static final NamespacedKey VEHICLE_FUEL_AMOUNT = new NamespacedKey(InfiniteVehicles.getPlugin(), "infinite-vehicle-fuel-amount");
      public static final NamespacedKey VEHICLE_UPGRADES_DATA = new NamespacedKey(InfiniteVehicles.getPlugin(), "infinite-vehicle-upgrades-data");
   }
}
