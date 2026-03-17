package advancedplugins.pm2.cv.models.api.model.rpc.error;

import advancedplugins.pm2.cv.models.api.utils.data.ResourceLocation;
import advancedplugins.pm2.cv.models.api.utils.logger.LogUtil;
import lombok.Generated;

public class WarnBadTexture extends IError.Warn {
   private final ResourceLocation name;
   private final ResourceLocation correct;

   public String getErrorMessage() {
      Object[] var1 = new Object[2];
      String var2 = String.valueOf(LogUtil.LogColor.BLUE);
      var1[0] = var2 + this.name.toString() + String.valueOf(LogUtil.LogColor.YELLOW);
      var2 = String.valueOf(LogUtil.LogColor.BLUE);
      var1[1] = var2 + this.correct.toString() + String.valueOf(LogUtil.LogColor.YELLOW);
      return String.format("Warn: Texture name %s contains non [a-z0-9/._-] characters. Using alternative texture name %s.", var1);
   }

   @Generated
   public WarnBadTexture(ResourceLocation var1, ResourceLocation var2) {
      this.name = var1;
      this.correct = var2;
   }
}
