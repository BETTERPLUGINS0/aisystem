package github.nighter.smartspawner.libs.mariadb.export;

import java.sql.DataTruncation;

public class MariaDbDataTruncation extends DataTruncation {
   private static final long serialVersionUID = 1L;
   private final String message;
   private final String sqlState;
   private final int errorCode;

   public MariaDbDataTruncation(String message, String sqlState, int errorCode, Throwable cause) {
      super(-1, false, false, -1, -1);
      this.message = message;
      this.sqlState = sqlState;
      this.errorCode = errorCode;
      if (cause != null) {
         this.initCause(cause);
      }

   }

   public String getMessage() {
      return this.message;
   }

   public String getSQLState() {
      return this.sqlState;
   }

   public int getErrorCode() {
      return this.errorCode;
   }
}
