package advancedplugins.pm2.cv.models.api.model.rpc.error;

public class WarnBoxUV extends IError.Warn {
   public String getErrorMessage() {
      return "Warning: Box UV detected. Cube UVs might not generate correctly if Box UV is used.";
   }
}
