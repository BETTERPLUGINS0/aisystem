package advancedplugins.pm2.cv.enums;

public enum EnumAxisOrder {
   XYZ,
   YXZ,
   ZXY,
   ZYX,
   YZX,
   XZY;

   // $FF: synthetic method
   private static EnumAxisOrder[] $values() {
      return new EnumAxisOrder[]{XYZ, YXZ, ZXY, ZYX, YZX, XZY};
   }
}
