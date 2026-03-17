package advancedplugins.pm2.cv.models.api.model.rpc.error;

import advancedplugins.pm2.cv.models.api.utils.logger.LogUtil;
import lombok.Generated;

public class ErrorUnknownJointBehavior extends IError.Error {
   private final String jointName;
   private final String id;

   public String getErrorMessage() {
      Object[] var1 = new Object[2];
      String var2 = String.valueOf(LogUtil.LogColor.BLUE);
      var1[0] = var2 + this.id + String.valueOf(LogUtil.LogColor.RED);
      var2 = String.valueOf(LogUtil.LogColor.BLUE);
      var1[1] = var2 + this.jointName + String.valueOf(LogUtil.LogColor.RED);
      return String.format("Error: Unknown joint behavior %s on joint %s.", var1);
   }

   @Generated
   public ErrorUnknownJointBehavior(String var1, String var2) {
      this.jointName = var1;
      this.id = var2;
   }
}
