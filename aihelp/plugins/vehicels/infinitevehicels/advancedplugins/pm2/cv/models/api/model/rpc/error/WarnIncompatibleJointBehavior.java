package advancedplugins.pm2.cv.models.api.model.rpc.error;

import advancedplugins.pm2.cv.models.api.utils.logger.LogUtil;
import lombok.Generated;

public class WarnIncompatibleJointBehavior extends IError.Warn {
   private final String jointName;
   private final String id;

   public String getErrorMessage() {
      Object[] var1 = new Object[2];
      String var2 = String.valueOf(LogUtil.LogColor.BLUE);
      var1[0] = var2 + this.id + String.valueOf(LogUtil.LogColor.YELLOW);
      var2 = String.valueOf(LogUtil.LogColor.BLUE);
      var1[1] = var2 + this.jointName + String.valueOf(LogUtil.LogColor.YELLOW);
      return String.format("Warning: Joint behavior %s on joint %s detected incompatibility with other joint behaviors. Beware of bugs and glitches.", var1);
   }

   @Generated
   public WarnIncompatibleJointBehavior(String var1, String var2) {
      this.jointName = var1;
      this.id = var2;
   }

   @Generated
   public String getJointName() {
      return this.jointName;
   }

   @Generated
   public String getId() {
      return this.id;
   }
}
