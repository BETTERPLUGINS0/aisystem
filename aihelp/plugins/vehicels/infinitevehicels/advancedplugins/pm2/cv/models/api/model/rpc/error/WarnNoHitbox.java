package advancedplugins.pm2.cv.models.api.model.rpc.error;

public class WarnNoHitbox extends IError.Warn {
   public String getErrorMessage() {
      return "Warning: Missing hitbox.";
   }
}
