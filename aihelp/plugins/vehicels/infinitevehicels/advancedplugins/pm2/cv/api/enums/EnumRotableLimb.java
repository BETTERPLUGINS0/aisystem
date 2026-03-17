package advancedplugins.pm2.cv.api.enums;

import advancedplugins.pm2.cv.api.util.Constants;
import me.PM2.infinitevehicles.math.geometry.euclidean.threed.Vector3D;

public enum EnumRotableLimb {
   HEAD {
      public Vector3D getDefaultRotation() {
         return Vector3D.ZERO;
      }

      public Vector3D getRotationPivot(boolean var1) {
         return var1 ? Constants.Pivot.SMALL_ARMOR_STAND_HEAD_ROTATION_PIVOT : Constants.Pivot.ARMOR_STAND_HEAD_ROTATION_PIVOT;
      }
   },
   LEFT_ARM {
      public Vector3D getDefaultRotation() {
         return new Vector3D(-10.0D, 0.0D, -10.0D);
      }

      public Vector3D getRotationPivot(boolean var1) {
         return var1 ? Constants.Pivot.SMALL_ARMOR_STAND_LEFT_ARM_ROTATION_PIVOT : Constants.Pivot.ARMOR_STAND_LEFT_ARM_ROTATION_PIVOT;
      }
   },
   RIGHT_ARM {
      public Vector3D getDefaultRotation() {
         return new Vector3D(-15.0D, 0.0D, 10.0D);
      }

      public Vector3D getRotationPivot(boolean var1) {
         return var1 ? Constants.Pivot.SMALL_ARMOR_STAND_RIGHT_ARM_ROTATION_PIVOT : Constants.Pivot.ARMOR_STAND_RIGHT_ARM_ROTATION_PIVOT;
      }
   };

   public abstract Vector3D getDefaultRotation();

   public abstract Vector3D getRotationPivot(boolean var1);

   public EnumStandSlot toStandSlot() {
      switch(this.ordinal()) {
      case 0:
         return EnumStandSlot.HEAD;
      case 1:
         return EnumStandSlot.LEFT_HAND;
      case 2:
         return EnumStandSlot.RIGHT_HAND;
      default:
         throw new IllegalStateException();
      }
   }

   // $FF: synthetic method
   private static EnumRotableLimb[] $values() {
      return new EnumRotableLimb[]{HEAD, LEFT_ARM, RIGHT_ARM};
   }
}
