package advancedplugins.pm2.cv.models.api.model.rpc.error;

import advancedplugins.pm2.cv.models.api.model.rpc.joint.behavior.JointActionType;
import advancedplugins.pm2.cv.models.api.utils.logger.LogUtil;
import lombok.Generated;

public class ErrorWrongJointBehaviorDataType extends IError.Error {
   private final String joint;
   private final JointActionType<?> jointActionType;
   private final String key;
   private final Class<?> expects;
   private final Class<?> provided;

   public String getErrorMessage() {
      Object[] var1 = new Object[5];
      String var2 = String.valueOf(LogUtil.LogColor.BLUE);
      var1[0] = var2 + this.jointActionType.getId() + String.valueOf(LogUtil.LogColor.RED);
      var2 = String.valueOf(LogUtil.LogColor.BLUE);
      var1[1] = var2 + this.joint + String.valueOf(LogUtil.LogColor.RED);
      var2 = String.valueOf(LogUtil.LogColor.BLUE);
      var1[2] = var2 + this.key + String.valueOf(LogUtil.LogColor.RED);
      var2 = String.valueOf(LogUtil.LogColor.BLUE);
      var1[3] = var2 + this.expects.getSimpleName() + String.valueOf(LogUtil.LogColor.RED);
      var2 = String.valueOf(LogUtil.LogColor.BLUE);
      var1[4] = var2 + this.provided.getSimpleName() + String.valueOf(LogUtil.LogColor.RED);
      return String.format("Error: The joint behavior %s of %s was given the wrong data type for %s. Expected %s, provided %s. Removing behavior.", var1);
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
   public Class<?> getExpects() {
      return this.expects;
   }

   @Generated
   public Class<?> getProvided() {
      return this.provided;
   }

   @Generated
   public ErrorWrongJointBehaviorDataType(String var1, JointActionType<?> var2, String var3, Class<?> var4, Class<?> var5) {
      this.joint = var1;
      this.jointActionType = var2;
      this.key = var3;
      this.expects = var4;
      this.provided = var5;
   }
}
