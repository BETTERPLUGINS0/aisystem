package advancedplugins.pm2.cv.models.api.model.rpc.error;

import advancedplugins.pm2.cv.models.api.utils.logger.LogUtil;
import lombok.Generated;

public class ErrorIncompatibleJoint extends IError.Error {
   private final boolean rendering;
   private final String jointName;
   private final String id;

   public String getErrorMessage() {
      Object[] var1 = new Object[]{this.rendering ? "ghost" : "renderer", null, null, null};
      String var2 = String.valueOf(LogUtil.LogColor.BLUE);
      var1[1] = var2 + this.id + String.valueOf(LogUtil.LogColor.RED);
      var1[2] = this.rendering ? "renderer joint" : "ghost joint";
      var2 = String.valueOf(LogUtil.LogColor.BLUE);
      var1[3] = var2 + this.jointName + String.valueOf(LogUtil.LogColor.RED);
      return String.format("Error: The %s joint action type %s is not compatible with %s %s.", var1);
   }

   @Generated
   public boolean isRendering() {
      return this.rendering;
   }

   @Generated
   public String getJointName() {
      return this.jointName;
   }

   @Generated
   public String getId() {
      return this.id;
   }

   @Generated
   public ErrorIncompatibleJoint(boolean var1, String var2, String var3) {
      this.rendering = var1;
      this.jointName = var2;
      this.id = var3;
   }
}
