package advancedplugins.pm2.cv.api.enums;

public enum EnumInterpolationMode {
   SMOOTH,
   STEP;

   // $FF: synthetic method
   private static EnumInterpolationMode[] $values() {
      return new EnumInterpolationMode[]{SMOOTH, STEP};
   }
}
