package advancedplugins.pm2.cv.models.api.model.rpc.error;

public interface IError {
   ErrorUnknownFormat UNKNOWN_FORMAT = new ErrorUnknownFormat();
   WarnBoxUV BOX_UV = new WarnBoxUV();
   WarnBadEyeHeight BAD_EYE_HEIGHT = new WarnBadEyeHeight();
   WarnNoHitbox NO_HITBOX = new WarnNoHitbox();

   String getErrorMessage();

   IError.Severity getSeverity();

   default void log(ErrorCollector collector) {
      collector.collect(this);
   }

   default void log() {
   }

   public abstract static class Warn implements IError {
      public IError.Severity getSeverity() {
         return IError.Severity.WARN;
      }
   }

   public abstract static class Error implements IError {
      public IError.Severity getSeverity() {
         return IError.Severity.ERROR;
      }
   }

   public static enum Severity {
      WARN,
      ERROR;

      private static IError.Severity[] $values() {
         return new IError.Severity[]{WARN, ERROR};
      }

      // $FF: synthetic method
      private static IError.Severity[] $values$() {
         return new IError.Severity[]{WARN, ERROR};
      }
   }
}
