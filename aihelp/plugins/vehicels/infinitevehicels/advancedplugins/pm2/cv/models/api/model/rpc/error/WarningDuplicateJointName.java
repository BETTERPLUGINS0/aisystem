package advancedplugins.pm2.cv.models.api.model.rpc.error;

import advancedplugins.pm2.cv.models.api.utils.logger.LogUtil;
import java.util.UUID;
import lombok.Generated;

public class WarningDuplicateJointName extends IError.Warn {
   private final String joint;
   private final UUID uuid;

   public String getErrorMessage() {
      Object[] var1 = new Object[2];
      String var2 = String.valueOf(LogUtil.LogColor.BLUE);
      var1[0] = var2 + this.joint + String.valueOf(LogUtil.LogColor.YELLOW);
      var2 = String.valueOf(LogUtil.LogColor.BLUE);
      var1[1] = var2 + this.uuid.toString() + String.valueOf(LogUtil.LogColor.YELLOW);
      return String.format("Warning: Model contains duplicate joint names %s. Naming joint with UUID %s", var1);
   }

   @Generated
   public WarningDuplicateJointName(String var1, UUID var2) {
      this.joint = var1;
      this.uuid = var2;
   }

   @Generated
   public String getJoint() {
      return this.joint;
   }

   @Generated
   public UUID getUuid() {
      return this.uuid;
   }
}
