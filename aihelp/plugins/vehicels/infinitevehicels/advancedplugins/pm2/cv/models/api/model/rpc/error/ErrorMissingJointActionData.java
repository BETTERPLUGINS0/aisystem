package advancedplugins.pm2.cv.models.api.model.rpc.error;

import advancedplugins.pm2.cv.models.api.model.rpc.joint.behavior.JointActionType;
import advancedplugins.pm2.cv.models.api.utils.logger.LogUtil;
import lombok.Generated;

public class ErrorMissingJointActionData extends IError.Error {
   private final String joint;
   private final JointActionType<?> jointActionType;
   private final String key;

   public String getErrorMessage() {
      Object[] var1 = new Object[3];
      String var2 = String.valueOf(LogUtil.LogColor.BLUE);
      var1[0] = var2 + this.jointActionType.getId() + String.valueOf(LogUtil.LogColor.RED);
      var2 = String.valueOf(LogUtil.LogColor.BLUE);
      var1[1] = var2 + this.joint + String.valueOf(LogUtil.LogColor.RED);
      var2 = String.valueOf(LogUtil.LogColor.BLUE);
      var1[2] = var2 + this.key + String.valueOf(LogUtil.LogColor.RED);
      return String.format("Error: The joint action data %s of %s is missing required data %s. Removing joint action.", var1);
   }

   @Generated
   public String getJoint() {
      return this.joint;
   }

   @Generated
   public JointActionType<?> getJointActionType() {
      return this.jointActionType;
   }

   @Generated
   public String getKey() {
      return this.key;
   }

   @Generated
   public ErrorMissingJointActionData(String var1, JointActionType<?> var2, String var3) {
      this.joint = var1;
      this.jointActionType = var2;
      this.key = var3;
   }
}
