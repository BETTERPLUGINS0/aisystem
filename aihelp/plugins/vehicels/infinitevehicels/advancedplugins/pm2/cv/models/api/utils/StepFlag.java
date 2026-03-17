package advancedplugins.pm2.cv.models.api.utils;

import advancedplugins.pm2.cv.models.api.utils.math.MathUtils;

public enum StepFlag {
   POSITION,
   ROTATION,
   SCALE;

   private static StepFlag[] $values() {
      return new StepFlag[]{POSITION, ROTATION, SCALE};
   }

   public byte setStep(byte var1, boolean var2) {
      return MathUtils.setBit(var1, this.ordinal(), var2);
   }

   public boolean isStepping(byte var1) {
      return MathUtils.getBit(var1, this.ordinal());
   }

   // $FF: synthetic method
   private static StepFlag[] $values$() {
      return new StepFlag[]{POSITION, ROTATION, SCALE};
   }
}
