package advancedplugins.pm2.cv.models.api.model.rpc.error;

public class WarnBadEyeHeight extends IError.Warn {
   public String getErrorMessage() {
      return "Warning: Eye height is below 0. Entity might suffocate.";
   }
}
